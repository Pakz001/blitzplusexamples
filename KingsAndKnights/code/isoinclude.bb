; Include for isoturnbased

;
; This function checks if there are enemy units in a the direct surounding area 
;
; A little bit slow - sorry
Function checkzoneforenemies(player,x,y)

For ii=1 To 8 ; loop thru the eight different zones
q = returnmapposition(x,y,ii,0)
tx = getxposition(q)
ty = getyposition(q)
For i=0 To 1000
	If units(i,0) = True Then ; is the unit active
	If Not units(i,11) = player Then ; is the unit not our player
	If units(i,1) = tx Then ; is he present on this x
	If units(i,2) = ty Then ; is he present on this y
;	fortressmap(tx,ty)=0
	If warstate(player,units(i,11)) = True Then ; are the factions at war
	Return True
	End If
	End If
	End If
	End If
	End If
Next
Next
End Function

;
; This function updates the mapdisplay
;
;
Function updatescreen()
maketempmap:drawminimap:drawmap
DrawImage mapbuffer,0,0
drawgamescreen ; draw the righter game screen
End Function

;
; This function return if a unit moves against a enemy city 
;
;
Function isunitonenemycity(unit,x,y)
unitplayer = units(unit,11) ; move the player number in
;
For i=0 To maxcities
If cities(i,0) = True Then
If Not cities(i,4) = unitplayer
If x = cities(i,1) Then
If y = cities(i,2) Then
Return True
End If
End If
End If
End If
Next

Return False
End Function

;
; This function return the city number on a x,y location
;
;
Function getcitynumber(x,y)
For i=0 To maxcities
If cities(i,0)=True Then
If x = cities(i,1) Then
If y = cities(i,2) Then
Return i
End If
End If
End If
Next
Return -1
End Function

;
; This function gets the ontop or below units number on a x,y location
;
Function getunitnumber(x,y)
Local otherreturnvalue = -1
For i=0 To maxunits
If units(i,0) = True Then
If units(i,1) = x And units(i,2) = y Then
If units(i,unit_ontop) = True Then Return i
otherreturnvalue = i
End If
End If
Next
If otherreturnvalue = -1 Then Return False
Return i
End Function


;
; This function removes the ontop unit
;
;
Function removeontopunit(x,y)
For i=0 To maxunits
If units(i,0)=True Then
If units(i,3) = True Then
If x= units(i,1) Then
If y= units(i,2) Then
units(i,0) = False
End If
End If
End If
End If
Next

setstrongdefenderontop(x,y)

End Function

;
; This function set the strongest defender on top
;
;
Function setstrongdefenderontop(x,y)
counter = 0
For i=0 To maxunits
If units(i,0) = True Then
If x = units(i,1) Then
If y = units(i,2) Then
units(i,3) = False
values(counter,0) = i
values(counter,1) = units(i,6)
counter = counter + 1
End If
End If
End If
Next

v = 0 : counter2 = 0
For i=0 To counter-1
If values(i,1) > v Then v = values(i,1) : counter2 = i
Next

units(values(counter2,0),3) = True

End Function



;
; This function returns if a position is a zone of control
;
; 
;
Function zoneofcontrol(unit,x,y)
; First check the eight surrounding positions around the current unit location
For i=1 To 8
	dest1 = returnmapposition(units(unit,1),units(unit,2),i,1)
	; If a enemy unit is present in one of these squares
	If checkforenemies(unit,getxposition(dest1),getyposition(dest1))=True Then
		; Loop thru eight surrounding positions of the enemy location
		For ii=1 To 8
			dest2 = returnmapposition(getxposition(dest1),getyposition(dest1),ii,1)
			; If the new position is a zone of control then return true
			If getxposition(dest2) = x Then
				If getyposition(dest2) = y Then
					If isthereanyunitpresent(getxposition(dest2),getyposition(dest2))=False
						Return True
					End If
				End If
			End If
		Next
	End If
Next
Return False
End Function

;
; This function returns if a x,y position is a zone of control
;
; 
;
Function zoneofcontrol2(unit,x,y)
; First check the eight surrounding positions around the current unit location
For i=1 To 8
	dest1 = returnmapposition(x,y,i,1)
	; If a enemy unit is present in one of these squares
	If checkforenemies(unit,getxposition(dest1),getyposition(dest1))=True Then
		; Loop thru eight surrounding positions of the enemy location
		For ii=1 To 8
			dest2 = returnmapposition(getxposition(dest1),getyposition(dest1),ii,1)
			; If the new position is a zone of control then return true
			If getxposition(dest2) = x Then
				If getyposition(dest2) = y Then
					If isthereanyunitpresent(getxposition(dest2),getyposition(dest2))=False
						Return True
					End If
				End If
			End If
		Next
	End If
Next
Return False
End Function



;
; Returns if there is any unit present
;
Function isthereanyunitpresent(x,y)
For i=0 To maxunits
If units(i,0) = True Then
If units(i,1) = x Then
If units(i,2) = y Then
Return True 
End If
End If
End If
Next
Return False
End Function

;
; returns if a player of is on location x,y
;
;
;
;
Function isthereanyplayerpresent(player,x,y)
For i=0 To maxunits
If units(i,0) = True Then
If units(i,1) = x Then
If units(i,2) = y Then
If Not units(i,11) = player Then
Return True 
End If
End If
End If
End If
Next
Return False
End Function

;
; This unit returns true if a unit in on a city 
;
;
Function isunitoncity(n)
For i=0 To maxcities
If cities(i,0) = True
If units(n,1) = cities(i,1) And units(n,2) = cities(i,2) Then Return True
End If
Next
Return False
End Function

Function countunits(player,x,y)
counter = 0
For i=0 To maxunits
If units(i,0) = True Then
If units(i,unit_x) = x
If units(i,unit_y) = y
If units(i,unit_alignment) = player Then
counter = counter + 1
End If
End If
End If
End If
Next
Return counter
End Function
;
; Returns the city id of the city on x,y - , -1 if no city present
;
Function returncityid(x,y)
For i=0 To maxcities
If cities(i,0) = True Then
If cities(i,city_x) = True Then
If cities(i,city_y) = True Then
Return i
End If
End If
End If
Next
Return -1
End Function

Function isunitonboat(unit)
For i=0 To maxunits
If units(i,0) =True Then
If units(i,unit_x) = units(unit,unit_x) Then
If units(i,unit_y) = units(unit,unit_y) Then
If units(i,unit_cancarry) > 0 Then
Return True
End If
End If
End If
End If
Next
End Function

;
; This function returns the map location 
;
; 1 - up
; 2 - right
; 3 - down
; 4 - left
; 5 - up/right
; 6 - right/down
; 7 - left/down
; 8 - up/left
;  
Function returnmapposition(x,y,d,method)
Select d
Case 1
x=x-1:y=y-1
Case 5
y=y-1
Case 2
x=x+1:y=y-1
Case 6
x=x+1
Case 3
y=y+1:x=x+1
Case 7
y=y+1
Case 4
y=y+1
x=x-1
Case 8
x=x-1
End Select

If method=0 Then
; Keep the unit inside the map
If x > mapwidth Then x = mapwidth
If y > mapheight Then y = mapheight
If x < 1 Then x = 1
If y < 1 Then y = 1
End If

Return (y*mapwidth)+x

End Function

;
; This function returns the xvalue from a combined x*y coordinate
;
;
;
Function getxposition(v)
y = v / mapwidth
x = v - y * mapwidth
Return x
End Function
;
; This function returns the yvalue from a combined x*y coordinate
;
;
;
Function getyposition(v)
y = v / mapwidth
x = v - y * mapwidth
Return y
End Function

;
; This function checks if there are multiple units below a x,y position
;
;
Function checkformultipleunits(x,y)
counter = 0
For i=0 To maxunits
If units(i,0) = True Then
If units(i,1) = x Then
If units(i,2) = y Then
counter = counter + 1
End If
End If
End If
Next

Return counter
End Function


;
; This function will make a victorious veteran if. This with a chance from 1 of 3
;
;
Function makehumanunitveteran()
If Rand(3) = 1 Then
	If currentunit(2) > -1 Then
	If units(currentunit(2),9) = False Then
		units(currentunit(2),9) = True
		simplemessage("This unit has become a veteran")
	End If
	End If
End If
End Function

;
; This function will make a defending unit a veteran. This with a chance of 1 to 3
;
;
Function makedefendingunitveteran(x,y)
If Rand(3) = 1 Then
	n = getunitnumber(x,y)
	If units(n,9) = False
		units(n,9) = True
		simplemessage("The defending unit has become a veteran")
	End If
End If
End Function

;
; This function displays all units at a its x,y position
;
;
Function showunitsatcursor(x,y)

;If cities(getcitynumber(x,y),city_alignment) > 0 Then
;If cities(getcitynumber(x,y),city_alignment) <> currentplayer Then Return
;End If

For i=0 To maxunits
If units(i,0) = True Then
If units(i,unit_x) = x And units(i,unit_y) = y Then
If units(i,unit_alignement) <> currentplayer Then Return
End If
End If
Next

For i=0 To maxcities
If cities(i,0) = True Then
If cities(i,city_x) = x And cities(i,city_y) = y Then
If cities(i,city_alignement) <> currentplayer Then Return
End If
End If
Next


; If the goto mode is active then exit this function
If gotomodeactive = True Then Return

Delay(200) : FlushMouse()
; First empty the array
For i=0 To maxunits
For ii=0 To 3
tempunits(i,ii) = -1
Next
Next
; Move all units in the temp array
counter = 0
For i=0 To maxunits
If units(i,0) = True Then
If x = units(i,1) And y = units(i,2)
tempunits(counter,0) = i
counter = counter + 1
End If
End If
Next
snumunits = counter 
; If there is only one unit to select
If counter = 1 Then Return
Local listcursor = 0
Local wx = screenwidth/2-150
Local wy = screenheight/2-150
Local wwidth = 300
Local wheight = 320
Local exitloop = False
;
While KeyHit(1) = False And exitloop=False And moused(2) = False
	we = WaitEvent()
	Select we
		Case $101 	;- Key down 
		Case $102 	;- Key up
		If EventData() = 1 Then exitloop=True
		Case $103 	;- Key stroke 
		Case $201 	;- Mouse down
		moused(EventData()) = True	
		Case $202 	;- Mouse up
		moused(EventData()) = False
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
			donotupdate = True		
		Case $2002 	;- App resume
			donotupdate = False
			moused(1) = False:moused(2) = False:moused(3) = False
		Case $2002 	;- App Display Change 	
		Case $2003 	;- App Display Change 
		Case $2004 ;- App Begin Modal	
		Case $2005 ;- App End Modal	
		Case $4001	;- Timer tick;
		If donotupdate = False Then			
			Cls
			doflickertimer ; Do the flicker	
			;drawmap ; first draw the map
			DrawImage mapbuffer,0,0
			drawgamescreen ; draw the righter game screen
			dominimap ; draw the minimap
			;
			gwindow(wx,wy,wwidth,wheight,0,0,0)
			gwindow(wx+3,wy+3,wwidth-32,wheight-6,0,1,1)
			;
			;If gbutton1(wx+wwidth-30,wy+10,32-4,64,"",1,suacbutton) = True Then suacbutton = 1
			;If gbutton1(wx+wwidth-30,wy+wheight-64-10,32-4,64,"",2,suacbutton) = True Then suacbutton = 2
		
			If gbutton4(wx+wwidth-25,wy+10,small_up_arrow,1,suacbutton) = True Then suacbutton = 1
			If gbutton4(wx+wwidth-25,wy+wheight-64-10,small_down_arrow,2,suacbutton) = True Then suacbutton = 2
			If gbutton4(wx+6,wy+6,big_close_button,6,suacbutton) = True Then suacbutton = 6
			
			; Graphical buttons
			;DrawImage buttons(5),wx+10,wy+10
			; If we press on the exit icon then exit the loop
			;If moused(1) =True Then
			;	If RectsOverlap(MouseX(),MouseY(),1,1,wx+10,wy+10,10,10) = True Then
			;		exitloop = True
			;	End If
			;End If
		
		If units(tempunits(0,0),unit_alignment) = currentplayer Then
		
			; Three stack commands
			If gbutton1(wx+15,wy+wheight-17-10,76,20,"Act. All",3,suacbutton) = True Then suacbutton = 3
			If gbutton1(wx+15+76,wy+wheight-17-10,76,20,"Fort. All",4,suacbutton) = True Then suacbutton = 4
			If gbutton1(wx+15+76+76,wy+wheight-17-10,76,20,"Sent. All",5,suacbutton) = True Then suacbutton = 5	
		
		End If
		
			; Operate the buttons
			If moused(1) = True And suacbutton > 0 Then
				If gcheckarea(wx+wwidth-25,wy+10,16,16) And suacbutton = 1 Then
					If listcursor > 0 Then listcursor = listcursor - 1 : Delay(150)
				End If
				If gcheckarea(wx+wwidth-25,wy+wheight-64-10,16,16) And suacbutton = 2 Then
					If listcursor+5 < counter Then listcursor = listcursor + 1 : Delay(150)
				End If	
				
				; Close button
				If gcheckarea(wx+6,wy+6,32,32) = True And suacbutton = 6 Then
					;If unitsmove(tempunits(y2-1,0)) => 0.33 Then
						exitloop=True
					;End If
				End If
				
				; Activate all units
				If gcheckarea(wx+15,wy+wheight-17-10,76,20) And suacbutton = 3 Then
					For i=0 To maxunits
						If units(i,0) = True Then
						If units(i,1) = x Then
						If units(i,2) = y Then
						If unitsmove(i) =>0.33 Then
						If Not units(i,13) = 1 Then
						If Not units(i,13) = 2 Then
						If Not units(i,13) = 6 Then
							units(i,13) = False
							units(i,unit_fortified) = False
							findnextmovableunit(currentplayer)
						End If
						End If
						End If
						End If
						End If
						End If
						End If
					Next
				End If
				; Fortify all units
				If gcheckarea(wx+15+76,wy+wheight-17-10,76,20) And suacbutton = 4 Then
					For i=0 To maxunits
						If units(i,0) = True Then
						If units(i,1) = x Then
						If units(i,2) = y Then
						If unitsmove(i) =>0.33 Then
						If Not units(i,13) = 1 Then				
						If Not units(i,13) = 2 Then
						If Not units(i,13) = 6 Then
							units(i,13) = False
							units(i,unit_fortified) = True					
							setstrongdefenderontop(x,y)
							findnextmovableunit(currentplayer)
						End If
						End If
						End If
						End If
						End If
						End If
						End If
					Next
		
				End If
				; Sentry all units
				If gcheckarea(wx+15+76+76,wy+wheight-17-10,76,20) And suacbutton = 5 Then
					For i=0 To maxunits
						If units(i,0) = True Then
						If units(i,1) = x Then
						If units(i,2) = y Then
						If unitsmove(i) =>0.33 Then
						If Not units(i,13) = 1 Then
						If Not units(i,13) = 2 Then
						If Not units(i,13) = 6 Then
							units(i,13) = 10
							units(i,unit_fortified) = False
							setstrongdefenderontop(x,y)
							findnextmovableunit(currentplayer)
						End If
						End If
						End If
						End If
						End If
						End If
						End If
					Next
		
				End If
				
			suacbutton = 0
			End If
			
			SetFont fonts(19)
			Color 0,0,0
			For xz1=-1 To 1
			For yz1=-1 To 1
				Text wx+6+64+xz1,wy+4+yz1,snumunits + " Units in the stack"
			Next
			Next	
			Color 255,255,255
			Text wx+6+64,wy+4,snumunits + " Units in the stack"
			
			; Draw the units
		
			y2 = listcursor
			For y3=0 To 4
				y1 = (y3*50)+16
				If tempunits(y2,0) > -1 Then
				; Draw the image and info
				unitnum = units(tempunits(y2,0),unit_type)
				DrawImage unitgraphics(units(tempunits(y2,0),unit_type)-1),wx+6,wy+4+y1
				Color 255,255,255
				; Draw the text info
				SetFont fonts(14)
				
				; Load in the city name
				If units(tempunits(y2,0),unit_homecity) > -1 Then
				cname$ = citiesstring$(units(tempunits(y2,0),unit_homecity),0)
				Else
				cname$ = "<None>"
				End If
		
				
				; First line
				Color 255,255,255
				Text2 wx+6+64,wy+y1+9,unitsname$(units(tempunits(y2,0),unit_type))+" ( A"+unitdefault(unitnum,0)+"/D"+unitdefault(unitnum,1)+"/M"+unitdefault(unitnum,2)+")"
				; second line
				Text2 wx+6+64,wy+y1+19+9,"Homecity : " + cname$
				SetFont fonts(12)
				Text2 wx+8,wy+y1+19+9,Right("0"+(y2+1),2)
				
				If units(tempunits(y2,0),unit_fortified) = True Then
					DrawImage fortified,wx+48,wy+4+10+y1
				End If
				If units(tempunits(y2,0),13) = 10 Then
					DrawImage sentry,wx+48,wy+4+10+y1
				End If
				If units(tempunits(y2,0),13) = 6 Then
					DrawImage building,wx+48,wy+4+10+y1
				End If
		
				End If
				y2=y2+1
				;
				; Exit if the mouse = pressed on a unit
				If moused(1) = True Then
				If RectsOverlap(MouseX(),MouseY(),1,1,wx+6,wy+4+y1,64,48) = True Then
				If unitsmove(tempunits(y2-1,0)) => 0.33 Then
					unitnum = tempunits(y2-1,0)
					currentunit(2) = tempunits(y2-1,0)
					currentunit(0) = units(tempunits(y2-1,0),1)
					currentunit(1) = units(tempunits(y2-1,0),2)
					lastunitstartx = currentunit(0)
					lastunitstarty = currentunit(1)
					units(unitnum,unit_fortified) = False 
					;units(unitnum,unit_active) = False
					units(unitnum,unit_onhold) = False
					placeunitontop(tempunits(y2-1,0))
					; remove sentry status
					units(tempunits(y2-1,0),13) = False 
					maketempmap()
					drawminimap()
					exitloop=True
				End If
				End If
				End If
		
			Next
			;
			DrawImage mousepointer,MouseX(),MouseY() ; lastly draw the mousepointer
			Flip
			; make a screenshot if this feature is enabled and the 1 key is pressed
			If screenshotsenabled=True And KeyHit(2) = True Then SaveBuffer(FrontBuffer(),"c:\windows\desktop\screenshot.bmp"):End		
	End If
	End Select
