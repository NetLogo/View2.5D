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
	
    //TODO: fix this.  shouldn't be generic
	protected Observer observer = new Observer(40, 0, 49.5);
	
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

        // Remove back-face rendering

	    //  gl.glCullFace(GL.GL_BACK);
	    //  gl.glEnable(GL.GL_CULL_FACE);
        int StencilBits[] = new int[1];
        gl.glGetIntegerv(GL.GL_STENCIL_BITS, IntBuffer.wrap(StencilBits));
        mainViewport( gl, glu );
    }
    
    protected void mainViewport( GL gl, GLU glu ) {
    	int worldWidth = myViewer.worldWidth;
    	int worldHeight = myViewer.worldHeight;
    	double ratio = worldWidth / worldHeight;
    	
    	gl.glViewport(0, 0, worldWidth, worldHeight);
    	
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        double zClip = Math.max(worldWidth, worldHeight) * 4;

        glu.gluPerspective(45.0f, ratio, 0.1, zClip);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        observer.goHome( myViewer );
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
		}
		
		oldx = nx;
		oldy = ny;
		myCanvas.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent me) {
		oldx = me.getX();
		oldy = me.getY();
		dragging = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		dragging = false;
	}


}