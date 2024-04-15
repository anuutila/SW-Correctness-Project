package com.example.scala

import com.example.scala.CommandType.CommandType

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.jdk.CollectionConverters._
import com.example.scala

import _root_.scala.collection.mutable

class GraphicsLanguageInterpreter(graphicsProgram: String, canvasInfo: CanvasInformation) {

  // Canvas properties
  private val gs: Int = canvasInfo.gridSpacing
  private val ch: Int = canvasInfo.canvasHeight

  // Bounding box pixel values:
  private var bb_lower_left = new Pixel(0,ch)
  private var bb_upper_right = new Pixel(26*gs, ch-(16*gs))

  /**
   * Interprets the commands and returns a list with results of all the commands.
   */
  def interpretProgram(): java.util.List[CommandResult] = {
    // Split the program into individual lines
    val graphicsCommands: Array[String] = graphicsProgram.split("\n")
    val results = ListBuffer.empty[CommandResult]

    for (line <- graphicsCommands) {
      parseCommand(line.trim) match {
        case Some(command) =>
          val result = command.execute()

          // NOTE: This is a bit ugly with the conversions. Could be be changed slightly
          if(result.commandType == CommandType.BOUNDING_BOX) {
            // List only has two pixels. Lower left first and top right after.
            val new_bb_values = result.pixels.asScala
            bb_lower_left = new_bb_values(0)
            bb_upper_right = new_bb_values(1)
          }
          else {
            // Filter result based on Bounding box values:
            val pixels_scala = result.pixels.asScala
            result.pixels = filterBoundingBox(pixels_scala).asJava
          }

          results += result
          command match {
            // A DRAW command can have multiple nested commands
            case multiResultCommand: MultiResultCommand =>
              val multiResults: List[CommandResult] = multiResultCommand.executeNestedCommands()
              for (res <- multiResults) {
                res.pixels = filterBoundingBox(res.pixels.asScala).asJava
              }
              results ++= multiResults
            case _ => // Do nothing if it's not a MultiResultCommand
          }
        case None =>
          // If the command is invalid, add an empty list of pixels and an error message to the results
          val errorMessage = new Message(s"Invalid command: $line", MessageType.ERROR)
          val result = CommandResult(new java.util.ArrayList[Pixel](), errorMessage, CommandType.INVALID)
          results += result
      }
    }
    return results.asJava
  }

  // Regular expressions for matching the different types of valid commands
  private val linePattern: Regex = """\(LINE \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\) \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\)\)""".r
  private val rectanglePattern: Regex = """\(RECTANGLE \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\) \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\)\)""".r
  private val circlePattern: Regex = """\(CIRCLE \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\) (\d+(?:\.\d+)?)\)""".r
  private val textAtPattern: Regex = """\(TEXT-AT \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\) (.+)\)""".r
  private val fillRectanglePattern: Regex = """\(FILL (#(?:[0-9a-fA-F]{3}){1,2}) \(RECTANGLE \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\) \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\)\)\)""".r
  private val fillCirclePattern: Regex = """\(FILL (#(?:[0-9a-fA-F]{3}){1,2}) \(CIRCLE \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\) (\d+(?:\.\d+)?)\)\)""".r
  private val boundingBoxPattern: Regex = """\(BOUNDING-BOX \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\) \((\d+(?:\.\d+)?) (\d+(?:\.\d+)?)\)\)""".r
  private val drawPattern: Regex = """\(DRAW (#(?:[0-9a-fA-F]{3}){1,2}) (.+)\)""".r

