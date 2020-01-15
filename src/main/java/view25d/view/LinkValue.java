package view25d.view;

import java.awt.Color;

// The link value class contains the data needed to specify a 3D link for
// view25d.

public class LinkValue {
    public String shape;      // the link shape is currently always a line
    public Color color;       // link color
    public double thickness;  // link thickness
    public double xcor1;      // x coordinate of end1 turtle
    public double ycor1;      // y coordinate of end1 turtle
    public double zcor1;      // z coordinate of end1 turtle
    public double xcor2;      // x coordinate of end2 turtle
    public double ycor2;      // y coordinate of end2 turtle
    public double zcor2;      // z coordinate of end2 turtle

    // Note that when turtles and links are displayed, their z coordinates are multiplied by the zScale associated with the view

    public LinkValue( String sh, Color co, double th,
                      double x1, double y1, double z1,
                      double x2, double y2, double z2) {
        this.shape = sh;
        this.color = co;
        this.thickness = th;
        this.xcor1 = x1;
        this.ycor1 = y1;
        this.zcor1 = z1;
        this.xcor2 = x2;
        this.ycor2 = y2;
        this.zcor2 = z2;
    }

    @Override
    public String toString() {
       return "End1: [" +  this.xcor1 + " " +  this.ycor1 + " " + this.zcor1 + "], " +
           "End2: [" + this.xcor2 + " " +  this.ycor2 + " " + this.zcor2 +  "]" + 
           ", Thickness: " + this.thickness + ", Shape: " + this.shape + ", Color: " + this.color;
    }
}
