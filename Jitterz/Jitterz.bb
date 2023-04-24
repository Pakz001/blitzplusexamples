; game
;
Include "jitterssetup.bb"
;

Global Togamestring$
Global ignorelastkeypress = False
Global Lasticetype = 0

Global timer = CreateTimer(60)
;
.mainloop

While we<>$803
we = WaitEvent()
Select we
	Case $101 	;- Key down 
	Case $102 	;- Key up		
		If EventData() = 1 Then Exit
	Case $103 	;- Key stroke 
	Case $201 	;- Mouse down
		mmx = EventX():	mmy = EventY()	
	Case $202 	;- Mouse up
	Case $203 	;- Mouse move 
	Case $204 	;- Mouse wheel 
	Case $205 	;- Mouse enter 
	Case $206 	;- Mouse leave 
	Case $401 	;- Gadget action 
	Case $801 	;- Window move 
	Case $802 	;- Window size 
	Case $803 	;- Window close 
	Case $804 	;- Window activate 
	Case $1001 	;- Menu event 
	Case $2001 	;- App suspend 
	Case $2002 	;- App Display Change 
	Case $2003 	;- App Begin Modal 
	Case $2004 	;- App End Modal
	Case $2002 	;- App resume 
	Case $4001	;- Timer tick 
	;	
	setclscol(levelclscolor(currentlevel),0)
	Cls
	WaitTimer(timer)
	;
	If MouseDown(1) = True Then
	;Notify mmx
	If mmx<320 And mmy<240 Then
	playerx=mmx
	playery=mmy
	End If
	End If
	;
	showfps()	
	debuggrid()	
	switchlevels()
	levelselectscreen()
	levelselect()
	letsbemario()
	;
	;
	readuserinput()
	alignblock()
	delayedpoopexecution()
	doicesliding()
	doplayeranimation()
	gravity()
	doobjects()
	updateplayerobjects()
	For i=0 To 15
		dobaddies()
	Next
	drawmap(0,0)
	drawbaddies(0,0)
	drawsprites()
	;
	If KeyDown(keytab) = True And statusbary<0 Then showstatusbar()
	If KeyDown(keytab) = True And statusbary = 0 Then hidestatusbar()
	;
		
;	For x1= 0 To 3
;	Rect baddieslocation(0,0)+x1*16,baddieslocation(0,1)-15,16,16
;	Next
	;
	DrawImage statusbarimage,statusbarx,statusbary
	
	;a = iscoll(playerx,playery,1)
	;a = iscoll(playerx,playery,4)
	;a = iscoll(playerx,playery,5)
	;
	;setcol(2,0)
	Text 30,62,iceslidevalue
	
	Text 10,32,"level : " + currentlevel + " Use - and + to cycle thru maps"
	Text 10,32+16," JFC "+jumpfromclimb
	Text 10,32+16+16,"fps "+fps
	Text 96,64,"playerx : " + playerx + " mx : " +MouseX()
        ;	Text 0,0,playergravityactive
    For i=0 To 2
    	Text 10,64+16+i*16,a
	Next
;	Text 0,20,moveableblockinway(2,0)
	If Togamestring$="showtiles" Then showtiles
	FlipCanvas can

	End Select
Wend
End

.Functions

;
; This function will place a falling block on its end location
; after it has fallen thru the screen.
;
Function processblockthrulevel(level,x)
Local oldlevel = currentlevel
currentlevel = level
For y=0 To 15
	If isblocked(x+16,y*16) = True Then
		processgameobjects(x+16,(y*16)-16) 
		;gamemap(level,(x/16)+1,y-1,2) = ispushblock
		; Insert the new block in the object system
		newobject = -1
		For i=0 To maxobjects
		If objects(level,i,0) = False Then newobject = i : Exit
		Next
		If newobject = -1 Then RuntimeError("To many objects in level")
		objects(level,newobject,0) = ispushblock
		objects(level,newobject,1) = x
		objects(level,newobject,2) = (y*16)-16
		;RuntimeError (gamemap(level,(x/16)+1,y-1,2))
		;processgameobjects(x,(y*16)-16)
		currentlevel = oldlevel	
		Return
	End If
Next
currentlevel = oldlevel
End Function

;
; This function removes the blocked bananas from the level
;
Function removeblockedbananas(level)
For y=0 To 15
	For x=0 To 20
		If gamemap(level,x,y,2) = isblockedbanana Then
			gamemap(level,x,y,2) = 0
		End If
	Next
Next
End Function
;
; This function returns false if there are no more bananas on
; the given level
;
Function checkformorebananas(level)
For y=0 To 15
	For x=0 To 20
		If gamemap(level,x,y,2) = isbanana Then Return True
	Next
Next
End Function

;
; This function will delay the bullets that are fired
;
Function delayedpoopexecution()
If delayedpoopright = True Or delayedpoopleft = True Then
	If delayedpooptimer + delayedpoopdelay < MilliSecs() Then
		If delayedpoopright = True Then
			createplayerobject(findplayerobject(),0,1,1)			
			delayedpoopright = False
		End If
		If delayedpoopleft = True Then
			createplayerobject(findplayerobject(),0,0,-1)
			delayedpoopleft = False	
		End If
	End If
End If
End Function
;
;
; This function lets the player slide on the ice
;
Function doicesliding()
If playergravityactive = True Then Return


;If gamemap(currentlevel,playerx/16,(playery+1)/16,0) < 80 Or gamemap(currentlevel,playerx/16,(playery+1)/16,0) > 88 Then
; 
n = returnslidetile(playerx,playery+1)

If n = 0 Then Return
; 
; If we change slidable surface then change the sliding speed
If Not Lasticetype = n And (Not iceslidevalue = 0) Then
;If n = 1 And iceslidevalue >1 Then iceslidevalue = 1
;If n = 1 And iceslidevalue <-1 Then iceslidevalue = -1
;If n = 2 And iceslidevalue > .9 Then iceslidevalue = iceslidevalue *2
;If n = 2 And iceslidevalue <-.9 Then iceslidevalue = iceslidevalue*2
If n = 1 And iceslidevalue >0 Then iceslidevalue = iceslidevalue/ 2
If n = 1 And iceslidevalue <0 Then iceslidevalue = iceslidevalue/2
If n = 2 And iceslidevalue >0 Then iceslidevalue = iceslidevalue *2
If n = 2 And iceslidevalue <0 Then iceslidevalue = iceslidevalue*2
End If
Lasticetype = n
;
If n = 0 Then
	If Not gamemap(currentlevel,playerx/16,(playery+1)/16,0) = 0 Then
		iceslidevalue = 0
	End If
	If gamemap(currentlevel,playerx/16,(playery+1)/16,0) = 0 And iceslideairtimer > 0 Then 
		iceslideairtimer = iceslideairtimer - 1
	End If
End If
;
; Do the actual sliding
If isblocked(playerx+1,playery) = False And playerobjectcollision(playerx+1,playery) = False Then
	If isblocked(playerx+1,playery-1) = False Then
		If isblocked(playerx-1,playery) = False And playerobjectcollision(playerx-1,playery) = False Then
			If isblocked(playerx-1,playery-1) = False Then
				If (playerx < 320-9) And (playerx > 9) Then playerx = playerx + iceslidevalue Else iceslidevalue = 0
				If iceslidevalue < 0 Then iceslidevalue = iceslidevalue + 0.03 
				If iceslidevalue > 0 Then iceslidevalue = iceslidevalue - 0.03
				If iceslidevalue >-0.05 And iceslidevalue <0.05 Then iceslidevalue = 0				
			End If
		End If
	End If
End If

End Function

;
; Function that returns 0 if a tile is slidable 1 if a tile is medium slidable
; and 2 if it is very slidable
;
Function returnslidetile(x,y)
x = x /16
y = y /16
If gamemap(currentlevel,x,y,0) >= 80 And gamemap(currentlevel,x,y,0) <= 88 Then
	Return 1
End If
If gamemap(currentlevel,x,y,0) = 54  Then
	Return 2
End If
End Function

; Shows the debug grid the user presses 1
;
;
Function debuggrid()
	; Show the debug grid
	If KeyHit(2) = True
		If showdebuggrid = False Then
			showdebuggrid = True
			Else
			showdebuggrid = False
		End If
	End If
End Function
;
; Switch thru the levels using the plus and minus keys
;
Function switchlevels()
	If KeyHit(keyminus)=True Then
		If currentlevel > 0 Then
		scrolllevel(currentlevel,currentlevel-1,Rand(0,3))
		currentlevel = currentlevel - 1
		createlevelai()
		setplayerstartlocation(4)
		End If
	End If
	If KeyHit(keyis)=True Then
		If currentlevel < 256 Then
		currentlevel = currentlevel + 1
		scrolllevel(currentlevel-1,currentlevel,Rand(0,3))
		createlevelai()
		setplayerstartlocation(4)
		End If
	End If
End Function
;
;
; Show the level select screen
Function levelselectscreen()
	; show level map selector
	If KeyHit(keym) = True Then
		selectmap()
	End If
End Function
; Show the player as mario
;
;
Function letsbemario()
	;If KeyHit(keyf1) = True Then
	If Togamestring$ = "bemario" And marioactive = False Then
		marioactive = True : Togamestring$=""
	Else If Togamestring$ = "bemario"
		marioactive =  False: : Togamestring$=""
	End If
	If Togamestring$ = "letsnotbemario" And marioactive = True Then
		marioactive = False : Togamestring$=""
	End If
	;If marioactive = False Then marioactive = True Else marioactive = False
	;End If
End Function
;
;
; This function lets the user select to go to a level
Function levelselect()
	; Select level thru input
	If KeyHit(keyenter)
		FlushKeys()
		Delay(50)
		Togamestring$ = ""
		While Not KeyDown(keyenter) Or KeyDown(1) Or MouseDown(1) Or MouseDown(2) Or MouseDown(3)
			; Draw the game screen
			drawmap(0,0)
			drawbaddies(0,0)
			drawsprites()
			; Draw the console type stuff
			Color 0,0,0
			Rect 0,0,320,10
			Color 255,255,255			
			Text 0,0,"A t  y o u r  c o m m a n d  : " + Togamestring$		
			; Read a key
			a = GetKey()
			If a>32 And a<128 Then
				Togamestring$ = Togamestring$ + Chr$(a)
			End If
	
			; Backspace
			If KeyDown(14) Then
				If Len(Togamestring) > 0
					Togamestring = Left(Togamestring,Len(Togamestring)-1)
					Delay(100)
				End If
			End If	
			
			Delay(1)	
			FlipCanvas can
			friet = 0
			; Wait until the user presses a key
			Repeat
				For i=0 To 255
					If KeyDown(i)=True Then friet=True
				Next
				Delay(1)
			Until friet	
		Wend
		;
		a = Togamestring$
		If a>0 And a<256 Then
			currentlevel = a
			scrolllevel(currentlevel-1,currentlevel,Rand(0,3))
			createlevelai()
			setplayerstartlocation(4)
			Delay(500) : FlushKeys()
		End If
		Delay(200)
		FlushKeys()
		FlushEvents()
	End If	
