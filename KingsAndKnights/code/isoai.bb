;
; AI code include
;
; AI method (Roam):
;
; ,0 = reset function
; ,1 = move up
; ,2 = move right
; ,3 = move down
; ,4 = move left
; ,5 = move up/right
; ,6 = move down/right
; ,7 = move left/down
; ,8 = move up/left
; ,9 = fortify
;
;
; Advanced AI method
;
; 1000 to 2000 = Advanced AI container
;
;
Dim AIcontainer(1000,10)
;
; 0 = active true/false
; 1 = pathfinding x destination
; 2 = pathfinding y destination
; 3 = 
; 4 = 
;
Dim enemylocations(100)

Dim enemyloc(8)

Dim temppathmap(100,100) ; For saving the pathfinding map

Function execute10000General()


;For i=0 To maxunits
;If units(i,13) > 10000 And units(i,13) < 10000+maxmissions Then
;mission = units(i,13)-10000
;units(i,13) = False
;aimission(mission)=False
;End If
;Next

For i=0 To maxunits
If units(i,0) = True Then
If units(i,13)>10000 And units(i,13)<15000 Then
If units(i,unit_alignment) = currentplayer Then

If MouseDown(1) = True Then Exit ; exit the loop

;

; Hopefull bugfix
placeunitontop(i)
fixontopunits()
; If the enemy is near the player then center the screen to that position
;If enemynearplayer(units(i,1),units(i,2),currentplayer) Then


If vismap(units(i,1),units(i,2),currentplayer) = True Or fogofwarenabled = False Then
If fogofwarenabled=False Then
	If enemynearplayer(units(i,1),units(i,2),currentplayer) = True Then
		fixontopunits
		centerscreen(units(i,1),units(i,2)) : If MouseDown(2) = False Then Delay 200
	End If
	Else
	fixontopunits
	centerscreen(units(i,1),units(i,2)) : If MouseDown(2) = False Then Delay 200
End If
End If




;End If

;
; Here we check if the player comes into contact with another player
;
stricktly = False
; If a unit has moved next to us then disable the movement
;If checkzoneforenemies(units(i,11),units(i,1),units(i,2)) = True Then stricktly = True
For x = units(i,1)-1 To units(i,1) + 1
For y = units(i,2)-1 To units(i,2) + 1
If x=>0 And y=>0 And x=<100 And y=<100 Then
For ii=0 To maxunits
If units(ii,0) = True Then
If units(ii,1) = x And units(ii,2) = y Then
If Not units(i,unit_alignment) = units(ii,unit_alignment) Then
stricktly = True
End If
End If
End If
Next
End If
Next
Next


While unitsmove#(i) > 0 And stricktly = False


mission = units(i,13) - 10000

If mission > -1 Then

pathslot = aimissionpathpointer(mission,0)


x=-1
y=-1

;If aimissionpathpointer(mission,1) = 0
If aimissionpathpointer(mission,1) = 0 Then
x = shortpathmap(pathslot,aimissionpointer(mission),0)
y = shortpathmap(pathslot,aimissionpointer(mission),1)
End If
If aimissionpathpointer(mission,1) = 1 Then
x = mediumpathmap(pathslot,aimissionpointer(mission),0)
y = mediumpathmap(pathslot,aimissionpointer(mission),1)
End If
If aimissionpathpointer(mission,1) = 2 Then
x = longpathmap(pathslot,aimissionpointer(mission),0)
y = longpathmap(pathslot,aimissionpointer(mission),1)
End If

End If

;If x=0 And y=0 Then
;	allzero = False
;	;DebugLog "mission : " + mission
;	For ii=0 To 10
;		If aimissionpathpointer(mission,1) = 0 Then
;			x = shortpathmap(pathslot,aimissionpointer(mission),0)
;			y = shortpathmap(pathslot,aimissionpointer(mission),1)
;		End If
;		If aimissionpathpointer(mission,1) = 1 Then
;			x = mediumpathmap(pathslot,aimissionpointer(mission),0)
;			y = mediumpathmap(pathslot,aimissionpointer(mission),1)
;		End If
;		If aimissionpathpointer(mission,1) = 2 Then
;			x = longpathmap(pathslot,aimissionpointer(mission),0)
;			y = longpathmap(pathslot,aimissionpointer(mission),1)
;		End If
;
;		If x = 0 And y=0 Then
;		    DebugLog "ii : " + ii
;			DebugLog "x : "+x
;			DebugLog "y : "+y
;		End If
;	Next
;
;End If

fixontopunits
; Little test to see if it fixed a nasty bug
;createontopunit(i,x,y)

; Is there a road?
If x>-1 And y>-1 Then
If roadmap(x,y) > 0 Then 
unitsmove#(i) = unitsmove#(i) - .33
Else
unitsmove#(i) = unitsmove#(i) - 1
End If
End If

weareatwar = False
For ii=1 To 8
If warstate(units(i,unit_alignment),ii) = True Then weareatwar = True
Next

;
; Disable the mission if there is a enemy presence
;
; If there is a non player on the screen
;If isthereanyplayerpresent(units(i,11),x,y) = True Then
; version 1.1 change
If checkzoneforenemies(units(i,11),x,y) = True Or weareatwar = False Then
units(i,13) = 0
stricktly = True
aimission(mission) = False ; disable mission
;
; Free path from memory
;
If aimissionpathpointer(mission,1) = 0 Then
shortpathmapactive(pathslot) = False
End If
If aimissionpathpointer(mission,1) = 1 Then
mediumpathmapactive(pathslot) = False
End If
If aimissionpathpointer(mission,1) = 2 Then
longpathmapactive(pathslot) = False
End If

aimissionpointer(mission)=0
;Goto skipper1
End If
;For xi=0 To 32 : DebugLog xi + " " + shortpathmap(pathslot,xi,0) + "," + shortpathmap(pathslot,xi,1):Next
;
previouscheck=False

If aimissionpathpointer(mission,1) = 0 Then
	If aimissionpointer(mission) < 33 Then
		If shortpathmap(pathslot,aimissionpointer(mission),0) = -1 Then previouscheck=True
	End If
End If
If aimissionpathpointer(mission,1) = 1 Then
	If aimissionpointer(mission) < 201 Then
		 If mediumpathmap(pathslot,aimissionpointer(mission),0) = -1 Then previouscheck=True
	End If
End If
If aimissionpathpointer(mission,1) = 2 Then
	If aimissionpointer(mission) < 1001 Then
		If longpathmap(pathslot,aimissionpointer(mission),0) = -1 Then previouscheck=True
	End If
End If


If previouscheck = True Then
stricktly = True ; was having a bug and this makes sure the loop isnt met if the mission is false
units(i,13) = 0
aimission(mission) = False
;
;
; clear the path memory
If aimissionpathpointer(mission,1) = 0 Then
shortpathmapactive(pathslot) = False
End If
If aimissionpathpointer(mission,1) = 1 Then
mediumpathmapactive(pathslot) = False
End If
If aimissionpathpointer(mission,1) = 2 Then
longpathmapactive(pathslot) = False
End If

aimissionpointer(mission)=0
;
;
;
unitsmove#(i) = 0
stricktly = True
aimissionpointer(mission)=0


If fortressmap(units(i,1),units(i,2)) = True Then units(i,unit_fortified) = True


Else
If x=>0 And y=>0 And x=<100 And y=<100 Then
units(i,1) =x
units(i,2) =y
updatevismap(units(i,unit_alignment),x,y)
placeunitontop(i)
;Else
;DebugLog "aimissionpathpointer" + aimissionpointer(mission)
;DebugLog "mission "+mission
;DebugLog "i "+i
;DebugLog "pathslot " + pathslot
;units(i,1) = shortpathmap(pathslot,aimissionpointer(mission)-1,0)
;units(i,2) = shortpathmap(pathslot,aimissionpointer(mission)-1,1)
End If
aimissionpointer(mission) = aimissionpointer(mission) + 1
End If

;showupdatedinges = False
;For i=0 To numminimapzones
;If minimapzones(i,0) = True Then
;If RectsOverlap(minimapzones(i,1),minimapzones(i,2),minimapzones(i,3)-minimapzones(i,1),minimapzones(i,4)-minimapzones(i,2),units(i,1),units(i,2),1,1) Then
;showupdatedinges=True
;End If
;End If
;Next

;If showupdatedinges = True Then
;If enemynearplayer(x,y,currentplayer) Then
If vismap(x,y,currentplayer) = True Or fogofwarenabled = False Then
	If fogofwarenabled=False Then
		If enemynearplayer(units(i,1),units(i,2),currentplayer) = True Then
			updatescreen() : Flip : If MouseDown(2) = False Then Delay 200
			placeunitontop(i)
		End If
		Else
		Cls
		updatescreen() : Flip : If MouseDown(2) = False Then Delay 200
		placeunitontop(i)
	End If

