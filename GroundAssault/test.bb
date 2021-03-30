

Graphics 800,600,32,2
SetBuffer BackBuffer()
Include "arrow.bb"

SeedRnd MilliSecs()

Dim mymap(150,150)

Dim formation(8,8)
readformation()

Dim mybrush(8,8)

Type form
	Field x,y
End Type

Type selection
	Field x1,y1,x2,y2,w,h
	Field state
End Type
Global sel.selection = New selection
Type base
	Field buildingx[128],buildingy[128]
	Field linex[512],liney[512]
End Type

Type game
	Field cx,cy
	Field maxline,maxbuilding
End Type
Global game.game = New game
game\maxline = 512
game\maxbuilding = 128
Type myfont
	Field smallfont
End Type
	Global font.myfont = New myfont
	font\smallfont = LoadFont("verdana.ttf",11)
	SetFont font\smallfont
Type gfx
	Field buffer
	Field building1
	Field zone
	Field unit
	Field unitselected
	Field stars
End Type
Global gfx.gfx = New gfx

Dim unitmap(100,100)

Type myunit
	Field x,y,incx,incy
	Field x1#,y1#
	Field state,laststate
	Field destx,desty
	Field selected
End Type

base.base = New base
For this.base = Each base
	For i=0 To 128
		rx = Rand(100)
		ry = Rand(100)
		this\buildingx[i] = rx
		this\buildingy[i] = ry
	Next
Next	

makegfx
buffermap
inifieldarmy(24,4)
inifieldarmy(44,4)
;selectdest(12*8,20*8,True)
;drawmap
While KeyDown(1) = False
	Cls
	TileImage gfx\stars,-128,-128
	DrawImage gfx\buffer,0,0
	drawunits
	userselect
	updateunits
	selectdest(MouseX(),MouseY(),False)
	drawminimap
	Flip
Wend
End

Function drawminimap()
x1 = GraphicsWidth()-150
Rect x1,0,150,150,False
For this.myunit = Each myunit
Rect this\x1/16+x1,this\y1/16+y1,1,1,True
Next
Color 255,255,255
Rect x1,0,32,32,False
End Function

Function drawunits()
For this.myunit = Each myunit
	Select this\selected
	Case False
	DrawImage gfx\unit,this\x1,this\y1
	Case True
	DrawImage gfx\unitselected,this\x1,this\y1
	End Select
	Color 255,255,255
	Oval this\destx*8,this\desty*8,8,8,False
Next
End Function
Function updateunits()
For this.myunit = Each myunit
If RectsOverlap(this\x1,this\y1,1,1,this\destx*8,this\desty*8,1,1) = False
; angle between 2 points
;angle = ATan2((y2-y1),(x2-x1)) 
angle# = ATan2((this\desty*8-this\y1),(this\destx*8-this\x1))

vx# = Cos(angle) 
vy# = Sin(angle)

this\x1 = this\x1 + vx
this\y1 = this\y1 + vy

Else 
unitmap(this\x1/8,this\y1/8) = True
End If
Next
End Function


Function userselect()
If MouseDown(1) = True And sel\state = 0 Then
	sel\x1 = MouseX()
	sel\y1 = MouseY()
	sel\state = 1
End If
If MouseDown(1) = True And sel\state = 1 Then
	If MouseX() > sel\x1 Then
	sel\x2 = MouseX()
	sel\w = sel\x2-sel\x1
	Else
End If
If MouseY() > sel\y1 Then
	sel\y2 = MouseY()
	sel\h = sel\y2-sel\y1
	Else
End If
	Rect sel\x1,sel\y1,sel\w,sel\h,False
End If
If MouseDown(1) = False And sel\state = 1 Then
	selectunits()
	sel\state = 0
	sel\w = 0
	sel\h = 0
End If
End Function
Function selectunits()
For this.myunit = Each myunit
If RectsOverlap(this\x1,this\y1,1,1,sel\x1,sel\y1,sel\w,sel\h) = True Then
this\selected = True
Else
this\selected = False
End If
Next
End Function

Function selectdest(x,y,overide)
Local cnt = 0
Local selun = countselectedunits()
If MouseDown(2) = True Or overide = True Then
If selun =< 4 Then

	createformation4(x/8,y/8)
	For this.myunit = Each myunit
		If this\selected = True Then
			that.form = First form
			this\destx = that\x
			this\desty = that\y
			Delete that			
		End If
	Next
	For that.form = Each form
		Delete that
	Next
ElseIf selun>3 Then
	For this.myunit = Each myunit
		If this\selected = True Then
			unitmap(this\x1/8,this\y1/8) = False
			this\destx = unitnewlocx(x/8,y/8,cnt)
			this\desty = unitnewlocy(x/8,y/8,cnt)
			If this\destx = 0 And this\desty = 0 Then 
			this\destx = this\x1/8
			this\desty = this\y1/8
			End If
			cnt=cnt+1
		End If
	Next
End If
End If
End Function

Function createformation4(x,y)
For x1=0 To 1
For y1=0 To 1
this.form = New form
this\x = x1*2+x
this\y = y1*2+y
Next:Next
End Function