End Function
;
; Hide statusbar
;
Function hidestatusbar()
Local clocktimer = MilliSecs()
While statusbary > -32
Cls
If clocktimer+10 < MilliSecs() Then
statusbary=statusbary - 1
clocktimer = MilliSecs()
End If
drawmap(0,0)
drawbaddies(0,0)
drawsprites()
DrawImage statusbarimage,statusbarx,statusbary
Flip
Wend
End Function
;
; Show the statusbar
;
Function showstatusbar()
Local clocktimer = MilliSecs()

While statusbary < 0
Cls
If clocktimer+10 < MilliSecs() Then
statusbary = statusbary + 1
clocktimer = MilliSecs()
End If
drawmap(0,0)
drawbaddies(0,0)
drawsprites()
DrawImage statusbarimage,statusbarx,statusbary

Flip
Wend
End Function
;
; This function animates the sprite
;
Function doplayeranimation()
If playeranimtimer + playeranimdelay < MilliSecs() Then
	playeranimpointer = playeranimpointer + 1
	a = playeranim(playercurrentanim,playeranimpointer)
	; If the player is throwing then end the animation
	If a=-1 Then
		If playercurrentanim = playerthrowingleft Then setplayeranimation(playerstandingleft) : Return
		If playercurrentanim = playerthrowingright Then setplayeranimation(playerstandingright) : Return
	End If
	
	If a=-1 Then playeranimpointer = 0 : a = playeranim(playercurrentanim,playeranimpointer) 
	playeranimframe = a
	
	;
	playeranimtimer = MilliSecs()
End If
End Function
;
; This function sets the sprite animation
;
Function setplayeranimation(anim)
	playeranimframe = playeranim(anim,0)
	playercurrentanim = anim
	playeranimpointer = 0
End Function
;
; This function returns a free position in a array
;
Function findplayerobject()
	For i=0 To maxplayerobjects
		If playerobjects(i,0) = False Then
			Return i
		End If
	Next
	Return -1
End Function
;
; This function creates the poop and flaming poop sprites 
;
Function createplayerobject(slot,objecttype,direction,speed)
	;active,x,y,speed,Type, frame
	playerobjects(slot,0) = True
	;
	If direction = 0 Then 
		playerobjects(slot,1) = playerx - 16
	End If
	If direction = 1 Then
		playerobjects(slot,1) = playerx + 8
	End If
	;
	playerobjects(slot,2) = playery
	playerobjects(slot,3) = speed
	playerobjects(slot,4) = objecttype
	;
	Select objecttype
		Case 0
			playerobjects(slot,5) = 140 ; normal poop
			poopanimpointer(slot) = 0
			poopanimdelay(slot) = defaultpoopanimdelay
		Case 1
			playerobjects(slot,5) = 145 ; flaming poop
			poopanimpointer(slot) = 0
			poopanimdelay(slot) = defaultpoopanimdelay
	End Select
	;
	playerobjects(slot,6) = 0
	;
End Function 
;
; This function updates the player objects (poop)
;
Function updateplayerobjects()
	;

	For i=0 To maxplayerobjects
		If playerobjects(i,0) = True Then
			;
			playerobjects(i,1) = playerobjects(i,1) + playerobjects(i,3)
			;
			x = playerobjects(i,1)
			y = playerobjects(i,2)
			;
			; If the poop is blocked then change the state
			changestate = False
			If playerobjects(i,6) = 0 Then
				;
				If playerobjects(i,3) < 0 Then
					If isblocked(x,y) = True Then changestate = True 
					Else
					If isblocked(x+16,y) = True Then changestate = True 
				End If
				;
				If playerobjectcollision(x+8,y) = True Then changestate = True
			End If
			;
			If changestate = True Then
				;
				If soundeffectsenabled = True Then
					poopsound = PlaySound(poopsplatsound)
				End If
				;
				poopanimdelay(i) = defaultsmashingpoopanimdelay
				poopanimtimer(i) = MilliSecs()
				playerobjects(i,5) = 143
				playerobjects(i,6) = 1
				playerobjects(i,3) = 0
			End If

			;
			; Animate the poop
			;active,x,y,speed,Type, frame, state
			If playerobjects(i,4) = 0 Then
				If playerobjects(i,6) = 0 Then
					If poopanimtimer(i)+poopanimdelay(i)<MilliSecs() Then
						;
						poopanimpointer(i) = poopanimpointer(i) + 1
						If poopanim(0,poopanimpointer(i)) = -1 Then poopanimpointer(i) = 0
						playerobjects(i,5) = poopanim(0,poopanimpointer(i))
						;
						poopanimtimer(i) = MilliSecs()
					End If
					Else
					If poopanimtimer(i)+poopanimdelay(i)<MilliSecs() Then
						;
						poopanimpointer(i) = poopanimpointer(i) + 1
						If poopanim(1,poopanimpointer(i)) = -1 Then playerobjects(i,0) = False
						playerobjects(i,5) = poopanim(1,poopanimpointer(i))
						;
						poopanimtimer(i) = MilliSecs()
					End If
				End If
			End If
;			If poopanimtimer(i) + poopanimdelay(i) < MilliSecs() Then
;				updatefragmentedpoop = True
;				poopanimtimer(i) = MilliSecs()
;			End If

	
			;
		End If
	Next
End Function
;
; function that reads the user input
;
Function readuserinput()
;
Local hasmoved = False

If playercurrentanim = playerthrowingright Then
	hasmoved = True
End If
;
;
movingblock = False ; Reset the flag for moving a block
;
;;
;; Player Fire
;;
If KeyHit(playerfire) = True Or KeyHit(playeraltfire) = True Then
	If (Not playeranimframe = 4) And (Not playeranimframe = 14) Then
		If playeranimdirection = 1 Then
			;createplayerobject(findplayerobject(),0,1,1)		
			delayedpoopright = True
			delayedpooptimer = MilliSecs()
			setplayeranimation(playerthrowingright)
		End If
		If playeranimdirection = 0 Then
			;createplayerobject(findplayerobject(),0,0,-1)
			delayedpoopleft = True
			delayedpooptimer = MilliSecs()
			setplayeranimation(playerthrowingleft)
		End If
	End If
	If playeranimframe = 4 Then
		;createplayerobject(findplayerobject(),0,1,1)
		delayedpoopright = True
		delayedpooptimer = MilliSecs()
		setplayeranimation(playerthrowingright)
	End If
	If playeranimframe = 14 Then
		;createplayerobject(findplayerobject(),0,0,-1)
		delayedpoopleft = True
		delayedpooptimer = MilliSecs()
		setplayeranimation(playerthrowingleft)
	End If
End If

;
; Jump the player
;
If playercanjump = True Then
	If playergravityactive = False Then
		If KeyDown(playerjump) = True Or KeyDown(playeraltjump) = True Then
			;
			; Play the sfx
			If soundeffectsenabled = True Then
				If isblocked(playerx,playery-16) = False And playerobjectcollision(playerx,playery-16) = False Then
					If ChannelPlaying(channeljump) = False Then channeljump = PlaySound(playerjumpsound)
				End If
			End If
			;
			; animate the jumping
			If (Not playercurrentanim = playerjumpingright) Or (Not playercurrentanim = playerjumpingleft) Then
				If playeranimdirection = 1 Then setplayeranimation(playerjumpingright)				
				If playeranimdirection = 0 Then setplayeranimation(playerjumpingleft)									
			End If
			;
			hasmoved = True
			;
			;jumpfromclimb = False
			;
			If jumpfromclimb = True Then
				inclimb = False
				jumpfromclimb = False
			;	jumpfromclimb = True
				playergravityactive = True
				playerfallingspeed = -2.6
			End If
			;
			If isblocked(playerx,playery+1) = True Or isclimb(playerx,playery+1) = True Then
				playergravityactive = True
				playerfallingspeed = - 2.6
			End If
			;
			If playerobjectcollision(playerx,playery) = False
				If playerobjectcollision(playerx,playery+2) = True
					playergravityactive = True
					playerfallingspeed = -2.6
				End If
			EndIf
		End If
	End If
End If

If KeyDown(playerright) = True Or KeyDown(playeraltright) = True Then
;If KeyDown(playerjump) = False And KeyDown(playeraltjump) = False Then
	;	
	hasmoved = True
	;
	; Animate the walk
	playeranimdirection = 1
	If (Not playercurrentanim = playerwalkingright) And  (Not playergravityactive = True) Then
		setplayeranimation(playerwalkingright)
	End If
	; If we are jumping and we turn around then update the anim
	If playergravityactive = True And (Not playercurrentanim = playerjumpingright) Then
		playercurrentanim  = playerjumpingright
	End If
	;
	;Global iceslidevalue = 0
	;Global iceslidedelay = -1 ; infinite slide
	;Global iceslidetimer = MilliSecs()

	; Here we activate the sliding on the ice
	;If gamemap(currentlevel,(playerx/16),(playery+1)/16,0) >= 80 And gamemap(currentlevel,(playerx)/16,(playery+1)/16,0) <= 88 Then
	n = returnslidetile(playerx,playery+1)
	If n>0 Then
      	iceslidevalue = n
		iceslideairtimer = iceslideairdelay
		Else
      	iceslidevalue = 0
	End If
	; If the player is jumping and lands he should move so the code below handles this
	If iceslidevalue = 0 And playergravityactive = True Then
		iceslidevalue = 1
		iceslideairtimer = iceslideairdelay
	End If

	; if the next position = blocked
	If isblocked2(playerx+1,playery) = False And playerobjectcollision(playerx+1,playery) = False Then
		If isblocked2(playerx+1,playery-1) = False Then
			If inclimb = False Then
				; Check for whole collision				
				
				If iscoll(playerx-8,playery,5) = False Then
					If playerx<320-9 Then playerx = playerx + 1
				
				End If
				;End If
			End If
		End If
	End If
	;
	; if we are climbing when we move right then disable the climbing
	;
	If inclimb = True Then
		If isblocked2(playerx+16,playery) = False Then
			inclimb=False
			jumpfromclimb = True
		End If
	End If
	;
	; If we encounter a movable stone then move it ; push a block
	If playergravityactive = False Then
		a = moveableblockinway(2,0)
		If a>-1 Then
			; is there not another block in the way
			If moveableblockisblocked(a,1,0) = False Then
				; if there is no baddy there
				If playerbaddycollision(playerx+16+8,playery) = False Then
					; if 2 positions away is not blocked
					If isblocked2(playerx+16+8,playery) = False Then					
						; If the block is on a surface
						If isblocked2(playerx+8,playery+1) = True Or playerobjectcollision(playerx+8,playery+1) Then						
							; If the player is on the ground
							If isblocked(playerx,playery+1) = True Or isclimb(playerx,playery) = True Or playerobjectcollision(playerx,playery+5) Then							
								objects(currentlevel,a,1) = objects(currentlevel,a,1) + 1							
								movingblock = True
							End If
						End If
					End If
				End If
			End If
		End If
	End If
	;
	; Pull the block
	If KeyDown(playerpullblock) = True Then
		If playerobjectcollision(playerx-5,playery) = True Then
			If playergravityactive = False Then
				a = moveableblockinway(-16,0)
				If a>-1 Then
					;
					stoneisaligned = False
					For y=0 To 15
						If objects(currentscreen,a,2) = y*16 Then stoneisaligned = True : Exit
					Next
					;
					If stoneisaligned = True Then
						If moveableblockisblocked(a,1,0) = False Then
							If RectsOverlap(playerx-8,playery-16,16,16,objects(currentlevel,a,1),objects(currentlevel,a,2),16,16) = False Then
								; if the player is on the ground								
								If  isblocked2(playerx,playery+1) = True Or isclimb(playerx,playery) = True  Or playerobjectcollision(playerx,playery+4) Then								
									objects(currentlevel,a,1) = objects(currentlevel,a,1) + 1
									movingblock = True
								End If								
							End If
						End If
					End If
				End If
			End If
		End If		
	End If
