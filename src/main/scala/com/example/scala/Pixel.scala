package com.example.scala

class Pixel(x_coord:Int, y_coord:Int, coloring: String = "#000000")
{
  var color: String = coloring
  var x: Int = x_coord
  var y: Int = y_coord

  def Get_Color: String = {
    return this.color
  }

  def Get_X(): Int = {
    return this.x
    }

  def Get_Y(): Int = {
    return this.y
  }

}