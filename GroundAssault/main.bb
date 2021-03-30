Graphics 640,480,32,2
SetBuffer BackBuffer()

SeedRnd MilliSecs()
Dim inf(8,8)
Dim ordermap(100,100)
Dim defensemap(100,100)
Dim worldmap(100,100)

Dim smallformations(4,4,2)

Dim occupationmap(25,25)

Type font
	Field smallfont
End Type
Global myfont.font = New font
myfont\smallfont = LoadFont("verdana.ttf",12)
SetFont myfont\smallfont

Type selection 
	Field x1,y1,x2,y2,w,h
	Field phase1,phase2,phase3
End Type
Global sel.selection = New selection
Type occupation
	Field city
End Type

Type battleresults
	Field compcas
	Field playcas
	Field citiescaptured
End Type
Global battleresults.battleresults = New battleresults

Type pshipslib
	Field targetx[10],targety[10]
End Type
Global strategy.pshipslib = New pshipslib

Dim pship(100,100)
Type pships
	Field x#,y#,velx#,vely#
	Field selx,sely
	Field lastcx,lastcy
	Field srcx,srcy,destx,desty
	Field offsetx,offsety
	Field officer
	Field selection
	Field formationid
	Field state,laststate,substate,lastsubstate
End Type


Type drones
	Field x#
	Field y#
	Field basex
	Field basey
	Field destx#
	Field desty#
	Field lastcx
	Field lastcy

End Type

Type explosion
	Field frame
	Field x,y
	Field timer
End Type
Type sfx
	Field explosion
End Type
Global sfx.sfx = New sfx
sfx\explosion = LoadSound("explosion.wav")
Type gfx
	Field land
	Field layer1
	Field territory
	Field drones
	Field drone
	Field pship
	Field explosion
	Field redhouse
	Field greyhouse
End Type
Global gfx.gfx = New gfx
gfx\explosion = LoadAnimImage("explosion.bmp",16,16,0,7)
gfx\redhouse = LoadImage("redhouse.bmp") 
gfx\greyhouse = LoadImage("greyhouse.bmp")

Type s
	Field fps,fpscounter,fpstimer
End Type
Global s.s = New s

Type ai_orders
	Field cityx[512]
	Field cityy[512]
	Field citybuild[512]
	Field cityowner[512]
	Field lineowner[512]
	Field phase2[512]
	Field linex[512]
	Field liney[512]
	Field lastcity
End Type

gfx\layer1 = CreateImage(100*5,100*5) : MaskImage gfx\layer1,0,0,0
gfx\drones = CreateImage(100*5,100*5) : MaskImage gfx\drones,0,0,0
gfx\territory = CreateImage(100*5,100*5) : MaskImage gfx\territory,0,0,0


;linedefenses
;translatelinedefenses(player1)

iniformations
;
startlevel(Rand(5,9),Rand(5,15))
;
;
bufferdrone
bufferland
bufferlayer1
bufferdrones
bufferterritory
	tzz = MilliSecs() + 60*10*1000
	sel\x1=0
	sel\y1=0
	sel\x2=50
	sel\y2=50
	sel\w=50
	sel\h=50
	tz = MilliSecs() + 2000
While KeyDown(1) = False
	ClsColor 0,0,0
	Cls
;	drawdefensemap
	DrawImage gfx\land,0,0
	DrawImage gfx\territory,0,0
	DrawImage gfx\layer1,0,0
	DrawImage gfx\drones,0,0
	updateexplosions
	drawexplosions
	
	drawstrikeforce
	drawselection
	Selectunits()
	updatestrikeforce
	;drawdefensemap
	If KeyHit(2) = True Or tz < MilliSecs() Then
;	clearterritory
;	For this.ai_orders = Each ai_orders
;		;draworders(this)
;		buildnextcity(this) :updateorders(this):linedefenses:translatelinedefenses(this)
;		bufferterritory
;		bufferlayer1
;	Next
	senddrones
	tz = MilliSecs() + 5000
	End If
	
	If KeyDown(49) = True Or tzz < MilliSecs() Then
	tzz = MilliSecs() + 60*10*1000

	For this.ai_orders = Each ai_orders
		Delete this
	Next
	For that.drones = Each drones
		Delete that
	Next
	clearterritory
	clearworldmap
	cleardefensemap
	clearordermap
	cleardronemap
	
	startlevel(Rand(5,9),Rand(5,15))
	bufferland
	bufferlayer1	
	bufferdrones

	End If
