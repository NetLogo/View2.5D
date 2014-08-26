package varchart;

import java.util.HashMap;



import org.nlogo.api.DefaultClassManager;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.ExtensionManager;
import org.nlogo.api.LogoException;
import org.nlogo.api.Syntax;

import org.nlogo.api.PrimitiveManager;

import org.nlogo.gl.render.JOGLException;
import org.nlogo.gl.render.JOGLLoader;

import varchart.prims.MakePatchView;
import varchart.prims.MakeTurtleView;
import varchart.prims.UpdateAllViews;
import varchart.prims.UpdateOnePatchView;
import varchart.prims.UpdateOneTurtleView;
import varchart.view.TurtleView;
import varchart.view.VarviewWindow;

public class VarchartExtension extends DefaultClassManager {

	static public HashMap<String, VarviewWindow> windowMap = new HashMap<String, VarviewWindow>();
	static public HashMap<String, TurtleView> turtleWindowMap = new HashMap<String, TurtleView>();
		
	public static int numWindows() {
		return turtleWindowMap.size() + windowMap.size();
	}
	
	//Add a window to the indexed set. Get rid of any existing window with that name
	public static void storePatchWindowWithTitle( String s, VarviewWindow win ){
		removePatchWindowWithTitle(s);
		windowMap.put(s,win);		
	}
	
	public static void removePatchWindowWithTitle(String name) {
		if (  windowMap.containsKey(name) ) {
			VarviewWindow vvw = windowMap.get(name);
			windowMap.remove(name);
			vvw.dispose();
		}
	}
	

	//Add a window to the indexed set. Remove any existing t view.
	public static void storeTurtleWindowWithTitle( String s, TurtleView win ){
		removeTurtleWindowWithTitle(s);
		turtleWindowMap.put(s,win);
	}
	
	public static void removeTurtleWindowWithTitle(String name) {
		if (  turtleWindowMap.containsKey(name) ) {
			VarviewWindow tvw = turtleWindowMap.get(name);
			turtleWindowMap.remove(name);
			tvw.dispose();
		}
	}
	

	@Override
	public void runOnce(ExtensionManager em) throws ExtensionException {
		if (!JOGLLoader.isLoaded()) {
			try {
				JOGLLoader.load(this.getClass().getClassLoader());
			}
			catch (JOGLException jex) {
				throw new ExtensionException("Unable to Load JOGL: ", jex);
			}
		}
	}
	
	@Override
	public void load(PrimitiveManager primManager) throws ExtensionException {
		primManager.addPrimitive("patch-view", new MakePatchView() );
		primManager.addPrimitive("turtle-view", new MakeTurtleView() );
		
		primManager.addPrimitive("update-patch-view", new UpdateOnePatchView() );
		primManager.addPrimitive("update-all-patch-views", new UpdateAllViews() );
		
		primManager.addPrimitive("update-turtle-view", new UpdateOneTurtleView() );
		
		primManager.addPrimitive("remove-patch-view", new RemoveOnePatchView() );
		primManager.addPrimitive("remove-turtle-view", new RemoveOneTurtleView() );
		
		primManager.addPrimitive("count-windows", new GetWindowCount() );
	}
	
	
	//utility (or less user-facing) primitives
	public static class GetWindowCount extends DefaultReporter {
		@Override
		public Object report(Argument[] arg0, Context arg1)
				throws ExtensionException, LogoException {
			return new Double(numWindows());
		}
	}
	
	public static class RemoveOnePatchView extends DefaultCommand {
		@Override
		public Syntax getSyntax() {
			int[] argType = {Syntax.StringType()};
			return  Syntax.commandSyntax( argType );
		}
		
		@Override
		public void perform(Argument[] args, Context arg1)
				throws ExtensionException, LogoException {
			String title = args[0].getString();
			removePatchWindowWithTitle( title );
		}
	}
	
	public static class RemoveOneTurtleView extends DefaultCommand {
		@Override
		public Syntax getSyntax() {
			int[] argType = {Syntax.StringType()};
			return  Syntax.commandSyntax( argType );
		}
		
		@Override
		public void perform(Argument[] args, Context arg1)
				throws ExtensionException, LogoException {
			String title = args[0].getString();
			removeTurtleWindowWithTitle( title );
		}
	}
	//end utility primitives


	@Override
	public void unload(ExtensionManager em) {
		for (VarviewWindow win: windowMap.values() ) {
			if ( win != null ) {
				win.dispose();
			}
		}
		for (TurtleView tv : turtleWindowMap.values() ) {
			if ( tv != null ) {
				tv.dispose();
			}
		}
	}


}
