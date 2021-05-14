package view25d;

import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.Command;
import org.nlogo.api.Reporter;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.ExtensionManager;
import org.nlogo.api.LogoException;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;
import org.nlogo.api.PrimitiveManager;

import view25d.prims.DecoratePatchView;
import view25d.prims.GetObserverAngles;
import view25d.prims.GetObserverDistance;
import view25d.prims.GetObserverFocus;
import view25d.prims.GetZScale;
import view25d.prims.MakePatchView;
import view25d.prims.MakeTurtleView;
import view25d.prims.SetLinkDisplayMode;
import view25d.prims.SetObserverAngles;
import view25d.prims.SetObserverDistance;
import view25d.prims.SetObserverFocus;
import view25d.prims.SetTurtleStemThickness;
import view25d.prims.SetTurtleStemColor;
import view25d.prims.SetZScale;
import view25d.prims.UpdateAllViews;
import view25d.prims.UpdateOnePatchView;
import view25d.prims.UpdateOneTurtleView;
import view25d.view.PatchView;
import view25d.view.TurtleView;
import view25d.view.VarviewWindow;
import view25d.view.View25DShapeChangeListener;

import java.util.ArrayList;
import java.util.HashMap;

public class View25DExtension extends DefaultClassManager {

    static public HashMap<String, PatchView> patchWindowMap = new HashMap<String, PatchView>();
    static public HashMap<String, TurtleView> turtleWindowMap = new HashMap<String, TurtleView>();

    public static int numWindows() {
        return turtleWindowMap.size() + patchWindowMap.size();
    }

    //Add a window to the indexed set. Get rid of any existing window with that name
    public static void storePatchWindowWithTitle( String s, PatchView win ){
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


    //Add a window to the indexed set. Remove any existing turtle view.
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

  // When a turtle shape is deleted or added notify all views
  public static void updateTurtleShapesAllViews() {
    ArrayList<String> names =  new ArrayList<String>();
    names.addAll( turtleWindowMap.keySet() );
    for ( String name : names ) {
      TurtleView tv = turtleWindowMap.get(name);
      tv.updateTurtleShapes();
    }

    names.clear();
    names.addAll( patchWindowMap.keySet() );
    for ( String name : names ) {
      PatchView pv = patchWindowMap.get(name);
      pv.updateTurtleShapes();
    }
  }

    //run at extension startup.  ensure that the NetLogo native JOGL is loaded.
    @Override
    public void runOnce(ExtensionManager em) throws ExtensionException {
      View25DShapeChangeListener.listen();
    }

    @Override
    public void load(PrimitiveManager primManager) throws ExtensionException {
        primManager.addPrimitive("patch-view", new MakePatchView() );
        primManager.addPrimitive("decorate-patch-view", new DecoratePatchView(true) );
        primManager.addPrimitive("undecorate-patch-view", new DecoratePatchView(false) );

        primManager.addPrimitive("turtle-view", new MakeTurtleView() );

        primManager.addPrimitive("update-patch-view", new UpdateOnePatchView() );
        primManager.addPrimitive("update-all-patch-views", new UpdateAllViews() );

        primManager.addPrimitive("update-turtle-view", new UpdateOneTurtleView() );

        primManager.addPrimitive("remove-patch-view", new RemoveOnePatchView() );
        primManager.addPrimitive("remove-turtle-view", new RemoveOneTurtleView() );
        primManager.addPrimitive("remove-all-patch-views", new RemoveAllPatchViews() );
        primManager.addPrimitive("remove-all-turtle-views", new RemoveAllTurtleViews() );

        primManager.addPrimitive("count-windows", new GetWindowCount() );

        primManager.addPrimitive("set-z-scale", new SetZScale() );
        primManager.addPrimitive("get-z-scale", new GetZScale() );

        primManager.addPrimitive("set-turtle-stem-thickness", new SetTurtleStemThickness() );
        primManager.addPrimitive("set-turtle-stem-color", new SetTurtleStemColor() );

        primManager.addPrimitive("get-observer-angles", new GetObserverAngles() );
        primManager.addPrimitive("set-observer-angles", new SetObserverAngles() );

        primManager.addPrimitive("get-observer-distance", new GetObserverDistance() );
        primManager.addPrimitive("set-observer-distance", new SetObserverDistance() );
        primManager.addPrimitive("get-observer-xy-focus", new GetObserverFocus() );
        primManager.addPrimitive("set-observer-xy-focus", new SetObserverFocus() );

        primManager.addPrimitive("show-links-xy-plane", new SetLinkDisplayMode(true) );
        primManager.addPrimitive("show-links-xyz", new SetLinkDisplayMode(false) );
    }

    /*public static class DecoratedPatchView extends DefaultCommand {

        @Override
        public void perform(Argument[] arg0, Context arg1)
                throws ExtensionException, LogoException {
            // TODO Auto-generated method stub

        }

    }*/



    //utility (or less user-facing) primitives
    public static class GetWindowCount implements Reporter {
        @Override
        public Syntax getSyntax() {
            return SyntaxJ.reporterSyntax(Syntax.NumberType());
        }

        @Override
        public Object report(Argument[] arg0, Context arg1)
                throws ExtensionException, LogoException {
            return Double.valueOf(numWindows());
        }
    }

    public static class RemoveOnePatchView implements Command {
        @Override
        public Syntax getSyntax() {
            int[] argType = {Syntax.StringType()};
            return  SyntaxJ.commandSyntax( argType );
        }

        @Override
        public void perform(Argument[] args, Context arg1)
                throws ExtensionException, LogoException {
            String title = args[0].getString();
            removePatchWindowWithTitle( title );
        }
    }

    public static class RemoveOneTurtleView implements Command {
        @Override
        public Syntax getSyntax() {
            int[] argType = {Syntax.StringType()};
            return  SyntaxJ.commandSyntax( argType );
        }

        @Override
        public void perform(Argument[] args, Context arg1)
                throws ExtensionException, LogoException {
            String title = args[0].getString();
            removeTurtleWindowWithTitle( title );
        }
    }

    public static class RemoveAllTurtleViews implements Command {
        @Override
        public Syntax getSyntax() {
            return  SyntaxJ.commandSyntax();
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

    public static class RemoveAllPatchViews implements Command {
        @Override
        public Syntax getSyntax() {
            return SyntaxJ.commandSyntax();
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
