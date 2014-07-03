package varchart.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.nlogo.api.Context;

public abstract class VarviewWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	int ID;
	
	public int worldWidth, worldHeight, minPxcor, minPycor, maxPxcor, maxPycor;
	public int xExpand, yExpand; //pixels that add to the square-inner panel. used in asserting aspect ratio.
	
	protected JPanel mainPanel;
	private VarviewDashboard dashboard;
	private ScaleManipulator scale;
	
	public static final int ORBIT = 0;
	public static final int MOVE = 1;
	public static final int ZOOM = 2;
	public static final int RESET = -1;
	
	private int mode = ORBIT;
	
	public void setMode(int newmode) { 
		mode = newmode;
	}
	public int getMode() { return mode; }
	
	public double getMaxY() { return scale.maxy; }
	public double getMinY() { return scale.miny; }
	

	public VarviewWindow(String title, int anId) {
		super(title);
		ID = anId;
		setupUI();
	}
	
	private void setupUI() {
		dashboard = new VarviewDashboard( this );
		//scale = new ScaleManipulator( this, -1, 10 );
		mainPanel = new JPanel();
		mainPanel.setLayout( new BorderLayout() );
		//mainPanel.add(scale, BorderLayout.WEST);
		mainPanel.add(dashboard, BorderLayout.SOUTH);
		this.getContentPane().add(mainPanel);
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

	protected void applySquareConstraint(int xBorder, int yBorder, JPanel inner) {
		int chromeH = this.getHeight() - inner.getHeight();
        int w = inner.getWidth() - xBorder;
        int h = inner.getHeight() - yBorder;
        int constraint = Math.min(w, h);
        inner.setPreferredSize(new Dimension(constraint+xBorder, constraint+yBorder));
        this.setSize(new Dimension(constraint+xBorder, constraint+yBorder + chromeH));
        this.invalidate();
    } 
	
}
