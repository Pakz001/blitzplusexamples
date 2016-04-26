Global win = CreateWindow("Painting Program - Outline fill - Example",100,100,800,600,0,1) 
Global can = CreateCanvas(0,20,800,600,win)

Dim cols(10,3);the colors

Global mw = 48;wapwidth
Global mh = 48;height
Global tw = 640/mw;tilewidth
Global th = 480/mh;height

; closed list (floodfill ea)
Type ol
	Field x
	Field y
End Type
Type cl
	Field x
	Field y
End Type



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
		If EventData()=24;o outline colorarea				
			outlinefill
			updateinterface		
		End If		
		; key 1 to 9 brushsize
		If EventData()>=2 And EventData()<=10
			brushsize = EventData()-1
			updateinterface
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
	For y=0 To 10 
		DrawImage tileimbig,680,y*32,y
		If brushindex = y
			Rect 681,y*32,31,31,False
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
	Text 10,500,"Brush size :"+brushsize+" (press 1 to 9)
	Text 10,515,"Outline Fill (o) - outline is with current brush color"
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
Function outlinefill()
	For this.ol = Each ol
		Delete this
	Next
	For that.cl = Each cl
		Delete that
	Next
	
	Local fillc

	Local st[10]
	st[0] = 0
	st[1] = -1
	st[2] = 1
	st[3] = 0
	st[4] = 0
	st[5] = 1
	st[6] = -1
	st[7] = 0
	If screen="canvas"
	If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)	
		
		Local sx = cmx/tw
		Local sy = cmy/th	

		this.ol = New ol
		this\x = sx 
		this\y = sy
		fillc = map(sx,sy)
		Local xm
		Local my
		While fflistopen() = True
			this.ol = Last ol
			mx = this\x
			my = this\y
			Delete this
			a.cl = New cl
			a\x = mx
			a\y = my
			For i=0 To 6 Step 2
				x = st[i]
				y = st[i+1]
				If mx+x >=0 And mx+x<=mw
				If my+y >=0 And my+y<=mh
				If isonclosedlist(mx+x,my+y)=False
				If map(mx+x,my+y) = fillc				
					z.ol = New ol
					z\x = mx+x
					z\y = my+y
				End If
				End If
				End If
				End If
			Next
		Wend
	End If
	End If
	For e.cl = Each cl
		x1 = e\x
		y1 = e\y
		For i=0 To 6 Step 2
			x2 = st[i]
			y2 = st[i+1]
			If x1+x2 >=0 And x1+x2<=mw
			If y1+y2 >=0 And y1+y2<=mh
				If map(x1+x2,y1+y2)<>fillc
					map(x1+x2,y1+y2)=brushindex
				End If
			End If
			End If
		Next
	Next
DebugLog cnt
End Function

Function fflistopen()
	Local cnt=0
	For this.ol = Each ol
		cnt=cnt+1
	Next
	If cnt>0 Then Return True
	Return False
End Function


Function isonclosedlist(x,y)
	For this.cl = Each cl
		If this\x = x And this\y = y Then Return True
	Next
	Return False
End Function


Function olistopen()
	Local cnt=0
	For this.ol = Each ol
	cnt=cnt+1
	If cnt>0 Then Return True
	Next
	Return False
End Function

Function brushdown(cmx,cmy,ind)
	If brushsize=1 Then map(cmx/tw,cmy/th) = ind
	If brushsize>1 Then
		For y=-brushsize/2 To brushsize/2
		For x=-brushsize/2 To brushsize/2 
			If cmx/tw+x >=0 And cmx/tw+x <=mw
			If cmy/th+y >=0 And cmy/th+y <=mh
			map(cmx/tw+x,cmy/th+y) = ind
			End If
			End If
		Next
		Next
	End If				
End Function
