package varchart;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class SandboxView implements GLEventListener {

	  GLU glu;//only one instance needed per context
	  
	  int display_list_handle;
	  int disp_list_handle2;
	  /**
	  * Take care of initialization here.
	  */
	  public void init(GLAutoDrawable gld) {
	    //The mode is GL.GL_MODELVIEW by default
	    //We will also use the default ViewPort
	    GL gl = gld.getGL();
	    glu = new GLU();
	    
	    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	    
	    //Define points for eye, at and up.
	    //This is your camera. It ALWAYS goes
	    //in the GL_MODELVIEW matrix.
	    glu.gluLookAt(
	                  20, 18,  0,
	                  20, 18, 30,
	                   0,  1,  0
	                  );
	    
	    setupDisplayList(gl);
	    setupDisplayList2(gl);
	    
	    /*
	    
	    float positiona[] = {30f, 25f, 30f, -1.0f};
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


        float material0a[] = { 0.05f, 0.6f, 0.7f, 1.0f };
        FloatBuffer material0 = FloatBuffer.wrap(material0a);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, material0);
	    
	    
	    */
	    
	  }
	  

	  private void setupDisplayList(GL gl) {
		
		 display_list_handle = gl.glGenLists(1);
		 gl.glNewList(display_list_handle, GL.GL_COMPILE);
		 gl.glBegin(GL.GL_POLYGON);
		 	gl.glVertex2d(-.4, .4);
		 	gl.glVertex2d(.4, .4);
		 	gl.glVertex2d(.4, -.4);
		 	gl.glVertex2d(-.4, -.4);
		 	gl.glVertex2d(-.4, .4);
		 gl.glEnd();
		 gl.glEndList();
		
	}
	  
	private void setupDisplayList2( GL gl ) {
		
		disp_list_handle2 = gl.glGenLists(1);
		gl.glNewList(disp_list_handle2, GL.GL_COMPILE);
		gl.glBegin(GL.GL_TRIANGLES); 
	    gl.glNormal3f(0f, 0f, -1f);
	      gl.glVertex3f(-0.10f, -0.125f, 0f);
	      gl.glVertex3f(0f, 0.125f, 0f);
	      gl.glVertex3f(0f, -0.075f, 0f);

	      gl.glNormal3f(0f, 0f, -1f);
	      gl.glVertex3f(0f, -0.075f, 0f);
	      gl.glVertex3f(0f, 0.125f, 0f);
	      gl.glVertex3f(0.10f, -0.125f, 0f);

	      gl.glNormal3f(-0.707f, +0f, 0.707f);
	      gl.glVertex3f(0f, +0.125f, 0f);
	      gl.glVertex3f(0.10f, -0.125f, 0f);
	      gl.glVertex3f(0f, -0.075f, 0.08f);

	      gl.glNormal3f(0.707f, 0f, 0.707f);
	      gl.glVertex3f(0f, +0.125f, 0f);
	      gl.glVertex3f(0f, -0.075f, 0.08f);
	      gl.glVertex3f(0.10f, -0.125f, 0f);

	      gl.glNormal3f(-0.6f, -0.8f, 0f);
	      gl.glVertex3f(0f, -0.075f, 0.08f);
	      gl.glVertex3f(-0.10f,-0.125f, 0f);
	      gl.glVertex3f(+0f, -0.075f, 0f);

	      gl.glNormal3f(-0.6f, 0.8f, 0f);
	      gl.glVertex3f(0f, -0.075f, 0.08f);
	      gl.glVertex3f(0f, -0.075f, 0f);
	      gl.glVertex3f(0.10f, -0.125f, 0f);
	        gl.glEnd();
	        gl.glEndList();
		
	}


	/**
	  * Take care of drawing here.
	  */
	  public void display(GLAutoDrawable gld) {

		  float red = 1.0f;
		    float green = 0.4f;
		    float blue = 0.5f;
		    
		    GL gl = gld.getGL();
		    
		    //We're changing the mode to GL.GL_PROJECTION
		    //the only JOGL methods that should be called 
		    //while using the GL_PROJECTION matrix are:
		    //  gluPerspective
		    //  glFrustum
		    //  glOrtho
		    //  gluOrtho2D
		    //  glLoadIdentity
		    //  glLoadMatrix
		    gl.glMatrixMode(GL.GL_PROJECTION);
		    gl.glLoadIdentity();
		    
		    // Aspect is width/height
		    double w = gld.getWidth();
		    double h =  gld.getHeight();
		    double aspect = w/h;
		    //Notice we're not using gluOrtho2D.
		    //When using gluPerspective near and far need 
		    //to be positive.
		    //The arguments are:
		    //fovy, aspect, near, far
		    glu.gluPerspective(60.0, aspect, 25.0, 55.0);
		    
		    gl.glMatrixMode(GL.GL_MODELVIEW);
		    //we don't want to initialize the GL_MODELVIEW
		    //using gl.glLoadIdentity() this time. It has
		    //settings from the init() that we wish to keep.


		    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		    
		    gl.glColor3f(blue, red, green);
		    
		    for (int i = 1; i<50; i++) {
		    for (int j = 1; j<50; j++) {
		    	gl.glPushMatrix();
			    gl.glTranslated(i,j, 30);
			    gl.glCallList(display_list_handle);
			    gl.glPopMatrix();
		    }
		    }
		    
		    
		    gl.glColor3f(0.9f, 0.9f, 0.9f);
		    
		    for (int i = 1; i<50; i++) {
		    for (int j = 1; j<50; j++) {
		    	gl.glPushMatrix();
			    gl.glTranslated(i,j, 35);
			    gl.glRotated(i*5, j*6, i*7, j*8);
			    gl.glScaled(2.5,2.5, 2.5);
			    gl.glCallList(disp_list_handle2);
			    gl.glPopMatrix();
		    }
		    }
		    
		    
		    gl.glColor3f(red, green, blue);
		    
		    
		   
	        
	        
		    //notice that the three squares are
		    //exactly the same size. They appear
		    //different on screen because of 
		    //perspective
		    gl.glBegin(GL.GL_QUADS);
		    
		      //1st Plane
		     // gl.glVertex3i(0, 30, 30);
		     // gl.glVertex3i(10, 30, 30);
		    //  gl.glVertex3i(10, 20, 30);
		     // gl.glVertex3i(0, 20, 30);
		    
		      //2nd Plane
		      gl.glVertex3i(20, 20, 37);
		      gl.glVertex3i(30, 20, 37);
		      gl.glVertex3i(30, 10, 37);
		      gl.glVertex3i(20, 10, 37);
		    
		      //3rd Plane
		      gl.glVertex3i(40, 10, 45);
		      gl.glVertex3i(50, 10, 45);
		      gl.glVertex3i(50, 0, 45);
		      gl.glVertex3i(40, 0, 45);
		      
		    gl.glEnd();
		    gl.glPushMatrix();
		    gl.glTranslatef(10f, 25f, 30f);
		 //   gl.glRotatef(90f, 1f, 0f, 0f);
		 //   gl.glRotatef(90f, 0f, 1f, 0f);
		    gl.glScalef(55.5f, 55.5f, 55.5f);
		    
			    gl.glPopMatrix();
	    /*
	    gl.glBegin(GL.GL_TRIANGLES); 
	    gl.glNormal3f(10f+0f, 25f+0f, 30f-1f);
	      gl.glVertex3f(10f-0.10f, 25f-0.125f, 30f+0f);
	      gl.glVertex3f(10f+0f, 25f+0.125f,30f+0f);
	      gl.glVertex3f(10f+0f, 25f-0.075f, 30f+0f);

	      gl.glNormal3f(10f+0f, 25f+0f, 30f-1f);
	      gl.glVertex3f(10f+0f, 25f-0.075f, 30f+0f);
	      gl.glVertex3f(10f+0f, 25f+0.125f, 30f+0f);
	      gl.glVertex3f(10f+0.10f, 25f-0.125f, 30f+0f);

	      gl.glNormal3f(10f-0.707f, 25f+0f, 30f+0.707f);
	      gl.glVertex3f(10f+0f, 25f+0.125f, 30f+0f);
	      gl.glVertex3f(10f+0.10f, 25f-0.125f, 30f+0f);
	      gl.glVertex3f(10f+0f, 25f-0.075f, 30f+0.08f);

	      gl.glNormal3f(10f+0.707f, 25f+0f, 30f+0.707f);
	      gl.glVertex3f(10f+0f, 25f+0.125f, 30f+0f);
	      gl.glVertex3f(10f+0f, 25f-0.075f, 30f+0.08f);
	      gl.glVertex3f(10f+0.10f, 25f-0.125f, 30f+0f);

	      gl.glNormal3f(10f-0.6f, 25f-0.8f, 30f+0f);
	      gl.glVertex3f(10f+0f, 25f-0.075f, 30f+0.08f);
	      gl.glVertex3f(10f-0.10f, 25f-0.125f, 30f+0f);
	      gl.glVertex3f(10f+0f, 25f-0.075f, 30f+0f);

	      gl.glNormal3f(10f-0.6f, 25f+0.8f, 30f+0f);
	      gl.glVertex3f(10f+0f, 25f-0.075f, 30f+0.08f);
	      gl.glVertex3f(10f+0f, 25f-0.075f, 30f+0f);
	      gl.glVertex3f(10f+0.10f, 25f-0.125f, 30f+0f);
	        gl.glEnd();
	    */
	  }
	  
	  public void reshape(
				GLAutoDrawable drawable, 
				int x, 
				int y, 
				int width, 
				int height
			 ) {}

	  public void displayChanged(
						GLAutoDrawable drawable, 
						boolean modeChanged, 
						boolean deviceChanged
					) {}
}
