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
import view25d.view.VarviewWindow;

public class DecoratePatchView implements Command {

    boolean onOff;

    public DecoratePatchView( boolean onOrOff ) {
        this.onOff = onOrOff;
    }


    @Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType()};
       return  SyntaxJ.commandSyntax(argTypes);
    }

    @Override
    public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {
        String whichView = args[0].getString();
        VarviewWindow pvwt = View25DExtension.patchWindowMap.get(whichView);

        if (pvwt != null) {
            ((PatchView)pvwt).doingTurtles = onOff;
            ((PatchView)pvwt).manuallyRefreshReporterView(context);
        }
    }
}
