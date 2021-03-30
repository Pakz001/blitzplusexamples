;
;
; Diplomacy include file - By Rudy 'Nebula' van etten.
;
;
;

Global dvisibleplayer = 1

Global lasttradegameturn
Global lastrequestcity
Global lastrequestgold ; gets updated with the gameturn if triggered, same as the gameturn then no deal
Global lastrequestpeace

Global lastrequestedgold$ = "150"
;
; Dimplomacy window
;
;
Function diplomacy(x,y)

; Switch to a non human player view
For i=1 To 8
	If dvisibleplayer = currentplayer Then dvisibleplayer = i
Next

;w = 600 ; default width
;h = 400 ; default height

; Handle the autocentering if required
If x=-1 Then
x = 800/2-(600/2)
End If

If y=-1 Then
y = 600/2-(400/2)
End If

; Grab the foreground
SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()
FlushKeys():FlushMouse()
gexitwindow = False
While KeyHit(1) = False And gexitwindow = False
Cls
DrawBlock gbackgroundimage,0,0
;
;

; Insert Gui view here >>>>>
gwindow(x,y,600,400,0,0,0)

gtextbar(x+5,y+3,212,26,"Diplomacy",0)
gtextbar(x+11,y+119,172,42,"Warfare",0)
gtextbar(x+411,y+119,172,42,"Offer",0)
gtextbar(x+210,y+119,172,42,"Request",0)

; Show current treaties/status
gtextbar(x+309,y+82,212,26,dstatus(dvisibleplayer),0)

; Show the Currentplayer name
gtextbar(x+93,y+82,212,26,tribesnames$(dvisibleplayer),0)


If gbutton1(x+11,y+243,172,26,"Declare War",1,gbuttonpressed) = True Then gbuttonpressed=1
;gcheckarea(x+11,y+243,172,26)
If gbutton1(x+11,y+275,172,26,"End hostilities",2,gbuttonpressed) = True Then gbuttonpressed=2
;gcheckarea(x+11,y+275,172,26)
; Request gold
If gbutton1(x+209,y+173,172,26,"Gold",6,gbuttonpressed) = True Then gbuttonpressed=6
;gcheckarea(x+209,y+173,172,26)
If gbutton1(x+209,y+207,172,26,"Unit",7,gbuttonpressed) = True Then gbuttonpressed=7
;gcheckarea(x+209,y+207,172,26)
If gbutton1(x+209,y+242,172,26,"Trade",8,gbuttonpressed) = True Then gbuttonpressed=8
;gcheckarea(x+209,y+242,172,26)
If gbutton1(x+209,y+276,172,26,"City",9,gbuttonpressed) = True Then gbuttonpressed=9
;gcheckarea(x+209,y+276,172,26)
If gbutton1(x+209,y+310,172,26,"Remove Units",10,gbuttonpressed) = True Then gbuttonpressed=10
;gcheckarea(x+209,y+310,172,26)
If gbutton1(x+220,y+4,50,26,"Int",11,gbuttonpressed) = True Then gbuttonpressed=11
;gcheckarea(x+220,y+4,50,26)
If gbutton4(x+570,y+2,Big_close_button,13,gbuttonpressed) = True Then gbuttonpressed = 13
;gcheckarea(x+570,y+2,25,22)
If gbutton1(x+499,y+366,92,27,"Ok",14,gbuttonpressed) = True Then gbuttonpressed=14
;gcheckarea(x+499,y+366,92,27)
;If gbutton1(x+401,y+366,92,27,"Cancel",15,gbuttonpressed) = True Then gbuttonpressed=15
;gcheckarea(x+401,y+366,92,27)
If gbutton1(x+11,y+172,172,26,"Request Alliance",16,gbuttonpressed) = True Then gbuttonpressed=16
;gcheckarea(x+11,y+172,172,26)
If gbutton1(x+11,y+208,172,26,"Request Assistance",17,gbuttonpressed) = True Then gbuttonpressed=17
;gcheckarea(x+11,y+208,172,26)
; Offer gold
If gbutton1(x+411,y+173,172,26,"Gold",18,gbuttonpressed) = True Then gbuttonpressed=18
;gcheckarea(x+411,y+173,172,26)
If gbutton1(x+411,y+207,172,26,"Unit",19,gbuttonpressed) = True Then gbuttonpressed=19
;gcheckarea(x+411,y+207,172,26)
; Offer trade
If gbutton1(x+411,y+242,172,26,"Trade",20,gbuttonpressed) = True Then gbuttonpressed=20
;gcheckarea(x+411,y+242,172,26)
If gbutton1(x+411,y+276,172,26,"City",21,gbuttonpressed) = True Then gbuttonpressed=21
;gcheckarea(x+411,y+276,172,26)
If gbutton1(x+411,y+310,172,26,"Remove Units",22,gbuttonpressed) = True Then gbuttonpressed=22
;gcheckarea(x+411,y+310,172,26)
If gbutton1(x+94,y+64,50,16,"P1",23,gbuttonpressed) = True Then gbuttonpressed=23
;gcheckarea(x+94,y+64,50,16)
If gbutton1(x+148,y+64,50,16,"P2",24,gbuttonpressed) = True Then gbuttonpressed=24
;gcheckarea(x+148,y+64,50,16)
If gbutton1(x+201,y+64,50,16,"P3",25,gbuttonpressed) = True Then gbuttonpressed=25
;gcheckarea(x+201,y+64,50,16)
If gbutton1(x+255,y+64,50,16,"P4",26,gbuttonpressed) = True Then gbuttonpressed=26
;gcheckarea(x+255,y+64,50,16)
If gbutton1(x+310,y+64,50,16,"P5",27,gbuttonpressed) = True Then gbuttonpressed=27
;gcheckarea(x+310,y+64,50,16)
If gbutton1(x+364,y+64,50,16,"P6",28,gbuttonpressed) = True Then gbuttonpressed=28
;gcheckarea(x+364,y+64,50,16)
If gbutton1(x+417,y+64,50,16,"P7",29,gbuttonpressed) = True Then gbuttonpressed=29
;gcheckarea(x+417,y+64,50,16)
If gbutton1(x+471,y+64,50,16,"P8",30,gbuttonpressed) = True Then gbuttonpressed=30
;gcheckarea(x+471,y+64,50,16)
; End Gui interface insertion <<<<<<
;

