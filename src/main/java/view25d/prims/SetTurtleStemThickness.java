package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import view25d.View25DExtension;
import view25d.view.TurtleView;
import view25d.view.VarviewWindow;

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
		
		VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);
		if (vvwt != null) {
			double newThickness = args[1].getDoubleValue();
			if ( newThickness < 0 ) { newThickness = 0.0; }
			if ( newThickness > 1 ) { newThickness = 1.0; }
			
			((TurtleView)vvwt).viewOptions.setStemThickness(newThickness);
			((TurtleView)vvwt).refresh();
		}

	}

}