Wend
Delay(150)
FlushMouse():FlushKeys()
End Function

;
; Function that displayes all the units on the square
;
;
Function showunitsatcursor2(x,y)
; temparray contents
; 0 = units number / 1 = active or not active / 2 = unit type



wacht(200) : FlushMouse()
; First empty the array
For i=0 To 300
For ii=0 To 3
tempunits(i,ii)=False
Next
Next
; Move all units at x,y location into the temp array
counter = 0
For i=0 To maxunits
If units(i,0) = True Then
If x = units(i,1) Then
If y = units(i,2) Then
tempunits(counter,0) = i ; move then number in
;tempunits(counter,1) = units(i,10) ; move the moves in
tempunits(counter,1) = unitsmove(i) ; move the moves in
tempunits(counter,2) = units(i,4) ; move the type in
tempunits(counter,3) = units(i,8) ; move fortified status in
counter = counter + 1
End If
End If
End If
Next
; Here we check if there are actually multiple units on this position
If counter<2 Then Goto suakskipout2

; Here is the window's main loop
While KeyDown(1) = False

we = WaitEvent()

Select we
	Case $101 	;- Key down 
	Case $102 	;- Key up
	If EventData() = 1 Then Exit
	Case $103 	;- Key stroke 
	Case $201 	;- Mouse down
	moused(EventData()) = True	
	Case $202 	;- Mouse up
	moused(EventData()) = False
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
		donotupdate = True		
	Case $2002 	;- App resume
		donotupdate = False
		moused(1) = False:moused(2) = False:moused(3) = False
	Case $2002 	;- App Display Change 	
	Case $2003 	;- App Display Change 
	Case $2004 ;- App Begin Modal	
	Case $2005 ;- App End Modal	
	Case $4001	;- Timer tick;
		If donotupdate = False Then		
		Cls
		doflickertimer ; Do the flicker	
		;drawmap ; first draw the map
		DrawImage mapbuffer,0,0
		drawgamescreen ; draw the righter game screen
		dominimap ; draw the minimap
		;
		; Draw the surface
		Color 0,0,0
		Rect screenwidth/2-100,screenheight/2-150,200,300,1
		Color 255,255,255
		Rect screenwidth/2-100,screenheight/2-150,200,300,0
		;
		For zy=0 To 5
		If Not tempunits(zy,0) = False Then
		
		DrawImage unitgraphics(tempunits(zy,2)-1),screenwidth/2-90,150+zy*50
		If tempunits(zy,3) = True Then
		DrawImage fortified,(screenwidth/2-90)+42,(150+zy*50)+10
		End If
		
		End If
		Next
		;
		If moused(1) = True Then	
		mzx = (MouseX()-4)/32
		mzy = (MouseY()-2)/50	
		a = mzy*(screenwidth/32)
		mzz = mzx+a
		Select mzz
			Case 85
				If Not tempunits(0,0) = False Then
				If tempunits(0,1) > 0 Then
					currentunit(0) = x : currentunit(1) = y
					currentunit(2) = tempunits(0,0)
					units(currentunit(2),13)=0
					placeunitontop(tempunits(0,0))
					units(currentunit(2),8)=False
					maketempmap 
					Goto suacskipout
				End If
				End If
			Case 110
				If Not tempunits(0,0) = False Then
				If tempunits(1,1) > 0 Then
					currentunit(0) = x : currentunit(1) = y
					currentunit(2) = tempunits(1,0)
					units(currentunit(2),13)=0
					placeunitontop(tempunits(1,0))
					units(currentunit(2),8)=False
					maketempmap
					Goto suacskipout
				End If
				End If
			Case 135
				If Not tempunits(0,0) = False Then
				If tempunits(2,1) > 0 Then
					currentunit(0) = x : currentunit(1) = y
					currentunit(2) = tempunits(2,0)
					units(currentunit(2),13)=0
					placeunitontop(tempunits(2,0))
					units(currentunit(2),8)=False
					maketempmap
					Goto suacskipout
				End If
				End If
			Case 160
				If Not tempunits(0,0) = False Then
				If tempunits(3,1) > 0 Then
					currentunit(0) = x : currentunit(1) = y
					currentunit(2) = tempunits(3,0)
					units(currentunit(2),13)=0
					placeunitontop(tempunits(3,0))
					units(currentunit(2),8)=False
					maketempmap
					Goto suacskipout
				End If
				End If
			Case 185
				If Not tempunits(4,0) = False Then
				If tempunits(4,1) > 0 Then
					currentunit(0) = x : currentunit(1) = y
					currentunit(2) = tempunits(4,0)
					units(currentunit(2),13)=0
					placeunitontop(tempunits(4,0))
					units(currentunit(2),8)=False
					maketempmap
					Goto suacskipout
				End If
				End If
			Case 210
				If Not tempunits(5,0) = False Then
				If tempunits(5,1) > 0 Then
					currentunit(0) = x : currentunit(1) = y
					currentunit(2) = tempunits(5,0)
					units(currentunit(2),13)=0
					placeunitontop(tempunits(5,0))
					units(currentunit(2),8)=False
					maketempmap
					Goto suacskipout
				End If
				End If
		End Select
		End If
		;
		DrawImage mousepointer,MouseX(),MouseY() ; lastly draw the mousepointer
		Flip
	End If
	End Select
Wend
.suacskipout
wacht(250)
.suakskipout2
FlushKeys:FlushMouse
End Function



;
; Here we insert default information in the units
;
;
Function validateactiveunits()
For i=0 To maxunits
If units(i,0) = True Then
units(i,5) = unitdefault(units(i,4),0)
units(i,6) = unitdefault(units(i,4),1)
units(i,10) = unitdefault(units(i,4),2) ; insert the moves
unitsmove(i) = unitdefault(units(i,4),2) ; insert the moves
End If
Next
End Function


;
; returns the slot number of a empty units slot
;
;
Function findnewunit()
For i=0 To maxunits
If units(i,0) = False Then Return i
Next
Return -1
End Function

; Function to find the next movable unit. Checks the visible screen first then the entire map
;
; This function is to move all players. Not limited to one player. This will be added
; once a game/editor switch is added.
;
;
Function findnextmovableunit(player)

;Cls
;Color 255,255,255
;Text 0,0,player
;Text 0,20,currentplayer
;Flip
;WaitKey
nothingfound = True
.fnmuskipin
;First check if there are movable units left
thisistrue = False
For i=0 To maxunits
	If units(i,0)=True Then ; is the unit active
		If units(i,14) = False Then ; is the unit not in a city
		If units(i,11) = player Then ; is the unit of the current player
			If units(i,8) = False ; is the unit fortified
				;If units(i,10)>0 Then ; has the unit moves left
				If unitsmove(i)>0 Then ; has the unit moves left				
				If units(i,15)= 0 Then ; is the unit not busy
					If Not units(i,13)=10 Then ; if there is no special method
					If units(i,unit_onhold) = False Then ; is the unit not on hold				
					If units(i,unit_invisible) = False Then ; not invisible
					thisistrue = True 
					End If
					End If
				;End If
				End If
				End If
				End If
			End If
		End If
		End If
	End If
Next
; If this code is called for then it means there are no more movable units on the map.
;
;
If thisistrue = False Then 
	currentunit(0) = -1
	currentunit(1) = -1
	currentunit(2) = -1

	If aretheremovableunitsleft(player)=True Then 
		;simplemessage("Enabling units")
		enablewaitingunits(player,lastunitstartx,lastunitstarty)
		; Later implementation
		If arethereactiveunits(player) = True Then
			;simplemessage("Making all units shit")
			enablewaitingunits(player,-1,-1)
			lastunitstartx = -1
			lastunitstarty = -1
			Else
			
		End If		
		Goto fnmuskipin
	End If
	endofturn = True
	Goto fnmuskipout
End If


;
; This section checks if there are movable units left on the last units starting position 
;


For i=0 To maxunits
	If units(i,0) = True Then
	If units(i,unit_onhold) = False Then
		If units(i,1) = lastunitstartx And units(i,2) = lastunitstarty Then
			If units(i,14) = False Then ; if not in a city
				If units(i,unit_alignment) = player Then
					If units(i,8) = False Then ; if not fortified
						If unitsmove(i)>0 Then
							If units(i,15) = 0 Then ; if not busy
								If Not units(i,13) = 10 Then ; if not building
								If Not units(i,unit_invisible) = True Then
									If units(i,13) = False Then
										If units(i,unit_invisible) = False Then
										a = preferunloadingunit(i)
										
										If a>-1 Then
										currentunit(0) = units(a,1)
										currentunit(1) = units(a,2)
										currentunit(2) = a										
										Else
										currentunit(0) = units(i,1)
										currentunit(1) = units(i,2)
										currentunit(2) = i										
										End If
										lastunitstartx = currentunit(0)
										lastunitstarty = currentunit(1)
										placeunitontop(currentunit(2))
										nothingfound = False
										; here we check if we have to center the screen
										donotcenterscreen = False
										For my.screencoords1 = Each screencoords1
											If my\x+mapx = lastunitstartx And my\y+mapy = lastunitstarty Then
												donotcenterscreen = True
											End If
										Next
										; center the screen
										If donotcenterscreen = False Then
											centerscreen(lastunitstartx,lastunitstarty)
										End If
										;					
										
										Goto fnmuskipout
										End If
									End If
								End If
								End If
							End If
						End If
					End If
				End If
			End If
		End If
	End If
	End If
Next



; This section checks if there are movable units on the current screen.
;
q =  arethereactiveunitsinscreen()
If Not q=-1 Then

	currentunit(0) = units(q,1)
	currentunit(1) = units(q,2)
	currentunit(2) = q
	lastunitstartx = currentunit(0)
	lastunitstarty = currentunit(1)
	placeunitontop(currentunit(2))
	nothingfound = False
	Goto fnmuskipout
End If

;If we get here it means there are movable units left but not on the screen.
; So we check the entire map for movable units
For i=0 To maxunits
If units(i,0) = True Then
If units(i,14) = False Then ; if not in a city
If units(i,11) = player Then
If units(i,8) = False
If Not units(i,unit_invisible) = True Then
;If units(i,10) > 0 Then
If unitsmove(i) > 0 Then
If units(i,12) = False Then
If units(i,15)= 0 Then 
If Not units(i,13)=10 Then
If units(i,unit_invisible) = False Then
currentunit(0) = units(i,1)
currentunit(1) = units(i,2)
currentunit(2) = i
lastunitstartx = currentunit(0)
lastunitstarty = currentunit(1)
nothingfound=False

If delayafterplayermovement = False Then
centerscreen(units(i,1),units(i,2)) ; center the screen
End If

placeunitontop(currentunit(2))
Goto fnmuskipout
End If
;End If
End If
End If
End If
End If
End If
End If
End If
End If
End If
Next
.fnmuskipout
If nothingfound = True Then Return -1
End Function

Function preferunloadingunit(unit)
For i=0 To maxunits
If units(i,unit_active) = True Then
If units(i,unit_x) = units(unit,unit_x) Then
If units(i,unit_y) = units(unit,unit_y) Then
If units(i,unit_invisible) = False Then
If units(i,unit_fortified) = False Then
If units(i,unit_onhold) = False Then
If units(i,unit_turnsinactive) = 0 Then
If units(i,unit_cancarry) = 0 Then
If unitsmove(i) > 0 Then
If units(i,unit_carried) > -1 Then
If units(units(i,unit_carried),unit_cancarry) > 0 Then
Return i
End If
End If
End If
End If
End If
End If
End If
End If
End If
End If
End If
Next
Return -1
End Function

;
;
;
;
Function arethereactiveunits(player)
For i=0 To maxunits
If units(i,0) = True Then
If Not units(i,unit_fortified) = True Then
If units(i,unit_turnsinactive) = 0
If Not units(i,unit_invisible) = True
If units(i,unit_ai) = 0 Then
If unitsmove(i) > 0 Then
If units(i,unit_onhold) = False Then
Return True
End If
End If
End If
End If
End If
End If
End If
Next
Return False
End Function
;
; This function returns if there are active units in the visible screen
;
;
Function arethereactiveunitsinscreen()
; Get the current location in the integers
;
For i=0 To maxunits
If units(i,0) = True Then ; is this unit active
If units(i,11) = currentplayer Then ; is this unit from the current player
If units(i,14) = False Then ; is the unit not in a city
For my.screencoords1 = Each screencoords1
If my\x+mapx = units(i,1) And my\y+mapy = units(i,2) ; is this unit on the current location
If units(i,8) = False ; Is the unit not fortified
If unitsmove(i) > 0 Then ; are there moves left
If units(i,12) = False Then ; is the unit on hold
If units(i,15)= 0 Then ; if the unit is not busy
If Not units(i,13)=10 Then ; if he is not doing something
If units(i,unit_invisible) = False Then
Return i
End If
End If
End If
End If
End If
End If
End If
Next
End If
End If
End If
Next

