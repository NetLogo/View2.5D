package view25d.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import javax.swing.JFrame;

import org.nlogo.agent.AgentSet;
import org.nlogo.agent.Turtle;
import org.nlogo.api.Agent;
import org.nlogo.api.AgentException;
import org.nlogo.api.Context;
import org.nlogo.api.Link;
import org.nlogo.api.Patch;
import org.nlogo.api.AnonymousReporter;
import org.nlogo.app.App;

import view25d.View25DExtension;
import view25d.view.gl.PatchGL;

public class PatchView extends VarviewWindow {

    private static final long serialVersionUID = 1L;

    private PatchGL glManager;
    public MouseableGLWindow getGLWindow() { return glManager; }
    private AnonymousReporter reporter;

    public PatchViewOptions viewOptions;

    public PatchValue[][] reporterValueMatrix;

    public boolean doingTurtles = false;
    public ArrayList<TurtleValue> turtleValues = new ArrayList<TurtleValue>();
    public ArrayList<LinkValue> linkValues;

    public PatchView(String title, AnonymousReporter rt) {
        super(title);
        reporter = rt;
    }

    //called by class that instantiates PatchView, after constructor.
    public void postConstructor() {
        setupUI();
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        final String myTitle = this.getTitle();
        //window listener to ensure that the HashMap gets cleaned up.
        this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowClosing(WindowEvent e) { View25DExtension.removePatchWindowWithTitle(myTitle); }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });
        GLCapabilities glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        GLCanvas glCanvas =  new GLCanvas(glCapabilities);

        glManager = new PatchGL(this);

        glCanvas.addGLEventListener(glManager);
        glCanvas.addMouseListener(glManager);
        glCanvas.addMouseMotionListener(glManager);
        glCanvas.addMouseWheelListener(glManager);

        glManager.setCanvas( glCanvas );
        mainPanel.add(glCanvas, BorderLayout.CENTER);

        viewOptions = new PatchViewOptions( this, true, false, false);
        mainPanel.add(viewOptions, BorderLayout.NORTH);

        setSize(new Dimension(600, 650));
        xExpand = 0;
        yExpand = 50;
        /*      this.addComponentListener( new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                applySquareConstraint(xExpand, yExpand, mainPanel);
                }
                });
                */
        centerWindow(this);
    }



    public void setupForRendering(  int wWidth, int wHeight, int minX, int maxX, int minY, int maxY ) {
        worldWidth = wWidth;
        worldHeight = wHeight;
        minPxcor = minX;
        minPycor = minY;
        maxPxcor = maxX;
        maxPycor = maxY;
        reporterValueMatrix = new PatchValue[worldWidth][worldHeight];
    }


    public void updateValueArray(Context context) {
        for (int i = 0; i< worldWidth; i++){
            for (int j = 0;j< worldHeight; j++) {
                try {
                    Patch patch = App.app().workspace().world().getPatchAt(i + minPxcor, j + minPycor);
                    Color c = org.nlogo.api.Color.getColor(patch.pcolor());
                    double val = (Double)reporter.report(context, new Object[]{patch});
                    reporterValueMatrix[i][j] = new PatchValue(c, val);
                } catch (AgentException e) {
                    e.printStackTrace();
                }
            }
        }

        ArrayList<TurtleValue> temp = new ArrayList<TurtleValue>();
        ArrayList<LinkValue> temp2 = new ArrayList<LinkValue>();
        if (doingTurtles) {
            AgentSet as = App.app().workspace().world().turtles();
            for (Agent a : as.agents() ) {
                Turtle t = (Turtle)a;
                double val = (Double)reporter.report(context, new Object[]{t});
                Color c = org.nlogo.api.Color.getColor(t.color());
                double stemColor = 4.5f;  // This will be ignored no stem in PatchView
                TurtleValue tv = new TurtleValue( t.shape(), c, t.size(), t.xcor(), t.ycor(), val, stemColor );
                tv.setHeading(t.heading());
                temp.add(tv);
            }
            // Get Set of Links associated with the Turtles
            Set<Link> linkSet = TurtleView.getLinkSetFromTurtleSet(as);
            for (Link link : linkSet) {

                Turtle end1 = (Turtle)link.end1();
                Turtle end2 = (Turtle)link.end2();

                Color c = org.nlogo.api.Color.getColor(link.color());
                double zcor1 = (Double)reporter.report(context, new Object[]{end1});
                double zcor2 = (Double)reporter.report(context, new Object[]{end2});

                LinkValue lv = new LinkValue(link.shape(), c, link.lineThickness(),
                                             end1.xcor(), end1.ycor(), zcor1,
                                             end2.xcor(), end2.ycor(), zcor2);
                temp2.add(lv);
            }
        }
        //copy over the new values in an atomic step to avoid concurrent access
        turtleValues = temp;
        linkValues = temp2;
    }

    @Override
    public void manuallyRefreshReporterView(Context context) {
        updateValueArray( context );
        glManager.repaintCanvas();
    }

    public void refresh() {
        glManager.repaintCanvas();
    }

    @Override
    public void resetPerspective() {
        glManager.observer.goHome(this);
        glManager.repaintCanvas();
    }

    public void zoomZby( double change ) {
        if (-change < zScale) {
            zScale += change;
        }
        glManager.repaintCanvas();
    }

}
