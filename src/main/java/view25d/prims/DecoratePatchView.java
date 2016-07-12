package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import view25d.View25DExtension;
import view25d.view.PatchView;
import view25d.view.VarviewWindow;

public class DecoratePatchView extends DefaultCommand {

	boolean onOff;
	
	public DecoratePatchView( boolean onOrOff ) {
		this.onOff = onOrOff;
	}
	
	
	@Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType()};
       return  Syntax.commandSyntax(argTypes);
    }
	
	@Override
	public String getAgentClassString() {
		return "O";
	}
	
	@Override
	public void perform(Argument[] args, Context context)
			throws ExtensionException, LogoException {
		
		String whichView = args[0].getString();
		
		VarviewWindow pvwt = View25DExtension.patchWindowMap.get(whichView);
		
		if (pvwt != null) {
			((PatchView)pvwt).doingTurtles = onOff;
			((PatchView)pvwt).manuallyRefreshReporterView(context);
		}

	}

}