  /**
   * Parse a single line of the program and return the corresponding Command object
   */
  private def parseCommand(commandString: String): Option[Command] = {
    commandString.trim match {
      // Match the commands and return a valid command instance if match found
      case linePattern(x0, y0, x1, y1) => Some(LineCommand(x0.toDouble, y0.toDouble, x1.toDouble, y1.toDouble, gs, ch))
      case rectanglePattern(x0, y0, x1, y1) => Some(RectangleCommand(x0.toDouble, y0.toDouble, x1.toDouble, y1.toDouble, gs, ch))
      case circlePattern(x, y, r) => Some(CircleCommand(x.toDouble, y.toDouble, r.toDouble, gs, ch))
      case textAtPattern(x, y, text) => Some(TextAtCommand(x.toDouble, y.toDouble, text, gs, ch))
      case fillCirclePattern(c,x,y,r) => Some(FillCircleCommand(x.toDouble, y.toDouble, r.toDouble, c, gs, ch))
      case fillRectanglePattern(c, x0, y0, x1, y1) => Some(FillRectangleCommand(c, x0.toDouble, y0.toDouble, x1.toDouble, y1.toDouble, gs, ch))
      case boundingBoxPattern(x0,y0,x1,y1) => Some(BoundingBoxCommand(x0.toDouble, y0.toDouble, x1.toDouble, y1.toDouble, gs, ch))
      case drawPattern(coloring, commandStrings) =>
        // Find all the individual nested commands from inside the DRAW command
        val nestedCommands = "(\\([A-Z].+?(?=\\s\\([A-Z]|$))".r.findAllIn(commandStrings)
        // Call parseCommand recursively for all the found nested commands
        val validCommands = nestedCommands.flatMap(parseCommand).toList
        Some(DrawCommand(coloring, validCommands))
      case _ => None // Return None if the command string doesn't match any pattern
    }
  }

  // Filter based on bounding box values:
   private def filterBoundingBox(commandResult: mutable.Buffer[Pixel]): List[Pixel] = {
     // Because of how the coordinates work. The Y conditions are inverted.
    val filtered_result = commandResult.filter(p => (bb_lower_left.Get_Y() > p.Get_Y() && p.Get_Y() > bb_upper_right.Get_Y() && bb_lower_left.Get_X() < p.Get_X() &&  p.Get_X() < bb_upper_right.Get_X() ))

    return filtered_result.toList
  }


}

case class CanvasInformation(gridSpacing: Int, canvasHeight: Int)

sealed trait Command {
  var color: Option[String] = None;
  def formatVar(x: Double): String = {
    if (x == x.toInt) x.toInt.toString else x.toString
  }
  def  execute(): CommandResult  // Returns the generated pixels and optional info/warning/error message
}

case class CommandResult(
                          var pixels: java.util.List[Pixel],
                          message: Message,
                          commandType: CommandType,
                          color: String = null, // For the Draw command, null by default
                          text: Option[String] = None // For the TextAt command, None by default
                        )

object CommandType extends Enumeration {
  type CommandType = Value
  val BOUNDING_BOX, LINE, RECTANGLE, CIRCLE, TEXT_AT, DRAW, FILL, INVALID = Value
}

case class BoundingBoxCommand(x0: Double, y0: Double, x1: Double, y1: Double, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {

    // Find the biggest and smallest values of x and y:
    val smallest_x = if(x0 < x1) x0 else x1
    val biggest_x = if (x0 > x1) x0 else x1

    val smallest_y = if(y0 < y1) y0 else y1
    val biggest_y = if (y0 > y1) y0 else y1


    // gs is canvas grid spacing and ch is canvas height
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = List(new Pixel((smallest_x*gs).toInt,(ch-(smallest_y*gs)).toInt), new Pixel((biggest_x*gs).toInt,(ch-(biggest_y*gs)).toInt))

    return CommandResult(
      pixels.asJava,
      new Message(s"Bounding box set from [${formatVar(x0)}, ${formatVar(y0)}] to [${formatVar(x1)}, ${formatVar(y1)}]", MessageType.INFO),
      CommandType.BOUNDING_BOX
    )
  }
}

