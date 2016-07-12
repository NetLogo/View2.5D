package view25d.view.gl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import org.nlogo.api.Color;

public class NetLogoGLU {

    final int SMOOTHNESS = 16;
    boolean is3D = false;

    GLUquadric quadric;
    public void setQuadric( GLUquadric quad ) { quadric = quad; }


    public void renderRectangle(GL2 gl, int offset,
            org.nlogo.shape.Rectangle rect,
            boolean rotatable) {
        float zDepth = 0.01f + offset * 0.0001f;

        if (!rect.marked()) {
            float[] rgb = rect.displayColor(null).getRGBColorComponents(null);
            gl.glPushAttrib(GL2.GL_CURRENT_BIT);
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

    public void renderRectangle(GL2 gl, float x0, float x1, float y0, float y1,
            float z0, float z1, boolean filled, boolean rotatable) {
        renderRectangularPrism(gl, x0, x1, y0, y1,
                z0, z1, filled, false, rotatable);
    }

    public  void renderRectangularPrism(GL2 gl, float left, float right,
            float back, float front,
            float bottom, float top, boolean hollow,
            boolean hasBottom, boolean hasSides) {

        if (hollow)
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

        gl.glBegin(GL2.GL_QUADS);

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
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
            gl.glDisable(GL2.GL_CULL_FACE);
        }

        if (hasSides) {

            gl.glBegin(GL2.GL_QUADS);

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
            gl.glEnable(GL2.GL_CULL_FACE);
    }


    public void renderCircle(GL2 gl, GLU glu, int offset,
            org.nlogo.shape.Circle circle,
            boolean rotatable) {
        float zDepth = 0.01f + offset * 0.0001f;

        if (!circle.marked()) {
            float[] rgb = circle.displayColor(null).getRGBColorComponents(null);
            gl.glPushAttrib(GL2.GL_CURRENT_BIT);
            gl.glColor3fv(java.nio.FloatBuffer.wrap(rgb));
        }

        // for now assume it is a circle
        float radius = (float)circle.bounds().getWidth() * .0005f;
        float origin[] =
            {(float) circle.origin().getX() * .001f - 0.15f,
                (300 - (float) circle.origin().getY()) * .001f - 0.15f};

        gl.glPushMatrix();

        if (!circle.filled()) {
            glu.gluQuadricDrawStyle(quadric, GLU.GLU_SILHOUETTE);
        }

        // no need to "pancake" if it is always facing the user
        if (rotatable) {
            if (!circle.filled()) {
                glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
                gl.glDisable(GL2.GL_CULL_FACE);
            }

            gl.glTranslatef(origin[0], origin[1], -zDepth);
            glu.gluCylinder(quadric, radius, radius, (2 * zDepth),
                    SMOOTHNESS, 1);

            if (!circle.filled()) {
                gl.glEnable(GL2.GL_CULL_FACE);
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

    public void renderCircle(GL2 gl, GLU glu,
            float innerRadius, float outerRadius, float zDepth,
            boolean rotatable) {
        glu.gluDisk(quadric, innerRadius, outerRadius, SMOOTHNESS, 1);
        if(is3D && rotatable) {
            gl.glRotatef(180f, 1f, 0f, 0f);
            gl.glTranslatef(0f, 0f, zDepth * 2);
            glu.gluDisk(quadric, innerRadius, outerRadius, SMOOTHNESS, 1);
        }
    }

    public void renderLine(GL2 gl, int offset, org.nlogo.shape.Line line) {
        float zDepth = offset * 0.0001f;

        if (!line.marked()) {
            float[] rgb = line.displayColor(null).getRGBColorComponents(null);
            gl.glPushAttrib(GL2.GL_CURRENT_BIT);
            gl.glColor3fv(java.nio.FloatBuffer.wrap(rgb));
        }

        java.awt.Point start = line.getStart();
        java.awt.Point end = line.getEnd();
        float coords[] =
            {start.x * .001f - 0.15f, (300 - start.y) * .001f - 0.15f,
                end.x * .001f - 0.15f, (300 - end.y) * .001f - 0.15f};

        gl.glBegin(GL2.GL_LINES);
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

}
