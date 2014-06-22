package varchart.view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class VarviewDashboard extends JPanel {

	private static final long serialVersionUID = 1L;
	private int myId;
	
	public VarviewDashboard(int id) {
		myId = id;
		setupUI();
	}
	
	private void setupUI() {
		JLabel wip = new JLabel("Work in progress. In Orbit mode, with Rotation focus at 0, 0, 0");
		this.setLayout(new BorderLayout());
		this.add(wip, BorderLayout.CENTER);
	}

}
