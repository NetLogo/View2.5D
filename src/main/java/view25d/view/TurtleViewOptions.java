package view25d.view;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TurtleViewOptions extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;

	private double stemThickness = 0.0;
	private double stemColor = 4.5f;
	TurtleView myWindow;
	JCheckBox color, size, shape, pcolor;

	public TurtleViewOptions( TurtleView parent, boolean co, boolean sz, boolean sh, boolean pco ) {
		super();

		this.myWindow = parent;

		this.setLayout(new GridLayout(1,0) );
		color = new JCheckBox("Color", co);
		size = new JCheckBox("Size", sz);
		shape = new JCheckBox("Shape", sh);
		pcolor = new JCheckBox("PColor", pco);

		color.addChangeListener(this);
		size.addChangeListener(this);
		shape.addChangeListener(this);
		pcolor.addChangeListener(this);

		this.add( new JLabel("View Options: ") );
		this.add(color);
		this.add(size);
		this.add(shape);
		this.add(pcolor);
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
	public double getStemColor() { return stemColor; }
	public void setStemColor(double newColor) {
		if ( newColor < 0 ) { newColor = 0.0; }
		if ( newColor >= 140 ) { newColor = 139.9; }
		stemColor = newColor;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		//System.err.println("state.  now usepcolor selection is: " + usePColor() );
		//System.err.println("source = " + arg0.getSource());
		myWindow.refresh();
	}

}
