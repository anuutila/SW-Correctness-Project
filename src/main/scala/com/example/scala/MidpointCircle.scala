package com.example.scala

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._

class MidpointCircle(){

  // Midpoint Circle Algorithm
  def midpoint_circle (x:Int, y:Int, r:Int) : ListBuffer[Pixel] = {
    // List of Integers
    val pixels = ListBuffer.empty[Pixel]

    var x_coordinate: Int = r
    var y_coordinate: Int = 0

    pixels += new Pixel(x_coordinate+x, y_coordinate+y)

    // Only one pixels if radius<0
    if (r > 0) {
      pixels += new Pixel(x_coordinate + x, -y_coordinate + y)
      pixels += new Pixel(y_coordinate + y, x_coordinate + y)
      pixels += new Pixel(-y_coordinate + x, x_coordinate + y)
    }

    var P: Int = 1 - r

    breakable {
      while (x_coordinate > y_coordinate) {

        y_coordinate = y_coordinate + 1

        if (P <= 0) {
          P = P + 2 * y_coordinate + 1
        } else {
          x_coordinate = x_coordinate - 1
          P = P + 2 * y_coordinate - 2 * x_coordinate + 1
        }

        if (x_coordinate < y_coordinate) {
          break
        }

        // Adding pixels to list
        pixels += new Pixel(x_coordinate + x, y_coordinate + y)
        pixels += new Pixel(-x_coordinate + x, y_coordinate + y)
        pixels += new Pixel(x_coordinate + x, -y_coordinate + y)
        pixels += new Pixel(-x_coordinate + x, -y_coordinate + y)

        if (x_coordinate != y_coordinate) {
          pixels += new Pixel(y_coordinate + x, x_coordinate + y)
          pixels += new Pixel(-y_coordinate + x, x_coordinate + y)
          pixels += new Pixel(y_coordinate + x, -x_coordinate + y)
          pixels += new Pixel(-y_coordinate + x, -x_coordinate + y)
        }

      }

    }
      return pixels
    }

  def fill_circle(x_center:Int, y_center:Int, r:Int, colour: String): ListBuffer[Pixel] = {
    val pixels = ListBuffer.empty[Pixel]

    var x: Int = x_center - r
    var y: Int = y_center - r

    for( x <- 1 to x_center){
      for( y <- 1 to y_center){
        if((x-x_center)*(x-x_center)+(y - y_center)*(y - y_center) <= r*r){
          var xSym = x_center - (x - x_center)
          var ySym = y_center - (y - y_center)

          pixels += new Pixel(x,y,colour)
          pixels += new Pixel(x,ySym,colour)
          pixels += new Pixel(xSym,y,colour)
          pixels += new Pixel(xSym,ySym,colour)
        }
      }
    }

    return pixels
  }
}