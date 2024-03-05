package com.example.swcproject.scala

class Pixel(x_coord:Int, y_coord:Int)
{
  private var x: Int = x_coord
  private var y: Int = y_coord

  def Get_X(): Int = {
    return this.x
    }

  def Get_Y(): Int = {
    return this.y
  }

}