package viewtoo.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import viewtoo.View25DExtension;
import viewtoo.view.VarviewWindow;

public class GetZScale extends DefaultReporter {

	@Override
	public String getAgentClassString() {
		return "OTPL:OTPL";
	}
	
	@Override
	public Syntax getSyntax() {
		int[] argType = {Syntax.StringType()};
		int retType = Syntax.NumberType();
		return  Syntax.reporterSyntax( argType, retType, "OTPL", "?" );
	}
	
	@Override
	public Object report(Argument[] args, Context ctxt)
			throws ExtensionException, LogoException {
		
		String whichView = args[0].getString();
		
		VarviewWindow vvwp = View25DExtension.patchWindowMap.get(whichView);
 		VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);
 		
 		double scale = 0;
 		
		if (vvwp != null) {
			scale = vvwp.zScale;
		}
		
		if (vvwt != null) {
			scale = vvwt.zScale;
		}
		
		return new Double(scale);
	}

}
