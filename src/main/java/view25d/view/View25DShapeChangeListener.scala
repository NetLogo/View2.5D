// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package view25d.view
import view25d.View25DExtension
import org.nlogo.app.App
import org.nlogo.core.ShapeEvent

object View25DShapeChangeListener {
  def listen() {
    val turtleShapeTracker = App.app.workspace.world.turtleShapes

    val turtleListener = new turtleShapeTracker.Sub {
      def notify(pub: turtleShapeTracker.Pub, event: ShapeEvent): Unit = {
        View25DExtension.updateTurtleShapesAllViews
      }
    }
    turtleShapeTracker.subscribe(turtleListener)
  }
}
