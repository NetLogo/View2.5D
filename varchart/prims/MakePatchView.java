package varchart.prims;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.nlogo.api.AgentException;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Patch;
import org.nlogo.api.ReporterTask;
import org.nlogo.api.Syntax;
import org.nlogo.api.World;
import org.nlogo.app.App;

import varchart.VarchartExtension;
import varchart.view.PatchView;

public class MakePatchView extends DefaultReporter {


	@Override
	public String getAgentClassString() {
		return "O";
	}
	
	@Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType(), Syntax.ReporterTaskType()};
       return  Syntax.reporterSyntax(argTypes, Syntax.NumberType());
    }
	
	@SuppressWarnings("unused")
	@Override
	public Object report(Argument[] args, final Context context)
			throws ExtensionException, LogoException {
		String title = args[0].getString().trim();
		if ( title.length() == 0 ) { title = "Patch Variable Visualizer"; }
		
		ReporterTask patchReporterTask = args[1].getReporterTask();

		try {
			Patch patch = App.app().workspace().world().getPatchAt(0,0);
			double test = (Double)patchReporterTask.report(context, new Object[]{patch});
			
		} catch (AgentException e) {
			throw new ExtensionException("ERROR:  Your task cannot be run by patches!", e);
		}
		
		final Integer id = VarchartExtension.getNextIndex();
		
		final PatchView manualPatchView = new PatchView(title, patchReporterTask, id);

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
					manualPatchView.setupForRendering( worldWidth, worldHeight, minX, maxX, minY, maxY );
					manualPatchView.manuallyRefreshReporterView(context);
					manualPatchView.setVisible(true);
				}
			});

		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			throw new ExtensionException( "ERROR in setting up window: " + e.getMessage() );
		} catch (InvocationTargetException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			throw new ExtensionException( "ERROR in setting up window: " + e.getMessage() );
		} 
		
		int i = VarchartExtension.storeWindowAtIndex(id, manualPatchView);
		if (i < 0 ) {
			throw new ExtensionException( "ERROR in setting up window.  Unable to place window in static list." );
		}

		return id.doubleValue();
	}
	
}
