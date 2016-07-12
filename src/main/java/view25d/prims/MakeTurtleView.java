package view25d.prims;

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

import view25d.View25DExtension;
import view25d.view.TurtleView;

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
	
	@Override
	public void perform(final Argument[] args, final Context context) throws ExtensionException, LogoException {
		
		final String title =  args[0].getString().trim();
		if ( title.length() == 0) {
		  throw new ExtensionException("Window title cannot be empty.\nThis is the identifier for your window"); 
		}
		
		//test the reporter against the supplied turtles (throw away the result of report here)
		final AgentSet as = args[1].getAgentSet();
		final ReporterTask turtleReporterTask = args[2].getReporterTask();
		try {
			for (Agent a: as.agents() ) {
				Turtle turtle = (Turtle)a;
				turtleReporterTask.report(context, new Object[]{turtle});
			}
		}
		catch (Exception e1) {
			throw new ExtensionException("Error in processing your reporter. " + e1);
		} 
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
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
			}
		});
	}
}