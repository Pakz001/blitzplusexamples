;
;
; Edit mode
;
;
;

;
; This function lets you draw a background tile
;


;
; This function displays a menu for creating units and stuff
;
;



Function editarea()

SetFont minifont

; Draw, run the buttons
If gbutton1(650,300,50,40,"Create",1,editcurrentbutton) Then editcurrentbutton = 1
editdrawunit(694,296,editcreateunittype)
If gbutton1(760,300,30,20,"Pr.",2,editcurrentbutton) Then editcurrentbutton = 2
If gbutton1(760,320,30,20,"Nxt",3,editcurrentbutton) Then editcurrentbutton = 3
Text 650,360,"Player",0,1
Color playercolor(editcreateunitplayer,0),playercolor(editcreateunitplayer,1),playercolor(editcreateunitplayer,2)
Rect 700,340,60,40,1
Color 255,255,255
Rect 700,340,60,40,0
If gbutton1(760,340,30,20,"Pr.",4,editcurrentbutton) Then editcurrentbutton = 4
If gbutton1(760,360,30,20,"Nxt",5,editcurrentbutton) Then editcurrentbutton = 5


; Execute the buttons
If MouseDown(1) = False And editcurrentbutton>0 Then

If editcurrentbutton = 1 Then ; create the new unit
If gcheckarea(650,300,50,40) = True Then
editcreatenewunit()
drawminimap : maketempmap:drawmap ; update the screen
End If
End If

If editcurrentbutton = 2 Then ; decrease unit graphic list
If gcheckarea(760,300,30,20)=True Then
	If editcreateunittype>1 Then editcreateunittype = editcreateunittype - 1 : wacht(50)
End If
End If
If editcurrentbutton = 3 Then ; increase unit graphic list
If gcheckarea(760,320,30,20) = True Then
	If editcreateunittype<numberofunitgraphics Then editcreateunittype = editcreateunittype + 1 :wacht(50)
End If
End If
If editcurrentbutton = 4 Then ; decrease player number
If gcheckarea(760,340,30,20)=True Then
	If editcreateunitplayer > 1 Then editcreateunitplayer = editcreateunitplayer - 1 : wacht(50)
End If
End If
If editcurrentbutton = 5 Then ; increase player number
If gcheckarea(760,360,30,20) = True Then
	If editcreateunitplayer < maxplayers Then editcreateunitplayer = editcreateunitplayer + 1:wacht(50)
End If
End If

editcurrentbutton=0
End If


SetFont mainfont
End Function

;
; This function draws a given unit to a given location
;
Function editdrawunit(x,y,n)
DrawImage unitgraphics(n-1),x,y
End Function

;
; This function creates a new unit on the map
;
;
Function editcreatenewunit()
unitcreated = False

If editcheckplayerunitlocation(cursor(0),cursor(1),editcreateunitplayer)=False Then 
simplemessage("Cannot create units of different alignments on same square")
Return False
End If

; check if we are not going to make units of a different kind in a city
For ii=0 To maxcities
If cities(ii,0) = True Then
If cities(ii,1) = cursor(0) And cities(ii,2) = cursor(1) Then
If Not editcreateunitplayer = cities(ii,4) Then galop = True 
End If
End If
Next

If galop = True Then simplemessage("Cannot create units of different alignments in the same city") : Return False


For i=1 To maxunits
If units(i,0) = False Then ;we have found our new unit

units(i,0) = True ; activate
units(i,1) = cursor(0) ; x loc on map
units(i,2) = cursor(1) ; y loc on map
units(i,3) = True ; ontop status
units(i,4) = editcreateunittype ; the new unit type
units(i,5) = unitdefault(editcreateunittype,0) ; attack default
units(i,6) = unitdefault(editcreateunittype,1) ; defence default
units(i,7) = 0 ; not implented (damage)
units(i,8) = False ; fortified
units(i,9) = False ; veteran
units(i,10)= unitdefault(editcreateunittype,2) ; movement default
unitsmove(i) = unitdefault(editcreateunittype,2)
units(i,11)= editcreateunitplayer ; player number
units(i,12)= False ; on hold
units(i,13)= 0 ; AI method
units(i,unit_cancarry) = unitdefault(editcreateunittype,5) ; can carry no of units
units(i,unit_invisible)  = False

; Assign the city label to the unit
a= returncityid(units(i,unit_x),units(i,unit_y))
If a>-1 Then
units(i,unit_homecity) = a
Else
units(i,unit_homecity) = -1
End If


If isunitoncity(i)=False Then
units(i,14)= False ; Inside city
Else
units(i,14) = True
End If

