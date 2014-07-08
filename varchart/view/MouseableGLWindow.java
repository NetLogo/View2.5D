package varchart.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;


public abstract class MouseableGLWindow implements MouseListener,
		MouseMotionListener {

	protected VarviewWindow myViewer;
	
    //initial values are overwritten immediately - this is the correct initial value for the default model
	protected Observer observer = new Observer(0, 0, 49.5);
	
	protected GLCanvas myCanvas;
	protected int oldx, oldy;
	public boolean dragging = false;
	
	
	public MouseableGLWindow(VarviewWindow parent) {
		myViewer = parent;
	}
	
	public void setCanvas(GLCanvas glCanvas) {
		myCanvas = glCanvas;	
	}

    public void repaintCanvas() {
      myCanvas.repaint();
    }
    
 
    protected void setupLightingAndViewPort(GL gl, GLU glu ) {
    	gl.glShadeModel(GL.GL_SMOOTH);                     // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          // Black Background
        gl.glClearDepth(1.0f);                            // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);              // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);              // The Type Of Depth Testing To Do

        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_FASTEST);

        // Lighting
        gl.glEnable(GL.GL_LIGHTING);

        float direction1[] = {-1.0f, -0.3f, 0.4f, 0.0f};
        float ambient1[] = {0.25f, 0.25f, 0.25f, 1.0f};
        float diffuse1[] = {0.35f, 0.35f, 0.35f, 1.0f};
        float specular1[] = {0.0f, 0.0f, 0.0f, 0.0f};
        
        gl.glLightfv(1, GL.GL_POSITION, FloatBuffer.wrap(direction1)); 
        gl.glLightfv(1, GL.GL_AMBIENT, FloatBuffer.wrap(ambient1));
        gl.glLightfv(1, GL.GL_DIFFUSE, FloatBuffer.wrap(diffuse1));
        gl.glLightfv(1, GL.GL_SPECULAR, FloatBuffer.wrap(specular1));
        gl.glEnable(1);
        
        
        float direction2[] = {1.0f, 0.6f, -0.5f, 0.0f};
        float ambient2[] = {0.25f, 0.25f, 0.25f, 1.0f};
        float diffuse2[] = {0.35f, 0.35f, 0.35f, 1.0f};
        float specular2[] = {0.0f, 0.0f, 0.0f, 0.0f};
        
        gl.glLightfv(2, GL.GL_POSITION, FloatBuffer.wrap(direction2)); 
        gl.glLightfv(2, GL.GL_AMBIENT, FloatBuffer.wrap(ambient2));
        gl.glLightfv(2, GL.GL_DIFFUSE, FloatBuffer.wrap(diffuse2));
        gl.glLightfv(2, GL.GL_SPECULAR, FloatBuffer.wrap(specular2));
        gl.glEnable(2);
      
        // This is necessary for properly rendering scaled objects. Without this, small objects
        // may look too bright, and large objects will look flat.
        gl.glEnable(GL.GL_NORMALIZE);

        // Coloring
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        gl.glEnable(GL.GL_COLOR_MATERIAL);

        // Remove back-face rendering -- temporarily kept in.
	    //  gl.glCullFace(GL.GL_BACK);
	    //  gl.glEnable(GL.GL_CULL_FACE);
        int StencilBits[] = new int[1];
        gl.glGetIntegerv(GL.GL_STENCIL_BITS, IntBuffer.wrap(StencilBits));
        mainViewport( gl, glu );
    }
    
    
    protected void mainViewport( GL gl, GLU glu ) {
    	int worldWidth = myViewer.worldWidth;
    	int worldHeight = myViewer.worldHeight;
    	double ratio = ((double)worldWidth) / ((double)worldHeight);
    	
    	//setting ratio to 1 because we really don't want non-square windows.
    	//TODO: check the logic here, especially on the viewport.
    	ratio = 1.0;
    	gl.glViewport(0, 0, worldWidth, worldHeight);
    	
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        double zClip = Math.max(worldWidth, worldHeight) * 4;

        glu.gluPerspective(45.0f, ratio, 0.1, zClip);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        observer.goHome( myViewer );
    }
    
    
    protected void drawAxesIfDragging( GL gl, int axisHeadHandle ) {
    	if (dragging) {
			
    		double zmax = 10.0 * myViewer.zScale + 1;
			gl.glColor3f(2.5f, .5f, .5f);
			
			gl.glLineWidth(2.4f);
			gl.glBegin (GL.GL_LINES);
			gl.glVertex3i (0, 0, 0);
			gl.glVertex3i (myViewer.maxPxcor + 1, 0, 0);
			gl.glVertex3i (0, 0, 0);
			gl.glVertex3d (0, myViewer.maxPycor + 1, 0);
			gl.glVertex3i (0, 0, 0);
			gl.glVertex3d (0, 0, zmax);
			gl.glVertex3i (0, 0, 0);
			gl.glEnd();
			
			gl.glPushMatrix();
			gl.glTranslated(myViewer.maxPxcor + 1 ,0, 0);
			gl.glRotated(90.0, 0, 1, 0);
			gl.glCallList(axisHeadHandle);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslated(0, myViewer.maxPycor + 1, 0);
			gl.glRotated(-90.0, 1, 0, 0);
			gl.glCallList(axisHeadHandle);
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			gl.glTranslated(0 ,0, zmax);
			gl.glCallList(axisHeadHandle);
			gl.glPopMatrix();
		}
    }
    
    
	@Override
	public void mouseDragged(MouseEvent me) {
		int nx = me.getX();
		int ny = me.getY();
		
		if ( myViewer.getMode() == VarviewWindow.ORBIT ) {
			double thetaX = (nx - oldx) / 2.0;
			double thetaY = (oldy - ny) / 2.0;
			observer.updatePerspective( thetaX, thetaY );
		} else if ( myViewer.getMode() == VarviewWindow.MOVE ) {
			double deltax = (nx - oldx) / 2.0;
			double deltay = (oldy - ny) / 2.0;
			observer.shift(deltax, deltay);
		} else if ( myViewer.getMode() == VarviewWindow.ZOOM ) {
			double deldist =  (oldy - ny);
			observer.zoomby(deldist);
		} else if (myViewer.getMode() == VarviewWindow.SCALEZ ) {
			double deldist = (oldy - ny) / 30.0;
			myViewer.zoomZby(deldist);
		}
		
		oldx = nx;
		oldy = ny;
		myCanvas.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {		
	}

	@Override
	public void mousePressed(MouseEvent me) {
		oldx = me.getX();
		oldy = me.getY();
		dragging = true;
		myCanvas.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		dragging = false;
		myCanvas.repaint();
	}

}
