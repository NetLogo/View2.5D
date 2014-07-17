package varchart;

import java.util.HashMap;


import org.nlogo.api.DefaultClassManager;

import org.nlogo.api.ExtensionException;
import org.nlogo.api.ExtensionManager;
import org.nlogo.api.PrimitiveManager;

import org.nlogo.gl.render.JOGLException;
import org.nlogo.gl.render.JOGLLoader;

import varchart.prims.MakePatchView;
import varchart.prims.MakeTurtleView;
import varchart.prims.UpdateAllViews;
import varchart.prims.UpdateOneView;
import varchart.prims.UpdateTurtleView;
import varchart.view.TurtleView;
import varchart.view.VarviewWindow;

public class VarchartExtension extends DefaultClassManager {

	static private Integer windex = 0;
	static public HashMap<Integer, VarviewWindow> windowMap = new HashMap<Integer, VarviewWindow>();
	
	static private Integer tindex = 0;
	static public HashMap<Integer, TurtleView> turtleWindowMap = new HashMap<Integer, TurtleView>();
	
	public static int numWindows() { return windex + tindex; }
	
	//Get next available index for windows (patch)
	 public static Integer getNextIndex() {
		windex++;
		return windex;
	}
	
	//Add a window to the indexed set.  Only do so if the proposed index is correct and if the spot is open.
	public static int storeWindowAtIndex( Integer i, VarviewWindow win ){
		if ( i.equals(windex) && !windowMap.containsKey(i) ) {
			windowMap.put(i,win);
			return windex;
		}
		else {
			return -1; 
		}
		
	}
	
	//Get next available index for turtle views
	public static Integer getNextTurtleIndex() {
		tindex++;
		return tindex;
	}

	//Add a window to the indexed set.  Only do so if the proposed index is correct and if the spot is open.
	public static int storeTurtleWindowAtIndex( Integer i, TurtleView win ){
		if ( i.equals(tindex) && !turtleWindowMap.containsKey(i) ) {
			turtleWindowMap.put(i,win);
			return tindex;
		}
		else {
			return -1; 
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
		primManager.addPrimitive("patch", new MakePatchView() );
		primManager.addPrimitive("turtle", new MakeTurtleView() );
		
		primManager.addPrimitive("update", new UpdateOneView() );
		primManager.addPrimitive("update-all", new UpdateAllViews() );
		
		primManager.addPrimitive("update-turtle-view", new UpdateTurtleView() );
	}

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
