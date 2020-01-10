package view25d.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import javax.swing.JFrame;


import org.nlogo.api.Agent;
import org.nlogo.api.AgentException;
import org.nlogo.api.AgentSet;
import org.nlogo.api.AnonymousReporter;
import org.nlogo.api.Context;
import org.nlogo.api.Link;
import org.nlogo.api.LogoException;
import org.nlogo.api.Patch;
import org.nlogo.agent.Turtle;
import org.nlogo.api.World;
import org.nlogo.app.App;
import org.nlogo.nvm.ExtensionContext;

import view25d.View25DExtension;
import view25d.view.gl.TurtleGL;

public class TurtleView extends VarviewWindow {

    private static final long serialVersionUID = 1L;

    AgentSet myAgents;
    private TurtleGL glManager;
    public MouseableGLWindow getGLWindow() { return glManager; }

    private AnonymousReporter reporter;
    private AnonymousReporter stemColorReporter = null;
    private double defaultStemColor = 4.5f;
    public ArrayList<TurtleValue> turtleReporterValues;
    public ArrayList<LinkValue> linkValues;
    public Color[][] patchColorMatrix;

    public TurtleViewOptions viewOptions;


    public TurtleView( String title, AgentSet set, AnonymousReporter rt) {
        super(title);
        reporter = rt;
        myAgents = set;
    }

    public void postConstructor() {
        setupUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final String myTitle = this.getTitle();
        this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowClosing(WindowEvent e) { View25DExtension.removeTurtleWindowWithTitle(myTitle); }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });
        GLCapabilities glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        final GLCanvas glCanvas =  new GLCanvas(glCapabilities);

        glManager = new TurtleGL(this);

        glCanvas.addGLEventListener(glManager);
        glCanvas.addMouseListener(glManager);
        glCanvas.addMouseMotionListener(glManager);
        glCanvas.addMouseWheelListener(glManager);

        glManager.setCanvas( glCanvas );
        mainPanel.add(glCanvas, BorderLayout.CENTER);

        viewOptions = new TurtleViewOptions(this, true, true, true, false );
        mainPanel.add(viewOptions, BorderLayout.NORTH);

        //change if re-add scalemanipulator
        this.setSize(new Dimension(600, 640));
        xExpand = 0;
        yExpand = 40;