; Tooltip hover over textarea's
If gcheckarea(x+93,y+82,212,26) = True Then
	showtooltip(100+dvisibleplayer)
End If


; Execute the buttons
If MouseDown(1) = False And gbuttonpressed > 0 Then

Select gbuttonpressed
	;
	
	Case 13 ; Close Button
	If gcheckarea(x+570,y+2,25,22) = True Then
		gexitwindow = True
	End If
	;
	Case 14 ; Ok Button
	If gcheckarea(x+499,y+366,92,27) = True Then
		gexitwindow = True
	End If
	;
	Case 1; Declare war
	If gcheckarea(x+11,y+243,172,26) = True Then
		If warstate(dvisibleplayer,currentplayer) = False Then
			; Set war state to true
			warstate(dvisibleplayer,currentplayer) = True
			warstate(currentplayer,dvisibleplayer) = True
			; Disable trade treaty
			tradestatus(dvisibleplayer,currentplayer) = False
			tradestatus(currentplayer,dvisibleplayer) = False
			; Update relations
			playerrelations(dvisibleplayer,currentplayer,0) = 0 ; friendly
			playerrelations(dvisibleplayer,currentplayer,1) = playerrelations(dvisibleplayer,currentplayer,1) / 2 ; trust
			; Update the fear
			updateplayerfear(dvisibleplayer,currentplayer)
			updateplayerfear(currentplayer,dvisibleplayer)
			Else
			Simplemessage("Already at War")
		End If
	End If
	Case 2 ; End hostilities
	If gcheckarea(x+11,y+275,172,26) = True Then
		If warstate(dvisibleplayer,currentplayer) = True Then
			If returnplayeranswer(dvisibleplayer,currentplayer) > 5 And lastrequestpeace <> gameturn Then
				warstate(dvisibleplayer,currentplayer) = False
				warstate(currentplayer,dvisibleplayer) = False
				Else
				a = Rand(1,3)
				If a = 1 Then simplemessage("Do you truly believe we want that?")
				If a = 2 Then simplemessage("Not at this Time")
				If a = 3 Then simplemessage("No")
			End If
			lastrequestpeace = gameturn
		Else
			simplemessage("Already at Peace")
		End If
	End If

	Case 6 ; Request gold
	If gcheckarea(x+209,y+173,172,26) = True Then
		If lastrequestgold <> gameturn Then
			a = ginputbox$(screenwidth/2-400/2,screenheight/2-200/2,400,200,"Request Gold",lastrequestedgold,"Ok")
			If a>0 And a<50000 Then
				If a<playergold(dvisibleplayer)/4 Then
					If returnplayeranswer(dvisibleplayer,currentplayer) > 8 Then
						; Give the gold
						playergold(currentplayer) = playergold(currentplayer) + a
						playergold(dvisibleplayer) = playergold(Dvisibleplayer) - a
						;
						updaterelationfriendly(dvisibleplayer,currentplayer,-10)
						updaterelationtrust(dvisibleplayer,currentplayer,-5)
						Simplemessage(a + " Gold has been transferred")
						Else simplemessage("Not a chance")
					End If
					Else simplemessage("No sufficient funds")
				End If
				lastrequestgold = gameturn
			End If 
		Else
		simplemessage("Cannot ask more then once per time")		
		End If
	End If
	Case 8 ; Offer Trade
	If gcheckarea(x+209,y+242,172,26) = True Then
		If warstate(dvisibleplayer,currentplayer) = False Then
			If lasttradegameturn <> gameturn And Rand(1,10) = True Then
				tradestatus(dvisibleplayer,currentplayer) = True
				tradestatus(currentplayer,dvisibleplayer) = True
				updaterelationfriendly(dvisibleplayer,player,10)
				lastgameturn = gameturn
			Else
				lasttradegameturn = gameturn
			End If
			Else
			Simplemessage("Trade not possible - Make peace first")
		End If
	End If
	Case 9 ; Request city
	If gcheckarea(x+209,y+276,172,26) = True Then
		If returnplayeranswer > 8 And lastrequestcity <> gameturn
			counter = 0
			; Move cities into the listbox fields
			For i=0 To returnnumcities(dvisibleplayer)
				counter = getnextcitynum(counter,dvisibleplayer)
				If counter = -1 Then Exit
				glistfield$(i) = citiesstring$(counter,0)
				glistfieldint(i) = counter
			Next
			; Ask which city
			a = glistbox(800/2-320/2,600/2-400/2,320,400,"Select City",returnnumcities(dvisibleplayer))
			
			; Move the city to players control	
			If a>-1 And returnplayeranswer(dvisibleplayer,currentplayer) > 9 Then
				cities(glistfieldint(a),city_alignment) = currentplayer
				simplemessage("Control of the city has been tranfered to you")
				Else
				simplemessage("Not a chance")
			End If
			
		Else
			lastrequestcity = gameturn
			simplemessage("No way")
		End If
	End If
	Case 18 ; Offer gold
	If gcheckarea(x+411,y+173,172,26) = True Then
		a = ginputbox$(screenwidth/2-400/2,screenheight/2-200/2,400,200,"Offer Gold",lastrequestedgold,"Ok")
		If a>0 And a<50000 Then
			If a<playergold(currentplayer) Then
				; Give the gold
				playergold(dvisibleplayer) = playergold(dvisibleplayer) + a
				playergold(currentplayer) = playergold(currentplayer) - a
				; Update the friendly status between the players
				If a > playergold(currentplayer) Then
					updaterelationfriendly(dvisibleplayer,currentplayer,15)
				ElseIf a > playergold(currentplayer)/2
					updaterelationfriendly(dvisibleplayer,currentplayer,8)
				ElseIf a > playergold(currentplayer)/3
					updaterelationfriendly(dvisibleplayer,currentplayer,5)
				Else
					updaterelationfriendly(dvisibleplayer,currentplayer,1)
				End If
				
				;
				Simplemessage(a + "Your gold has been transferred")
				Else simplemessage("Not enough gold in your treasury")
			End If
		Else simplemessage("To much gold offered (50000 max)")
		End If 
	End If
	Case 20 ; Offer Trade
	If gcheckarea(x+411,y+242,172,26) = True Then
		If warstate(dvisibleplayer,currentplayer) = False Then
			If lasttradegameturn <> gameturn And returnplayeranswer(dvisibleplayer,currentplayer) > 5 Then
				tradestatus(dvisibleplayer,currentplayer) = True
				tradestatus(currentplayer,dvisibleplayer) = True
				lastgameturn = gameturn
				updaterelationfriendly(dvisibleplayer,currentplayer,10)
			Else
				lasttradegameturn = gameturn
			End If
			Else
			Simplemessage("Trade not possible - Make peace first")
		End If
	End If
	
	Case 21 ; Offer city
	If gcheckarea(x+411,y+276,172,26) = True Then
		If Not returnnumcities(currentplayer) < 2 Then 
			counter = 0
			; Move cities into the listbox fields
			For i=0 To returnnumcities(currentplayer)
				counter = getnextcitynum(counter,currentplayer)
				If counter = -1 Then Exit
				glistfield$(i) = citiesstring$(counter,0)
				glistfieldint(i) = counter
			Next
			; Ask which city
			a = glistbox(800/2-320/2,600/2-400/2,320,400,"Select City",returnnumcities(currentplayer))
			
			; Move the city to players control	
			If a>-1 Then
				cities(glistfieldint(a),city_alignment) = dvisibleplayer
				simplemessage("Control of the city has been tranfered to the player")
				updaterelationfriendly(dvisibleplayer,currentplayer,15)
				updaterelationtrust(dvisibleplayer,currentplayer,5)
			End If
		Else
		simplemessage("You cannot give away your last city")				
		End If
	End If
	

	;
	Case 23 ; Player 1
	If gcheckarea(x+94,y+64,50,16) = True Then
		If Not currentplayer = 1 Then dvisibleplayer = 1 : playdamusic(6) ; player 1
	End If
	Case 24 ; Player 2
	If gcheckarea(x+148,y+64,50,16)	= True Then
		If Not currentplayer = 2 Then dvisibleplayer = 2 : playdamusic(7) ; player 2
	End If
	Case 25 ; Player 3
	If gcheckarea(x+201,y+64,50,16)	= True Then
		If Not currentplayer = 3 Then dvisibleplayer = 3 : playdamusic(8) ; player 3
	End If
	Case 26 ; Player 4
	If gcheckarea(x+255,y+64,50,16)	= True Then
		If Not currentplayer = 4 Then dvisibleplayer = 4 : playdamusic(9) ; player 4
	End If
	Case 27 ; Player 5
	If gcheckarea(x+310,y+64,50,16)	= True Then
		If Not currentplayer = 5 Then dvisibleplayer = 5 : playdamusic(10) ; player 5
	End If
	Case 28 ; Player 6
	If gcheckarea(x+364,y+64,50,16)	= True Then
		If Not currentplayer = 6 Then dvisibleplayer = 6 : playdamusic(11) ; player 6
	End If
	Case 29 ; Player 7
	If gcheckarea(x+417,y+64,50,16)	= True Then 
		If Not currentplayer = 7 Then dvisibleplayer = 7 : playdamusic(12) ; player 7
	End If
	Case 30 ; Player 8
	If gcheckarea(x+471,y+64,50,16)	= True Then
		If Not currentplayer = 8 Then dvisibleplayer = 8 : playdamusic(13) ; player 8
	End If
	;