units(i,15)= 0 ; turns inactive ***********
placeunitontop(i)
; Set flag - unit is created
unitcreated = True
Return True : i=maxunits ; what the hell :)
End If
Next
; If this is triggered then it means then the maximum amount of units is used
If unitcreated = False Then
simplemessage("Cannot create more units")
End If
End Function

;
; This function checks if there are any units of other players on the location ; returns true/false
;
;
Function editcheckplayerunitlocation(x,y,player)
For i=0 To maxunits
If units(i,0) = True Then ; active ?
If units(i,1) = x And units(i,2) = y Then ; x and y the same?
; If there is a unit there and not our players then return false
If Not units(i,11) = player Then Return False 
End If
End If
Next

Return True
End Function

;
; Here we handle the key combinations that perform a edit feature
;
Function editshortcuts()
Local escLctrl = 29
Local escRctrl = 157
Local escLshift = 42
Local escRshift = 54
Local esckeyw = 17
;
; Shortcut for placing water on the map (Ctrl+Shift+w)
;
If KeyDown(esckeyw) = True Then
	If KeyDown(esclctrl) = True Or KeyDown(escrctrl) = True Then
		If KeyDown(esclshift) = True Or KeyDown(escrshift) = True Then
			; Set map tile here
			If RectsOverlap(cursor(0),cursor(1),1,1,0,0,mapwidth,mapheight) = True Then
		 		map(cursor(0),cursor(1)) = 64
			End If
		 	For x1=cursor(0)-1 To cursor(0) + 1
		 	For y1=cursor(1)-1 To cursor(1) + 1
		 	;If isnotoverbound(x1,mapwidth) = False And isnotoverbound(y1,mapheight) = False Then
		 	If RectsOverlap(x1,y1,1,1,0,0,mapwidth,mapheight) = True Then
			docoastline(x1,y1)
			End If
			;End If
			Next
			Next
			;
		End If
	End If
End If
; Shortcut for creating a city
If KeyDown(29) Or KeyDown(157) Then ; if pressed R or L CTRL
If KeyDown(46) = True Then
If visiblecursor = True Then
createnewcity(cursor(0),cursor(1))
drawminimapterrain()
updatescreen()
Delay(200)
FlushKeys()
End If
End If
End If

; Shortcut for destroying a tile
If KeyDown(29) Or KeyDown(157) Then ; if pressed R or L CTRL
If KeyDown(211) Then ; delete
If visiblecursor = True Then

; Destroy the map at the cursor
map(cursor(0),cursor(1)) = 0
; Destroy the fortress at the cursor
fortressmap(cursor(0),cursor(1)) = 0
; destroy the road at the cursor
roadmap(cursor(0),cursor(1)) = 0
; Destroy the mine at the cursor
minemap(cursor(0),cursor(1),0) = False
; Destroy all units at the cursor postion
For i=0 To maxunits
If units(i,0) = True Then
If units(i,1) = cursor(0) Then
If units(i,2) = cursor(1) Then
units(i,0) = False
End If
End If
End If
Next
; Destroy any city at the cursor postion
For i=0 To maxcities
If cities(i,0) = True Then
If cities(i,1) = cursor(0) Then
If cities(i,2) = cursor(1) Then
cities(i,0) = False
End If
End If
End If
Next
; update the screen
updateroadmap(cursor(0),cursor(1))
maketempmap : drawmap :  drawminimap


;
End If
End If
End If

; Shortcut for building a fortress
If KeyDown(29) Or KeyDown(157) Then ; if pressed R or L CTRL
If KeyDown(33) Then ; if pressed 'f'
If visiblecursor = True Then
fortressmap(cursor(0),cursor(1)) = 1
maketempmap : drawmap : drawminimap
End If
End If
End If
; Shortcut for building a road
If KeyDown(29) Or KeyDown(157) Then ; if pressed R or L CTRL
If KeyDown(19) Then ; if pressed 'r'
If visiblecursor = True Then ; if the cursor is visible
roadmap(cursor(0),cursor(1)) = 1 ; add to the road map
updateroadmap(cursor(0),cursor(1)) ; update the roadmap
maketempmap : drawmap : drawminimap
End If
End If
End If
; Shortcut for the screnario background editscreen
If KeyDown(29) Or KeyDown(157) Then ; if pressed R or L Ctrl
If visiblecursor = True Then ; if the cursor is visible
For i=2 To 11
If KeyDown(i) Then 
map(cursor(0),cursor(1))=i-2
If i-2 = 7 Then
minemap(cursor(0),cursor(1),0) = True
minemap(cursor(0),cursor(1),1) = currentplayer
minemap(cursor(0),cursor(1),2) = Rand(50)+25
End If
maketempmap : drawmap :

