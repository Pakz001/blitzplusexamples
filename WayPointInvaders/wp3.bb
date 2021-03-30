
Dim galaxy(50,50,14) ; act, 2/inrange
Dim galaxystring$(50,50,5)
Dim nodebuffer(50,50,6)
Dim ebeam(60,2) ; electron beam data
Dim rpship(96)

Global debugding$
Global gwidth = 640
Global gheight = 480
Global gdepth = 16
Global smode = 2
selscreenmode()

Include "wp_types.bb"

Global electronbeam = LoadSound("electronbeam.wav")
If electronbeam = 0 Then RuntimeError "Error - reinstall !"
LoopSound electronbeam
Global explosionsound = LoadSound("explosion.wav")
If explosionsound = False Then RuntimeError "Error - reinstall program"
Global launchsound = LoadSound("launch.wav")
If launchsound = False Then RuntimeError "Error - reinstall program"
Global explosiontimer

Dim planets(10)
planets(0) = LoadImage("planet1.bmp")
If planets(0) = False Then RuntimeError "Error - reinstall!!"

Graphics gwidth,gheight,gdpeth,smode
intro
HidePointer
Global explosion = CreateImage(32,32,12)
Global mousepointer = CreateImage(16,16)
HandleImage mousepointer,ImageWidth(mousepointer)/2,ImageHeight(mousepointer)/2
Global starfield = CreateImage(128,128)
Global starfield2 = CreateImage(128,128)
HandleImage explosion,16,16
createexplosion
createmousepointer
createstarfield
Const key_up=200
Const key_down=208
Const key_left=203
Const key_right=205
Const key_space=57
Const key_enter=28
Const RacerCount%=5
Const PathWaySize%=30
Const maxspeed#=5

Global waypoints%=0
Global gameon%=False



Dim collgrid(32,32)
Global gw = 20
Global gh = 11
Global gamescore = 0
Global power = 100
SetBuffer BackBuffer()
SeedRnd MilliSecs()
ResetRacers()

Global aiplayerenabled = False
Global flashscreen = False ; flash the screen
Global flashtimer = 0

Global NOTDOINGtimer = MilliSecs()+9000
Global notdoing = False
Global mus2 ; ingame mus
Global firedelaytimer
Global explosiondelaytimer
Global mainmenutimer = 0
Global mainmenublink = True
Global bigexplosiontimer
Global bigexplosiontriggered = False
Global bigexplosionready
Global electronbeamready = True
Global electrontime = 200
Global electrontimer
Global electronrechargetimer = 20
Global starttimer
Global level = 0 ; game level of difficulty
Global eplay ; electron beam
Global elecchan
Global exitmainloop = False
Global spacebucks = 200
Global currentnukerecharge = 0
Global currentelectronrecharge = 0
Global currentshiprecharge = 0
Global currentplasmaspeed = 4
Global hyperjumppower = 4
Global slowaifiretimer = MilliSecs()+20

Global f = LoadFont("verdana",18)
Global f2 = LoadFont("verdana",24)
Global f3 = LoadFont("verdana",29)
Global f4 = LoadFont("verdana",12)

Global stuff = LoadAnimImage("stuff.bmp",16,16,0,23)
MaskImage stuff,255,255,255


Global mus = PlayMusic("muzak.xm",1)
ChannelVolume mus,.5

Global ship1 = LoadImage("ship1.bmp")
Global pship = LoadImage("pship.bmp")
MaskImage ship1,255,255,255
MaskImage pship,255,255,255
inirtstarfield()

Global buts#
Global buts2#

Global rechargepowertimer ; rechargetimer for power
rotatepship()
;aproachplanet

;inigalaxy 
;galaxymap

While exitmainloop = False
	Cls	
	dostarfield()
	render()
	rtstarfield
	donuke()
	bigexplosion()

	If KeyHit(63) = True And gameon = True Then	
		gameon = False
		resetracers
		randomwaypoints()
		aproachplanet
		Delay(50)
		FlushKeys()
		gameon = True
		notdoingtimer = MilliSecs() + 9000
		notdoing = False
	End If
;Global currentnukerecharge = 0
;Global currentelectronrecharge = 0
;Global currentshiprecharge = 0
;Global currentplasmaspeed = 0
	If gameon = True And power<100 And rechargepowertimer < MilliSecs()
		If currentshiprecharge > 0 Then
			power = power + 1
			If power>100 Then power = 100
		End If
		rechargepowertimer = MilliSecs() + 10000 - currentshiprecharge * 100
	End If
	

	If gameon = True Then
			notdoingtimer = MilliSecs() + 9000
			updateracers()
			If gt < MilliSecs() Then
				gt = MilliSecs()+10000
				addracers
				randomwaypoints
			End If
			burp = 0
			For q.tracer = Each tracer
				burp = burp + 1
			Next
			If power <= 0 Or burp > 100 Then ; Game over!!!!!
				If eplay = True Then StopChannel elecchan
				eplay = False
				gameon = False :endseq(notdoing): resetracers :power = 100 : ResumeChannel mus : gameon=False:notdoing=False			
			End If
		End If
	newgame()
	doexitgame()
	doaiplayer() ; enable / disable ai player
	doplasmamissile()
	doelectronbeam()
	 If aiplayerenabled = True Then aiplayer
	 movelasers
	 doexplosions()
	 explosioncollision	 
	If gameon = False Then gamemenu
	If notdoing = True And gameon = True Then gamemenu
	doflashscreen()	
	DrawImage pship,GraphicsWidth()/2-5,GraphicsHeight()-20
	DrawImage mousepointer,MouseX(),MouseY()
	;
	ingameinterface()
	;
	Flip
Wend
End
Function dostarfield(drawit = True)
 	 	buts# = buts# + .2
	 	buts2# = buts2# + .3
	 	If buts > 0 Then buts = -ImageWidth(starfield)
	 	If buts2 > 0 Then buts2 = -ImageWidth(starfield)
		If drawit = True Then
	 		TileImage starfield,0,buts
	 		TileImage starfield2,12,buts2
		End If
End Function
Function doaiplayer()
	If KeyHit(59) Then
		If aiplayerenabled = True Then aiplayerenabled = False Else aiplayerenabled = True
	EndIf
End Function
Function doexitgame()
	If KeyHit(1) = True Then
		If gameon = True Then gameon = False Else exitmainloop = True
	End If
End Function
Function doflashscreen()
	If flashscreen = True Then
		ClsColor  80,80,0
	Else
		ClsColor 0,0,0
	EndIf
	If flashscreen = True And flashtimer < MilliSecs() Then
		flashscreen = False
	End If