End Select
;
gbuttonpressed = 0
End If



; Draw the mouse (not implented yet)
;Rect MouseX(),MouseY(),10,10,1
DrawImage mousepointer,MouseX(),MouseY()
Flip
Wend

FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
playdamusic(0) : musicplayingtimer = 0
End Function

;
; Returns the current status as a String$
;
Function dstatus$(player)
	a$ = ""
	If warstate(player,currentplayer) = True Then
	a$ = a$ + "At War"
	Else
	a$ = a$ + "At Peace"
	End If
	
	If tradestatus(player,currentplayer) = True Then
	a$ = a$ + " / Trade"
	Else
	a$ = a$ + " / No Trade"
	End If
	Return a$
End Function

;
; This function returns the number of cities a player has
;
Function returnnumcities(player)
counter = 0
For i=0 To maxcities
	If cities(i,0) = True Then
		If cities(i,city_alignment) = player Then
			counter = counter + 1
		End If
	End If
Next
Return counter
End Function


;
; Returns the next number of the city of the selected player
;
Function getnextcitynum(num,player)
If num+1 > maxcities Then Return -1
For i=num+1 To maxcities
	If cities(i,0) = True Then
		If cities(i,city_alignment) = player
			Return i
		End If
	End If
Next
Return -1
End Function

