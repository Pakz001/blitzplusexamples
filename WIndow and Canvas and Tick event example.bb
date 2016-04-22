Global win = CreateWindow("Window and Canvas Example",0,100,800,600,0,1) 
Global can = CreateCanvas(0,0,800,600,win)

Global timer = CreateTimer(60)

Global mx
Global my

SetBuffer CanvasBuffer(can)
Repeat 
	If WaitEvent()=$203
		mx = EventX()
		my = EventY()		
	End If
	If WaitEvent()=$4001
		Cls
		Text 0,0,mx+","+my
		Rect Rnd(GadgetWidth(can)),Rnd(GadgetHeight(can)),Rnd(100),Rnd(100)
		FlipCanvas can
	End If
	If WaitEvent()=$803 Then End
Forever 

End ; bye! 
