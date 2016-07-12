package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.LogoListBuilder;
import org.nlogo.api.Syntax;

import view25d.View25DExtension;
import view25d.view.VarviewWindow;

public class GetObserverFocus extends DefaultReporter {

	@Override
	public String getAgentClassString() {
		return "OTPL:OTPL";
	}
	
	@Override
	public Syntax getSyntax() {
		int[] argType = {Syntax.StringType()};
		int retType = Syntax.ListType();
		return  Syntax.reporterSyntax( argType, retType, "OTPL", "?" );
	}
	
	
	@Override
	public Object report(Argument[] args, Context ctxt)
			throws ExtensionException, LogoException {
		
		String whichView = args[0].getString();
		
		VarviewWindow vvwp = View25DExtension.patchWindowMap.get(whichView);
 		VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);
 		
 		double[] focuspoint = {0.0, 0.0};
 		
		if (vvwp != null) {
			focuspoint = vvwp.getGLWindow().getObserverPerspectiveFocusPoint();
		}
		
		if (vvwt != null) {
			focuspoint = vvwt.getGLWindow().getObserverPerspectiveFocusPoint();
		}
		
		LogoListBuilder llb = new LogoListBuilder();
		llb.add(focuspoint[0]);
		llb.add(focuspoint[1]);
		return llb.toLogoList();
	}

}
