package view25d.view

import java.awt.{ BorderLayout, Component, Dimension, Toolkit }

import javax.swing.{ JFrame, JPanel }

import org.nlogo.agent.Turtle
import org.nlogo.api.{ AgentSet, Context, Link }
import org.nlogo.app.App

import scala.collection.JavaConverters._

import view25d.View25DExtension

object VarviewWindow {
  val ORBIT = 0
  val MOVE = 1
  val ZOOM = 2
  val RESET = -1
  val SCALEZ = 3
}

abstract class VarviewWindow(title: String) extends JFrame(title) {
  var worldWidth = 0
  var worldHeight = 0
  var minPxcor = 0
  var minPycor = 0
  var maxPxcor = 0
  var maxPycor = 0

  // pixels that add to the square-inner panel. used in asserting aspect ratio.
  var xExpand = 0
  var yExpand = 0

  // scale factor for the rendered turtle/patch variable.
  var zScale = 1.0

  def zoomZby(amount: Double): Unit
  def getGLWindow: MouseableGLWindow

  private var mode = VarviewWindow.ORBIT

  def setMode(newMode: Int) {
    mode = newMode
  }

  def getMode: Int =
    mode

  protected val dashboard = new VarviewDashboard(VarviewWindow.this)

  protected val mainPanel = new JPanel(new BorderLayout) {
    add(dashboard, BorderLayout.SOUTH)
  }

  getContentPane.add(mainPanel)

  protected def centerWindow(frame: Component) {
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val frameSize = new Dimension(frame.getWidth.min(screenSize.width), frame.getHeight.min(screenSize.height))

    val wincycle = 5

    val shiftWEach = (screenSize.width - frameSize.width) / wincycle
    val shiftHEach = (screenSize.height - frameSize.height) / wincycle

    val numWins = View25DExtension.numWindows

    val placeW = (numWins % wincycle) * shiftWEach + shiftWEach / 2
    val placeH = (numWins % wincycle) * shiftHEach + shiftHEach / 2

    frame.setLocation(placeW, placeH)
  }

  def resetPerspective(): Unit
  def manuallyRefreshReporterView(context: Context): Unit

  protected def applySquareConstraint(xBorder: Int, yBorder: Int, inner: JPanel) {
    val constraint = (inner.getWidth - xBorder).min(inner.getHeight - yBorder)

    inner.setPreferredSize(new Dimension(constraint + xBorder, constraint + yBorder))

    setSize(new Dimension(constraint + xBorder, constraint + yBorder + getHeight - inner.getHeight))
    invalidate()
  }

  // Given an AgentSet of turtles, produce a Set of associated Links.
  // Assumes check that AgentSet contains Turtles has already been made.
  // Currently includes links for which only one end is in the AgentSet
  protected def getLinkSetFromTurtleSet(turtleSet: AgentSet): Set[Link] = {
    // no work to do if there are no links or no turtles
    if (App.app.workspace.world.links.count == 0 || turtleSet.count == 0)
      return Set()

    turtleSet.agents.asScala.toSeq.collect {
      case turtle: Turtle =>
        turtle.links.filter(!_.hidden)
    }.flatten.toSet
  }
}
