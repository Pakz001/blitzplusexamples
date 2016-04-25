Global win = CreateWindow("Painting program - Protected colors - Example",100,100,800,600,0,1) 
Global can = CreateCanvas(0,20,800,600,win)

Dim cols(10,3);the colors

; This array contains the colors that are protected
; true/false
Dim protcol(10)

Global mw = 48;wapwidth
Global mh = 48;height
Global tw = 640/mw;tilewidth
Global th = 480/mh;height

Dim map(mw,mh);create map
Global canim = CreateImage(800,600);the image (speedup)

Global brushindex=5;brushcolor
Global brushsize=4
Global cmx;mouse x pos
Global cmy; mouse y pos

Global tileim = CreateImage(tw,th,11)
Global tileimbig = CreateImage(32,32,11)

; create the colors and images
refreshtileimages(True)

; our area of operation
Global screen$="canvas"


updateinterface
.main
Repeat 
	we = WaitEvent()
	If we=$102;keyup
		If screen = "canvas"
		If EventData()=25 ;p pick color
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				brushindex = map(cmx/tw,cmy/th)
				updateinterface
			End If
		End If		
		; brush size 1/9
		If EventData()>=2 And EventData()<=10
			brushsize = EventData()-1
			updateinterface
		End If
		If EventData()=18;e
			If explode = True Then explode = False Else explode = True
		End If
		End If		
	End If
	If we=$201;mosuedown
	End If
	If we=$202;mouseup
		If EventSource() = can
			If EventData() = 1
				If RectsOverlap(cmx,cmy,1,1,680,0,32,11*32)
					brushindex=cmy/32
					updateinterface
				End If
				; undestructable checkboxes
				If RectsOverlap(cmx,cmy,1,1,712,0,16,11*32)					
					If protcol(cmy/32) = True
						protcol(cmy/32) = False
						Else
						protcol(cmy/32) = True
					End If					
					updateinterface
				End If
				If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
					brushdown(cmx,cmy,brushindex)
					updateinterface
				End If
			End If
			If EventData() = 2
				If RectsOverlap(cmx,cmy,1,1,680,0,32,11*32)
					If RequestColor()=True
						brushindex=cmy/32
						cols(brushindex,0) = RequestedRed()
						cols(brushindex,1) = RequestedGreen()
						cols(brushindex,2) = RequestedBlue()
						refreshtileimages
						updateinterface
					End If
				End If
				; undestructable checkboxes
				If RectsOverlap(cmx,cmy,1,1,712,0,16,11*32)					
					For i=0 To 10
					protcol(i)=False
					Next
					If protcol(cmy/32) = True
						protcol(cmy/32) = False
						Else
						protcol(cmy/32) = True
					End If
					updateinterface
				End If		
				If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
					brushdown(cmx,cmy,0)
					updateinterface
				End If
			End If
		End If
	End If
	If we=$203;mousemove
		If EventSource()=can
			cmx = EventX()
			cmy = EventY()			
			If MouseDown(1) = True
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				brushdown(cmx,cmy,brushindex)
				updateinterface
			End If
			End If
			If MouseDown(2) = True
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				brushdown(cmx,cmy,0)
				updateinterface
			End If
			End If
			updateinterface			
		End If
	End If
	If we=$401;gadgetaction
	End If
	If we=$4001
	End If	
	If we=$803 Then Exit 
Forever 
End

.funcs

Function updateinterface()
	SetBuffer ImageBuffer(canim)
	Cls
	Color 255,255,255
	For y=0 To mh-1
	For x=0 To mw-1
		DrawImage tileim,x*tw,y*th,map(x,y)
	Next
	Next
	Color 255,255,255
	For y=0 To 10 
		DrawImage tileimbig,680,y*32,y
		If brushindex = y		
			Rect 681,y*32,31,31,False
		End If
		; the indestructable checkbox
		Rect 712,y*32,16,32,False
		If protcol(y) = True
			Rect 714,y*32,12,30,True
		End If
	Next
	For y=0 To 47
	For x=0 To 47
		a = map(x,y)
		Color cols(a,0),cols(a,1),cols(a,2)
		Rect x*2+660,y*2+370,2,2
	Next
	Next
	; draw the brush view
	Color 255,255,255
	Rect cmx-brushsize*tw/2,cmy-brushsize*th/2,brushsize*tw,brushsize*th,False	
	Text 10,490,"Brush size :"+brushsize+" (press 1 to 9)
	Text 10,510,"Pick color (p)"
	
	SetBuffer CanvasBuffer(can)
	Cls
	DrawImage canim,0,0
	FlipCanvas can
End Function
Function refreshtileimages(init=False)
	For i = 0 To 10
		SetBuffer ImageBuffer(tileim,i)
		If init=True
			Color 200+i*3,10+i*20,10
			cols(i,0) = ColorRed()
			cols(i,1) = ColorGreen()
			cols(i,2) = ColorBlue()			
		End If
		Color cols(i,0),cols(i,1),cols(i,2)
		Rect 0,0,tw,th,True
		SetBuffer ImageBuffer(tileimbig,i)
		Color cols(i,0),cols(i,1),cols(i,2)
		Rect 0,0,32,32
		For x=-1 To 1
		For y=-1 To 1
			Color 4,4,4
			Text tw/2+x,th/2+y,i,1,1
		Next
		Next
		Color 255,255,255
		Text tw/2,th/2,i,1,1
	Next
End Function
.brushfuncs
Function brushdown(cmx,cmy,ind)
	If brushsize=1 Then map(cmx/tw,cmy/th) = ind
	If brushsize>1 Then
		For y=-brushsize/2 To brushsize/2
		For x=-brushsize/2 To brushsize/2 
			If cmx/tw+x >=0 And cmx/tw+x <=mw
			If cmy/th+y >=0 And cmy/th+y <=mh
			If protcol(map(cmx/tw+x,cmy/th+y)) = False
			map(cmx/tw+x,cmy/th+y) = ind
			End If
			End If
			End If
		Next
		Next
	End If				
End Function
