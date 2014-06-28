package varchart.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import org.nlogo.api.Agent;
import org.nlogo.api.AgentSet;
import org.nlogo.api.Context;
import org.nlogo.api.ReporterTask;
import org.nlogo.api.Turtle;

import varchart.view.gl.TurtleGL;

public class TurtleView extends VarviewWindow {

	private static final long serialVersionUID = 1L;
	
	AgentSet myAgents;
	private TurtleGL glManager;
	private ReporterTask reporterTask;
	
	public ArrayList<TurtleValue> turtleReporterValues;
	
	public boolean showShape, showSize, showColor;
	
	
	public TurtleView( String title, AgentSet set, ReporterTask rt, Integer id ) {
		super(title, id);
		reporterTask = rt;
		myAgents = set;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GLCapabilities glCapabilities = new GLCapabilities();
		GLCanvas glCanvas =  new GLCanvas(glCapabilities);

		glManager = new TurtleGL(this);
		
		glCanvas.addGLEventListener(glManager);
		glCanvas.addKeyListener(glManager);
		glCanvas.addMouseListener(glManager);
		glCanvas.addMouseMotionListener(glManager);

		glManager.setCanvas( glCanvas );

		getContentPane().add(glCanvas, BorderLayout.CENTER);
		
		//Use controls/prefs for turtle view.
		showShape = true;
		showSize = true;
		showColor = true;
				
		setSize(new Dimension(600, 600));
		
		

		
		centerWindow(this);		
	}
	
	@Override
	public void manuallyRefreshReporterView(Context context) {
		updateArrayList(context);
		glManager.repaintCanvas();
	}
	
	private void updateArrayList(Context context) {
		turtleReporterValues.clear();
		for (Agent a : myAgents.agents()) {
			Turtle turtle = (Turtle)a;
			Color c = org.nlogo.api.Color.getColor(turtle.color());
			double val = (Double)reporterTask.report(context, new Object[]{turtle});
			System.err.println("SIZE: " + turtle.size());
			TurtleValue tv = new TurtleValue( turtle.shape(), c, turtle.size(), turtle.xcor(), turtle.ycor(), val);
			turtleReporterValues.add(tv);
		}
	}
	
	public void setupForRendering(  int wWidth, int wHeight, int minX, int maxX, int minY, int maxY ) {
		worldWidth = wWidth;
		worldHeight = wHeight;
		minPxcor = minX;
		minPycor = minY;
		maxPxcor = maxX;
		maxPycor = maxY;
		
		turtleReporterValues = new ArrayList<TurtleValue>(myAgents.count());
	}

	public void setAgentSet(AgentSet as) {
		myAgents = as;
	}
	
	
	@Override
	public void resetPerspective() {
		glManager.observer.goHome(this);
		glManager.repaintCanvas();
	}

}