End If
;End If

;End If

Wend

.skipperdeskip
fixontopunits()
End If
End If
End If
Next

End Function









;
Function execute10000ai()



;For i=0 To maxunits
;If units(i,13) > 10000 And units(i,13) < 10000+maxmissions Then
;mission = units(i,13)-10000
;units(i,13) = False
;aimission(mission)=False
;End If
;Next
 
For i=0 To maxunits
If units(i,0) = True Then
If Not units(i,unit_alignment) = currentplayer Then
If units(i,13)>10000 And units(i,13)<15000 Then
If units(i,0) = True Then

updatescreen
Color Rand(0,100),Rand(0,100),Rand(0,100)
Rect 0,0,20,20,1
Flip

;

; Hopefull bugfix
placeunitontop(i)
fixontopunits()
; If the enemy is near the player then center the screen to that position
;If enemynearplayer(units(i,1),units(i,2),currentplayer) Then
If vismap(units(i,1),units(i,2),currentplayer) = True Or fogofwarenabled = False Then
If fogofwarenabled=False Then
	If enemynearplayer(units(i,1),units(i,2),currentplayer) = True Then
		fixontopunits
		centerscreen(units(i,1),units(i,2)) 
		If MouseDown(2) = False Then Delay 500
	End If
Else
	fixontopunits
	centerscreen(units(i,1),units(i,2))
	If MouseDown(2) = False Then Delay 200
End If
End If

;End If

;
; Here we check if the player comes into contact with another player
;
stricktly = False
; If a unit has moved next to us then disable the movement
;If checkzoneforenemies(units(i,11),units(i,1),units(i,2)) = True Then stricktly = True
For x1 = units(i,1)-1 To units(i,1) + 1
For y1 = units(i,2)-1 To units(i,2) + 1
If x1=>0 And y1=>0 And x1=<100 And y1=<100 Then
For ii=0 To maxunits
If units(ii,0) = True Then
If units(ii,1) = x1 And units(ii,2) = y1 Then
If Not units(i,unit_alignment) = units(ii,unit_alignment) Then
stricktly = True
End If
End If
End If
Next
End If
Next
Next



While unitsmove#(i) > 0 And stricktly = False And units(i,13) > 10000


mission = units(i,13) - 10000



pathslot = aimissionpathpointer(mission,0)

x=-1
y=-1


;If aimissionpathpointer(mission,1) = 0
If shortpathmapactive(pathslot) = True Then
If aimissionpathpointer(mission,1) = 0 Then
	If aimissionpointer(mission) < 33 Then
		x = shortpathmap(pathslot,aimissionpointer(mission),0)
		y = shortpathmap(pathslot,aimissionpointer(mission),1)
	End If
End If
End If
If mediumpathmapactive(pathslot) = True Then
If aimissionpathpointer(mission,1) = 1 Then
	If aimissionpointer(mission) < 201 Then
		x = mediumpathmap(pathslot,aimissionpointer(mission),0)
		y = mediumpathmap(pathslot,aimissionpointer(mission),1)
	End If
End If
End If
If longpathmapactive(pathslot) = True Then
If aimissionpathpointer(mission,1) = 2 Then
	If aimissionpointer(mission) < 1001 Then
		x = longpathmap(pathslot,aimissionpointer(mission),0)
		y = longpathmap(pathslot,aimissionpointer(mission),1)
	End If
End If
End If

; Bug test
If x = 0 And y = 0 Then
	;Delay 500 : FlushKeys : FlushMouse
	;simplemessage(aimissionpointer(mission)-1)
	;simplemessage("x : "+shortpathmap(pathslot,aimissionpointer(mission)-1,0)+" y : "+shortpathmap(pathslot,aimissionpointer(mission)-1,1))
	If shortpathmap(pathslot,aimissionpointer(mission)-1,0) = -1 Then
	units(i,0) = False
	End If
End If
;simplemessage("x : "+shortpathmap(pathslot,aimissionpointer(mission),0)+" y : "+shortpathmap(pathslot,aimissionpointer(mission),1))
;simplemessage("x : "+shortpathmap(pathslot,aimissionpointer(mission)+1,0)+" y : "+shortpathmap(pathslot,aimissionpointer(mission),1)+1)

;If x=0 And y=0 Then
;	allzero = False
;	;DebugLog "mission : " + mission
;	For ii=0 To 10
;		If aimissionpathpointer(mission,1) = 0 Then
;			x = shortpathmap(pathslot,aimissionpointer(mission),0)
;			y = shortpathmap(pathslot,aimissionpointer(mission),1)
;		End If
;		If aimissionpathpointer(mission,1) = 1 Then
;			x = mediumpathmap(pathslot,aimissionpointer(mission),0)
;			y = mediumpathmap(pathslot,aimissionpointer(mission),1)
;		End If
;		If aimissionpathpointer(mission,1) = 2 Then
;			x = longpathmap(pathslot,aimissionpointer(mission),0)
;			y = longpathmap(pathslot,aimissionpointer(mission),1)
;		End If
;
;		If x = 0 And y=0 Then
;		    DebugLog "ii : " + ii
;			DebugLog "x : "+x
;			DebugLog "y : "+y
;		End If
;	Next
;
;End If

fixontopunits
; Little test to see if it fixed a nasty bug
;createontopunit(i,x,y)

; Is there a road?
If x > -1 And y>-1 Then 
If roadmap(x,y) > 0 Then 
unitsmove#(i) = unitsmove#(i) - .33
Else
unitsmove#(i) = unitsmove#(i) - 1
End If
End If

weareatwar = False
For ii=1 To 8
If warstate(units(i,unit_alignment),ii) = True Then weareatwar = True
Next


;
; Disable the mission if there is a enemy presence
;
; If there is a non player on the screen
;If isthereanyplayerpresent(units(i,11),x,y) = True Then
; version 1.1 change
If checkzoneforenemies(units(i,11),x,y) = True Or weareatwar = False Or x = -1 Then
units(i,13) = 0
stricktly = True
aimission(mission) = False ; disable mission
;
; Free path from memory
;

If aimissionpathpointer(mission,1) = 0 Then
shortpathmapactive(pathslot) = False
End If
If aimissionpathpointer(mission,1) = 1 Then
mediumpathmapactive(pathslot) = False
End If
If aimissionpathpointer(mission,1) = 2 Then
longpathmapactive(pathslot) = False
End If

aimissionpointer(mission)=0
;Goto skipper1
End If
;For xi=0 To 32 : DebugLog xi + " " + shortpathmap(pathslot,xi,0) + "," + shortpathmap(pathslot,xi,1):Next
;
previouscheck=False

If aimissionpathpointer(mission,1) = 0 Then
	If aimissionpointer(mission) < 33 Then		
		If shortpathmap(pathslot,aimissionpointer(mission),0) = -1 Then previouscheck=True
	End If
End If
If aimissionpathpointer(mission,1) = 1 Then
	If aimissionpointer(mission) < 201 Then
		If mediumpathmap(pathslot,aimissionpointer(mission),0) = -1 Then previouscheck=True
	End If
End If
If aimissionpathpointer(mission,1) = 2 Then
	If aimissionpointer(mission) < 1001 Then
		If longpathmap(pathslot,aimissionpointer(mission),0) = -1 Then previouscheck=True
	End If
End If


If previouscheck = True Then
stricktly = True ; was having a bug and this makes sure the loop isnt met if the mission is false
units(i,13) = 0
aimission(mission) = False
;
;
; clear the path memory
If aimissionpathpointer(mission,1) = 0 Then
shortpathmapactive(pathslot) = False
End If
If aimissionpathpointer(mission,1) = 1 Then
mediumpathmapactive(pathslot) = False
End If
If aimissionpathpointer(mission,1) = 2 Then
longpathmapactive(pathslot) = False
End If

aimissionpointer(mission)=0
;
;
;
unitsmove#(i) = 0
stricktly = True
aimissionpointer(mission)=0


If fortressmap(units(i,1),units(i,2)) = True Then units(i,unit_fortified) = True



Else

If x=>0 And y=>0 And x=<100 And y=<100 Then


units(i,1) =x
units(i,2) =y

;If units(i,unit_x) = 0 And units(i,unit_y) = 0 Then End