End If
;
If KeyDown(playerleft) = True Or KeyDown(playeraltleft) = True Then
	;
	hasmoved = True
	;
	; Animate the walk
	playeranimdirection = 0
	If (Not playercurrentanim = playerwalkingleft) And (Not playergravityactive = True) Then
		setplayeranimation(playerwalkingleft)
	End If
	; If we are jumping and we turn around then update the anim
	If playergravityactive = True And (Not playercurrentanim = playerjumpingleft) Then
		playercurrentanim  = playerjumpingleft
	End If
	;
	;See if we can move and if so do move
	;
	;
	If isblocked2(playerx-1,playery) = False And playerobjectcollision(playerx-1,playery) = False Then
		If isblocked2(playerx-1,playery-1) = False Then
				If inclimb = False Then
					; test collision test
					If iscoll(playerx+8,playery,3) = False Then
					If playerx > 9 Then playerx = playerx - 1
					End If
				End If
		End If
	End If
	;
	If inclimb = True Then
		If isblocked2(playerx-16,playery) = False Then
			inclimb=False
			jumpfromclimb = True
		End If
	End If
	;
	; Here we activate the sliding on the ice
	;If gamemap(currentlevel,(playerx/16),(playery+1)/16,0) >= 80 And gamemap(currentlevel,(playerx)/16,(playery+1)/16,0) <= 88 Then
	n = returnslidetile(playerx,playery+1)
    If n > 0 Then
      	iceslidevalue = -n
		iceslideairtimer = iceslideairdelay
		Else
      	iceslidevalue = 0
	End If
	; If the player is jumping and lands he should move so the code below handles this
	If iceslidevalue = 0 And playergravityactive = True Then
		iceslidevalue = -1
		iceslideairtimer = iceslideairdelay
	End If
	;
	; If we encounter a movable stone then move it
	If playergravityactive = False Then
		a = moveableblockinway(-2,0)
		If a>-1 Then
			If moveableblockisblocked(a,-1,0) = False Then
				If playerbaddycollision(playerx-8,playery) = False Then			
					If isblocked2((playerx-16)-8,playery) = False Then
						; Let the block drop straight down
						;If isblocked2(playerx-8,playery+1) = True Then
							If a<maxobjects Then objects(currentlevel,a,1) = objects(currentlevel,a,1) - 1
							movingblock=True
						;End If
					End If
				End If
			End If
		End If
	End If
	;
	; Pull the block
	If KeyDown(playerpullblock) = True Then
		If playergravityactive = False Then
		If playerobjectcollision(playerx+5,playery) = True Then
			a = moveableblockinway(+16,0)
			If a>-1 Then
				stoneisaligned = False
				For y=0 To 15
				    If objects(currentlevel,a,2) = y*16 Then stoneisaligned = True:Exit
				Next
				If stoneisaligned = True Then
					If moveableblockisblocked(a,-1,0) = False Then
						If RectsOverlap(playerx-8,playery-16,16,16,objects(currentlevel,a,1),objects(currentlevel,a,2),16,16) = False Then
							; if the player is on the ground
							If isblocked(playerx,playery+1) = True Or isclimb(playerx,playery) = True Or playerobjectcollision(playerx,playery+8) Then
								objects(currentlevel,a,1) = objects(currentlevel,a,1) - 1
								movingblock = True
							End If
						End If
					End If
				End If
			End If	
		End If
		End If
	End If 
	;
End If

If KeyDown(playerup) = True Or KeyDown(playeraltup) Then
	;
	hasmoved = True
	;
	alignment = False
	;If gamemap(currentmap,playerx/16,((playery-8)/16),1) = 44 Then
	If isclimb(playerx,playery-8) = True Then ;gamemap(currentmap,playerx/16,((playery-8)/16),1) = 44 Then
		For x=0 To 20
			For x1 = -3 To 3
				If (playerx-8)+x1 = (x*16) And alignment = False Then
					alignment = True
					playerx = (x*16)+8
				End If
			Next
		Next
	End If
	;
	; if we are at the top of a ladder exit the climb state
	;
	;If isclimb(playerx,playery) = False And Then
	If isblocked(playerx,playery-8) = True Or isclimb(playerx,playery) = False Then
		; Stop the climbing animation
		If (Not playercurrentanim = playerstandingright) And (Not playercurrentanim = playerstandingleft) Then
			If playeranimdirection = 0 Then
				setplayeranimation(playerstandingleft)
				Else
				setplayeranimation(playerstandingright)
			End If
		End If
		;
		;
		inclimb=False
		jumpfromclimb = False
		;
	End If

;	If inclimb = True Then
;		For y=0 To 15
;			For y1 = -2 To 2
;			If playery+y1 = y*16 Then
;				If KeyDown(playerright) = True Or KeyDown(playerleft) = True Or KeyDown(playeraltleft) = True Or KeyDown(playeraltright) = True Then
;				inclimb = False
;				exitladder = True
;				;End
;				;playery=playery-1
;				playery = y*16
;				End If
;			End If
;			Next
;		Next
;	End If
	;
	; If we are aligned to the ladder and have not moved to the left or right
	; -9
	If isclimb(playerx,playery-8) = True Then ;gamemap(currentmap,(playerx/16),((playery-8)/16),1) = 44 Then
		If alignment = True And exitladder = False Then
			inclimb = True
			;
			; Do the climbing
			;
			If (Not playercurrentanim = playerclimbingleft) And (Not playercurrentanim = playerclimbingright) Then
				If playerdirection = 0 Then
					setplayeranimation(playerclimbingleft)
					Else
					setplayeranimation(playerclimbingright)
				End If
			End If
			;
			playergravityactive = False
		End If
	End If
	;
	; test collision test
	;If iscoll(playerx,playery,1) = False Then
	;If playerobjectcollision(playerx,playery-1) = False Then
	If inclimb = True Then playery = playery - 1 : jumpfromclimb = True : iceslidevalue = 0
	;End If
	;End If
	;
	;
End If

If KeyDown(playerdown) = True Or KeyDown(playeraltdown) Then
	;
	hasmoved = True
	;
	; Align ourself with the climbable things
	;
	alignment = False
	If isclimb(playerx,playery+1) = True Then ;gamemap(currentmap,playerx/16,(playery)/16,1) = 44 Then
		For x=0 To 20
			For x1 = -3 To 3
				If (playerx-8)+x1 = (x*16) And alignment = False Then
					alignment = True
					playerx = (x*16)+8
				End If
			Next
		Next
	End If
	;
	;
;	If inclimb = True Then
;		For y=0 To 15
;			If playery = y*16 Then
;				If KeyDown(playerright) = True Or KeyDown(playerleft) = True Or KeyDown(playeraltright) = True Or KeyDown(playeraltleft) = True Then
;				 inclimb = False
;				 exitladder = True
;				 playery=playery-1
;				End If
;			End If
;		Next
;	End If
;
	; Check if we can climb down ahead
	If isclimb(playerx,playery+1) = True Then ;gamemap(currentmap,playerx/16,(playery)/16,1) = 44 Then		
		If alignment = True And exitladder = False Then
			inclimb=True			
			jumpfromclimb = True
		End If
	End If
	;
	; Check if we can climb down atposition
	If isclimb(playerx,playery) = True Then ;gamemap(currentmap,playerx/16,(playery)/16,1) = 44 Then
		If alignment = True Then			
			inclimb=True
			jumpfromclimb = True
		End If
	End If
	;
	;
	
	If playerobjectcollision(playerx,playery+1) = True Then
	 	inclimb = False
		
		jumpfromclimb = False
	End If
	
	If isblocked2(playerx,playery+1) = True Then	
		If isclimb(playerx,playery+1) = False Then;And (Not gamemap(currentmap,playerx/16,(playery/16)+1,1) = 44) Then		
			inclimb = False			
			jumpfromclimb = False
		End If
	End If
	
	;
	
	; Set the climbing animation
	;
	If inclimb = True Then
		If (Not playercurrentanim = playerclimbingleft) And (Not playercurrentanim = playerclimbingright) Then
			If playerdirection = 0 Then
				setplayeranimation(playerclimbingleft)
				Else
				setplayeranimation(playerclimbingright)
			End If
		End If
		Else
		If (Not playercurrentanim = playerstandingleft) And (Not playercurrentanim = playerstandingright) Then
			If playeranimdirection = 0 Then
				setplayeranimation(playerstandingleft)
				Else
				setplayeranimation(playerstandingright)
			End If
		End If
	End If
	;
	; Bugfix, get of the ladder even if a objects is close by
	;
	
	;
	; test collision test
	;If iscoll(playerx,playery-16,7) = False Then	
		If inclimb = True Then playery = playery + 1 : jumpfromclimb = True : iceslidevalue = 0
	;End If	
	;
	
End If

; Execute layer 1/0 commands
;
If playeronusableobject(playerx,playery) = True Then
	playerprocessusableobject(playerx,playery)
End If

