package varchart.view.gl;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


import varchart.view.MeshView;

public class MeshGL implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	MeshView myViewer;
	
	GLU glu;
    GLCanvas myCanvas;

    double xPosition = 0.0;
    double yPosition = 0.0;
    double zPosition = 0.0;
    double xangle = 0.0;
    double yangle = 0.0;
    double zbase = 0.0;

    Point dragOrigin;
    
    public MeshGL(MeshView parent) {
    	myViewer = parent;
    }
    
	public void setCanvas(GLCanvas glCanvas) {
		myCanvas = glCanvas;	
	}

    public void repaintCanvas() {
      myCanvas.repaint();
    }
	
   
	@Override
	public void init(GLAutoDrawable glAutoDrawable) {
		glu = new GLU();
		zbase = StrictMath.max( myViewer.worldHeight, myViewer.worldWidth );
		zbase = 1.5 * zbase;
		zPosition = zbase;
	}
	
	
	@Override
	public void display(GLAutoDrawable glAutoDrawable) {
		
		int worldWidth = myViewer.worldWidth;
		int worldHeight = myViewer.worldHeight;
		
		GL gl = glAutoDrawable.getGL();
		//GLUT glut = new GLUT();

		//set up the camera
		gl.glMatrixMode( GL.GL_PROJECTION );
		gl.glLoadIdentity();
		
		double w = glAutoDrawable.getWidth();
		double h = glAutoDrawable.getHeight();
		double aspect = w/h;

		double distToCorners = StrictMath.sqrt( (zbase + zPosition)*(zbase + zPosition)  +  StrictMath.max(worldHeight, worldWidth)*StrictMath.max(worldHeight, worldWidth) );
		glu.gluPerspective(60.0, aspect, 2.0, distToCorners * 3); //100.0);


		gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glLoadIdentity();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);


		glu.gluLookAt(xPosition,yPosition,zPosition,
				0, 0, -10,
				0, 1, 0);

		//set up lighting for the world
		float positiona[] = {0f, 15f, 30f, 0f};
		FloatBuffer position = FloatBuffer.wrap(positiona);
		gl.glLightfv( GL.GL_LIGHT0, GL.GL_POSITION, position);

		float diffusea[] = {.7f, .7f, .7f, 0f};
		FloatBuffer diffuse = FloatBuffer.wrap(diffusea);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse);

		float ambienta[] = {.2f, .2f, .2f, 0f};
		FloatBuffer ambient = FloatBuffer.wrap(ambienta);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient);

		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_FASTEST);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);


		double stw = -1 * worldWidth / 2;
		double sth = -1 * worldHeight / 2;


		float material0a[] = { 0.01f, 0.6f, 0.3f, 1.0f };
		FloatBuffer material0 = FloatBuffer.wrap(material0a);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, material0);
		gl.glBegin(GL.GL_QUADS);
		gl.glLineWidth(3.0f);

		for(int i = 1; i<worldWidth; i++) {
			for (int j = 1; j<worldHeight; j++){
				gl.glVertex3d(stw + i - 1,sth + j - 1,myViewer.reporterValueMatrix[i-1][j-1]);
				gl.glVertex3d(stw + i,sth + j - 1,myViewer.reporterValueMatrix[i][j-1]);
				gl.glVertex3d(stw + i,sth + j,myViewer.reporterValueMatrix[i][j]);
				gl.glVertex3d(stw + i-1,sth + j,myViewer.reporterValueMatrix[i-1][j]);
			}
		}

		gl.glEnd();


		//float material1a[] = { 0.01f, 0.01f, 0.2f, 1.0f };
		float material1a[] = { 0.9f, 0.9f, 0.7f, 1.0f };
		FloatBuffer material1 = FloatBuffer.wrap(material1a);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, material1);
		double epsilon = .05;
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINES);

		for(int i = 1; i<worldWidth; i++) {
			for (int j = 1; j<worldHeight; j++) {
				gl.glVertex3d(stw + i - 1,sth + j - 1,myViewer.reporterValueMatrix[i-1][j-1]  + epsilon);
				gl.glVertex3d(stw + i,sth + j - 1,myViewer.reporterValueMatrix[i][j-1] + epsilon);
				gl.glVertex3d(stw + i,sth + j,myViewer.reporterValueMatrix[i][j] + epsilon);
				gl.glVertex3d(stw + i - 1,sth + j,myViewer.reporterValueMatrix[i][j] + epsilon);
				gl.glVertex3d(stw + i - 1,sth + j - 1,myViewer.reporterValueMatrix[i-1][j-1]  + epsilon);	
			}
		}
		gl.glEnd();
		
		
		gl.glBegin(GL.GL_LINES);

		for(int i = 1; i<worldWidth; i++) {
			for (int j = 1; j<worldHeight; j++) {
				gl.glVertex3d(stw + i - 1,sth + j - 1,0);
				gl.glVertex3d(stw + i,sth + j - 1,0);
				gl.glVertex3d(stw + i,sth + j,0);
				gl.glVertex3d(stw + i - 1,sth + j,0);
				gl.glVertex3d(stw + i - 1,sth + j - 1,0);
			}
		}
		gl.glEnd();
	}

	
	@Override
    public void mousePressed(MouseEvent e) {
    	dragOrigin = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	Point currentPoint = e.getPoint();
    	double deltax =  currentPoint.getX() - dragOrigin.getX();
    	double deltay =  currentPoint.getY() - dragOrigin.getY();
    	xPosition -= (int)deltax;
    	yPosition += (int)deltay;

    	recalculatePOV();
    	dragOrigin = currentPoint;
    	myCanvas.repaint();
    }

    public void recalculatePOV() {
    	if (xPosition > 1.48 * myViewer.worldWidth) { xPosition = 1.48 * myViewer.worldWidth; }
    	if (xPosition < -1.48 * myViewer.worldWidth) { xPosition = -1.48 * myViewer.worldWidth; }
    	if (yPosition > 0) { yPosition = 0; }
    	if (yPosition < -1.48 * myViewer.worldHeight) { yPosition = -1.48 * myViewer.worldHeight; }

    	zPosition = Math.sqrt( zbase * zbase - xPosition * xPosition - yPosition * yPosition);
    }


	
    @Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		
	}
	
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		
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
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}


}
