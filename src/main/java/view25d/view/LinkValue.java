package view25d.view;

import java.awt.Color;

public class LinkValue {
	
	public String shape;
	public Color color;
	public double thickness;
	public double xcor1; 
	public double ycor1;
    public double zcor1;
    public double xcor2; 
	public double ycor2;
    public double zcor2;
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



// link.pitch() ?

//     private void updateArrayList(Context context) throws LogoException {
//         //linkReporterValues.clear();
//         linkReporterValues = new ArrayList<LinkValue>();
//         for (Agent a : myAgents.agents()) {
//             Link link = (Link)a;
//             Color c = org.nlogo.api.Color.getColor(link.color());
// Turtle t1 = Turtle link.end1
//      Turtle t2 = link.end1
//             double stemColor = getStemColor(context, link);
//             LinkValue lv = new LinkValue( link.shape(), c, link.lineThickness(),
//                                           link.x1(), link.y1(), z1,
//                                            link.x2(), link.y2(), z2);
//             linkReporterValues.add(lv);
//         }
//     }
