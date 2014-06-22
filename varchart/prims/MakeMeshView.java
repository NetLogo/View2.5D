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
import varchart.view.MeshView;

public class MakeMeshView extends DefaultReporter {

	@Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType(), Syntax.ReporterTaskType()};
       return  Syntax.reporterSyntax(argTypes, Syntax.NumberType());
    }
	
	@Override
	public Object report(Argument[] args, final Context context)
			throws ExtensionException, LogoException {
		String title = args[0].getString().trim();
		if ( title.length() == 0 ) { title = "Patch Reporter Results"; }
		ReporterTask task = args[1].getReporterTask();

		try {
			Patch patch = App.app().workspace().world().getPatchAt(0,0);
			double test = (Double)task.report(context, new Object[]{patch});
			
		} catch (AgentException e) {
			throw new ExtensionException("ERROR:  Your task cannot be run by patches!", e);
		}

		final Integer id = VarchartExtension.getNextIndex();
		final MeshView manualMeshView = new MeshView(title, task, id);

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					World w = App.app().workspace().world();
					int worldWidth = w.worldWidth();
					int worldHeight = w.worldHeight();
					manualMeshView.setupForRendering( worldWidth, worldHeight );
					manualMeshView.manuallyRefreshReporterView(context);
					manualMeshView.setVisible(true);
				}
			});

		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			throw new ExtensionException( "ERROR in setting up window: " + e.getMessage() );
		} catch (InvocationTargetException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			throw new ExtensionException( "ERROR in setting up window: " + e.getMessage() );
		} 
		
		int i = VarchartExtension.storeWindowAtIndex(id, manualMeshView);
		if (i < 0 ) {
			throw new ExtensionException( "ERROR in setting up window.  Unable to place window in static list." );
		}

		return id.doubleValue();
	}
}

