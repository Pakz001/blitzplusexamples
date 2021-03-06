;
;
;

Graphics 640,480,16,2
SetBuffer BackBuffer()

Global unitimage = CreateImage(8,8)
Global map = LoadImage("world100b.png")

SetBuffer ImageBuffer(unitimage)
Color 255,255,255
Oval 0,0,8,8,True
SetBuffer BackBuffer()
SetBuffer ImageBuffer(map)
For x=0 To ImageWidth(map)
For y=0 To ImageHeight(map)
GetColor x,y
If ColorRed() = 0 And ColorGreen() = 0 And ColorBlue() = 0 Then Color 0,200,0 : Plot x,y
Next:Next
SetBuffer BackBuffer()
MaskImage map,255,255,255
TFormFilter False
ResizeImage(map,640,480)
;
Dim zonear(32,32)
Global zoneim = CreateImage(32,32)
SetBuffer ImageBuffer(zoneim)
Color 255,255,255
Oval 0,0,32,32,True
For y=0 To 32
For x=0 To 32
	GetColor x,y
	If ColorRed() = 255 And ColorGreen() = 255 And ColorBlue() = 255 Then
		zonear(x,y) = True
	End If
Next:Next
SetBuffer BackBuffer()

Dim zonear2(34,34)
Global zoneim2 = CreateImage(34,34)
SetBuffer ImageBuffer(zoneim2)
Color 255,255,255
Oval 0,0,34,34,False
For x=0 To 34
For y=0 To 34
	GetColor x,y
	If ColorRed() = 255 And ColorGreen() = 255 And ColorBlue() = 255 Then
		zonear2(x,y) = True
	End If
Next:Next
SetBuffer BackBuffer()

Global impmap = CreateImage(640,480)
Global citymap = CreateImage(640,480)
Global zonemap = CreateImage(640,480)
Global bordermap = CreateImage(640,480)
Global colormap = LoadImage("worldcolored.png")

Type game
	Field activeunit
	Field flickrid,flickrtimer,flickrstate
End Type
Global g.game = New game

Global maxunits = 256
Dim unit(maxunits,32)

iniunit(10,10,2)
iniunit(15,12,2)
iniunit(36,12,2)

While KeyDown(1) = False
	ClsColor 0,0,200
	Cls
	DrawImage colormap,0,0
	DrawImage impmap,0,0
	DrawImage zonemap,0,0
	DrawImage bordermap,0,0
	DrawImage citymap,0,0
	drawunits
	flickractiveunit
	userkeys()
	If MouseHit(1) = True Then setactiveunit(unitmousecollision())
	Color 0,0,0
	Text 0,GraphicsHeight() - 16,"Press C to build city - Cursor keys to move - Mouse to select unit"
	Flip
Wend
End

Function userkeys()
	k = GetKey()
	If k>0 Then DebugLog k
	;
	If k = 28 Then moveunit("up")
	If k = 29 Then moveunit("down")
	If k = 31 Then moveunit("left")
	If k = 30 Then moveunit("right")
	;
	If k = 105 Then makeimprovement() ; i key
	If k = 99 Then makecity() ; c key
End Function

Function addzone()
	For x=0 To 32
	For y=0 To 32
		If zonear(x,y) = True Then
			SetBuffer ImageBuffer(map)
			GetColor x+aunitx()-12,y+aunity()-12
			If colorwater() = True Then
				SetBuffer ImageBuffer(colormap)
				GetColor x+aunitx()-12,y+aunity()-12
				cb = ColorBlue() * 1.2
				cr = ColorRed() * 1.2				
				cg = ColorGreen() * 1.2
				If cr > 255 Then cr = 255
				If cg > 255 Then cg = 255
				If cb > 255 Then cb = 255

				SetBuffer ImageBuffer(zonemap)
				;Color 20,55,255
				Color cr,cg,cb
				Plot x+aunitx()-12,y+aunity()-12
				SetBuffer BackBuffer()
			End If
			If colorland() = True Then
				SetBuffer ImageBuffer(colormap)
				GetColor x+aunitx()-12,y+aunity()-12
				cb = ColorBlue() * 1.2
				cr = ColorRed() * 1.2				
				cg = ColorGreen() * 1.2
				If cr > 255 Then cr = 255
				If cg > 255 Then cg = 255
				If cb > 255 Then cb = 255
				;Color 55,255,100
				SetBuffer ImageBuffer(zonemap)				
				Color cr,cg,cb
				Plot x+aunitx()-12,y+aunity()-12
				SetBuffer BackBuffer()		
			End If
			SetBuffer BackBuffer()
		End If
	Next:Next
	;
	For x=0 To 34
	For y=0 To 34
		If zonear2(x,y) = True Then		
		SetBuffer ImageBuffer(zonemap)
		GetColor aunitx()-x+20,aunity()-y+20		
		If colorblack() 
			SetBuffer ImageBuffer(bordermap)
			Color 255,255,255
			Plot aunitx()-x+20,aunity()-y+20
		End If
		SetBuffer BackBuffer()		
	End If
	Next:Next
	
	For x=0 To 32
	For y=0 To 32
		If zonear(x,y) = True Then
			SetBuffer ImageBuffer(bordermap)
			GetColor x+aunitx()-12,y+aunity()-12
			If colorblack() = False Then
				SetBuffer ImageBuffer(zonemap)
				x1 = x+aunitx()-12
				y1 = y+aunity()-12
				zip = False
				GetColor x1-1,y1
				If colorblack() = False Then zip = True
				GetColor x1+1,y1
				If colorblack() = False Then zip = True
				GetColor x1,y1-1
				If colorblack() = False Then zip = True
				GetColor x1,y1+1
				If colorblack() = False Then zip = True
				If zip = True Then 
				SetBuffer ImageBuffer(bordermap)
				Color 0,0,0
				Plot x1,y1
				SetBuffer BackBuffer()
				End If
				SetBuffer BackBuffer()
			End If
			SetBuffer BackBuffer()
		End If
	Next:Next

