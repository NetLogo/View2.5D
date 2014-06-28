package varchart.view;

import java.awt.Color;

public class TurtleValue {
	
	public String shape;
	public Color color;
	public double xcor; 
	public double ycor;
	public double size;
	public double reporterValue;

	public TurtleValue( String sh, Color co, double sz, double x, double y, double val) {
		this.shape = sh;
		this.color = co;
		this.xcor = x;
		this.ycor = y;
		this.size = sz;
		this.reporterValue = val;
	}
}
