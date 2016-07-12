package view25d.view;

import java.awt.Color;

public class TurtleValue {
	
	public String shape;
	public Color color;
	public double xcor; 
	public double ycor;
	public double size;
	public double reporterValue;
	public double heading = 0.0;

	public TurtleValue( String sh, Color co, double sz, double x, double y, double val) {
		this.shape = sh;
		this.color = co;
		this.xcor = x;
		this.ycor = y;
		this.size = sz;
		this.reporterValue = val;
	}
	
	//heading is optional; only used for the decorated patch view.  
	//all other turtles are viewed as sprites
	public void setHeading( double head ) { 
		this.heading = head;
	}
}