;
; This function returns if the status between players is good enough (0 to 10)
;
Function returnplayeranswer(player,destplayer)
a = 0
If warstate(player,destplayer) = False Then
	If playerrelations(player,destplayer,relationfriendly) > 50 Then ; friendly
		a=a+playerrelations(player,destplayer,relationfriendly) / 20
	End If
	If playerrelations(player,destplayer,relationtrust) > 50 Then ; Trust
		a=a+playerrelations(player,destplayer,releationtrust) / 20
	End If
	;
	Else
	;
	If playerrelations(player,destplayer,relationfear) > 50 Then ; fear
		a=a+playerrelations(player,destplayer,relationfear) / 10
	End If
End If
Return a

End Function

;
; Sets the fear a player has for another player - used in diplomacy
;
;
Function updateplayerfear(player,destplayer)

a = getplayerunits(player)
b = getplayerunits(destplayer)
c = returnnumcities(player)
d = returnnumcities(destplayer)

;
If a/4 > b And c/4 > d Then
	playerrelations(player,destplayer,2) = 100 ; full fear
	Return
End If
If a/3 > b And c/3 > d Then
	playerrelations(player,destplayer,2) = 90 ; Less Fear
	Return
End If
If a/2 > b And c/2 > d Then
	playerrelations(player,destplayer,2) = 80 ; some fear
	Return
