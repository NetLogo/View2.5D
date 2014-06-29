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
		 NetLogoGLU nlGLU = new NetLogoGLU();
		 nlGLU.setQuadric(quadric);
		 
		 Set<String> names = App.app().workspace().world().turtleShapeList().getNames();
		 
		 for (String name : names) {
			 int handle = gl.glGenLists(1);
			 VectorShape vs = (VectorShape)App.app().workspace().world().turtleShapeList().shape( name );
			 compileShape(nlGLU, gl, glu, vs, handle, false );
			 compiledShapes.put(name, handle);
		 }
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
		
		for (TurtleValue tv : ((TurtleView)myViewer).getCopyOfReporterValues()) {
			
				gl.glPushMatrix();
				gl.glTranslated(tv.xcor , tv.ycor, tv.reporterValue);
				
				gl.glColor3f(2.5f, 2.5f, 2.5f);
				gl.glLineWidth(0.1f);
				gl.glBegin (GL.GL_LINES);
				gl.glVertex3i (0, 0, 0);
				gl.glVertex3d (0, 0, -tv.reporterValue);
				gl.glEnd();
				
				
				if ( ((TurtleView)myViewer).viewOptions.showSize() )
					gl.glScaled(tv.size, tv.size, tv.size);
				observer.applyNormal(gl);
				if ( ((TurtleView)myViewer).viewOptions.showColor() )
					gl.glColor3f((float)(4.0*tv.color.getRed()/255f), (float)(4.0*tv.color.getGreen()/255f), (float)(4.0*tv.color.getBlue()/255f));
				if (((TurtleView)myViewer).viewOptions.showShape() ) {
					gl.glScaled(3.0, 3.0, 3.0);
					gl.glCallList(compiledShapes.get(tv.shape));
				}
				else {
					gl.glCallList(sphereDotListHandle);
				}
				
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