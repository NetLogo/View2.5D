package varchart.view;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TurtleViewOptions extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	
	TurtleView myWindow;
	JCheckBox color, size, shape;
	
	public TurtleViewOptions( TurtleView parent, boolean co, boolean sz, boolean sh ) {
		super();
		
		this.myWindow = parent;
		
		this.setLayout(new GridLayout(1,0) );
		color = new JCheckBox("Color", co);
		size = new JCheckBox("Size", sz);
		shape = new JCheckBox("Shape", sh);
		
		color.addChangeListener(this);
		size.addChangeListener(this);
		shape.addChangeListener(this);
		
		this.add( new JLabel("View Options: ") );
		this.add(color);
		this.add(size);
		this.add(shape);
	}
	
	public boolean showColor() { return color.isSelected(); }
	public boolean showSize() { return size.isSelected(); }
	public boolean showShape() { return shape.isSelected(); }

	@Override
	public void stateChanged(ChangeEvent arg0) {
		myWindow.refresh();
	}

}