End Function

Function colorblack()
	If ColorRed() = 0 And ColorGreen() = 0 And ColorBlue() = 0 Then Return True
End Function
Function colorwater()
	If ColorRed() = 255 And ColorGreen() = 255 And ColorBlue() = 255 Then Return True
End Function
Function colorland()
	If ColorRed() = 0 And ColorGreen() = 200 And ColorBlue() = 0 Then Return True
End Function

Function makecity()
	SetBuffer ImageBuffer(citymap)
	Color 255,255,255
	Oval aunitx(),aunity(),8,8
	Color 20,20,20
	Oval aunitx(),aunity(),8,8,False
	SetBuffer BackBuffer()
	addzone
End Function

Function makeimprovement()
	SetBuffer ImageBuffer(impmap)
	Color 200,200,200
	Rect aunitx(),aunity(),8,8,True
	SetBuffer BackBuffer()
End Function

Function moveunit(dir$)
	Select dir$
		Case "up"
			moveunitup()
		Case "down"
			moveunitdown()
		Case "left"
			moveunitleft()
		Case "right"
			moveunitright()
	End Select
End Function

Function moveunitup()
	If ImagesCollide(unitimage,aunitx(),aunity()-8,0,map,0,0,0) = False Then 
		setflickrstate(True)
		Return
	End If
	If unitisthere(aunitx(),aunity()-8) Then 
		setflickrstate(True)
		Return
	End If
	setunity(g\activeunit,unity(g\activeunit)-8)
	setflickrstate(True)
End Function
Function moveunitdown()
	If ImagesCollide(unitimage,aunitx(),aunity()+8,0,map,0,0,0) = False Then 
		setflickrstate(True)
		Return
	End If
	If unitisthere(aunitx(),aunity()+8) Then 
		setflickrstate(True)
		Return
	End If
	setunity(g\activeunit,unity(g\activeunit)+8)
	setflickrstate(True)
End Function
Function moveunitleft()
	If ImagesCollide(unitimage,aunitx()-8,aunity(),0,map,0,0,0) = False Then 
		setflickrstate(True)
		Return
	End If
	If unitisthere(aunitx()-8,aunity()) Then 
		setflickrstate(True)
		Return
	End If
	setunitx(g\activeunit,unitx(g\activeunit)-8)
	setflickrstate(True)	
End Function
Function moveunitright()
	If ImagesCollide(unitimage,aunitx()+8,aunity(),0,map,0,0,0) = False Then 
		setflickrstate(True)
		Return
	End If
	If unitisthere(aunitx()+8,aunity()) Then 
		setflickrstate(True)
		Return
	End If
	setunitx(g\activeunit,unitx(g\activeunit)+8)
	setflickrstate(True)
End Function

Function aunitx()
	Return unitx(g\activeunit)
End Function
Function aunity()
	Return unity(g\activeunit)
End Function

Function unitisthere(x,y)
	For i=0 To maxunits
		If unitactive(i) = True Then
			If unity(i) = y Then
			If unitx(i) = x Then
				Return True
			End If:End If
		End If
	Next
	Return False
End Function

Function setactiveunit(n)
	If n = -1 Then Return
	g\activeunit = n	
	setflickrstate(True)
End Function

Function unitmousecollision()
	For i=0 To maxunits
		If unitactive(i) = True Then
			If RectsOverlap(MouseX(),MouseY(),1,1,unitx(i),unity(i),8,8) = True Then
				Return i
			End If
		End If
	Next
	Return -1
End Function

Function iniunit(gridx,gridy,allegiance)
	;
	a = freeunit()
	If a = -1 Then Return
	;
	activateunit(a)
	setunitx(a,gridx*8)
	setunity(a,gridy*8)
	setunitallegiance(a,allegiance)
	;
End Function

Function drawunits()
	For i=0 To maxunits
		If unitactive(i) = True Then
			drawunit(unitx(i),unity(i),unitallegiance(i))
		End If
	Next
End Function

Function drawunit(x,y,c)
	Color 0,0,0
	Oval x,y,8,8,True
	Select c
		Case 1:Color 200,0,0
		Case 2:Color 200,200,0
	End Select
	Oval x+1,y+1,6,6,True
End Function

Function flickractiveunit()
	If g\flickrtimer < MilliSecs() Then
		If g\flickrstate = 1 Then g\flickrstate = 0 Else g\flickrstate = 1
		g\flickrtimer = MilliSecs() + 1000		
	End If
	If g\flickrstate = 0 Then Color 0,0,0 : Oval unitx(g\activeunit),unity(g\activeunit),8,8,True
End Function


Function setflickrstate(state)
	Select state
		Case True	: g\flickrstate = 1
		Case False	: g\flickrstate = 0
	End Select
	g\flickrtimer = MilliSecs() + 500
End Function

Function deactivateunit(i)
	unit(i,0) = False
End Function

Function activateunit(i)
	unit(i,0) = True
End Function

Function setunity(i,y)
	unit(i,2) = y
End Function

Function setunitx(i,x)
	unit(i,1) = x
End Function

Function setunitallegiance(i,all)
	unit(i,3) = all
End Function

Function freeunit()
	For i=0 To maxunits
		If unit(i,0) = False Then Return i
	Next
	Return -1
End Function

Function unitactive(i)
	Return unit(i,0)
End Function

Function unity(i)
	Return unit(i,2)
End Function

Function unitx(i)
	Return unit(i,1)
End Function

Function unitallegiance(i)
	Return unit(i,3)
End Function