fixontopunits()
updatescreen()
updatevismap(units(i,unit_alignment),x,y)
placeunitontop(i)
;Else
;DebugLog "aimissionpathpointer" + aimissionpointer(mission)
;DebugLog "mission "+mission
;DebugLog "i "+i
;DebugLog "pathslot " + pathslot
;units(i,1) = shortpathmap(pathslot,aimissionpointer(mission)-1,0)
;units(i,2) = shortpathmap(pathslot,aimissionpointer(mission)-1,1)
End If
aimissionpointer(mission) = aimissionpointer(mission) + 1
End If

;showupdatedinges = False
;For i=0 To numminimapzones
;If minimapzones(i,0) = True Then
;If RectsOverlap(minimapzones(i,1),minimapzones(i,2),minimapzones(i,3)-minimapzones(i,1),minimapzones(i,4)-minimapzones(i,2),units(i,1),units(i,2),1,1) Then
;showupdatedinges=True
;End If
;End If
;Next

;If showupdatedinges = True Then
;If enemynearplayer(x,y,currentplayer) Then
If x >-1 And y>-1 Then
If vismap(x,y,currentplayer) = True Or fogofwarenabled = False Then
	If fogofwarenabled=False Then
		If enemynearplayer(units(i,1),units(i,2),currentplayer) = True Then
			Cls:updatescreen() : Flip : If MouseDown(2) = False Then Delay 500
			placeunitontop(i)
		End If
		Else
		Cls
		updatescreen() : Flip: If MouseDown(2) = False Then Delay 500
		placeunitontop(i)
	End If

End If
End If

;End If

Wend

.skipperdeskip
fixontopunits
End If
End If
End If
End If
Next

End Function




Function aicreateattackmission(player)
Local olddifficultylevel = playerdifficultylevel(player)

destx = -1
desty = -1
; if we are not at war then exit the function
Local arewego = False
For i=1 To 8
If Not i = player Then
If warstate(player,i) = True Then arewego = True
End If
Next
If arewego = False Then Return

; Every 6 turns switch the difficulty level between 0 - (all out) and 5(medium)
If Rand(0,5) = 0 Then
playerdifficultylevel(player) = Rand(0,5)
End If



For i=0 To maxcities
	If cities(i,0) = True Then
	If cities(i,1) > 0 And cities(i,2) > 0 Then
	If cities(i,4) = player Then
	
	If Rand(0,playerdifficultylevel(player)) = 0 Then
	
	If playergold(player)>200 Then
	
		; create the mission
		;find nearest enemycity
		destinationplayer = 0
		Local destplayer[8]
		counter = 0
		For ii= 1 To 8
			If warstate(player,ii) = True And getplayercities(ii) > 0 Then destplayer[counter]=ii : counter = counter + 1
		Next
		counter = counter - 1
		
		If counter = -1 Then Return
		destinationplayer = destplayer[Rand(0,counter)]
		If destinationplayer = 9 Then Return
		If destinationplayer = currentplayer And warstate(player,currentplayer) = False Then Return
		
		If destinationplayer = player Then Return
		
		;DebugLog destinationplayer
		
		; If we have a priority target then use that one for the remainder of the increased ai activity
		If missiontarget(player) > 0 Then destinationplayer = missiontarget(player)
		
		
		;DebugLog "player : " + player
		;DebugLog "destination player " + destinationplayer
		;
		;DebugLog "counter : " + counter
		;DebugLog "player : " + player + " destinationplayer : " + destinationplayer
		
		;
		
		;If Not player = 4 Then destinationplayer = 4
		;If player = 4 Then destinationplayer = 3
		
		uboundshit = 0
		
		;Global ai_retakecities = 20
		;Global ai_attacknearbycities = 20
		;Global ai_attacknearbyfortress = 20
		;Global ai_attacknearbyunits = 20
		;Global ai_attackatrandom = 20
		
		a = Rand(1,100)
		aidestinationmode = 6;5
		;If a=>0 And a=<40 Then aidestinationmode = 1 ; retake conquered cities
		;If a>40 And a=<60 Then aidestinationmode = 2 ; attack nearby cities
		;If a>60 And a=<70 Then aidestinationmode = 3 ; attack nearby fortress
		;If a>70 And a=<90 Then aidestinationmode = 4 ; attack nearby units
		;If a>90 And a=<100 Then aidestinationmode = 5 ; attack at random
		
		;DebugLog missionselection(player,0,0) 
		;DebugLog missionselection(player,0,1)
		
		If a=>missionselection(player,0,0) And a=<missionselection(player,0,1) Then aidestinationmode = 1 ; retake conquered cities
		If a>missionselection(player,1,0) And a=<missionselection(player,1,1) Then aidestinationmode = 2 ; attack nearby cities
		If a>missionselection(player,2,0) And a=<missionselection(player,2,1) Then aidestinationmode = 3 ; attack nearby fortress
		If a>missionselection(player,3,0) And a=<missionselection(player,3,1) Then aidestinationmode = 4 ; attack nearby units
		If a>missionselection(player,4,0) And a=<missionselection(player,4,1) Then aidestinationmode = 5 ; attack at random
		
		;If aidestinationmode = 1 Then aidestinationmode = 4
		;aidestinationmode = 4
		
		;If aidestinationmode = 3 Then aidestinationmode = 4
		
		
		destx = -1
		desty = -1
		
		