Function countselectedunits()
For this.myunit = Each myunit
If this\selected = True Then
cnt=cnt+1
End If
Next
Return cnt
End Function

Function unitnewlocx(x,y,pos)
Local cnt = 0
For y1 = 0 To 7
For x1 = 0 To 7
If formation(x1,y1) = True Then
If surrounding(x+x1,y+y1) = False
If cnt = pos Then
Return x+x1
End If
End If
cnt=cnt+1
End If
Next:Next
End Function 
Function unitnewlocy(x,y,pos)
Local cnt = 0
For y1 = 0 To 7
For x1 = 0 To 7
If formation(x1,y1) = True Then
If surrounding(x+x1,y+y1) = False Then
If cnt = pos Then
Return y+y1
End If
End If
cnt=cnt+1
End If
Next:Next
End Function 
Function surrounding(x,y)
For y1=-1 To 1
For x1=-1 To 1
If unitmap(x+x1,y+y1) = True Then Return True
Next:Next
End Function 
Function inifieldarmy(x1,y1)
For x=0 To 3
For y=0 To 3
	this.myunit = New myunit
	this\x1 = (x)+x1
	this\y1 = (y)+y1
	this\destx = x+x1
	this\desty = y+y1
	unitmap(x+x1,y+y1) = True
	this\selected = True
Next:Next
End Function

Function spaceunits()
End Function

Function drawmap()
	SetBuffer ImageBuffer(gfx\buffer)

ClsColor 0,0,255
Cls

	For y=0 To 100
	For x=0 To 100
	If mymap(x,y) = True Then
		DrawImage gfx\zone,x*8-12,y*8-12
	End If
	Next:Next

	Color 0,255,0
	For y=0 To 100
	For x=0 To 100
	If mymap(x,y) = True Then
		DrawImage gfx\building1,x*8,y*8
		Rect x*8,y*8,8,8,True
	End If
	Next:Next
	
	SetBuffer BackBuffer()
End Function

Function buffermap()
	For this.base = Each base
		ax = Rand(30,40)
		ay = Rand(30,40)
		For i=0 To 40
			zx = ax + Rand(-15,15)
			zy = ay + Rand(-15,15)			
			While RectsOverlap(zx,zy,1,1,0,0,100,100) = False
			zx = ax + Rand(-15,15)
			zy = ay + Rand(-15,15)
			Wend
			this\buildingx[i] = zx
			this\buildingy[i] = zy
			mymap(this\buildingx[i],this\buildingy[i]) = True
			populate(this\buildingx[i],this\buildingy[i])
		Next
	Next
	
End Function

Function populate(x,y)
a = Rand(7)
For y1=-1 To 1
For x1=-1 To 1
If x1<> 0 And y1 <> 0 Then
	If RectsOverlap(x+x1,y+y1,1,1,0,0,100,100) = True
		mymap(x+x1,y+y1) = True
	End If
End If
Next
Next

End Function

Function readbrush()
	Restore brush
	For y=0 To 7
	For x=0 To 7
		Read a
		mybrush(x,y) = a
	Next:Next
End Function

.brush
Data 0,0,0,1,1,0,0,0
Data 0,0,1,1,1,1,0,0
Data 0,1,1,1,1,1,1,0
Data 1,1,1,1,1,1,1,1
Data 1,1,1,1,1,1,1,1
Data 0,1,1,1,1,1,1,0
Data 0,0,1,1,1,1,0,0
Data 0,0,0,1,1,0,0,0

Function makegfx()
	gfx\buffer = CreateImage(GraphicsWidth(),GraphicsHeight())
	gfx\building1 = CreateImage(8,8)
	gfx\zone = CreateImage(32,32)
	gfx\unit = CreateImage(10,10)
	gfx\unitselected = CreateImage(10,10)
	gfx\stars = LoadImage("stars.bmp")
	SetBuffer ImageBuffer(gfx\building1)
	Color 255,255,100
	Oval 0,0,8,8,True
	SetBuffer ImageBuffer(gfx\zone)
	Color 50,90,255
	Oval 0,0,32,32
	SetBuffer ImageBuffer(gfx\unit)
	Color 255,255,255
	Oval 2,2,6,6,True
	SetBuffer ImageBuffer(gfx\unitselected)
	Color 255,255,255
	Oval 2,2,6,6,True
	Color 255,255,0
	Oval 0,0,10,10,False
	SetBuffer BackBuffer()
End Function

.formation1
Data 0,0,0,0,0,0,0,0
Data 0,1,0,1,0,1,0,1
Data 0,0,0,0,0,0,0,0
Data 0,1,0,1,0,1,0,1
Data 0,0,0,0,0,0,0,0
Data 0,1,0,1,0,1,0,1
Data 0,0,0,0,0,0,0,0
Data 0,1,0,1,0,1,0,1

Function readformation()
	Restore formation1
	For y=0 To 7
	For x=0 To 7
	Read a
	formation(x,y) = a
	Next:Next
End Function 