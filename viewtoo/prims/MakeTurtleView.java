package viewtoo.prims;

import javax.swing.SwingUtilities;

import org.nlogo.api.Agent;
import org.nlogo.api.AgentSet;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.ReporterTask;
import org.nlogo.api.Syntax;
import org.nlogo.api.Turtle;
import org.nlogo.api.World;
import org.nlogo.app.App;

import viewtoo.View25DExtension;
import viewtoo.view.TurtleView;

public class MakeTurtleView extends DefaultCommand {

	@Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType(), Syntax.AgentsetType(), Syntax.ReporterTaskType()};
       return  Syntax.commandSyntax(argTypes);
    }
	
	@Override
	public String getAgentClassString() {
		return "O";
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
					AgentSet as = args[1].getAgentSet();
					ReporterTask turtleReporterTask = args[2].getReporterTask();
					for (Agent a: as.agents() ) {
						Turtle turtle = (Turtle)a;
						double test = (Double)turtleReporterTask.report(context, new Object[]{turtle});
					}
					
					TurtleView manualTurtleView = new TurtleView(title, as, turtleReporterTask);
					manualTurtleView.postConstructor();

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
					View25DExtension.storeTurtleWindowWithTitle(title, manualTurtleView);

				} catch (ExtensionException e1) {
					e1.printStackTrace();
				} catch (LogoException e1) {
					e1.printStackTrace();
				} 
			}
		});
	}
}