Return -1
End Function


;
; This function checks if there are units on hold
;
;
Function aretheremovableunitsleft(player)
gobblegobble = False
For i=0 To maxunits
If units(i,0) = True
If units(i,11) = player
If units(i,unit_invisible) = False Then
If units(i,unit_fortified) = False Then
If units(i,unit_onhold) = True Then gobblegobble=True
End If
End If
End If
End If
Next
Return gobblegobble
End Function

;
; This function sets all waiting units to non-waiting
;
;
Function enablewaitingunits(player,x,y)
For i=0 To maxunits
If units(i,0) = True
If units(i,unit_alignment) = player
If units(i,unit_invisible) = False Then
If unitsmove(i) > 0 Then
If units(i,unit_fortified) = False Then
If units(i,unit_onhold) = True Then
	If (Not units(i,unit_x) = x) And (Not units(i,unit_y) = y) Then
		If units(i,12) = True Then units(i,12)=False
	End If
End If
End If
End If
End If
End If
EndIf
Next
End Function 

; This unit places a unit on a x,y location ontop 
;
;
;
Function placeunitontop2(x,y)
	makeontopper = -1
	For i=0 To maxunits
		If units(i,0) = True Then
			If Not i=u Then
				If x = units(i,1) Then
					If y = units(i,2) Then
						If units(i,unit_ontop) = True And units(i,unit_invisible) = False Then
							makeontopper = i
						End If
						units(i,unit_ontop)=False						
					End If
				End If
			End If
		End If
	Next
	If makeontopper >0 Then	units(makeontopper,unit_ontop) = True : Return
	For i=0 To maxunits
		If units(i,0) = True Then
			If Not i=u Then
				If x = units(i,1) Then
					If y = units(i,2) Then
						If units(i,unit_invisible) = False Then
							units(i,unit_ontop)=True 
							Return
						End If
					End If
				End If
			End If
		End If
	Next

	
End Function

; This unit places a unit on a x,y location ontop 
;
;
;
Function placeunitontop(u)
	x = units(u,1)
	y = units(u,2)
	For i=0 To maxunits
		If units(i,0) = True Then
			If Not i=u Then
				If x = units(i,1) Then
					If y = units(i,2) Then
						units(i,3)=False
					End If
				End If
			End If
		End If
	Next
	units(u,3) = True
End Function

;
; This function makes a unit on a stack become ontop and the rest false ontop
; 
;
Function createontopunit(player,x,y)
For i=0 To maxunits
If units(i,0) = True 
If units(i,1) = x 
If units(i,2) = y
If units(i,11) = player
units(i,3) = True : Return
End If
End If
End If
End If
Next
End Function

;
; This function sets everything up so the player can move units again
;
;
Function nextturn()

;
; Make a backup save
;
savegame("autosave.sav")

gameturn=gameturn + 1
endofturn=False
;
; 
; See if the player owns more then half the cities on the map
; if so then all kings declare war on you
;
counter = 0
counter2 = 0
For i=0 To maxcities
If cities(i,0) = True Then
counter = counter + 1
If cities(i,4) = currentplayer Then counter2=counter2+1
End If
Next
If counter>20 And Rand(20) = 0 Then
; If the all enemies combined have equal or less cities
If (counter/2)=<counter2 Then
For i=1 To 8
warstate(i,currentplayer) = True
warstate(currentplayer,i) = True
;
simplemessage("All the kings declare war on you")
;
Next
End If
End If

;
; If one empire has no cities left then set the warstate to zero
For i=1 To 8
citiesleft = 0 
For ii=0 To maxcities
If cities(ii,0) = True Then
If cities(ii,city_alignment) = i Then
citiesleft = citiesleft + 1
End If
End If
Next
If citiesleft = 0 Then
For ii=1 To 8
warstate(ii,i) = False
warstate(i,ii) = False
Next
End If
Next

;
;
;						 Set the difficulty level
; Count enemy cities 
difficultylevel = 15

; Difficulty based on army size
If getplayerunits(currentplayer)>20 Then difficultylevel = 10
If getplayerunits(currentplayer)>40 Then difficultylevel = 8
If getplayerunits(currentplayer)>60 Then difficultylevel = 7
If getplayerunits(currentplayer)>80 Then difficultylevel = 5
If getplayerunits(currentplayer)>90 Then difficultylevel = 3
If getplayerunits(currentplayer)>100 Then difficultylevel = 1
If getplayerunits(currentplayer)>150 Then difficultylevel = 0

For i=1 To 8
playerdifficultylevel(i) = difficultylevel
Next


; Count the amount of units on the map
counter = 0
For i=1 To 8
counter = counter + getplayerunits(i)
Next
; If there are to many units on the map then decrease the output of the ai
For i=1 To 8
If counter > maxunits-500 Then playerdifficultylevel(i) = 10
If counter > maxunits-400 Then playerdifficultylevel(i) = 50
If counter > maxunits-300 Then playerdifficultylevel(i) = 100
If counter > maxunits-200 Then playerdifficultylevel(i) = 500
If counter > maxunits-10 Then playerdifficultylevel(i) = 1000
Next




For attackstrategy = 0 To 5
; 
;
; Create attack missions and update the defensive forces for the cpu in their cities
For i=1 To 8
	If Not i=currentplayer Then 
		;
		wargames = False
		For ii=0 To 8 
			If warstate(i,ii) = True Then
				wargames = True
			End If
		Next
		;
		If wargames = True Then
			If attackstrategy = 0 Then updatedefensiveforce(i)
			
			gothruit = False
			; if there are units with moves left then create attack missions
			For zi=0 To maxunits
				If units(zi,0) = True Then
					If units(zi,unit_alignment) = i Then
						If unitsmove(zi) > 1 Then gothruit = True : Exit
					End If
				End If
			Next

			If gothruit=True Or Rand(2) = 1 Then
				switchaistate(i)
				reassignai(i)
				;If attackstrategy = 0 Or getplayerunits(i) < getplayerunits(currentplayer) Then aicreateattackmission(i)
				aicreateattackmission(i)
			End If
			
		End If
		;
		upgradeaicities(i)
	End If
Next
;
;
; Execute the ai missions
execute10000ai()
execute10000ai()

;
; Here we move the enemy players
;roamplayer(2)
For i=1 To 8
	If Not currentplayer = i Then playerattackenemies(i)
Next

Next



;
; Here we decrease unit working turns
For i=0 To maxunits
If units(i,0) = True Then
If units(i,15) = 1 Then executeunitcommand(i) 
If units(i,15)>0 Then units(i,15) = units(i,15) - 1
End If
Next

; Here we return the movement to the units
For i=0 To maxunits
If units(i,0) = True
;units(i,10) = unitdefault(units(i,4),2)
unitsmove(i) = unitdefault(units(i,unit_type),default_movement)
End If
Next
;
; Add tax from cities to player gold
For i=0 To maxcities
If cities(i,0) = True Then
playergold(cities(i,4)) = playergold(cities(i,4)) + cities(i,5)
End If
Next
;
; Add minemoney
For x=0 To 100
For y=0 To 100
If minemap(x,y,0) = True Then
goldtoadd = minemap(x,y,2)
playergold(minemap(x,y,1)) = playergold(minemap(x,y,1)) + goldtoadd
End If
Next
Next

; Perform the city units queue
;hasbuildit = False
; New feature ; >110 >120 >130 >140 =150
;
For i=0 To maxcities
produceamount = 0
If cities(i,5) =>110 Then produceamount = 1
If cities(i,5) =>120 Then produceamount = 2
If cities(i,5) =>130 Then produceamount = 3
If cities(i,5) =>140 Then produceamount = 4
If cities(i,5)  =150 Then produceamount = 5

For shitdigger = 0 To produceamount
hasbuildit = False
For ii=0 To 4 
If citiesunitsproduction(i,ii) > -1 And hasbuildit=False Then
If playergold(cities(i,city_alignment)) > unitdefault(citiesunitsproduction(i,ii),4) Then
newunitincity(i,citiesunitsproduction(i,ii))
playergold(cities(i,city_alignment)) = playergold(cities(i,city_alignment)) - unitdefault(citiesunitsproduction(i,ii),4)
citiesunitsproduction(i,ii) = -1
hasbuildit = True
; Reshuffle the queue list
For iii=0 To 3
citiesunitsproduction(i,iii) = citiesunitsproduction(i,iii+1)
Next
citiesunitsproduction(i,4) = -1
End If
End If
Next
Next
Next

;
; Removes the sentry of units in the game
;
removesentry()
;
; Do the goto stuff
For i = 1 To 8
automoveunits(i)
Next


;
; Here we find the next unit to move
currentunit(2) = -1
findnextmovableunit(currentplayer)
End Function

;
; This function holds the commands for the units
;
;
Function unitcommands()
;if the user pressed shift plus delete then delete the current unit
If KeyDown(42)=True Or KeyDown(54) Then
If KeyHit(211) = True Then
; Delete the current unit
units(currentunit(2),0) = False
findnextmovableunit(currentplayer)
maketempmap : drawmap : drawminimap
End If
End If

If currentkey = 98 Then ; 'B' build a fortress
If units(currentunit(2),4) = 1 Then ; If the unit is a settler
If settleralreadybuilding(currentunit(2))=False Then ; if there isn't a settler already building a road

If fortressmap(units(currentunit(2),unit_x),units(currentunit(2),unit_y)) = False Then
If isunitoncity(currentunit(2)) = False Then
If Not units(currentunit(2),13) = 6 Then
	unitsmove(currentunit(2)) = 0
	units(currentunit(2),13) = 6 ; set the building flag
	units(currentunit(2),15) = 3 ; it takes 3 turns to build a fortress
	findnextmovableunit(currentplayer):maketempmap
End If	
End If
End If
End If
End If
End If

; If the user presses space (Remove all moves and select new unit to move)
If currentkey = 32 Then
;units(currentunit(2),10) = 0
unitsmove#(currentunit(2)) = 0

;findnextmovableunit(currentplayer) : drawminimap:maketempmap
findnextmovableunit(currentplayer) : maketempmap

End If
; If the user pressed the f key then fortify the unit
If currentkey = 102 Then
;

units(currentunit(2),unit_fortified) = True ; set fortified flag to true


If isunitonboat(currentunit(2)) = True Then 
If units(currentunit(2),unit_cancarry) = 0 Then
units(currentunit(2),unit_invisible) = True
units(currentunit(2),unit_fortified) = False ; set fortified flag to true
End If
End If
;
; Tutorial
;
tm = tutorial_fortify
If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
;
;units(currentunit(2),10) = 0 ; set moves to ZERO
;unitsmove(currentunit(2)) = 0; set moves to ZERO ; New gamerule - fortify does not cost  points
If isunitoncity(currentunit(2)) = True Then units(currentunit(2),14)=True
;findnextmovableunit(currentplayer) : drawminimap:maketempmap
findnextmovableunit(currentplayer) :maketempmap
End If


; If the user presses the w key then wait the unit
If currentkey = 119 Then
units(currentunit(2),unit_onhold)=True
;If isunitoncity(currentunit(2)) = True Then units(currentunit(2),14)=True
;findnextmovableunit(currentplayer):drawminimap:maketempmap
findnextmovableunit(currentplayer):maketempmap
End If
; If the user presses the s key then sentry
If currentkey = 115 Then
units(currentunit(2),13) = 10 ; set a flag
If isunitoncity(currentunit(2)) = True Then units(currentunit(2),14)=True
;findnextmovableunit(currentplayer):drawminimap:maketempmap
findnextmovableunit(currentplayer):maketempmap

End If

; Settler commands
If currentkey = 114 Then ; if pressed 'r' (road building)
If units(currentunit(2),4) = 1 Then ; If the unit is a settler
;units(currentunit(2),10) = units(currentunit(2),10) - 1
If settleralreadybuilding(currentunit(2))=False Then ; if there isn't a settler already building a road
If Not roadmap(units(currentunit(2),1),units(currentunit(2),2)) = 1 Then ;  if there isn't already a road present
unitsmove(currentunit(2)) = 0
units(currentunit(2),13) = 1 ; set the road building flag
units(currentunit(2),15) = 3 ; it takes 3 turns to build a road
;findnextmovableunit(currentplayer):drawminimap:maketempmap
findnextmovableunit(currentplayer):maketempmap
End If
End If
End If
End If
If currentkey = 109 Then ; if pressed m build mine
If units(currentunit(2),4) = 1 Then ; if the unit is a settler
If settleralreadybuilding(currentunit(2)) = False Then ; if he is not building
If Not map(units(currentunit(2),1),units(currentunit(2),2)) = 7 Then ; if not a mine
If map(units(currentunit(2),1),units(currentunit(2),2)) = 6 Then ; if it is a gold mountain
simplemessage("building")
unitsmove(currentunit(2)) = 0
units(currentunit(2),13) = 2
units(currentunit(2),15) = 3
;findnextmovableunit(currentplayer):drawminimap:maketempmap:drawmap
findnextmovableunit(currentplayer):maketempmap:drawmap
End If
Else ; if not a mine already
simplemessage("This area is already being mined")
End If
End If
End If
End If


End Function


;
; This function executes unit commands
;
;
Function executeunitcommand(unit)

; Here we execute workforce commands

If units(unit,4) = 1 Then 

; If the unit was building a road
If units(unit,13) = 1 Then
roadmap(units(unit,1),units(unit,2)) = 1 ; add to the road map
updateroadmap(units(unit,1),units(unit,2)) ; update the roadmap
units(unit,13) = False ; set building flag to zero
End If


; if the unit was building a mine
If units(unit,13) = 2 Then 
map(units(unit,1),units(unit,2)) = 7 ; change the map
units(unit,13) = False ; set building flag to zero
;
minemap(units(unit,1),units(unit,2),0) = True
minemap(units(unit,1),units(unit,2),1) = units(unit,unit_alignment)
minemap(units(unit,1),units(unit,2),2) = Rand(50)+25
;
End If

;
; Build the fortress

If units(unit,13) = 6 Then
fortressmap(units(unit,unit_x),units(unit,unit_y)) = True
units(unit,13) = False
End If
End If

End Function

;
; Removes the sentry of units in the game
;
Function removesentry()
For i=0 To maxunits
If units(i,unit_active) = True Then

;If units(i,unit_ai) = 10 Then units(i,unit_ai) = False
If units(i,13) = 10 Then
If checkforenemyallaround(i) = True Then
;If checkzoneforenemies(units(i,unit_alignment),units(i,unit_x),units(i,unit_y)) = True Then
;DebugLog units(i,13)
;End
units(i,13) = False
;DebugLog "i : " +i
;DebugLog "unit_au " + unit_ai
End If
End If
End If
Next
End Function
;
; This function checks if there if a enemy around the player
;
Function checkforenemyallaround(unit)
x = units(unit,unit_x)
y = units(unit,unit_y)

For x1=x-1 To x+1
For y1=y-1 To y+1
If x1>0 And y1>0 And x1<mapwidth And y1<mapheight Then
For i=0 To maxunits
If units(i,unit_active) = True Then
If units(i,unit_x) = x1 And units(i,unit_y) = y1 Then 


If warstate(units(i,unit_alignment),units(unit,unit_alignment)) = True Then
Return True

End If
End If
End If
Next

End If
Next
Next
End Function
;
; This function returns if there is already a settler working on a road on
; a position. inputs is _unit_
;
Function settleralreadybuilding(unit)
For i=1 To maxunits
If units(i,0) = True Then
If units(i,1) = units(unit,1) Then
If units(i,2) = units(unit,2) Then
	If units(i,13) = 1 Then Return True
