package varchart.view.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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
import org.nlogo.shape.Polygon;
import org.nlogo.shape.VectorShape;

import varchart.view.MouseableWindow;
import varchart.view.TurtleValue;
import varchart.view.TurtleView;

public class TurtleGL extends MouseableWindow implements GLEventListener {

	GLU glu;
    
    //handles for compiled GL shapes
    int patchTileListHandle, sphereDotListHandle;
    HashMap<String,Integer> compiledShapes = new HashMap<String, Integer>();
   
    
	private GLUquadric quadric;
	private final int SMOOTHNESS = 16;
	private boolean is3D = false;
    
    public TurtleGL(TurtleView parent) {
    	super(parent);
    }
    
    
    private void setupCompiliedDisplayLists(GL gl) {
    	 patchTileListHandle = gl.glGenLists(1);
		 gl.glNewList(patchTileListHandle, GL.GL_COMPILE);
		 Compilables.PatchTile(gl);
		 gl.glEndList();
		 
		 sphereDotListHandle = gl.glGenLists(1);
		 GLUquadric quadr = glu.gluNewQuadric();
		 glu.gluQuadricDrawStyle(quadr, GLU.GLU_FILL);
		 glu.gluQuadricNormals(quadr, GLU.GLU_SMOOTH);
		 final float radius = 0.4f;
		 final int slices = 16;
		 final int stacks = 16;
		 
		 gl.glNewList(sphereDotListHandle, GL.GL_COMPILE);
		 Compilables.Sphere(gl, glu, quadr, radius, slices, stacks);
		 gl.glEndList();
		 
		 
		 quadric = glu.gluNewQuadric();
		 glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
		 
		 Set<String> names = App.app().workspace().world().turtleShapeList().getNames();
		 
		 for (String name : names) {
			 int handle = gl.glGenLists(1);
			 VectorShape vs = (VectorShape)App.app().workspace().world().turtleShapeList().shape( name );
			 compileShape(gl, glu, vs, handle, false );
			 compiledShapes.put(name, handle);
		 }
	}
    
    
    private void renderRectangle(GL gl, int offset,
    		org.nlogo.shape.Rectangle rect,
    		boolean rotatable) {
    	float zDepth = 0.01f + offset * 0.0001f;

    	if (!rect.marked()) {
    		float[] rgb = rect.getColor().getRGBColorComponents(null);
    		gl.glPushAttrib(GL.GL_CURRENT_BIT);
    		gl.glColor3fv(java.nio.FloatBuffer.wrap(rgb));
    	}

    	java.awt.Point[] corners = rect.getCorners();

    	float coords[] =
    		{corners[0].x * .001f - 0.15f,
    			(300 - corners[0].y) * .001f - 0.15f,
    			corners[1].x * .001f - 0.15f,
    			(300 - corners[1].y) * .001f - 0.15f};

    	// no need to "pancake" if it is always facing the user
    	renderRectangle
    	(gl, coords[0], coords[2], coords[3], coords[1],
    			-zDepth, zDepth, !rect.filled(), rotatable);

    	if (!rect.marked()) {
    		gl.glPopAttrib();
    	}
    }

    void renderRectangle(GL gl, float x0, float x1, float y0, float y1,
    		float z0, float z1, boolean filled, boolean rotatable) {
    	renderRectangularPrism(gl, x0, x1, y0, y1,
    			z0, z1, filled, false, rotatable);
    }

    public void renderRectangularPrism(GL gl, float left, float right,
    		float back, float front,
    		float bottom, float top, boolean hollow,
    		boolean hasBottom, boolean hasSides) {

    	if (hollow)
    		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);

    	gl.glBegin(GL.GL_QUADS);

    	// top
    	gl.glNormal3f(0f, 0f, 1f);
    	gl.glVertex3f(left, front, top);
    	gl.glVertex3f(left, back, top);
    	gl.glVertex3f(right, back, top);
    	gl.glVertex3f(right, front, top);

    	// bottom
    	if (hasBottom) {
    		gl.glNormal3f(0f, 0f, -1f);
    		gl.glVertex3f(left, front, bottom);
    		gl.glVertex3f(right, front, bottom);
    		gl.glVertex3f(right, back, bottom);
    		gl.glVertex3f(left, back, bottom);
    	}

    	gl.glEnd();

    	if (hollow) {
    		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
    		gl.glDisable(GL.GL_CULL_FACE);
    	}