;		aidestinationmode = 3

		;
		; If there are units nearby then attack
		;
		If returnenemyunitsinarea(player,cities(i,city_x),cities(i,city_y),6,6) > 1 Then
			aidestinationmode = 4
		End If


		;
		; Reconquer fallen cities
		;
		If aidestinationmode = 1 Then
			uboundshit=0
			For ii=0 To maxcities
				If playercityconquest(ii,0) = player Then
					citiesdestinations(uboundshit,0) = cities(ii,1)
					citiesdestinations(uboundshit,1) = cities(ii,2)
					uboundshit = uboundshit + 1
				End If
			Next			
			If uboundshit > 0 Then
				a = Rand(uboundshit-1)
				destx = citiesdestinations(a,0)
				desty = citiesdestinations(a,1)
				unittocreate = 0
				DebugLog ("attack conquered city" + ii)
				Else
				aidestinationmode = 5
			End If
		End If
		

	
		;
		; Find cities in a 20*20 area of the attacking city
		
		If aidestinationmode = 2 Then
		
			uboundshit = 0
			For ii=0 To maxcities
				If cities(ii,0) = True Then
				If cities(ii,4) = destinationplayer Then 
				If cities(ii,1) >cities(i,1)-15 And cities(ii,1)<cities(i,1)+15 Then
				If cities(ii,2) > cities(i,2)-15 And cities(ii,2)<cities(i,2)+15 Then
				;destx = cities(ii,1)
				;desty = cities(ii,2)
				amap(0,0) = 10
				If amap(cities(ii,1),cities(ii,2)) = player Then
					citiesdestinations(uboundshit,0) = cities(ii,1)
					citiesdestinations(uboundshit,1) = cities(ii,2)
					uboundshit = uboundshit + 1
				End If
				End If
				End If
				End If
				End If
			Next
			;
			If uboundshit > 0 Then
			a = Rand(uboundshit-1)
			destx = citiesdestinations(a,0)
			desty = citiesdestinations(a,1)
			Else
			aidestinationmode = 5
			End If
			unittocreate = 0 ; create siege weapons and cavalry
		End If
		
		; Target a nearby fortess
		;
		If aidestinationmode = 3 Then
			;
			
			
			uboundshit = 0
			For x1=cities(i,city_x)-4 To cities(i,city_x)+4	
			For y1=cities(i,city_y)-4 To cities(i,city_y)+4
				If RectsOverlap(x1,y1,1,1,0,0,100,100) = True Then
				If fortressmap(x1,y1) = True Then
				If countunits(player,x1,y1) < 5 Then
					fortressdestinations(uboundshit,0) = x1
					fortressdestinations(uboundshit,1) = y1
					uboundshit = uboundshit + 1					
				End If
				End If
				End If
			Next
			Next
			;
			If uboundshit > 0 Then
			a=Rand(uboundshit-1)
			destx = fortressdestinations(a,0)
			desty = fortressdestinations(a,1)
			Else
			aidestinationmode = 5
			DebugLog "dest 3 to dest 5"
			End If
			unittocreate = 2 ; pikemen
			
		End If
		
		;
		; Attack a nearby unit
		
		If aidestinationmode = 4 Then
		
			uboundshit = 0
			For x1=cities(i,1)-6 To cities(i,1)+6
			For y1=cities(i,2)-6 To cities(i,2)+6
				If x1=>0 And x1=<100 And y1=>0 And y1=<100 Then
					For ii=0 To maxunits
						If units(ii,1) = x1 And units(ii,2) = y1 Then
						If units(ii,unit_alignment) = destinationplayer Then
						If isunitoncity(ii) = False Then
					
						If Not map(units(ii,unit_x),units(ii,unit_y)) = 64 Then
						unitsdestinations(uboundshit,0) = x1
						unitsdestinations(uboundshit,1) = y1
						uboundshit = uboundshit + 1
						
						End If
						End If
						End If
						End If
					Next
				End If
			Next
			Next
		
			If uboundshit > 0 Then
			a = Rand(uboundshit-1)
			destx = unitsdestinations(a,0)
			desty = unitsdestinations(a,1)
			Else
			aidestinationmode = 5
			End If
		
		
			unittocreate = 1 ; modifier 2 - create cavalry
		
		End If
		
		
		;
		; attack at random, any enemy city as destination - 5
		
		;
		If aidestinationmode = 5 Then

			uboundshit=0
			For ii=0 To maxcities
			If cities(ii,0) = True Then
			If cities(ii,4) = destinationplayer Then 
			;destx = cities(ii,1)
			;desty = cities(ii,2)
			If amap(cities(ii,1),cities(ii,2)) = player Then
				citiesdestinations(uboundshit,0) = cities(ii,1)
				citiesdestinations(uboundshit,1) = cities(ii,2)
				uboundshit = uboundshit + 1
		
			End If
			End If
			End If
			Next
			;
			destx = -1 : desty = -1
			If uboundshit>0 Then 
			a = Rand(uboundshit-1)
		
			destx = citiesdestinations(a,0)
			desty = citiesdestinations(a,1)
			
			unittocreate = 0 ; create siege weapons and cavalry
			End If
		
		End If
		
		;
		;
		
		If destx = -1 And desty = -1 Then Goto skipper;simplemessage("no target for ai") : End
		
		

		
		;If destx = 0 And desty = 0 Then End
		; Special ai for not getiing into trouble while attacking
		;
		;
		; Basically it analyzes a 10*10 area of the destination and sees if it has a enemy ocupies surface
		; ratio of 5. Then it changes the assault unit as cavalry
		; if the ratio is above 10 then the units will be attacked
		;
		
		If aidestinationmode = 5 Or aidestinationmode = 2 Or aidestinationmode = 1 Or aidestinationmode = 3 Then
			;
			; Check if the area around the targer contains lots of enemies
			For x1=0 To 11
				For y1=0 To 11
					areaunitmap(x1,y1,0) = False
				Next
			Next
			x2 = 0
			y2 = 0
			areacounter = 0
			For x1=destx-5 To destx+5
				For y1=desty-5 To desty+5
					If x1=>0 And x1=<100 And y1=>0 And y1=<100 Then
						For ii=0 To maxunits
							If units(ii,0) = True Then
								If warstate(player,units(ii,unit_alignment)) = True Then
									If units(ii,1) = x1 And units(ii,2) = y1 Then
										; fill in a 10x10 map 
										areaunitmap(x2,y2,0) = True
										areaunitmap(x2,y2,1) = x1
										areaunitmap(x2,y2,2) = y1
									End If
								End If
							End If
						Next
					End If
					y2=y2+1 : If y2>10 Then y2=0
				Next
				x2=x2+1
			Next
			
			; analyze the density of the areamap
			areacounter = 0
			For x1=0 To 10
				For y1=0 To 10
					If areaunitmap(x1,y1,0) = True Then
					areacounter = areacounter + 1
					End If
				Next
			Next
			;DebugLog areacounter : End
			; At which density do we send cavalry
			If areacounter > 4 Then unittocreate = 1; create calvalry
			; If there are a lot of enemies there then attack a enemy instead
			If areacounter > 10 Then
				;
				; Select a enemy in that area as target instead
				a = Rand(areacounter)
				counter = 0
				For x1=0 To 10
					For y1=0 To 10
						If areaunitmap(x1,y1,0) = True Then
						If counter = a Then 
						destx = areaunitmap(x1,y1,1)
						desty = areaunitmap(x1,y1,2)
						DebugLog("Ai assaults defending forces instead - aicreateattackmission#")
						End If
						counter = counter + 1
						End If
					Next
				Next
				;
			End If
			
			
			
		End If ; end if if aidestinationmode = 5 or aidestinationmode = 2 or aidestinationmode = 1 then
		;savepathmap() ; save the pathfinding map
		;createpathstrategy01(player,destx,desty)
		
		;DebugLog cities(i,1) + " " + cities(i,2) + " " + destx + " " + desty
		; find the path
		;
		; Here we select where by the city we want to send this unit
		
		
		
		If playergold(player)>1000 And Rand(1,5) = 1 Then
			attackingroup = True
			Else
			attackingroup = False
		End If
		;soepvis = False
		;If getplayerunits(player) < getplayerunits(destinationplayer) Then soepvis = True
		;If playergold(player) < 600 Then soepvis = False
		;If soepvis = True Then attackingroup = True
		
		If attackingroup = True Then loopend = Rand(2,6) Else loopend = 0
		
		For zi=0 To loopend
			
			;writedebug("Attack mode : " + aidestinationmode + " x : " + destx + " y : " + desty)
			
			Select aidestinationmode
				Case 1 : DebugLog("Reconquer fallen cities - 1")
				Case 2 : DebugLog("Find cities in a 20*20 area of the attacking city - 2")
				Case 3 : DebugLog("Target a nearby fortess - 3")
				Case 4 : DebugLog("Attack a nearby unit - 4")
				Case 5 : DebugLog("attack at random, any enemy city as destination - 5")
			End Select
			
			
			; attack at random, any enemy city as destination - 5
			; Attack a nearby unit - 4
			; Target a nearby fortess - 3
			; Find cities in a 20*20 area of the attacking city - 2
			; Reconquer fallen cities - 1
			
			If map(destx,desty) <> 64 Then ; if not on water
			If destx> - 1 Then b = createpath(cities(i,4),cities(i,1),cities(i,2),destx,desty)
			End If
			
			
			If b>0 Then DebugLog "path created"
			
			
			;loadpathmap() ; restore the pathfinding map
			
			If Not b=False And as_outsize>0 Then
				;  Fill in the mission parameters
				c = findemptymission()
				If c>-1 Then ; if a mission is available
					aimissionpointer(c)=0
					aimission(c) = True
					If b=>1000 And b<2000 Then
						aimissionpathpointer(c,0) = b-1000
						missionnumber = c
						aimissionpathpointer(c,1) = 0
						Else If b=>2000 And b<3000 
						aimissionpathpointer(c,0) = b-2000
						missionnumber = c
						aimissionpathpointer(c,1) = 1
						Else If b=>3000 And b<4000
						aimissionpathpointer(c,0) = b-3000
						missionnumber = c
						aimissionpathpointer(c,1) = 2
					End If
				
					a = findnewunit()
					If Not a=-1 Then
						;	

					; create the unit
						If unittocreate = 0 Then
							Select Rand(1,5)
								Case 1
								createunit(a,player,cities(i,1),cities(i,2),11,missionnumber+10000) : createdunit = 11
								Case 2
								createunit(a,player,cities(i,1),cities(i,2),10,missionnumber+10000) : createdunit = 10
								Case 3
								createunit(a,player,cities(i,1),cities(i,2),9,missionnumber+10000) : createdunit = 9
								Case 4
								createunit(a,player,cities(i,1),cities(i,2),8,missionnumber+10000) : createdunit = 8
								Case 5
								createunit(a,player,cities(i,1),cities(i,2),7,missionnumber+10000) : createdunit = 7
							End Select
						End If
						If unittocreate = 1 Then
							Select Rand(1,2)
								Case 1
								createunit(a,player,cities(i,1),cities(i,2),8,missionnumber+10000) : createdunit = 8
								Case 2
								createunit(a,player,cities(i,1),cities(i,2),7,missionnumber+10000): createdunit = 7
							End Select	
						End If
						If unittocreate = 2 Then
								createunit(a,player,cities(i,1),cities(i,2),4,missionnumber+10000) : createdunit = 4
						End If

					End If
				
					; If the distance is to long then create a fast moving unit
					If unittocreate = 0 And as_outsize > 20 Then createdunit = Rand(7,8)
					; 
					; decrease player money
					playergold(player) = playergold(player) - unitdefault(createdunit,4)
					; 
				End If
			End If
		Next
		.skipper
	End If
	End If
	End If
	End If
	End If

Next
playerdifficultylevel(player) = olddifficultylevel
End Function

Function findemptymission()
For i=0 To maxmissions
If aimission(i) = False Then Return i
Next
Return -1
End Function

