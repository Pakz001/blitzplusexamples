;
; Worklog
;
; 13 - nov
;Cleaned up the main loop - Everything is placed in functions
;Created a true main b+ loop
;Changed the mousedown in isoturnbased.bb and isoinclude.bb to moused(). moused() is handled by the b+ loop.
;Added a frozen state when the application is not focussed.
;Minor bug testing of surface functionality
;+/- 2 hours
;Added a settings file (settings.txt)
;Implemented the settings file
;+/- 1 hour
; Researched and Added AlphaBlending functionality. One core function and one function wrapper for units.
; +drawalphaunit(unitimage,destbuffer,x,y,alpha#(0.0.1.0)) - limited to small tile images
; Blinking Unit now blinks into full state and partial visible state.
; Added videomem info to the screen (disabled in release version) 6 mb is used atm.
; +/- 2 hours
; Made a custom rotation function for getting a diamond shaped minimap view. Units and cities and selection is not implemented yet.
; isoinclude now has event loops.
; More configuration options added.
; +/- 2 hours
; Spend hours making two functions that rotate x,y coordinates when given a width and height.
; I almost got sidetracked when I noticed the possibilities with this kind of math but when I had
; a real time rotating grid I finished the functions.
; +/- 4 hours
; Finished implementing the new diamond shaped mini map.
; Added more event loops
; +/- 1 hour








; Plan list
; --------------------------------------------------------------------------------------------------------------------------
;
;Quite nice. Not my style of game, but cool anyways. :)
;- - You have a meg of backups in your zip file. :P
;Game control is a bit awkward, And you don't give enough
;
; ---- Control Configuration screen *****
;
;info that a person can play without going through the
; tutorial/thrashiing through it. For the record- I did Not
; (yet). Some of what I note as 'issues' you may have
; implemented- though I have no idea how Or 'how To
; get there from here'. In any Case...
;
; ---- Mission about screen
;
; Modified textreader 	(design)
; 								(Copy)
;								(Adjust)
;								(Implement)
;
;Your huge Select wndow shouldn't obscure screen.
;
; ??
;
;Activated units should probably pop up a tiny menu To
;either move/station/defend/stand down/etc.
;
; --- Right info screen update
;
;
;Pointing at
;a city should give it's ownwership & status- maybe even
; zoom up a larger (6x6)+ top view city map w/'positioned'
; units inside- 'at a glance' is always good.
;
; ---- Right info screen info about city
;
;
;I have no clue (should be listed)...
;How much income I get/where it comes from?
;Will other (beter) units become avail?
;
; ----- Add to Mission about window
;
;
;Can cities be improved/How many degrees?
;
; ---- Add to mission about window
;
;How does terrain modify movement/scouting?
;
; Delayed for later update ***
;
;Is there scouting other than just moving units around?
;
; ?????
;
;
;etc.
;
; [Input error]
;
;Some of this could be implemented with a Right click
; 'ask about' contextual menu. But of course- you are free To do what you want.
;
;
;
;