End If
End If
End If
Next
End Function
;
; Here a function to give the user some information
;
;
Function simplemessage(s$)
Delay(200)
FlushMouse()
FlushKeys()

exitmes$ = "Press any key to continue"
w = StringWidth(s)
If StringWidth(exitmes)>w Then w = StringWidth(exitmes)
;
;Color 0,0,0
;Rect screenwidth/2-(w/2+20),screenheight/2-100,w+40,200,1
x = 800/2-(w/2+20)
y = 600/2-100
w1 = w+40
h1 = 200
gwindow(x,y,w1,h1,1,0,0)
gwindow(x+5,y+10,w1-10,h1-20,0,1,1)
Color 0,0,0
Text 800/2,600/2-40,s,1,1
Text 800/2,600/2,exitmes,1,1
Flip
Delay(50)
FlushKeys()
playsfx(3)
;WaitKey

While KeyDown(1) = False And moused(1)=False And moused(2) = False And KeyDown(57)=False And KeyDown(28) = False
we = WaitEvent()

Select we
	Case $101 	;- Key down 
	Case $102 	;- Key up
	If EventData() = 1 Then Exit
	Case $103 	;- Key stroke 
	Case $201 	;- Mouse down
	moused(EventData()) = True	
	Case $202 	;- Mouse up
	moused(EventData()) = False
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
		donotupdate = True		
	Case $2002 	;- App resume
		donotupdate = False
		moused(1) = False:moused(2) = False:moused(3) = False
	Case $2002 	;- App Display Change 	
	Case $2003 	;- App Display Change 
	Case $2004 ;- App Begin Modal	
	Case $2005 ;- App End Modal	
	Case $4001	;- Timer tick;
End Select
Wend

Delay(200)
playsfx(4)
FlushKeys()
End Function
;
; Here a function to give the user some information - nographics
;
;
Function simplemessage2(s$)

exitmes$ = "Press any key to continue"
w = StringWidth(s)
If StringWidth(exitmes)>w Then w = StringWidth(exitmes)
;
Color 0,0,0
Rect screenwidth/2-(w/2+20),screenheight/2-100,w+40,200,1
x = 800/2-(w/2+20)
y = 600/2-100
w1 = w+40
h1 = 200
;gwindow(x,y,w1,h1,1,0,0)
;gwindow(x+5,y+10,w1-10,h1-20,0,1,1)
Color 255,255,255
Text 800/2,600/2-40,s,1,1
Text 800/2,600/2,exitmes,1,1
Flip
Delay(50)
FlushKeys()
playsfx(3)
;WaitKey
While KeyDown(1) = False And moused(1)=False And moused(2) = False And KeyDown(57)=False And KeyDown(28) = False
we = WaitEvent()

Select we
	Case $101 	;- Key down 
	Case $102 	;- Key up
	If EventData() = 1 Then Exit
	Case $103 	;- Key stroke 
	Case $201 	;- Mouse down
	moused(EventData()) = True	
	Case $202 	;- Mouse up
	moused(EventData()) = False
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
		donotupdate = True		
	Case $2002 	;- App resume
		donotupdate = False
		moused(1) = False:moused(2) = False:moused(3) = False
	Case $2002 	;- App Display Change 	
	Case $2003 	;- App Display Change 
	Case $2004 ;- App Begin Modal	
	Case $2005 ;- App End Modal	
	Case $4001	;- Timer tick;
End Select
Wend
Delay(200)
;playsfx(4)
FlushKeys()
End Function




;
; This function returns the enemy player number at a x,y position
;
Function findenemyplayer(x,y)
For i=0 To maxunits
If units(i,0) = True Then ; is the unit active
If x = units(i,1) And y=units(i,2) Then
If units(i,3) = True Then
Return units(i,11)
End If
End If
End If
Next
End Function


;;
; This function checks if a location on the map contains a enemy unit
; 
;
;
Function checkforenemies(unit,x,y)
; Preset
enemynumber = False
playernumber = False
; get our player number
playernumber = units(unit,11)

; check for enemy player
For i=0 To maxunits
If units(i,0) = True Then ; is the unit active
If x = units(i,1) And y=units(i,2) Then
If units(i,3) = True Then
enemynumber = units(i,11)
End If
End If
End If
Next

;if there is a enemy in the destination area then return true
If Not enemynumber = False Then
	If Not enemynumber = playernumber Then Return True
End If

; If there isnt a enemy unit in the destination area then return false
If enemyunit = False Then Return False

End Function


;
; This is the function that handles the battle with the enemy
;
; 
;Function dobattle(unit,x,y)
; get attacking units values
;attackstrength# = units(unit,5) ; what is the attacking units strength
;attackveteran = units(unit,9) ; is it a veteran unit
; get the defending units values
;q = getunitnumber(x,y)
;defendstrength# = units(q,6)
;defendveteran = units(q,9)
;defendfortified = units(q,8)
; take veteran modifier into account
;a#=False
;If attackveteran = True Then a = attackstrength + attackstrength/2
;If  a=False Then a = attackstrength
;d1# = False
;d2# = False
  ;If defendveteran  = True Then d = defendstrength + defendstrength/2
  ;If defendfortified = True Then d = d + defendstrength/2
;d# = defendstrength
;If defendveteran = True Then d1# = defendstrength/2
;If defendfortified = True Then d2# = defendstrength/2
;If d1 = False Then d1=0
;If d2 = False Then d2=0
;d = d + d1 
;d = d + d2

;
; here is the formula that does the battle, still have to add difficulty levels

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
;If aa>dd Then Return True Else Return False

;End Function


;
; Turn based Attack thingy
; as = attack strength, ds = defense strength, av = attack veteran (true/false)
; dv = defense veteran, df = defense veteran
Function dobattle(unit,x,y);as,ds,av,dv,df)
playsfx(1):Delay(200)
SeedRnd MilliSecs() ; randomize timer

as = units(unit,5) ; attack strength
av = units(unit,9) ; is it a veteran unit
q = getunitnumber(x,y) ; get unitnumber we are attacking
ds = units(q,6) ; get defense strength
dv = units(q,9) ; defense veteran
df = units(q,8) ; defense fortified


If av = True Then a1 = as / 2
as = as + a1
a1 = 0

If dv = True Then a1 = ds/2
If df = True Then a2 = ds/2
If fortressmap(units(q,1),units(q,2)) = True Then a3 = ds / 2
ds = ds + a1
ds = ds + a2
ds = ds + a3

aa = Rand(as)
dd = Rand(ds)

;DebugLog "as : "+as+" aa : "+aa
;DebugLog "ds : "+ds+" dd : "+dd
If aa=>dd Then Return True

End Function



;
; this function get the ontop unit number on a x,y location
;
;
;Function getunitnumber(x,y)
;For i=0 To maxunits
;If units(i,0) = True
;If x = units(i,1) And y = units(i,2) Then
;If units(i,3) = True Then
;Return i
;End If
;End If
;End If
;Next
;End Function

;
; This function destroys all units at a x,y location
;
;
Function destroyunitsatposition(x,y)
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
; This one show the battle result
;
;
Function showbattleresults(n)
If n=1 Then t$="You have won the battle" 
If n=2 Then t$="You have lost the battle"

Color 0,0,0
Rect screenwidth/2-100,screenheight/2-100,200,200
Color 255,255,255
Text screenwidth/2-StringWidth(t$)/2,screenheight/2-StringHeight(t$)/2,t$,0,0
Flip
wacht(800)
End Function




;
; Function that returns if a value if even (true) or uneven(false)
;
;
Function isthiseven(n)
n = Right(n,1)
value = True
Select n
Case 1 : value = False
Case 3 : value = False
Case 5 : value = False
Case 7 : value = False
Case 9 : value = False
End Select
Return value
End Function


; This here function draws a game screen to the right.
; To be replaced with a graphic if needed.
;
Function drawgamescreen()

Viewport screenwidth-160,0,160,screenheight

x = screenwidth-160
y = 0
w = 160
h = screenheight

gwindow(x,y,w,h,1,0,0)
gwindow(x+6,y+170,w-12,40,0,1,1)
gwindow(x+6,y+220,w-12,h-230,0,1,1)

Viewport 0,0,screenwidth,screenheight


; Here is the optionbutton
SetFont gsmallfont
If gbutton1(x+50,y+180,60,20,"Options",1,gameoptionbutton) = True Then gameoptionbutton = 1

; execute the optionbutton
If moused(1) = False And gameoptionbutton>0 Then
If gcheckarea(x+50,y+180,60,20) = True Then gameoptionbutton=0 : gamemenu 
gameoptionbutton=0
End If

Color 0,0,0

SetFont mainfont

t$ = "Gold : " + playergold(currentplayer)
Text screenwidth-80,240,t$,1,1

If currentunit(2) > 0 Then
	SetFont grequesterfont

	x = screenwidth-80
	y = 390
	; Sheet label
	t$ = "UNIT INFO"
	Text screenwidth-80,y,t$,1,1
	; Show the unit type
	t$ = unitsname$(units(currentunit(2),4))
	Text screenwidth-80,y+15,t$,1,1
	; Show the moves left
	;t$ = "Moves : " + units(currentunit(2),10)
	t$ = "Moves : " + Left(unitsmove(currentunit(2)),3)
	Text screenwidth-80,y+30,t$,1,1
	; Show the unit location
	t$ = "Unit location " + currentunit(0) + "," + currentunit(1)
	Text screenwidth-80,y+45,t$,1,1
	; Show the veteran status
	If units(currentunit(2),9) = True Then 
	t$ = "Veteran"
	Else
	t$ = "Non Veteran"
	End If
	Text screenwidth-80,y+60,t$,1,1
	
	SetFont gsmallfont	
	If gbutton1(x-60,y+70,60,20,"Wait",1,unitcurrentbutton) Then unitcurrentbutton = 1
	If gbutton1(x-60,y+90,60,20,"Sleep",2,unitcurrentbutton) Then unitcurrentbutton = 2
	If gbutton1(x-60,y+110,60,20,"Fortify",3,unitcurrentbutton) Then unitcurrentbutton = 3
	If gbutton1(x,y+70,60,20,"Build Road",4,unitcurrentbutton) Then unitcurrentbutton = 4
	If gbutton1(x,y+90,60,20,"Disband",5,unitcurrentbutton) Then unitcurrentbutton = 5
	If gbutton1(x,y+110,60,20,"Pillage",6,unitcurrentbutton) Then unitcurrentbutton = 6	
	If gbutton1(x-60,y+130,120,20,"Unit skip turn",7,unitcurrentbutton) Then unitcurrentbutton = 7

	
	; Tooltips
	If gcheckarea(x-60,y+70,60,20) = True Then
		enabletooltip(5)
	End If
	If gcheckarea(x-60,y+90,60,20) = True Then
		;showtooltip(5)
		enabletooltip(5)
	End If
	If gcheckarea(x-60,y+110,60,20) = True  Then		
		enabletooltip(6)
	End If
	If gcheckarea(x,y+70,60,20) = True  Then		
		enabletooltip(7)
	End If
	If gcheckarea(x,y+90,60,20) = True  Then		
		enabletooltip(8)
	End If
	If gcheckarea(x,y+110,60,20) = True  Then		
		enabletooltip(9)
	End If
		If gcheckarea(x-60,y+130,120,20) = True  Then		
		enabletooltip(10)
	End If
	

	If moused(1) = False And unitcurrentbutton > 0 Then
	If gcheckarea(x-60,y+70,60,20) = True And unitcurrentbutton = 1 Then ; if pressed wait
		;
		;
		; Tutorial
		;
		tm = tutorial_wait
		If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
		;

		; from unit commands
		units(currentunit(2),12)=True
		findnextmovableunit(currentplayer):drawminimap:maketempmap
	End If
	If gcheckarea(x-60,y+90,60,20) = True And unitcurrentbutton = 2 Then ; if pressed Sleep
		; from unit commands
		units(currentunit(2),13) = 10 ; set a flag
		findnextmovableunit(currentplayer):drawminimap:maketempmap
	End If
	If gcheckarea(x-60,y+110,60,20) = True And unitcurrentbutton = 3 Then; if pressed fortify
		;
		;
		; Tutorial
		;
		tm = tutorial_fortify
		If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
		;
		units(currentunit(2),8) = True ; set fortified flag to true
		unitsmove(currentunit(2)) = 0; set moves to ZERO
		If isunitoncity(currentunit(2)) = True Then units(currentunit(2),14)=True
		findnextmovableunit(currentplayer) : drawminimap:maketempmap
	End If
	If gcheckarea(x,y+70,60,20) = True And unitcurrentbutton = 4 Then ; if pressed build road
		;
		;
		; Tutorial
		;
		tm = tutorial_roads
		If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
		;
		If units(currentunit(2),4) = 10 Then ; If the unit is a settler
		If settleralreadybuilding(currentunit(2))=False Then ; if there isn't a settler already building a road
		If Not roadmap(units(currentunit(2),1),units(currentunit(2),2)) = 1 Then ;  if there isn't already a road present
		unitsmove(currentunit(2)) = 0
		units(currentunit(2),13) = 1 ; set the road building flag
		units(currentunit(2),15) = 3 ; it takes 3 turns to build a road
		findnextmovableunit(currentplayer):drawminimap:maketempmap
		End If
		End If
		End If
	End If
	If gcheckarea(x,y+90,60,20) = True And unitcurrentbutton = 5 Then ; disband
		;
		;
		; Tutorial
		;
		tm = tutorial_disband
		If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
		;

		; insert disband here
		units(currentunit(2),0) = False
		findnextmovableunit(currentplayer):drawminimap:maketempmap
	End If
	If gcheckarea(x,y+110,60,20) = True And unitcurrentbutton = 6 Then ; pillage
		; insert pillage here
		simplemessage("Not implented")
	End If
	If gcheckarea(x-60,y+130,120,20) = True And unitcurrentbutton = 7 Then ; skip turn
		unitsmove(currentunit(2)) = 0 ; set moves to zero
		findnextmovableunit(currentplayer) : drawminimap:maketempmap
	End If
	unitcurrentbutton = 0
	End If
	
	
End If


;
;
; End of turn button	
SetFont gsmallfont	
If gbutton1(screenwidth-70,551,60,20,"End Turn",8,endofturnbutton) Then endofturnbutton = 1

If moused(1) = True And endofturnbutton > 0 Then
If gcheckarea(screenwidth-70,551,60,20) = True And endofturnbutton = 1 Then ; End turn
	oldmapx = mapx
		oldmapy = mapy
		nextturn
;		If currentunit(2) = -1 Then		
;		simplemessage (currentunit(2)) 
		If currentunit(2) = -1 Then
		mapx = oldmapx
		mapy = oldmapy
;		Else 
;		centerscreen(currentunit(0),currentunit(1))
		End If
        currentkey = 0
		findnextmovableunit(currentplayer)
		maketempmap
		drawminimap
		drawmap
End If
endofturnbutton = 0
End If

; Draw the turn string
;
SetFont mainfont
DrawImage (minmap,screenwidth-159,2)

Color 255,255,255
t$ = "Turn : " + gameturn
Text screenwidth-124,560,t$,1,1


End Function





;
; Show the entire map only.
;
Function drawminimapterrain()

offsetx = 2
offsety = 2
tx# = (screenwidth - 160) + offsetx
ty# = (0) + offsety 
mwidth# = 160-(offsetx*2)
mheight# = 160-(offsety*2)

stepx# = mwidth / mapwidth
stepy# = mheight / mapheight

;Viewport tx,ty,159,159