Function createunit(unit,player,x,y,unittype,aiexecution)
units(unit,0) = True
units(unit,1) = x
units(unit,2) = y
units(unit,3) = True
units(unit,4) = unittype
units(unit,5) = unitdefault(unittype,0)
units(unit,6) = unitdefault(unittype,1)
units(unit,7) = 0     ; Damage (0 - no, 99 - max)
units(unit,8) = False ; Fortified
units(unit,9) = False ; Veteran status 
units(unit,10) = 1    ; Moves left - not working
units(unit,11) = player    ; Belongs to player x
units(unit,12) = False; true if units is on hold
units(unit,13) = aiexecution    ; AI method/unit method
units(unit,14) = False; Unit is inside a city
units(unit,15) = 0    ; turns inactive
units(unit,16) = 0    ; Home city

unitsmove#(unit) = unitdefault(units(unit,unit_type),default_movement)
End Function


;
; This function reassigns useless units on the map
;
Function reassignai(player)
For i=0 To maxunits
	If units(i,0) = True Then
	If units(i,14) = False Then ; if the unit is not in a city
	If units(i,1) >0 And units(i,2) > 0 Then
		If units(i,unit_alignment) = player Then
			If units(i,unit_fortified) = False Then
				
				If Not units(i,13) > 0 Then

					destinationplayer = 0
					Local destplayer[8]
					counter = 0
					For ii= 1 To 8
						If warstate(player,ii) = True Then destplayer[counter]=ii : counter = counter + 1
					Next
					counter = counter - 1
					If counter = -1 Then Return
					destinationplayer = destplayer[Rand(counter)]
					If destinationplayer = 0 Then Goto deskipper
					
					If destinationplayer = player Then Return
					
					If missiontarget(player) > 0 Then destinationplayer = missiontarget(player)
					
					x = units(i,1)
					y = units(i,2)
					destx = -1
					desty = -1
					
			
					; First scan the direct area for enemy presence and record their presence for
					; possible later use
					For x1=0 To 11
						For y1=0 To 11
						areaunitmap(x1,y1,0) = False
						Next
					Next
					counter = 0
					x2 = 0
					y2 = 0
					For x1=x-5 To x+5
						For y1=y-5 To y+5
							If x1=>0 And x1=<100 And y1=>0 And y1=<100 Then
								If returnenemypresence(player,x1,y1) = True Then
								areaunitmap(x2,y2,0) = True
								areaunitmap(x2,y2,1) = x1
								areaunitmap(x2,y2,2) = y1
								counter = counter + 1
								End If
							End If
							y2=y2+1:If y2>10 Then y2=0
						Next
						x2 = x2 + 1
					Next
					;
					; ------------------------------------------------
					;  This is the first attack tactic
					; ------------------------------------------------
					;
					; If there is no presence in the area
					If counter = 0 Then
						a = Rand(0,100)
						;
						destinationselected = False
						aidestinationmode = 5
						;
;						If a=>0 And a=<40 Then aidestinationmode = 1 ; retake conquered cities
;						If a>40 And a=<60 Then aidestinationmode = 2 ; attack nearby cities
;						If a>60 And a=<70 Then aidestinationmode = 3 ; attack nearby fortress
;						If a>70 And a=<90 Then aidestinationmode = 4 ; attack nearby units
;						If a>90 And a=<100 Then aidestinationmode = 5 ; attack at random
						If a=>missionselection(player,0,0) And a=<missionselection(player,0,1) Then aidestinationmode = 1 ; retake conquered cities
						If a>missionselection(player,1,0) And a=<missionselection(player,1,1) Then aidestinationmode = 2 ; attack nearby cities
						If a>missionselection(player,2,0) And a=<missionselection(player,2,1) Then aidestinationmode = 3 ; attack nearby fortress
						If a>missionselection(player,3,0) And a=<missionselection(player,3,1) Then aidestinationmode = 4 ; attack nearby units
						If a>missionselection(player,4,0) And a=<missionselection(player,4,1) Then aidestinationmode = 5 ; attack at random


						destx = -1
						desty = -1


						;
						; Reconquer fallen cities
						;	
						If aidestinationmode = 1 Then
							uboundshit=0
							For ii=0 To maxcities
								If playercityconquest(ii,0) = player Then
									If playercityconquest(ii,1) = destinationplayer Then
										citiesdestinations(uboundshit,0) = cities(ii,1)
										citiesdestinations(uboundshit,1) = cities(ii,2)
										uboundshit = uboundshit + 1
									End If
								End If
							Next

							If uboundshit > 0 Then
								a = Rand(uboundshit-1)
								destx = citiesdestinations(a,0)
								desty = citiesdestinations(a,1)
								unittocreate = 0
								destinationselected = True
								Else
								aidestinationmode = 5
							End If
						End If


						;
						; Find cities in a 20*20 area of the attacking city
						
						If aidestinationmode = 2 Then
						
							uboundshit = 0
							For ii=0 To maxcities
								If cities(ii,0) = True Then
									If cities(ii,4) = destinationplayer Then 
										If cities(ii,1) >cities(i,1)-15 And cities(ii,1)<cities(i,1)+15 Then
											If cities(ii,2) > cities(i,2)-15 And cities(ii,2)<cities(i,2)+15 Then
											;destx = cities(ii,1)
											;desty = cities(ii,2)
											amap(0,0) = 10
												If amap(cities(ii,1),cities(ii,2)) = player Then
													citiesdestinations(uboundshit,0) = cities(ii,1)
													citiesdestinations(uboundshit,1) = cities(ii,2)
													uboundshit = uboundshit + 1
												End If
											End If
										End If
									End If
								End If
							Next
							;
							If uboundshit > 0 Then
								a = Rand(uboundshit-1)
								destx = citiesdestinations(a,0)
								desty = citiesdestinations(a,1)
								Else
								aidestinationmode = 5
							End If
							unittocreate = 0 ; create siege weapons and cavalry
						End If
				
						; Target a nearby fortess
						;
						If aidestinationmode = 3 Then
							;
						;	
						;	
							uboundshit = 0
							For x1=cities(i,city_x)-4 To cities(i,city_x)+4	
							For y1=cities(i,city_y)-4 To cities(i,city_y)+4
								If RectsOverlap(x1,y1,1,1,0,0,100,100) = True Then
								If fortressmap(x1,y1) = True Then
									fortressdestinations(uboundshit,0) = x1
									fortressdestinations(uboundshit,1) = y1
									uboundshit = uboundshit + 1					
								End If
								End If
							Next
							Next
							;
							If uboundshit > 0 Then
								a=Rand(uboundshit-1)
								destx = fortressdestinations(a,0)
								desty = fortressdestinations(a,1)
								Else
								aidestinationmode = 5
							End If
							unittocreate = 1
						;	
						End If
						;
						; Attack a nearby unit
						;
						
						If aidestinationmode = 4 Then
						;
							uboundshit = 0
							For x1=cities(i,1)-6 To cities(i,1)+6
								For y1=cities(i,2)-6 To cities(i,2)+6
									If x1=>0 And x1=<100 And y1=>0 And y1=<100 Then
										For ii=0 To maxunits
											If units(ii,1) = x1 And units(ii,2) = y1 Then
												If units(ii,unit_alignment) = destinationplayer Then
													If isunitoncity(ii) = False Then									
														If Not map(units(ii,unit_x),units(ii,unit_y)) = 64 Then
														unitsdestinations(uboundshit,0) = x1
														unitsdestinations(uboundshit,1) = y1
														uboundshit = uboundshit + 1											
														End If
													End If
												End If
											End If
										Next
									End If
								Next
							Next
						;
							If uboundshit > 0 Then
								a = Rand(uboundshit-1)
								destx = unitsdestinations(a,0)
								desty = unitsdestinations(a,1)
								Else
								aidestinationmode = 5
							End If
						;						
							unittocreate = 1 ; modifier 2 - create cavalry
						;
						End If
				
						;
						; attack at random, any enemy city as destination - 5
						
						;
						If aidestinationmode = 5 Then
							uboundshit=0
							For ii=0 To maxcities
								If cities(ii,0) = True Then
									If cities(ii,4) = destinationplayer Then 
									;destx = cities(ii,1)
									;desty = cities(ii,2)
										If amap(cities(ii,1),cities(ii,2)) = player Then
											citiesdestinations(uboundshit,0) = cities(ii,1)
											citiesdestinations(uboundshit,1) = cities(ii,2)
											uboundshit = uboundshit + 1
										End If
									End If
								End If
							Next
							;
							destx =-1 : desty = -1
							If uboundshit>0 Then 
								a = Rand(uboundshit-1)
							
								destx = citiesdestinations(a,0)
								desty = citiesdestinations(a,1)
								
								;DebugLog "false destination"		
								unittocreate = 0 ; create siege weapons and cavalry
							End If						
						End If
					End If
					;
					; ------------------------------------------------
					; This is the second attack tactic
					; ------------------------------------------------
					;
					; If there is enemy presence in the area
					If counter > 0 Then
						; attack a nearby unit
						Local selecteddest = False
						destinationselected = False
						;
						While selecteddest = False
							For x1=0 To 10
								For y1=0 To 10
									If areaunitmap(x1,y1,0) = True Then
										If Rand(20) = 1 Then						
											destx = areaunitmap(x1,y1,1)
											desty = areaunitmap(x1,y1,2)
											destinationselected = True
											selecteddest = True
										End If
									End If
								Next
							Next
						Wend
					End If
					;
					; -------------------------------------------------
					;  Here we find a path for the new unit
					; -------------------------------------------------
					;
					pathissearched = False
					If destinationselected = True And destx>-1 And desty>-1 Then
						;
						

						;
						If Not map(destx,desty) = 64 Then
							b = createpath(units(i,unit_alignment),units(i,1),units(i,2),destx,desty)
						End If
						pathissearched = True

						If Not b=False And as_outsize>0 Then
							;  Fill in the mission parameters
							c = findemptymission()
							If c>-1 Then ; if a mission is available
								aimissionpointer(c)=0
								aimission(c) = True
								If b=>1000 And b<2000 Then
									aimissionpathpointer(c,0) = b-1000
									missionnumber = c
									aimissionpathpointer(c,1) = 0
									Else If b=>2000 And b<3000 
									aimissionpathpointer(c,0) = b-2000
									missionnumber = c
									aimissionpathpointer(c,1) = 1
									Else If b=>3000 And b<4000
									aimissionpathpointer(c,0) = b-3000
									missionnumber = c
									aimissionpathpointer(c,1) = 2
								End If
								; assign the ai mission to the unit
								units(i,13) = missionnumber + 10000
								;				
							End If
						End If
						;
						
						;
					End If
					;
					;
					;
					;
					.deskipper
				End If
			End If
		End If
		End If
		End If
	End If
