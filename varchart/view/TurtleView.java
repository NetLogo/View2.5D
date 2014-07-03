package varchart.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
	
	public TurtleViewOptions viewOptions;
	
	
	public TurtleView( String title, AgentSet set, ReporterTask rt, Integer id ) {
		super(title, id);
		reporterTask = rt;
		myAgents = set;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GLCapabilities glCapabilities = new GLCapabilities();
		final GLCanvas glCanvas =  new GLCanvas(glCapabilities);

		glManager = new TurtleGL(this);
		
		glCanvas.addGLEventListener(glManager);
		glCanvas.addMouseListener(glManager);
		glCanvas.addMouseMotionListener(glManager);

		glManager.setCanvas( glCanvas );
		mainPanel.add(glCanvas, BorderLayout.CENTER);
		
		viewOptions = new TurtleViewOptions(this, true, true, true );
		mainPanel.add(viewOptions, BorderLayout.NORTH);
		
		//change if re-add scalemanipulator
		this.setSize(new Dimension(600, 640));
		xExpand = 0;
		yExpand = 40;
		this.addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				applySquareConstraint(xExpand, yExpand, mainPanel);
			}
		});
		
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
			TurtleValue tv = new TurtleValue( turtle.shape(), c, turtle.size(), turtle.xcor(), turtle.ycor(), val);
			turtleReporterValues.add(tv);
		}
	}
	
	public ArrayList<TurtleValue> getCopyOfReporterValues() {
		ArrayList<TurtleValue> rtn = new ArrayList<TurtleValue>(turtleReporterValues.size() );
		rtn.addAll( turtleReporterValues );
		return rtn;
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
		refresh();
	}
	
	public void refresh() {
		glManager.repaintCanvas();
	}

}
