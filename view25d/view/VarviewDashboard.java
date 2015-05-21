package view25d.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class VarviewDashboard extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private VarviewWindow myWindow;
	
	private JRadioButton orbitButton, zoomButton, moveButton, zscaleButton;
	public VarviewDashboard( VarviewWindow window) {
		myWindow = window;
		setupUI();
	}
	
	private void setupUI() {
		
		orbitButton = new JRadioButton("orbit");
		orbitButton.setActionCommand("orbit");
		orbitButton.setSelected(true);
		
		zoomButton = new JRadioButton("zoom");
		zoomButton.setActionCommand("zoom");
		moveButton = new JRadioButton("move");
		moveButton.setActionCommand("move");
		
		zscaleButton = new JRadioButton("z-scale");
		zscaleButton.setActionCommand("zscale");
		
		ButtonGroup group = new ButtonGroup();
		group.add(orbitButton);
		group.add(zoomButton);
		group.add(moveButton);
		group.add(zscaleButton);
		
		
		//Register a listener for the radio buttons.
		orbitButton.addActionListener(this);
		zoomButton.addActionListener(this);
		moveButton.addActionListener(this);
		zscaleButton.addActionListener(this);
		
			
		JButton resetPerspective = new JButton("reset perspective");
		resetPerspective.setActionCommand("reset");
		resetPerspective.addActionListener(this);
		
		this.setLayout(new GridLayout(1, 0));
		this.add( new JLabel( "  ") );
		this.add( orbitButton );
		this.add( zoomButton );
		this.add( moveButton );
		this.add(zscaleButton);
		this.add( new JLabel( "  ") );
		this.add( resetPerspective );
		
		this.setSize( new Dimension (600, 40));
		
		//JLabel wip = new JLabel("Work in progress. In Orbit mode, with Rotation focus at 0, 0, 0");
		//this.add(wip, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd =  e.getActionCommand();
	    if ( cmd == "orbit" ) {
	    	myWindow.setMode(VarviewWindow.ORBIT);
	    }
	    else if ( cmd == "move" ) {
	    	myWindow.setMode(VarviewWindow.MOVE);
	    }
	    else if  ( cmd == "zoom" ) {
	    	myWindow.setMode(VarviewWindow.ZOOM);
	    }
	    else if  ( cmd == "zscale" ) {
	    	myWindow.setMode(VarviewWindow.SCALEZ);
	    }
	    else if  ( cmd == "reset" ) {
	    	myWindow.setMode(VarviewWindow.ORBIT);
	    	orbitButton.setSelected(true);
	    	//myWindow.zScale = 1;
	    	myWindow.resetPerspective();
	    }
	}
			 

}