Next
End Function


;
; Ai defensive strategy assignent function
;
Function switchaistate(player)
Local numcitiescaptured = 0
; Count the number of cities the player has lost
;
;
For i=0 To maxcities
	If playercityconquest(i,0) = player Then numcitiescaptured = numcitiescaptured + 1
Next

; Set a new playing tactic - 4 strategies
;
;
If numcitiescaptured > 0 Then 
newtactic = Rand(3)
End If

; If the cpu player has lost a city more then 20 turns ago then switch to regular playing style
;
;
Local lastcitylostturn = 20
For i=0 To maxcities
	If playercityconquest(i,0) = player Then
		If playercityconquest(i,2) < lastcitylostturn Then
			lastcitylostturn = playercityconquest(i,2)
			missiontarget(player) = playercityconquest(i,1)
	End If
End If
Next
; set a flag if the last city was lost more then 20 turns ago
If lastcitylostturn=>20 Then
	newtactic = -1
	; Set the player destination to zero so the ai will atack all its enemies
	missiontarget(player) = 0
End If
;
; Here we set the new playing tactic
If newtactic = -1 Then ; if the last city was lost 20 turns ago then reset to default
	For i=0 To 4
		missionselection(player,i,0) = missionselectiondefault(player,i,0)
		missionselection(player,i,1) = missionselectiondefault(player,i,1)
	Next
Else ; Set to the new stategy
	For i=0 To 4
		missionselection(player,i,0) = missionselectionpreset(newtactic,i,0)
		missionselection(player,i,1) = missionselectionpreset(newtactic,i,1)
	Next
	playerdifficultylevel(player) = 0
End If



;

End Function
;
;
; This function lets a computer controlled player attack if units
; appear next to them
Function playerattackenemies(player)




For i=0 To maxunits
If units(i,11) = player
If units(i,0) = True Then
If units(i,14) = False Then
If unitsmove(i) > 1 Then
x = units(i,1)
y = units(i,2)
;DebugLog "unit : " + i + " x : " + x + " y : " + y 
If (units(i,unit_fortified) = True) And (fortressmap(units(i,unit_x),units(i,unit_y)) = True) Then zipper = 15 Else zipper = 1

If Rand(1,zipper) = 1 Then
;Delay(100) : playsfx(7)
;If checkzoneforenemies(player,units(i,1),units(i,2)) = True Then
;a = getenemylocationnumber(player,units(i,1),units(i,2))
;DebugLog units(a,1)
;DebugLog units(a,2)
counter = 0
For y2=-1 To 1
For x2=-1 To 1
x1 = x + x2
y1 = y + y2
If x1>mapwidth Then x1=mapwidth
If x1<0 Then x1=0
If y1>mapheight Then y1=mapheight
If y1<0 Then y1=0

For ii=0 To maxunits
;If (Not x2=0) And (Not y2 = 0) Then
If units(ii,0) = True Then
If units(ii,1) = x1 Then
If units(ii,2) = y1 Then
If Not units(ii,11) = player Then
If warstate(player,units(ii,11))=True Then
enemylocations(counter) = ii
counter=counter+1
;End If
End If
End If
End If
End If
End If
Next

Next
Next

;If counter = 0 Then Return
;For ii=0 To counter
;DebugLog " counter : " + counter + " container " + enemylocations(ii)
;units(enemylocations(ii),0) = False
;Next
counter = counter - 1

If counter>-1 Then a = enemylocations(Rand(0,counter)) Else a = -1
;DebugLog " selected  " + a


If a>-1 Then
If Rand(1,1) = 1 Then
x = units(a,1)
y = units(a,2)
	;
	; Zoom in to the battle
	If showallbattles = True Then
	fixontopunits
	centerscreen(x,y)
	updatescreen()
	Flip
	If MouseDown(2) = False Then Delay(500)
End If	

result = dobattle(i,x,y)
If result = False Then ; if the attacker has lost
units(i,0) = False
;removeontopunit(units(i,1),units(i,2))
;createontopunit(units(i,unit_alignment),units(i,unit_x),units(i,unit_y))
fixontopunits
updatescreen() :Flip: If MouseDown(2) = False Then Delay(1250)
Else ; if the attacker has won ************	
;If fortressmap(x,y) = True Then
;removeontopunit(x,y)
;setstrongdefenderontop(x,y)
;End If
	
	; is the new position on a fortress
	If fortressmap(x,y)=True Then
		removeontopunit(x,y)
		setstrongdefenderontop(x,y)
		If isthereanyplayerpresent(units(i,unit_alignment),x,y) = False Then
			units(i,1) = units(a,1)	
			units(i,2) = units(a,2)
			updatevismap(units(i,unit_alignment),x,y)
		End If
	End If

	; is the new position on the enemycity
	If isunitonenemycity(i,x,y)=True Then
		removeontopunit(x,y)
		setstrongdefenderontop(x,y)
		If isthereanyplayerpresent(units(i,unit_alignment),x,y) = False Then
			
			za = findcityxy(x,y)
			If za>-1 Then cities(za,city_alignment) = units(i,unit_alignment)
			; Feed the new conquest in the captured city array
			playercityconquest(za,0) = cities(za,city_alignment) ; move in previous owner
			playercityconquest(za,1) = player ; move in the new owner
			playercityconquest(za,2) = gameturn ; move in the gameturn
			units(i,1) = units(a,1)	
			units(i,2) = units(a,2)
			updatevismap(units(i,unit_alignment),x,y)
		End If
	End If
	; Is the new position not a city and not a fort
	If isunitonenemycity(i,x,y)=False And fortressmap(x,y) = False Then
;		destroyunitsatposition2(a)
;		DebugLog "pass 1"
;		For ii=0 To maxunits
;		If units(ii,0) = True Then
;		If units(ii,1) = x And units(ii,2) = y Then
;		DebugLog "present"
;		If units(ii,3) = True Then
;		DebugLog "Ontop"
;		End If
;		End If
;		End If
;		Next
;		DebugLog "Pass 2"
		removeontopunit(x,y)
		setstrongdefenderontop(x,y)
		fixontopunits()
;		For ii=0 To maxunits
;		If units(ii,0) = True Then
;		If units(ii,1) = x And units(ii,2) = y Then
;		DebugLog "present"
;		If units(ii,3) = True Then
;		DebugLog "Ontop"
;		End If
;		End If
;		End If
;		Next

