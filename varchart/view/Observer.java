package varchart.view;

import javax.media.opengl.GL;

public class Observer {
	
	double heading, pitch, roll;
	double x, y, z;
	
	double rotx, roty, rotz;

	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}

	public Observer( double xv, double yv, double zv ) {
		this.x = xv;
		this.y = yv;
		this.z = zv;
		heading = 0.0;
		pitch = 90.0;
		roll = 0.0;

		rotx = 0;
		roty = 0;
		rotz = 0;
	}

	public void goHome( VarviewWindow world ) {		
		x = world.minPxcor + ((world.maxPxcor - world.minPxcor) / 2.0);
		y = world.minPycor + ((world.maxPycor - world.minPycor) / 2.0);
		z = StrictMath.max(world.worldWidth, world.worldHeight) * 1.5;
		heading = 0;
		pitch = 90;
		roll = 0;
		rotx = x;
		roty = y;
		rotz = 0;
		System.err.println("at start, x = " + x + "y = " + y + "z = " + z );
	}

	public void applyPerspective( GL gl ) {
		gl.glRotated(90, -1.0, 0.0, 0.0);
		gl.glRotated(heading, 0.0, 0.0, 1.0);
		gl.glRotated(pitch,
				Math.cos(Math.toRadians(heading)),
				-Math.sin(Math.toRadians(heading)), 0.0);
		gl.glTranslated(-1*x, -1*y, -1*z);
	}

	public void applyNormal( GL gl ) {
		double xp, yp, zp;
		//cross product of x, y, z and 0, 0, 1
		gl.glRotated( -heading, x, y, z);
		xp = y; // - z * 0;
		yp = -x; //+ z * 0
		zp = 0; // x+0 - y*0
		gl.glRotated(pitch - 90, xp, yp, zp);

	}


	public void updatePerspective(double thetax, double thetay) {
		orbitRight(thetax);
		System.err.println("ORBIT RIGHT: delta = " + thetax + ":" + x + "," + y + "," + z + ", HEADING=" + heading + ", PITCH=" + pitch);
		System.err.println("ROTATION PT:" + rotx + ", "+ roty + ", "+ rotz  );
		orbitUp(thetay);
		System.err.println("ORBIT UP: delta = " + thetay + ":" + x + "," + y + "," + z + ", HEADING=" + heading + ", PITCH=" + pitch);
		System.err.println("ROTATION PT:" + rotx + ", "+ roty + ", "+ rotz  );
	}


	private void orbitRight(double delta) {
		heading = heading + delta;
		if (heading > 360) { heading = heading - 360; }
		if (heading < 0 ) { heading = heading + 360; }

		double dxy = dist() * StrictMath.cos(StrictMath.toRadians(pitch));
		double xn = -dxy * StrictMath.sin(StrictMath.toRadians(heading));
		double yn = -dxy * StrictMath.cos(StrictMath.toRadians(heading));

		x = rotx + xn;
		y = roty + yn;
		//System.err.println( "orbitRight " + x + "," + y + "," + z);
	}

	private void orbitUp(double delta) {
		double newpitch = pitch - delta;
		double zn = dist() * StrictMath.sin(StrictMath.toRadians(newpitch));
		double dxy = dist() * StrictMath.cos(StrictMath.toRadians(newpitch));
		double xn = -dxy * StrictMath.sin(StrictMath.toRadians(heading));
		double yn = -dxy * StrictMath.cos(StrictMath.toRadians(heading));

		// don't let observer go under patch-plane or be upside-down
		if (zn + rotz > 0 && newpitch < 90) {
			x = xn + rotx;
			y = yn + roty;
			z = zn + rotz;
			pitch = newpitch;
			System.err.println( "orbitUp change");
		}
	}


	public void shift( double deltax, double deltay ) {
		double headingR = StrictMath.toRadians(heading);
		double sinH = StrictMath.sin(headingR);
		double cosH = StrictMath.cos(headingR);

		double delx = - ((cosH * deltax + sinH * deltay) * 0.1);
		double dely = ((sinH * deltax - cosH * deltay) * 0.1);

		x += delx;
		y += dely;

		rotx += delx; 
		roty += dely; 
	}


	public void zoomby(double deltavert) {
		if (deltavert < dist()) {
			x += deltavert * dx();
			y += deltavert * dy();
			z -= deltavert * dz();
		}
	}


	public double dist() {
		return StrictMath.sqrt((rotx - x) * (rotx - x)
				+ (roty - y) * (roty - y)
				+ (rotz - z) * (rotz - z));
	}


	public double dx() {
		double value = StrictMath.cos(StrictMath.toRadians(pitch)) *
				StrictMath.sin(StrictMath.toRadians(heading));
		if (StrictMath.abs(value) < org.nlogo.api.Constants.Infinitesimal()) {
			value = 0;
		}
		return value;
	}

	public double dy() {
		double value = StrictMath.cos(StrictMath.toRadians(pitch)) *
				StrictMath.cos(StrictMath.toRadians(heading));
		if (StrictMath.abs(value) < org.nlogo.api.Constants.Infinitesimal()) {
			value = 0;
		}
		return value;
	}

	public double dz() {
		double value = StrictMath.sin(StrictMath.toRadians(pitch));
		if (StrictMath.abs(value) < org.nlogo.api.Constants.Infinitesimal()) {
			value = 0;
		}
		return value;
	}

}