; If we have not moved then stop any animation
;
If hasmoved = False Then
	; do not stop if we are in the air
	If Not playercurrentanim = playerthrowingleft Then
	If Not playercurrentanim = playerthrowingright Then
	If playergravityactive = False Then
		If playeranimdirection = 0 Then 
			setplayeranimation(playerstandingleft)
			Else
			setplayeranimation(playerstandingright)
		End If
	End If
	End If	
	End If
End If

;

End Function
;
; This function lets the player fall
;
Function gravity()
;
; Move the player outside any objects
;
;If playerobjectcollision(playerx,playery) = True Then
;adjust
;End If


; disable falling when we hit a object
;
If playergravityactive = True Then
	If playerfallingspeed# > 0 Then
		If playerobjectcollision(playerx,playery+1) = True Then 
			playergravityactive = False
			;
			; reset the animation to standing once we hit something
			If playeranimdirection = 0 Then
				setplayeranimation(playerstandingleft)
				Else
				setplayeranimation(playerstandingright)
			End If

			;
			While playerobjectcollision(playerx,playery+1) = True
				playery=playery-1
			Wend
		End If
	End If
End If

; If we are going up then reset the falling value if we hit a object
;
If playergravityactive = True Then
	If playerfallingspeed#<0 Then
		If playerobjectcollision(playerx,playery-2) = True Then
			playerfallingspeed# = 0			
			If isblocked(playerx,playery+1) = True Then
				If Not isblocked(playerx,playery-8) = True Then
					playergravityactive = False
					
				End If
			End If
		End If
	End If
End If

;
; If we are in the air then fall down
;
If playergravityactive = False Then
	If isblocked(playerx,playery+1) = False Then
	If isblocked(playerx-7,playery+1) = False Then
	If isblocked(playerx+7,playery+1) = False Then
	  If Not playerobjectcollision(playerx,playery+2) = True Then
		If isclimb(playerx,playery+1) = False ;Not gamemap(currentmap,playerx/16,(playery+1)/16,1) = 44 Then
			If inclimb = False Then
				;
				; Play the sound effect
				If soundeffectsenabled = True Then
					If isblocked(playerx,playery+32) = False And isblocked(playerx,playery+16) = False Then
						If playerobjectcollision(playerx,playery+16) = False And playerobjectcollision(playerx,playery+32) = False Then
							channelfall  = PlaySound(playerfallsound)
						End If
					End If
				End If
				;
				playergravityactive = True
				playerfallingspeed# = 0
			End If
		End If
		End If
	End If
	End If
	End If
End If

; do the gravity
;
;
If playergravityactive = True Then

	playerfallingspeed# = playerfallingspeed# + .10
	If playerfallingspeed<-8 Then playerfallingspeed = -8
	If playerfallingspeed>8 Then playerfallingspeed = 8

	Local startval# = playerfallingspeed#
	Local exitloop = False

	; Do the falling sound
	If playerfallingspeed# >-0.5 And playerfallingspeed#<0
		If isblocked(playerx-16,playery+16) = False And playerobjectcollision(playerx-16,playery+16) = False Then
			If isblocked(playerx+16,playery+16) = False And playerobjectcollision(playerx+16,playery+16) = False Then
				If isblocked(playerx,playery+35) = False And playerobjectcollision(playerx,playery+35) = False Then
					If isblocked(playerx,playery+35) = False And playerobjectcollision(playerx,playery+35) = False Then
						If soundeffectsenabled = True Then
							If isblocked(playerx,playery+32) = False And isblocked(playerx,playery+16) = False Then
								If ChannelPlaying(channelfall) = False Then channelfall  = PlaySound(playerfallsound)
							End If
						End If  
					End If
				End If
			End If
		End If
	End If
		
	While exitloop = False And playergravityactive = True

		wholevalue = False
		For i=-9 To 9
			If i-.10<startval# And i+.10>startval# Then wholevalue = True : Exit
		Next

		If playerfallingspeed#>0 Then playery=playery+0.10
		If playerfallingspeed#<0 Then playery = playery-0.10
		If startval# < 0 Then startval# = startval# + .10 
		If startval# > 0 Then startval# = startval# - .10
		If startval#>-.10 And startval#<.10 Then exitloop = True


		If wholevalue = True Then

			; If we are falling see when we reach the ground and then stop falling
			If playergravityactive = True Then			
				If isblocked(playerx,playery) = True And isblocked(playerx,playery-8) = False And playerobjectcollision(playerx,playery) = False Then ;+1
					;gravity hotspot					
					 If playerfallingspeed>0 Then playergravityactive = False; PlaySound playerjumpsound										
					;
					If playeranimdirection = 0 Then
						setplayeranimation(playerstandingleft)
						Else
						setplayeranimation(playerstandingright)
					End If
					; if we are stuck on the left side
;					If isclimb(playerx,playery) = False Then
						;While isblocked(playerx-1,playery-9) = True 
						;	playerx = playerx + 1
						;	If KeyDown(1) = True Then Exit
						;Wend
						; If we are stuck inside the right side
						;While isblocked(playerx+1,playery-9) = True
						;	playerx = playerx - 1
						;Wend
						; move ourself up from out the ground 
						While isblocked(playerx,playery) = True 
							playery = playery - 1
							If isblocked(playerx,playery-8) Then							
								Exit
							End If
							If KeyDown(1) = True Then Exit
						Wend
;					End If
					;
				End If
			End If

			;
			; If we jump our head against a wall then reset gravity to 0
			;
			If playergravityactive = True Then
				If isblocked(playerx,playery-8) = True Or playerobjectcollision(playerx,playery) = True Then
					If isblocked(playerx,playery) = False Then						
						playerfallingspeed# = 0						
					End If
				End If
			End If
			
			; If we land on a spring then jump higher
			;
			If playergravityactive = True Then
				If gamemap(currentlevel,(playerx-8)/16,(playery-12)/16,1) = isspring Then
					If playerfallingspeed#>0 Then
						;
						If soundeffectsenabled = True Then
							springsound = PlaySound(playerspringsound)
						End If
						;
						playerfallingspeed# = -4
					End If
				End If
			End If
			
			; If we are in a climb and the tile is not a climb tile then disable the climb mode
			If inclimb = True Then
				If isclimb(playerx,playery) = False;Not gamemap(currentmap,playerx/16,(playery-7)/16,1) = 44 Then
					inclimb = False
				End If
			End If
		End If
	
	Wend
		
End If

End Function
;
; This function scrolls the level thru the screen
;
;
Function scrolllevel(source,destination,direction)
oldcurrentlevel = currentlevel
x1 = 0
y1 = 0
x2 = 0
y2 = 0
Select direction
	Case 0 : x2 = 0 : y2=-240
	Case 1 : x2 = -320 : y2=0
	Case 2 : x2 = 320 : y2=0
	Case 3 : x2 = 0: y2=240
End Select
Local exitloop = False
While KeyDown(1) = False And exitloop = False
	Cls
	currentlevel = source
	drawmap(x1,y1)
	currentlevel = destination
	drawmap(x2,y2)
	For i=0 To 3
		Select direction
			Case 0:y1=y1+1 : y2=y2+1 : If y2>=0 Then exitloop = True
			Case 1:x1=x1+1 : x2=x2+1 : If x2>=0 Then exitloop = True
			Case 2:x1=x1-1 : x2=x2-1 : If x2<=0 Then exitloop = True
			Case 3:y1=y1-1 : y2=y2-1 : If y2<=0 Then exitloop = True
		End Select
		Next
	FlipCanvas can
Wend
currentlevel = oldcurrentlevel
End Function
;
; This function processes all the games objects
;
Function doobjects()
;
; Let the blocks drop if the hang in the air
;
For i=0 To maxobjects
	If objects(currentlevel,i,0) > 0 Then
		x = objects(currentlevel,i,1)
		y = objects(currentlevel,i,2)
		;
		If y = 240-16 Then
			processblockthrulevel(currentlevel+16,x)
			objects(currentlevel,i,0) = False
		End If
		;
		gothru = True
		;For x1=-4 To 4
		If isblocked(x,y+16) = True Then gothru = False
		;
		If isblocked(x+15,y) = True Then
		gothru = False
		Else If isblocked(x,y) = True Then
		gothru = False
		Else If isblocked(x+15,y+16) = True
		gothru = False
		End If
		;
		;Next
		;
		If moveableblockisblocked(i,0,1) = False And gothru = True
			If RectsOverlap(playerx-8,playery-16,16,16,x,y,16,16) = False Then
				objects(currentlevel,i,2) = objects(currentlevel,i,2) + 1
			End If
		End If
		;
	End If
Next
End Function

;
; Process the game objects
;
Function processgameobjects(x,y)
x = x / 16
y = y / 16
layer0 = gamemap(currentlevel,x,y,0)
layer1 = gamemap(currentlevel,x,y,1)
layer2 = gamemap(currentlevel,x,y,2)
;
If layer2 > 0 Then
	Select layer2
		Case isswitch ; remove all blocked switches
			For y1=0 To 15
				For x1=0 To 20
				If gamemap(currentlevel,x1,y1,2) = isblockedswitch Then
					gamemap(currentlevel,x1,y1,2) = 0
					If soundeffectsenabled = True Then channelswitchopen = PlaySound(playerdooropensoundsound)
				End If
				Next
			Next
	End Select	
End If
End Function

;
; Execute the objects features
;
Function playerprocessusableobject(x,y)

Local currenttile
x = x / 16
y = y / 16
currenttile = gamemap(currentlevel,x,y,1)
layer0tile = gamemap(currentlevel,x,y,0)
layer2tile = gamemap(currentlevel,x,y,2)
;
; Layer 1 features
;
Select currenttile
;	Case isbanana
;		gamemap(currentlevel,x,y,1) = 0
;	Case ispoop
;		gamemap(currentlevel,x,y,1) = 0
;	Case isboot1
;		gamemap(currentlevel,x,y,1) = 0
;	Case isboot2
;		gamemap(currentlevel,x,y,1) = 0
;	Case isboot3
;		gamemap(currentlevel,x,y,1) = 0
;	Case isboot4
;		gamemap(currentlevel,x,y,1) = 0
;	Case ishammer
;		gamemap(currentlevel,x,y,1) = 0
;	Case isheart
;		gamemap(currentlevel,x,y,1) = 0
;	Case iskey
;		gamemap(currentlevel,x,y,1) = 0
End Select
;
; Layer 0 features
;
Select layer0tile
	Case isblockedkey
		gamemap(currentlevel,x,y,0) = 0
