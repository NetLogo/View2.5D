// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package view25d.view

import java.awt.GraphicsEnvironment
import java.util.Objects

import view25d.View25DExtension
import org.nlogo.app.App

object View25DShapeChangeListener {
  def isHeadless: Boolean =
    GraphicsEnvironment.isHeadless || Objects.equals(System.getProperty("org.nlogo.preferHeadless"), "true")

  def listen(): Unit = {
    if (!isHeadless)
      App.app.workspace.world.turtleShapes.subscribe(_ => View25DExtension.updateTurtleShapesAllViews())
  }
}
