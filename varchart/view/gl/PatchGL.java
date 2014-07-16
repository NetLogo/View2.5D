package varchart.view.gl;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import varchart.view.MouseableGLWindow;
import varchart.view.PatchValue;
import varchart.view.PatchView;

public class PatchGL extends MouseableGLWindow implements GLEventListener {
	GLU glu;
    
    //handles for compiled GL shapes
    int patchTileListHandle, patchThickTileListHandle, patchStickListHandle, sphereDotListHandle, altThickPatchHandle, axisHeadHandle, patchDiskTileHandle, patchSkyscraperHandle, pinHeadListHandle;

   // boolean sticks = false;
   // boolean tangents = true;
   boolean colors = true;
    
    public PatchGL(PatchView parent) {
    	super(parent);
    }
    
    private void setupCompiliedDisplayLists(GL gl) {
		 patchTileListHandle = gl.glGenLists(1);
		 gl.glNewList(patchTileListHandle, GL.GL_COMPILE);
		 Compilables.PatchTile(gl);
		 gl.glEndList();
		 
		 patchStickListHandle = gl.glGenLists(1);
		 gl.glNewList(patchStickListHandle, GL.GL_COMPILE);
		 Compilables.PatchStickTile(gl, 10 );
		 gl.glEndList();
		 
		 patchThickTileListHandle = gl.glGenLists(1);
		 gl.glNewList(patchThickTileListHandle, GL.GL_COMPILE);
		 Compilables.ThickPatchTile(gl, .4f, .2f);
		 gl.glEndList();
		 
		 sphereDotListHandle = gl.glGenLists(1);
		 GLUquadric quadr = glu.gluNewQuadric();
		 glu.gluQuadricDrawStyle(quadr, GLU.GLU_FILL);
		 glu.gluQuadricNormals(quadr, GLU.GLU_SMOOTH);
		 final float radius = 0.4f;
		 final int slices = 16;
		 gl.glNewList(sphereDotListHandle, GL.GL_COMPILE);
		 Compilables.PinHead(gl, glu, quadr, radius, slices );
		 gl.glEndList();
		 
		 altThickPatchHandle = gl.glGenLists(1);
		 gl.glNewList(altThickPatchHandle, GL.GL_COMPILE);
		 Compilables.ThickPatchTile(gl, .4f, .15f);
		 Compilables.PinHead(gl, glu, quadr, 0.3f, slices );
		 gl.glEndList();
		 
		 patchSkyscraperHandle = gl.glGenLists(1);
		 gl.glNewList(patchSkyscraperHandle, GL.GL_COMPILE);
		 Compilables.box(gl, .4f, 1.0f);
		 gl.glEndList();
		 
		 axisHeadHandle = gl.glGenLists(1);
		 gl.glNewList(axisHeadHandle, GL.GL_COMPILE);
		 Compilables.AxisHead(gl, glu, quadr, 1.3, slices);
		 gl.glEndList();
		 
		 patchDiskTileHandle = gl.glGenLists(1);
		 gl.glNewList(patchDiskTileHandle, GL.GL_COMPILE);
		 Compilables.DiskPatchTile(gl, glu, quadr, 0.4, slices);
		 gl.glEndList();
		 
		 pinHeadListHandle = gl.glGenLists(1);
		 gl.glNewList(pinHeadListHandle, GL.GL_COMPILE);
		 Compilables.PinHead(gl, glu, quadr, 0.6f, slices );
		 gl.glEndList();
		 
		 glu.gluDeleteQuadric(quadr);
	}
    
    
    @Override
	public void init(GLAutoDrawable drawable) {
    	GL gl = drawable.getGL();
    	glu = new GLU();
    	setupCompiliedDisplayLists( gl );
    	setupLightingAndViewPort(gl, glu);
	}

    
    @Override
	public void display(GLAutoDrawable drawable) {
    	GL gl = drawable.getGL();
    	gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glLoadIdentity();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);	
		gl.glLineWidth(1.0f);

		//gl.glColor3f(1.0f, 0.6f, 5.9f);
		//gl.glColor3f(0.2f, 0.2f, 1.9f);
		
		setColorAndStandardMaterial(gl, 0f, 0.1f, 0.9f);
		gl.glPushMatrix();
		observer.applyPerspective(gl);
		
		
		if (  ((PatchView)myViewer).viewOptions.tangents() || !colors) {
			for (int i=0; i<myViewer.worldWidth; i++) {
				for (int j = 0; j<myViewer.worldHeight; j++) {
					gl.glPushMatrix();
					gl.glTranslated(i + myViewer.minPxcor, j + myViewer.minPycor, -0.01);
					gl.glCallList(patchTileListHandle);
					gl.glPopMatrix();
				}
			}
		}

		for (int i=0; i<myViewer.worldWidth; i++) {
			for (int j = 0; j<myViewer.worldHeight; j++) {
				gl.glPushMatrix();

				PatchValue pv =  ((PatchView)myViewer).reporterValueMatrix[i][j];
				double val = myViewer.zScale * pv.reporterValue;
				Color c = pv.color;
				
				gl.glTranslated(i + myViewer.minPxcor, j + myViewer.minPycor,val);

				if (((PatchView)myViewer).viewOptions.sticks()) {
					//gl.glColor3f(2.5f, 2.5f, 2.5f);
					setColorAndStandardMaterial(gl, 0.6f, 0.6f, 0.6f);
					gl.glLineWidth(0.1f);
					gl.glBegin (GL.GL_LINES);
					gl.glVertex3i (0, 0, 0);
					gl.glVertex3d (0, 0, -val);
					gl.glEnd();
				}
				
				if ( colors ) {
					float red = ((float)c.getRed())/255.0f;
					float green = ((float)c.getGreen())/255.0f;
					float blue = ((float)c.getBlue())/255.0f;
					setColorAndStandardMaterial(gl, red, green, blue);
				} else {
					//gl.glColor3f(1.0f, 3.9f, 0.6f);
					setColorAndStandardMaterial(gl, 0f, 0.9f, 0.1f);
				}
				
				if ( ((PatchView)myViewer).viewOptions.tangents() ) {
					
					if ( j>0 && j<myViewer.worldHeight-1  ) {
						double slopey = (((PatchView)myViewer).reporterValueMatrix[i][j+1].reporterValue - ((PatchView)myViewer).reporterValueMatrix[i][j-1].reporterValue ) / 2.0 ;
						double beta = 180.0 * Math.atan(slopey) / Math.PI;
						gl.glRotated(beta, 1, 0, 0);
					}
					if ( i>0 && i<myViewer.worldWidth-1  ) {
						double slopex = (((PatchView)myViewer).reporterValueMatrix[i+1][j].reporterValue - ((PatchView)myViewer).reporterValueMatrix[i-1][j].reporterValue ) / 2.0 ;
						double alpha = 180.0 * Math.atan(slopex) / Math.PI;
						gl.glRotated(-alpha, 0, 1, 0);
					}
					gl.glCallList(pinHeadListHandle);
					//gl.glCallList(patchDiskTileHandle);
				} else {
					//gl.glColor3f(1.0f, 3.9f, 0.6f);
					//gl.glColor3f(0.1f, 0.8f, 0.1f);
					gl.glTranslated(0, 0, -val);
					gl.glScaled(1, 1, val);
					gl.glCallList(patchSkyscraperHandle);
				}
				gl.glPopMatrix();
			}
		}
		drawAxesIfDragging(gl, axisHeadHandle);
		gl.glPopMatrix();
    }

    
    
    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
    		boolean deviceChanged) {
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
    		int height) {
    }
	
}
