package com.example.scala

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.jdk.CollectionConverters._

class GraphicsLanguageInterpreter(graphicsProgram: String, canvasInfo: CanvasInformation) {

  // Canvas properties
  val gs: Int = canvasInfo.gridSpacing
  val ch: Int = canvasInfo.canvasHeight

  def interpretProgram(): java.util.List[CommandResult] = {
    // Split the graphicsCommands string into individual lines
    val graphicsCommands: Array[String] = graphicsProgram.split("\n")
    val results = ListBuffer.empty[CommandResult]

    for (line <- graphicsCommands) {
      parseCommand(line.trim) match {
        case Some(command) =>
          val result = command.execute()
          results += result
        case None =>
          // If the command is invalid, add an empty list of pixels and an error message to the results
          val errorMessage = new Message(s"Invalid command: $line", MessageType.ERROR)
          val result = CommandResult(new java.util.ArrayList[Pixel](), errorMessage)
          results += result
      }
    }
    return results.asJava
  }

  // Regular expressions for matching different types of commands
  private val linePattern: Regex = """\(LINE \((\d+) (\d+)\) \((\d+) (\d+)\)\)""".r
  private val rectanglePattern: Regex = """\(RECTANGLE \((\d+) (\d+)\) \((\d+) (\d+)\)\)""".r
  private val circlePattern: Regex = """\(CIRCLE \((\d+) (\d+)\) (\d+)\)""".r
  private val fillRectanglePattern: Regex = """\(FILL (#(?:[0-9a-fA-F]{3}){1,2}) \(RECTANGLE \((\d+) (\d+)\) \((\d+) (\d+)\)\)\)""".r



  // Other patterns...

  // Parse a single command string and return the corresponding Command object
  private def parseCommand(commandString: String): Option[Command] = {
    commandString.trim match {
      // Match the commands and return a valid command instance if match found
      case linePattern(x0, y0, x1, y1) => Some(LineCommand(x0.toInt, y0.toInt, x1.toInt, y1.toInt, gs, ch))
      case rectanglePattern(x0, y0, x1, y1) => Some(RectangleCommand(x0.toInt, y0.toInt, x1.toInt, y1.toInt, gs, ch))
      case circlePattern(x, y, r) => Some(CircleCommand(x.toInt, y.toInt, r.toInt, gs, ch))
      case fillRectanglePattern(c, x0, y0, x1, y1) => Some(FillRectangleCommand(c, x0.toInt, y0.toInt, x1.toInt, y1.toInt, gs, ch))
      // Other cases...
      case _ => None // Return None if the command string doesn't match any pattern
    }
  }
}

case class CanvasInformation(gridSpacing: Int, canvasHeight: Int)

sealed trait Command {
  def execute(): CommandResult  // Returns the generated pixels and optional info/warning/error message
}

case class CommandResult(pixels: java.util.List[Pixel], message: Message)

case class LineCommand(x0: Int, y0: Int, x1: Int, y1: Int, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // gs is canvas grids pacing and ch is canvas width
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = new CustomLine().drawLine(x0*gs, ch-(y0*gs), x1*gs, ch-(y1*gs))
    return CommandResult(pixels.asJava, new Message(s"Line drawn from [$x0, $y0] to [$x1, $y1]", MessageType.INFO))
  }
}

case class RectangleCommand(x0: Int, y0: Int, x1: Int, y1: Int, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // gs is canvas grids pacing and ch is canvas width
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = new CustomLine().drawRect(x0*gs, ch-(y0*gs), x1*gs, ch-(y1*gs))
    return CommandResult(pixels.asJava, new Message(s"Rectangle drawn between [$x0, $y0] and [$x1, $y1]", MessageType.INFO))
  }
}

case class CircleCommand(x: Int, y: Int, r: Int, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // gs is canvas grids pacing and ch is canvas width
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = new MidpointCircle().midpoint_circle(x*gs, ch-(y*gs), r*gs)
    return CommandResult(pixels.asJava, new Message("Circle drawn at [" + x + ", " + y + "] with radius " + r, MessageType.INFO))
  }
}

case class FillRectangleCommand(colour:String, x0: Int, y0: Int, x1: Int, y1: Int, gs: Int, ch: Int) extends Command {
  override def execute(): CommandResult = {
    // gs is canvas grids pacing and ch is canvas width
    // They are needed to adjust the coordinate values to the scale and orientation of the javafx canvas
    val pixels = new CustomLine().fillRect(x0*gs, ch-(y0*gs), x1*gs, ch-(y1*gs), colour)
    return CommandResult(pixels.asJava, new Message(s"Rectangle filled between [$x0, $y0] and [$x1, $y1]", MessageType.INFO))
  }
}