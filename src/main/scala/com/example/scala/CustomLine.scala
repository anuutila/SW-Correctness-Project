package com.example.scala
import com.example.scala.Pixel
import scala.collection.mutable.ListBuffer

final class CustomLine() {


  private def plotLineHigh(x0: Int, y0: Int, x1: Int, y1: Int): List[Pixel] = {
    var pixels: ListBuffer[Pixel] = ListBuffer[Pixel]()
    var dx = x1 - x0
    val dy = y1 - y0
    var xi = 1

    if (dx < 0) {
      xi = -1
      dx = -dx
    }

    var D = (2 * dx) - dy
    var x = x0


    for (y <- y0 to y1) {
      pixels += new Pixel(x, y)

      if (D > 0) {
        x = x + xi
        D = D + (2 * (dx - dy))
      }
      else {
        D = D + 2 * dx
      }

    }

    return pixels.toList
  }

  private def plotLineLow(x0: Int, y0: Int, x1: Int, y1: Int): List[Pixel] = {
    var pixels: ListBuffer[Pixel] = ListBuffer[Pixel]()
    val dx = x1 - x0
    var dy = y1 - y0
    var yi = 1

    if (dy < 0) {
      yi = -1
      dy = -dy
    }

    var D = (2 * dy) - dx
    var y = y0


    for (x <- x0 to x1) {
      pixels += new Pixel(x, y)

      if (D > 0) {
        y = y + yi
        D = D + (2 * (dy - dx))
      }
      else {
        D = D + 2 * dy
      }

    }

    return pixels.toList
  }


  def drawLine(x0: Int, y0: Int, x1: Int, y1: Int): List[Pixel] = {
    return drawLine_priv(x0, y0, x1, y1)
  }

  private def drawLine_priv(x0: Int, y0: Int, x1: Int, y1: Int): List[Pixel] = {

    if ((y1 - y0).abs < (x1 - x0).abs) {
      if (x0 > x1) {
        return plotLineLow(x1, y1, x0, y0)
      }
      else return plotLineLow(x0, y0, x1, y1)
    }
    else {
      if (y0 > y1) {
        return plotLineHigh(x1, y1, x0, y0)
      }
      else return plotLineHigh(x0, y0, x1, y1)

    }
  }


  def drawRect(x0: Int, y0: Int, x1: Int, y1: Int): List[Pixel] = {
    // defining the 4 corners of the rectangle:
    val corner_bl = new Pixel(x0, y0)
    val corner_tl = new Pixel(x0, y1)
    val corner_br = new Pixel(x1, y0)
    val corner_tr = new Pixel(x1, y1)

    // Lines between every corner:
    val bl_to_br_pixels = drawLine_priv(corner_bl.Get_X(), corner_bl.Get_Y(), corner_br.Get_X(), corner_br.Get_Y())
    val bl_to_tl_pixels = drawLine_priv(corner_bl.Get_X(), corner_bl.Get_Y(), corner_tl.Get_X(), corner_tl.Get_Y())
    val tr_to_tl_pixels = drawLine_priv(corner_tr.Get_X(), corner_tr.Get_Y(), corner_tl.Get_X(), corner_tl.Get_Y())
    val tr_to_br_pixels = drawLine_priv(corner_tr.Get_X(), corner_tr.Get_Y(), corner_br.Get_X(), corner_br.Get_Y())

    val total_pixels: List[Pixel] = bl_to_br_pixels ::: bl_to_tl_pixels ::: tr_to_tl_pixels ::: tr_to_br_pixels

    // Return total amount of pixels.
    return total_pixels
  }




  // Command to create a rectangle and fill it with pixels of colour
  def fillRect(x0: Int, y0: Int, x1: Int, y1: Int, colour: String):  List[Pixel]  = {

    var pixels: ListBuffer[Pixel] = ListBuffer[Pixel]()

    // If the x's or y's are equal then it is just a line and can't be filled.
    if(x0 == x1 | y0 == y1){
      return pixels.toList
    }

    // Find the biggest and smallest values of x and y:
    val least_x = if(x0 < x1) x0 else x1
    val biggest_x = if (x0 > x1) x0 else x1

    val least_y = if(y0 < y1) y0 else y1
    val biggest_y = if (y0 > y1) y0 else y1



    // Foreach x value in the range:
    for( new_x <- least_x to biggest_x)
    {
      // Create a pixel of every y-value in the range:
      for( new_y <- least_y to biggest_y){
        pixels += new Pixel(new_x, new_y, colour)

      }
    }

    return pixels.toList
  }

}