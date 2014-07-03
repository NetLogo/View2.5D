package varchart.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class ScaleManipulator extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	VarviewWindow myWindow;
	
	
	double miny, maxy;
	double defaultmin, defaultmax;
	JTextField minyf, maxyf;
	//boolean isAuto = false;
	//JButton autoResize;
	ScaleRepresentation scaler;
	
	public ScaleManipulator( VarviewWindow vvw, double minYval, double maxYval ) {
		myWindow = vvw;
		defaultmin = minYval;
		defaultmax = maxYval;
		miny = minYval;
		maxy = maxYval;
		
		minyf = new JTextField(String.valueOf(minYval));
		minyf.setActionCommand("miny");
		minyf.addActionListener(this);
		
		maxyf = new JTextField(String.valueOf(maxYval));
		maxyf.setActionCommand("maxy");
		maxyf.addActionListener(this);
		
//		autoResize = new JButton("AUTO");
//		autoResize.setActionCommand("auto");
//		autoResize.addActionListener(this);
		
		scaler = new ScaleRepresentation();
	
//		JPanel repAndAuto  = new JPanel();
//		repAndAuto.setLayout(new BorderLayout() );
//		repAndAuto.add( autoResize, BorderLayout.SOUTH); 
//		
//		repAndAuto.add( scaler, BorderLayout.CENTER );
		
		this.setLayout( new BorderLayout() );
		
		this.add(maxyf, BorderLayout.NORTH);
		this.add(scaler, BorderLayout.CENTER );
		this.add(minyf, BorderLayout.SOUTH );
		
		this.setPreferredSize(new Dimension(60, 600));
	}
	

	@Override
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if ( cmd.equals( "miny" ) ) {
			if ( validateEntry(minyf) ) {
				miny = Double.valueOf(minyf.getText());
				System.err.println("changed miny to "+ maxy);
				//isAuto = false;
				//autoResize.setEnabled(true);
				myWindow.repaint();
			} else {
				minyf.setText(  Double.toString(miny)  );
			}
		}
		if ( cmd.equals( "maxy") ) {
			if ( validateEntry(maxyf) ) {
				maxy = Double.valueOf(maxyf.getText());
				System.err.println("changed maxy to "+ maxy);
				//isAuto = false;
				//autoResize.setEnabled(true);
				myWindow.repaint();
			} else {
				maxyf.setText(  Double.toString(maxy)  );
			}
		}
//		if ( cmd.equals( "auto") ) {
//			isAuto = true;
//			autoResize.setEnabled(false);
//			myWindow.repaint();
//		}
		
	}
	
	@SuppressWarnings("unused")
	private boolean validateEntry(JTextField tfield) {
		try {
			 double d = Double.valueOf(tfield.getText());
			 return true;
		}
		catch (Exception e ) {
			return false;
		}
	}
	
	
	
	private class ScaleRepresentation extends JPanel {
		private static final long serialVersionUID = 1L;
		private int ymax;
		private int ymin;
		private int xmargin = 10;
		private int ymargin = 10;

		public void paintComponent( Graphics g ) {
			Color oldc = g.getColor();
			g.setColor( Color.WHITE );
			ymax = this.getHeight() - ymargin;
			ymin = 2 * ymargin;
			int tickrad = 3;
			g.fillRect(0, 0, this.getWidth(), this.getHeight() );
			g.setColor( Color.BLACK );
			g.drawLine(xmargin, ymin, xmargin, ymax);
			g.drawLine(xmargin - tickrad, ymin, xmargin + tickrad, ymin);
			g.drawLine(xmargin - tickrad, ymax, xmargin + tickrad, ymax);
			g.drawString( " " + Double.toString(maxy), xmargin + tickrad, ymin);
			g.drawString( " " + Double.toString(miny), xmargin + tickrad, ymax);
			if ( miny <= 0 && maxy >= 0) {
				int zero = logicalToPixelY(0);
				
				g.drawLine(xmargin - tickrad, zero, xmargin + tickrad, zero);
				g.drawString( " 0", xmargin + tickrad, zero);
			}
			g.setColor(oldc);
		}
		
		public int logicalToPixelY( double yval ) {
			double perc = (yval - miny) / ( maxy - miny);
			return  (int) Math.round( (1 - perc) * (ymax - ymin) );
		}
	}




}
