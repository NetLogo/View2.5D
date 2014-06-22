package varchart.prims;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;

import varchart.VarchartExtension;
import varchart.view.VarviewWindow;

public class UpdateAllViews extends DefaultCommand {

	@Override
	public void perform(Argument[] arg0, Context context)
			throws ExtensionException, LogoException {
		for (VarviewWindow vvw: VarchartExtension.windowMap.values()) {
			vvw.manuallyRefreshReporterView(context);
		}
	}
}
