package at.fh.spiess.swengb.sierpinski_android

import android.content.Context
import android.graphics.{Canvas, Paint, Path}
import android.util.AttributeSet
import android.view.View


class SierpinskiCalculation (var context: Context, val attrs: AttributeSet) extends View(context, attrs) {

  override protected def onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    val screenWidth = this.getResources.getDisplayMetrics.widthPixels
    val screenHeight = this.getResources.getDisplayMetrics.heightPixels
    type XY = (Float, Float)
    type Triangle = Map[String, XY]

    def getFirstTriangle(): Triangle = {
      val triangleSize = screenWidth / 2
      val triangleHeight = Math.sqrt(Math.pow(triangleSize, 2) - Math.pow(triangleSize/2, 2))
      val path : Path = new Path()
      val paint: Paint = new Paint()
      paint.setStyle(Paint.Style.STROKE)
      path.moveTo(0, screenHeight  - screenHeight/ 4)
      path.lineTo(screenWidth, screenHeight  - screenHeight/ 4)
      path.lineTo(screenWidth / 2, (screenHeight  - screenHeight / 4 - triangleHeight * 2).asInstanceOf[Float])
      path.lineTo(0, screenHeight  - screenHeight/ 4)
      canvas.drawPath(path, paint)

      val bottom : XY = (screenWidth / 2, screenHeight  - screenHeight/ 4)
      val top : XY = (bottom._1, (bottom._2 - triangleHeight).asInstanceOf[Float])
      val left : XY = (bottom._1 - triangleSize / 2, top._2)
      val right : XY = (left._1 + triangleSize, left._2)
      val midLeft : XY = (bottom._1 - triangleSize/4, (bottom._2 - triangleHeight/2).asInstanceOf[Float])
      val midRight : XY = (bottom._1 + triangleSize/4, midLeft._2)
      Map("left"->left, "midLeft"->midLeft, "top"->top, "bottom"->bottom, "midRight"->midRight, "right"->right)
    }

    def getRecursionTriangles(originTriangle: Triangle): (Triangle, Triangle, Triangle) = {
      val newTriangleSize = (originTriangle.get("right").get._1 - originTriangle.get("left").get._1) / 2
      val newTriangleHeight = Math.sqrt(Math.pow(newTriangleSize, 2) - Math.pow(newTriangleSize/2, 2))

      val rightOuterLT: XY = originTriangle.getOrElse("midLeft", null)
      val topLT : XY = (originTriangle.get("left").get._1, rightOuterLT._2)
      val leftOuterLT : XY = (topLT._1 - newTriangleSize / 2, topLT._2)
      val leftInnerLT : XY = (topLT._1 - newTriangleSize / 4, (topLT._2 + newTriangleHeight / 2).asInstanceOf[Float])
      val bottomLT : XY = (originTriangle.get("left").get._1, originTriangle.get("bottom").get._2)
      val rightInnerLT : XY = (topLT._1 + newTriangleSize / 4, (topLT._2 + newTriangleHeight / 2).asInstanceOf[Float])

      val bottomTT : XY = originTriangle.getOrElse("top", null)
      val leftInnerTT : XY = (bottomTT._1 - newTriangleSize / 4, (bottomTT._2 - newTriangleHeight / 2).asInstanceOf[Float])
      val leftOuterTT : XY = (originTriangle.get("left").get._1, (bottomTT._2 - newTriangleHeight).asInstanceOf[Float])
      val topTT : XY = (bottomTT._1, leftOuterTT._2)
      val rightOuterTT : XY = (originTriangle.get("right").get._1, topTT._2)
      val rightInnerTT : XY = (topTT._1 + newTriangleSize / 4, leftInnerTT._2)
      val leftOuterRT : XY = originTriangle.getOrElse("right", null)
      val topRT : XY = (originTriangle.get("right").get._1, leftOuterRT._2)
      val rightOuterRT : XY = (topRT._1 + newTriangleSize / 2, topRT._2)
      val rightInnerRT : XY = (topRT._1 + newTriangleSize / 4, (topRT._2 + newTriangleHeight / 2).asInstanceOf[Float])
      val bottomRT : XY = (topRT._1, originTriangle.get("bottom").get._2)
      val leftInnerRT : XY = (leftOuterRT._1 + newTriangleSize / 4, rightInnerRT._2)
      val leftTriangle = Map("left"->leftOuterLT, "midLeft"->leftInnerLT, "top"->topLT, "bottom"->bottomLT, "midRight"->rightInnerLT, "right"->rightOuterLT)
      val topTriangle = Map("left"->leftOuterTT, "midLeft"->leftInnerTT, "top"->topTT, "bottom"->bottomTT, "midRight"->rightInnerTT, "right"->rightOuterTT)
      val rightTriangle = Map("left"->leftOuterRT, "midLeft"->leftInnerRT, "top"->topRT, "bottom"->bottomRT, "midRight"->rightInnerRT, "right"->rightOuterRT)

      (leftTriangle, topTriangle, rightTriangle)
    }

    def draw(triangle : Triangle) : Unit = {
      val path : Path = new Path()
      val paint: Paint = new Paint()
      paint.setStyle(Paint.Style.STROKE)

      val left : XY = triangle.getOrElse("left", null)
      val right : XY = triangle.getOrElse("right", null)
      val bottom : XY = triangle.getOrElse("bottom", null)

      path.moveTo(left._1, left._2)
      path.lineTo(right._1, right._2)
      path.lineTo(bottom._1, bottom._2)
      path.lineTo(left._1, left._2)
      canvas.drawPath(path, paint)
    }

    def recursiveSierpinski(triangle: Triangle, rec: Int) : Unit = {
      val recTriangles = getRecursionTriangles(triangle)

      if (rec > 0) {
        draw(recTriangles._1)
        draw(recTriangles._2)
        draw(recTriangles._3)

        recursiveSierpinski(recTriangles._1, rec - 1)
        recursiveSierpinski(recTriangles._2, rec - 1)
        recursiveSierpinski(recTriangles._3, rec - 1)
      }
    }

    val firstTriangle = getFirstTriangle()
    draw(firstTriangle)
    recursiveSierpinski(firstTriangle,7)
  }
}
