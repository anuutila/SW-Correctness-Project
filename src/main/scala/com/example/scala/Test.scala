package com.example.scala

import scala.util.Random
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class Test {

  def generatePixels(): java.util.List[Pixel] = {
    val pixels = ListBuffer.empty[Pixel]
    val rand = new Random()
    val randomNumbers = Seq.fill(4)(rand.nextInt(601)).sorted
    var useColorParameter = true

    for {
      i <- randomNumbers(0) until randomNumbers(1)
      j <- randomNumbers(2) until randomNumbers(3)
    } {
      val pixel = if (useColorParameter) new Pixel(i, j, "#00FF00") else new Pixel(i, j)
      pixels += pixel
      useColorParameter = !useColorParameter
    }

    return pixels.toList.asJava
  }

}
