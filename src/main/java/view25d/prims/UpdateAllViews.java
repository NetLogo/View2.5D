package view25d.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;

import view25d.View25DExtension;
import view25d.view.VarviewWindow;

public class UpdateAllViews extends DefaultCommand {

	@Override
	public String getAgentClassString() {
		return "O";
	}
	
	@Override
	public void perform(Argument[] arg0, Context context)
			throws ExtensionException, LogoException {
		for (VarviewWindow vvw: View25DExtension.patchWindowMap.values()) {
			vvw.manuallyRefreshReporterView(context);
		}
	}
}