End Function
Function ingameinterface()

	Color 255,255,255
	Text 0,GraphicsHeight()-18,"Score : " + gamescore
	If aiplayerenabled = True Then Text GraphicsWidth()-120,GraphicsHeight()-18,"Ai player enabled"
	If power > 16 And bigexplosiontriggered = False And bigexplosionready < MilliSecs() Then
	Text GraphicsWidth()-220,GraphicsHeight()-18,"Nuke!"
	End If
	If electronbeamready = True Then
		Text GraphicsWidth()-180,GraphicsHeight()-18,"Electron"
		Rect GraphicsWidth()-180,GraphicsHeight()-18,(electrontime/2)/2,10
		Else
		;Rect GraphicsWidth()-180,GraphicsHeight()-18,electrontime/20,10
	EndIf
	Text 10,5,"Spacebucks : " + spacebucks
	Text GraphicsWidth()-96,3,"Level : " + level
	If power < 20 Then Color 200,0,0
	If power >20 Then Color 200,200,0
	If power >50 Then Color 0,255,0
	Rect 150,GraphicsHeight()-18,power,10,True
	If notdoing = False And gameon = False Then
	If notdoingtimer < MilliSecs() Then
		gameon = True
		level = Rand(3,16)
		notdoing=True
		aiplayerenabled = True
	End If
	End If
End Function
Function donuke()
	If KeyDown(57) And power > 16 And bigexplosiontriggered = False Then
		If bigexplosionready < MilliSecs() Then
			bigexplosiontimer = MilliSecs()
			bigexplosiontriggered = True
			bigexplosionready = MilliSecs() + 15000 - ((currentnukerecharge * 150))
		End If
	End If
	If bigexplosiontriggered = True Then
	If bigexplosiontimer + 300 > MilliSecs() Then
		spreadexplosion(Rand(0,GraphicsWidth()),Rand(0,GraphicsHeight()))
		nukepods
		Else
		If bigexplosiontriggered = True Then
			bigexplosiontriggered = False
			power = power - 15
		End If
	End If
	End If
End Function
Function doelectronbeam()
	If MouseDown(2) And electronbeamready = True Then
		electronbeam()
		electrontime = electrontime - 1
		If eplay = False Then
		elecchan = PlaySound(electronbeam)
		ChannelVolume elecchan,.5
		eplay = True
		End If
		If electrontime < 0  Then electronbeamready = False : eplay= False : StopChannel elecchan
		electrontimer = MilliSecs() + 10000
		Else
		If eplay = True Then StopChannel elecchan : eplay = False
	End If
	If electronbeamready = False And electrontimer < MilliSecs() Then electronbeamready = True : electrontime = 200
	If electrontime < 200 Then
		If electronrechargetimer < MilliSecs() Then
			electronrechargetimer = MilliSecs() + (1000 - currentelectronrecharge * 10)
			electrontime = electrontime + 1
		End If
	End If
End Function	
Function newgame()
	
	 If KeyHit(key_enter) Then ; new gfame
	 	If notdoing = True Then notdoing = False : gameon = False
	 	If gameon = True Then
			StopChannel mus2 : ResumeChannel mus
			gameon = False
			aiplayerenabled = False
		Else
		starttimer = MilliSecs()
		StopChannel elecchan
		level = 0
		resetracers
		updateracers
		gameon=True
		electrontime = 200
		currentelectronrecharge = 0
		power = 100
		gamescore = 0
		aiplayerenabled = False
		currentplasmaspeed = 4
		notdoing=False
		spacebucks = 200
		PauseChannel mus		
		mus2 = PlayMusic("ingame.mid",1)
		ChannelVolume mus2,1
		End If
	 End If
End Function
Function doplasmamissile()
	If MouseHit(1) Or MouseDown(1) Then ; fire
		If firedelaytimer < MilliSecs() Then
		firelaser(MouseX(),MouseY())
		firedelaytimer = MilliSecs() + 200
		End If
	End If
End Function

Function spreadexplosion(x,y)
inibigexplosion(x,y,0)
inibigexplosion(x+Rand(3,6),y+Rand(3,6),0,230)
inibigexplosion(x-Rand(3,6),y-Rand(3,6),0,460)
inibigexplosion(x-Rand(3,6),y-Rand(3,6),0,760)
inibigexplosion(x-Rand(3,6),y-Rand(3,6),0,860)
inibigexplosion(x,y,1)
inibigexplosion(x,y,1)
End Function
Function inibigexplosion(x,y,tp = 0,Dlay=0)
If tp = 1 Then z = 4 Else z = 0
For i=0 To z
a.bigexplosion = New bigexplosion
	a\x = x
	a\y = y
	a\tp = tp
	Select tp
	Case 0
		a\frame = 0
		a\waitawhile = MilliSecs() + dlay
	Case 1
		a\waitawhile = MilliSecs() + dlay
		a\frame = 16
		a\mx# = Rnd(0,2)
		a\my# = Rnd(0,2)
		If Rand(0,1) = 1 Then a\mx = -a\mx
		If Rand(0,1) = 1 Then a\my = -a\my
	End Select
	a\timer = MilliSecs() + 30
Next
End Function
Function bigexplosion()
	For a.bigexplosion = Each bigexplosion
		del = False
		If a\waitawhile < MilliSecs() Then
		If a\timer < MilliSecs() Then			
			a\frame = a\frame + 1
			Select a\tp ;22
			Case 0
			a\timer = MilliSecs() + 30
			If a\frame > 8 Then del = True : Delete a
			Case 1
			a\timer = MilliSecs() + 60
			If a\frame > 22 Then del = True : Delete a
			End Select
		End If
		
		If del= False Then
			a\x = a\x + a\mx
			a\y = a\y + a\my
			DrawImage stuff,a\x,a\y,a\frame
		End If
		End If
	Next
End Function

Function nukepods()
	For a.tracer = Each tracer		
		spreadexplosion(a\x,a\y)
		Delete a
		gamescore= gamescore + 1
	Next
End Function

Function firelaser(tx,ty)
	z= PlaySound(launchsound)
	ChannelVolume z,.1
	a.laserfield = New laserfield
	a\x = GraphicsWidth()/2
	a\y = GraphicsHeight()-20
	a\ox = a\x
	a\oy = a\y
	a\tx = tx
	a\ty= ty
	a\rot = 0
	a\timer = MilliSecs() + 200 
End Function
Function movelasers()

Color 0,0,200

For a.laserfield = Each laserfield

ang = getangle(a\x,a\y,a\tx,a\ty)

z# = currentplasmaspeed
;DebugLog z
mx# = Sin(ang) 
my# = Cos(ang)



krr = (z)
krrr = (z)


col = False
For i=0 To krr
If a\timer < MilliSecs() Then
	a\ox = a\ox + mx#
End If
a\x = a\x + mx#
If RectsOverlap (a\x,a\y,8,8,a\tx,a\ty,8,8) Then col = True
Next

For i=0 To krrr
If a\timer < MilliSecs() Then
	a\oy = a\oy + my#
End If
a\y = a\y + my#
If RectsOverlap (a\x,a\y,8,8,a\tx,a\ty,8,8) Then col = True
Next


;If a\timer < MilliSecs() Then
;	a\ox = a\ox + (mx*z)
;	a\oy = a\oy + (my*z)
;End If
;
;a\x = a\x + mx*z
;a\y = a\y + my*z
;


