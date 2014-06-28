package varchart.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLCanvas;


public abstract class MouseableWindow implements MouseListener,
		MouseMotionListener {

	protected VarviewWindow myViewer;
	
    //TODO: fix this.  shouldn't be generic
	protected Observer observer = new Observer(0, 0, 49.5);
	
	protected GLCanvas myCanvas;
	protected int oldx, oldy;
	public boolean dragging = false;
	
	
	public MouseableWindow(VarviewWindow parent) {
		myViewer = parent;
	}
	
	public void setCanvas(GLCanvas glCanvas) {
		myCanvas = glCanvas;	
	}

    public void repaintCanvas() {
      myCanvas.repaint();
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
			double delx = -2 * (nx - oldx) / observer.dist();
			double dely = -2 * (oldy - ny) / observer.dist();
			if ( delx > myViewer.minPxcor  &&  delx < myViewer.maxPxcor  && dely > myViewer.minPycor && dely < myViewer.maxPycor) {
				observer.x += delx;
				observer.y += dely;
				observer.rotx += delx;
				observer.roty += dely;
			}
		} else if ( myViewer.getMode() == VarviewWindow.ZOOM ) {
			double deldist = -1 * (oldy - ny);
			observer.zoomby(myViewer, deldist);
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