;	Color 255,255,255
;	Rect 0,0,100*5,100*5,False
	;
		bufferdrones
		drawmenu
	Text GraphicsWidth()-256,GraphicsHeight()-32,"Press n for new map"
	Color 250,250,250
	Text 0,0,s\fps
	updatefps
	Flip
Wend
End

; num of armies against number of base expansions
Function startlevel(a,b)

For zz.pships = Each pships
Delete zz
Next
battleresults\compcas = 0
battleresults\playcas = 0
battleresults\citiescaptured = 0
;three enemy base groups
For i=0 To 2
	this.ai_orders = New ai_orders
	makeorders(this,2,Rand(30,65),Rand(30,65))
	bufferterritory
	this\citybuild[0] = True
	this\lastcity = 0
Next

For i=0 To a
x1 = Rand(30,450)
y1 = Rand(30,90)
inistrikeforce(x1,y1,i)
strategy\targetx[i] = x1
strategy\targety[i] = y1
Next

;
clearterritory
For i=0 To b
	For this.ai_orders = Each ai_orders
		;draworders(this)
		;makeorders(this,2,Rand(30,65),Rand(30,65))
		buildnextcity(this) :updateorders(this):linedefenses:translatelinedefenses(this)
		bufferterritory
		;bufferlayer1
	Next
Next
	bufferland
	bufferlayer1	
	bufferdrones


End Function

Function capturecitybyfriendyforces()
If Rand(150) <> 1 Then Return

If Rand(2) = 1 Then
For this.ai_orders = Each ai_orders
For i=0 To 512

If this\citybuild[i] = True Then
For that.pships = Each pships
	x1 = this\cityx[i]*5
	y1 = this\cityy[i]*5
	x2 = that\x
	y2 = that\y
	If this\cityowner[i] <> 10 Then
		If RectsOverlap(x1,y1,16,16,x2,y2,16,16) = True Then		
			battleresults\citiescaptured = battleresults\citiescaptured + 1
			this\cityowner[i] = 10		
		End If
	End If
Next
End If
Next
Next
bufferlayer1
End If

End Function

Function buildnextcity(this.ai_orders)
a = closestunopenedcity(this\cityx[this\lastcity],this\cityy[this\lastcity],this)
this\citybuild[a] = True
End Function


Function translatelinedefenses(this.ai_orders)

For y=0 To 100
For x=0 To 100
If defensemap(x,y) = 2 Then 
this\linex[cnt] = x
this\liney[cnt] = y
cnt=cnt+1
End If
Next:Next
End Function

Function makeorders(this.ai_orders,sizea,inx,iny)
For ii=0 To 12
maka = inx-20+Rand(40)
makb = iny-20+Rand(40)
For i=0 To 3
	x = Rand(10)
	y = Rand(10)
	ordermap(x+maka,y+makb) = 1
	this\cityx[cnt] = x+maka
	this\cityy[cnt] = y+makb
	cnt=cnt+1
	insertcityinfluence(x+maka,y+makb)
	For y1=0 To 100
	For x1=0 To 100
	If defensemap(x1,y1) = 1 Then worldmap(x1,y1) = 1
	Next:Next
Next
Next
End Function

Function updateorders(this.ai_orders)
For y=0 To 100:For x=0 To 100
	defensemap(x,y) = 0
Next:Next
For i=0 To 512
	If this\citybuild[i] = True Then 
	insertcityinfluence(this\cityx[i],this\cityy[i])
	
	End If
Next

End Function

Function draworders(this.ai_orders)
Color 255,0,0
;For y = 0 To 100
;For x = 0 To 100
;If ordermap(x,y) > 0 Then Oval x*5,y*5,5,5
;Next:Next
For i=0 To 512
	If this\citybuild[i] = True Then
	If this\cityx[i] > 0 And this\cityy[i] > 0 Then 
	If this\cityowner[i] = 10 Then 
		;Color 30,60,240
		DrawImage gfx\greyhouse,this\cityx[i]*5-2,this\cityy[i]*5-2
	End If
	If this\cityowner[i] < 10 Then 
		DrawImage gfx\redhouse,this\cityx[i]*5-2,this\cityy[i]*5-2
	End If
	End If
	EndIf
Next
For i=0 To 512
	If this\linex[i] > 0 And this\liney[i] > 0 Then 		
		;If this\lineowner[i] < 10 Then
		;	Color 165,15,10
		;Else Color 15,52,240
		;End If
		Color 132,72,8
		Oval this\linex[i]*5-1,this\liney[i]*5-1,7,7
	End If
