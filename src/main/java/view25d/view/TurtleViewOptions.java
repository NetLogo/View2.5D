package view25d.view;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TurtleViewOptions extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;

	private double stemThickness = 0.0;
	TurtleView myWindow;
	JCheckBox color, size, shape, pcolor;
  private JRadioButton threeDButton, twoDButton;


	public TurtleViewOptions( TurtleView parent, boolean co, boolean sz, boolean sh, boolean pco, boolean threeD, boolean twoD  ) {
		super();

		this.myWindow = parent;

		this.setLayout(new GridLayout(2, 5));
		color = new JCheckBox("Color", co);
		size = new JCheckBox("Size", sz);
		shape = new JCheckBox("Shape", sh);
		pcolor = new JCheckBox("PColor", pco);

		color.addChangeListener(this);
		size.addChangeListener(this);
		shape.addChangeListener(this);
		pcolor.addChangeListener(this);

		this.add( new JLabel("  View Options:") );
		this.add(color);
		this.add(size);
		this.add(shape);
		this.add(pcolor);

    threeDButton = new JRadioButton("xyz");
		threeDButton.setActionCommand("xyz");
		if (threeD) { threeDButton.setSelected(true); }

		twoDButton = new JRadioButton("xy-plane");
		twoDButton.setActionCommand("xy-plane");
		if (twoD) { twoDButton.setSelected(true); }

		ButtonGroup group2 = new ButtonGroup();
		group2.add(threeDButton);
		group2.add(twoDButton);

		threeDButton.addChangeListener(this);
		twoDButton.addChangeListener(this);

		this.add( new JLabel( "  Link Options:") );
		this.add( threeDButton );
		this.add( twoDButton );
		this.add( new JLabel( "") );
		this.add( new JLabel( "") );
	}

	public boolean showColor() { return color.isSelected(); }
	public boolean showSize() { return size.isSelected(); }
	public boolean showShape() { return shape.isSelected(); }
	public boolean usePColor() { return pcolor.isSelected(); }
	public double getStemThickness() { return stemThickness; }
	public void setStemThickness(double thickness) {
		if (thickness > 0) { stemThickness = thickness;}
		else { stemThickness = 0; }
	}
	public boolean linksAreThreeD() { return threeDButton.isSelected(); }
	public boolean linksAreTwoD() { return twoDButton.isSelected(); }
	public void setLinksDisplayMode(boolean linksInXYPlane) {
		if (linksInXYPlane) {
			twoDButton.setSelected(true);
		} else {
			threeDButton.setSelected(true);
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		//System.err.println("state.  now usepcolor selection is: " + usePColor() );
		//System.err.println("source = " + arg0.getSource());
		myWindow.refresh();
	}

}
