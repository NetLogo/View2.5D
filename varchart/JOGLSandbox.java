package varchart;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class JOGLSandbox extends JFrame {

	private static final long serialVersionUID = 1L;

	
	public static void doIt() {
		final JOGLSandbox app = new JOGLSandbox();
	    SwingUtilities.invokeLater (
	      new Runnable() {
	        public void run() {
	          app.setVisible(true);
	        }
	      }
	    );
	}
	
	public JOGLSandbox() {
		super("Sandbox");
		//displose window  when the JFrame is closed
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    //JOGL code below
	    GLCanvas glcanvas = new GLCanvas();
	    glcanvas.addGLEventListener(new SandboxView());

	    //add the GLCanvas just like we would any Component
	    getContentPane().add(glcanvas, BorderLayout.CENTER);
	    setSize(500, 300);

	    //center the JFrame on the screen
	    centerWindow(this);
	}
	
	protected void centerWindow( Component frame ) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if ( frameSize.width > screenSize.width )
			frameSize.width = screenSize.width;

		if ( frameSize.height > screenSize.height )
			frameSize.height = screenSize.height;

		frame.setLocation(
			(screenSize.width - frameSize.width) >> 1,
			(screenSize.height - frameSize.height) >> 1
		);
	}
	
	
}
