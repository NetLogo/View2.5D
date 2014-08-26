View2.5D Extension for NetLogo

Purpose: To offer visualization for Patch and Turtle reporters, in real time, in a simulation's context.

Status: Version 0.1 released 08/26/14

Primitives:

view2.5d:patch-view <Title: String> <Reporter: Task>
This primitive must be called from the Observer context.  (Attempting to call from another context causes an error)
The Title is a String, which will be used to label the new Window and to call for subsequent updates.
Specification of the Reporter uses the NetLogo task syntax, from the Observer perspective.  
Example:  
view2.5d:patch-view "Test" [[pxcor] of ?1]

view2.5d:turtle-view <Title: String> <Agents: TurtleSet> <Reporter: Task>
This primitive must be called from the Observer context.  (Attempting to call from another context causes an error)
The Title is a String, which will be used to label the new Window and to call for subsequent updates.
The turtle-set is any selector for turtles.
Specification of the Reporter uses the NetLogo task syntax, from the Observer perspective.
Example:  
view2.5d:turtle-view "Test" turtles with [color = red] [[energy] of ?1]
This would create a new 2.5d window, plotting the ENERGY value of all turtles that are red.

view2.5d:update-all-patch-views
Must be called from the Observer context.  Updates all existing patch-view windows according to the latest values.

view2.5d:update-patch-view <Title: String>
Must be called from the Observer context.  Updates only the patch-view window with the specified title (if any).

view2.5d:update-turtle-view <Title: String> <Agents: TurtleSet>
Must be called from the Observer context.  Updates only the turtle-view window with the specified title (if any). 
The turtle-set selector must be supplied to refresh the set of turtles.

OTHER LESS IMPORTANT PRIMITIVES:

view2.5d:remove-patch-view <Title: String>
Closes and removes the specified patch view programmatically (equivalent to closing the window manually).

view2.5d:remove-turtle-view <Title: String>
Closes and removes the specified turtle view programmatically (equivalent to closing the window manually).

view2.5d:count-windows
Reporter giving the number of turtle and patch views that are currently active.


