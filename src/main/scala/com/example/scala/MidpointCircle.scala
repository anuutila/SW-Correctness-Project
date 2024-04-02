package com.example.scala

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._
import scala.jdk.CollectionConverters._

class MidpointCircle(){

  // Midpoint Circle Algorithm
  def midpoint_circle (x:Int, y:Int, r:Int) : java.util.List[Pixel] = {
    // List of Integers
    //val pixels: ListBuffer[Pixel] = ListBuffer()
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
        //pixels.addOne(new Pixel(x_coordinate + x, y_coordinate + y))
        //pixels.addOne(new Pixel(-x_coordinate + x, y_coordinate + y))
        //pixels.addOne(new Pixel(x_coordinate + x, -y_coordinate + y))
        //pixels.addOne(new Pixel(-x_coordinate + x, -y_coordinate + y))

        // Test
        //print("hello")
        // Little change

        if (x_coordinate != y_coordinate) {
          pixels += new Pixel(y_coordinate + x, x_coordinate + y)
          pixels += new Pixel(-y_coordinate + x, x_coordinate + y)
          pixels += new Pixel(y_coordinate + x, -x_coordinate + y)
          pixels += new Pixel(-y_coordinate + x, -x_coordinate + y)
          //pixels.addOne(new Pixel(y_coordinate + x, x_coordinate + y))
          //pixels.addOne(new Pixel(-y_coordinate + x, x_coordinate + y))
          //pixels.addOne(new Pixel(y_coordinate + x, -x_coordinate + y))
          //pixels.addOne(new Pixel(-y_coordinate + x, -x_coordinate + y))
        }

      }

    }
      return pixels.toList.asJava
    }
}