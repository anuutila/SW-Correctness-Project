package com.example.scala

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.break

final class MidpointCircle(){

  // Midpoint Circle Algorithm
  def midpoint_circle (x:Int, y:Int, r:Int) : List[Pixel] = {
    // List of Integers
    val pixels: ListBuffer[Pixel] = ListBuffer()

    var x_coordinate:Int = r
    var y_coordinate:Int = 0

    // Only one pixels if radius<0
    if(r<0){

    }

    var P:Int = 1-r

    while(x_coordinate<y_coordinate){

      y_coordinate = y_coordinate + 1
      if(P <= 0){
        P = P + 2 * y + 1
      }else{
        x_coordinate = x_coordinate - 1
        P = P + 2 * y - 2 * x + 1
      }

      if(x_coordinate < y_coordinate){
        break
      }

      // Adding pixels to list
      pixels.addOne(new Pixel(x_coordinate + x, y_coordinate + y))
      pixels.addOne(new Pixel(-x_coordinate + x, y_coordinate + y))
      pixels.addOne(new Pixel(x_coordinate + x, -y_coordinate + y))
      pixels.addOne(new Pixel(-x_coordinate + x, -y_coordinate + y))

      // Test
      print("hello")
      // Little change

      if (x != y) {
        pixels.addOne(new Pixel(y_coordinate + x, x_coordinate + y))
        pixels.addOne(new Pixel(-y_coordinate + x, x_coordinate + y))
        pixels.addOne(new Pixel(y_coordinate + x, -x_coordinate + y))
        pixels.addOne(new Pixel(-y_coordinate + x, -x_coordinate + y))
      }

    }

    return pixels.toList
  }
}