terraineditchangeeffectactive = True
terraineditchangeeffect = MilliSecs()

drawminimap
End If
Next
End If
End If

If KeyDown(29) Or KeyDown(57) Then ; if pressed r or l ctrl
If KeyDown(211) = True Then ; if pressed del
If visiblecursor = True Then ; if the cursor is visible
destroyunitsatcursor(cursor(0),cursor(1))
End If
End If
End If


End Function


Function destroyunitsatcursor(x,y)
For i=0 To maxunits
If units(i,0) = True Then
If x = units(i,1) Then
If y = units(i,2) Then
units(i,0) = False
End If
End If
End If
Next

End Function

Function createnewcity(xloc,yloc)

destroyunitsatcursor(xloc,yloc)
fortressmap(xloc,yloc) = 0
roadmap(xloc,yloc) = 1
updateroadmap(xloc,yloc)

newcity = False
For i=0 To maxcities
If cities(i,0) = False Then
newcity = i
Exit
End If
Next

cities(newcity,0) = True
cities(newcity,1) = xloc
cities(newcity,2) = yloc
a$ = ginputbox$(200,200,400,200,"Input city name (nothing = auto)",createcitydefault$(0),"Ok")
If a$ = "" Then a$ = returncityname$()
createcitydefault$(0)= a$
citiesstring$(newcity,0) = a$
a$ = ginputbox$(200,200,400,200,"Input city size (1,2,3,4)",createcitydefault$(1),"Ok")
createcitydefault$(1) = a$
b = a$
If b<1 Or b>4 Then b=1
cities(newcity,3) = b
a$ = ginputbox$(200,200,400,200,"Input unit alegience (1,2,3,4,5,6,7,8)",createcitydefault$(2),"Ok")
createcitydefault$(2) = a$
b = a$
If b<1 Or b>8 Then b = 1
cities(newcity,4) = b
a$ = ginputbox$(200,200,400,200,"City tax income (0 to xx)",createcitydefault$(3),"Ok")
createcitydefault$(3) = a$
b = a$
If b<1 Or b>999 Then b=1
cities(newcity,5) = b
End Function

Function isnotoverbound(n,higher)
If n<0 Then Return False
If n>higher Then Return False
End Function
;
; This function does the coastline
;
Function docoastline(x,y)
Local cl[9]


counter = 0
For y1 = y-1 To y+1
For x1 = x-1 To x+1
cl[counter] = 0
If RectsOverlap(x1,y1,1,1,0,0,mapwidth+1,mapheight+1) = True Then
	If (Not map(x1,y1) = 64) Then cl[counter] = 1 Else cl[counter] = 0
Else
;If Not map(x1,y1) = 64 Then cl[counter] = 0
;If map(x1,y1) = 64 Then cl[counter] = 1
End If
counter = counter + 1
Next
Next

;debugstring$ = ""
;For i=0 To 9
;If cl[i] = 1 Then debugstring = debugstring + "1" Else debugstring=debugstring+"0"
;Next

;First pass
If cl[3] = 1 And cl[0] = 1 And cl[1] = 1 Then
	If RectsOverlap(x*2,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap(x*2,y*2) = 14  
End If
If cl[3] = 1 And cl[6] = 1 And cl[7] = 1 Then
	If RectsOverlap((x*2)+1,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)) = 46
End If
If cl[1] = 1 And cl[2] = 1 And cl[5] = 1 Then
	If RectsOverlap(x*2,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2),(y*2)+1) = 47
End If
If cl[7] = 1 And cl[8] = 1 And cl[5] = 1 Then
	If RectsOverlap((x*2)+1,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)+1) = 30
End If

;Second pass
If cl[3] = 0 And cl[0] = 1 And cl[1] = 1 Then
	If RectsOverlap(x*2,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap(x*2,y*2) = 14 -2
End If
If cl[3] = 1 And cl[6] = 1 And cl[7] = 0 Then
	If RectsOverlap((x*2)+1,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)) = 46 -2
End If
If cl[1] = 0 And cl[2] = 1 And cl[5] = 1 Then
	If RectsOverlap(x*2,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2),(y*2)+1) = 47-2
End If
If cl[7] = 1 And cl[8] = 1 And cl[5] = 0 Then
	If RectsOverlap((x*2)+1,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)+1) = 30-2
End If

;Third pass
If cl[3] = 1 And cl[0] = 0 And cl[1] = 1 Then
	If RectsOverlap(x*2,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap(x*2,y*2) = 14-4
End If
If cl[3] = 1 And cl[6] = 0 And cl[7] = 1 Then
	If RectsOverlap((x*2)+1,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)) = 46-4
