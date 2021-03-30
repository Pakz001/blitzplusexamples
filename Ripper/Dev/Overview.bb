;
;

Global overviewwin
Global overviewcan

overviewwin = CreateWindow("Over View",ClientWidth(win)-190,94,177,131,win,1)
overviewcan = CreateCanvas(9,6,153,85,overviewwin)

SetBuffer CanvasBuffer(overviewcan)
ClsColor 0,0,0
Cls
SetBuffer CanvasBuffer(can)



