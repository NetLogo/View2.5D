package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Reporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.LogoListBuilder;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import view25d.View25DExtension;
import view25d.view.VarviewWindow;

public class GetObserverAngles implements Reporter {

    @Override
    public Syntax getSyntax() {
        int[] argType = {Syntax.StringType()};
        int retType = Syntax.ListType();
        return  SyntaxJ.reporterSyntax( argType, retType );
    }


    @Override
    public Object report(Argument[] args, Context ctxt)
            throws ExtensionException, LogoException {

        String whichView = args[0].getString();

        VarviewWindow vvwp = View25DExtension.patchWindowMap.get(whichView);
        VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);

        double[] angles = {0.0, 0.0};

        if (vvwp != null) {
            angles = vvwp.getGLWindow().getObserverPerspectiveAngles();
        }

        if (vvwt != null) {
            angles = vvwt.getGLWindow().getObserverPerspectiveAngles();
        }

        LogoListBuilder llb = new LogoListBuilder();
        llb.add(angles[0]);
        llb.add(angles[1]);
        return llb.toLogoList();
    }


}
