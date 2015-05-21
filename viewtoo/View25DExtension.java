package viewtoo;

import org.nlogo.api.Agent;
import org.nlogo.api.AgentSet;
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
import org.nlogo.nvm.ExtensionContext;
import org.nlogo.nvm.Reporter;


import viewtoo.prims.GetObserverPerspective;
import viewtoo.prims.GetZScale;
import viewtoo.prims.MakePatchView;
import viewtoo.prims.MakeTurtleView;
import viewtoo.prims.SetObserverPerspective;
import viewtoo.prims.SetTurtleStemThickness;
import viewtoo.prims.SetZScale;
import viewtoo.prims.UpdateAllViews;
import viewtoo.prims.UpdateOnePatchView;
import viewtoo.prims.UpdateOneTurtleView;
import viewtoo.view.TurtleView;
import viewtoo.view.VarviewWindow;

import java.util.ArrayList;
import java.util.HashMap;

public class View25DExtension extends DefaultClassManager {

	static public HashMap<String, VarviewWindow> patchWindowMap = new HashMap<String, VarviewWindow>();
	static public HashMap<String, TurtleView> turtleWindowMap = new HashMap<String, TurtleView>();
		
	public static int numWindows() {
		return turtleWindowMap.size() + patchWindowMap.size();
	}
	
	//Add a window to the indexed set. Get rid of any existing window with that name
	public static void storePatchWindowWithTitle( String s, VarviewWindow win ){
		removePatchWindowWithTitle(s);
		patchWindowMap.put(s,win);		
	}
	
	//remove a patch window with given name, if any exists
	public static void removePatchWindowWithTitle(String name) {
		if (  patchWindowMap.containsKey(name) ) {
			VarviewWindow vvw = patchWindowMap.get(name);
			patchWindowMap.remove(name);
			vvw.dispose();
		}
	}
	

	//Add a window to the indexed set. Remove any existing t view.
	public static void storeTurtleWindowWithTitle( String s, TurtleView win ){
		removeTurtleWindowWithTitle(s);
		turtleWindowMap.put(s,win);
	}

	//remove a turtle window with given name, if any exists
	public static void removeTurtleWindowWithTitle(String name) {
		if (  turtleWindowMap.containsKey(name) ) {
			VarviewWindow tvw = turtleWindowMap.get(name);
			turtleWindowMap.remove(name);
			tvw.dispose();
		}
	}
	

	//run at extension startup.  ensure that the NetLogo native JOGL is loaded.
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
		//primManager.addPrimitive("patch-view-with-turtles", new DecoratedPatchView() );
		primManager.addPrimitive("turtle-view", new MakeTurtleView() );
		
		primManager.addPrimitive("update-patch-view", new UpdateOnePatchView() );
		primManager.addPrimitive("update-all-patch-views", new UpdateAllViews() );
		
		primManager.addPrimitive("update-turtle-view", new UpdateOneTurtleView() );
		
		primManager.addPrimitive("remove-patch-view", new RemoveOnePatchView() );
		primManager.addPrimitive("remove-turtle-view", new RemoveOneTurtleView() );
		primManager.addPrimitive("remove-all-patch-views", new RemoveAllPatchViews() );
		primManager.addPrimitive("remove-all-turtle-views", new RemoveAllTurtleViews() );
		
		
		primManager.addPrimitive("count-windows", new GetWindowCount() );
		
		primManager.addPrimitive("testblock", new TestBlock() );
		
		primManager.addPrimitive("set-z-scale", new SetZScale() );
		primManager.addPrimitive("get-z-scale", new GetZScale() );
		
		primManager.addPrimitive("set-turtle-stem-thickness", new SetTurtleStemThickness() );
		
		primManager.addPrimitive("set-observer-perspective", new SetObserverPerspective() );
		primManager.addPrimitive("get-observer-perspective", new GetObserverPerspective() );
	}
	
	/*public static class DecoratedPatchView extends DefaultCommand {

		@Override
		public void perform(Argument[] arg0, Context arg1)
				throws ExtensionException, LogoException {
			// TODO Auto-generated method stub
			
		}
		
	}*/
	
	
	//TESTING TOWARD GETTING BETTER SYNTAX THAN THE TASK ?1 SYNTAX.
	public static class TestBlock extends DefaultReporter {
		
		@Override
		public String getAgentClassString() {
			return "OTPL:OTPL";
		}
		
		@Override
		public Syntax getSyntax() {
			int[] argType = {Syntax.AgentsetType(), Syntax.NumberBlockType()};
			int retType = Syntax.NumberType();
			return  Syntax.reporterSyntax( argType, retType, "OTPL", "?" );
		}

		@Override
		public Object report(Argument[] args, Context ctx) throws ExtensionException, LogoException {
			AgentSet as = args[0].getAgentSet();
			Reporter reporter = ((org.nlogo.nvm.Argument)args[1]).getReporter();

			try {
				Double resul = 0.0;
				for (Agent a: as.agents() ) {
					org.nlogo.agent.Agent agent = (org.nlogo.agent.Agent)a;						
					org.nlogo.nvm.Context con = ((ExtensionContext)ctx).nvmContext();
					resul += (Double)(new org.nlogo.nvm.Context(con, agent).evaluateReporter(agent, reporter));
				}
				return resul;
			}
			catch (Exception e1) {
				throw new ExtensionException("Error in processing your reporter. " + e1);
			} 
		}
		
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
		public String getAgentClassString() {
			return "O";
		}
		
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
		public String getAgentClassString() {
			return "O";
		}
		
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
	
	public static class RemoveAllTurtleViews extends DefaultCommand {
		@Override
		public String getAgentClassString() {
			return "O";
		}
		
		@Override
		public void perform(Argument[] args, Context arg1)
				throws ExtensionException, LogoException {
			ArrayList<String> namesToDelete =  new ArrayList<String>();
			namesToDelete.addAll( turtleWindowMap.keySet() );
			for ( String name : namesToDelete ) {
				VarviewWindow tvw = turtleWindowMap.get(name);
				turtleWindowMap.remove(name);
				tvw.dispose();
			}
		}
	}

	public static class RemoveAllPatchViews extends DefaultCommand {
		@Override
		public String getAgentClassString() {
			return "O";
		}
		
		@Override
		public void perform(Argument[] args, Context arg1)
				throws ExtensionException, LogoException {
			
			ArrayList<String> namesToDelete =  new ArrayList<String>();
			namesToDelete.addAll( patchWindowMap.keySet() );
			for ( String name : namesToDelete ) {
				VarviewWindow vvw = patchWindowMap.get(name);
				patchWindowMap.remove(name);
				vvw.dispose();
			}
		}
	}
	//end utility primitives


	
	public static void disposeAllPatchViews() {
		for (VarviewWindow win: patchWindowMap.values() ) {
			if ( win != null ) {
				win.dispose();
			}
		}
	}
	
	public static void disposeAllTurtleViews() {
		for (TurtleView tv : turtleWindowMap.values() ) {
			if ( tv != null ) {
				tv.dispose();
			}
		}
	}
	//ensure that the windows are cleaned up when we unload the extension
	@Override
	public void unload(ExtensionManager em) {
		disposeAllPatchViews();
		disposeAllTurtleViews();
	}


}
