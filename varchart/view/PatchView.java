package varchart.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import org.nlogo.api.AgentException;
import org.nlogo.api.Context;
import org.nlogo.api.Patch;
import org.nlogo.api.ReporterTask;
import org.nlogo.app.App;

import varchart.view.gl.PatchGL;

public class PatchView extends VarviewWindow {

	private static final long serialVersionUID = 1L;
	
	private PatchGL glManager;
	private ReporterTask reporterTask;
	
	
	public double[][] reporterValueMatrix;

	public PatchView(String title, ReporterTask rt, Integer id) {
		super(title, id);
		reporterTask = rt;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GLCapabilities glCapabilities = new GLCapabilities();
		GLCanvas glCanvas =  new GLCanvas(glCapabilities);

		glManager = new PatchGL(this);
		
		glCanvas.addGLEventListener(glManager);
		//glCanvas.addKeyListener(glManager);
		glCanvas.addMouseListener(glManager);
		glCanvas.addMouseMotionListener(glManager);

		glManager.setCanvas( glCanvas );

		getContentPane().add(glCanvas, BorderLayout.CENTER);
		setSize(new Dimension(600, 600));
		centerWindow(this);
	}
	
	
	public void setupForRendering(  int wWidth, int wHeight, int minX, int maxX, int minY, int maxY ) {
		worldWidth = wWidth;
		worldHeight = wHeight;
		minPxcor = minX;
		minPycor = minY;
		maxPxcor = maxX;
		maxPycor = maxY;
		reporterValueMatrix = new double[worldWidth][worldHeight];
	}
	
	
	public void updateValueArray(Context context) {
		for (int i = 0; i< worldWidth; i++){
			for (int j = 0;j< worldHeight; j++) {
				try {
					Patch patch = App.app().workspace().world().getPatchAt(i + minPxcor, j + minPycor);
					reporterValueMatrix[i][j] = (Double)reporterTask.report(context, new Object[]{patch});
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
	
	@Override
	public void resetPerspective() {
		glManager.observer.goHome(this);
		glManager.repaintCanvas();
	}

}