Next


Color 112,62,8
For i=0 To 512
	If this\linex[i] > 0 And this\liney[i] > 0 Then 
		Oval this\linex[i]*5,this\liney[i]*5,5,5
	End If
Next

End Function

Function drawdefensemap()
For y = 0 To 100
For x = 0 To 100
If defensemap(x,y) = 1 Then Color 155,155,155 :Oval x*5,y*5,5,5
If defensemap(x,y) = 2 Then Color 255,255,255 :Oval x*5,y*5,5,5

Next:Next

End Function

Function linedefenses()
For y=1 To 99
For x=1 To 99
If defensemap(x,y) = 0 And defensemap(x+1,y) = 1 Then defensemap(x,y) = 2
If defensemap(x,y) = 1 And defensemap(x+1,y) = 0 Then defensemap(x+1,y) = 2
If defensemap(x,y) = 0 And defensemap(x,y-1) = 1 Then defensemap(x,y-1) = 2
If defensemap(x,y) = 0 And defensemap(x,y+1) = 1 Then defensemap(x,y+1) = 2
Next
Next
End Function

Function insertcityinfluence(x,y)
Restore influence
For y1=0 To 7
For x1=0 To 7
	Read a
	inf(x1,y1) = a
Next:Next
For y1 = y-4 To y+4
For x1 = x-4 To x+4
If defensemap(x1,y1) = 0 Then defensemap(x1,y1) = inf(ax,ay)
ax=ax+1
Next
ax = 0
ay = ay+1
Next
End Function

.influence
Data 0,0,0,1,1,0,0,0
Data 0,0,1,1,1,1,0,0
Data 0,1,1,1,1,1,1,0
Data 1,1,1,1,1,1,1,1
Data 1,1,1,1,1,1,1,1
Data 0,1,1,1,1,1,1,0
Data 0,0,1,1,1,1,0,0
Data 0,0,0,1,1,0,0,0

Function closestopenedcity(x,y,this.ai_orders)
Local dist[512]

For i=0 To 512
	If this\cityx[i] > 0 And this\cityy[i] > 0 Then
		If this\citybuild[i] = True And this\cityowner[i] <> 10 Then
			If distance(this\cityx[i],this\cityy[i],x,y) < 5 Then
				dist[i] = distance(this\cityx[i],this\cityy[i],x,y)
			End If
		End If
	End If
Next

d = 1000
For i=0 To 512
	If dist[i] > 0 Then
		If dist[i] < d Then d = dist[i] : val = i
	End If
Next

Return val

End Function

Function closestunopenedcity(x,y,this.ai_orders)
Local dist[512]

For i=0 To 512
	If this\cityx[i] > 0 And this\cityy[i] > 0 Then
		If this\citybuild[i] = False Then
			dist[i] = distance(this\cityx[i],this\cityy[i],x,y)
		End If
	End If
Next

d = 1000
For i=0 To 512
	If dist[i] > 0 Then
		If dist[i] < d Then d = dist[i] : val = i
	End If
Next

Return val

End Function

Function distance#(x1,y1,x2,y2)
	xd=Abs(x2-x1)
	yd=Abs(y2-y1)
	Return Sqr((xd*xd)+(yd*yd))
End Function

Function updatefps()
s\fpscounter = s\fpscounter+1
If s\fpstimer < MilliSecs() Then
s\fpstimer = MilliSecs() + 1000
s\fps = s\fpscounter
s\fpscounter = 0
End If
End Function

Function bufferland()
	gfx\land = CreateImage(100*5,100*5)
	SetBuffer ImageBuffer(gfx\land)
	ClsColor 33,62,195
	Cls
	For x=0 To 100
	For y=0 To 100
		Color 22,100,195
		Oval x*5,y*5,5,5
	Next
	Next
	
	For y=0 To 100
	For x=0 To 100
		If worldmap(x,y) = 1 Then 
		Color 40,40,40
		Oval x*15-356-7,y*15-356-7,27,27
		End If
	Next:Next
	
	For y=0 To 100
	For x=0 To 100
		If worldmap(x,y) = 1 Then 
		Color 30,34,39
		Oval x*15-356,y*15-356,15,15
		End If
	Next:Next
	
	For y=0 To 100
	For x=0 To 100
		If worldmap(x,y) = 1 Then 
		Color 70,70,70
		Oval x*10-356-5,y*10-356-5,20,20
		End If
	Next:Next
	
	For y=0 To 100
	For x=0 To 100
		If worldmap(x,y) = 1 Then 
		Color 50,54,59
		Oval x*10-356,y*10-356,10,10
		End If
	Next:Next
	
	For y=0 To 100
	For x=0 To 100
		If worldmap(x,y) = 1 Then 
		Color 130,30,20
		Oval x*5-2,y*5-2,10,10
		End If
	Next:Next
	
	For y=0 To 100
	For x=0 To 100
		If worldmap(x,y) = 1 Then 
		Color 100,0,0
		Oval x*5,y*5,6,6
		End If
	Next:Next
	SetBuffer BackBuffer()