; Set the image buffer
SetBuffer ImageBuffer(minmapter)
ClsColor 10,10,10
Cls
Color 200,200,200
Rect 0,0,159,159,0

;

For y5=0 To 100
For x5=0 To 100
x6# = x5
y6# = y5
If vismap(x5,y5,currentplayer) = True Or fogofwarenabled = False Then
Select map(x5,y5)
Case 0 ; grass
Color 94,108,49
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 1 ; hills
Color 80,150,80
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 2 ; mountain;
Color 150,150,150
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 3 ; pines
Color 0,100,0
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 4 ; swamp
Color 0,60,0
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 5 ; trees
Color 50,120,0
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 6 ; gold mountain
Color 0,200,200
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 7 ; mine
Color 147,86,45
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 8 ; crops
Color 64,233,64
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 9 ; beaver
Color 0,0,200
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
Case 64 ;water
Color 0,0,150
;Plot (x6*stepx),(y6*stepy)
Oval (x5*stepx)-1,(y5*stepy)-1,2,2,1
End Select
End If
Next
Next
;
rotatescaleimage(minmapter)
;bitch = CreateImage(110,110)
;fuck = CreateImage(400,400)
;bitch = CopyImage(minmapter)
;SetBuffer ImageBuffer(fuck)
;drawrotatedimage(bitch,110,0,-45)
;bitch = CopyImage(fuck)
;SetBuffer ImageBuffer(minmapter)
;Cls
;drawscaledimage(fuck,2,0,.7,.7)
;;
SetBuffer BackBuffer()
ClsColor 0,0,0
;Viewport 0,0,640,480
End Function

Function rotatescaleimage(inputimage)
	bitch = CreateImage(110,110)	
	fuck = CreateImage(400,400)
	bitch = CopyImage(minmapter)
	ClsColor 0,0,0 : Cls
	SetBuffer ImageBuffer(fuck)
	drawrotatedimage(bitch,110,0,-45)
	bitch = CopyImage(fuck)
	SetBuffer ImageBuffer(minmapter)
	Cls
	drawscaledimage(fuck,2,0,.7,.7)
End Function









;
; Show the entire map only.
;
Function drawminimap()

offsetx = 2
offsety = 2
tx# = (screenwidth - 160) + offsetx
ty# = (0) + offsety 
mwidth# = 160-(offsetx*2)
mheight# = 160-(offsety*2)

stepx# = mwidth / mapwidth
stepy# = mheight / mapheight

;Viewport tx,ty,159,159

;Goto sapje

; Set the image buffer
;drawminimapterrain()
SetBuffer ImageBuffer(minmap)
ClsColor 10,10,10
Cls
DrawImage minmapter,0,0
;ClsColor 10,10,10
;Color 50,50,50
;

;DrawBlock minmapter,0,0
;

; Draw all cities on the minimap
For i=0 To maxcities
If cities(i,0) = True Then
If cities(i,4) > 0 Then
x# = cities(i,1)
y# = cities(i,2)
zy = y#
If isthiseven(zy) = False Then x# = x# + .5
;Plot (tx)+(x*stepx),(ty)+(y*stepy)
;Plot (x*stepx),(y*stepy)
If vismap(x,y,currentplayer) = True Or fogofwarenabled = False Then
;Color 255,255,255
;Plot (x*stepx),(y*stepy)
Color playercolor(cities(i,4),0),playercolor(cities(i,4),1),playercolor(cities(i,4),2)
;Oval (x*stepx)-2,(y*stepy)-2,4,4,1
zzx# = rotatenumberx(x*stepx,y*stepy,-45,100,100)/1.4
zzy# = rotatenumbery(x*stepx,y*stepy,-45,100,100)/1.4
Oval zzx-2+78,zzy-2+40,4,4,1
End If
End If
End If
Next




; Draw all units on the minimap
Color 255,255,255
For i=0 To maxunits
If units(i,0) = True Then
x# = units(i,1)
y# = units(i,2)
zy = y#
If isthiseven(zy) = False Then x# = x# + .5
;Plot (tx)+(x*stepx),(ty)+(y*stepy)
If vismap(x,y,currentplayer) = True Or fogofwarenabled = False Then
zzx# = rotatenumberx(x*stepx,y*stepy,-45,100,100)/1.4
zzy# = rotatenumbery(x*stepx,y*stepy,-45,100,100)/1.4
Plot zzx+78,zzy+39
;Plot (x*stepx),(y*stepy)
End If
End If
Next

For i=0 To numminimapzones
If minimapzones(i,0) = True Then
Color 255,255,0
Rect minimapzones(i,1),minimapzones(i,2),minimapzones(i,3)-minimapzones(i,1),minimapzones(i,4)-minimapzones(i,2),0
End If
Next

SetBuffer BackBuffer()
;Viewport 0,0,640,480
End Function

; If the user presses inside the minimap then move the map to that position
;
;
Function dominimap()
; If the user presses left mouse button
;If moused(1) = True If minimapdragactive = False Then
;For i=0 To numminimapzones 
;If minimapzones(i,0) = False Then
;currentminimapzone = i:minimapzones(i,0)=True : Exit
;End If
;Next
;minimapzones(currentminimapzone,1) = MouseX() - (800 - 160)
;minimapzones(currentminimapzone,2) = MouseY()
;minimapdragactive = True
;End If
;If moused(1) = False And minimapdragactive = True Then
;minimapzones(currentminimapzone,3) = MouseX() - (800 - 160)
;minimapzones(currentminimapzone,4) = MouseY()
;minimapdragactive = False
;drawminimap
;End If

;If moused(3) = True Then
;If minimapzones(currentminimapzone,0) =True Then
;dewidth = minimapzones(currentminimapzone,3) - (minimapzones(currentminimapzone,1))
;deheight = minimapzones(currentminimapzone,4) - (minimapzones(currentminimapzone,2))
;mposx = MouseX() - (800-160)
;mposy = MouseY()

;cm = currentminimapzone

;minimapzones(cm,1) = mposx -dewidth/2
;minimapzones(cm,2) = mposy -deheight/2
;minimapzones(cm,3) = mposx + dewidth/2
;minimapzones(cm,4) = mposy + deheight/2


;drawminimap
;End If
;End If



; If the user presses the mouse on the minimap then move move the view to this location
If moused(1) = True Then
tx# = (screenwidth - 160)
ty# = (0)
mwidth# = 160-(4)
mheight# = 160-(4)

mmstepx# = mwidth / mapwidth
mmstepy# = mheight / mapheight

If MouseX()=>tx And MouseX()<tx+mwidth Then
If MouseY()=>ty And MouseY()<ty+mheight Then
drawminimap

;
; Tutorial
;
tm = 13;tutorial_minimap
If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True : Return



; Erase the check zones
;For i=0 To numminimapzones
;minimapzones(i,0)= False
;Next