;Line a\x,a\y,a\tx,a\ty
Line a\ox,a\oy,a\x,a\y

;If RectsOverlap (a\x,a\y,3,3,a\tx,a\ty,3,3) Then
If col = True Then
	newexplosion(a\x,a\y)
	Delete a
End If

Next

End Function
Function explosioncollision()

	For a.explosionfield = Each explosionfield	
	For b.tracer = Each tracer
		If RectsOverlap(a\x,a\y,5,5,b\x,b\y,5,5) Then ; explo
		gamescore = gamescore + 33
		spacebucks = spacebucks + 3
		spreadexplosion(b\x,b\y)
		Delete b		
		End If
	Next
	Next
		
	; electro
	If MouseDown(2) = True Then		
			For ab.tracer = Each tracer
				For i=0 To 59
					If ebeam(i+1,0) = True Then
						x = ebeam(i,1)
						y = ebeam(i,2)
						If RectsOverlap(ab\x,ab\y,5,5,x,y,5,5) Then
							spreadexplosion(ab\x,ab\y)
							Delete ab
							gamescore=gamescore + 33
							spacebucks = spacebucks + 3						
							i=59
						EndIf
					End If
				Next
		Next
	End If
End Function
Function doexplosions()
	For a.explosionfield = Each explosionfield
		If a\timer<MilliSecs() Then
				yo = False
				a\timer = MilliSecs()+40
				a\frame=nextframe(a\frame,0,11)
			If a\frame = 0 Then
				inibigexplosion(a\x,a\y,1)
				inibigexplosion(a\x,a\y,1)
				Delete a : yo = True
				
			End If
		End If
		If yo = False Then DrawImage explosion,a\x,a\y,a\frame
	 Next
End Function
Function newexplosion(x,y)
	If explosiontimer < MilliSecs() Then
		z = PlaySound(explosionsound)
		ChannelVolume z,.035
		explosiontimer = MilliSecs() + 129
	End If
	a.explosionfield = New explosionfield
	;a\x = Rand(0,GraphicsWidth()-32)
	;a\y = Rand(0,GraphicsHeight()-32)
	a\x = x
	a\y = y
	a\gx = x/32
	a\gy = y/32
End Function
Function nextframe(f,s,e)
	f=f + 1
	If f>e Then f=s
	Return f
End Function
Function createexplosion()
	For i=0 To 11
		SetBuffer ImageBuffer(explosion,i)
		Color 200,40,20
		Oval 16-i*2/2,16-i*2/2,i*3-6,i*3-6,True		
	Next
	SetBuffer BackBuffer()
End Function
Function createmousepointer()

	SetBuffer ImageBuffer(mousepointer)
	Color 160,160,160
	Oval 4,4,16-7,16-7,False
	Color 200,200,200
	Line 8,1,8,16
	Line 1,8,16,8
	Color 0,0,0
	Plot 8,8:Plot 10,8:Plot 6,8	
	SetBuffer BackBuffer()
End Function
Function createstarfield()
	SetBuffer ImageBuffer(starfield)
	For x=0 To 128-2 Step 10
	For y=0 To 128-2 Step 10
	If Rand(0,10) = 1 Then
		krp = Rand(10,60)
		Color  krp,krp,krp
		Plot x,y
	End If
	Next
	Next
	SetBuffer ImageBuffer(starfield2)
	For x=0 To 128-2 Step 10
	For y=0 To 128-2 Step 10
	If Rand(0,10) = 1 Then
		krp = Rand(20,150)
		Color  krp,krp,krp
		Plot x,y
	End If
	Next
	Next
	SetBuffer BackBuffer()
End Function
Function aiplayer()
	For a.tracer = Each tracer
		wp =  a\lastwaypoint + 1
		cnt = 0
		For b.twaypoint = Each twaypoint
			If wp = cnt-1 Then
				ax = a\x
				ay = a\y			
			End If
			If wp = cnt Then
				If ax > a\x Then ax = -ax
				If ay < a\y Then ay= -ay
				tx =(b\x)+Rand(0,ax)
				ty = (b\y)+Rand(0,ay)
				 If ty<GraphicsHeight()-20 Then
					If Rand(0,250) = 0 And slowaifiretimer < MilliSecs() Then
						firelaser(tx,ty)
						slowaifiretimer = MilliSecs() + 100
					End If
				End If
			End If
			cnt=cnt+1
		Next
	Next

End Function
Function selscreenmode()
Graphics 320,280,16,2
SetBuffer BackBuffer()
Local f = LoadFont("verdana",18)
Local f2 = LoadFont("verdana",24)
Local f3 = LoadFont("verdana",29)
Local f4 = LoadFont("verdana",12)
Local cpos = 0
Local ding$[5]
ding[0] = "640 480 16 Windowed"
ding[1] = "640 480 16 Fullsreen"
ding[2] = "800 600 16 Windowed"
ding[3] = "800 600 16 Fullscreen"
ding[4] = "1024 768 16 Windowed"
ding[5] = "1024 768 16 Fullscreen"
While KeyDown(1) = False And exitloop = False
	Cls
	If KeyHit(200) Then
	If cpos > 0 Then cpos = cpos - 1
	End If
	If KeyHit(208) Then
	If cpos < 5 Then cpos = cpos + 1
	End If
	;
	Color 255,255,255
	SetFont f3
	Text GraphicsWidth()/2,0,"Waypoint Invaders",1,0
	SetFont f2
	
	Text GraphicsWidth()/2,80,"Select screenmode",1,0
	;
	SetFont f
	For i=0 To 5 
		If i = cpos Then Color 255,255,0 Else Color 255,255,255
		Text GraphicsWidth()/2,GraphicsHeight()/2-20+i*20,ding[i],1,1	
	Next
	
	Color 255,255,255
	SetFont f4
	Text GraphicsWidth()/2,GraphicsHeight()-15,"Use cursor keys to select mode - Enter to Set",1,1
	
	SetFont f
	
	If KeyHit(28) Then
	Select cpos
		Case 0:gwidth=640:gheight=480:gdepth = 16:smode=2
		Case 1:gwidth=640:gheight=480:gdepth = 16:smode=1
		Case 2:gwidth=800:gheight=600:gdepth = 16:smode=2
		Case 3:gwidth=800:gheight=600:gdepth = 16:smode=1
		Case 4:gwidth=1024:gheight=768:gdepth = 16:smode=2
		Case 5:gwidth=1024:gheight=768:gdepth = 16:smode=1
	End Select
	exitloop = True
	End If
	
	Flip
Wend
EndGraphics()
End Function
Function gamemenu()
;Cls
Color 255,255,255
SetFont f3
Text GraphicsWidth()/2,20,"Waypoint Invaders",1,0
SetFont f
If mainmenutimer < MilliSecs() Then	
	mainmenutimer = MilliSecs() + 800
	If mainmenublink = True Then mainmenublink = False Else mainmenublink = True
