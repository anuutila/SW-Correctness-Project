<<<<<<<< HEAD:src/main/scala/com/example/scala/CustomLine.scala
package com.example.swcproject.scala
========
package com.example.scala
>>>>>>>> 36673577df5bde07fd6426c1ee6b01306e7f2245:src/main/scala/com/example/scala/Line.scala




// Draw line from Wikipedia:
/**
plotLine(x0, y0, x1, y1)
if abs(y1 - y0) < abs(x1 - x0)
if x0 > x1
plotLineLow(x1, y1, x0, y0)
else
plotLineLow(x0, y0, x1, y1)
end if
else
  if y0 > y1
plotLineHigh(x1, y1, x0, y0)
else
plotLineHigh(x0, y0, x1, y1)
end if
  end if


 plotLineHigh(x0, y0, x1, y1)
 dx = x1 - x0
 dy = y1 - y0
 xi = 1
 if dx < 0
 xi = -1
 dx = -dx
 end if
 D = (2 * dx) - dy
 x = x0

 for y from y0 to y1
 plot(x, y)
 if D > 0
 x = x + xi
 D = D + (2 * (dx - dy))
 else
 D = D + 2*dx
 end if


 plotLineLow(x0, y0, x1, y1)
 dx = x1 - x0
 dy = y1 - y0
 yi = 1
 if dy < 0
 yi = -1
 dy = -dy
 end if
 D = (2 * dy) - dx
 y = y0

 for x from x0 to x1
 plot(x, y)
 if D > 0
 y = y + yi
 D = D + (2 * (dy - dx))
 else
 D = D + 2*dy
 end if
**/

<<<<<<<< HEAD:src/main/scala/com/example/scala/CustomLine.scala
import scala.collection.JavaConverters.seqAsJavaListConverter
========
import com.example.scala.Pixel

>>>>>>>> 36673577df5bde07fd6426c1ee6b01306e7f2245:src/main/scala/com/example/scala/Line.scala
import scala.collection.mutable.ListBuffer

final class CustomLine {


private def plotLineHigh(x0:Int, y0:Int, x1:Int, y1:Int) : List[Pixel] = {
  var pixels: ListBuffer[Pixel] = ListBuffer[Pixel]()
  var dx = x1 - x0
  val dy = y1 - y0
  var xi = 1

  if (dx < 0) {
    xi = -1
    dx = -dx
  }

  var D = (2*dx)-dy
  var x = x0



  for (y <- y0 to y1){
    pixels += new Pixel(x,y)

    if(D > 0){
      x = x + xi
      D = D + (2 * (dx - dy))
    }
    else{
      D = D + 2*dx
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



  for (x <- x0 to x1){
    pixels += new Pixel(x,y)

    if(D>0){
      y = y + yi
      D = D + (2 * (dy - dx))
    }
    else{
      D = D + 2*dy
    }

  }

  return pixels.toList
}




def drawLine(x0:Int, y0:Int, x1:Int, y1:Int ) : java.util.List[Pixel]  = {

  if( (y1-y0).abs < (x1-x0).abs  ) {
    if (x0 > x1){
        return plotLineLow(x1,y1,x0,y0).asJava
      }
    else return plotLineLow(x0,y0,x1,y1).asJava
  }
  else
  {
    if (y0 > y1) {
      return plotLineHigh(x1,y1,x0,y0).asJava
    }
    else return plotLineHigh(x0,y0,x1,y1).asJava

  }
}


}