;	If checkforenemies(i,x,y) = False Then
	If returnenemypresence(player,x,y) = False Then
		deleteallunitsattile(player,x,y)
		; set new coordinates
		units(i,1) = x
		units(i,2) = y
		units(i,3)=True
		setstrongdefenderontop(x,y)
		updatevismap(units(i,unit_alignment),x,y)
	End If
		
;		units(i,1) = units(a,1)
;		units(i,2) = units(a,2)
		
		unitsmove(i) = unitsmove(i) - 1

;		units(i,13) = 9
		updatescreen() :Flip: If MouseDown(2) = False Then Delay(1250)
	End If

fixontopunits()
End If

End If

End If
End If
End If

End If
End If
End If
Next

End Function

; This function returns true if a enemy unit is on the x y location
;
Function returnenemypresence(player,x,y)
For i=0 To maxunits
If units(i,0) = True Then
If units(i,1) = x And units(i,2) = y Then
pn = units(i,unit_alignment)
If warstate(player,pn) = True Then
Return True
End If
End If
End If
Next
End Function

;
; This upgrades the ai cities
;
Function upgradeaicities(player)
For i=0 To maxcities
If cities(i,0) = True Then
If cities(i,4) = player Then
If cities(i,5) < 150 Then
If playergold(player) > 1600 Then
cities(i,5) = cities(i,5) + 15
playergold(player) = playergold(player) - 500
If cities(i,5) > 50 Then cities(i,3) = 2
If cities(i,5) > 100 Then cities(i,3) = 3
If cities(i,5) > 140 Then cities(i,3) = 4
End If
End If
End If
End If
Next
End Function
;
; function for finding the city at a x and y coordinates
;
Function findcityxy(x,y)
For i=0 To maxcities
If cities(i,0) = True Then
If cities(i,city_x) = x And cities(i,city_y) = y Then Return i
End If
Next
Return False
End Function
;
; This function makes sure the ai cities have enough defensive forces
;
;
Function updatedefensiveforce(player)
For i=0 To maxcities
If cities(i,city_active) = True Then
If cities(i,city_alignment) = player Then
If playergold(player) > 300 Then
If returndefensiveforcesincity(i,player) < 5 Then
unittobuild = 4 ; make a spearman
playergold(player) = playergold(player) - unitdefault(unittobuild,4) ; decrease the price of the unit
Select player
Case 1 
If Rand(player1homeforceincome) = 1 Then
buildnewunitincity(i,player)
End If
Case 2
If Rand(player2homeforceincome) = 1 Then
buildnewunitincity(i,player)
End If
Case 3
If Rand(player3homeforceincome) = 1 Then
buildnewunitincity(i,player)
End If
Case 4
If Rand(player4homeforceincome) = 1 Then
buildnewunitincity(i,player)
End If
Case 5
If Rand(player5homeforceincome) = 1 Then
buildnewunitincity(i,player)
End If
Case 6
If Rand(player6homeforceincome) = 1 Then
buildnewunitincity(i,player)
End If
Case 7
If Rand(player7homeforceincome) = 1 Then
buildnewunitincity(i,player)
End If
Case 8
If Rand(player8homeforceincome) = 1 Then
buildnewunitincity(i,player)
End If
End Select
End If
End If
End If
End If
Next
End Function

;
; Function that builds a new unit in a city
;
Function buildnewunitincity(city,player)
a = findnewunit()
;removeontopunit(cities(city,city_x),cities(city,city_y))
setstrongdefenderontop(cities(city,city_x),cities(city,city_y))
If a>-1 Then
units(a,0) = True  ; Active
units(a,1) = cities(city,city_x)     ; X pos
units(a,2) = cities(city,city_y)     ; Y pos
units(a,3) = True  ; Ontop status
units(a,4) = 4    ; Unit type 
units(a,5) = unitdefault(4,0)
units(a,6) = unitdefault(4,1)
units(a,7) = 0     ; Damage (0 - no, 99 - max)
units(a,8) = True  ; Fortified
units(a,9) = False ; Veteran status 
units(a,10) = 0    ; Moves left
units(a,11) = player   ; Belongs to player x
units(a,12) = False; true if units is on hold
units(a,13) = 0    ; AI method/unit method
units(a,14) = True; Unit is inside a city
units(a,15) = 0    ; turns inactive
units(a,16) = 0    ; Home city
End If
End Function
;
;
; Function that returns the number of defensive forces in a city
;
Function returndefensiveforcesincity(city,player)
counter = 0
For i=0 To maxunits
If units(i,unit_active) = True Then
If units(i,unit_alignment) = player Then
If units(i,unit_x) = cities(city,city_x) And units(i,unit_y) = cities(city,city_y) Then
counter = counter + 1
End If
End If
End If
Next
Return counter
End Function

;
Function roamplayer(n)
tempx = mapx : tempy = mapy
movesleft=True
While movesleft = True Or KeyDown(1) = True
movesleft = False
For i=0 To maxunits ; loop thru all units
If units(i,11) = n Then ; if the units is of player 'n'
If units(i,0) = True Then ; if the unit is active
;If units(i,10) > 0 Then ; if there are moves left
If unitsmove(i) > 0 Then ; If there are moves left
;While units(i,10) > 0 
While unitsmove(i) > 0 
; Random AI modifier or reset ai option
If Rand(5) = 1 Or units(i,13) = 0 Then
units(i,8) = False ; be sure the unit is not fortified
units(i,13) = Rand(8)+1
End If

oldx = units(i,1)
oldy = units(i,2)

; Check if there are non player friendly units nearby
; Only if there is no city present
; 
If checkzoneforenemies(n,units(i,1),units(i,2))=True Then
	; 
	; If the player unit is in sentry then activate the unit
	;
	removesentryinarea(n,units(i,1),units(i,2))
	;
	
	; First decide if we want to attack or fortify
	If Rand(3) = 1 Then
		; here we attack - enenloc = unit 
		enemloc = getenemylocationnumber(i,units(i,1),units(i,2))
		;result = aiattackenemy(i,enemloc)
		result = dobattle(i,getxposition(enemloc),getyposition(enemloc))
			If result = False Then ; if the attacker has lost
				units(i,0) = False
				Else ; if the attacker has won ************
				
				If isunitonenemycity(i,units(enemloc,1),units(enemloc,2))=True Then 
					removeontopunit(units(enemloc,1),units(enemloc,2))
				Else
				; destroy the units
				destroyunitsatposition2(enemloc)
				; move into new position
				units(i,1) = units(enemloc,1)
				units(i,2) = units(enemloc,2)
				; decrease one turn
				End If
				
				unitsmove(i) = unitsmove(i) - 1
				
			End If
		Else
		; here we fortify
		units(i,13) = 9 ; set aimode to fortify
		;units(i,10) = 0 ; set moves to ZERO
		unitsmove(i) = 0
	End If
End If

; This first routine lets the units move in a straight line
If units(i,13) =>1 And units(i,13) =< 8
z = returnmapposition(units(i,1),units(i,2),units(i,13),0)
dx = getxposition(z)
dy = getyposition(z)
If zoneofcontrol(i,dx,dy) = False Then ; if not a zone of control
units(i,1) = getxposition(z)
units(i,2) = getyposition(z)
If roadmap(units(i,1),units(i,2)) = 1 Then
;units(i,10) = units(i,10) - 1
unitsmove(i) = unitsmove(i) - .5
Else
unitsmove(i) = unitsmove(i) - 1
End If
Else
units(i,13) = 9
End If
End If

; If the unit has moved onto a position of multiple unit
placeunitontop(i)
createontopunit(units(i,11),oldx,oldy)

; This piece of code lets the computer player fortify
If units(i,13) = 9 Then
units(i,8) = True
;units(i,10) = 0
unitsmove(i) = 0
End If

;If units(i,10) > 0 Then movesleft = True
If unitsmove(i) > 0 Then movesleft = True
;
If centerscreencheck(units(i,1),units(i,2)) = True Then
centerscreen(units(i,1),units(i,2))
End If
drawminimap:maketempmap:Cls:drawmap:DrawImage mapbuffer,0,0:drawgamescreen:Flip
wacht(200)
;
Wend
End If
End If
End If
Next
Wend
mapx=tempx : mapy=tempy
drawmap:DrawImage mapbuffer,0,0:drawgamescreen:Flip
End Function

;
; Attack all enemies AI method
;
;
;
Function defendplayer(player)
movesleft = False
For i=0 To 1000
If units(i,0) = True ; if the unit is active
If units(i,11) = player ; if the unit is of the player
If unitsmove(i)>0 Then ; are there moves left
;While unitsmove(i) > 0 Then ; keep moving unit until he has no moves left