End Function
Function bufferlayer1()
	SetBuffer ImageBuffer(gfx\layer1)
	ClsColor 0,0,0
	Cls
	For this.ai_orders = Each ai_orders
		draworders(this)
	Next
	SetBuffer BackBuffer()
End Function

Function clearordermap()
For y=0 To 100
For x=0 To 100
ordermap(x,y) = 0
Next:Next
End Function
Function cleardefensemap()
For y=0 To 100
For x=0 To 100
defensemap(x,y) = 0
Next:Next
End Function
Function clearworldmap()
For y=0 To 100
For x=0 To 100
worldmap(x,y) = 0
Next:Next

End Function

Function drawdrones()
	Color 255,255,255
	For d1.drones = Each drones
		Oval d1\x*5,d1\y*5,5,5
	Next
End Function

Function senddrones()
Local activelist[512]
Local closedlist[512]

tn = MilliSecs() + 5000
While tn>MilliSecs()
ClsColor 0,0,0
Cls
DrawImage gfx\land,0,0
	DrawImage gfx\territory,0,0
DrawImage gfx\layer1,0,0
DrawImage gfx\drones,0,0
updateexplosions
drawexplosions
	drawstrikeforce
	drawselection
	Selectunits()
	updatestrikeforce
c=0
For d1.drones = Each drones
	If activelist[c] = True And closedlist[c] = False
	If d1\y > d1\basey Then d1\y=d1\y-.2
	If d1\x > d1\basex Then d1\x=d1\x-.2
	If d1\y < d1\basey Then d1\y=d1\y+.2
	If d1\x < d1\basex Then d1\x=d1\x+.2
;	If RectsOverlap(d1\x,d1\y,1,1,d1\basex,d1\basey,1,1) = True Then
;	closedlist[c] = True
;	End If
	End If
	c=c+1
Next
For z=0 To 5
activelist[Rand(512)] = True
Next
bufferdrones
drawmenu
Color 255,255,255
Text GraphicsWidth()-256,GraphicsHeight()-32,"Expanding territory"
Text 0,0,s\fps
updatefps
If KeyDown(1) = True Then End
Flip
Wend


For that.drones = Each drones
	Delete that
Next
Local occ[512]
For this.ai_orders= Each ai_orders
For i=0 To 512
If this\linex[i] > 0 And this\liney[i] > 0 Then
If Rand(2) = 1 Then
d.drones = New drones
d\destx = this\linex[i]
d\desty = this\Liney[i]
n = closestopenedcity(d\destx,d\desty,this)
d\x = this\cityx[n]
d\y = this\cityy[n]
d\basex = d\x
d\basey = d\y
If n=0 Then Delete d
End If
End If
Next
Next


For i=0 To 512 : activelist[i]=False:Next
For i=0 To 512 : closedlist[i]=False:Next

tn = MilliSecs() + 4000
While tn>MilliSecs()
ClsColor 0,0,0
Cls
DrawImage gfx\land,0,0
	DrawImage gfx\territory,0,0
DrawImage gfx\layer1,0,0
DrawImage gfx\drones,0,0
	drawstrikeforce
	updateexplosions
	drawexplosions
	drawselection
	Selectunits()
	updatestrikeforce	
c = 0
up=False
For d1.drones = Each drones
	If activelist[c] = True And closedlist[c] = False
	If d1\y > d1\desty Then d1\y=d1\y-.1
	If d1\x > d1\destx Then d1\x=d1\x-.1
	If d1\y < d1\desty Then d1\y=d1\y+.1
	If d1\x < d1\destx Then d1\x=d1\x+.1
	;If RectsOverlap(d1\x,d1\y,1,1,d1\destx,d1\desty,1,1) Then 
	;closedlist[c] = True
	;End If
	up=True
	EndIf
	c=c+1