End Select
;
; layer 2 features
;
Select layer2tile
	Case isbanana
		If soundeffectsenabled = True Then channelbanana = PlaySound(playergetbanannasound)
		gamemap(currentlevel,x,y,2) = 0
		If checkformorebananas(currentlevel) = False Then
			removeblockedbananas(currentlevel)
			If soundeffectsenabled = True Then channelbanana = PlaySound(playerdooropensoundsound)
		End If
	Case ispoop
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case isboot1
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case isboot2
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case isboot3
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case isboot4
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case ishammer
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case isheart
		If soundeffectsenabled = True Then channelget = PlaySound(playergethealthsound)
		gamemap(currentlevel,x,y,2) = 0
	Case iskey
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case isdiamond
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case isnugget
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case isgold
		If soundeffectsenabled = True Then channelget = PlaySound(playeritemgetsound)
		gamemap(currentlevel,x,y,2) = 0
	Case 201; move one level to the right
	If currentlevel + 1 =< 256 Then
		If playerx =>311 Then 
			playergravityactive = False
			inclimb = False
			scrolllevel(currentlevel,currentlevel+1,2)
			currentlevel = currentlevel + 1
			createlevelai()
			setplayerstartlocation(2)
		End If
	End If
	Case 202; move one level to the left
	If currentlevel - 1 => 0 Then
		If playerx =< 9 Then
			inclimb = False
			playergravityactive = False
			scrolllevel(currentlevel,currentlevel-1,1)
			currentlevel = currentlevel - 1
			createlevelai()
			setplayerstartlocation(1)
		End If
	End If
	Case 203 ;move one level down
	If currentlevel + 16 =< 256 Then
		If playery =>240-9 Then
    		playergravityactive = False
			scrolllevel(currentlevel,currentlevel+16,3)
			currentlevel = currentlevel + 16
			createlevelai()
			setplayerstartlocation(3)
		End If
	End If
	Case 204;move one level up
	If currentlevel - 16 => 0 Then

;		If playery=<9 Then 
			playergravityactive = False
			scrolllevel(currentlevel,currentlevel-16,0)
			currentlevel = currentlevel - 16
			createlevelai()
			setplayerstartlocation(0)
;		End If
	End If
End Select
End Function
;
; Returns if the player is on a object
;
Function playeronusableobject(x,y)
x = x / 16
y = y / 16
If gamemap(currentlevel,x,y,1) > 0 Then
	Return True
End If
If gamemap(currentlevel,x,y,0) <20 Or gamemap(currentlevel,x,y,0) > 20 Then
	Return True
End If
If gamemap(currentlevel,x,y,2) <240 Then Return True
End Function
;
; If the x,y location is inside a object
;
Function playerobjectcollision(x,y)
For i=0 To maxobjects
	If objects(currentlevel,i,0) > 0 Then
		If RectsOverlap(x-7,y-15,15,15,objects(currentlevel,i,1),objects(currentlevel,i,2),16,16) = True Then
			Return True ; if the player is inside a object then return true
		End If
	End If 
Next
End Function
;
; Returns true if blocks overlap
;
Function moveableblockisblocked(Obj,x1,y1)

x = objects(currentlevel,obj,1)
y = objects(currentlevel,obj,2)

For i=0 To maxobjects
	If Not i = obj
		If objects(currentlevel,i,0) > 0 Then
			;
			x2 = objects(currentlevel,i,1)
			y2 = objects(currentlevel,i,2)
			;
			If RectsOverlap(x+x1,y+y1,16,16,x2,y2,16,16) = True Then Return True
			;
		End If
	End If
Next

If x1>0 Then
	If isblocked(playerx+(16-9)+x1,playery+y1) = True Then Return True
End If
If x1<0 Then
	If isblocked(playerx-(16-9)+x1,playery+y1) = True Then Return True
End If

End Function
;
; This function returns the block movable by the player 1024 if the stone cannot move
;
Function moveableblockinway(x1,y1)

; cycle thru all objects
For i=0 To maxobjects
	; if it is a push block
	If objects(currentlevel,i,0) = ispushblock Then
		;
		x = objects(currentlevel,i,1)
		y = objects(currentlevel,i,2)
		;
;		notpossible = False
		stoneinway = False
;		If isblocked((x+16)+x1,y) = True Then notpossible = True
;		If isblocked((x-16)+x1,y) = True Then notpossible = True
		If RectsOverlap(x,y,15,15,(playerx-8)+x1,playery-16+y1,15,15) = True Then stoneinway = True

;		If playerx + 8 = x And playery = y Then stoneinway = True
;		If playerx - 8 = x And playery = y Then stoneinway = True
		;
		If stoneinway = True Then Return i
		;If notpossible = False And stoneinway = True Then Return i
		;
		;
		;If RectsOverlap(x,y,16,16,(playerx-8)+x1,playery-16,16,16) = True Then Return 1024
		;
		;
	End If
Next
;
;
Return -1
End Function
;
; This function returns if a tile is climable
;
Function isclimb(x,y)
x = x/16
y = y/16
If gamemap(currentlevel,x,y,1) = 44 Then Return True
If gamemap(currentlevel,x,y,1) = 45 Then Return True
If gamemap(currentlevel,x,y,1) = 46 Then Return True
If gamemap(currentlevel,x,y,0) = 24 Then Return True
If gamemap(currentlevel,x,y,0) = 25 Then Return True
If gamemap(currentlevel,x,y,0) = 37 Then Return True 
If gamemap(currentlevel,x,y,1) = 47 Then Return True
;If gamemap(currentmap,x,y,0) = 38 Then Return True
Return False
End Function
;
; This function returns the layer for the given tile
;
;
Function returnlayer(tile)
Select tile
	Case 0 : Return 0
	Case 1 : Return 0
	Case 2 : Return 0
	Case 3 : Return 0
	Case 4 : Return 0
	Case 5 : Return 0
	Case 6 : Return 0
	Case 7 : Return 0
	Case 8 : Return 0
	Case 9 : Return 0
	Case 10 : Return 0
	Case 11 : Return 0
	Case 12 : Return 0
	Case 13 : Return 0
	Case 14 : Return 0
	Case 15 : Return 0
	Case 16 : Return 0
	Case 17 : Return 0
	Case 18 : Return 0
	Case 19 : Return 0
	Case 20 : Return 0
	Case 21 : Return 0
	Case 22 : Return 0
	Case 23 : Return 0
	Case 24 : Return 0
	Case 25 : Return 0
	Case 26 : Return 0
	Case 27 : Return 0
	Case 28 : Return 0
	Case 29 : Return 0
	Case 30 : Return 0
	Case 31 : Return 0
	Case 32 : Return 0
	Case 33 : Return 0
	Case 34 : Return 0
	Case 35 : Return 0
	Case 36 : Return 0
	Case 37 : Return 0
	Case 38 : Return 0
	Case 39 : Return 0
	Case 40 : Return 1
	Case 41 : Return 1
	Case 42 : Return 1
	Case 43 : Return 1
	Case 44 : Return 1
	Case 45 : Return 1
	Case 46 : Return 1
	Case 47 : Return 1
	Case 48 : Return 1
	Case 49 : Return 1
	Case 50 : Return 0
	Case 51 : Return 0
	Case 52 : Return 0
	Case 53 : Return 0
	Case 54 : Return 0
	Case 55 : Return 0
	Case 56 : Return 2
	Case 57 : Return 2
	Case 58 : Return 2
	Case 59 : Return 2
	Case 60 : Return 2
	Case 61 : Return 2
	Case 62 : Return 2
	Case 63 : Return 2
	Case 64 : Return 2
	Case 65 : Return 2
	Case 66 : Return 2
	Case 67 : Return 2
	Case 68 : Return 2
	Case 69 : Return 2
	Case 70 : Return 2
	Case 71 : Return 2
	Case 72 : Return 2
	Case 73 : Return 2
	Case 74 : Return 2
	Case 75 : Return 2
	Case 76 : Return 2
	Case 77 : Return 2
	Case 78 : Return 2
	Case 79 : Return 2
	Case 80 : Return 0
	Case 81 : Return 0
	Case 82 : Return 0
	Case 83 : Return 0
	Case 84 : Return 0
	Case 85 : Return 0
	Case 86 : Return 0
	Case 87 : Return 0
	Case 88 : Return 0
	Case 89 : Return 0
	Case 90 : Return 0
	Case 91 : Return 0
	Case 92 : Return 0
	Case 93 : Return 0
	Case 94 : Return 0
	Case 95 : Return 0
	Case 96 : Return 0
	Case 97 : Return 0
	Case 98 : Return 0
	Case 99 : Return 0
	Case 100 : Return 0
	Case 101 : Return 0
	Case 102 : Return 0
	Case 103 : Return 0
	Case 104 : Return 0
	Case 105 : Return 0
	Case 106 : Return 0
	Case 107 : Return 0
	Case 108 : Return 0
	Case 109 : Return 1
	Case 110 : Return 1
	Case 111 : Return 0
	Case 112 : Return 0
	Case 113 : Return 0
	Case 114 : Return 0
	Case 115 : Return 0
	Case 116 : Return 0
	Case 117 : Return 0
	Case 118 : Return 0
	Case 119 : Return 0
	Case 120 : Return 0
	Case 121 : Return 0
	Case 122 : Return 0
	Case 123 : Return 0
	Case 124 : Return 0
	Case 125 : Return 0
	Case 126 : Return 0
	Case 127 : Return 0
	Case 128 : Return 0
	Case 129 : Return 0
	Case 130 : Return 0
	Case 131 : Return 0
	Case 132 : Return 0
	Case 133 : Return 0
	Case 134 : Return 0
	Case 135 : Return 0
	Case 136 : Return 0
	Case 137 : Return 0
	Case 138 : Return 0
	Case 139 : Return 0
	Case 140 : Return 0
	Case 141 : Return 0
	Case 142 : Return 0
	Case 143 : Return 0
	Case 144 : Return 0
	Case 145 : Return 0
	Case 146 : Return 0
	Case 147 : Return 0
	Case 148 : Return 0
	Case 149 : Return 0
	Case 150 : Return 0
	Case 151 : Return 0
	Case 152 : Return 0
	Case 153 : Return 0
	Case 154 : Return 0
	Case 155 : Return 0
	Case 156 : Return 0
	Case 157 : Return 0
	Case 158 : Return 0
	Case 159 : Return 0
	Case 160 : Return 1
	Case 161 : Return 1
	Case 162 : Return 1
	Case 163 : Return 1
	Case 164 : Return 1
	Case 165 : Return 1
	Case 166 : Return 1
	Case 167 : Return 1
	Case 168 : Return 1
	Case 169 : Return 1
	Case 170 : Return 1
	Case 171 : Return 1
	Case 172 : Return 1
	Case 173 : Return 1
	Case 174 : Return 1
	Case 175 : Return 1
	Case 176 : Return 1
	Case 177 : Return 1
	Case 178 : Return 1
	Case 179 : Return 1
	Case 180 : Return 1
	Case 181 : Return 1
	Case 182 : Return 1
	Case 183 : Return 1
	Case 184 : Return 1
	Case 185 : Return 1
	Case 186 : Return 1
	Case 187 : Return 1
	Case 188 : Return 1
	Case 189 : Return 1
	Case 190 : Return 1
	Case 191 : Return 1
	Case 192 : Return 1
	Case 193 : Return 1
	Case 194 : Return 1
	Case 195 : Return 0
	Case 196 : Return 0
	Case 197 : Return 0
	Case 198 : Return 0
	Case 199 : Return 0
	Case 200 : Return 2
	Case 201 : Return 2
	Case 202 : Return 2
	Case 203 : Return 2
	Case 204 : Return 2
	Case 205 : Return 2
	Case 206 : Return 2
	Case 207 : Return 2
	Case 208 : Return 2
	Case 209 : Return 2
	Case 210 : Return 2
	Case 211 : Return 2
	Case 212 : Return 2
	Case 213 : Return 2
	Case 214 : Return 2
	Case 215 : Return 2
	Case 216 : Return 2
	Case 217 : Return 2
	Case 218 : Return 2
	Case 219 : Return 2
	Case 220 : Return 2
	Case 221 : Return 2
	Case 222 : Return 2
	Case 223 : Return 2
	Case 224 : Return 2
	Case 225 : Return 2
	Case 226 : Return 2
	Case 227 : Return 2
	Case 228 : Return 2
	Case 229 : Return 2
	Case 230 : Return 2
	Case 231 : Return 2
	Case 232 : Return 2
	Case 233 : Return 2
	Case 234 : Return 2
	Case 235 : Return 2
	Case 236 : Return 2
	Case 237 : Return 2
	Case 238 : Return 2
	Case 239 : Return 2
	Case 240 : Return 2