End If
If mainmenublink = True
Text GraphicsWidth()/2,GraphicsHeight()/2,"Press Enter to Begin",1,1
End If
SetFont f4
Text GraphicsWidth()/2,GraphicsHeight()/2+40,"lmb - plasma / rmb - electron / space - nuke",1,1
Text GraphicsWidth()/2,GraphicsHeight()/2+60,"F5 - Hyperjump to base planet",1,1
;Text GraphicsWidth()/2,GraphicsHeight()/2+60,"Press F1 to enable ai player.....",1,1
Text GraphicsWidth()/2,GraphicsHeight()/2+80,"Waypoint system by MetalMan",1,1
Text GraphicsWidth()/2,GraphicsHeight()/2+95,"Gamecoding by Nebula",1,1
Text GraphicsWidth()/2,GraphicsHeight()/2+115,"Visit www.Blitzcoder.com for more games",1,1
If Rand(0,30) = 1 Then
	newexplosion(Rand(GraphicsWidth()-32),Rand(GraphicsHeight()-32))
End If
End Function
Function inirtstarfield()
For i=0 To 30
a.rtstarfield = New rtstarfield
a\x = Rand(0,GraphicsWidth())
a\y = Rand(0,GraphicsHeight())
a\mx = 0
a\my = 1+Rnd(1)
Next
End Function
Function rtstarfield()
For a.rtstarfield = Each rtstarfield
Color 165,165,165
Plot a\x,a\y
a\y = a\y + a\my
If a\y > GraphicsHeight() Then a\y = 0
Next
End Function


Function endseq(nd)
If nd = True Then Return

ms = MilliSecs()
tm = CreateTimer(10)
n# = 1
While ms+2000 > MilliSecs()
waittimer(tm)
ChannelVolume mus2,n
If  n>0 Then n=n-.04
Wend
ChannelVolume mus2,1
StopChannel mus2



ms = MilliSecs()

mu = PlayMusic("highscore.mid")
ChannelVolume mu,.5
ClsColor 0,0,0
Color 255,255,255
While (ms + 7200 > MilliSecs()) And KeyDown(1) =False
Cls
Text GraphicsWidth()/2,GraphicsHeight()/2,"Game Over",1,1
Flip
Wend

ms = MilliSecs()
tm = CreateTimer(10)
n# = .5
While ms+2000 > MilliSecs()
	waittimer(tm)
	ChannelVolume mu,n
	If  n>0 Then n=n-.04
Wend
StopChannel mu
resetracers
updateracers
gameon = False
notdoingtimer = MilliSecs() + 9000
End Function


Function electronbeam()


	Color 255,255,0
	mx = MouseX()
	my = MouseY()
	x = GraphicsWidth()/2
	y = GraphicsHeight()-20
	ebeam(0,0) = True
	ebeam(0,1) = x
	ebeam(0,2) = y
	mos = MilliSecs() + 10
	cnt = 0
	For i=0 To 60
	ebeam(i,0) = False
	Next
	While exitloop = False And mos > MilliSecs() And cnt < 59
		If x<mx Then x=x+Rand(7,18)
		If x>mx Then x=x-Rand(7,18)
		If y<my Then y=y+Rand(7,18)
		If y>my Then y=y-Rand(7,18)
		If cnt<59 Then cnt = cnt + 1 Else cnt = 59
		ebeam(cnt,0) = True
		ebeam(cnt,1) = x
		ebeam(cnt,2) = y
		ebeam(cnt+1,0) = False
		If cnt<58 Then ebeam(cnt+2,0) = False
		If RectsOverlap(x,y,16,16,MouseX(),MouseY(),16,16) = True Then exitloop = True
;		If Rand(0,20) = 1 Then DebugLog "!" + MilliSecs()
	Wend
	For i=0 To 59
		If ebeam(i+1,0) = True Then
			Color 255,255,0
			Line ebeam(i,1),ebeam(i,2),ebeam(i+1,1),ebeam(i+1,2)
			Color 255,255,255
			Line ebeam(i,1)+2,ebeam(i,2)+2,ebeam(i+1,1)+3,ebeam(i+1,2)+2
		EndIf		
	Next
End Function

Function intro()
ms = MilliSecs()
a = OpenMovie("clappingmonkey.gif")
If a = 0 Then RuntimeError "!!!"
tm = CreateTimer(4)
b = LoadSound("monkey.wav")
ChannelVolume b,.2
ps = False
While ms+3600>MilliSecs()
Cls
If ps = False Then
If ms+500 < MilliSecs() Then
PlaySound b
ps = True
End If
End If
waittimer(tm)
DrawMovie a,GraphicsWidth()/2-32,GraphicsHeight()/2-48,64,96
Flip
Wend
Delay(500)
End Function

Function galaxymap()

a.galaxytype = First galaxytype
While KeyDown(1)  = False
	Cls
	drawgalaxymap(50,50)
	demomessage()
	If MouseHit(2) Then
		x1 = a\currentplanetx
		y1 = a\currentplanety
		x2 = a\planetselectx
		y2 = a\planetselecty
		;	
	  	dist = distance(x1,y1,x2,y2)
		;
		If hyperjumppower => dist Then ; In range?
			a\currentplanetx = a\planetselectx
			a\currentplanety = a\planetselecty
			Else
			a\currentplanetx = a\planetselectx
			a\currentplanety = a\planetselecty
			;mes.galaxymessage = First galaxymessage
		;	mes\message = galaxystring(x2,y2,1)+" is out of HyperJump reach."
		;	mes\timeout = MilliSecs() + 3000
		End If
	End If
	DrawImage mousepointer,MouseX(),MouseY()
	Flip
Wend
End Function
Function drawgalaxymap(x,y,w=4,h=4)
gw = GraphicsWidth()/100
gh = GraphicsHeight()/80
;
a.galaxytype = First galaxytype


If a\cursortimer < MilliSecs() Then
	a\cursortimer = MilliSecs() + 10
	;
	Select a\cursorstate
		Case 0
			If a\cursorwidth > 0 Then
				a\cursorwidth = a\cursorwidth - 1
				Else
				a\cursorstate = 1
			End If
		Case 1
			If a\cursorwidth < 16 Then
				a\cursorwidth = a\cursorwidth + 1
				Else
				a\cursorstate = 0
			End If
	End Select
End If
cw = a\cursorwidth
For x1=0 To 50
For y1=0 To 50
If galaxy(x1,y1,0) = True Then
	dx = x+x1*gw
	dy = y+y1*gh
	If galaxy(x1,y1,2) = True Then		
		Color 2,5,30
		dw = (hyperjumppower*gw)*2+gw
		dh = (hyperjumppower*gh)*2+gh
		;Rect dx-dw/2,dy-dh/2,dw,dh,True
		Oval dx-dw/2,dy-dh/2,dw,dh
	End If