Next
If up=True Then bufferdrones

activelist[Rand(512)] = True
activelist[Rand(512)] = True
activelist[Rand(512)] = True


;drawdrones
Color 255,255,255
Text 0,0,s\fps
Text GraphicsWidth()-256,GraphicsHeight()-32,"Expanding territory"
bufferdrones
drawmenu
updatefps
If KeyDown(1) = True Then End
Flip
Wend

End Function

Function bufferdrones()
SetBuffer ImageBuffer(gfx\drones)
ClsColor 0,0,0
Cls
Color 255,255,255
For this.drones = Each drones
	;Rect this\x*5,this\y*5,5,5
	DrawImage gfx\drone,this\x*5,this\y*5
Next
SetBuffer BackBuffer()
End Function
Function cleardronemap()
SetBuffer ImageBuffer(gfx\drones)
ClsColor 0,0,0
Cls
SetBuffer BackBuffer()
End Function

Function bufferdrone()
gfx\drone = CreateImage(7,7)
SetBuffer ImageBuffer(gfx\drone)
ClsColor 0,0,0
Cls
Color 255,255,255
Oval 1,1,5,5,True
Color 210,210,210
Oval 1,1,5,5,False
Color 210,210,10
Oval 0,0,7,7,False
SetBuffer BackBuffer()
gfx\pship = CreateImage(7,7)
SetBuffer ImageBuffer(gfx\pship)
ClsColor 0,0,0
Cls
Color 0,0,200
Oval 1,1,5,5,True
Color 10,50,210
Oval 1,1,5,5,False
Color 180,180,180
Oval 0,0,7,7,False
SetBuffer BackBuffer()
End Function

Function bufferterritory()
SetBuffer ImageBuffer(gfx\territory)
;ClsColor 0,0,0
;Cls
Color 150,39,27
For y=0 To 100
For x=0 To 100
If defensemap(x,y) > 0 Then
Color 75,5,5
Oval x*5,y*5,5,5,True
End If
Next
Next
SetBuffer BackBuffer()
End Function
Function clearterritory()
	SetBuffer ImageBuffer(gfx\territory)
	ClsColor 0,0,0:Cls
	SetBuffer BackBuffer()
End Function

Function iniformations()
	Restore formation1
	For y=0 To 4
	For x=0 To 4
		Read a
		smallformations(x,y,1) = a
	Next:Next
End Function

Function updatestrikeforce()
	If MouseDown(2) = True Then
		strike_setnewposition(MouseX(),MouseY())
	End If
For this.pships = Each pships
Select this\state
	Case 1 ; move To
		movepship(this,this\formationid)
	Case 2 ;
End Select
	;
	;
Next
strikeforcecombat
capturecitybyfriendyforces
End Function
Function strikeforcecombat()
If Rand(5) <> 1 Then Return
For this.pships = Each pships
	For that.drones = Each drones
		If Rand(500) = 1 Then
			x1 = this\x
			y1 = this\y
			x2 = that\x*5
			y2 = that\y*5
			If RectsOverlap(x1,y1,30,30,x2,y2,30,30) = True
				If Rand(3) = 1 Then
					delout = True
					iniexplosion(this\x,this\y)
					battleresults\playcas = battleresults\playcas + 1
				Else
					iniexplosion(x2-5,y2-5)
					PlaySound sfx\explosion
					battleresults\compcas = battleresults\compcas + 1					
					Delete that
				End If
			End If
		End If
	Next
	If delout = True Then delout = False : Delete this
Next
End Function

Function strike_setnewposition(x,y)
Local selected[10]
For i=0 To 10 : selected[i]=-1:Next
Local offsetx[10]
Local offsety[10]
	For this.pships = Each pships
		If this\selection = True Then
			selected[this\formationid] = True
		End If
	Next
	For i=0 To 10
		If selected[i] > -1 Then
			cnt=cnt+1
			strategy\targetx[i] = 	x + (cnt*32)-20
			strategy\targety[i] = 	y+10
		End If
	Next
	; spread out distance between units
End Function

