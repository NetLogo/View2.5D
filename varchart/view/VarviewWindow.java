package varchart.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.nlogo.api.Context;

public abstract class VarviewWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	int ID;
	
	public int worldWidth, worldHeight, minPxcor, minPycor, maxPxcor, maxPycor;
	
	private VarviewDashboard dashboard;
	
	public static final int ORBIT = 0;
	public static final int MOVE = 1;
	public static final int ZOOM = 2;
	public static final int RESET = -1;
	
	private int mode = ORBIT;
	
	public void setMode(int newmode) { 
		mode = newmode;
	}
	
	public int getMode() { return mode; }

	public VarviewWindow(String title, int anId) {
		super(title);
		ID = anId;
		setupUI();
	}
	
	private void setupUI() {
		dashboard = new VarviewDashboard( this );
		this.setLayout( new BorderLayout() );
		this.add(dashboard, BorderLayout.SOUTH);
	}

	protected void centerWindow( Component frame ) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if ( frameSize.width > screenSize.width )
			frameSize.width = screenSize.width;

		if ( frameSize.height > screenSize.height )
			frameSize.height = screenSize.height;

		frame.setLocation(
			(screenSize.width - frameSize.width) >> 1,
			(screenSize.height - frameSize.height) >> 1
		);
	}

	public abstract void resetPerspective();
	
	public abstract void manuallyRefreshReporterView( Context context );

	
	
}
