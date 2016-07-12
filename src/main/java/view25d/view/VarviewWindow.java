package view25d.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.nlogo.api.Context;

import view25d.View25DExtension;

public abstract class VarviewWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public int worldWidth, worldHeight, minPxcor, minPycor, maxPxcor, maxPycor;
	public int xExpand, yExpand; //pixels that add to the square-inner panel. used in asserting aspect ratio.
	
	//scale factor for the rendered turtle/patch variable.
	public double zScale = 1.0;
	public abstract void zoomZby( double amount );
	public abstract MouseableGLWindow getGLWindow();
	
	protected JPanel mainPanel;
	private VarviewDashboard dashboard;
	
	public static final int ORBIT = 0;
	public static final int MOVE = 1;
	public static final int ZOOM = 2;
	public static final int RESET = -1;
	public static final int SCALEZ = 3;
	
	private int mode = ORBIT;
	
	public void setMode(int newmode) { 
		mode = newmode;
	}
	public int getMode() { return mode; }
	
	
	public VarviewWindow(String title) {
		super(title);
	}
	
	//now called via 'postconstructor' method in subclasses
	protected void setupUI() {
		dashboard = new VarviewDashboard( this );
		mainPanel = new JPanel();
		mainPanel.setLayout( new BorderLayout() );
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
		
		int wincycle = 5;
		int widPlay = (screenSize.width - frameSize.width);
		int heightPlay = (screenSize.height - frameSize.height);
		
		int shiftWEach = widPlay / wincycle;
		int shiftHEach = heightPlay / wincycle;
		
		int numWins = View25DExtension.numWindows();
		int placeW = (numWins % wincycle) * shiftWEach + shiftWEach / 2;
		int placeH = (numWins % wincycle) * shiftHEach + shiftHEach / 2;
	

		frame.setLocation(
			placeW,
			placeH
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
