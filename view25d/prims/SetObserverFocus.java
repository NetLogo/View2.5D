package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import view25d.View25DExtension;
import view25d.view.MouseableGLWindow;
import view25d.view.VarviewWindow;

public class SetObserverFocus extends DefaultCommand {

	@Override
    public Syntax getSyntax() {
       int[] argTypes = {Syntax.StringType(), Syntax.NumberType(), Syntax.NumberType()};
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
		double newX = args[1].getDoubleValue();
		double newY = args[2].getDoubleValue();
		
		VarviewWindow vvwp = View25DExtension.patchWindowMap.get(whichView);
		
 		VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);
 		
		if (vvwp != null) {
			MouseableGLWindow win = vvwp.getGLWindow();
			double [] coords = win.getObserverPerspectiveFocusPoint();
			
			double oldX = coords[0];
			double delX = newX - oldX;
			
			double oldY = coords[1];
			double delY = newY - oldY;

			win.shiftObserverFocusPoint(delX, delY);
		}
		
		if (vvwt != null) {
			MouseableGLWindow win = vvwt.getGLWindow();
			double [] coords = win.getObserverPerspectiveFocusPoint();
			
			double oldX = coords[0];
			double delX = newX - oldX;
			
			double oldY = coords[1];
			double delY = newY - oldY;

			win.shiftObserverFocusPoint(delX, delY);
		}

	}

}
