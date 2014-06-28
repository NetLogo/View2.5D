package varchart.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;

import org.nlogo.api.AgentException;
import org.nlogo.api.Context;
import org.nlogo.api.Patch;
import org.nlogo.api.ReporterTask;
import org.nlogo.app.App;

import varchart.view.gl.MeshGL;

public class MeshView extends VarviewWindow {

	private static final long serialVersionUID = 1L;
	
	private MeshGL glManager;
	private ReporterTask reporterTask;
	
	public int worldWidth, worldHeight;
	public double[][] reporterValueMatrix;

	public MeshView(String title, ReporterTask rt, Integer id) {
		super(title, id);
		reporterTask = rt;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GLCapabilities glCapabilities = new GLCapabilities();
		GLCanvas glCanvas =  new GLCanvas(glCapabilities);

		glManager = new MeshGL(this);
		
		glCanvas.addGLEventListener(glManager);
		glCanvas.addKeyListener(glManager);
		glCanvas.addMouseListener(glManager);
		glCanvas.addMouseMotionListener(glManager);

		glManager.setCanvas( glCanvas );

		getContentPane().add(glCanvas, BorderLayout.CENTER);
		setSize(new Dimension(500, 500));
		centerWindow(this);
	}



	public void setupForRendering(  int wWidth, int wHeight ) {
		worldWidth = wWidth;
		worldHeight = wHeight;
		reporterValueMatrix = new double[worldWidth][worldHeight];
	}

	
	public void updateValueArray(Context context) {
		int minpx = App.app().workspace().world().minPxcor();
		int minpy = App.app().workspace().world().minPycor();

		for (int i = 0; i< worldWidth; i++){
			for (int j = 0;j< worldHeight; j++) {
				try {
					Patch patch = App.app().workspace().world().getPatchAt(i + minpx, j + minpy);
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
		
	}
}
