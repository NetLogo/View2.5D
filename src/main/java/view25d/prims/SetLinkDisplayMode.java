package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Command;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import view25d.View25DExtension;
import view25d.view.PatchView;
import view25d.view.TurtleView;
import view25d.view.VarviewWindow;

public class SetLinkDisplayMode implements Command {

    boolean linksInXYPlane;

    public SetLinkDisplayMode( boolean inXYPlane ) {
        this.linksInXYPlane = inXYPlane;
    }


    @Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType()};
       return  SyntaxJ.commandSyntax(argTypes);
    }

    @Override
    public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {
        String whichView = args[0].getString();

        VarviewWindow vvwp = View25DExtension.patchWindowMap.get(whichView);

        VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);

        if (vvwp != null) {
          ((PatchView)vvwp).viewOptions.setLinksDisplayMode(linksInXYPlane);
        }

        if (vvwt != null) {
          ((TurtleView)vvwt).viewOptions.setLinksDisplayMode(linksInXYPlane);
        }
      }
}
