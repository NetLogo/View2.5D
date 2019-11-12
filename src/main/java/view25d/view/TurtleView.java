package view25d.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import javax.swing.JFrame;

import org.nlogo.api.Agent;
import org.nlogo.api.AgentException;
import org.nlogo.api.AgentSet;
import org.nlogo.api.Context;
import org.nlogo.api.Patch;
import org.nlogo.api.AnonymousReporter;
import org.nlogo.api.Turtle;
import org.nlogo.app.App;

import view25d.View25DExtension;
import view25d.view.gl.TurtleGL;

public class TurtleView extends VarviewWindow {

    private static final long serialVersionUID = 1L;

    AgentSet myAgents;
    private TurtleGL glManager;
    public MouseableGLWindow getGLWindow() { return glManager; }

    private AnonymousReporter reporter;
	private AnonymousReporter stemColorReporter = null;
	private double defaultStemColor = 4.5f;
	
    public ArrayList<TurtleValue> turtleReporterValues;
    public Color[][] patchColorMatrix;

    public TurtleViewOptions viewOptions;


    public TurtleView( String title, AgentSet set, AnonymousReporter rt) {
        super(title);
        reporter = rt;
        myAgents = set;
    }

    public void postConstructor() {
        setupUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final String myTitle = this.getTitle();
        this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowClosing(WindowEvent e) { View25DExtension.removeTurtleWindowWithTitle(myTitle); }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });
        GLCapabilities glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        final GLCanvas glCanvas =  new GLCanvas(glCapabilities);

        glManager = new TurtleGL(this);

        glCanvas.addGLEventListener(glManager);
        glCanvas.addMouseListener(glManager);
        glCanvas.addMouseMotionListener(glManager);
        glCanvas.addMouseWheelListener(glManager);

        glManager.setCanvas( glCanvas );
        mainPanel.add(glCanvas, BorderLayout.CENTER);

        viewOptions = new TurtleViewOptions(this, true, true, true, false );
        mainPanel.add(viewOptions, BorderLayout.NORTH);

        //change if re-add scalemanipulator
        this.setSize(new Dimension(600, 640));
        xExpand = 0;
        yExpand = 40;
/*
        this.addComponentListener( new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                applySquareConstraint(xExpand, yExpand, mainPanel);
            }
        });
*/
        centerWindow(this);
    }


    @Override
    public void manuallyRefreshReporterView(Context context) {
        updateArrayList(context);
        glManager.repaintCanvas();
    }

    private void updateArrayList(Context context) {
        //turtleReporterValues.clear();
        turtleReporterValues = new ArrayList<TurtleValue>();
        for (Agent a : myAgents.agents()) {
            Turtle turtle = (Turtle)a;
            Color c = org.nlogo.api.Color.getColor(turtle.color());
            double val = (Double)reporter.report(context, new Object[]{turtle});
			double stemColor = getStemColor(context, turtle);
            TurtleValue tv = new TurtleValue( turtle.shape(), c, turtle.size(), turtle.xcor(), turtle.ycor(), val, stemColor);
            turtleReporterValues.add(tv);
        }
        if (viewOptions.usePColor()) {
            updatePColors();
        }
    }

    private void updatePColors() {
        for (int i = 0; i< worldWidth; i++){
            for (int j = 0;j< worldHeight; j++) {
                try {
                    Patch patch = App.app().workspace().world().getPatchAt(i + minPxcor, j + minPycor);
                    patchColorMatrix[i][j] = org.nlogo.api.Color.getColor(patch.pcolor());
                } catch (AgentException e) {
                    e.printStackTrace();
                }
            }
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

        patchColorMatrix = new Color[worldWidth][worldHeight];
        for (int i = 0; i< worldWidth; i++){
            for (int j = 0;j< worldHeight; j++) {
                patchColorMatrix[i][j] = Color.BLUE;
            }
        }
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
        if (viewOptions.usePColor()) {
            updatePColors();
        }
    }

    public void zoomZby( double change ) {
        if (-change < zScale) {
            zScale += change;
        }
        glManager.repaintCanvas();
    }

	public double trimStemColor(double stemColor) {
		if ( stemColor < 0 ) { stemColor = 0.0; }
		if ( stemColor >= 140 ) { stemColor = 139.9; }
		return stemColor;
	}

	public double getDefaultStemColor() { return defaultStemColor; }
	
	public double getStemColor(Context context, Turtle turtle) {
		double stemColor;
		if (stemColorReporter == null) {
			stemColor = defaultStemColor;
		} else {
			stemColor = (Double)stemColorReporter.report(context, new Object[]{turtle});
		}
		return trimStemColor(stemColor); 
	}

	public AnonymousReporter getStemColorReporter() { return stemColorReporter; }
	public void setStemColorReporter( AnonymousReporter reporter) {
	    stemColorReporter = reporter;
    }
}