End Select
End Function

;
; This function will check for a collision with a tile
;
Function iscoll(x,y,check)
x2 = x / 16 * 16
y2 = y / 16 * 16
counter= 0
For y1 = -1 To 1
For x1 = -1 To 1
If counter = check Then
Rect (x2+8)+x1*16,(y2+8)+y1*16,3,3
If isblocked((x2+8)+x1*16,(y2+8)+y1*16) = True Then Return True
End If
counter = counter  +1
Next
Next
Return False
End Function

;
; This function returns true if the position on the x and y position is 
; blocked - layer 0
;
Function isblocked(x,y)
If x<0 Then Return True
If y<0 Then Return True
If x>320 Then Return True
If y>240 Then Return True
frame = gamemap(currentlevel,x/16,y/16,0)
Select frame
	Case 0 : isitblocked=False
	Case 1 : isitblocked=True
	Case 2 : isitblocked=True
	Case 3 : isitblocked=True
	Case 4 : isitblocked=True
	Case 5 : isitblocked=True
	Case 6 : isitblocked=True
	Case 7 : isitblocked=True
	Case 8 : isitblocked=True
	Case 9 : isitblocked=True
	Case 10: isitblocked=True
	Case 11: isitblocked=True
	Case 12: isitblocked=True
	Case 13: isitblocked=True
	Case 14: isitblocked=True
	Case 15: isitblocked=True
	Case 16: isitblocked=True
	Case 17: isitblocked=True
	Case 18: isitblocked=True
	Case 19: isitblocked=True
	Case 20 : isitblocked = False
	Case 21 : isitblocked = False
	Case 22 : isitblocked = False
	Case 23 : isitblocked = False
	Case 24 : isitblocked = False
	Case 25 : isitblocked = False
	Case 26 : isitblocked = False
	Case 27 : isitblocked = False
	Case 28 : isitblocked = False
	Case 29 : isitblocked = False
	Case 30 : isitblocked = False
	Case 31 : isitblocked = False
	Case 32 : isitblocked = False
	Case 33 : isitblocked = False
	Case 34 : isitblocked = False
	Case 35 : isitblocked = False
	Case 36 : isitblocked = False
	Case 37 : isitblocked = False
	Case 38 : isitblocked = False
	Case 39 : isitblocked = False
	Case 40 : isitblocked = False
	Case 41 : isitblocked = False
	Case 42 : isitblocked = False
	Case 43 : isitblocked = False
	Case 44 : isitblocked = False
	Case 45 : isitblocked = False
	Case 46 : isitblocked = False
	Case 47 : isitblocked = False
	Case 48 : isitblocked = False
	Case 49 : isitblocked = False
	Case 50 : isitblocked = False 
	Case 51 : isitblocked = True
	Case 52 : isitblocked = False
	Case 53 : isitblocked = True
	Case 54 : isitblocked = True
	Case 55 : isitblocked = True
	Case 56 : isitblocked = True
	Case 57 : isitblocked = True
	Case 58 : isitblocked = True
	Case 59 : isitblocked = True
	Case 60 : isitblocked = False
	Case 61 : isitblocked = False
	Case 62 : isitblocked = False
	Case 63 : isitblocked = False
	Case 64 : isitblocked = False
	Case 65 : isitblocked = False
	Case 66 : isitblocked = False
	Case 67 : isitblocked = False
	Case 68 : isitblocked = False
	Case 69 : isitblocked = False
	Case 70 : isitblocked = False
	Case 71 : isitblocked = False
	Case 72 : isitblocked = False
	Case 73 : isitblocked = False
	Case 74 : isitblocked = False
	Case 75 : isitblocked = False
	Case 76 : isitblocked = False
	Case 77 : isitblocked = False
	Case 78 : isitblocked = False
	Case 79 : isitblocked = False
	Case 80 : isitblocked = True
	Case 81 : isitblocked = True
	Case 82 : isitblocked = True
	Case 83 : isitblocked = True
	Case 84 : isitblocked = True
	Case 85 : isitblocked = True
	Case 86 : isitblocked = True
	Case 87 : isitblocked = True
	Case 88 : isitblocked = True
	Case 89 : isitblocked = True
	Case 90 : isitblocked = True
	Case 91 : isitblocked = True
	Case 92 : isitblocked = True
	Case 93 : isitblocked = True
	Case 94 : isitblocked = False
	Case 95 : isitblocked = False
	Case 96 : isitblocked = False
	Case 97 : isitblocked = True
	Case 98 : isitblocked = True
	Case 99 : isitblocked = True
	Case 100 : isitblocked = True
	Case 101 : isitblocked = True
	Case 102 : isitblocked = True
	Case 103 : isitblocked = True
	Case 104 : isitblocked = True
	Case 105 : isitblocked = True
	Case 106 : isitblocked = True
	Case 107 : isitblocked = True
	Case 108 : isitblocked = True
	Case 109 : isitblocked = True
	Case 110 : isitblocked = True
	Case 111 : isitblocked = True
	Case 112 : isitblocked = True
	Case 113 : isitblocked = True
	Case 114 : isitblocked = True
	Case 115 : isitblocked = True
	Case 116 : isitblocked = True
	Case 117 : isitblocked = True
	Case 118 : isitblocked = True
	Case 119 : isitblocked = True
	Case 120 : isitblocked = True
	Case 121 : isitblocked = True
	Case 122 : isitblocked = True
	Case 123 : isitblocked = True
	Case 124 : isitblocked = True
	Case 125 : isitblocked = True
	Case 126 : isitblocked = True
	Case 127 : isitblocked = True
	Case 128 : isitblocked = True
	Case 129 : isitblocked = True
	Case 130 : isitblocked = True
	Case 131 : isitblocked = True
	Case 132 : isitblocked = True
	Case 133 : isitblocked = True
	Case 134 : isitblocked = True
	Case 135 : isitblocked = True
	Case 136 : isitblocked = True
	Case 137 : isitblocked = True
	Case 138 : isitblocked = True
	Case 139 : isitblocked = True
	Case 140 : isitblocked = True
	Case 141 : isitblocked = True
	Case 142 : isitblocked = True
	Case 143 : isitblocked = True
	Case 144 : isitblocked = True
	Case 145 : isitblocked = True
	Case 146 : isitblocked = True
	Case 147 : isitblocked = True
	Case 148 : isitblocked = True
	Case 149 : isitblocked = True
	Case 150 : isitblocked = True
	Case 151 : isitblocked = True
	Case 152 : isitblocked = True
	Case 153 : isitblocked = True
	Case 154 : isitblocked = True
	Case 155 : isitblocked = True
	Case 156 : isitblocked = True
	Case 157 : isitblocked = True
	Case 158 : isitblocked = True
	Case 159 : isitblocked = True
	Case 160 : isitblocked = False
	Case 161 : isitblocked = False
	Case 162 : isitblocked = False
	Case 163 : isitblocked = False
	Case 164 : isitblocked = False
	Case 165 : isitblocked = False
	Case 166 : isitblocked = False
	Case 167 : isitblocked = False
	Case 168 : isitblocked = False
	Case 169 : isitblocked = False
	Case 170 : isitblocked = False
	Case 171 : isitblocked = False
	Case 172 : isitblocked = False
	Case 173 : isitblocked = False
	Case 174 : isitblocked = False
	Case 175 : isitblocked = False
	Case 176 : isitblocked = False
	Case 177 : isitblocked = False
	Case 178 : isitblocked = False
	Case 179 : isitblocked = False
	Case 180 : isitblocked = True
	Case 181 : isitblocked = True
	Case 182 : isitblocked = True
	Case 183 : isitblocked = True
	Case 184 : isitblocked = True
	Case 185 : isitblocked = True
	Case 186 : isitblocked = True
	Case 187 : isitblocked = True
	Case 188 : isitblocked = True
	Case 189 : isitblocked = True
	Case 190 : isitblocked = True
	Case 191 : isitblocked = True
	Case 192 : isitblocked = True
	Case 193 : isitblocked = True
	Case 194 : isitblocked = True
	Case 195 : isitblocked = False
	Case 196 : isitblocked = False
	Case 197 : isitblocked = False
	Case 198 : isitblocked = False
	Case 199 : isitblocked = False
	Case 200 : isitblocked = True
	Case 201 : isitblocked = True
	Case 202 : isitblocked = True
	Case 203 : isitblocked = True
	Case 204 : isitblocked = True
	Case 205 : isitblocked = True
	Case 206 : isitblocked = True
	Case 207 : isitblocked = True
	Case 208 : isitblocked = True
	Case 209 : isitblocked = True
	Case 210 : isitblocked = True
	Case 211 : isitblocked = True
	Case 212 : isitblocked = True
	Case 213 : isitblocked = True
	Case 214 : isitblocked = True
	Case 215 : isitblocked = True
	Case 216 : isitblocked = True
	Case 217 : isitblocked = True
	Case 218 : isitblocked = True
	Case 219 : isitblocked = True
	Case 220 : isitblocked = True
	Case 221 : isitblocked = False
	Case 222 : isitblocked = False
	Case 223 : isitblocked = False
	Case 224 : isitblocked = False
	Case 225 : isitblocked = False
	Case 226 : isitblocked = False
	Case 227 : isitblocked = False
	Case 228 : isitblocked = False
	Case 229 : isitblocked = False
	Case 230 : isitblocked = False
	Case 231 : isitblocked = False
	Case 232 : isitblocked = False
	Case 233 : isitblocked = False
	Case 234 : isitblocked = False
	Case 235 : isitblocked = False
	Case 236 : isitblocked = False
	Case 237 : isitblocked = False
	Case 238 : isitblocked = False
	Case 239 : isitblocked = False
	Case 240 : isitblocked = False
