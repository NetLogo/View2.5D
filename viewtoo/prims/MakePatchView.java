package viewtoo.prims;

import javax.swing.SwingUtilities;

import org.nlogo.api.AgentException;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Patch;
import org.nlogo.api.ReporterTask;
import org.nlogo.api.Syntax;
import org.nlogo.api.World;
import org.nlogo.app.App;

import viewtoo.View25DExtension;
import viewtoo.view.PatchView;

public class MakePatchView extends DefaultCommand {

	@Override
	public String getAgentClassString() {
		return "O";
	}
	
	@Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType(), Syntax.ReporterTaskType()};
       return  Syntax.commandSyntax(argTypes);
    }
	
	@SuppressWarnings("unused")
	@Override
	public void perform(final Argument[] args, final Context context) throws ExtensionException, LogoException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				String title = "";
				try {
					title = args[0].getString().trim();
					if ( title.length() == 0 ) { throw new ExtensionException("Window title cannot be empty"); }
				} catch (ExtensionException e1) {
					throw new RuntimeException(e1.fillInStackTrace());
				} catch (LogoException e1) {
					e1.printStackTrace();
				}
				
				
				try {
					
					ReporterTask patchReporterTask = args[1].getReporterTask();
					
					Patch patch = App.app().workspace().world().getPatchAt(0,0);
					double test = (Double)patchReporterTask.report(context, new Object[]{patch});


					PatchView manualPatchView = new PatchView(title, patchReporterTask );
					manualPatchView.postConstructor();

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
					View25DExtension.storePatchWindowWithTitle(title, manualPatchView);
					
				} catch (ExtensionException e1) {
					e1.printStackTrace();
				} catch (LogoException e1) {
					e1.printStackTrace();
				} catch (AgentException ae) {
					ae.printStackTrace();
				}	
			}
		});
	}
}
