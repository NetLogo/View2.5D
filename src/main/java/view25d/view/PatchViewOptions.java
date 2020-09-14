package view25d.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PatchViewOptions extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;
	PatchView myWindow;
	private JRadioButton structuresButton, sheetButton, pinsButton, threeDButton, twoDButton;


	public PatchViewOptions( PatchView parent, boolean str, boolean she, boolean pin, boolean threeD, boolean twoD ) {
		super();

		this.myWindow = parent;

		this.setLayout(new GridLayout(1,0) );
		setupUI( str, she, pin, threeD, twoD );
	}

	private void setupUI( boolean str, boolean she, boolean pin,  boolean threeD, boolean twoD  ) {

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

		threeDButton = new JRadioButton("xyz");
		threeDButton.setActionCommand("xyz");
		if (threeD) { threeDButton.setSelected(true); }

		twoDButton = new JRadioButton("xy-plane");
		twoDButton.setActionCommand("xy-plane");
		if (twoD) { twoDButton.setSelected(true); }

		ButtonGroup group2 = new ButtonGroup();
		group2.add(threeDButton);
		group2.add(twoDButton);

		structuresButton.addChangeListener(this);
		sheetButton.addChangeListener(this);
		pinsButton.addChangeListener(this);
		threeDButton.addChangeListener(this);
		twoDButton.addChangeListener(this);

		this.setLayout(new GridLayout(2, 4));
		this.add( new JLabel( "  Display Options:") );
		this.add( structuresButton );
		this.add( sheetButton );
		this.add( pinsButton );
		this.add( new JLabel( "  Link Options:") );
		this.add( threeDButton );
		this.add( twoDButton );
		this.setSize( new Dimension (600, 60));
	}

	public boolean sticks() { return pinsButton.isSelected(); }
	public boolean tangents() { return sheetButton.isSelected() || pinsButton.isSelected(); }
	public boolean structures() { return structuresButton.isSelected(); }
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
	public void stateChanged(ChangeEvent changeEvent) {

	JRadioButton aButton = (JRadioButton) changeEvent.getSource();
	ButtonModel aModel = aButton.getModel();
	String text = aButton.getText();

	if (aModel.isSelected()) {
		if (text.equals("Sheet") || text.equals("Pins") ) {
				threeDButton.setEnabled ( false );
				twoDButton.setEnabled ( false );
		}
		if (text.equals("Structures")  ) {
				threeDButton.setEnabled ( true );
				twoDButton.setEnabled ( true );
		}
	}
		myWindow.refresh();
	}
}