End If
Next
Next
	x1 = a\currentplanetx
	y1 = a\currentplanety
	dx = x+x1*gw
	dy = y+y1*gh
	
	dw = (hyperjumppower*gw)*2+gw
	dh = (hyperjumppower*gh)*2+gh
	;Rect dx-dw/2,dy-dh/2,dw,dh,True
	Color 5,9,45
	Oval dx-dw/2,dy-dh/2,dw,dh,True

;draw network
Color 40,5,2
For gn.galaxynodes = Each galaxynodes
ax1=x+gn\x1*gw
ay1=y+gn\y1*gh
ax2=x+gn\x2*gw
ay2=y+gn\y2*gh
Line ax1,ay1,ax2,ay2
Next

;draw path
Color 160,160,0
For gn.galaxynodes = Each galaxynodes
If gn\active = True Then 
ax1=x+gn\x1*gw
ay1=y+gn\y1*gh
ax2=x+gn\x2*gw
ay2=y+gn\y2*gh
Line ax1,ay1,ax2,ay2
End If
Next

For x1=0 To 50
For y1=0 To 50
If galaxy(x1,y1,0) = True Then
	dx = x+x1*gw
	dy = y+y1*gh

	Color 255,255,255
	Rect dx-1,dy-1,2,2
	Color 100,100,100
	Plot dx,dy
	If RectsOverlap(dx-gw,dy-gh,gw*2,gh*2,MouseX(),MouseY(),1,1) = True Then
		Color 255,255,0
		Rect (dx-gw)-cw/2,(dy-gh)-cw/2,(gw*2)+cw,(gh*2)+cw,False		
		If MouseHit(1) = True Then
			a\planetselectx = x1
			a\planetselecty = y1
			findpath(a\currentplanetx,a\currentplanety,x1,y1)		
		End If
	End If
	If a\planetselectx = x1 And a\planetselecty = y1 Then
		Color 30,50,250
		Rect dx-8,dy-8,16,16,False
		Color 200,200,200
		Text 50,20,galaxystring(x1,y1,1)
		x1 = a\planetselectx
		y1 = a\planetselecty
		x2 = a\currentplanetx
		y2 = a\currentplanety
		dist = distance(x1,y1,x2,y2)
		Text 240,20,"Distance : " + dist
	End If
	If a\currentplanetx = x1 And a\currentplanety = y1 Then
		Color 20,200,30
		Rect dx-8,dy-8,16,16,False		
	End If
End If
Next
Next
;
; Draw buttons
For but.galaxyobjects = Each galaxyobjects
	mooierect(but\x,but\y,but\w,but\h)
	Color 255,255,255
	mooierecttext(but\inhoud,but\x,but\y,but\w,but\h)
	;Color 255,255,255
	;Text but\x+but\w/2,but\y+but\h/2,but\inhoud,1,1
Next
;
; Draw messages
;
For mes.galaxymessage = Each galaxymessage
	If mes\timeout > MilliSecs() Then
		Color 255,255,255
		Text 50,GraphicsHeight()-60,mes\message	
	Else
		mes\message=""
	End If
Next
End Function
Function mooieRect(x,y,w,h)
	Color 70,70,70
	Rect x,y,w,h
	;
	Color 170,170,170
	;
	For i=0 To 1
		xz=i : yz=i
		If xz=1 Then Color 90,90,90
		Line x+xz,y+yz,x+w+xz,y+yz
		Line x+Xz,y+yz,x+xz,y+h+yz
	Next
	Color 40,40,40
	Line x+w,y+1,x+w,y+h-1
	Line x+w,y+h,x+1,y+h
End Function
Function mooierecttext(t$,x,y,w,h)
Text x+w/2,y+h/2,t$,1,1
End Function


Function findpath(x1,y1,x2,y2) ; main
Local startx[20]
Local starty[20]
Local endx[20]
Local endy[20]
ms = MilliSecs()
; disable path
For a.galaxynodes = Each galaxynodes
	a\active=False
Next
tx1 = x1 : ty1 = y1
wegotit = False ; true = path = found
For i = 0 To 20
	odist = 999 ; long dest
	For a.galaxynodes = Each galaxynodes
		If a\x1 = tx1 And a\y1 = ty1 Then ; found nearest
			; is its dest! out dest?
			For b.galaxynodes = Each galaxynodes
				If a\x1 = b\x1 And a\y1 = b\y1
					If b\x2 = x2 And b\y2 = y2 Then
					wegotit = True
					startx[i] = a\x1
					starty[i] = a\y1
					endx[i] = b\x2
					endy[i] = b\y2
					oi = i
					EndIf
				End If
			Next
			; Find closest to final dest
			If wegotit = False Then
				adist = distance(a\x2,a\y2,x2,y2)
				If odist > adist And adist > 0 Then
					odist = adist
					startx[i] = a\x1
					starty[i] = a\y1
					endx[i] = a\x2
					endy[i] = a\y2
				End If
			End If
		End If
	Next
	If wegotit = False Then
		tx1 = endx[i]
		ty1 = endy[i]
		Else
		i = 20
	End If
Next

If wegotit = False Then Return False
For i=0 To oi
	For a.galaxynodes = Each galaxynodes
		If a\x1 = startx[i] And a\y1 = starty[i]
		If a\x2 = endx[i] And a\y2 = endy[i]
		a\active = True
		End If
		End If
	Next
Next

;DebugLog MilliSecs() - ms
;Notify MilliSecs() - ms
;debugding$ = "shortest Path found in " + (MilliSecs() - ms) + " MilliSecs.."
mes.galaxymessage = First galaxymessage
mes\message = "A path was found in " + (MilliSecs() - ms) + " MilliSecs.."
mes\timeout = MilliSecs()+2000
mes\scroll = 0
;DebugLog oi + " = Distance " 


End Function

Function findfirstclosestpath(x1,y1,x2,y2)
Local endx[20]
Local endy[20]
For a.galaxynodes = Each galaxynodes
a\active=False
Next
mgdist = distance(x2,y2,x1,y1)
odist = 999
For a.galaxynodes = Each galaxynodes
If a\x1 = x1 And a\y1 = y1 Then
dist  = distance(a\x2,a\y2,x1,y1)
;DebugLog a\x2 + " : " + a\y2  + " : " + x1 + " : " + y1
;If odist > dist And dist > 0 Then odist = dist : ox = a\x2 : oy = a\y2 ; shortest
adist = distance(a\x2,a\y2,x2,y2)
If odist > adist Then
odist = adist
ox1 = a\x1
oy1 = a\y1
ox2 = a\x2
oy2 = a\y2
End If
End If
Next

For a.galaxynodes = Each galaxynodes
	If a\x1 = ox1 And a\y1 = oy1
	If a\x2 = ox2 And a\y2 = oy2
	a\active = True
	End If
	End If
Next


DebugLog odist + " Distance " 


End Function

Function findclosestpath(x1,y1,x2,y2)
For a.galaxynodes = Each galaxynodes
a\active=False
Next

