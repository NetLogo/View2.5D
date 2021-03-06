package view25d.prims;

import javax.swing.SwingUtilities;

import org.nlogo.api.AgentException;
import org.nlogo.api.AnonymousReporter;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Command;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Patch;
import org.nlogo.api.ReporterRunnable;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;
import org.nlogo.api.World;
import org.nlogo.app.App;

import view25d.View25DExtension;
import view25d.view.PatchView;

public class MakePatchView implements Command {

    @Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType(), Syntax.ReporterType()};
       return  SyntaxJ.commandSyntax(argTypes, "O---");
    }

    @Override
    public void perform(final Argument[] args, final Context context) throws ExtensionException, LogoException {

        final String title =  args[0].getString().trim();
        if ( title.length() == 0) {
            throw new ExtensionException("Window title cannot be empty.\nThis is the identifier for your window");
        }

        //test the patchReporter against patch 0,0 (throw away the result of report here)
        final AnonymousReporter patchReporter = args[1].getReporter();
        try {
            Patch patch = App.app().workspace().world().getPatchAt(0,0);
            patchReporter.report(context, new Object[]{patch});
        } catch (AgentException e) {
            throw new ExtensionException("Error in accessing patches. " + e);
        } catch (Exception e) {
            throw new ExtensionException("Error in processing your reporter. " + e);
        }

        context.workspace().updateUI();
        PatchView newPatchView = context.workspace().waitForResult(new InitPatchView(context, title, patchReporter));

        if (newPatchView != null) {
          View25DExtension.storePatchWindowWithTitle(title, newPatchView);
        }
    }

    class InitPatchView implements ReporterRunnable<PatchView> {
      Context context;
      String title;
      AnonymousReporter patchReporter;

      InitPatchView(Context context, String title, AnonymousReporter patchReporter) {
        this.context = context;
        this.title = title;
        this.patchReporter = patchReporter;
      }

      @Override
      public PatchView run() {
        try {
          PatchView manualPatchView = new PatchView(title, patchReporter);
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

          return manualPatchView;
        } catch (Exception e1) {
          e1.printStackTrace();
          return null;
        }
      }
    }
}