pupx= MouseX() - (screenwidth-160)+2
pupy = MouseY() - 2
;ttx# = MouseX() - (screenwidth-160)+2
;tty# = MouseY() - 2
x2# = rotatenumberx(pupx,pupy,-45,100,100)/1.1
y2# = rotatenumbery(pupx,pupy,-45,100,100)/1.1
;x = ttx# / mmstepx
;y = tty# / mmstepy
x = (-x2# +50);* mmstepx
y = (y2#);/ mmstepy

;x2 = rotatenumberx(x,y,-45,100,100)/1.4
;y2 = rotatenumbery(x,y,-45,100,100)/1.4


;If Confirm (x2 + "  e " + y2) Then End

If x2<0 Then x2=0
If x2>100 Then x2 = 100
If y2<0 Then y2=0
If y2>100 Then y2 = 100

centerscreen(y,x)

; Center the position
; update the visible units
;Cls
maketempmap
drawmap
End If
End If
End If
End Function

;
; This function is the game menu where important functions reside
;
;
Function gamemenu()


timer = CreateTimer(60)


While KeyHit(1) = False

we = WaitEvent()

Select we
	Case $101 	;- Key down 
	Case $102 	;- Key up
	If EventData() = 1 Then Exit
	Case $103 	;- Key stroke 
	Case $201 	;- Mouse down
	moused(EventData()) = True	
	Case $202 	;- Mouse up
	moused(EventData()) = False
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
		donotupdate = True		
	Case $2002 	;- App resume
		donotupdate = False
		moused(1) = False:moused(2) = False:moused(3) = False
	Case $2002 	;- App Display Change 	
	Case $2003 	;- App Display Change 
	Case $2004 ;- App Begin Modal	
	Case $2005 ;- App End Modal	
	Case $4001	;- Timer tick;

		Cls
		
		; First draw the background
		DrawImage mapbuffer,0,0
		drawgamescreen
		
		
		x = 300
		y = 150
		w = 200
		h = 300
		
		gwindow(x,y,w,h,1,0,0)
		gwindow(x+10,y+10,w-20,h-20,0,1,1)
		
		SetFont gsmallfont
		If gbutton1(x+20,y+20,160,20,"Load Game",1,currentbutton) Then currentbutton = 1
		If gbutton1(x+20,y+50,160,20,"Save Game",2,currentbutton) Then currentbutton = 2
		If gbutton1(x+20,y+80,160,20,"Edit Mode On/Off",3,currentbutton) Then currentbutton = 3
		If gbutton1(x+20,y+110,160,20,"Help",4,currentbutton) Then currentbutton = 4
		
		If gbutton1(x+20,y+210,160,20,"About",8,currentbutton) Then currentbutton = 8
		If gbutton1(x+20,y+230,160,20,"Quit",5,currentbutton) Then currentbutton = 5
		If gbutton1(x+20,y+260,160,20,"Exit this window",6,currentbutton) Then currentbutton = 6
		If gbutton1(x+20,y+130,160,20,"Map Options",7,currentbutton) Then currentbutton = 7
		
		If moused(1) = False And currentbutton>0 Then
		
		; button 1
		If gcheckarea(x+20,y+20,160,20) = True And currentbutton = 1 Then
		loadsave(Asc("l")) : maketempmap:drawminimap:drawmap:Goto gmskipout
		End If
		
		; button 2
		If gcheckarea(x+20,y+50,160,20) = True And currentbutton = 2 Then
		loadsave(Asc("s")) : Goto gmskipout
		End If
		
		; button 3
		If gcheckarea(x+20,y+80,160,20) = True And currentbutton = 3 Then
		If editmode = False Then editmode = True Else editmode = False
		Goto gmskipout
		End If
		
		; button 4
		If gcheckarea(x+20,y+110,160,20) = True And currentbutton = 4 Then
		helpmenu
		End If
		
		; button 5
		If gcheckarea(x+20,y+230,160,20) = True And currentbutton = 5 Then
		If grequest(800/2-150,600/2-80,300,170,"Request","","Quit this game ?","","Ok","Cancel") = True Then End
		End If
		
		; button 6
		If gcheckarea(x+20,y+260,160,20) = True And currentbutton = 6 Then
		Goto gmskipout
		End If
		
		; button 7
		If gcheckarea(x+20,y+130,160,20) = True And currentbutton = 7 Then
		gameoptionsscreen()
		End If
		
		; button 8
		If gcheckarea(x+20,y+210,160,20) = True And currentbutton = 8 Then
		intromessage=2
		End If
		
		currentbutton = 0
		
		End If
		
		
		DrawImage mousepointer,MouseX(),MouseY() ; lastly draw the mousepointer
		If screenshotsenabled=True And KeyHit(2) = True Then SaveBuffer(FrontBuffer(),"c:\windows\desktop\screenshot.bmp"):End
		
		Flip
	End Select
Wend
.gmskipout
DrawImage mapbuffer,0,0
drawgamescreen
FlushKeys:FlushMouse
End Function

Function gameoptionsscreen()

timer = CreateTimer(60)

While KeyHit(1) = False

we = WaitEvent()

Select we
	Case $101 	;- Key down 
	Case $102 	;- Key up
	If EventData() = 1 Then Exit
	Case $103 	;- Key stroke 
	Case $201 	;- Mouse down
	moused(EventData()) = True	
	Case $202 	;- Mouse up
	moused(EventData()) = False
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
		donotupdate = True		
	Case $2002 	;- App resume
		donotupdate = False
		moused(1) = False:moused(2) = False:moused(3) = False
	Case $2002 	;- App Display Change 	
	Case $2003 	;- App Display Change 
	Case $2004 ;- App Begin Modal	
	Case $2005 ;- App End Modal	
	Case $4001	;- Timer tick;
		
		Cls
		; First draw the background
		DrawImage mapbuffer,0,0
		drawgamescreen
		gwindow(0,0,640,600,0,1,0)
		
		SetFont gsmallfont
		If gbutton1(20,20,160,20,"Clear units",1,currentbutton) Then currentbutton = 1
		If gbutton1(20,50,160,20,"Clear Cities",2,currentbutton) Then currentbutton = 2
		If gbutton1(20,80,160,20,"Clear Roads",3,currentbutton) Then currentbutton = 3
		If gbutton1(20,110,160,20,"Clear Fortresses",4,currentbutton) Then currentbutton = 4
		If gbutton1(20,140,160,20,"Clear Map",5,currentbutton) Then currentbutton = 5
		If gbutton1(20,170,160,20,"Clear All",6,currentbutton) Then currentbutton = 6
		
		If gbutton1(200,20,160,20,"Generate Map",51,currentbutton) Then currentbutton = 51
		If gbutton1(200,50,160,20,"Set gold",52,currentbutton) Then currentbutton = 52
		
		
		
		If gbutton1(20,220,160,20,"Play as #1",101,currentbutton) Then currentbutton = 101
		If gbutton1(20,250,160,20,"Play as #2",102,currentbutton) Then currentbutton = 102
		If gbutton1(20,280,160,20,"Play as #3",103,currentbutton) Then currentbutton = 103
		If gbutton1(20,310,160,20,"Play as #4",104,currentbutton) Then currentbutton = 104
		If gbutton1(20,340,160,20,"Play as #5",105,currentbutton) Then currentbutton = 105
		If gbutton1(20,370,160,20,"Play as #6",106,currentbutton) Then currentbutton = 106
		If gbutton1(20,400,160,20,"Play as #7",107,currentbutton) Then currentbutton = 107
		If gbutton1(20,430,160,20,"Play as #8",108,currentbutton) Then currentbutton = 108
		Color playercolor(1,0),playercolor(1,1),playercolor(1,2)
		Oval 190,220,20,20,1
		Color playercolor(2,0),playercolor(2,1),playercolor(2,2)
		Oval 190,250,20,20,1
		Color playercolor(3,0),playercolor(3,1),playercolor(3,2)
		Oval 190,280,20,20,1
		Color playercolor(4,0),playercolor(4,1),playercolor(4,2)
		Oval 190,310,20,20,1
		Color playercolor(5,0),playercolor(5,1),playercolor(5,2)
		Oval 190,340,20,20,1
		Color playercolor(6,0),playercolor(6,1),playercolor(6,2)
		Oval 190,370,20,20,1
		Color playercolor(7,0),playercolor(7,1),playercolor(7,2)
		Oval 190,400,20,20,1
		Color playercolor(8,0),playercolor(8,1),playercolor(8,2)
		Oval 190,430,20,20,1
		
		
		If fogofwarenabled = False Then fogenabledstring$ = "is disabled" Else fogenabledstring$ = "is enabled"
		If gbutton1(20,480,160,20,"Fog of war " + fogenabledstring$,1501,currentbutton) Then currentbutton = 1501
		
		If gbutton1(200,480,160,20,"Reset fog",1500,currentbutton) Then currentbutton = 1500
		
		
		
		If gbutton1(640-160,20,160,20,"Exit",999,currentbutton) Then currentbutton = 999
		
		
		
		If moused(1) = False And currentbutton>0 Then
		
		; Button 1 Clear units
		If gcheckarea(20,20,160,20) = True And currentbutton=1 Then
		resetgamedata(True,False,False,False,False)
		End If
		
		; Button 2 Clear Cities
		If gcheckarea(20,50,160,20) = True And currentbutton=2 Then
		resetgamedata(False,True,False,False,False)
		End If
		
		; Button 3 Clear Roads
		If gcheckarea(20,80,160,20) = True And currentbutton=3 Then
		resetgamedata(False,False,True,False,False)
		End If
		
		; Button 4 Clear fortresses
		If gcheckarea(20,110,160,20) = True And currentbutton=4 Then
		resetgamedata(False,False,False,True,False)
		End If
		
		; Button 5 Clear map
		If gcheckarea(20,140,160,20) = True And currentbutton=5 Then
		resetgamedata(False,False,False,False,True)
		; Set the fog of war to true
		For i=1 To 8
		For x=0 To 100
		For y=0 To 100
		vismap(x,y,i) = True
		Next
		Next
		Next
		;
		For x=0 To 100
		For y=0 To 100
		minemap(x,y,0) = False
		Next
		Next
		End If
		
		; Button 6 Clear all
		If gcheckarea(20,170,160,20) = True And currentbutton=6 Then
		resetgamedata(True,True,True,True,True)
		; Set the fog of war to true
		For i=1 To 8
		For x=0 To 100
		For y=0 To 100
		vismap(x,y,i) = True
		Next
		Next
		Next
		;
		For x=0 To 100
		For y=0 To 100
		minemap(x,y,0) = False
		Next
		Next
		End If
		
		
		; Button 51 Clear map
		If gcheckarea(200,20,160,20) = True And currentbutton=51 Then
		;mg_setupmap(Rand(20,60))
		mapcreatearea(0)
		;mapcreatearea(1)
		;mapcreatearea(2)
		;mapcreatearea(3)
		;mapcreatearea(4)
		;mapcreatearea(5)
		End If
		
		; Button 52 Set gold
		If gcheckarea(200,50,160,20) = True And currentbutton=52 Then
		a$ = ginputbox(100,200,200,200,"Set money","1000","Ok")
		b = a$
		If b<0 Or b>64000 Then b = 64000
		playergold(currentplayer) = b
		End If
		
		
		; Play as player buttons
		; Button 1 Clear units
		If gcheckarea(20,220,160,20) = True And currentbutton=101 Then
		currentplayer = 1
		End If
		
		; Button 2 Clear Cities
		If gcheckarea(20,250,160,20) = True And currentbutton=102 Then
		currentplayer = 2
		End If
		
		; Button 3 Clear Roads
		If gcheckarea(20,280,160,20) = True And currentbutton=103 Then
		currentplayer = 3
		End If
		
		; Button 4 Clear fortresses
		If gcheckarea(20,310,160,20) = True And currentbutton=104 Then
		currentplayer = 4
		End If
		
		; Button 5 Clear map
		If gcheckarea(20,340,160,20) = True And currentbutton=105 Then
		currentplayer = 5
		End If
		
		; Button 6 Clear map
		If gcheckarea(20,370,160,20) = True And currentbutton=106 Then
		currentplayer = 6
		End If
		
		; play as player 7
		If gcheckarea(20,400,160,20) = True And currentbutton=107 Then
		currentplayer = 7
		End If
		
		; play as player 8
		If gcheckarea(20,430,160,20) = True And currentbutton=108 Then
		currentplayer = 8
		End If
		
		; Button 1500 Createfogofwar
		If gcheckarea(200,480,160,20) = True And currentbutton=1500 Then
		;
		For x=0 To 100
		For y=0 To 100
		For i=1 To 8
		vismap(x,y,i) = False
		Next
		Next
		Next
		; Create visible parts around area
		For i=1 To 8
		createbasevisibleareamap(i)
		Next
		
		For x=0 To mapwidth
		For y=0 To mapheight
		If amap(x,y) = currentplayer Then vismap(x,y,currentplayer) = True
		Next
		Next
		
		
		drawminimapterrain
		drawminimap
		updatescreen
		
		End If
		
		; For of war disabler
		If gcheckarea(20,480,160,20) = True And currentbutton=1501 Then
		If fogofwarenabled = True Then fogofwarenabled = False Else fogofwarenabled = True
		Delay(100)
		End If
		
		; Button 999 Exit button
		If gcheckarea(640-160,20,160,20) = True And currentbutton=999 Then
		maketempmap:drawminimap:drawmap
		Return
		End If
		
		
		currentbutton=0
		End If
		
		DrawImage mousepointer,MouseX(),MouseY()
		If screenshotsenabled=True And KeyHit(2) = True Then SaveBuffer(FrontBuffer(),"c:\windows\desktop\screenshot.bmp"):End
		Flip
	End Select
Wend
.skipout3
Delay(500) : FlushKeys()

End Function


;
;
; Create forrest
;
Function mapcreatearea(tile)

mg_setupmap(0)

drawminimapterrain()
drawminimap()

;For pass1=0 To 10
;x = Rand(100)
;y = Rand(100)

;For i=0 To 50
;x2 = Rand(5) - 2
;y2 = Rand(5) - 2
;If x+x2<100 And y+y2<100 And x+x2>0 And y+y2>0 Then
;map(x+x2,y+y2) = tile
;End If
;Next

;For i=0 To 50
;x2 = Rand(10)-5
;y2 = Rand(10)-5
;If x+x2<100 And y+y2<100 And x+x2>0 And y+y2>0 Then
;map(x+x2,y+y2) = tile
;End If
;Next
;Next
End Function

;
;
;
; Reset game data
Function resetgamedata(param1,param2,param3,param4,param5)

If param1 = True Then ; clear all the units
For i=0 To maxunits
For ii=0 To 32
units(i,ii) = 0
Next
Next
End If

If param2 = True Then ; clear all the cities
For i=0 To maxcities
For ii=0 To 5
cities(i,ii) = 0
Next
Next
End If

If param3 = True Then ; clear the road
For x=0 To 100
For y=0 To 100
roadmap(x,y) = 0
Next
Next
End If

If param4 = True Then ; clear all fortresses
For x=0 To 100
For y=0 To 100
fortressmap(x,y) = 0
Next
Next
End If

If param5 = True Then ; clear map
For x=0 To 100
For y=0 To 100
map(x,y) = 0
Next
Next
End If

End Function

;
; This gives some help on the game
;
Function helpmenu()

greadtext("help.txt",1)
gtextviewer(100,100,500,300)

DrawImage mapbuffer,0,0
drawgamescreen
FlushKeys:FlushMouse
End Function

; calculate the framerate
;
;
Function drawframerate()
	fpscounter2=fpscounter2+1
	If fpstimer+1000 < MilliSecs()
		fpstimer = MilliSecs()
		fpscounter = fpscounter2
		fpscounter2=0
	End If
End Function

;
; Hold the program flow
;
;
Function wacht(i)
t = MilliSecs()
While t+i>MilliSecs()
Wend
End Function

;
; toggle grid
;
;
Function togglegrid()
If KeyDown(29) And KeyDown(34) Then 
	If grid=False Then grid=True : Goto skipgrid
	If grid=True Then grid=False 
	.skipgrid
	drawmap
	wacht(100)
End If
End Function

;
; Fill up the map with the maximum amount of units
;
;
Function fillmap()
Cls
;Locate 0,0 : Print "Adding 1000 units to the world ..."
Print "If you have debug enabled then it could take a while."
Flip
wacht(500)

For i=0 To maxunits
If units(i,0)=False Then
x% = Rnd(0,100)
y% = Rnd(0,100)
units(i,0) = True
units(i,1) = x
units(i,2) = y
units(i,3) = True
units(i,4) = Rnd(1,9)
units(i,5) = Rnd(1,5)
units(i,6) = Rnd(1,5)
units(i,7) = 0
units(i,8) = False
units(i,9) = False
;units(i,10)=1
unitsmove(i) = 1
units(i,11)=Rnd(1,5)
End If
Next
;
zappa=0
For i=0 To maxunits
If units(i,0)=True Then
x = units(i,1)
y = units(i,2)
For ii=0 To maxunits
If units(ii,0)=True
If Not i=ii Then
If x = units(ii,1) And y=units(ii,2) Then
units(ii,3)=False
units(ii,11) = units(i,11)
zappa = zappa+1
Text 0,80,Rnd(0,10)
End If
End If
End If
Next
End If
Next
;
Cls
;Locate 0,0
Print "Added 1000 units to the world"
Print "Press any key"
Flip
WaitKey
drawminimap : maketempmap
End Function

; Here a function to center the map on a x and y coordinate
Function centerscreen(x,y)
; hold the units on the map


centerx = mapx+14
centery = mapy+15


If x<centerx Then
ax = centerx-x

mapx=mapx-ax
End If

If x>centerx Then
ax = x-centerx
mapx=mapx+ax
;simplemessage("ax" + ax + "centerx" + centerx + "x" + x)
End If

If y<centery Then
ay = centery-y
mapy=mapy-ay
End If

If y>centery Then
ay=y-centery
mapy=mapy+ay
End If

If mapx<-20 Then mapx=-20
If mapx>90 Then mapx=90
If mapy<-20 Then mapy=-20
If mapy>80 Then mapy=80

maketempmap
drawmap
drawminimap

End Function


;
; This function returns true if the screen needs centering
;
;
Function centerscreencheck(cx,cy)
isox=0:isoy=10
ux = 0
uy = 0 
activateflag = False
For y=0 To 19
	For x=0 To 10
		ux = (isox + x)+mapx 
		uy = (isoy - x)+mapy
		
		If cx=ux And cy=uy Then
		activateflag = True
		If x*64<50 Or x*64>560 Or y*32<50 Or y*32>750 Then
		Return True
		End If
		End If
		
		ux=ux+1	
		
		If cx=ux And cy=uy Then
		activateflag = True
		If x*64<50 Or x*64>560 Or y*32<50 Or y*32>750 Then
		Return True
		End If
		End If

	Next
	isox = isox+1
	isoy = isoy+1			
Next

If activateflag = True Then Return False Else Return True
End Function


;
; This is a function to flicker a timer on and of on a 1/5 second basis
;
Function doflickertimer()
If flickertimer+300 < MilliSecs() Then
flickerswitch = - flickerswitch
flickertimer=MilliSecs()
End If
End Function

Function isocollision()
; slow but simple collision. Only triggered when the mouse is pressed meaning it does not
; increase on CPU usage in the main program flow.

activecursor = True ; if this is true then the visible cursor will show
collision = False
; When then button is held down
If moused(1) = True Or moused(2) = True Then
; These two If's are For only inputting the mouse from inside the playing Field
If MouseX()>0 And MouseX()<screenwidth-160 Then
If MouseY()>0 And MouseY()<screenheight Then
isox = 0
isoy = 10
tx = 0 ; This is a counter for the loop
ty = 0 ; Same here
For y=0 To 18
For x=0 To 10
tx = isox + x : ty = isoy - x
If ImagesCollide (singlepixel,MouseX(),MouseY(),0,isodesert,(x*64)-32,(y*32)-16,0) Then
;currentunit(0) = tx + mapx
;currentunit(1) = ty + mapy
cursor(0) = tx+mapx
cursor(1) = ty+mapy
Goto skipout
End If
tx = (isox + x) + 1 : ty = isoy - x
If ImagesCollide(singlepixel,MouseX(),MouseY(),0,isodesert,(x*64),(y*32),0) Then
;currentunit(0) = (tx)+mapx
;currentunit(1) = (ty+1)+mapy
cursor(0) = tx+mapx
cursor(1) = ty+mapy
Goto skipout
End If
Next
isox = isox + 1
isoy = isoy + 1
Next


.skipout


If moused(1) = True Then

; Check if there is a city on the location
For i=0 To maxcities
	If cities(i,0) = True Then ; check if the city is active
	If cities(i,1) = cursor(0) And cities(i,2) = cursor(1) Then ; check if the city is on the selected cursor coordinates
	If cities(i,4) = currentplayer Then	
	;
	;
	; Tutorial
	;
	tm = tutorial_cities
	If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
	;
	;
	activecursor = False
	Delay(200) : FlushMouse() : FlushKeys()
	cityscreen(i)
	drawmap
	Goto icskipout1
	End If
	End If
	End If
Next


If checkformultipleunits(cursor(0),cursor(1))>1 Then

;
;
; Tutorial
;
tm = tutorial_units
If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
;
.shit
activecursor = False
showunitsatcursor(cursor(0),cursor(1))
drawmap
Else

; Check if there is a unit on the location
For i=0 To maxunits
If units(i,0) = True Then
If units(i,3) = True Then
If units(i,11) = currentplayer Then
x = units(i,1)
y = units(i,2)
If x = cursor(0) And y = cursor(1) Then 
If units(i,3)=True Then ; is the unit ontop
;If units(i,10)>0 Then ; has the unit moves left
If unitsmove(i) > 0 Then ; has the unit moves left
activecursor = False
units(i,8)=False ; disable fortified
units(i,13) = 0 ; disable flag
currentunit(0) = x
currentunit(1) = y
currentunit(2) = i 
collision = True
;
;
; Tutorial
;
tm = tutorial_units
If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True

;
End If
End If
End If
End If
End If
End If
Next

End If


End If ; if moused(1) = true

.icskipout1
If MouseHit(1) = True Or MouseHit(2) = True Then
If MouseX()<40 Or MouseX()>540 Or MouseY()<40 Or MouseY()>560 Then
If MouseX()>3 Or MouseX()<screenwidth-150 Or MouseY()>3 Or MouseY()<600-3 Then
centerscreen(cursor(0),cursor(1)) : Delay(20)
End If
End If
End If

; This section let the visible cursor be displayed
;
If activecursor = True Then 
visiblecursor = True ; activate the visible cursor
currentunit(2) = -1
Else
visiblecursor = False ; disable the visible cursor
End If

drawminimap : maketempmap:drawmap
;wacht(100)
;roadmap(cursor(0),cursor(1))=1 :updateroadmap(cursor(0),cursor(1))
;updateroadmap(cursor(0),cursor(1))
;simplemessage(roadmap(cursor(0),cursor(1)) + " | " + roadmapstring(cursor(0),cursor(1)))

End If
End If
End If


End Function




;
; Speed it up a bit 
;
;

Function maketempmap()

; Erase the tempmap
For x = 0 To 29
For y = 0 To 41
For i=0 To 6
tempmap(x,y,i) = False
Next
citymap(x,y,0) = False
citymap(x,y,1) = False
Next
Next

;

; Place the fortresses in the tempmap
For x=0 To 29
For y=0 To 41
If x+mapx=>0 And x+mapx=<mapwidth Then
If y+mapy=>0 And y+mapy=<mapheight Then
If fortressmap(x+mapx,y+mapy) = 1 Then tempmap(x,y,4) = 1
End If
End If
Next
Next

;
; first the find the movable/visible units
;
;
For i = 0 To maxunits
If units(i,0) = True Then ; if the unit is active
If units(i,14) = False Then ; if the unit is not inside a city
x = units(i,1)
y = units(i,2)
If x=>mapx And x<mapx+29 Then ; if the unit x coordinate is inside the current screen
If y=>mapy And y<mapy+(19*2)+1 Then ; if the unit y coordinate is inside the current screen
; Count the number of units in the stack
tempmap(x-mapx,y-mapy,6) = tempmap(x-mapx,y-mapy,6) + 1
If units(i,3) = True Then ; if the unit is ontop
tempmap(x-mapx,y-mapy,0) = units(i,4) ; fill the temp map with the current unit type
tempmap(x-mapx,y-mapy,1) = units(i,8) ; is this one fortified?

If units(i,13)=2 Or units(i,13) = 3 Then
tempmap(x-mapx,y-mapy,2) = True ; units(i,13) ; Is this one building?
End If


; Is this one sentry
If units(i,13) = 10 Then
tempmap(x-mapx,y-mapy,3) = True
End If

; If the ai slot is set to building a road 
If units(i,13) = 1 Then 
tempmap(x-mapx,y-mapy,5) = True
End If

; If the ai slot is set to building a road 
If units(i,13) = 6 Then 
tempmap(x-mapx,y-mapy,2) = True
End If



If x=currentunit(0) And y=currentunit(1) Then flickerstat(0)=x-mapx:flickerstat(1)=y-mapy
playermap(x-mapx,y-mapy) = units(i,11)
End If
End If
End If
End If
End If
Next


;
; Find the visible cities
;
;
For i=0 To maxcities
If cities(i,0) = True Then
x = cities(i,1)
y = cities(i,2)
If x=>mapx And x<mapx+29 Then ; if the unit x coordinate is inside the current screen

If y=>mapy And y<mapy+(19*2)+1 Then ; if the unit y coordinate is inside the current screen
citymap(x-mapx,y-mapy,0) = cities(i,3) ; send the city type into the temp map
citymap(x-mapx,y-mapy,1) = i ; send the city number into the temp map
playercitymap(x-mapx,y-mapy) = cities(i,4) ; send the owner of city in playercitymap
End If
End If
End If
Next

;
; Draw the roads on a bitmap
;
;
;
SetBuffer ImageBuffer(roadbufferimage)
Cls
isox = 0
isoy = 10
ux = 0
uy = 0
For y=0 To 19
For x=0 To 10
ux = (isox + x)+mapx
uy = (isoy - x)+mapy

If ux=>0 And uy=>0 And ux=<100 And uy=<100 Then
;If roadmap(ux,uy) = 1 Then drawroad(ux,uy,layer1(x,y,0,0),layer1(x,y,1,0))
If roadmap(ux,uy) = 1 Then
If vismap(ux,uy,currentplayer)=True Or fogofwarenabled = False Then drawroad(ux,uy,x*64-32,y*32-16)
End If
End If
ux=ux+1
If ux=>0 And uy=>0 And ux=<100 And uy=<100 Then
;If roadmap(ux,uy) = 1 Then drawroad(ux,uy,layer1(x,y,0,1),layer1(x,y,1,1))
If roadmap(ux,uy) = 1 Then
If vismap(ux,uy,currentplayer)=True Or fogofwarenabled = False Then drawroad(ux,uy,x*64+32-32,y*32+16-16)
End If
End If

Next
isox=isox+1
isoy=isoy+1
Next
SetBuffer BackBuffer()


End Function

;
; This function draws the background tiles
;
;
Function drawbacktile(x,y,n,x1,y1)
Select n
Case 0
DrawImage isogras,x,y
Case 1
DrawImage isogras,x,y
DrawImage isohills,x,y
Case 2
DrawImage isogras,x,y
DrawImage isomountain,x,y
Case 3
DrawImage isogras,x,y
DrawImage isopinetrees,x,y
Case 4
DrawImage isoswamp,x,y
Case 5
DrawImage isogras,x,y
DrawImage isotrees,x,y
Case 6
DrawImage isogras,x,y
DrawImage isogoldmountain,x,y
Case 7
DrawImage isogras,x,y
DrawImage isogoldmountain,x,y
DrawImage isomine,x,y
Case 8
DrawImage isogras,x,y
DrawImage isocrops,x,y
Case 9
DrawImage isogras,x,y
DrawImage isotrees,x,y
DrawImage isobeaver,x,y
Case 64 ; water
DrawImage isowater,x,y
;

;coastlinemap((x*2),(y*2)) = 14
;coastlinemap((x*2)+1,(y*2)) = 46
;coastlinemap((x*2),(y*2)+1) = 47
;coastlinemap((x*2)+1,(y*2)+1) = 30

If RectsOverlap(x1*2,y1*2,1,1,0,0,mapwidth*2+4,mapheight*2+4) = True Then DrawImage isocoastlines,x+16,y,coastlinemap(x1*2,y1*2)
If RectsOverlap((x1*2)+1,y1*2,1,1,0,0,mapwidth*2+4,mapheight*2+4) = True Then DrawImage isocoastlines,x,y+8,coastlinemap((x1*2)+1,(y1*2))
If RectsOverlap(x1*2,(y1*2)+1,1,1,0,0,mapwidth*2+4,mapheight*2+4) = True Then DrawImage isocoastlines,x+32,y+8,coastlinemap((x1*2),(y1*2)+1)
If RectsOverlap((x1*2)+1,(y1*2)+1,1,1,0,0,mapwidth*2+4,mapheight*2+4) = True Then DrawImage isocoastlines,x+16,y+16,coastlinemap((x1*2)+1,(y1*2)+1)
;If x1*2=>0 And y1*2=>0 And x1*2=<mapwidth*2 And y1*2=<mapheight*2 Then DrawImage isocoastlines,x+16,y,coastlinemap(x1*2,y1*2)
;If x1*2=>0 And y1*2=>0 And x1*2=<mapwidth*2 And y1*2=<mapheight*2 Then DrawImage isocoastlines,x,y+8,coastlinemap((x1*2)+1,(y1*2))
;If x1*2=>0 And y1*2=>0 And x1*2=<mapwidth*2 And y1*2=<mapheight*2 Then DrawImage isocoastlines,x+32,y+8,coastlinemap((x1*2),(y1*2)+1)
;If x1*2=>0 And y1*2=>0 And x1*2=<mapwidth*2 And y1*2=<mapheight*2 Then DrawImage isocoastlines,x+16,y+16,coastlinemap((x1*2)+1,(y1*2)+1)

;DrawImage isocoastlines,x+16,y,14
;DrawImage isocoastlines,x,y+8,46
;DrawImage isocoastlines,x+32,y+8,47
;DrawImage isocoastlines,x+16,y+16,30


      
End Select
End Function


;
; This function draws the map/Units/cities
;
;
Function drawmap2()
;
; The first layer handles the scene graphics. Currently only 1 graphic
;
;
SetBuffer ImageBuffer(mapbuffer2)
Cls
isox=0:isoy=10
ux = 0
uy = 0 
switch = 1
;15 ; 7
SetFont minifont
;Color 255,255,255
Color 0,0,0
; 
; Draw the ground tiles layer
;
For i=0 To 429-1
ux = groundlayer(i,0) + mapx
uy = groundlayer(i,1) + mapy
ux2 = groundlayer(i,0) 
uy2 = groundlayer(i,1) 

dx = groundlayer(i,2)
dy = groundlayer(i,3)

If ux=>0 And uy=>0 And ux=<mapwidth And uy=<mapheight Then


If vismap(ux,uy,currentplayer) = True Or fogofwarenabled = False Then 

	zetx = -1
	zety = -1
	If RectsOverlap(ux2-1,uy2,1,1,0,0,100,100) = False Then zetx = 0
	If RectsOverlap(ux2,uy2-1,1,1,0,0,100,100) = False Then zety = 0

	;If (Not citymap(ux2+zetx,uy2,0) > 0) And (Not citymap(ux2,uy2+zety,0) > 0) Then
	;If (Not map(ux+zetx,uy) = 0 ) And (Not map(ux,uy+zety) = 0) Then
	If (Not citymap(ux2+zetx,uy2,0) > 0) And (Not citymap(ux2,uy2+zety,0) > 0) Then
		drawbacktile(dx,dy,map(ux,uy),ux,uy)
		Else
		If map(ux,uy) > 0 Then drawbacktile(dx,dy,map(ux,uy),ux,uy)
	End If
	;EndIf
	;End If


	; Draw the fortress
	setfortress = False
	If tempmap(ux2,uy2,4) > 0 Then
		DrawImage fortress,dx,dy-16
		setfortress = True
	End If





	If citymap(ux2,uy2,0) > 0 Then 
		;If vismap(ux+mapx,uy+mapy,currentplayer) = True Then
		If map(ux,uy+1) = 0 Then drawbacktile(dx-32,dy+16,0,ux,uy)
		If map(ux+1,uy) = 0 Then drawbacktile(dx+32,dy+16,0,ux,uy)		
		;If map(ux+1,uy) = 64 Then drawbacktile(dx-32,dy+16,64,ux,uy)
		;If map(ux,uy+1) = 64 Then drawbacktile(dx+32,dy+16,64,ux,uy)
		DrawImage citygraphics(citymap(ux2,uy2,0)-1),dx,dy-10
	End If

	; Draw the iso cursor
	If visiblecursor = True Then
	If ux = cursor(0) And uy = cursor(1) Then
	;DrawImage isocursor,layer1(x,y,0,0),layer1(x,y,1,0)
	DrawImage isocursor,dx,dy
	End If
	End If

	; If the grid is on
	If grid=True Then
	Text (dx)-10,(dy)-8,ux-1 +"," + uy
	DrawImage isogrid,dx,dy
	End If

End If

End If

Next

SetFont mainfont

DrawImage roadbufferimage,0,0


; layer 2 , cities and such

For i=0 To 429-1
ux = groundlayer(i,0)
uy = groundlayer(i,1)
dx = groundlayer(i,2)
dy = groundlayer(i,3)

If ux=>0 And uy=>0 And ux=<mapwidth And uy=<mapheight Then
If ux+mapx=>0 And ux+mapx=<100 And uy+mapy=>0 And uy+mapy=<100 Then

If vismap(ux+mapx,uy+mapy,currentplayer) = True Or fogofwarenabled = False Then

	If citymap(ux,uy,0) > 0 Then 
		;If vismap(ux+mapx,uy+mapy,currentplayer) = True Then
		;DrawImage citygraphics(citymap(ux,uy,0)-1),dx,dy-10
		; get the player color
		playernumber = playercitymap(ux,uy)
		Color 255,255,255
		Rect dx+47,dy-5,10,10,1
		Color 0,0,0
		Rect dx+47,dy-5,10,10,0
		r = playercolor(playernumber,0)
		g = playercolor(playernumber,1)
		b = playercolor(playernumber,2)
		Color r,g,b
		Oval dx+48,dy-4,8,8
		; Draw the city name on the city
		Color 255,255,255
		Text dx+28,dy+28,citiesstring$(citymap(ux,uy,1),0),1,1
		;DebugLog playernumber
		;End If
	End If

End If

End If
End If

Next







;
; The third layer handles all the units, including the extra info.
;
;

For i=0 To 429-1
ux = groundlayer(i,0) ;+ mapx
uy = groundlayer(i,1) ;+ mapy
dx = groundlayer(i,2)
dy = groundlayer(i,3)

If ux=>0 And uy=>0 And ux=<mapwidth And uy=<mapheight Then
If ux+mapx=>0 And ux+mapx=<100 And uy+mapy=>0 And uy+mapy=<100 Then

	If vismap(ux+mapx,uy+mapy,currentplayer) = True Or fogofwarenabled = False Then

;		setfortress = False
;		If tempmap(ux,uy,4) > 0 Then
;			DrawImage fortress,dx,dy-16
;			setfortress = True
;		End If


		If tempmap(ux,uy,0) > 0 Then

			; This section is for the flickering
			drawit=True

			If currentunit(2)>-1
			If currentunit(0)-mapx=ux
			If currentunit(1)-mapy=uy
			drawit=False
			If RectsOverlap(dx,dy,1,1,30,30,560-30,600-30) = False Then
			centerscreen(currentunit(0),currentunit(1)) : Return
			;drawimagealpha(unitgraphics(tempmap(ux,uy,0)-1),ImageBuffer(mapbuffer2),dx,dy-16,.6,135,83,135,0)			
			End If
;			End If
			End If
			End If
			End If

			If drawit=True
				If tempmap(ux,uy,1) = True And setfortress = False Then 
					DrawImage fortified2,dx,dy-16
				End If
				
				DrawImage unitgraphics(tempmap(ux,uy,0)-1),dx,dy-16		
				; get the player color
				playernumber = playermap(ux,uy)
				Color 255,255,255
				Rect dx+47,dy-17,10,10,1
				Color 0,0,0
				Rect dx+47,dy-17,10,10,0
				r = playercolor(playernumber,0)
				g = playercolor(playernumber,1)
				b = playercolor(playernumber,2)
				Color r,g,b
				Oval dx+48,dy-16,8,8
				; If the unit is building a road
			 	If tempmap(ux,uy,5) = True Then
					DrawImage buildingroad,dx+47,dy-8
				End If
				; If the unit is fortified
				If tempmap(ux,uy,1) = True Then
					DrawImage fortified,dx+47,dy-8
				End If
				; building
				If tempmap(ux,uy,2) = True Then
					DrawImage building,dx+47,dy-8
				End If
				; Sentry
				If tempmap(ux,uy,3) = True Then
					DrawImage sentry,dx+47,dy-8
				End If
				; Is this unit stacked
				If tempmap(ux,uy,6)-1 > 0 Then
					stackgraph = tempmap(ux,uy,6)-1
					If tempmap(ux,uy,6)-1>4 Then stackgraph = 4
				    DrawImage stackedgraphics,dx+37,dy-8,stackgraph
				End If
			Else ;not visioble
				;drawimagealpha(unitgraphics(tempmap(ux,uy,0)-1),mapbuffer2,dx,dy-16,.6,135,83,135,0)

				drawunitalpha(unitgraphics(tempmap(ux,uy,0)-1),mapbuffer2,dx,dy-16,0.4)

			End If
			
		End If
		
	End If

End If		
End If


Next


.dmskipout
SetBuffer BackBuffer()

End Function

;
; This function draws the map/Units/cities
;
;
Function drawmap()
;
; The first layer handles the scene graphics. Currently only 1 graphic
;
;
SetBuffer ImageBuffer(mapbuffer)
Cls
isox=0:isoy=10
ux = 0
uy = 0 
switch = 1
;15 ; 7
SetFont minifont
;Color 255,255,255
Color 0,0,0
; 
; Draw the ground tiles layer
;
For i=0 To 429-1
ux = groundlayer(i,0) + mapx
uy = groundlayer(i,1) + mapy
ux2 = groundlayer(i,0)
uy2 = groundlayer(i,1)
dx = groundlayer(i,2)
dy = groundlayer(i,3)

If ux=>0 And uy=>0 And ux=<mapwidth And uy=<mapheight Then

	If vismap(ux,uy,currentplayer) = True Or fogofwarenabled = False Then

		zetx = -1
		zety = -1
		If RectsOverlap(ux2-1,uy2,1,1,0,0,100,100) = False Then zetx = 0
		If RectsOverlap(ux2,uy2-1,1,1,0,0,100,100) = False Then zety = 0

		If (Not citymap(ux2+zetx,uy2,0) > 0) And (Not citymap(ux2,uy2+zety,0) > 0) Then
			drawbacktile(dx,dy,map(ux,uy),ux,uy)
			Else
			If map(ux,uy) > 0 Then drawbacktile(dx,dy,map(ux,uy),ux,uy)
		End If

	; Draw the fortress
	setfortress = False
	If tempmap(ux2,uy2,4) > 0 Then
		DrawImage fortress,dx,dy-16
		setfortress = True
	End If




		If citymap(ux2,uy2,0) > 0 Then 
			;If map(ux,uy) = 0 Or map(ux,uy) = 64 Then drawbacktile(dx-32,dy+16,0,ux,uy)
			;If map(ux,uy) = 0 Or map(ux,uy) = 64 Then drawbacktile(dx+32,dy+16,0,ux,uy)
			If map(ux,uy+1) = 0 Then drawbacktile(dx-32,dy+16,0,ux,uy)
			If map(ux+1,uy) = 0 Then drawbacktile(dx+32,dy+16,0,ux,uy)		

			DrawImage citygraphics(citymap(ux2,uy2,0)-1),dx,dy-10
		End If

	
		; Draw the iso cursor
		If visiblecursor = True Then
		If ux = cursor(0) And uy = cursor(1) Then
		;DrawImage isocursor,layer1(x,y,0,0),layer1(x,y,1,0)
		DrawImage isocursor,dx,dy
		End If
		End If

		; If the grid is on
		If grid=True Then
		Text (dx)-10,(dy)-8,ux-1 +"," + uy
		DrawImage isogrid,dx,dy
		End If

		End If
		
	End If

Next

SetFont mainfont

DrawImage roadbufferimage,0,0


; layer 2 , cities and such

For i=0 To 429-1
ux = groundlayer(i,0) ;+ mapx
uy = groundlayer(i,1) ;+ mapy
dx = groundlayer(i,2)
dy = groundlayer(i,3)

If ux=>0 And uy=>0 And ux=<mapwidth And uy=<mapheight Then
If ux+mapx=>0 And ux+mapx=<100 And uy+mapy=>0 And uy+mapy=<100 Then

	;If vismap(ux,uy,currentplayer) = True Then

		If citymap(ux,uy,0) > 0 Then
		If vismap(ux+mapx,uy+mapy,currentplayer) = True Or fogofwarenabled = False Then 
		;DrawImage citygraphics(citymap(ux,uy,0)-1),dx,dy-10
		; get the player color
		playernumber = playercitymap(ux,uy)
		Color 255,255,255
		Rect dx+47,dy-5,10,10,1
		Color 0,0,0
		Rect dx+47,dy-5,10,10,0
		r = playercolor(playernumber,0)
		g = playercolor(playernumber,1)
		b = playercolor(playernumber,2)
		Color r,g,b
		Oval dx+48,dy-4,8,8
		; Draw the city name on the city
		Color 255,255,255
		Text dx+28,dy+28,citiesstring$(citymap(ux,uy,1),0),1,1
		;DebugLog playernumber
		End If
		End If
		;
	;End If

End If
End If

Next
;
; The third layer handles all the units, including the extra info.
;
;

For i=0 To 429-1
ux = groundlayer(i,0) ;+ mapx
uy = groundlayer(i,1) ;+ mapy
dx = groundlayer(i,2)
dy = groundlayer(i,3)

If ux=>0 And uy=>0 And ux=<mapwidth And uy=<mapheight Then
If ux+mapx=>0 And ux+mapx=<100 And uy+mapy=>0 And uy+mapy=<100 Then

	If vismap(ux+mapx,uy+mapy,currentplayer) = True Or fogofwarenabled = False Then

; Draw the fortress
;		setfortress = False
;		If tempmap(ux,uy,4) > 0 Then
;			DrawImage fortress,dx,dy-16
;			setfortress = True
;		End If


		If tempmap(ux,uy,0) > 0 Then

			; This section is for the flickering
			drawit=True

;			If currentunit(2)>-1
;			If currentunit(0)-mapx=ux
;			If currentunit(1)-mapy=uy
;			If flickerswitch=-1 Then drawit=False
;			End If
;			End If
;			End If

			If drawit=True
				If tempmap(ux,uy,1) = True And setfortress = False Then 
					DrawImage fortified2,dx,dy-16
				End If
				
				DrawImage unitgraphics(tempmap(ux,uy,0)-1),dx,dy-16		
				; get the player color
				playernumber = playermap(ux,uy)
				Color 255,255,255
				Rect dx+47,dy-17,10,10,1
				Color 0,0,0
				Rect dx+47,dy-17,10,10,0
				r = playercolor(playernumber,0)
				g = playercolor(playernumber,1)
				b = playercolor(playernumber,2)
				Color r,g,b
				Oval dx+48,dy-16,8,8
				; If the unit is building a road
			 	If tempmap(ux,uy,5) = True Then
					DrawImage buildingroad,dx+47,dy-8
				End If
				; If the unit is fortified
				If tempmap(ux,uy,1) = True Then
					DrawImage fortified,dx+47,dy-8
				End If
				; building
				If tempmap(ux,uy,2) = True Then
					DrawImage building,dx+47,dy-8
				End If
				;if the unit is on sentry
				If tempmap(ux,uy,3) = True Then
					DrawImage sentry,dx+47,dy-8
				End If
				; Is this unit stacked
				If tempmap(ux,uy,6)-1 > 0 Then
					stackgraph = tempmap(ux,uy,6)-1
					If tempmap(ux,uy,6)-1>4 Then stackgraph = 4
				    DrawImage stackedgraphics,dx+37,dy-8,stackgraph
				End If
			End If
			
		End If
		
	End If

End If		
End If

Next


.dmskipout
SetBuffer BackBuffer()
drawmap2()
End Function

; This function stores all the blitting coordinates in arrays. This to speed up the loop
;
;
;
Function presetmap()
;
; Buffer the map 
;
isox=0:isoy=10
ux = 0
uy = 0 
switch = 1
counter = 0
For y=0 To 38;19
	For x=0 To 10
		ux = (isox + x)
		uy = (isoy - x)
		
		If switch = 1 Then
		dx = (x*64)-32 : dy = (y*16)-16
		Else
		dx = x*64 : dy = (y*16)-16
		End If
		
		groundlayer(counter,0) = ux
		groundlayer(counter,1) = uy
		groundlayer(counter,2) = dx
		groundlayer(counter,3) = dy
			
		counter = counter + 1
		
	Next
	If switch = 1 Then isox = isox+1
	If switch = -1 Then isoy = isoy + 1
	switch = - switch
Next




;15 ; 7
For y=0 To 19
	For x=0 To 10	
		xx = (x*64)-32
		yy = (y*32)-16
		xxx = ((x*64)+32)-32
		yyy = ((y*32)+16)-16
		layer1(x,y,0,0)=xx
		layer1(x,y,1,0)=yy
		layer1(x,y,0,1)=xxx
		layer1(x,y,1,1)=yyy
	Next
Next

;15 ; 7
For y=0 To 19
	For x=0 To 10
		xx =((x*64)-3)-28
		yy = ((y*32)-3)-35
		xxx = (x*64)+(32-3)-28
		yyy = ((y*32)+(16-3))-35
		layer2(x,y,0,0)=xx
		layer2(x,y,1,0)=yy
		layer2(x,y,0,1)=xxx
		layer2(x,y,1,1)=yyy	
	Next
Next


isox=0:isoy=10
ux = 0
uy = 0 
;15 ; 7
For y=0 To 19
	For x=0 To 10
		ux = (isox + x)
		uy = (isoy - x)
		
		my.screencoords1 = New screencoords1
		my\x = ux
		my\y = uy
		
		coordinatebuffer(x,y,0) = ux
		coordinatebuffer(x,y,1) = uy
		ux=ux+1 
		coordinatebuffer(x,y,2) = ux
		coordinatebuffer(x,y,3) = uy
	Next
	isox = isox+1
	isoy = isoy+1	
Next





End Function




;
; This function draw the road
;
;
;
Function drawroad(x,y,dx,dy)
If roadmapstring$(x,y) = "22222222" Then
DrawImage roadimage(0),dx,dy 
Else
For i=1 To 8
If Mid(roadmapstring$(x,y),i,1)="1" Then
DrawImage roadimage(i),dx,dy
End If
Next
End If
End Function



;
; This function sets up the entire map with roads
;
;
;
Function setuproad()
; first add roads to all cities
For i=0 To maxcities
If cities(i,0) = True Then
roadmap(cities(i,1),cities(i,2)) = 1
End If
Next

For y=0 To mapheight
For x=0 To mapwidth
	s$="00000000"
	If roadmap(x,y) = 1 Then
		s$ = ""
		For i=1 To 8
			n=returnmapposition(x,y,roadscheme(i),0)
			x1=getxposition(n)
			y1=getyposition(n)
			If x1=>0 And x1=<mapwidth And y1=>0 And y1<=mapheight Then
				If roadmap(x1,y1) = 1 Then 
					s$=s$+"1"
					Else
				 	s$=s$+"0"
				End If
				Else
				s$=s$+"0"
			End If
		Next
		If s$="00000000" Then s$ = "22222222"
	End If
	roadmapstring$(x,y)=s$
Next
Next
End Function

;
; This function must be called if a new road is layed
;
;

Function updateroadmap(x,y)

; first check the surrounding area for roads
For i=1 To 8
	n=returnmapposition(x,y,roadscheme(i),1)
	x1=getxposition(n)
	y1=getyposition(n)
	If x1=>0 And x1=<mapwidth And y1=>0 And y1<=mapheight Then
		If roadmap(x1,y1) = 1 Then  
			s$=s$+"1"
			Else
		 	s$=s$+"0"
		End If
		Else
		s$=s$+"0"
	End If
Next
If s$="00000000" Then s$ = "22222222"
roadmapstring$(x,y)=s$

; Then update each of the surrounding area's 

For i=1 To 8
	n = returnmapposition(x,y,roadscheme(i),1)
	x1 = getxposition(n)
	y1 = getyposition(n)
	s$=""
	For ii=1 To 8		
		n2 = returnmapposition(x1,y1,roadscheme(ii),1)
		x2 = getxposition(n2)
		y2 = getyposition(n2)
		If x2=>0 And x2=<mapwidth And y2=>0 And y2<=mapheight Then
			If roadmap(x2,y2) = 1 Then
				s$=s$ + "1"
				Else 
				s$=s$ + "0"
			End If
			Else
			s$ = "0"
		End If
	Next
	If s$="0000000" Then s$="22222222"
	If x1>0 And x1<mapwidth And y1>0 And y1<mapheight Then roadmapstring$(x1,y1) = s$
Next

End Function





;
; This function returns if the mouse is underneath a zone.
; t - 0 = draw no visible grid
; t - 1 = draw a visible grid with zone underneath the mouse
;
Function domousezone(x,y,w,h,zw,zh,z,t)
counter = 0
x1 = x:y1 = y
If t=1 Then
	Repeat 
		Rect x1,y1,zw,zh,0
		If MouseX()>x1 And MouseX()<x1+zw And MouseY()>y1 And MouseY()<y1+zh Then Text 0,0,counter
		counter = counter + 1
		x1=x1+zw
		If x1=>x+w Then x1 = x : y1=y1+zh
	Until y1=>y+h
End If

rq = -1

If MouseX()>x Then
If MouseY()>y Then
If MouseX()<x+w Then 
If MouseY()<y+h Then

ax = (MouseX()-x) / zw
ay = (MouseY()-y) / zh

rq = (ay*(w/zw)) + ax

End If
End If
End If
End If

If t=1 Then Text 0,20,rq

Return rq

End Function

Function unitcancarry(x,y)
For i=0 To maxunits
	If units(i,unit_Active) = True Then
		If units(i,unit_alignment) = currentplayer Then
			If units(i,unit_x) = x Then
				If units(i,unit_y) = y Then
					If unitdefault(units(i,unit_type),default_cancarry) > 0 Then
						If unitcarries(i) < units(i,unit_cancarry) Then
							Return i
						End If
					End If
				End If
			End If
		End If
	End If
Next
Return -1
End Function

Function unitcarries(num)
For i=0 To maxunits
	If units(i,unit_active) = True Then
		If units(i,unit_alignment) = currentplayer Then
			If units(i,unit_carried) = num Then counter = counter + 1
		End If
	End If
Next
Return counter
End Function

Function movecarriedunits()
For i=0 To maxunits
If units(i,unit_active) = True Then
If units(i,unit_alignment) = currentplayer Then
If units(i,unit_carried) > 0 Then
x = units(i,unit_x)
y = units(i,unit_y)
units(i,unit_x) = units(units(i,unit_carried),unit_x)
units(i,unit_y) = units(units(i,unit_carried),unit_y)

placeunitontop2(x,y)

;units(i,unit_x) = units(unit,unit_x)
;units(i,unit_y) = units(unit,unit_y)
;If units(i,unit_invisible) = False Then End
End If
End If
End If
Next
End Function

Function activatecarriedunits(unit)
For i=0 To maxunits
If units(i,unit_Active) = True Then
If units(i,unit_alignment) = currentplayer Then
If units(i,unit_carried) = unit Then 
units(i,unit_invisible) = False
units(i,unit_incity) = False
End If
End If
End If
Next
End Function

Function writedebug(debugstring$)
file = OpenFile("debug.txt")
If file>0 Then
While Eof(file) = False
a$= a$ + ReadLine$(file)
Wend
CloseFile(file)
End If

file = WriteFile("debug.txt")
WriteLine file,a$
WriteLine file,debugstring$
CloseFile(file)

End Function




;
; This function parses a string , results returned into parse$(x)
;
Function parsestring(in$)
	counter = 0
	For i=1 To Len(in$)
		a$ = Mid(Lower(in$),i,1)
		If (a$=>"a" And a$=<"z") Or (a$=>0 And a$<=9) Or (a$ = "_") Or (a$ = "-") Then
			b$ = b$ + a$
			ElseIf a$ = "," 
				parse$(counter) = Trim(b$)
				b$ = ""
				counter = counter + 1
		End If
	Next
	parse$(counter) = Trim(b$)
	Return counter
End Function


Function text2(x,y,t$)
Color 100,100,100
Text x-1,y,t$
Color 180,180,180
Text x+1,y,t$
Color 255,255,255
Text x,y,t$

End Function

Function getfilename$(filename$) ; Returns the filename and extension

	lastdir = 1
	For i=1 To Len(filename$)
		If Mid$(filename$,i,1) = "\" Then Lastdir = i
	Next
	If Lastdir > 1 Then Lastdir = Lastdir + 1
	For i=Lastdir To Len(filename$)
		a$ = a$ + Mid(filename$,i,1)
	Next
	Return a$
End Function

Function getextension$(filename$) ; Returns the extension minus the .
	lastdir = 1
	For i=1 To Len(filename$)
		If Mid$(filename$,i,1) = "." Then Lastdir = i
	Next
	If Lastdir > 1 Then Lastdir = Lastdir + 1
	For i=Lastdir To Len(filename$)
		a$ = a$ + Mid(filename$,i,1)
	Next
	Return a$
End Function

Function getdirectory$(filename$) ; Returns the complete directory including drive

	lastdir = 1
	For i=1 To Len(filename$)
		If Mid$(filename$,i,1) = "\" Then Lastdir = i
	Next
	For i=1 To Lastdir
		a$ = a$ + Mid(filename$,i,1)
	Next
	Return a$
End Function


Function mistertime(time,set)
	;
	; This function returns true when a 'time' number of millisecs() have passed since the last call.
	; Use 'set' for each different timer.
	; Dim mistertimer(5)
	Select set
		Case 0 : If mistertimer(0) + time < MilliSecs() Then mistertimer(0) = MilliSecs() : Return True
		Case 1 : If mistertimer(1) + time < MilliSecs() Then mistertimer(1) = MilliSecs() : Return True
		Case 2 : If mistertimer(2) + time < MilliSecs() Then mistertimer(2) = MilliSecs() : Return True
		Case 3 : If mistertimer(3) + time < MilliSecs() Then mistertimer(3) = MilliSecs() : Return True
		Case 4 : If mistertimer(4) + time < MilliSecs() Then mistertimer(4) = MilliSecs() : Return True
		Case 5 : If mistertimer(5) + time < MilliSecs() Then mistertimer(5) = MilliSecs() : Return True
	End Select
	Return False
End Function



