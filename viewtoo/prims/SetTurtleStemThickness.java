package viewtoo.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import viewtoo.View25DExtension;
import viewtoo.view.TurtleView;
import viewtoo.view.VarviewWindow;

public class SetTurtleStemThickness extends DefaultCommand {

	@Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType(), Syntax.NumberType()};
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
		double newThickness = args[1].getDoubleValue();
		
		VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);
		
		if (vvwt != null) {
			((TurtleView)vvwt).viewOptions.setStemThickness(newThickness);
			((TurtleView)vvwt).refresh();
		}

	}

}
