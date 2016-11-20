;
; Go Clock tick X
;
; Find the X
;
; Mouse movement modifier
; Countdown 60 second rounds
; Quit upon loosing
;
Graphics 800,600,16,2
SetBuffer BackBuffer()
;
Global ms = 0
;
SeedRnd MilliSecs()
;
Dim alpha(256,20,20)
;
Type game
	Field part[128]	
	Field fontbmap[256]
End Type
	;
	Global g.game = New game
	;
	For i=0 To 24
		g\part[0] = CreateImage(128,128)
	Next
	;
	For i=0 To 256
		SetBuffer BackBuffer()
		Color 255,0,0
		Rect 0,0,GraphicsWidth(),GraphicsHeight(),True
		njet =Rand(GraphicsHeight())
		Color Rand(150,255),0,0
		Rect 0,njet,GraphicsWidth(),Rand(64,320),True
		g\fontbmap[i] = CreateImage(20,20)
		SetBuffer ImageBuffer(g\fontbmap[i])
		Color 255,255,255
		Text 10,10,Chr(i),1,1
		Flip
		For y=0 To 20
		For x=0 To 20
			GetColor x,y
			r = ColorRed()
			If r>0 Then alpha(i,x,y) = True;
		Next:Next
		SetBuffer BackBuffer()		
		Next
		;
		.mus
		Cls
		Color 255,30,20
		Text GraphicsWidth()-200,0,"Go Clock Tick X"
		Text GraphicsWidth()-200,20,Clock
		Dim alphaxy(1,1)
		;
		;
		Color 255,255,0
		Rect alphaxy(0,1)+12,alphaxy(1,0)+12,16,16,False
		Flip
		ms = MilliSecs() + 1000
		at#=0
		;
		drawchar
		ronde = 1
		stretch 100,200,400,100,"Find the X",0
		Flip
		Delay 1000:Cls
		drawchar
		ex=False
		clock = 0
		up = True
		timer = CreateTimer(40)
		While Ex = False
			we = WaitEvent()
			If we = $203
				If RectsOverlap(MouseX(),MouseY(),12,12,alphaxy(0,1)+12,alphaxy(1,0)+12,12,12) = True Then 
					stretch 100,100,400,100,"You FOUND X"
					Flip
					Delay 3000
					Cls
					ex = True
				End If
				
				If (oldmx <> MouseX()) And (oldmy <> MouseY()) Then
					at = at + 2
					;DebugLog a
					oldmx = MouseX()
					oldmy = MouseX()
					up = True
				End If
			End If
			If we = $4001
				;
				If ms < MilliSecs() Then
					clock = clock + 1
					ms = MilliSecs() + 1000
					up = True
				End If
				If up = True Then
					If Rand(2) = True Then
						Color Rand(100,255),0,0
						dokken = True
					End If
					If dokken = False Then Color 255,0,0
					Rect GraphicsWidth()-240,260,at,20,True
					Color 255,0,0
				End If
				If at > 160 Then ex = True
				If 60 - clock < 0 Then ex = True
				If up = True Then Flip True
				VWait
				If KeyDown(1) = True Then End
				;
				If gogem = False Then										
					stretch GraphicsWidth()-240,0,200,100,"Go Clock Tick X"
					stretch GraphicsWidth()-240,200,200,100,"(vind de X)"
					stretch GraphicsWidth()-240,300,200,100,"Ronde : " + ronde
					stretch GraphicsWidth()-240,400,200,100,"Score : " + tijd
					gogem = True
				End If
				If up = True Then
					stretch GraphicsWidth()-240,100,200,100,"(" + (60 - clock) + ")"
				End If				
				;
				If up = True Then up = False
				If dokken = True Then dokken = False: up=True
			End If
		Wend
		If Not clock = 60 Then 
			If at<160 Then
			tijd = tijd + ( 60 - clock ) + (160-at)
			End If
		End If
		ronde = ronde + 1
		gogem = False
		Goto mus
		;	
	Function stretch(x,y,xw,yw,t$,fl = True)
		;
		abc = CreateImage(150,15)
		HandleImage abc,0,0
		TFormFilter fl
		SetBuffer ImageBuffer(abc)
		;
		For i=0 To 10
			Color i*2,i/2,i/3
			Line 0,i,150,i
		Next
		;
		For i=0 To 10 
			Color i*2,i/2,i/3
			Line 0,i+10,150,i+10
		Next
		;
		Color 200,0,0
		Text ImageWidth(abc)/2,ImageHeight(abc)/2,t$,1,1
		Flip
		SetBuffer BackBuffer()
		ResizeImage abc,xw,yw
		DrawBlock abc,x,y
		FreeImage abc
		;
	End Function
	;
	Function drawchar()
		For i=0 To 256
			Color Rand(200,255),0,0
			ax1 = Rand(260)
			ay1 = Rand(260)
			If i = 88 Then
				alphaxy(0,1) = ax1 * 2
				alphaxy(1,0) = ay1 * 2
			End If
			;
			For y=0 To 20
			For x=0 To 20
				If alpha(i,x,y) = True Then
					Rect (x+ax1)*2,(y+ay1)*2,2,2
				End If
			Next:Next
		Next
	End Function
	;  