case class LineCommand(x0: Double, y0: Double, x1: Double, y1: Double, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // gs is canvas grid spacing and ch is canvas height
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = new CustomLine().drawLine((x0*gs).toInt, (ch-(y0*gs)).toInt, (x1*gs).toInt, (ch-(y1*gs)).toInt)
    return CommandResult(
      pixels.asJava,
      new Message(s"Line drawn from [${formatVar(x0)}, ${formatVar(y0)}] to [${formatVar(x1)}, ${formatVar(y1)}]", MessageType.INFO),
      CommandType.LINE,
      color.orNull
    )
  }
}

case class RectangleCommand(x0: Double, y0: Double, x1: Double, y1: Double, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // gs is canvas grid spacing and ch is canvas height
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = new CustomLine().drawRect((x0*gs).toInt, (ch-(y0*gs)).toInt, (x1*gs).toInt, (ch-(y1*gs)).toInt)
    return CommandResult(
      pixels.asJava,
      new Message(s"Rectangle drawn between [${formatVar(x0)}, ${formatVar(y0)}] and [${formatVar(x1)}, ${formatVar(y1)}]", MessageType.INFO),
      CommandType.RECTANGLE,
      color.orNull
    )
  }
}

case class CircleCommand(x: Double, y: Double, r: Double, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // gs is canvas grid spacing and ch is canvas height
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = new MidpointCircle().midpoint_circle((x*gs).toInt, (ch-(y*gs)).toInt, (r*gs).toInt)
    return CommandResult(
      pixels.asJava,
      new Message(s"Circle drawn at [${formatVar(x)}, ${formatVar(y)}] with radius of ${formatVar(r)}", MessageType.INFO),
      CommandType.CIRCLE,
      color.orNull
    )
  }
}

case class TextAtCommand(x: Double, y: Double, text: String, gs: Int, ch: Int) extends Command {

  override def execute(): CommandResult = {
    // gs is canvas grid spacing and ch is canvas height
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    var pixels = List(new Pixel((x*gs).toInt, (ch-(y*gs)).toInt))
    return CommandResult(
      pixels.asJava,
      new Message(s"Text '$text' drawn at [${formatVar(x)}, ${formatVar(y)}]", MessageType.INFO),
      CommandType.TEXT_AT,
      color.orNull,
      Some(text)
    )
  }
}

case class FillCircleCommand(x: Double, y: Double, r: Double, colour: String, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // Call Circle Command - if we want black border

    // Fill Circle
    val pixels = new MidpointCircle().fill_circle((x*gs).toInt, (ch-(y*gs)).toInt, (r*gs).toInt, colour)
    return CommandResult(
      pixels.asJava,
      new Message(s"Filled circle ($colour) drawn at [${formatVar(x)}, ${formatVar(y)}] with radius of ${formatVar(r)}", MessageType.INFO),
      CommandType.FILL
    )
  }
}

case class FillRectangleCommand(colour:String, x0: Double, y0: Double, x1: Double, y1: Double, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // gs is canvas grid spacing and ch is canvas height
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = new CustomLine().fillRect((x0*gs).toInt, (ch-(y0*gs)).toInt, (x1*gs).toInt, (ch-(y1*gs)).toInt, colour)
    return CommandResult(
      pixels.asJava,
      new Message(s"Filled rectangle ($colour) drawn between [${formatVar(x0)}, ${formatVar(y0)}] and [${formatVar(x1)}, ${formatVar(y1)}]", MessageType.INFO),
      CommandType.FILL
    )
  }
}

sealed trait MultiResultCommand extends Command {
  def executeNestedCommands(): List[CommandResult]
}

case class DrawCommand(coloring: String, commands: List[Command]) extends MultiResultCommand {

  // Execute each individual command and collect and return the results in a list
  override def executeNestedCommands(): List[CommandResult] = {
    commands.foreach(_.color = Some(coloring))
    return commands.map(_.execute())
  }

  override def execute(): CommandResult = {
    return CommandResult(
      new java.util.ArrayList[Pixel](),
      new Message("Draw next " + commands.size + " command(s) with color " + coloring, MessageType.INFO),
      CommandType.DRAW
    )
  }
}