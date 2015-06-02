##View2.5D Extension for NetLogo

###Purpose:
To offer visualization for Patch and Turtle reporters, in real time, in a simulation's context.

###Status:
- Version 0.1 released 08/26/14
- Version 0.9 presented CCL 05/22/15

###Primitives:

- view2.5d:patch-view {Title: String} {Reporter: Task}
> This *command* primitive must be called from the Observer context.  (Attempting to call from another context causes an error)
> The Title is a String, which will be used to label the new Window and to call for subsequent updates and modifications.
> Specification of the Reporter uses the NetLogo task syntax, from the Observer perspective.  
>> Example:  
>> view2.5d:patch-view "Test" [[pxcor] of ?1]

- view2.5d:decorate-patch-view {Title: String} 
> This *command* primitive must be called from the Observer context.  (Attempting to call from another context causes an error)
> The Title is a String, the label of an existing Patch View Window.
> Effect:  draws the turtles of the model at their current location, on top of the Patch view display
> NOTE: only has an effect in the "structures" patch view (in the others, the patch value is inclined based on neighbors & gradient)
> NOTE: for negative patch values, the turtle shapes are drawn below (orbit underneath to see them)
>> Example:  
>> view2.5d:decorate-patch-view "Test" 

- view2.5d:undecorate-patch-view {Title: String} 
> This *command* primitive must be called from the Observer context.  (Attempting to call from another context causes an error)
> The Title is a String, the label of an existing Patch View Window.
> Effect:  STOPS drawing the turtles of the model at their current location, on top of the Patch view display
>> Example:  
>> view2.5d:undecorate-patch-view "Test"

- view2.5d:turtle-view {Title: String} {Agents: TurtleSet} {Reporter: Task}
> This *command* primitive must be called from the Observer context.  (Attempting to call from another context causes an error)
> The Title is a String, which will be used to label the new Window and to call for subsequent updates.
> The turtle-set is any selector for turtles.
> Specification of the Reporter uses the NetLogo task syntax, from the Observer perspective.
>> Example:  
>> view2.5d:turtle-view "Test" turtles with [color = red] [[energy] of ?1]
>> This would create a new 2.5d window, plotting the ENERGY value of all turtles that are red.

- view2.5d:update-all-patch-views
> This *command* primitive must be called from the Observer context.  
> Updates all existing patch-view windows according to the latest values.

- view2.5d:update-patch-view {Title: String}
> This *command* primitive must be called from the Observer context.
> Updates only the patch-view window with the specified title (if any).

- view2.5d:update-turtle-view {Title: String} {Agents: TurtleSet}
> This *command* primitive must be called from the Observer context.  
> Updates only the turtle-view window with the specified title (if any). 
> The turtle-set selector must be supplied to refresh the set of turtles.

- view2.5d:get-z-scale {Title: String} 
> This *reporter* primitive must be called from the Observer context.  
> Returns the current z-scale of the turtle-view or patch-view window with the specified title (if any). 

- view2.5d:set-z-scale {Title: String} {Double: new z-scale}
> This *command* primitive must be called from the Observer context.  
> Updates only the turtle-view or patch-view window with the specified title (if any). 
> The view is now displayed with the new z-scale.

- view2.5d:set-turtle-stem-thickness {Title: String} {Double: thickness}
> This *command* primitive must be called from the Observer context.  
> Updates only the turtle-view window with the specified title (if any). 
> Turtles are now drawn with "pins" or "stems" that have the specified thickness (instead of the hairline default)

- view2.5d:get-observer-angles {Title: String} 
> This *reporter* primitive must be called from the Observer context.  
> Returns a list reflecting the observer's angular perspective { heading pitch } (the place on an imaginary sphere at the zoom distance is updated to obey heading & pitch given)

- view2.5d:set-observer-angles {Title: String} {Double: heading} {Double: pitch}
> This *command* primitive must be called from the Observer context.  
> Updates only the turtle-view window with the specified title (if any). 
> Sets the observer's angular perspective (the place on an imaginary sphere at the zoom distance is updated to obey heading & pitch given)

- view2.5d:get-observer-xy-focus {Title: String} 
> This *reporter* primitive must be called from the Observer context.  
> Returns a list reflecting the x and y coordinates the observer is "looking at" in the patch plane 

- view2.5d:set-observer-xy-focus {Title: String} {Double: xcor} {Double: ycor}
> This *command* primitive must be called from the Observer context.  
> Updates only the turtle-view window with the specified title (if any). 
> Sets the x and y coordinates the observer that is "looking at" in the patch plane

- view2.5d:get-observer-distance {Title: String} 
> This *reporter* primitive must be called from the Observer context.  
> Returns the observer's distance from its "focus point"

- view2.5d:set-observer-distance {Title: String} {Double: new distance} 
> This *command* primitive must be called from the Observer context.  
> Updates only the turtle-view window with the specified title (if any). 
> Sets the observer's distance from its "focus point"


####OTHER LESS IMPORTANT PRIMITIVES:

- view2.5d:remove-patch-view {Title: String}
> This *command* primitive closes and removes the specified patch view programmatically (equivalent to closing the window manually).

- view2.5d:remove-turtle-view {Title: String}
> This *command* primitive closes and removes the specified turtle view programmatically (equivalent to closing the window manually).

- view2.5d:remove-all-patch-views 
> This *command* primitive closes and removes all patch views programmatically (equivalent to closing the windows manually).

- view2.5d:remove-all-turtle-views 
> This *command* primitive closes and removes all turtle views programmatically (equivalent to closing the windows manually).


- view2.5d:count-windows
> This *reporter* primitive returns the number of turtle and patch views that are currently active.


###Building:
Follow instructions for building extensions, found at http://ccl.northwestern.edu/netlogo/docs/extensions.html
and https://github.com/NetLogo/NetLogo/wiki/Extensions-API
- Compile as view2.5d.jar, building against NetLogo.jar file for NetLogo 5.0.x or 5.1
- Use the manifest.txt file in this repo
- Place the result in a folder called 'view2.5d' as a subfolder of the extensions folder of your NetLogo distribution

###Incorporating in models:
- declare your use of the extension via
> extensions [ view2.5d ]
> 
- open a window using either the view2.5d:patch-view or view2.5d:turtle-view commands 
> (often in your 'SETUP' procedure or via a separate button).
> 
- update your window's view using one of the update commands 
> (often in your 'GO' procedure)
> 
- See also examples in the 'release'

###Comments & Complaints:
Please send to Corey Brady, cbrady@northwestern.edu

