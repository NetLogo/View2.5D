package varchart.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import org.nlogo.api.AgentException;
import org.nlogo.api.Context;
import org.nlogo.api.Patch;
import org.nlogo.api.ReporterTask;
import org.nlogo.app.App;

import varchart.VarchartExtension;
import varchart.view.gl.PatchGL;

public class PatchView extends VarviewWindow {

	private static final long serialVersionUID = 1L;
	
	private PatchGL glManager;
	private ReporterTask reporterTask;
	
	public PatchViewOptions viewOptions;
	
	public PatchValue[][] reporterValueMatrix;

	public PatchView(String title, ReporterTask rt) {
		super(title);
		reporterTask = rt;
	}
	
	public void postConstructor() {
		setupUI();
		this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		final String myTitle = this.getTitle();
		this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowClosing(WindowEvent e) { VarchartExtension.removePatchWindowWithTitle(myTitle); }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
         });
		GLCapabilities glCapabilities = new GLCapabilities();
		GLCanvas glCanvas =  new GLCanvas(glCapabilities);

		glManager = new PatchGL(this);
		
		glCanvas.addGLEventListener(glManager);
		glCanvas.addMouseListener(glManager);
		glCanvas.addMouseMotionListener(glManager);

		glManager.setCanvas( glCanvas );
		mainPanel.add(glCanvas, BorderLayout.CENTER);
		
		viewOptions = new PatchViewOptions( this, true, false, false);
		mainPanel.add(viewOptions, BorderLayout.NORTH);
		
		//change if re-add scalemanipulator
		setSize(new Dimension(600, 650));
		xExpand = 0;
		yExpand = 50;
		this.addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				applySquareConstraint(xExpand, yExpand, mainPanel);
			}
		});
		centerWindow(this);
	}
	
	
	
	public void setupForRendering(  int wWidth, int wHeight, int minX, int maxX, int minY, int maxY ) {
		worldWidth = wWidth;
		worldHeight = wHeight;
		minPxcor = minX;
		minPycor = minY;
		maxPxcor = maxX;
		maxPycor = maxY;
		reporterValueMatrix = new PatchValue[worldWidth][worldHeight];
	}
	
	
	public void updateValueArray(Context context) {
		for (int i = 0; i< worldWidth; i++){
			for (int j = 0;j< worldHeight; j++) {
				try {
					Patch patch = App.app().workspace().world().getPatchAt(i + minPxcor, j + minPycor);
					Color c = org.nlogo.api.Color.getColor(patch.pcolor());
					double val = (Double)reporterTask.report(context, new Object[]{patch});
					reporterValueMatrix[i][j] = new PatchValue(c, val);
				} catch (AgentException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void manuallyRefreshReporterView(Context context) {
		updateValueArray( context );
		glManager.repaintCanvas();
	}
	
	public void refresh() {
		glManager.repaintCanvas();
	}
	
	@Override
	public void resetPerspective() {
		glManager.observer.goHome(this);
		glManager.repaintCanvas();
	}
	
	public void zoomZby( double change ) {
		if (-change < zScale) {
			zScale += change;
		}
		glManager.repaintCanvas();
	}

}
