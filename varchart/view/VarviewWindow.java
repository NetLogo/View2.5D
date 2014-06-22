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
	
	private VarviewDashboard dashboard;

	public VarviewWindow(String title, int anId) {
		super(title);
		ID = anId;
		setupUI();
	}
	
	private void setupUI() {
		dashboard = new VarviewDashboard( ID );
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

	public abstract void manuallyRefreshReporterView( Context context );

	
	
}
