package varchart.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import varchart.VarchartExtension;
import varchart.view.VarviewWindow;

public class UpdateOneView extends DefaultCommand {

	@Override
	public Syntax getSyntax() {
		int[] argType = {Syntax.NumberType()};
		return  Syntax.commandSyntax( argType );
	}
	
	@Override
	public String getAgentClassString() {
		return "O";
	}

	@Override
	public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {
		Integer index = args[0].getIntValue();
		if ( VarchartExtension.windowMap.containsKey( index ) ) {
			VarviewWindow win = VarchartExtension.windowMap.get( index );
			win.manuallyRefreshReporterView(context);
		} else {
			throw new ExtensionException("No Variable Visualization window with id="+index);
		}
	}

}