End Select

;
;
Return isitblocked
End Function
; Function to draw the sprites
; 
Function drawsprites()
	;
	; If we have the mario setting active
	If marioactive = True Then addition = 120 Else addition = 0
	; Draw the player
	;setcol(1,0)
	;Rect playerx-8,playery-15,16,16,1
	DrawImage gamesprites,playerx-8,playery-15,playeranimframe + addition
	;
	;Draw the players poop sprites
	For i=0 To maxplayerobjects
		If playerobjects(i,0) = True Then
			DrawImage gamesprites,playerobjects(i,1),playerobjects(i,2)-15,playerobjects(i,5)
		End If
	Next
	; 
End Function
; Function to draw the map
;
Function drawmap(x1,y1)
For x=0 To 20
	For y=0 To 15
		For i=0 To 2
			frame = gamemap(currentlevel,x,y,i)
			If frame<200 And frame>0 Then
				DrawImage gametiles,(x*16)+x1,(y*16)+y1,frame
			End If
			If showdebuggrid = True Then
				setcol(1,0)
				Rect x*16,y*16,17,17,0
				If i = 1 Then
				Text x*16+8,y*16+8,frame,1,1
				End If
			End If
		Next
	Next
Next
; Draw the objects
For i=0 To 20
	If objects(currentlevel,i,0)>0 Then
		DrawImage gametiles,(objects(currentlevel,i,1)+x1),(objects(currentlevel,i,2)+y1),objects(currentlevel,i,0)
	End If
Next
End Function
; temporary function
;
Function temploadlevels()
Local filetoopen$ = "leveldata.map"
;
Local mapobject = 0
;
filein = ReadFile(filetoopen$)
For i=0 To 256
	For ii=0 To 2
		For x=0 To 20
			For y=0 To 15
				a = ReadInt(filein)
				; ; if we find a start location then place it in a lookup array
				If a = 200 Then
					levelhasplayerstart(i) = True
				End If
				; game tiles
				;
				; If a pushblock is found
				If a = ispushblock Then
					; move the object into the object array
					objects(i,mapobject,0) = a
					objects(i,mapobject,1) = x * 16
					objects(i,mapobject,2) = y * 16				
					; increase the counter
					mapobject = mapobject + 1
					; erase the block for we have it stored elsewhere (objects array)
					a = 0
				End If
				;
				;
				; If a start position is found
				If a=200 Then gamemap(i,x,y,ii) = 200
				;
				; Level cls codes
				If a = 205 Then levelclscolor(i) = levelcls_color1
				If a = 206 Then levelclscolor(i) = levelcls_color2
				If a = 207 Then levelclscolor(i) = levelcls_color3
				If a = 208 Then levelclscolor(i) = levelcls_color4
				;
				If a<240 Then
					gamemap(i,x,y,ii) = a
				End If
			Next
		Next
		; reset the mapobject
		mapobject = 0
	Next
Next
CloseFile(filein)
End Function
;
; Sets the player to the start location of the default start location
; 0 - up - 1 - left - 2 - right - 3 - down - 4 warp to level start position
Function setplayerstartlocation(state)
	Select state
		Case 0 ; up
		playery = playery + 16*12
		Case 1 ; left
		playerx = playerx + 16*18
		Case 2 ; right
		playerx = playerx - 16*18
		Case 3 ; down
		playery = playery - 16*12
		Case 4 ; warp
		For x=0 To 20
			For y=0 To 15
				If gamemap(currentlevel,x,y,2) = 200 Then
					playerx	= (x*16)+8
					playery = (y*16)+15
				End If
			Next
		Next
	End Select
End Function
;
; this function lets you select a map (only for testing purposes)
;
Function selectmap()
Local oldlevel = currentlevel
Local blockwidth = 18
Local blockheight = 14
Local exitloop = False
;
While KeyDown(1) = False And exitloop = False
	Cls
	;
	counter = 0
	For y=0 To 15
		For x=0 To 15
			;
			; the grid
			setcol(1,0)
			Rect x*blockwidth,y*blockheight,blockwidth-1,blockheight-1,0
			;
			; the levels with start locations
			If levelhasplayerstart(counter) = True Then
				;
				setcol(2,0)
				Rect x*blockwidth+4,y*blockheight+4,blockwidth-8,blockheight-8,1
				;
			End If
			;
			; highlight the current level
			If currentlevel = counter Then
				;
				setcol(3,0)
				Rect x*blockwidth,y*blockheight,blockwidth,blockheight,1
				;
			End If
			;
			; Draw the map number
			setcol(5,0)
			Text x*blockwidth+blockwidth/2,y*blockheight+blockheight/2,counter,1,1
			;
			;
			; Move thru the map
			;
			If KeyHit(playerup) = True Then
				If currentlevel > 16 Then currentlevel = currentlevel - 16
				Delay(100)
			End If
			If KeyHit(playerdown) = True Then
				If currentlevel < 255-16 Then currentlevel = currentlevel + 16
				Delay(100)
			End If
			If KeyHit(playerleft) = True Then
				If currentlevel > 0 Then currentlevel = currentlevel - 1
				Delay(100)
			End If
			If KeyHit(playerright) = True Then
				If currentlevel < 255 Then currentlevel = currentlevel + 1
				Delay(100)
			End If
			;
			; Select the new level
			;
			If KeyDown(playerfire) = True Or KeyDown(playeraltfire) Then
				exitloop = True
			End If
			;
			counter = counter + 1
			;
		Next
	Next
	;
	Flip
Wend
If currentlevel<>oldlevel Then setplayerstartlocation(4) : createlevelai()
Delay(200)
FlushKeys():FlushMouse()
End Function
;
; Sets the Clscolor to the limited palette
;
Function SetClsCol(PCol,Mode=0)
If Mode=0
	ClsColor Pal(PCol,1),Pal(PCol,2),Pal(PCol,3)
Else
	Return Pal(PCol,0)
EndIf
Return False
End Function
;
; Sets the color to the limited palette
;
Function SetCol(PCol,Mode=0)
If Mode=0
	Color Pal(PCol,1),Pal(PCol,2),Pal(PCol,3)
Else
	Return Pal(PCol,0)
EndIf
Return False
End Function
;
; Create the palette ( needs setup before u can use setcol )
;
Function SetPalette()
Restore Palette
For p=0 To 15
	For col=1 To 3
		Read Pal(p,col)
	Next
	Pal(p,0)=(Pal(p,1) Shl 16)+(Pal(p,2) Shl 8)+Pal(p,3)
Next
End Function
;Adjusted-Ansi:
.Palette
Data 0,0,0,63,63,63,127,127,127,255,255,255
Data 255,0,0,127,0,0,0,255,0,0,127,0,0,0,255
Data 0,0,127,0,255,255,0,127,127,96,0,191
Data 255,127,0,255,255,0,127,63,0
;
; This function sets the player ai for the current level
;
Function createlevelai()
	;
	; first free all baddies from memory
	freebaddies()
	;
	; Read the ai
	counter = 0
	For x=0 To 20
		For y=0 To 15
			If gamemap(currentlevel,x,y,2) = 220 Then
				createbaddy(returnfreebaddy(),(x*16)+8,(y*16)+15,0)
				counter = counter + 1
			End If
		Next
	Next
