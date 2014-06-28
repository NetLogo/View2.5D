package varchart.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.nlogo.api.Context;

//testing
public class DummyView extends VarviewWindow {

	private static final long serialVersionUID = 1L;
	private ReporterView rv;

	public DummyView(String title, int anId) {
		super(title, anId);
		setupReporterView();
	}

	private void setupReporterView() {
		rv = new ReporterView();
		this.add(rv, BorderLayout.CENTER);
		setSize(new Dimension(500, 500));
	    centerWindow(this);
		this.setVisible( true );
	}
	
	@Override
	public void manuallyRefreshReporterView(Context context) {
		rv.num++;
	}

	
	private class ReporterView extends JPanel {

		private static final long serialVersionUID = 1L;
		public String sal = "hello ";
		public int num = 0;
		public void paintComponent( Graphics g ) {
			g.drawString(sal+num, 10, 10);
		}
	}
	
	@Override
	public void resetPerspective() {
		
	}
}
