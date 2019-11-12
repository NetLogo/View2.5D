package view25d.prims;

import javax.swing.SwingUtilities;

import org.nlogo.api.Agent;
import org.nlogo.api.AgentSet;
import org.nlogo.api.AnonymousReporter;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Command;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.ReporterRunnable;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;
import org.nlogo.api.Turtle;
import org.nlogo.api.World;
import org.nlogo.app.App;

import view25d.View25DExtension;
import view25d.view.TurtleView;

public class MakeTurtleView implements Command {

    @Override
    public Syntax getSyntax() {
        int[] argTypes = {Syntax.StringType(), Syntax.AgentsetType(), Syntax.ReporterType()};
        return  SyntaxJ.commandSyntax(argTypes, "O---");
    }

    @Override
    public void perform(final Argument[] args, final Context context) throws ExtensionException, LogoException {

        final String title =  args[0].getString().trim();
        if ( title.length() == 0) {
            throw new ExtensionException("Window title cannot be empty.\nThis is the identifier for your window");
        }

        //test the reporter against the supplied turtles (throw away the result of report here)
        final AgentSet as = args[1].getAgentSet();
        final AnonymousReporter turtleReporter = args[2].getReporter();
        try {
            for (Agent a: as.agents() ) {
                Turtle turtle = (Turtle)a;
                turtleReporter.report(context, new Object[]{turtle});
            }
        }
        catch (Exception e1) {
            throw new ExtensionException("Error in processing your reporter. " + e1);
        }

        context.workspace().updateUI();
        TurtleView newTurtleView = context.workspace().waitForResult(new InitTurtleView(context, as, title, turtleReporter));

        View25DExtension.storeTurtleWindowWithTitle(title, newTurtleView);
    }

    class InitTurtleView implements ReporterRunnable<TurtleView> {
      AgentSet agentSet;
      AnonymousReporter turtleReporter;
      Context context;
      String title;

      InitTurtleView(Context context, AgentSet as, String title, AnonymousReporter turtleReporter) {
        this.context = context;
        this.agentSet = as;
        this.context = context;
        this.title = title;
        this.turtleReporter = turtleReporter;
      }

      @Override
      public TurtleView run() throws LogoException {
        TurtleView manualTurtleView = new TurtleView(title, agentSet, turtleReporter);
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
        return manualTurtleView;
      }
    }
}
