package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Command;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import view25d.View25DExtension;
import view25d.view.MouseableGLWindow;
import view25d.view.VarviewWindow;

public class SetObserverDistance implements Command {

    final public double MIN_DISTANCE = 1.0;
    final public double MAX_DISTANCE = 130.0;
    @Override
    public Syntax getSyntax() {
        int[] argTypes = {Syntax.StringType(), Syntax.NumberType() };
        return  SyntaxJ.commandSyntax(argTypes, "O---");
    }

    @Override
    public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {

        String whichView = args[0].getString();
        double newDistance = args[1].getDoubleValue();

        if (newDistance < MIN_DISTANCE ) {
            throw new ExtensionException("Cannot set observer distance below " + MIN_DISTANCE);
        }

        if (newDistance < MAX_DISTANCE ) {
            throw new ExtensionException("Cannot set observer distance above " + MAX_DISTANCE);
        }

        VarviewWindow vvwp = View25DExtension.patchWindowMap.get(whichView);
        VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);

        if (vvwp != null) {
            MouseableGLWindow win = vvwp.getGLWindow();
            win.zoomToDistance(newDistance);
        }

        if (vvwt != null) {
            MouseableGLWindow win = vvwt.getGLWindow();
            win.zoomToDistance(newDistance);
        }

    }

}
