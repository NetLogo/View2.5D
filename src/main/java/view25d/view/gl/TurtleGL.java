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

import view25d.view.MouseableGLWindow;
import view25d.view.LinkValue;
import view25d.view.TurtleValue;
import view25d.view.TurtleView;

public class TurtleGL extends MouseableGLWindow implements GLEventListener {
    //handles for compiled GL shapes
    int patchTileListHandle, stemSkyscraperHandle, pinHeadListHandle, axisHeadHandle;

    //GLU quadric for use in making spheres and in setting up NLGLU helper class for turtle shapes
    protected GLUquadric quadric;

    public TurtleGL(TurtleView parent) {
        super(parent);
    }

    private void setupCompiledDisplayLists(GL2 gl) {
        patchTileListHandle = gl.glGenLists(1);
        gl.glNewList(patchTileListHandle, GL2.GL_COMPILE);
        Compilables.PatchTile(gl);
        gl.glEndList();

        quadric = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
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

        compileShapes(gl, compiledShapes, false);

        axisHeadHandle = gl.glGenLists(1);
        gl.glNewList(axisHeadHandle, GL2.GL_COMPILE);
        Compilables.AxisHead(gl, glu, quadric, 1.3, slices);
        gl.glEndList();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = (GL2)drawable.getGL();
        if (areShapesStale) {
          compileShapes(gl, compiledShapes, false);
          setDeletedShapesToDefaultShape(compiledShapes);
          areShapesStale = false;
        }

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

        // Draw Turtle Stems
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

        // Draw links as lines
        for (LinkValue lv : ((TurtleView)myViewer).linkValues) {
          double zval1 = 0.0;
          double zval2 = 0.0;
          if (((TurtleView)myViewer).viewOptions.linksAreThreeD()) {
            zval1 = myViewer.zScale * lv.zcor1;
            zval2 = myViewer.zScale * lv.zcor2;
          }

          Color c = lv.color;
          float linkRed = c.getRed() / 255f;
          float linkGreen = c.getGreen() / 255f;
          float linkBlue = c.getBlue() / 255f;
          setColorAndStandardMaterial( gl, linkRed, linkGreen, linkBlue );

          // lines for links need a minimum thickness to be visible
          if ( lv.thickness == 0.0 ) {
            lv.thickness = 0.1f;
          }

          gl.glLineWidth((float)lv.thickness);
          gl.glBegin (GL2.GL_LINES);
          gl.glVertex3d (lv.xcor1, lv.ycor1, zval1);
          gl.glVertex3d (lv.xcor2, lv.ycor2, zval2);
          gl.glEnd();
        }

        drawAxesIfDragging( gl, axisHeadHandle );
        gl.glPopMatrix();
    }

    @Override
    // required by Interface GLEventListener
    public void init(GLAutoDrawable drawable) {
      compiledShapes = new HashMap<String, Integer>();
      GL2 gl = (GL2)drawable.getGL();
      glu = new GLU();
      setupCompiledDisplayLists( gl );
      setupLightingAndViewPort(gl, glu);
    }

    @Override
    // required by Interface GLEventListener
    public void dispose(GLAutoDrawable drawable) {}

    // required by Interface GLEventListener
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }
}