For a.galaxynodes = Each galaxynodes
If a\x1 = x1 And a\y1 = y1 Then
If a\x2 = x2 And a\y2 = y2 Then
a\active=True
End If
End If
If a\x2 = x1 And a\y2 = y1 Then
If a\x1 = x2 And a\y1 = y2 Then
a\active = True
End If
End If

Next
End Function
Function findneighbours(x,y,ln) ; main
ln = 20

;Dim nodebuffer(ln,ln
;cnt = 0

galaxy(x,y,2) = True
ms = MilliSecs()

ms = MilliSecs()
newstuff = True
While newstuff = True
newstuff = False
For x2=0 To 50
For y2=0 To 50
If galaxy(x2,y2,0) = True
For x1=x2-ln To x2+ln
For y1=y2-ln To y2+ln
If x1>0 And x1<50 And y1>0 And y1<50 Then
	If galaxy(x1,y1,0) = True Then
	skipthis = False
	For q.galaxynodes = Each galaxynodes
		If q\x1 = x1 And q\y1 = y1
			If q\x2 = x2 And q\y2 = y2
				skipthis = True
			End If
		End If
	Next
	If skipthis = False
	If distance(x1,y1,x2,y2) =< ln Then
		galaxy(x1,y1,2) = True
		;
		b.galaxynodes = New galaxynodes
		b\x1 = x1
		b\y1 = y1
		b\x2 = x2
		b\y2 = y2
		Newstuff = True
	End If
	End If
	End If
End If	
Next
Next
End If
Next
Next
Wend

;Notify MilliSecs()-ms + " msss"

End Function

Function inigalaxy()

but.galaxyobjects = New galaxyobjects
but\x = 50
but\y = GraphicsHeight() - 40
but\w = 80
but\h = 16
but\down = False
but\inhoud = "Travel"

mes.galaxymessage = New galaxymessage
mes\message = "Use left and right mouse button on the map. green = your pos."
mes\timeout = MilliSecs() + 3000
;
makeplanetnames
a.galaxytype = New galaxytype
a\cursorwidth = 0
a\cursortimer = MilliSecs()

; Populate planet
cnt = 1
pl.planettype = First planettype
pl = After pl
For x=1 To 49 Step 2
	For y=1 To 49 Step 2
		If Rand(0,10) = 1 Then
			galaxy(x,y,0) = True
			pl\x = x1
			pl\y = y1
			galaxystring(x,y,0) = True
			galaxystring(x,y,1) =  pl\name
			cnt=cnt+1
			pl = After pl
		End If
	Next
Next
; Set starting position
ms = MilliSecs() + 2000
done = False
zpl.planettype = First planettype
While exitloop = False
	If ms< MilliSecs() Then exitloop = True
	If a\currentplanetx = 0 And a\currentplanety = 0 Then exitloop = True
	For x=1 To 49 Step 2
		For y=1 To 49 Step 2
			If done = False
			If galaxy(x,y,0) = True
			If Rand(0,10) = 1 Then	
				a\currentplanetx = x
				a\currentplanety = y
				zpl\x = x
				zpl\y = y
				galaxystring(x,y,0) = True
				galaxystring(x,y,1) =  "Earth"
				done = True
			End If
			End If
			End If
		Next
	Next
Wend
findneighbours(a\currentplanetx,a\currentplanety,hyperjumppower)
End Function
Function makeplanetnames()
Local finalnames$[200]
Local basename$[10]
basename[0] = "Alpha"
basename[1] = "Beta"
basename[2] = "Theta"
basename[3] = "Kappa"
basename[4] = "Gamma"
basename[5] = "Delta"
basename[6] = "Phi"
basename[7] = "Omega"
basename[8] = "Sigma"
basename[9] = "Omega"
basename[10] = "Chi"
Local basenumber$[10]
basenumber[0] = "I"
basenumber[1] = "II"
basenumber[2] = "III"
basenumber[3] = "IV"
basenumber[4] = "V"
basenumber[5] = "VI"
basenumber[6] = "VII"
basenumber[7] = "VIII"
basenumber[8] = "IX"
basenumber[9] = "X"
basenumber[10] = "XI"
;
For i=0 To 200
	a = Rand(0,2)
	For ii=0 To a
		b = Rand(0,10)
		finalnames[i]=finalnames[i]+basename[b]+" "
	Next
Next
For i=0 To 200
dc = 0
For ii=i To 200
If finalnames[i] = finalnames[ii] Then dc=dc + 1
If dc > 1 Then
finalnames[ii] = finalnames[ii] + basenumber[dc-1] : ii=200
End If

Next
Next
z.planettype = New planettype
z\name$ = "Earth"
z\id = 0
For i=0 To 200
z.planettype = New planettype
z\name$ = finalnames[i]
z\id = i+1
;DebugLog finalnames[i]
Next

;
End Function

; Planet landing section
Function aproachplanet()
Local curs = 1
Local dp = 0 ; menu depth
Local docked = False
Local docking = False
ms = MilliSecs() + 4000
py# = 0
py2# = 0
;ztimer = CreateTimer(40)
While exitloop = False 
	Cls
	;DebugLog "!!!!!!!" + MilliSecs()
	;
;	waittimer(ztimer)
	dostarfield(False)
	dostarfield()	
	;
	
	plx = (GraphicsWidth()/2)-ImageWidth(planets(0))/2
	ply = py
	DrawImage planets(0),plx,ply
	rtstarfield()
	;
	;
	shipy = GraphicsHeight()-ImageHeight(pship) - py2
	If RectsOverlap(py,0,5,64,shipy-ImageHeight(pship)*4,0,5,64) = False		
		dist = shipy-py
		py=py+.5+dist/100:py2 = py2 +.5+ dist/100
		If dist < 200 Then
			docking = True							
		End If
	End If
	;
	If docking = True Then
		If n<22 Then n=n+1
		If n= 22 Then docked = True
	End If
	;
	If KeyHit(63) Then exitloop = True
	;
	If docked = True Then
		curs = planetinterface(curs,dp)
		If KeyHit(28)
		Select dp
		Case 0
		Select curs
			Case 1 ; Repair ship
			If spacebucks > 100 - power Then
			For i=0 To 9
				If power < 100 Then power = power + 1 : spacebucks = spacebucks - 1
			Next
			End If
			Case 2 ;lweapon buy
				dp = 1
				curs = 0
			Case 3 : ;leave system
			exitloop = True			
		End Select
		Case 1
			Select curs ; buy upgrades
				Case 0 :
				If spacebucks > (currentplasmaspeed + 1) * 5
					If currentplasmaspeed < 20 Then
						spacebucks = spacebucks - (currentplasmaspeed + 1) * 5
						currentplasmaspeed = currentplasmaspeed + 1
					End If
				End If
				dp = 0 : curs = 2
				Case 1 :
				If spacebucks > (currentshiprecharge + 1) * 10
					spacebucks = spacebucks - (currentshiprecharge + 1) * 10
					currentshiprecharge = currentshiprecharge + 1
				End If
				dp = 0 : curs = 2
				Case 2 :
				If spacebucks > (currentelectronrecharge + 1) * 15
					If currentelectronrecharge < 51
					spacebucks=spacebucks-(currentelectronrecharge + 1) * 15
					currentelectronrecharge=currentelectronrecharge+1
					End If
				End If
				dp = 0 : curs = 2
				Case 3 :
				If spacebucks > (currentnukerecharge + 1) * 20
					If currentnukerecharge < 99 Then
						spacebucks=spacebucks-(currentnukerecharge + 1) * 20
						currentnukerecharge =currentnukerecharge+1
					End If
				End If
				dp = 0 : curs = 2
				Case 4 : dp = 0 : curs = 2
			End Select
		End Select
		End If
	End If
	;
	;DrawImage pship,GraphicsWidth()/2-ImageWidth(ship1)/2,shipy
	shipx = GraphicsWidth()/2-ImageWidth(ship1)/2
	DrawImage rpship(n),shipx,shipy
	;
	ingameinterface()
	If KeyDown(1) = True Then exitloop = True
	;If ms < MilliSecs() Then exitloop = True
	Flip
Wend
FlushKeys()
Delay(120)
leaveplanet(shipx,shipy,plx,ply,n)
End Function
Function planetinterface(curs,dp=0)

hc = GraphicsWidth()/2 + 64
vc = GraphicsHeight()/2 - 5*12
Local men$[10]
Local men2$[10]
Local costmen[10]
men[0] = "Menu"
men[1] = "Repair"
men[2] = "Buy"
men[3] = "Leave"
;Global currentnukerecharge = 0
;Global currentelectronrecharge = 0
;Global currentshiprecharge = 0
;Global currentplasmaspeed = 0
men2[4] = "Back<< "
men2[0] = "Plasma Missile Upgrade " + (currentplasmaspeed + 1)
If currentplasmaspeed > 19 Then men2[0] = "Plasma Missiles Maxed"
men2[1] = "Ship Regeneration Upgrade " + (currentshiprecharge + 1)
If currentshiprecharge > 98 Then men2[1] = "Ship regeneration Maxed out"
men2[2] = "Electron Upgrade " + (currentelectronrecharge + 1)
If currentelectronrecharge  = 50 Then men2[2] = "Electron Maxed out"
men2[3] = "Nuke Upgrade " + (currentnukerecharge + 1)
If currentnukerecharge > 98 Then men2[3] = "Nuke Upgrade Maxed Out"

costmen[0] = (currentplasmaspeed + 1) * 5
costmen[1] = (currentshiprecharge + 1) * 10
costmen[2] = (currentelectronrecharge + 1) * 15
costmen[3] = (currentnukerecharge + 1) * 20

Color 255,255,255
SetFont f4
Select dp
Case 0
If KeyHit(200) And curs >  1 Then curs = curs - 1
If KeyHit(208) And curs < 3 Then curs = curs + 1
Case 1
If KeyHit(200) And curs >  0 Then curs = curs - 1
If KeyHit(208) And curs < 4 Then curs = curs + 1
End Select
For i=0 To 5
	Select i
	Case 0 : Color 255,255,255
	Default : Color 200,200,200
	End Select
	If dp = 0
		If curs = i Then
			Color 255,255,0
			Rect hc-8,vc+i*12+2,6,8
		End If
	End If
	Text hc,vc+i*12,men[i]
Next
If dp=1 Then
For i=0 To 4
	If i=curs Then
		Color 255,255,0 Else Color 255,255,255	
	End If
	Text hc+60,vc+i*12,men2[i]
	
Next
If spacebucks > costmen[curs] Then Color 255,255,255 Else Color 0,0,255
Text hc+60,vc-14,"Total cost : " + costmen[curs]
End If
Return curs
End Function
Function leaveplanet(shipx,shipy,plx,ply,rot)
	;timer = CreateTimer(60)
	ms = MilliSecs() + 3000
	Local py#
	Local py2#
	n = rot
	While exitloop = False 
		Cls
		;
	;	waittimer(timer)
		dostarfield(False)
		dostarfield(True)	
		;
		DrawImage planets(0),plx,ply
		rtstarfield()
		ingameinterface
		;
		;
		If ms2 < MilliSecs() And n<45 Then
			n = n + 1
			ms2 = MilliSecs() + 50		
		End If
		If n=>45 Then
			shipy = shipy + mx#
			mx# = mx# + .12
			ply=ply-mx
		End If
		;	
		;
		DrawImage rpship(n),shipx,shipy
		;
		If KeyDown(1) = True Then exitloop = True
		If ms < MilliSecs() Then exitloop = True
		Flip True
	Wend
End Function



Function rotatepship()
cnt = 0
ms = MilliSecs() + 5000
For i=0 To 359 Step 4
DebugLog i
TFormFilter 0 
If ms < MilliSecs() Then End
	rpship(cnt) = CopyImage(pship)
	MaskImage rpship(cnt),255,255,255
	RotateImage rpship(cnt),i
	cnt=cnt + 1
Next
Return
End Function
;
; Math
Function distance#(x1,y1,x2,y2)
	xd=Abs(x2-x1)
	yd=Abs(y2-y1)
	Return Sqr((xd*xd)+(yd*yd))
End Function
;
; Waypoint system
;
Function render()

If gameon = False Then
If kwak = True Then
 Color 255,255,255
 Text 0,0,"LMB: place waypoints. RMB: clear. Space: reset, Enter: start/pause"
 For w.twaypoint=Each twaypoint
   Color 255,0,0
   Rect w\x-(PathWaySize/2),(w\y-pathwaysize/2),pathwaysize,pathwaysize,False
   Oval w\x-2,w\y-2,4,4,True
   Text w\x,w\y,w\PathOrder
 Next
End If
End If
 For r.tracer=Each tracer
  Color r\R,r\G,r\B
  ;Oval r\x-1,r\y-1,6,6,True
  DrawImage ship1,r\x-1,r\y-1
 Next
End Function
Function updatewaypoints()
Return
 If MouseHit(1) Then ; create a new waypoint
    wp.twaypoint=New TWayPoint
    wp\x=MouseX()
    wp\y=MouseY()
    waypoints=waypoints+1
    wp\PathOrder=Waypoints
 End If
 If MouseHit(2) Then ; clear waypoints
    For wp.twaypoint=Each twaypoint
        Delete wp
    Next
    waypoints=0
 End If
End Function
Function randomwaypoints()

   For wp.twaypoint=Each twaypoint
        Delete wp
    Next
    waypoints=0

zz = Rand(5,10)
For i=0 To zz
	wp.twaypoint=New TWayPoint
    wp\x=Rand(0+50,GraphicsWidth()-100)
    wp\y=Rand(0+50,GraphicsHeight()-100)
    waypoints=waypoints+1
    wp\PathOrder=Waypoints
Next

; Final Attack!!
wp.twaypoint=New TWayPoint
wp\x = GraphicsWidth()/2
wp\y = GraphicsHeight()-48
waypoints=waypoints+1
wp\PathOrder=Waypoints
; Retreat
wp.twaypoint=New TWayPoint
wp\x = Rand(0+50,GraphicsWidth()-100)
wp\y = Rand(0+50,GraphicsHeight()-250)
waypoints=waypoints+1
wp\PathOrder=Waypoints
End Function
Function resetracers()
  For r.tracer=Each tracer ; delete any eventual old racer
     Delete r
  Next
  For cnt%=1 To RacerCount Step 1 ; Create new racers
      p.TRacer=New TRacer
      p\LastWayPoint=-1 ; so next waypoint aimed for will be '0'
      p\x=10
      p\y=400+(cnt*10)
      p\speed=2
      p\r=Rand(255)
      p\g=Rand(255)
      p\b=Rand(255)
  Next
End Function
Function addracers()
If starttimer + 3000 < MilliSecs() Then
	level = level + 1
	starttimer = MilliSecs()
End If
  ;For cnt%=1 To 5 Step 1 ; Create new racers
  While zub < (level*2)+5
      p.TRacer=New TRacer
      p\LastWayPoint=-1 ; so next waypoint aimed for will be '0'
	  If zub<=9 Then
      p\x=10
      p\y=350+(cnt*10)
	  ElseIf zub>9 And zub=<20
	  p\x=GraphicsWidth()-10
      p\y=350+(cnt*10)
	  ElseIf zub>20 And zub=<30
	  p\x=GraphicsWidth()-10
      p\y=150+(cnt*10)
	  ElseIf zub>30
	  p\x=Rand(0,GraphicsWidth()-10)
      p\y=0
	  End If
      p\speed=2
      ;p\r=Rand(255)
      ;p\g=Rand(255)
      ;p\b=Rand(255)
	  zub = zub + 1
  Wend
End Function
Function GetNextWayPoint.twaypoint(CurrentWayPoint%)
  retval.twaypoint=Null
 For wp.twaypoint=Each twaypoint
     If wp\PathOrder=CurrentWaypoint+1 Then
        retval=wp
     End If
 Next
 If retval=Null Then
  retval=First Twaypoint
 End If
 Return retval
End Function
Function GetAngle%(x#,y#,x1#,y1#)
 at#=ATan2(x-x1,y-y1)
 at=at-180
 If at>360 Then
	at=at-360
 Else If at<0 Then
	at=at+360
 End If
 Return at
End Function
Function CheckWayPoints(r.tracer,wp.twaypoint)
 If RectsOverlap(r\x,r\y,2,2,wp\x-(pathwaysize/2),wp\y-(pathwaysize/2),pathwaysize,pathwaysize) Then
    r\LastWayPoint=r\Lastwaypoint+1
 End If
End Function
; returns -1 or 1 depending if we should decrease or increase angle
; to get to the next waypoint
Function compareangle(angle1,angle2)
 val#=0
 If Abs(angle1-angle2)<>10 Then
  If angle1<angle2 Then
     val= 20
  Else If angle1>angle2 Then
    val= -20
  End If
  If ((angle1-angle2)>180) Or ((angle2-angle1)>180) Then
     val=-val
  End If
 End If
 Return val
End Function
Function updateracers()

 For r.tracer=Each tracer
     wp.TWaypoint=GetNextWayPoint(r\lastwaypoint)
     If wp<>Null Then
      angle#=GetAngle(r\x,r\y,wp\x,wp\y) ; now I have the angle to the next waypoint
      If Rand(3)=2 Then                  ; make some random confusion to avoid 'perfect' navigation
       aglModifier#=CompareAngle(r\direction,angle)
       ; and then, the faster you go, the wider the turns get
       aglModifier=aglModifier-r\speed
       r\direction=r\direction+aglmodifier
      End If
      If r\direction>360 Then
         r\direction=r\direction-360
      Else If r\direction<0 Then
        r\direction=r\direction+360
      End If
      xsp#=r\speed*Sin(r\direction)
      ysp#=r\speed*Cos(r\direction)
      x_diff= Abs(r\x-wp\x)
      y_diff= Abs(r\y-wp\y)
      r\distance= Sqr(x_diff * x_diff + y_diff * y_diff)
      r\x=r\x+(xsp/1.6)
      r\y=r\y+(ysp/1.6)
      CheckWaypoints(r,wp)
      ; Now, if there's a long way to next waypoint, allow acceleration
      If r\distance>100 Then
         If r\speed<maxspeed Then
           r\speed=r\speed+0.1
         End If
      End If
     End If
	 If r\y > GraphicsHeight() - 120
	 If Rand(0,7) = True Then
	 If r\x > GraphicsWidth()/2-150 And r\x < GraphicsWidth()/2+150 Then
	 	;newexplosion(GraphicsWidth()/2+Rand(20),GraphicsHeight()-20)
		If explosiondelaytimer < MilliSecs() Then
			For zi = 0 To 4
				spreadexplosion(GraphicsWidth()/2-10+Rand(20),GraphicsHeight()-20)
			Next
			explosiondelaytimer = MilliSecs() + 250
		End If
	 	power = power - 1
		flashscreen = True
		flashtimer = MilliSecs() + 4
	 End If
	 End If
	 End If
     ; otherwise if it's short, deaccelerate
     If r\distance<100 Then
        If r\speed>0.5 Then
           r\speed=r\speed*.98
        End If
     End If
 Next
End Function


Function demomessage()
a.galaxymessage = First galaxymessage
If a\timeout + 2500 < MilliSecs() Then

a\timeout = MilliSecs() + 7000
Select a\scroll
Case 0
a\message = "The Pathfinding method is distance based.":a\scroll = a\scroll + 1
Case 1
a\message = "Nodes are placed randomly on the map, then connected.":a\scroll = a\scroll + 1
Case 2
a\message = "This based on a predetermined and dynamic distance setting.":a\scroll = a\scroll + 1
Case 3
a\message = "Shorter distance settings mean less connections.":a\scroll = a\scroll + 1
Case 4
a\message = "The path is being looked up using a nearest-node-next method.":a\scroll = a\scroll + 1
Case 5
a\message = "The target nodes distance is compared with the nearest start nodes.":a\scroll = a\scroll + 1
Case 6
a\message ="The nearest node, closest to the target node is added to the path.":a\scroll = a\scroll + 1
Case 7
a\message ="This brings a couple of problems with complex and hard to reach paths.":a\scroll = a\scroll + 1
Case 8
a\message ="Several nodes closer to the target, are further away.":a\scroll = a\scroll + 1
Case 9
a\message ="Nothing a little tweaking and experimenting will not fix.":a\scroll = a\scroll + 1
Case 10
a\message ="End message":a\scroll = a\scroll + 1
Case 11
a\message ="(bleep)":a\scroll = a\scroll + 1
Default
a\scroll = 0
End Select
End If
End Function