End Function
;
; This function handles the states/ai for the baddies
;
Function dobaddies()
	Local x = 0
	Local y = 0
	Local cangoup
	Local cangodown
	Local goup = 0
	Local godown = 1
	Local selecteddirection = 0
	For i=0 To maxbaddies
		If baddies(i,baddies_active) = True Then
			x = baddieslocation(i,0)
			y = baddieslocation(i,1)
			state = baddies(i,baddies_state)
			laststate = baddies(i,baddies_laststate)
			;
			
			;
			; decide our path
			cangoup=False
			cangodown=False
			If (Not state = 27) And (Not state=28) Then
			If isclimb(x,y) = True Or isclimb(x,y+1) = True Then
				For x1=0 To 20
					If x1*16 = x+8 And Rand(0,2) = 0 Then
						;
						If isclimb(x,y-16) = True Then
							cangoup = True
						End If
						;
						If isclimb(x,y+16) = True Then
							cangodown = True
						End If
						;
						selecteddirection = goup
						If cangoup = True Then selecteddirection = goup
						If cangodown = True Then selecteddirection = godown
						If cangoup = True And cangodown = True Then selecteddirection = Rand(0,1)
						;
						
						If selecteddirection = goup And (Not laststate = 28) Then baddies(i,baddies_state) = 27 : baddies(i,baddies_laststate) = 27
						If selecteddirection = godown And (Not laststate = 27) Then baddies(i,baddies_state) = 28 : baddies(i,baddies_laststate) = 28
						;
						Exit
					End If
				Next
			End If
			End If
			;
			;
			; Here we see if we need to switch the direction we are walking in and if we see the enemy nearby
			Select baddies(i,baddies_state)			
				Case baddies_walkingright
					; If the position to the right is blocked then turn around
					If isblocked(x+8,y) = True Then baddies(i,baddies_state) = baddies_walkingleft : baddies(i,baddies_laststate) = baddies_walkingleft
					; If the position is the maximum right of the screen
					If x > 320-8 Then baddies(i,baddies_state) = baddies_walkingleft : baddies(i,baddies_laststate) = baddies_walkingleft
					; If the position to the right is a object then turn around
					If playerobjectcollision(x,y) = True Then baddies(i,baddies_state) = baddies_walkingleft: baddies(i,baddies_laststate) = baddies_walkingleft
					; If the position to the right is a cliff then turn around
					If isblocked(x+8,y+1) = False Then baddies(i,baddies_state) = baddies_walkingleft: baddies(i,baddies_laststate) = baddies_walkingleft
					;
					; If we see the enemy then switch to throwing state
					;For x1=0 To 3 
					;	If playerobjectcollision(x+(x1*16),y-1) = True Then Exit
					;	If isblocked(x+(x1*16),y-1) = True Then Exit
					;	If RectsOverlap(playerx-8,playery-15,16,16,(x+x1*16)-8,y-15,16,16) = True Then baddies(i,baddies_state) = 25
					If RectsOverlap(x-8,y-10,64,6,playerx-8,playery-15,16,16) Then 
						baddies(i,baddies_state) = 25	
						baddies(i,baddies_laststate) = 25
					End If
					
					;Next
					;
			
					
					;
				Case baddies_walkingleft
					If isblocked(x-8,y) = True Then baddies(i,baddies_state) = baddies_walkingright: baddies(i,baddies_laststate) = baddies_walkingright
					If x < 0+8 Then baddies(i,baddies_state) = baddies_walkingright
					If playerobjectcollision(x,y) = True Then baddies(i,baddies_state) = baddies_walkingright: baddies(i,baddies_laststate) = baddies_walkingright
					If isblocked(x-8,y+1) = False Then baddies(i,baddies_state) = baddies_walkingright: baddies(i,baddies_laststate) = baddies_walkingright
					;
					; If we see the enemy then switch to throwing state
					;For x1=0 To -3 Step -1 
					;	If playerobjectcollision(x+(x1*16),y-1) = True Then Exit
					;	If isblocked(x+(x1*16),y-1) = True Then Exit
					;	If RectsOverlap(playerx-8,playery-15,16,16,(x+x1*16)-8,y-15,16,16) = True Then baddies(i,baddies_state) = 26
					;Next
					If RectsOverlap(x-64,y-10,64,12,playerx-8,playery-15,16,16) = True Then
						baddies(i,baddies_state) = 26
						baddies(i,baddies_laststate) = 26
					End If
					;
				Case baddies_shoot
					; not implemented
				Case 25 ; shoot right
					; see if the player is still in sight if not continue walking
					stillinsight = False
					If RectsOverlap(x-8,y-10,64,6,playerx-8,playery-15,16,16) Then 
						stillinsight = True
					End If
					If stillinsight = False Then
						baddies(i,baddies_state) = baddies_walkingright
						baddies(i,baddies_laststate) = baddies_Walkingright
					End If
					;baddies(i,baddies_state) = baddies_walkingleft
				Case 26 ; shoot left
					; see if the player is still in sight if not continue walking
					stillinsight = False
					For x1=0 To -3 Step -1 
						If playerobjectcollision(x+(x1*16),y-1) = True Then Exit
						If isblocked(x+(x1*16),y-1) = True Then Exit
						If RectsOverlap(playerx-8,playery-15,16,16,(x+x1*16)-8,y-15,16,16) = True Then stillinsight = True
					Next
					If stillinsight = False Then
						baddies(i,baddies_state) = baddies_walkingleft
						baddies(i,baddies_laststate) = baddies_walkingleft
					End If
					;baddies(i,baddies_state) = baddies_walkingleft
				Case 27 ; climbup
					; Check if we can go off the ladder
					For y1=0 To 15
						If y1*16 = y+1 Then
							If Rand(0,1) = 0 And isblocked(x+5,y-15) = False And isblocked(x+5,y+1) = True Then baddies(i,baddies_state) = baddies_walkingright
							If Rand(0,1) = 0 And isblocked(x-5,y-15) = False And isblocked(x-5,y+1) = True Then baddies(i,baddies_state) = baddies_walkingleft
						End If
					Next
					;
					
					;
					; Check if we can keep climbing ; if not then wait until we can climb down
					If isblocked2(x,y-16) = False And playerobjectcollision(x,y-16) = False And isclimb(x,y) = True Then
					baddieslocation(i,1) = baddieslocation(i,1) - .25
						Else ; if the baddie cannot go any further
						;If isblocked(x+5,y) = False Then baddies(i,baddies_state) = baddies_walkingright
						;If isblocked(x-5,y) = False Then baddies(i,baddies_state) = baddies_walkingleft
					    If baddies(i,baddies_laststate) = 28 Then
				    		If Rnd(1) = 0 Then baddies(i,baddies_state) = baddies_walkingright
				    		If Rnd(1) = 0 Then baddies(i,baddies_state) = baddies_walkingleft
						End If
			
						;baddies(i,baddies_state) = 28
;;						baddies(i,baddies_state) = 28
					End If
				Case 28 ; climbdown
					; check if we can go off the ladder
					cangoright = False
					cangoleft = False
					For y1=0 To 15
						If y1*16 = y+1 Then
							If isblocked(x+5,y-15) = False And isblocked(x+5,y+1) = True Then cangoright = True
							If isblocked(x-5,y-15) = False And isblocked(x-5,y+1) = True Then cangoleft = True
						End If
					Next
					
					If Rand(2) = 0 Then
						newdirection = -1
						If cangoright = True And cangoleft = True Then
							newdirection = Rand(0,1)
						End If
						If newdirection = -1 Then
							If cangoright = True Then newdirection = 0
							If cangoleft = True Then newdirection = 1
						End If
						If newdirection = 0 Then baddies(i,baddies_state) = baddies_walkingright 
						If newdirection = 1 Then baddies(i,baddies_state) = baddies_walkingleft
					End If					

					; check if we can keep on climbing down and if not then reassign ourselfs
					If isblocked2(x,y+1) = False And playerobjectcollision(x,y+1) = False And isclimb(x,y+1) = True Then
						baddieslocation(i,1) = baddieslocation(i,1) + .25
						Else
						If cangoright = True And Rand(0,1) = 0 Then baddies(i,baddies_state) = baddies_walkingright
						If cangoleft = True And Rand(0,1) = 0 Then baddies(i,baddies_state) = baddies_walkingleft
						;If isblocked(x+5,y) = False Then baddies(i,baddies_state) = baddies_walkingright
						;If isblocked(x-5,y) = False Then baddies(i,baddies_state) = baddies_walkingleft
						
					;baddies(i,baddies_state) = 27
					End If
				;
			End Select
			;
			; Here we add to the baddies movement
			If baddies(i,baddies_state) = baddies_walkingright Then
				baddieslocation(i,0) = baddieslocation(i,0) + baddiesmovementspeed#(i)
				
			End If
			If baddies(i,baddies_state) = baddies_walkingleft Then 
				baddieslocation(i,0) = baddieslocation(i,0) - baddiesmovementspeed#(i)
			End If
			;
		End If
	Next
End Function
;
; This function will return if a block is passable if climable
; and if not climable then return true if it is blocked
;
Function isblocked2(x,y)
If isclimb(x,y) = True Then Return False
If isblocked(x,y) = True Then Return True
End Function 
;
; This function draws the baddies
;
Function drawbaddies(x,y)
	; Draw the baddies
	For i=0 To maxbaddies
		If baddies(i,0) = True Then
			;
			Rect (baddieslocation(i,0)-8)+x,(baddieslocation(i,1)-15)+y,16,16,1 ; add sprite drawing here
			;
		End If
	Next
	; Draw the baddies stuff (bullets and sticks and stuff)
	For i=0 To maxbaddies*maxbaddiesstuff
		If baddiesstuff(i,bstuff_active) = True Then
			;
			Rect baddiesstufflocation(i,0)+x,baddiesstufflocation(i,1)+y,4,4,1 ; add sprite drawing here
			;
		End If
	Next
End Function
;
; This function creates a baddy in the array
;
;
Function createbaddy(slot,x,y,baddytype)
	Select baddytype
		Case 0 ; first baddy - dumb walking one
			baddies(slot,baddies_active) = True
			baddies(slot,baddies_x) = x
			baddies(slot,baddies_y) = y
			baddies(slot,baddies_type) = baddytype
			baddies(slot,baddies_state) = baddies_walkingright
			baddies(slot,baddies_speedx) = baddies1_speedx
			baddies(slot,baddies_speedy) = baddies1_speedy
			baddies(slot,baddies_direction) = 1 ; moving right
			baddies(slot,baddies_frame) = 0 ; not implemented yet
			baddiesmovementspeed#(slot) = .33
			baddieslocation#(slot,0) = x
			baddieslocation#(slot,1) = y
	End Select
End Function
;
; This function clears the ai arrays for the current level
;
Function freebaddies()
	;
	For i=0 To maxbaddies
		baddies(i,0) = False
	Next
	;
	For i=0 To maxbaddies*maxbaddiesstuff
		baddiesstuff(i,0) = False
	Next
	;
End Function
;
; This function returns a free ai slot
;
Function returnfreebaddy()
	;
	For i=0 To maxbaddies
		If baddies(i,baddies_active) = False Then Return i
	Next
	;
	RuntimeError "To much baddies in level - Change const maxbaddies or use less baddies per level" 
	;
End Function
;
; This function returns wether a x,y location is inside a baddie
;
Function playerbaddycollision(x,y)
For i=0 To maxbaddies
	If baddies(i,baddies_active) = True Then
		If RectsOverlap(x-8,y-15,16,16,baddieslocation(i,0),baddieslocation(i,1),16,16) = True Then
			Return True
		End If
	End If
Next
End Function
;
; Does like it says except draw it
;
Function showfps()
	fps2 = fps2 + 1
	If fpstimer + fpsdelay < MilliSecs() Then
		fps = fps2
		fps2 = 0
		fpstimer = MilliSecs()
    End If
End Function

;
; Aligns the movable blocks when it is next to a ladder ect.
; 
Function alignblock()
;optimize me
If Lastblockstate = True And movingblock = False Then
	If isclimb(playerx,playery) = True Then
		q = objects(currentlevel,currentmovingblock,1)
		For i=0 To 320 Step 16
			If q => i-2 And q=<i+2 Then
				objects(currentlevel,currentmovingblock,1) = i : Exit
			End If
		Next
	End If
EndIf
; Store the movingblock state inside our variable
Lastblockstate = movingblock
End Function

Function showtiles()
Cls
cnt = 0
For y=0 To 240-16 Step 16
For x=0 To 320-16 Step 16
If cnt<240 Then
DrawBlock gametiles,x,y,cnt : Text x+8,y+13,cnt,1,1
End If
cnt=cnt+1
Next
Next
End Function

;ok, here are the enemies I have done so far... a grey monkey, that can walk around jump throw poop And climb ladders, basically you but a bad guy.... a grey monkey that is always upside down hanging from something... IE a tree... he can throw poop at you any direction below him...  And a bat... that just flies Left Until it hits a wall, Then flies Right till it hits a wall.
;also when you jump sometimes at that ledge on the left, you bounce up on top of it...
;you should get the player sliding on ice, swimming,And throwing poop in... get the keys working etc, Before you do any more on the ai... I think its more important To have all the current level actions in... If you agree.
