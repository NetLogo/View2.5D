package varchart.prims;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.nlogo.api.Agent;
import org.nlogo.api.AgentException;
import org.nlogo.api.AgentSet;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Patch;
import org.nlogo.api.ReporterTask;
import org.nlogo.api.Syntax;
import org.nlogo.api.Turtle;
import org.nlogo.api.World;
import org.nlogo.app.App;

import varchart.VarchartExtension;
import varchart.view.PatchView;
import varchart.view.TurtleView;

public class MakeTurtleView extends DefaultReporter {

	@Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType(), Syntax.AgentsetType(), Syntax.ReporterTaskType()};
       return  Syntax.reporterSyntax(argTypes, Syntax.NumberType());
    }
	
	@Override
	public Object report(Argument[] args, final Context context)
			throws ExtensionException, LogoException {
		String title = args[0].getString().trim();
		if ( title.length() == 0 ) { title = "Testing GL Stuff"; }
		
		AgentSet as = args[1].getAgentSet();
		ReporterTask turtleReporterTask = args[2].getReporterTask();
		for (Agent a: as.agents() ) {
			Turtle turtle = (Turtle)a;
			double test = (Double)turtleReporterTask.report(context, new Object[]{turtle});
		}
			
		final Integer id = VarchartExtension.getNextTurtleIndex();
		
		final TurtleView manualTurtleView = new TurtleView(title, as, turtleReporterTask, id);

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					World w = App.app().workspace().world();
					int worldWidth = w.worldWidth();
					int worldHeight = w.worldHeight();
					int minX = w.minPxcor();
					int minY = w.minPycor();
					int maxX = w.maxPxcor();
					int maxY = w.maxPycor();
					manualTurtleView.setupForRendering( worldWidth, worldHeight, minX, maxX, minY, maxY );
					manualTurtleView.manuallyRefreshReporterView(context);
					manualTurtleView.setVisible(true);
				}
			});

		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			throw new ExtensionException( "ERROR in setting up window: " + e.getMessage() );
		} catch (InvocationTargetException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			throw new ExtensionException( "ERROR in setting up window: " + e.getMessage() );
		} 
		
		int i = VarchartExtension.storeTurtleWindowAtIndex(id, manualTurtleView);
		if (i < 0 ) {
			throw new ExtensionException( "ERROR in setting up window.  Unable to place window in static list." );
		}

		return id.doubleValue();
	}
	
}