End If
If cl[1] = 1 And cl[2] = 0 And cl[5] = 1 Then
	If RectsOverlap(x*2,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2),(y*2)+1) = 47-4
End If
If cl[7] = 1 And cl[8] = 0 And cl[5] = 1 Then
	If RectsOverlap((x*2)+1,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)+1) = 30-4
End If

;Fourth pass
If cl[3] = 0 And cl[0] = 0 And cl[1] = 1 Then
	If RectsOverlap(x*2,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap(x*2,y*2) = 14-6;12
End If
If cl[3] = 1 And cl[6] = 0 And cl[7] = 0 Then
	If RectsOverlap((x*2)+1,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)) = 46-6
End If
If cl[1] = 0 And cl[2] = 0 And cl[5] = 1 Then
	If RectsOverlap(x*2,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2),(y*2)+1) = 47-6
End If
If cl[7] = 1 And cl[8] = 0 And cl[5] = 0 Then
	If RectsOverlap((x*2)+1,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)+1) = 30-6
End If

;fifth pass
If cl[3] = 1 And cl[0] = 1 And cl[1] = 0 Then
	If RectsOverlap(x*2,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap(x*2,y*2) = 14-8
End If
If cl[3] = 0 And cl[6] = 1 And cl[7] = 1 Then
	If RectsOverlap((x*2)+1,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)) = 46-8
End If
If cl[1] = 1 And cl[2] = 1 And cl[5] = 0 Then
	If RectsOverlap(x*2,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2),(y*2)+1) = 47-8
End If
If cl[7] = 0 And cl[8] = 1 And cl[5] = 1 Then
	If RectsOverlap((x*2)+1,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)+1) = 30-8
End If

;sixth pass
If cl[3] = 0 And cl[0] = 1 And cl[1] = 0 Then
	If RectsOverlap(x*2,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap(x*2,y*2) = 14-10
End If
If cl[3] = 0 And cl[6] = 1 And cl[7] = 0 Then
	If RectsOverlap((x*2)+1,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)) = 46-10
End If
If cl[1] = 0 And cl[2] = 1 And cl[5] = 0 Then
	If RectsOverlap(x*2,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2),(y*2)+1) = 47-10
End If
If cl[7] = 0 And cl[8] = 1 And cl[5] = 0 Then
	If RectsOverlap((x*2)+1,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)+1) = 30-10
End If

;seventh pass
If cl[3] = 1 And cl[0] = 0 And cl[1] = 0 Then
	If RectsOverlap(x*2,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap(x*2,y*2) = 14-12
End If
If cl[3] = 0 And cl[6] = 0 And cl[7] = 1 Then
	If RectsOverlap((x*2)+1,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)) = 46-12
End If
If cl[1] = 1 And cl[2] = 0 And cl[5] = 0 Then
	If RectsOverlap(x*2,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2),(y*2)+1) = 47-12
End If
If cl[7] = 0 And cl[8] = 0 And cl[5] = 1 Then
	If RectsOverlap((x*2)+1,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)+1) = 30-12
End If

;Eight pass
If cl[3] = 0 And cl[0] = 0 And cl[1] = 0 Then
	If RectsOverlap(x*2,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap(x*2,y*2) = 14-14
End If
If cl[3] = 0 And cl[6] = 0 And cl[7] = 0 Then
	If RectsOverlap((x*2)+1,y*2,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)) = 46-14
End If
If cl[1] = 0 And cl[2] = 0 And cl[5] = 0 Then
	If RectsOverlap(x*2,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2),(y*2)+1) = 47-14
End If
If cl[7] = 0 And cl[8] = 0 And cl[5] = 0 Then
	If RectsOverlap((x*2)+1,(y*2)+1,1,1,0,0,(mapwidth*2) + 2,(mapheight*2)+2) = True Then coastlinemap((x*2)+1,(y*2)+1) = 30-14
End If

;simplemessage("x " + x + "  y : " + y) 

;coastlinemap(x*2,y*2) = 14
;coastlinemap((x*2)+1,(y*2)) = 46
;coastlinemap((x*2),(y*2)+1) = 47
;coastlinemap((x*2)+1,(y*2)+1) = 30

End Function

Function returncityname$()
newname = False
counter = 0
Repeat
	a$ = citiesnames(Rand(0,numcitiesnames))
	newname = True
	For i=0 To numcities
		If cities(i,0) = True Then
			If citiesstring$(i,0) = a$ Then newname = False
		End If
	Next
	If counter = 500 Then newname = True
Until newname = True

If counter = 10000 Then Return "New City"

Return a$

End Function

