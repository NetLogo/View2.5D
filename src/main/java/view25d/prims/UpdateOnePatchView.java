package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Command;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import view25d.View25DExtension;
import view25d.view.VarviewWindow;

public class UpdateOnePatchView implements Command {

    @Override
    public Syntax getSyntax() {
        int[] argType = {Syntax.StringType()};
        return  SyntaxJ.commandSyntax( argType, "O---" );
    }

    @Override
    public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {
        String title = args[0].getString();
        if ( View25DExtension.patchWindowMap.containsKey( title ) ) {
            VarviewWindow win = View25DExtension.patchWindowMap.get( title );
            win.manuallyRefreshReporterView(context);
        } else {
            throw new ExtensionException("No Variable Visualization window with title "+title);
        }
    }

}
