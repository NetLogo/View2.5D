package varchart;

import java.util.HashMap;

import org.nlogo.api.Agent;
import org.nlogo.api.AgentSet;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.DefaultReporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.ExtensionManager;
import org.nlogo.api.LogoException;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.api.ShapeList;
import org.nlogo.api.Syntax;
import org.nlogo.api.World;
import org.nlogo.gl.render.GLLinkShape;
import org.nlogo.gl.render.JOGLException;
import org.nlogo.gl.render.JOGLLoader;
import org.nlogo.gl.render.ShapeRenderer;
import org.nlogo.shape.VectorShape;
import org.nlogo.api.ReporterTask;
import org.nlogo.app.App;

import varchart.prims.MakeBarView;
import varchart.prims.MakeMeshView;
import varchart.prims.MakePatchView;
import varchart.prims.UpdateAllViews;
import varchart.prims.UpdateOneView;
import varchart.view.DummyView;
import varchart.view.VarviewWindow;

public class VarchartExtension extends DefaultClassManager {

	static private Integer windex = 0;
	static public HashMap<Integer, VarviewWindow> windowMap = new HashMap<Integer, VarviewWindow>();
	
	//Get next available index
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
		
		//org.nlogo.gl.render.Renderer glr = (org.nlogo.gl.render.Renderer) App.app().workspace().view.renderer)
	}
	
	public static void frankDoesRock()  {
		World w = App.app().workspace().world;
		ShapeList tls = w.turtleShapeList();
		for (String s : tls.getNames())
			System.err.println(s);
				
		//ShapeRenderer s = new ShapeRenderer(w);
		//App.app().workspace().world.turtleShapeList().getShapes().get(0).
		//GLLinkShape ls = s.getLinkShape( "sheep" );
		//System.err.println(ls.toString());
		
		//org.nlogo.gl.render.ShapeManager sm = new ShapeManager;
		//s.renderAgent(gl, shape3D, color, size, xcor, ycor, zcor, stroke, outline, orientation)
		
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
		primManager.addPrimitive("dummy", new GLSandbox() );
		primManager.addPrimitive("patch", new MakePatchView() );
		
		primManager.addPrimitive("mesh", new MakeMeshView() );
		primManager.addPrimitive("bar", new MakeBarView() );
		primManager.addPrimitive("update", new UpdateOneView() );
		primManager.addPrimitive("update-all", new UpdateAllViews() );
		primManager.addPrimitive("turtle-map", new TestTurtleMap() );
	}

	
	
	
	private class TestTurtleMap extends DefaultReporter {
		@Override
	    public Syntax getSyntax() {
	       int[] argTypes = {Syntax.AgentsetType(), Syntax.ReporterTaskType()};
	       return  Syntax.reporterSyntax( argTypes , Syntax.NumberType());
	    }


		@Override
		public Object report(Argument[] args, Context context)
				throws ExtensionException, LogoException {
			AgentSet as = args[0].getAgentSet();
			ReporterTask rt = args[1].getReporterTask();
			//org.nlogo.nvm.ReporterTask ret = ((org.nlogo.nvm.ReporterTask)rt);
			Double d = 0.0;
			//org.nlogo.nvm.ExtensionContext c = ((org.nlogo.nvm.ExtensionContext)context);
			//org.nlogo.nvm.Context co = c.nvmContext();
			for (Agent a : as.agents() ) {
				//VectorShape vs = (VectorShape)App.app().workspace().world().turtleShapeList().shape("circle");
				//...cast to vectorshape
				Object o = rt.report(context, new Object[]{a});
				//Object o1 = ret.report(co, new Object[]{a});
				d = d + (Double)o;
			}
			return d.doubleValue();
		}
		
	}
	
	
	//for testing only
	private class GLSandbox extends DefaultCommand {
		public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {
//			final VarviewWindow jsbox = new DummyView("test", 1);
//			jsbox.setVisible(true);
			frankDoesRock();
			JOGLSandbox.doIt();
		}
	}

}
