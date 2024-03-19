package com.example.scala

import scala.util.Random
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class Test {

  def generatePixels(): java.util.List[Pixel] = {
    val pixels = ListBuffer.empty[Pixel]
    val rand = new Random()
    val randomNumbers = Seq.fill(4)(rand.nextInt(601)).sorted
    for {
      i <- randomNumbers(0) until randomNumbers(1)
      j <- randomNumbers(2) until randomNumbers(3)
    } pixels += new Pixel(i, j)

    return pixels.toList.asJava
  }

}