/*
        this.addComponentListener( new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                applySquareConstraint(xExpand, yExpand, mainPanel);
            }
        });
*/
        centerWindow(this);
    }


    @Override
    public void manuallyRefreshReporterView(Context context)  throws LogoException {
        updateArrayList(context);
        glManager.repaintCanvas();
    }

    private void updateArrayList(Context context) throws LogoException {
        //turtleReporterValues.clear();

        ArrayList<TurtleValue> temp = new ArrayList<TurtleValue>();
        ArrayList<LinkValue> temp2 = new ArrayList<LinkValue>();

        for (Agent a : myAgents.agents()) {
            Turtle turtle = (Turtle)a;
            if (turtle.hidden()) {
                continue;
            }
            Color c = org.nlogo.api.Color.getColor(turtle.color());
            double val = (Double)reporter.report(context, new Object[]{turtle});
            double stemColor = getStemColor(context, turtle);
            TurtleValue tv = new TurtleValue( turtle.shape(), c, turtle.size(), turtle.xcor(), turtle.ycor(), val, stemColor);
            temp.add(tv);
        }

        // Get Set of Links associated with the Turtles
        Set<Link> linkSet = getLinkSetFromTurtleSet(myAgents);

        // Store the data needed for link visualization
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

        turtleReporterValues = temp;
        linkValues = temp2;
            
        if (viewOptions.usePColor()) {
            updatePColors();
        }  
    }


    // Given an AgentSet of turtles, produce a Set of associated Links.
    // Assumes check that AgentSet contains Turtles has already been made.
    // Currently includes links for which only one end is in the AgentSet
    public static  Set<Link>  getLinkSetFromTurtleSet(AgentSet turtleSet) {
        
        Link[] linkArray = getLinkArrayFromTurtleSet(turtleSet);

        // Making it a set eliminates the duplicates
        Set<Link> linkSet = new HashSet<>(Arrays.asList(linkArray));

        return linkSet;
        
    }
        
    // Given an AgentSet of Turtles, produce an Array of associated links
    // Assumes check that AgentSet contains Turtles has already been made.
    // Each link will appear twice if both ends of a link are in the AgentSet
    // 
    public static Link[] getLinkArrayFromTurtleSet(AgentSet turtleSet) {
        
        // no work to do if there are no links or no turtles

        World world = App.app().workspace().world();
        int totalLinks = world.links().count();
        
        if (totalLinks == 0 || turtleSet.count() == 0) {
            return new Link[0];
        }

        // Allow space for each link to appear twice
        Link[] result = new Link[2 * totalLinks];
        int writeTo = 0;
        
        for (Agent a : turtleSet.agents()) {

            Turtle turtle = (Turtle)a;
            Link[] links = turtle.links();
            if (links.length == 0) {
                continue;
            }

            // Add links to link array
            for (Link link : links) {
                if (link.hidden()) {
                    continue;
                }
                result[writeTo] = link;
                writeTo++;
            }
        }

        // Eliminate unused Array space
        if (writeTo == result.length) {
            return result;
        } else {
            return Arrays.copyOfRange(result, 0, writeTo);
        }
    }


    private void updatePColors() {
        for (int i = 0; i< worldWidth; i++){
            for (int j = 0;j< worldHeight; j++) {
                try {
                    Patch patch = App.app().workspace().world().getPatchAt(i + minPxcor, j + minPycor);
                    patchColorMatrix[i][j] = org.nlogo.api.Color.getColor(patch.pcolor());
                } catch (AgentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<TurtleValue> getCopyOfReporterValues() {
        ArrayList<TurtleValue> rtn = new ArrayList<TurtleValue>(turtleReporterValues.size() );
        rtn.addAll( turtleReporterValues );
        return rtn;
    }

    
    public ArrayList<LinkValue> getCopyOfLinkValues() {
        ArrayList<LinkValue> rtn = new ArrayList<LinkValue>(linkValues.size() );
        rtn.addAll( linkValues );
        return rtn;
    }
    
    public void setupForRendering(  int wWidth, int wHeight, int minX, int maxX, int minY, int maxY ) {
        worldWidth = wWidth;
        worldHeight = wHeight;
        minPxcor = minX;
        minPycor = minY;
        maxPxcor = maxX;
        maxPycor = maxY;

        turtleReporterValues = new ArrayList<TurtleValue>(myAgents.count());

        patchColorMatrix = new Color[worldWidth][worldHeight];
        for (int i = 0; i< worldWidth; i++){
            for (int j = 0;j< worldHeight; j++) {
                patchColorMatrix[i][j] = Color.BLUE;
            }
        }
    }

    public void setAgentSet(AgentSet as) {
        myAgents = as;
    }


    @Override
    public void resetPerspective() {
        glManager.observer.goHome(this);
        refresh();
    }

    public void refresh() {
        glManager.repaintCanvas();
        if (viewOptions.usePColor()) {
            updatePColors();
        }
    }

    public void zoomZby( double change ) {
        if (-change < zScale) {
            zScale += change;
        }
        glManager.repaintCanvas();
    }

    public double trimStemColor(double stemColor) {
        if ( stemColor < 0 ) { stemColor = 0.0; }
        if ( stemColor >= 140 ) { stemColor = 139.9; }
        return stemColor;
    }

    public double getStemColor(Context context, Turtle turtle) throws LogoException {
        double stemColor;
        if (stemColorReporter == null) {
            stemColor = defaultStemColor;
        } else {
            try {
                stemColor = (Double)stemColorReporter.report(context, new Object[]{turtle});
            }
            catch (Exception e1) {
                throw new LogoException("Stem color reporter did not generate a number. ", e1) { };
            }
        }
        return trimStemColor(stemColor);
    }

    public AnonymousReporter getStemColorReporter() { return stemColorReporter; }
    public void setStemColorReporter( AnonymousReporter reporter) {
        stemColorReporter = reporter;
    }
}