If Rand(10) = 1 Then ; one in then chance of fortify
units(i,8) = True : unitsmove(i) = 0
End If




If unitsmove(i)>0 Then movesleft = True

End If
End If
End If
Next
End Function




Function getenemylocationnumber(player,x,y)
counter = 0
For y2 = -1 To 1
For x2 = -1 To 1
tx = x+x2
ty = y+y2
If tx>mapwidth Then tx=mapwidth
If tx<0 Then tx = 0
If ty > mapheight Then ty=mapheight
If ty<0 Then ty=0

For i=0 To 1000
	If units(i,0) = True Then ; if the unit is active
	If units(i,3) = True ; if the unit is ontop
	If Not units(i,11) = player Then ; if not of the player
	If units(i,1) = tx Then ; if x is true
	If units(i,2) = ty Then ; if y = true
	enemylocations(counter) = i
	counter=counter+1
	End If
	End If
	End If
	End If
	End If
Next
Next
Next

If counter>0 Then
Return enemylocations(Rand(counter))
Else
Return -1
End If

End Function


;
; This function returns the direction (1/8) where the enemy is
;
;
Function getenemylocationnumber2(player,x,y)
counter = 0
For ii=1 To 8 ; loop thru the eight different zones
q = returnmapposition(x,y,ii,0)
tx = getxposition(q)
ty = getyposition(q)
For i=0 To 1000
	If units(i,0) = True Then ; if the unit is active
	If units(i,3) = True ; if the unit is ontop
	If Not units(i,11) = player Then ; if not of the player
	If units(i,1) = tx Then ; if x is true
	If units(i,2) = ty Then ; if y = true
	enemylocations(counter) = i
	counter=counter+1
	End If
	End If
	End If
	End If
	End If
Next
Next

If counter>0 Then
Return enemylocations(Rand(counter))
Else
Return -1
End If

End Function


;
; This function does battle between the ai and another player
;
;
Function aiattackenemy(attacker,defender)
; get attacking units values
attackstrength# = units(attacker,5) ; what is the attacking units strength
attackveteran = units(attacker,9) ; is it a veteran unit
; get the defending units values
defendstrength# = units(defender,6)
defendveteran = units(defender,9)
defendfortified = units(defender,8)
; take veteran modifier into account
aa#=attackstrength
If attackveteran = True Then a1# = attackstrength/2
aa = aa + a1
;If defendveteran  = True Then d = defendstrength + defendstrength/2
;If defendfortified = True Then d = d + defendstrength/2
dd# = defendstrength
If defendveteran = True Then d1# = defendstrength/2
If defendfortified = True Then d2# = defendstrength/2
If isunitoncity(defender) = True Then d3# = defendstrength/2
If fortressmap(units(defender,1),units(defender,2)) = True Then d5# = defendstrenght/2
dd = dd + d1:dd = dd + d2:dd = dd + d3:dd = dd + d4:dd = dd + d5

;
; here is the formula that does the battle, still have to add difficulty levels

aa# = Rand(aa)
dd# = Rand(dd)


;For i=1 To a
;z# = Rnd(0,a)
;aa# = aa+z
;Next
;For i=1 To d
;z# = Rnd(0,d)
;dd# = dd+z
;Next

; If the attacking unit is not a veteran then his attack stength will
; be decreased every now and then. Maximum 50%
;If attackveteran = False Then
;If Rand(3) = 1 Then
;aa=aa/Rand(50)
;End If
;End If

; Return the outcome

If aa=>dd Then Return True Else Return False


End Function


;
; This function destroys all units at a units location
;
;
Function destroyunitsatposition2(location)
x = units(location,1)
y = units(location,2)
unitdestroyed = 0
For i=0 To maxunits
If units(i,0)=True Then
If units(i,1)=x And units(i,2) = y Then
units(i,0)=False : unitsdestroyed=unitsdestroyed+1
End If
End If
Next
If unitsdestroyed>1 Then 
	t$ = unitsdestroyed + " Units were destroyed"
	simplemessage(t$)
End If
End Function


;
; This function removes the sentry mode in the surrounding area
;
;
Function removesentryinarea(player,x,y)
For ii=1 To 8 ; loop thru the eight different zones
q = returnmapposition(x,y,ii,0)
tx = getxposition(q)
ty = getyposition(q)
For i=0 To maxunits
	If units(i,0) = True Then
	If Not units(i,11) = player Then
	If units(i,1) = tx Then
	If units(i,2) = ty Then
	If units(i,13) = 10 Then units(i,13)=0
	End If
	End If
	End If
	End If
Next
Next
End Function

;
; This function returns a location of a enemy unit in a 8*8 area 
; around the unit, 
;
;
Function returnenemypositioninsquare(player,x,y)
counter = 0 
For i=0 To 1000 ; loop thru thousand units
If units(i,0) = True ; if the unit is active
If units(i,3) = True ; if the unit is ontop
If Not units(i,11)=player ; if not this unit is current player
If Not units(i,14)= True ; if the unit is not inside a city
If warstate(player,units(i,11)) = True Then ; if the factions are at war
If units(i,1)=>x-4 And units(i,1)=<x+4 Then ; if the unit is inside x range
If units(i,2)=>y-4 And units(i,2)=<y+4 Then ; if the unit is inside y range

enemylocations(counter)=i
counter=counter+1
End If
End If
End If
End If
End If
End If
End If
Next

; Return a position where the enemy is
If counter >0 Then
	Return enemylocations(Rand(counter))
Else 
	Return -1
End If

End Function

;
; This function return true if the enemy is near a player city
;
;
Function enemynearplayer(x,y,player)
For i=0 To maxcities
If cities(i,0) = True Then
If cities(i,4) = player Then
If RectsOverlap(cities(i,1)-10,cities(i,2)-10,20,20,x,y,1,1) Then
Return True
End If
End If
End If
Next
For i=0 To maxunits
If units(i,0) = True Then
If units(i,unit_alignment) = player Then 
If RectsOverlap(units(i,unit_x),units(i,unit_y),1,1,x-5,y-5,10,10) = True Then
Return True
End If
End If
End If
Next

End Function

;
; This function copies the isopathfinding map into the temp map
;
Function savepathmap()
For y=0 To 100
For x=0 To 100
temppathmap(x,y) = pathmap(x,y) 
Next
Next
End Function

;
; This function loads from the temporary iso map
;
Function loadpathmap()
For y=0 To 100
For x=0 To 100
pathmap(x,y) = temppathmap(x,y)
Next
Next
End Function

;
;
;
Function createpathstrategy01(player,destx,desty)
For y1=desty-3 To desty+3 Step 3
For x1=destx-3 To destx+3 Step 3
; Check if the x and y stay inside their boundries
If x1>0 And x1<map_width And y1>0 And y1<map_height Then
If returnunitsinarea(player,x1,y1,3,3) > 3 Then
drawblockonpathmap(x1,y1,3,3,9)
End If
End If
Next
Next

End Function

;
; Returns the amount of units in a given area
;
Function returnunitsinarea(player,x,y,w,h)
counter = 0
counter3 = 0
For y1=y To y+h
For x1=x To x+w
If y1>0 And y1<map_height And x1>0 And x1<map_width Then
counter2 = 0
For i=0 To maxunits
If units(i,0) = True Then
If Not units(i,11) = player Then
If units(i,1) = x1 And units(i,2) = y1 Then
counter2 = 1
End If
End If
End If
Next
; increase the main counter by one if a unit is on the current square
If counter2 = 1 Then counter = counter + 1 : counter2 = 0
; Counter to count the total squares in the area
counter3 = counter3 + 1
End If
Next
Next
Return counter
End Function


;
; This function draws a impassible block on the pathmap
;
Function drawblockonpathmap(x,y,w,h,waarde)
For y1=y To y+h
For x1=x To x+w
If x1>0 And x1<map_width And y1>0 And y1<map_height Then
pathmap(x1,y1) = waarde
End If
Next
Next
End Function

;
; Returns the enemy units in the selected area
;
Function returnenemyunitsinarea(player,x,y,w,h)

counter = 0
For x1=x-w/2 To x+w/2
For y1=y-h/2 To y+h/2
	If RectsOverlap(x1,y1,1,1,0,0,mapwidth,mapheight) = True Then
	
		For i=0 To maxunits
			If units(i,0) = True Then
				If Not units(i,unit_incity) = True Then
				If Not units(i,unit_alignment) = player Then
					If warstate(units(i,unit_alignment),player) = True Then
						counter = counter + 1
					End If
				End If
				End If
			End If
		Next
		
	End If
Next
Next

Return counter
End Function








