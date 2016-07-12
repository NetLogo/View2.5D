package view25d.prims;

import org.nlogo.api.AgentSet;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Command;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import view25d.View25DExtension;
import view25d.view.TurtleView;

public class UpdateOneTurtleView implements Command {

	@Override
	public Syntax getSyntax() {
		int[] argType = {Syntax.StringType(), Syntax.AgentsetType()};
		return  SyntaxJ.commandSyntax( argType, "O---" );
	}

	@Override
	public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {
		String title = args[0].getString();
		AgentSet as = args[1].getAgentSet();
		if ( View25DExtension.turtleWindowMap.containsKey( title ) ) {
			TurtleView win = View25DExtension.turtleWindowMap.get( title );
			win.setAgentSet( as );
			win.manuallyRefreshReporterView(context);
		} else {
			throw new ExtensionException("No Variable Visualization window with title "+title);
		}
	}

}
