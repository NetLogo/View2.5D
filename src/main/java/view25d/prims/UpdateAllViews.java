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

public class UpdateAllViews implements Command {

    @Override
    public Syntax getSyntax() {
        return SyntaxJ.commandSyntax("O---");
    }

    @Override
    public void perform(Argument[] arg0, Context context)
            throws ExtensionException, LogoException {
        for (VarviewWindow vvw: View25DExtension.patchWindowMap.values()) {
            vvw.manuallyRefreshReporterView(context);
        }
    }
}
