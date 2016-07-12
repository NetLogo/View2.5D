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

public class SetZScale implements Command {

    @Override
    public Syntax getSyntax() {
        int[] argTypes = {Syntax.StringType(), Syntax.NumberType()};
        return  SyntaxJ.commandSyntax(argTypes, "O---");
    }

    @Override
    public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {

        String whichView = args[0].getString();
        double newScale = args[1].getDoubleValue();

        VarviewWindow vvwp = View25DExtension.patchWindowMap.get(whichView);

        VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);

        if (vvwp != null) {
            double oldScale = vvwp.zScale;
            double delScale = newScale - oldScale;
            vvwp.zoomZby(delScale);
        }

        if (vvwt != null) {
            double oldScale = vvwt.zScale;
            double delScale = newScale - oldScale;
            vvwt.zoomZby(delScale);
        }

    }

}
