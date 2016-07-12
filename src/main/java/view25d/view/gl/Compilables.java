package view25d.view.gl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public final class Compilables {

	public static void PatchTile( GL2 gl) {
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex2d(-.4, .4);
		gl.glVertex2d(.4, .4);
		gl.glVertex2d(.4, -.4);
		gl.glVertex2d(-.4, -.4);
		gl.glVertex2d(-.4, .4);
		gl.glEnd();
	}


	public static void PatchStickTile( GL2 gl, int stickheight) {
		gl.glColor3f(1.0f, 0.6f, 5.9f);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex2d(-.4, .4);
		gl.glVertex2d(.4, .4);
		gl.glVertex2d(.4, -.4);
		gl.glVertex2d(-.4, -.4);
		gl.glVertex2d(-.4, .4);
		gl.glEnd();

		gl.glColor3f(2.5f, 2.5f, 2.5f);
		gl.glLineWidth(0.1f);
		gl.glBegin (GL2.GL_LINES);
		gl.glVertex3i (0, 0, 0);
		gl.glVertex3i (0, 0, stickheight);
		gl.glEnd();
	}


	//sz should be greater than 0 and less than or equal to  .5
	//depth controls the z-size of the tile
	public static void ThickPatchTile( GL2 gl, final float sz, final float depth) {
		gl.glBegin(GL2.GL_POLYGON);/* f1: front */
		gl.glNormal3f(0,sz,0);
		gl.glVertex3f(-sz,sz,depth);
		gl.glVertex3f(sz,sz,depth);
		gl.glVertex3f(sz,sz,-depth);
		gl.glVertex3f(-sz,sz,-depth);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f2: bottom */
		gl.glNormal3f(0,0,-depth);
		gl.glVertex3f(-sz,sz,-depth);
		gl.glVertex3f(sz,sz,-depth);
		gl.glVertex3f(sz,-sz,-depth);
		gl.glVertex3f(-sz,-sz,-depth);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f3:back */
		gl.glNormal3f(0,-sz,0);
		gl.glVertex3f(-sz,-sz,depth);
		gl.glVertex3f(sz,-sz,depth);
		gl.glVertex3f(sz,-sz,-depth);
		gl.glVertex3f(-sz,-sz,-depth);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f4: top */
		gl.glNormal3f(0,0,depth);
		gl.glVertex3f(-sz,sz,depth);
		gl.glVertex3f(sz,sz,depth);
		gl.glVertex3f(sz,-sz,depth);
		gl.glVertex3f(-sz,-sz,depth);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f5: left */
		gl.glNormal3f(-sz,0,0);
		gl.glVertex3f(-sz,-sz,depth);
		gl.glVertex3f(-sz,sz,depth);
		gl.glVertex3f(-sz,sz,-depth);
		gl.glVertex3f(-sz,-sz,-depth);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f6: right */
		gl.glNormal3f(sz,0,0);
		gl.glVertex3f(sz,-sz,depth);
		gl.glVertex3f(sz,sz,depth);
		gl.glVertex3f(sz,sz,-depth);
		gl.glVertex3f(sz,-sz,-depth);
		gl.glEnd();
	}


	public static void box( GL2 gl, final float basehalf, final float height ) {
		gl.glBegin(GL2.GL_POLYGON);/* f1: front */
		gl.glNormal3f(0,1,0);
		gl.glVertex3f(-basehalf,basehalf,height);
		gl.glVertex3f(basehalf,basehalf,height);
		gl.glVertex3f(basehalf,basehalf,0);
		gl.glVertex3f(-basehalf,basehalf,0);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f2: bottom */
		gl.glNormal3f(0,0,-1);
		gl.glVertex3f(-basehalf,basehalf,0);
		gl.glVertex3f(basehalf,basehalf,0);
		gl.glVertex3f(basehalf,-basehalf,0);
		gl.glVertex3f(-basehalf,-basehalf,0);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f3:back */
		gl.glNormal3f(0,-1,0);
		gl.glVertex3f(-basehalf,-basehalf,height);
		gl.glVertex3f(basehalf,-basehalf,height);
		gl.glVertex3f(basehalf,-basehalf,0);
		gl.glVertex3f(-basehalf,-basehalf,0);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f4: top */
		gl.glNormal3f(0,0,1);
		gl.glVertex3f(-basehalf,basehalf,height);
		gl.glVertex3f(basehalf,basehalf,height);
		gl.glVertex3f(basehalf,-basehalf,height);
		gl.glVertex3f(-basehalf,-basehalf,height);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f5: left */
		gl.glNormal3f(-1,0,0);
		gl.glVertex3f(-basehalf,-basehalf,height);
		gl.glVertex3f(-basehalf,basehalf,height);
		gl.glVertex3f(-basehalf,basehalf,0);
		gl.glVertex3f(-basehalf,-basehalf,0);
		gl.glEnd();
		gl.glBegin(GL2.GL_POLYGON);/* f6: right */
		gl.glNormal3f(1,0,0);
		gl.glVertex3f(basehalf,-basehalf,height);
		gl.glVertex3f(basehalf,basehalf,height);
		gl.glVertex3f(basehalf,basehalf,0);
		gl.glVertex3f(basehalf,-basehalf,0);
		gl.glEnd();

	}


	public static void PinHead( GL2 gl, GLU glu, GLUquadric quadr, float radius, int slices ) {
		//glu.gluSphere(quadr, radius, slices, stacks);
		gl.glTranslated(0, 0, -radius/4);
		glu.gluCylinder(quadr, radius, radius, radius / 2, slices, slices);
	    //gl.glRotatef(180f, 1f, 0f, 0f);
	    glu.gluDisk(quadr, 0f, radius , slices, 1);
	    gl.glTranslated(0, 0, radius/2);
	    glu.gluDisk(quadr, 0f, radius , slices, 1);

	}

	public static void DiskPatchTile(GL2 gl, GLU glu, GLUquadric quadr, final double sz, final int smoothness) {
	    glu.gluDisk(quadr, 0f, sz, smoothness, 1);
	}

	public static void AxisHead(GL2 gl, GLU glu, GLUquadric quadr, final double sz, final int smoothness) {
	    glu.gluCylinder(quadr, sz / 2.0, 0.0, sz, smoothness, smoothness);
	    gl.glRotatef(180f, 1f, 0f, 0f);
	    glu.gluDisk(quadr, 0f, sz / 2, smoothness, 1);
	}

	public static void ThickStem(GL2 gl, GLU glu, GLUquadric quadr, final double sz, final int smoothness) {

		glu.gluCylinder(quadr, sz / 2.0, sz / 2.0, sz, smoothness, smoothness);
		//gl.glTranslated(0, 0, .8*sz);
		//glu.gluCylinder(quadr, sz / 2.0, 0.0, .2*sz, smoothness, smoothness);
	}



}