Function movepship(this.pships,num)
xx = 0
yy = 0
oldx = this\x
oldy = this\y
If this\x+this\offsetx < strategy\targetx[num] Then this\x=this\x + this\velx : xx = this\velx
If this\x+this\offsetx > strategy\targetx[num] Then this\x=this\x - this\velx : xx = -this\velx
If this\y+this\offsety < strategy\targety[num] Then this\y=this\y + this\vely : yy = this\vely
If this\y+this\offsety > strategy\targety[num] Then this\y=this\y - this\vely : yy = -this\vely

If this\x <> oldx Then
this\selx = this\x
End If
If this\y <> oldy Then
this\sely = this\y
End If

End Function

Function inistrikeforce(x,y,num)
cnt = 0
For y1=0 To 4
For x1=0 To 4
If smallformations(x1,y1,1) = 1 Then
this.pships = New pships
If cnt=0 Then 
this\officer = True
End If
this\selection = False
this\x = (x1*5)+x
this\y = (y1*5)+y
this\selx = x1+x
this\sely = y1+y
this\offsetx = x1*5
this\offsety = y1*5
this\velx = .5+(Rnd(1)/2)
this\vely = .5+(Rnd(1)/2)
this\formationid = num
this\state = 1
cnt=cnt+1
End If
Next:Next
End Function
Function drawstrikeforce()
Local sfsel[10]
	For this.pships = Each pships
		DrawImage gfx\pship,this\x,this\y
		If sfsel[this\formationid] = False Then
			Color 255,0,0
			If this\selection = True Then
				Color 255,255,0				
				Rect this\selx-5*5,this\sely-5*5,7*5,7*5,False
				sfsel[this\formationid] = True
			End If
		End If
	Next
End Function
.formation1
Data 1,0,1,0,1
Data 0,0,0,0,0
Data 1,0,1,0,1
Data 0,0,0,0,0
Data 1,0,1,0,1


Function updateexplosions()
For this.explosion = Each explosion
If this\timer < MilliSecs() Then
this\timer = MilliSecs() + 70
this\frame = this\frame + 1
If this\frame = 7 Then Delete this
End If
Next
End Function
Function drawexplosions()
	For this.explosion = Each explosion
		DrawImage gfx\explosion,this\x,this\y,this\frame
	Next
End Function
Function iniexplosion(x,y)
this.explosion = New explosion
this\timer = MilliSecs() + 20
this\x = x
this\y = y
this\frame = 0
End Function

Function Selectunits()
If MouseDown(1) = True And sel\phase1 = False Then
sel\phase1 = True
sel\x1 = MouseX()
sel\y1 = MouseY()
End If
If MouseDown(1) = True And sel\phase1 = True Then
x2 = MouseX()
y2 = MouseY()
If sel\x1 < x2 Then xx1 = sel\x1 : w = x2 - xx1
If sel\y1 < y2 Then yy1 = sel\y1 : h = y2 - yy1
If sel\x1 > x2 Then xx1 = x2 : w = sel\x1-x2
If sel\y1 > y2 Then yy1 = y2 : h = sel\y1-y2
Color 255,255,0
Rect xx1,yy1,w,h,False
sel\w = w
sel\h = h

End If
If MouseDown(1) = False And sel\phase1 = True Then
	sel\phase1 = False

	sel\x2 = MouseX()
	sel\y2 = MouseY()
	x1 = sel\x1
	y1 = sel\y1
	x2 = sel\x2
	y2 = sel\y2
	If x1>x2 Then sel\x1 = x2 : sel\x2=x1
	If y1>y2 Then sel\y1 = y2 : sel\y2=y1
	sel\w = sel\x2 - sel\x1
	sel\h = sel\y2 - sel\y1
	setstrikeforceselection(sel\x1,sel\y1,sel\x2,sel\y2)
	;Delay(50)
End If
End Function 
Function setstrikeforceselection(x1,y1,x2,y2)
For this.pships = Each pships
If RectsOverlap(x1,y1,x2-x1,y2-y1,this\x,this\y,16,16) = True Then
this\selection = True
Else
this\selection = False
End If
Next
End Function
Function drawselection()
	If sel\phase1 = False Then
	Color 255,255,255
	Rect sel\x1,sel\y1,sel\w,sel\h,False
	EndIf
End Function 

Function drawmenu()
Text GraphicsWidth()-138,GraphicsHeight()-96,"Comp. casualties : " + battleresults\compcas
Text GraphicsWidth()-138,GraphicsHeight()-80,"Cities destroyed : " + battleresults\citiescaptured
Text GraphicsWidth()-138,GraphicsHeight()-66,"player casualties : " + battleresults\playcas


End Function