package view25d.view.gl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import view25d.view.MouseableGLWindow;
import view25d.view.LinkValue;
import view25d.view.PatchValue;
import view25d.view.PatchView;
import view25d.view.TurtleValue;

public class PatchGL extends MouseableGLWindow implements GLEventListener {
    //handles for compiled GL shapes
    int patchTileListHandle, patchThickTileListHandle, patchStickListHandle, sphereDotListHandle, altThickPatchHandle, axisHeadHandle, patchDiskTileHandle, patchSkyscraperHandle, pinHeadListHandle;

    //GLU quadric for use in making spheres and in setting up NLGLU helper class for turtle shapes
    protected GLUquadric quadric;

    // boolean sticks = false;
    // boolean tangents = true;
    boolean colors = true;

    public PatchGL(PatchView parent) {
        super(parent);
    }

    private void setupCompiledDisplayLists(GL2 gl) {
        patchTileListHandle = gl.glGenLists(1);
        gl.glNewList(patchTileListHandle, GL2.GL_COMPILE);
        Compilables.PatchTile(gl);
        gl.glEndList();

        patchStickListHandle = gl.glGenLists(1);
        gl.glNewList(patchStickListHandle, GL2.GL_COMPILE);
        Compilables.PatchStickTile(gl, 10 );
        gl.glEndList();

        patchThickTileListHandle = gl.glGenLists(1);
        gl.glNewList(patchThickTileListHandle, GL2.GL_COMPILE);
        Compilables.ThickPatchTile(gl, .4f, .2f);
        gl.glEndList();

        sphereDotListHandle = gl.glGenLists(1);
        GLUquadric quadr = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(quadr, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadr, GLU.GLU_SMOOTH);
        final float radius = 0.4f;
        final int slices = 16;
        gl.glNewList(sphereDotListHandle, GL2.GL_COMPILE);
        Compilables.PinHead(gl, glu, quadr, radius, slices );
        gl.glEndList();

        altThickPatchHandle = gl.glGenLists(1);
        gl.glNewList(altThickPatchHandle, GL2.GL_COMPILE);
        Compilables.ThickPatchTile(gl, .4f, .15f);
        Compilables.PinHead(gl, glu, quadr, 0.3f, slices );
        gl.glEndList();

        patchSkyscraperHandle = gl.glGenLists(1);
        gl.glNewList(patchSkyscraperHandle, GL2.GL_COMPILE);
        Compilables.box(gl, .4f, 1.0f);
        gl.glEndList();

        axisHeadHandle = gl.glGenLists(1);
        gl.glNewList(axisHeadHandle, GL2.GL_COMPILE);
        Compilables.AxisHead(gl, glu, quadr, 1.3, slices);
        gl.glEndList();

        patchDiskTileHandle = gl.glGenLists(1);
        gl.glNewList(patchDiskTileHandle, GL2.GL_COMPILE);
        Compilables.DiskPatchTile(gl, glu, quadr, 0.4, slices);
        gl.glEndList();

        pinHeadListHandle = gl.glGenLists(1);
        gl.glNewList(pinHeadListHandle, GL2.GL_COMPILE);
        Compilables.PinHead(gl, glu, quadr, 0.6f, slices );
        gl.glEndList();

        nlGLU.setQuadric(quadr);
        compileShapes(gl, compiledShapes, false);
        glu.gluDeleteQuadric(quadr);
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
                    gl.glBegin (GL2.GL_LINES);
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
                        slopey = slopey * myViewer.zScale;
                        double beta = 180.0 * Math.atan(slopey) / Math.PI;
                        gl.glRotated(beta, 1, 0, 0);
                    }
                    if ( i>0 && i<myViewer.worldWidth-1  ) {
                        double slopex = (((PatchView)myViewer).reporterValueMatrix[i+1][j].reporterValue -  ((PatchView)myViewer).reporterValueMatrix[i-1][j].reporterValue ) / 2.0 ;
                        slopex = slopex * myViewer.zScale;
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

        if ( ((PatchView)myViewer).viewOptions.structures() ) {
            for ( TurtleValue tv : ((PatchView)myViewer).turtleValues ) {
                gl.glPushMatrix();
                try {
                    float red = (tv.color.getRed()/255f);
                    float green = (tv.color.getGreen()/255f);
                    float blue = (tv.color.getBlue()/255f);

                    setColorAndStandardMaterial( gl, red, green, blue);

                    double zval = myViewer.zScale * tv.reporterValue;
                    if (zval < 0 ) {
                        gl.glTranslated(tv.xcor , tv.ycor, zval - .045); //a tiny bit below the patch rep, take line thickness into account.
                    } else {
                        gl.glTranslated(tv.xcor , tv.ycor, zval + .045); //a tiny bit above the patch rep, take line thickness into account.
                    }

                    //observer.applyNormal(gl);  //DON'T turn the turtles to be 'sprites'
                    gl.glScaled(tv.size, tv.size, tv.size);
                    gl.glScaled(3.0, 3.0, 3.0);
                    gl.glRotated(-1 * tv.heading, 0, 0, 1);  //DO reflect the turtles' headings
                    gl.glCallList(compiledShapes.get(tv.shape));
                } catch (ConcurrentModificationException cme) {
                    cme.printStackTrace();
                    //proving that there are no cmes any more
                    throw cme;
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                    //proving there are not npes any more.
                    throw npe;
                }
                gl.glPopMatrix();
            }
            // Draw links as lines
            for (LinkValue lv : ((PatchView)myViewer).linkValues) {
              double zval1 = 0.0;
              double zval2 = 0.0;
              if (((PatchView)myViewer).viewOptions.linksAreThreeD()) {
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
        }

        drawAxesIfDragging(gl, axisHeadHandle);

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