End If
If a/4 > b Or c/4 > d Then
	playerrelations(player,destplayer,2) = 70 ; Normal fear
	Return True
End If
If a/3 > b Or c/3 > d Then
	playerrelations(player,destplayer,2) = 60 ; Less then normal fear
	Return
End If
If a/2 > b Or c/2 > d Then
	playerrelations(player,destplayer,2) = 50 ; Avarage fear
	Return
End If

 ;   --------------

If b/4 > a And d/4 > c Then
	playerrelations(player,destplayer,2) = 0 ; full fear
	Return
End If
If b/3 > a And d/3 > c Then
	playerrelations(player,destplayer,2) = 20 ; Less Fear
	Return
End If
If b/2 > a And d/2 > c Then
	playerrelations(player,destplayer,2) = 30 ; some fear
	Return
End If
If b/4 > a Or d/4 > c Then
	playerrelations(player,destplayer,2) = 20 ; Normal fear
	Return
End If
If b/3 > a Or d/3 > c Then
	playerrelations(player,destplayer,2) = 30 ; Less then normal fear
	Return
End If
If b/2 > a Or d/2 > c Then
	playerrelations(player,destplayer,2) = 40 ; Avarage fear
	Return
End If



playerrelations(player,destplayer,2) = 50


End Function

;
; Update the relationsship friendly category
;
Function updaterelationfriendly(player,destplayer,num) ; num is the amount to change
playerrelations(player,destplayer,relationfriendly) = playerrelations(player,destplayer,relationfriendly) + num
If playerrelations(player,destplayer,relationfriendly) > 100 Then playerrelations(player,destplayer,relationfriendly) = 100
If playerrelations(player,destplayer,relationfriendly) < 0 Then playerrelations(player,destplayer,relationfriendly) = 0

End Function



;
; Update the trust between the players
;
Function updaterelationTrust(polayer,destplayer,num)
playerrelations(player,destplayer,relationtrust) = playerrelations(player,destplayer,relationtrust) + num
If playerrelations(player,destplayer,relationtrust) > 100 Then playerrelations(player,destplayer,relationtrust) = 100
If playerrelations(player,destplayer,relationtrust) < 0 Then playerrelations(player,destplayer,relationtrust) = 0

End Function

Function readdiplomacyconfig()

End Function







