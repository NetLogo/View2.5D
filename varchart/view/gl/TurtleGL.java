package varchart.view.gl;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.GLUtessellator;

import org.nlogo.app.App;
import org.nlogo.gl.render.Polygons;
import org.nlogo.gl.render.Tessellator;
import org.nlogo.shape.VectorShape;

import varchart.view.MouseableGLWindow;
import varchart.view.TurtleValue;
import varchart.view.TurtleView;

public class TurtleGL extends MouseableGLWindow implements GLEventListener {

	GLU glu;
    
    //handles for compiled GL shapes
    int patchTileListHandle, pinHeadListHandle, axisHeadHandle;
    HashMap<String,Integer> compiledShapes = new HashMap<String, Integer>();
   
    //GLU quadric for use in making spheres and in setting up NLGLU helper class for turtle shapes
	private GLUquadric quadric;
	
    
    public TurtleGL(TurtleView parent) {
    	super(parent);
    }
    
    private void setupCompiliedDisplayLists(GL gl) {
    	 patchTileListHandle = gl.glGenLists(1);
		 gl.glNewList(patchTileListHandle, GL.GL_COMPILE);
		 Compilables.PatchTile(gl);
		 gl.glEndList();
		 		 
		 quadric = glu.gluNewQuadric();
		 glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		 glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
		 NetLogoGLU nlGLU = new NetLogoGLU();
		 nlGLU.setQuadric(quadric);
		 
		 pinHeadListHandle = gl.glGenLists(1);
		 final float radius = 0.4f;
		 final int slices = 16;
		 gl.glNewList(pinHeadListHandle, GL.GL_COMPILE);
		 Compilables.PinHead(gl, glu, quadric, radius, slices );
		 gl.glEndList();
		 
		 Set<String> names = App.app().workspace().world().turtleShapeList().getNames();
		 for (String name : names) {
			 int handle = gl.glGenLists(1);
			 VectorShape vs = (VectorShape)App.app().workspace().world().turtleShapeList().shape( name );
			 compileShape(nlGLU, gl, glu, vs, handle, false );
			 compiledShapes.put(name, handle);
		 }
		 
		 axisHeadHandle = gl.glGenLists(1);
		 gl.glNewList(axisHeadHandle, GL.GL_COMPILE);
		 Compilables.AxisHead(gl, glu, quadric, 1.3, slices);
		 gl.glEndList();		 
	}
    
 
    public void compileShape(NetLogoGLU nlGLU, GL gl, GLU glu,
    		VectorShape vShape,
    		int index, boolean rotatable) {

    	Tessellator tessellator = new Tessellator();
    	GLUtessellator tess = glu.gluNewTess();
    	glu.gluTessCallback(tess, GLU.GLU_TESS_BEGIN_DATA, tessellator);
    	glu.gluTessCallback(tess, GLU.GLU_TESS_EDGE_FLAG_DATA, tessellator);
    	glu.gluTessCallback(tess, GLU.GLU_TESS_VERTEX_DATA, tessellator);
    	glu.gluTessCallback(tess, GLU.GLU_TESS_END_DATA, tessellator);
    	glu.gluTessCallback(tess, GLU.GLU_TESS_COMBINE_DATA, tessellator);
    	glu.gluTessCallback(tess, GLU.GLU_TESS_ERROR_DATA, tessellator);
    	glu.gluTessProperty
    	(tess, GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
    	gl.glNewList(index, GL.GL_COMPILE);

    	if (!rotatable) {
    		gl.glDisable(GL.GL_LIGHTING);
    	}

    	// render each element in this shape
    	List<org.nlogo.shape.Element> elements = vShape.getElements();
    	for (int i = 0; i < elements.size(); i++) {
    		org.nlogo.shape.Element element = elements.get(i);

    		if (element instanceof org.nlogo.shape.Rectangle) {
    			nlGLU.renderRectangle
    			(gl, i, (org.nlogo.shape.Rectangle) element, rotatable);
    		} else if (element instanceof org.nlogo.shape.Polygon) {
    			Polygons.renderPolygon(gl, glu, tessellator, tess, i,
    					(org.nlogo.shape.Polygon) element, rotatable, false);  //is3D = false
    		} else if (element instanceof org.nlogo.shape.Circle) {
    			nlGLU.renderCircle(gl, glu, i,
    					(org.nlogo.shape.Circle) element, rotatable);
    		} else if (element instanceof org.nlogo.shape.Line) {
    			nlGLU.renderLine(gl, i,
    					(org.nlogo.shape.Line) element);
    		} else if (element instanceof org.nlogo.shape.Curve) {
    			throw new IllegalStateException();
    		}
    	}

    	if (!rotatable) {
    		gl.glEnable(GL.GL_LIGHTING);
    	}
    	gl.glDisable(GL.GL_CULL_FACE);
    	gl.glEndList();
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

    	gl.glPushMatrix();
    	observer.applyPerspective(gl);
    	
    	setColorAndStandardMaterial(gl, .1f, .1f, 1f);
    	for (int i=0; i<myViewer.worldWidth; i++) {
    		for (int j = 0; j<myViewer.worldHeight; j++) {
    			gl.glPushMatrix();
    			gl.glTranslated(i - myViewer.worldWidth/2 , j - myViewer.worldHeight/2, -0.01);
    			gl.glCallList(patchTileListHandle);
    			gl.glPopMatrix();
    		}
    	}

    	for (TurtleValue tv : ((TurtleView)myViewer).getCopyOfReporterValues()) {
    		gl.glPushMatrix();
    		double zval = myViewer.zScale * tv.reporterValue;
    		gl.glTranslated(tv.xcor , tv.ycor, zval);

    		setColorAndStandardMaterial( gl, .5f, .5f, .5f);
    		gl.glLineWidth(0.1f);
    		gl.glBegin (GL.GL_LINES);
    		gl.glVertex3i (0, 0, 0);
    		gl.glVertex3d (0, 0, -zval);
    		gl.glEnd();

    		float red = 0.6f;
    		float green = 0.6f;
    		float blue = 0.6f;
    	
    		if ( ((TurtleView)myViewer).viewOptions.showSize() )
    			gl.glScaled(tv.size, tv.size, tv.size);
    		
    		
    		if ( ((TurtleView)myViewer).viewOptions.showColor() ) {
    			red = (float)(tv.color.getRed()/255f);
    			green = (float)(tv.color.getGreen()/255f);
    			blue = (float)(4.0*tv.color.getBlue()/255f);
    		}
    		
    		setColorAndStandardMaterial( gl, red, green, blue);
    		if (((TurtleView)myViewer).viewOptions.showShape() ) {
    			observer.applyNormal(gl);
    			gl.glScaled(3.0, 3.0, 3.0);
    			gl.glCallList(compiledShapes.get(tv.shape));
    		}
    		else {
    			gl.glCallList(pinHeadListHandle);
    		}

    		gl.glPopMatrix();
    	}
    	drawAxesIfDragging( gl, axisHeadHandle );
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