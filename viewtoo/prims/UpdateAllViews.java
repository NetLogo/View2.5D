package viewtoo.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;

import viewtoo.View25DExtension;
import viewtoo.view.VarviewWindow;

public class UpdateAllViews extends DefaultCommand {

	@Override
	public String getAgentClassString() {
		return "O";
	}
	
	@Override
	public void perform(Argument[] arg0, Context context)
			throws ExtensionException, LogoException {
		for (VarviewWindow vvw: View25DExtension.windowMap.values()) {
			vvw.manuallyRefreshReporterView(context);
		}
	}
}
