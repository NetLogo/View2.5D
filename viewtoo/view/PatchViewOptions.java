package viewtoo.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PatchViewOptions extends JPanel implements ChangeListener {
	
	private static final long serialVersionUID = 1L;
	PatchView myWindow;
	private JRadioButton structuresButton, sheetButton, pinsButton;
	
	
	public PatchViewOptions( PatchView parent, boolean str, boolean she, boolean pin ) {
		super();
		
		this.myWindow = parent;
		
		this.setLayout(new GridLayout(1,0) );
		setupUI( str, she, pin );
	}
	
	private void setupUI( boolean str, boolean she, boolean pin  ) {
		
		structuresButton = new JRadioButton("Structures");
		structuresButton.setActionCommand("structures");
		if (str) { structuresButton.setSelected(true); }
		
		sheetButton = new JRadioButton("Sheet");
		sheetButton.setActionCommand("sheet");
		if (she) { sheetButton.setSelected(true); }
		
		pinsButton = new JRadioButton("Pins");
		pinsButton.setActionCommand("pins");
		if (pin) { pinsButton.setSelected(true); }
		
		ButtonGroup group = new ButtonGroup();
		group.add(structuresButton);
		group.add(sheetButton);
		group.add(pinsButton);
		
		//Register a listener (myself) for the radio buttons.
		structuresButton.addChangeListener(this);
		sheetButton.addChangeListener(this);
		pinsButton.addChangeListener(this);
		
		
		this.setLayout(new GridLayout(1, 0));
		this.add( new JLabel( "  ") );
		this.add( structuresButton );
		this.add( sheetButton );
		this.add( pinsButton );
	
		this.setSize( new Dimension (600, 30));
	}
	
	public boolean sticks() { return pinsButton.isSelected(); }
	public boolean tangents() { return sheetButton.isSelected() || pinsButton.isSelected(); }
	public boolean structures() { return structuresButton.isSelected(); }

	@Override
	public void stateChanged(ChangeEvent arg0) {
		myWindow.refresh();
	}

}
