package view25d.view.gl;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.glu.GLUtessellator;

import org.nlogo.app.App;
import org.nlogo.gl.render.Polygons;
import org.nlogo.gl.render.Tessellator;
import org.nlogo.shape.VectorShape;

import view25d.view.MouseableGLWindow;
import view25d.view.TurtleValue;
import view25d.view.TurtleView;

public class TurtleGL extends MouseableGLWindow implements GLEventListener {

    GLU glu;

    //handles for compiled GL shapes
    int patchTileListHandle, stemSkyscraperHandle, pinHeadListHandle, axisHeadHandle;
    HashMap<String,Integer> compiledShapes = new HashMap<String, Integer>();

    //GLU quadric for use in making spheres and in setting up NLGLU helper class for turtle shapes
    private GLUquadric quadric;


    public TurtleGL(TurtleView parent) {
        super(parent);
    }

    private void setupCompiliedDisplayLists(GL2 gl) {
        patchTileListHandle = gl.glGenLists(1);
        gl.glNewList(patchTileListHandle, GL2.GL_COMPILE);
        Compilables.PatchTile(gl);
        gl.glEndList();

        quadric = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
        NetLogoGLU nlGLU = new NetLogoGLU();
        nlGLU.setQuadric(quadric);

        stemSkyscraperHandle = gl.glGenLists(1);
        gl.glNewList(stemSkyscraperHandle, GL2.GL_COMPILE);
        //Compilables.box(gl, .4f, 1.0f);
        final int hexSlices = 6;
        Compilables.ThickStem(gl, glu, quadric, 1.0, hexSlices);
        gl.glEndList();

        pinHeadListHandle = gl.glGenLists(1);
        final float radius = 0.4f;
        final int slices = 16;
        gl.glNewList(pinHeadListHandle, GL2.GL_COMPILE);
        Compilables.PinHead(gl, glu, quadric, radius, slices );
        gl.glEndList();

        Set<String> names = scala.collection.JavaConversions.setAsJavaSet(App.app().workspace().world().turtleShapeList().names());
        for (String name : names) {
            int handle = gl.glGenLists(1);
            VectorShape vs = (VectorShape)App.app().workspace().world().turtleShapeList().shape( name );
            compileShape(nlGLU, gl, glu, vs, handle, false );
            compiledShapes.put(name, handle);
        }

        axisHeadHandle = gl.glGenLists(1);
        gl.glNewList(axisHeadHandle, GL2.GL_COMPILE);
        Compilables.AxisHead(gl, glu, quadric, 1.3, slices);
        gl.glEndList();
    }


    public void compileShape(NetLogoGLU nlGLU, GL2 gl, GLU glu,
            VectorShape vShape,
            int index, boolean rotatable) {

        Tessellator tessellator = new Tessellator();
        GLUtessellator tess = GLU.gluNewTess();
        GLU.gluTessCallback(tess, GLU.GLU_TESS_BEGIN_DATA, tessellator);
        GLU.gluTessCallback(tess, GLU.GLU_TESS_EDGE_FLAG_DATA, tessellator);
        GLU.gluTessCallback(tess, GLU.GLU_TESS_VERTEX_DATA, tessellator);
        GLU.gluTessCallback(tess, GLU.GLU_TESS_END_DATA, tessellator);
        GLU.gluTessCallback(tess, GLU.GLU_TESS_COMBINE_DATA, tessellator);
        GLU.gluTessCallback(tess, GLU.GLU_TESS_ERROR_DATA, tessellator);
        GLU.gluTessProperty
            (tess, GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
        gl.glNewList(index, GL2.GL_COMPILE);

        if (!rotatable) {
            gl.glDisable(GL2.GL_LIGHTING);
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
            gl.glEnable(GL2.GL_LIGHTING);
        }
        gl.glDisable(GL2.GL_CULL_FACE);
        gl.glEndList();
    }


    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = (GL2)drawable.getGL();
        glu = new GLU();
        setupCompiliedDisplayLists( gl );

        setupLightingAndViewPort(gl, glu);
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = (GL2)drawable.getGL();
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLineWidth(1.0f);

        gl.glPushMatrix();
        observer.applyPerspective(gl);

        if ( ((TurtleView)myViewer).viewOptions.usePColor() ) {
            for (int i=0; i<myViewer.worldWidth; i++) {
                for (int j = 0; j<myViewer.worldHeight; j++) {
                    gl.glPushMatrix();
                    gl.glTranslated(i + myViewer.minPxcor, j + myViewer.minPycor, 0);
                    Color c = ((TurtleView)myViewer).patchColorMatrix[i][j];
                    float red = ((float)c.getRed())/255.0f;
                    float green = ((float)c.getGreen())/255.0f;
                    float blue = ((float)c.getBlue())/255.0f;
                    setColorAndStandardMaterial(gl, red, green, blue);
                    gl.glCallList(patchTileListHandle);
                    gl.glPopMatrix();
                }
            }
        } else {
            setColorAndStandardMaterial(gl, .1f, .1f, 1f);
            for (int i=0; i<myViewer.worldWidth; i++) {
                for (int j = 0; j<myViewer.worldHeight; j++) {
                    gl.glPushMatrix();
                    gl.glTranslated(i + myViewer.minPxcor, j + myViewer.minPycor, 0);
                    gl.glCallList(patchTileListHandle);
                    gl.glPopMatrix();
                }
            }
        }


        double stemThickness = ((TurtleView)myViewer).viewOptions.getStemThickness();
        for (TurtleValue tv : ((TurtleView)myViewer).getCopyOfReporterValues()) {
            gl.glPushMatrix();
            double zval = myViewer.zScale * tv.reporterValue;
            gl.glTranslated(tv.xcor , tv.ycor, zval);

            double stemColor = tv.stemColor;
            Color c = org.nlogo.api.Color.getColor(stemColor);
            float stemRed = c.getRed() / 255f;
            float stemGreen = c.getGreen() / 255f;
            float stemBlue = c.getBlue() / 255f;
            setColorAndStandardMaterial( gl, stemRed, stemGreen, stemBlue );

            if ( stemThickness == 0.0 ) {
                gl.glLineWidth(0.1f);
                gl.glBegin (GL2.GL_LINES);
                gl.glVertex3i (0, 0, 0);
                gl.glVertex3d (0, 0, -zval);
                gl.glEnd();
            } else {
                gl.glPushMatrix();
                gl.glTranslated(0, 0, -zval);
                gl.glScaled(stemThickness, stemThickness, zval);
                gl.glCallList(stemSkyscraperHandle);
                gl.glPopMatrix();
            }

            float red = 0.8f;
            float green = 0.8f;
            float blue = 0.45f;

            if ( ((TurtleView)myViewer).viewOptions.showSize() )
                gl.glScaled(tv.size, tv.size, tv.size);


            if ( ((TurtleView)myViewer).viewOptions.showColor() ) {
                red = (tv.color.getRed()/255f);
                green = (tv.color.getGreen()/255f);
                blue = (tv.color.getBlue()/255f);
            }

            setColorAndStandardMaterial( gl, red, green, blue);
            if (((TurtleView)myViewer).viewOptions.showShape() ) {
                observer.applyNormal(gl);
                if (stemThickness > 0) { gl.glTranslated(0, 0, stemThickness / 2); }
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
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

}
