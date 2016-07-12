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

public class SetObserverAngles extends DefaultCommand {

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
		double newHeading = args[1].getDoubleValue();
		double newPitch = args[2].getDoubleValue();
		
		VarviewWindow vvwp = View25DExtension.patchWindowMap.get(whichView);
		
 		VarviewWindow vvwt = View25DExtension.turtleWindowMap.get(whichView);
 		
		if (vvwp != null) {
			MouseableGLWindow win = vvwp.getGLWindow();
			double [] headingAndPitch = win.getObserverPerspectiveAngles();
			
			double oldHeading = headingAndPitch[0];
			double delHeading = newHeading - oldHeading;
			
			double oldPitch = headingAndPitch[1];
			double delPitch = -1 * (newPitch - oldPitch);

			win.updateObserverPerspectiveAnglesWithDeltas(delHeading, delPitch);
		}
		
		if (vvwt != null) {
			MouseableGLWindow win = vvwt.getGLWindow();
			double [] headingAndPitch = win.getObserverPerspectiveAngles();
			
			double oldHeading = headingAndPitch[0];
			double delHeading = newHeading - oldHeading;
			
			double oldPitch = headingAndPitch[1];
			double delPitch = -1 * (newPitch - oldPitch);

			win.updateObserverPerspectiveAnglesWithDeltas(delHeading, delPitch);
		}

	}

}
