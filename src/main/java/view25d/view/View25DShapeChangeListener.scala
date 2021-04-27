// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package view25d.view
import view25d.View25DExtension
import org.nlogo.app.App;
import org.nlogo.core.{ ShapeEvent, ShapeAdded, ShapeRemoved }

object View25DShapeChangeListener {
  def listen() {
    val turtleShapeTracker = App.app.workspace.world.turtleShapes

    val turtleListener = new turtleShapeTracker.Sub {
      def notify(pub: turtleShapeTracker.Pub, event: ShapeEvent): Unit = {
        event match {
          case ShapeAdded(newShape, oldShapeOption, _) =>
             println("shape added new shape:" + newShape.name)

             for (o <- oldShapeOption) {
               println("old shape: " + o.name)
             }

            //oldShapeOption.foreach(workspace.shapeChanged)
            //workspace.shapeChanged(newShape)
            View25DExtension.updateTurtleShapesAllTurtleViews
          case ShapeRemoved(removedShape, _) => //workspace.shapeChanged(removedShape)
              println("shape removed shape:" + removedShape.name)
              View25DExtension.updateTurtleShapesAllTurtleViews
          case _ =>
            println("other ..." + event)
            View25DExtension.updateTurtleShapesAllTurtleViews
            // note that the other cases aren't handled here as they happen only when a view refresh would happen anyway
        }
      }
    }

    turtleShapeTracker.subscribe(turtleListener)
  }
}
