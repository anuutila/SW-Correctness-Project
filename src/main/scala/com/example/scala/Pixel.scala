package com.example.scala

class Pixel(x_coord:Int, y_coord:Int)
{
  var x: Int = x_coord
  var y: Int = y_coord

  def Get_X(): Int = {
    return this.x
    }

  def Get_Y(): Int = {
    return this.y
  }

}