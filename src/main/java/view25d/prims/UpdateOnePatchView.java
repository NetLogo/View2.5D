package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import view25d.View25DExtension;
import view25d.view.VarviewWindow;

public class UpdateOnePatchView extends DefaultCommand {

	@Override
	public Syntax getSyntax() {
		int[] argType = {Syntax.StringType()};
		return  Syntax.commandSyntax( argType );
	}
	
	@Override
	public String getAgentClassString() {
		return "O";
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
