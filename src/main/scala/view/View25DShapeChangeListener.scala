// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package view25d.view

import java.awt.GraphicsEnvironment
import java.util.Objects

import view25d.View25DExtension
import org.nlogo.app.App
import org.nlogo.core.ShapeEvent

object View25DShapeChangeListener {
  def isHeadless: Boolean =
    GraphicsEnvironment.isHeadless || Objects.equals(System.getProperty("org.nlogo.preferHeadless"), "true")

  def listen() {
    if (!isHeadless) {
      val turtleShapeTracker = App.app.workspace.world.turtleShapes

      val turtleListener = new turtleShapeTracker.Sub {
        def notify(pub: turtleShapeTracker.Pub, event: ShapeEvent): Unit = {
          View25DExtension.updateTurtleShapesAllViews
        }
      }
      turtleShapeTracker.subscribe(turtleListener)
    }
  }
}