    	if (hasSides) {

    		gl.glBegin(GL.GL_QUADS);

    		// left
    		gl.glNormal3f(-1f, 0f, 0f);
    		gl.glVertex3f(left, front, top);
    		gl.glVertex3f(left, front, bottom);
    		gl.glVertex3f(left, back, bottom);
    		gl.glVertex3f(left, back, top);

    		// front
    		gl.glNormal3f(0f, 1f, 0f);
    		gl.glVertex3f(right, front, top);
    		gl.glVertex3f(right, front, bottom);
    		gl.glVertex3f(left, front, bottom);
    		gl.glVertex3f(left, front, top);

    		// right
    		gl.glNormal3f(1f, 0f, 0f);
    		gl.glVertex3f(right, back, top);
    		gl.glVertex3f(right, back, bottom);
    		gl.glVertex3f(right, front, bottom);
    		gl.glVertex3f(right, front, top);

    		// back
    		gl.glNormal3f(0f, -1f, 0f);
    		gl.glVertex3f(left, back, top);
    		gl.glVertex3f(left, back, bottom);
    		gl.glVertex3f(right, back, bottom);
    		gl.glVertex3f(right, back, top);

    		gl.glEnd();
    	}

    	if (hollow)
    		gl.glEnable(GL.GL_CULL_FACE);
    }


    private void renderCircle(GL gl, GLU glu, int offset,
    		org.nlogo.shape.Circle circle,
    		boolean rotatable) {
    	float zDepth = 0.01f + offset * 0.0001f;

    	if (!circle.marked()) {
    		float[] rgb = circle.getColor().getRGBColorComponents(null);
    		gl.glPushAttrib(GL.GL_CURRENT_BIT);
    		gl.glColor3fv(java.nio.FloatBuffer.wrap(rgb));
    	}

    	// for now assume it is a circle

    	float radius = (float) circle.getBounds().getWidth() * .0005f;
    	float origin[] =
    		{(float) circle.getOrigin().getX() * .001f - 0.15f,
    			(300 - (float) circle.getOrigin().getY()) * .001f - 0.15f};

    	gl.glPushMatrix();

    	if (!circle.filled()) {
    		glu.gluQuadricDrawStyle(quadric, GLU.GLU_SILHOUETTE);
    	}

    	// no need to "pancake" if it is always facing the user
    	if (rotatable) {
    		if (!circle.filled()) {
    			glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
    			gl.glDisable(GL.GL_CULL_FACE);
    		}

    		gl.glTranslatef(origin[0], origin[1], -zDepth);
    		glu.gluCylinder(quadric, radius, radius, (2 * zDepth),
    				SMOOTHNESS, 1);

    		if (!circle.filled()) {
    			gl.glEnable(GL.GL_CULL_FACE);
    			glu.gluQuadricDrawStyle(quadric, GLU.GLU_SILHOUETTE);
    		}

    		gl.glTranslatef(0.0f, 0.0f, (2 * zDepth));
    	} else {
    		gl.glTranslatef(origin[0], origin[1], zDepth);
    	}

    	renderCircle(gl, glu, 0.0f, radius, zDepth, rotatable);

    	if (!circle.filled()) {
    		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
    	}

    	gl.glPopMatrix();

    	if (!circle.marked()) {
    		gl.glPopAttrib();
    	}
    }

    private void renderCircle(GL gl, GLU glu,
    		float innerRadius, float outerRadius, float zDepth,
    		boolean rotatable) {
    	glu.gluDisk(quadric, innerRadius, outerRadius, SMOOTHNESS, 1);
    	if(is3D && rotatable) {
    		gl.glRotatef(180f, 1f, 0f, 0f);
    		gl.glTranslatef(0f, 0f, zDepth * 2);
    		glu.gluDisk(quadric, innerRadius, outerRadius, SMOOTHNESS, 1);
    	}
    }

    private void renderLine(GL gl, int offset, org.nlogo.shape.Line line) {
    	float zDepth = offset * 0.0001f;

    	if (!line.marked()) {
    		float[] rgb = line.getColor().getRGBColorComponents(null);
    		gl.glPushAttrib(GL.GL_CURRENT_BIT);
    		gl.glColor3fv(java.nio.FloatBuffer.wrap(rgb));
    	}

    	java.awt.Point start = line.getStart();
    	java.awt.Point end = line.getEnd();
    	float coords[] =
    		{start.x * .001f - 0.15f, (300 - start.y) * .001f - 0.15f,
    			end.x * .001f - 0.15f, (300 - end.y) * .001f - 0.15f};

    	gl.glBegin(GL.GL_LINES);
    	gl.glNormal3f(0.0f, 0.0f, -1.0f);

    	// top line
    	gl.glVertex3f(coords[0], coords[1], zDepth);
    	gl.glVertex3f(coords[2], coords[3], zDepth);
    	gl.glEnd();

    	// we don't "pancake" line because we don't really need to,
    	// by default the line tilts to always show the user
    	// a flat face, and this way we can scale all lines
    	// by line thickness and it will look good and it will be fast. ev 4/5/06

    	if (!line.marked()) {
    		gl.glPopAttrib();
    	}
    }

    public void renderPolygon( GL gl, GLU glu, int offset, Polygon poly, boolean rotatable ) {
    	float zDepth = 0.01f + offset * 0.0001f;
    	List<Integer> xcoords = poly.getXcoords();
    	List<Integer> ycoords = poly.getYcoords();
    	//remove rotatability
    	renderPolygon2D(gl, glu, xcoords, ycoords, zDepth, false);
    }

    public void renderPolygon2D(GL gl, GLU glu, 
    		List<Integer> xcoords,
    		List<Integer> ycoords,
    		float zDepth, boolean rotatable) {

    	GLUtessellator tess = glu.gluNewTess();
    	glu.gluTessBeginPolygon(tess, null);
    	glu.gluTessBeginContour(tess);
    	for(int i = 0; i< xcoords.size(); i++) {
    		double coords[] = {
    				xcoords.get(i).intValue() * .001 - 0.15,
    				(300 - ycoords.get(i).intValue()) * .001 - 0.15,
    				zDepth};
    		glu.gluTessVertex(tess, coords, 0, coords);
    	}
    	glu.gluTessEndContour(tess);
    	glu.gluTessEndPolygon(tess);
    }


    public void compileShape(GL gl, GLU glu,
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
    			renderRectangle
    			(gl, i, (org.nlogo.shape.Rectangle) element, rotatable);
    		} else if (element instanceof org.nlogo.shape.Polygon) {
    			Polygons.renderPolygon(gl, glu, tessellator, tess, i,
    					(org.nlogo.shape.Polygon) element, rotatable, is3D);
    			//renderPolygon(gl, glu, i,
    			//		(org.nlogo.shape.Polygon) element, rotatable);
    		} else if (element instanceof org.nlogo.shape.Circle) {
    			renderCircle(gl, glu, i,
    					(org.nlogo.shape.Circle) element, rotatable);
    		} else if (element instanceof org.nlogo.shape.Line) {
    			renderLine(gl, i,
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
    
   
    private void mainViewport( GL gl ) {
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
	public void init(GLAutoDrawable drawable) {
    	GL gl = drawable.getGL();
    	glu = new GLU();
    	setupCompiliedDisplayLists( gl );
    	
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
        mainViewport( gl );
	}

    
    @Override
	public void display(GLAutoDrawable drawable) {
    	GL gl = drawable.getGL();
    	gl.glMatrixMode( GL.GL_MODELVIEW );
		gl.glLoadIdentity();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);	
		gl.glLineWidth(1.0f);

		gl.glColor3f(1.0f, 0.6f, 5.9f);
		gl.glPushMatrix();
		
		observer.applyPerspective(gl);
		for (int i=0; i<myViewer.worldWidth; i++) {
			for (int j = 0; j<myViewer.worldHeight; j++) {
				gl.glPushMatrix();
				gl.glTranslated(i - myViewer.worldWidth/2 , j - myViewer.worldHeight/2,0);
				 gl.glCallList(patchTileListHandle);
				gl.glPopMatrix();
			}
		}
		
		//ArrayList<TurtleValue> copyOfValues = new ArrayList<TurtleValue>( ((TurtleView)myViewer).turtleReporterValues.size() );
		//copyOfValues.addAll( ((TurtleView)myViewer).turtleReporterValues );
		for (TurtleValue tv : ((TurtleView)myViewer).getCopyOfReporterValues()) {
			
				gl.glPushMatrix();
				gl.glTranslated(tv.xcor , tv.ycor, tv.reporterValue);
				
				gl.glColor3f(2.5f, 2.5f, 2.5f);
				gl.glLineWidth(0.1f);
				gl.glBegin (GL.GL_LINES);
				gl.glVertex3i (0, 0, 0);
				gl.glVertex3d (0, 0, -tv.reporterValue);
				gl.glEnd();
				
				gl.glScaled(1.4, 1.4, 1.4);
				if ( ((TurtleView)myViewer).showSize )
					gl.glScaled(tv.size, tv.size, tv.size);
				observer.applyNormal(gl);
				if ( ((TurtleView)myViewer).showColor )
					gl.glColor3f((float)(tv.color.getRed()/255f), (float)tv.color.getGreen()/255f, (float)tv.color.getBlue()/255f);
				if (((TurtleView)myViewer).showShape )
					gl.glCallList(compiledShapes.get(tv.shape));
				else
					gl.glCallList(sphereDotListHandle);
				
				gl.glPopMatrix();
		}
		
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