;
; These functions are for the cities.
;
;
;
;
Global marketwindowscale = 100
Global upgradewindowscale = 100

Global portcity = False


Global buildunittype

Global buildunitscounter = 0

Dim cityunitactivatemap(256,1) ; active|unit


; Set the queue list to -1
For i=0 To maxcities : :For ii=0 To 4 : citiesunitsproduction(i,ii) = -1 : Next : Next


; This array hold the units in the currently selected city
Dim unitsincity(256)

;
; This function handles the city
;
;
Function cityscreen(city)
; load the units inside the city in unitsincity(x)
; Get the amount of numbers inside uboundunits
playsfx(3)

Delay(200)
FlushMouse()
FlushKeys()

x = cities(city,1)
y = cities(city,2)

portcity = False
For x1=-1 To 1
For y1=-1 To 1
If RectsOverlap(x+x1,y+y1,1,1,0,0,mapwidth,mapheight) = True Then
If map(x+x1,y+y1) = 64 Then portcity = True Else buildunitscounter = 0
End If
Next
Next

uboundunits = getunitsincity(city) 
unitscounter = 0
exitloop = False
;
;buildunitscounter = 0
; Clear the selection

For i=0 To 256
cityunitactivatemap(i,0) = False
Next

While KeyDown(1) = False And exitloop = False
Cls
doflickertimer ; Do the flicker	
;drawmap ; first draw the map
DrawImage mapbuffer,0,0
drawgamescreen ; draw the righter game screen
;



drawcityscreen(city,unitscounter,uboundunits,buildunitscounter)


If MouseDown(1) = True Then

; Zones For selecting units in city
i = domousezone((screenwidth/2-300)+10+6,(screenheight/2-150),250,350,50,50,i,0) 

;
; 'i' contains the zone pressed on
If i=>0 Then
		;
		;
		; Tutorial
		;
		tm = tutorial_citybaracks
		If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
		;

i = unitsincity(i)
;If units(i,10) > 0 Then
If unitsmove(i)>0 And units(i,0) = True Then 
	units(i,unit_incity) = False
	units(i,unit_ontop) = True
	units(i,unit_fortified) = False ; disable fortify
	units(i,13) = 0 ; just to be sure set the ai method to zero
	currentunit(0) = units(i,1)
	currentunit(1) = units(i,2)
	currentunit(2) = i
	placeunitontop(i)
	maketempmap : drawminimap : drawmap
	exitloop = True
End If
End If
;
; Zones for moving up and down thru the buy_units list
i = domousezone((screenwidth/2)+276,(screenheight/2)-150,20,180,20,20,0,0)
If i=>0 Then
If i = 0 Then 
	buildunitscounter = buildunitscounter - 1
	If buildunitscounter < 0 Then buildunitscounter=0
	wacht(100)
End If
If i = 8 Then 
	buildunitscounter = buildunitscounter + 1
	If portcity = True Then
	If buildunitscounter > 10 Then buildunitscounter = 10
	Else
	If buildunitscounter > 7 Then buildunitscounter = 7
	End If
	
	wacht(100)
End If
End If
;
; Zoes for selecting a unit to be build
i = domousezone((screenwidth/2)+5,(screenheight/2)-150,45,180,45,48,0,0)
If i=>0 Then
		;
		;
		; Tutorial
		;
		tm = tutorial_cityqueue
		If tutorialactive = True And visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
		;
unittoqueue = (i+buildunitscounter)+1
If unittoqueue = 12 Then unittoqueue = 15 ; small boat
If unittoqueue = 13 Then unittoqueue = 16 ; medium boat
If unittoqueue = 14 Then unittoqueue = 17 ; large boat
For zi=0 To 4
If citiesunitsproduction(city,zi) = -1 Then 
citiesunitsproduction(city,zi) = unittoqueue
playsfx(5)
Delay(100)
Exit
End If
Next
;unitprice = unitdefault(unittobuy,4)
;If Not unitprice > playergold(cities(city,4)) Then
;	playergold(cities(city,4)) = playergold(cities(city,4)) - unitprice
;	newunit = newunitincity(city,unittobuy)
;	unitsincity(uboundunits) = newunit
;	uboundunits = uboundunits + 1
;	wacht(100)
;	currentunit(0) = cities(city,1)
;	currentunit(1) = cities(city,2)
;	currentunit(2) = newunit
;	Else
;	simplemessage("Not enough gold to buy this unit")
;End If
End If

End If
;
; City improvements button
If gbutton2(screenwidth/2,screenheight/2+115,150,20,"Clear Queue",1,1,currentbutton) Then currentbutton = 1
If gbutton2(screenwidth/2+150,screenheight/2+115,150,20,"Upgrade Now",1,3,currentbutton) Then currentbutton = 3
; City fortify all units button
If gbutton2((screenwidth/2-298)+10,screenheight/2-160+330,200,20,"Fortify active units",1,4,currentbutton) Then currentbutton = 4

If gbutton2(screenwidth/2,screenheight/2+135,150,20,"Market",1,5,currentbutton) Then currentbutton = 5
If gbutton2(screenwidth/2+150,screenheight/2+135,150,20,"City Status",1,6,currentbutton) Then currentbutton = 6


; Exit window button
If gbutton2(screenwidth/2+210,(screenheight/2-198)+4,80,20,"Exit",1,2,currentbutton) Then currentbutton = 2


If RectsOverlap(MouseX(),MouseY(),1,1,screenwidth/2+150,screenheight/2+115,150,20) = True Then
;showtooltip(11)
enabletooltip(11)
End If


If MouseDown(1) = True And currentbutton>0 Then

If gcheckarea(screenwidth/2+210,(screenheight/2-198)+4,80,20) = True And currentbutton = 2 Then
exitloop = True
End If

; Clear build queue
If gcheckarea(screenwidth/2,screenheight/2+115,150,20) = True And currentbutton = 1 Then
For zi=0 To 4
citiesunitsproduction(city,zi) = -1
Next
End If

; Upgrade city, increase tax
If gcheckarea(screenwidth/2+150,screenheight/2+115,150,20) = True And currentbutton = 3 Then
If playergold(currentplayer)>500 And cities(city,5) < 150 Then
cities(city,5) = cities(city,5) + 15
If cities(city,5) > 150 Then cities(city,5) = 150
playergold(currentplayer) = playergold(currentplayer) - 500
playsfx(7)
;
If cities(city,5) > 30 Then cities(city,3) = 2
If cities(city,5) > 50 Then cities(city,3) = 3
If cities(city,5) > 70 Then cities(city,3) = 4
;
Delay(500)
Else
If cities(city,5)=>150 Then
simplemessage("City is at its maximum capacity")
Else
simplemessage("Not enough money to pay for a upgrade")
End If
End If
End If

If gcheckarea((screenwidth/2-298)+10,screenheight/2-160+330,200,20) = True And currentbutton = 4 Then
fortifyallunitsincity(city,currentplayer)
End If

; Show Market
;
If gcheckarea(screenwidth/2,screenheight/2+135,150,20) = True And currentbutton = 5 Then
tmarketwindow(-1,-1,city,marketwindowscale)
End If

; Show City Status
;

If gcheckarea(screenwidth/2+150,screenheight/2+135,150,20) = True And currentbutton = 6 Then
tupgradewindow(-1,-1,city,upgradewindowscale)
End If



currentbutton = 0
End If

;
DrawImage mousepointer,MouseX(),MouseY() ; Draw the mousepointer
Flip
If screenshotsenabled=True And KeyHit(2) = True Then SaveBuffer(FrontBuffer(),"c:\screenshot.bmp"):End
Wend
;
;
counter = 0
unitisactivated = False
For i=0 To 256
If cityunitactivatemap(i,0) = True And unitsmove#(i) > 0 Then
units(cityunitactivatemap(i,1),unit_incity) = False
units(cityunitactivatemap(i,1),13) = 0
units(cityunitactivatemap(i,1),unit_fortified) = False
units(cityunitactivatemap(i,1),unit_invisible) = False
unitisactivated = True
End If
Next
;
If unitisactivated = True Then findnextmovableunit(currentplayer)
;
; Little delay getting out of the city screen
wacht(200)
FlushKeys : FlushMouse

End Function

;
; This function draws the city screen
;
;
Function drawcityscreen(city,unitscounter,uboundunits,buildunitscounter)

; First draw the background
;Color 0,0,0
;Rect screenwidth/2-300,screenheight/2-200,600,400,1
;Color 255,255,255
;Rect screenwidth/2-300,screenheight/2-200,600,400,0
gwindow(screenwidth/2-300,screenheight/2-200,600,400,0,0,0)
gwindow(screenwidth/2-298,screenheight/2-198,596,32,0,1,3)
gwindow((screenwidth/2-298)+10,screenheight/2-160,260,330,0,1,1)
gwindow(screenwidth/2-10,screenheight/2-160,280,220,0,1,1)
;
t$ = "City of " + citiesstring$(city,0)
Text2 (screenwidth/2-300)+5,screenheight/2-197,t$
t$ = "Tax income : " + cities(city,5)
Text2 (screenwidth/2-300)+200,screenheight/2-197,t$
;
; Draw the units in city
counter = 0
x1 = 0
y1 = 0
For y=0 To 250 Step 50
For x=5 To 250 Step 50
If counter<uboundunits
If MouseDown(2)=True Then
If RectsOverlap((screenwidth/2-300)+x+6,(screenheight/2-150)+y,64,48,MouseX(),MouseY(),1,1) = True Then
cityunitactivatemap(counter,0) = True
End If
End If
DrawImage unitgraphics(units(unitsincity(counter),4)-1),(screenwidth/2-300)+x+6,(screenheight/2-150)+y
If cityunitactivatemap(counter,0) = True Then
Color 255,255,255
Rect ((screenwidth/2-300)+x+6)+8,((screenheight/2-150)+y)+6,64-16,48-12,0
End If
End If
;DrawImage unitgraphics(0),(screenwidth/2-300)+x,(screenheight/2-150)+y
counter = counter + 1
x1 = x1 + 1 : If x1 = 5 Then x1=0 : y1=y1+1
Next
Next
;
; Buy units section
;
; Button up
DrawImage buttons(1),(screenwidth/2)+276,(screenheight/2)-150
DrawImage buttons(3),(screenwidth/2)+276,(screenheight/2)+10
; Draw the buy inits
For y=0 To 3
buildnum = y+buildunitscounter
If buildnum = 13 Then buildnum = 16 ; large boat
If buildnum = 12 Then buildnum = 15 ; medium boat
If buildnum = 11 Then buildnum = 14 ; small boat
DrawImage unitgraphics(buildnum),(screenwidth/2),((screenheight/2)-150)+y*48
nm$ = unitsname$((buildnum)+1)
mv$ = unitdefault((buildnum)+1,2)
att$ = unitdefault((buildnum)+1,0)
df$ = unitdefault((buildnum)+1,1)
t$ = nm$ + " ("+mv$+"/"+att$+"/"+df$+")"
Text2 (screenwidth/2)+50,((screenheight/2)-150)+y*48,t$
t$ = "Costs : " + unitdefault((buildnum)+1,4)
Text2 (screenwidth/2)+50,(((screenheight/2)-150)+y*48)+15,t$
Next
;
;citiesunitsproduction

gwindow(screenwidth/2-10,(screenheight/2-150)+208,280,48+6,0,1,1)

counter = 4
For x1 = 4 To 0 Step -1
If citiesunitsproduction(city,counter) > -1 Then
DrawImage unitgraphics(citiesunitsproduction(city,counter)-1),(screenwidth/2)+(x1*48),((screenheight/2)-150)+210
End If
counter = counter - 1
Next


; 
End Function
;
;
;

;
; This function is for loading the units that are inside the city
;
;
Function getunitsincity(city)
;First clean out the unitincity array
For i=0 To 256
unitsincity(i) = False
Next

; Load the units inside the array
counter = 0 
x=0
y=0
For i=0 To maxunits
If units(i,0) = True Then ; is the unit active
;If units(i,14) = True Then ; is the unit inside a city
If units(i,1) = cities(city,1) And units(i,2) = cities(city,2) Then ; is the unit inside our current city
unitsincity(counter)=i
cityunitactivatemap(counter,1) = i
counter = counter + 1
x=x+1
y=y+1
If x=5 Then x=0 : y=y+1

End If
;End If
End If
Next
Return counter
End Function

;
; This function insert a new unit into the city
;
;
Function newunitincity(city,newunittype)


counter = -1
For i=1 To maxunits
If units(i,0) = False Then counter = i : i=maxunits
Next
units(counter,0) = True ; active
units(counter,1) = cities(city,1) ; x loc
units(counter,2) = cities(city,2) ; y loc
units(counter,3) = True  ; Ontop status
units(counter,4) = newunittype  ; Unit type 
units(counter,5) = unitdefault(newunittype,0)     ; Attack
units(counter,6) = unitdefault(newunittype,1)     ; Defense
units(counter,7) = 0     ; Damage (0 - no, 9 - max)
units(counter,8) = False ; Fortified
units(counter,9) = False ; Veteran status 
unitsmove#(counter) = unitdefault(newunittype,2)
units(counter,10) = unitdefault(newunittype,2)    ; Moves left
units(counter,11)= cities(city,4) ; set in the player number
units(counter,12) = False; true if unit is on hold
units(counter,13) = 0    ; AI method/unit method
units(counter,14) = False ; Unit is inside a city
units(counter,15) = 0    ; turns inactive
units(counter,unit_homecity) = city
units(counter,unit_invisible) = False
units(counter,unit_cancarry) = unitdefault(newunittype,5)
units(counter,unit_carried) = 0


placeunitontop(counter) ; place the last created unit ontop
;playsfx(5)
Return counter
End Function


;
; This function fortifies all new units
;
Function fortifyallunitsincity(city,player)
For i=0 To maxunits
If units(i,0) = True Then
If units(i,11) = player Then
If units(i,14) = False Then
If units(i,1) = cities(city,1) And units(i,2) = cities(city,2) Then
unitsmove(i) = 0
units(i,8) = True
units(i,14) = True
End If
End If
End If
End If
Next
findnextmovableunit(currentplayer)
End Function





;
; Market window
;
; City is the city to be viewed
; setzoom - 0 = actual size (+-)
; x = -1 = center on screen (800-w/2)
; y = -1 = center on screen (600-h/2)
;
;
Function tMarketwindow(x,y,city,setzoom)
Delay(200):FlushKeys() : FlushMouse()
; Set the zoom of the gui
guiscale = setzoom

w = 400 ; default width
h = 300 ; default height


; Handle the autocentering if required
If x=-1 Then
x = 800/2-enl(w)/2
End If

If y=-1 Then
y = 600/2-enl(h)/2
End If




; Grab the foreground
SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()

FlushKeys:FlushMouse
gexitwindow = False
While KeyHit(1) = False And gexitwindow = False
Cls
DrawBlock gbackgroundimage,0,0
; Draw the window
gwindow(x,y,enl(w),enl(h),1,0,1)

; left top backdrop
gwindow(x+enl(3),y+enl(38),enl(100),enl(h-210-8),-1,-1,-1)
; Right top backdrop
gwindow(x+enl(106),y+enl(38),enl(w-110),enl(h-210-8),-1,-1,-1)
; Bottom backdrop
gwindow(x+enl(3),y+enl(132),enl(w-6),enl(h-16-132),-1,-1,-1)

; Buy Sell backdrop
;gwindow(x+enl(115),y+enl(140),enl(161),enl(136),-1,-1,-1)


SetFont gminifont
Color 255,255,255

If gtextbar(x,y,enl(w),enl(12),"City Of Danzig Market Window",0) = False Then gbuttonpressed = 99
If gtextbar(x,y+enl(h-14),enl(w),enl(12),"",0) = False Then gbuttonpressed = 92
If gbutton4(x+enl(w-12),y+enl(2),small_close_button,98,gbuttonpressed) = True Then gbuttonpressed = 98
If gtextbar(x+enl(6),y+enl(h-14),enl(40),enl(12),"View",6) = False Then gbuttonpressed = 97
If gbutton5(x+enl(40+6),y+enl(h-14),enl(40),enl(12),"Small",96,gbuttonpressed) = True Then gbuttonpressed = 96
If gbutton5(x+enl(40+40+6),y+enl(h-14),enl(40),enl(12),"Medium",95,gbuttonpressed) = True Then gbuttonpressed = 95
If gbutton5(x+enl(40+40+40+6),y+enl(h-14),enl(40),enl(12),"Large",94,gbuttonpressed) = True Then gbuttonpressed = 94
If gbutton5(x+enl(166),y+enl(h-14),enl(64),enl(12),"Exit",93,gbuttonpressed) = True Then gbuttonpressed = 93

;
;
; Countries most urgent need message
;
If gtextbar(x+enl(20),y+enl(45),enl(70),enl(56),"Message",4) = False Then gbuttonpressed = 1

; Requirements and caravans
;
;
If gtextbar(x+enl(111),y+enl(40),enl(270),enl(17),"Country Needs",7) = False Then gbuttonpressed = 2
; #1
If gtextbar( x+enl(125),y+enl(60),enl(154),enl(12),"Requirements",3) = False Then gbuttonpressed = 3
If gbutton5(x+enl(279),y+enl(60),enl(53),enl(12),"Caravan",3,gbuttonpressed) = True Then gbuttonpressed = 3
If gbutton5(x+enl(332),y+enl(60),enl(35),enl(12),"Ignore",4,gbuttonpressed) = True Then gbuttonpressed = 4
; #2
If gtextbar( x+enl(125),y+enl(60+14),enl(154),enl(12),"Requirements",5) = False Then gbuttonpressed = 6
If gbutton5(x+enl(279),y+enl(60+14),enl(53),enl(12),"Caravan",5,gbuttonpressed) = True Then gbuttonpressed = 5
If gbutton5(x+enl(332),y+enl(60+14),enl(35),enl(12),"Ignore",6,gbuttonpressed) = True Then gbuttonpressed = 6
; #3
If gtextbar( x+enl(125),y+enl(60+28),enl(154),enl(12),"Requirements",4) = False Then gbuttonpressed = 3
If gbutton5(x+enl(279),y+enl(60+28),enl(53),enl(12),"Caravan",7,gbuttonpressed) = True Then gbuttonpressed = 7
If gbutton5(x+enl(332),y+enl(60+28),enl(35),enl(12),"Ignore",8,gbuttonpressed) = True Then gbuttonpressed = 8
; #4
If gtextbar( x+enl(125),y+enl(60+42),enl(154),enl(12),"Requirements",5) = False Then gbuttonpressed = 6
If gbutton5(x+enl(279),y+enl(60+42),enl(53),enl(12),"Caravan",9,gbuttonpressed) = True Then gbuttonpressed = 9
If gbutton5(x+enl(332),y+enl(60+42),enl(35),enl(12),"Ignore",10,gbuttonpressed) = True Then gbuttonpressed = 10


; City market
;If gtextbar(x+enl(8),  y+enl(122),enl(271),enl(16),"City Market",7) = False Then gbuttonpressed = 9
;If gtextbar(x+enl(280),y+enl(122),enl(105),enl(16),"City Stores",7) = False Then gbuttonpressed = 10
If gtextbar(x+enl(8),  y+enl(143),enl(106),enl(18),"Market Goods",7) = False Then gbuttonpressed = 11
;
; Market Goods display stores
If gbutton5(x+enl(11), y+enl(165+0*22),enl(98) ,enl(20),"Food",12,gbuttonpressed) = True Then gbuttonpressed = 12
If gbutton5(x+enl(11), y+enl(165+1*22),enl(98) ,enl(20),"Misc goods",13,gbuttonpressed) = True Then gbuttonpressed = 13
If gbutton5(x+enl(11), y+enl(165+2*22),enl(98) ,enl(20),"Constr. mat.",14,gbuttonpressed) = True Then gbuttonpressed = 14
If gbutton5(x+enl(11), y+enl(165+3*22),enl(98) ,enl(20),"Clothing",15,gbuttonpressed) = True Then gbuttonpressed = 15
If gbutton5(x+enl(11), y+enl(165+4*22),enl(98) ,enl(20),"Weapons",16,gbuttonpressed) = True Then gbuttonpressed = 16
;
; Buy Sell Section
;
;If gbutton5(x+115,y+140,161,136,"",17,gbuttonpressed) = True Then gbuttonpressed = 17
If gtextbar(x+enl(122),y+enl(143),enl(150),enl(17),"Treasury 1000 gold",0) = False Then gbuttonpressed = 18
If gtextbar(x+enl(171),y+enl(164),enl(54) ,enl(17),"Cloth",0) = False Then gbuttonpressed = 19
If gtextbar(x+enl(140),y+enl(180),enl(31) ,enl(17),"Sup",0) = False Then gbuttonpressed = 20
If gtextbar(x+enl(176),y+enl(180),enl(43) ,enl(17),"Cost",0) = False Then gbuttonpressed = 21
If gtextbar(x+enl(225),y+enl(180),enl(30) ,enl(17),"Sup",0) = False Then gbuttonpressed = 22
If gtextbar(x+enl(139),y+enl(197),enl(38) ,enl(17),"100",0) = False Then gbuttonpressed = 23
If gtextbar(x+enl(178),y+enl(197),enl(39) ,enl(17),"17",0) = False Then gbuttonpressed = 24
If gtextbar(x+enl(218),y+enl(197),enl(39) ,enl(17),"20",0) = False Then gbuttonpressed = 25
If gbutton5(x+enl(138),y+enl(215),enl(33) ,enl(17) ,"Sell",26,gbuttonpressed) = True Then gbuttonpressed = 26
If gbutton5(x+enl(171),y+enl(215),enl(12) ,enl(17) ,"<<",27,gbuttonpressed) = True Then gbuttonpressed = 27
If gbutton5(x+enl(184),y+enl(215),enl(28) ,enl(17) ,"1",28,gbuttonpressed) = True Then gbuttonpressed = 28
If gbutton5(x+enl(213),y+enl(215),enl(11) ,enl(17) ,">>",29,gbuttonpressed) = True Then gbuttonpressed = 29
If gbutton5(x+enl(224),y+enl(215),enl(32) ,enl(17) ,"Buy",30,gbuttonpressed) = True Then gbuttonpressed = 30
If gbutton5(x+enl(138),y+enl(234),enl(34) ,enl(17) ,"S.All",31,gbuttonpressed) = True Then gbuttonpressed = 31
If gbutton5(x+enl(224),y+enl(234),enl(34) ,enl(17) ,"B.All",32,gbuttonpressed) = True Then gbuttonpressed = 32
If gtextbar(x+enl(178),y+enl(252),enl(39) ,enl(17) ,"15",0) = False Then gbuttonpressed = 33
;
; City display stores
;
If gtextbar(x+enl(280),y+enl(143),enl(109),enl(18),"City Supply",7) = False Then gbuttonpressed = 34
;
If gbutton5(x+enl(284),y+enl(165+0*22),enl(98) ,enl(20),"Food",35,gbuttonpressed) = True Then gbuttonpressed = 35
If gbutton5(x+enl(284),y+enl(165+1*22),enl(98) ,enl(20),"Misc. goods",36,gbuttonpressed) = True Then gbuttonpressed = 36
If gbutton5(x+enl(284),y+enl(165+2*22),enl(98) ,enl(20),"Constr. Mat.",37,gbuttonpressed) = True Then gbuttonpressed = 37
If gbutton5(x+enl(284),y+enl(165+3*22),enl(98) ,enl(20),"Clothing",38,gbuttonpressed) = True Then gbuttonpressed = 38
If gbutton5(x+enl(284),y+enl(165+4*22),enl(98) ,enl(20),"Weapons",39,gbuttonpressed) = True Then gbuttonpressed = 39



; Execute the buttons
If MouseDown(1) = False And gbuttonpressed > 0 Then
;
;If gtextbar(enl(x),enl(y),enl(w),enl(12),"City Of Danzig Market Window",0) = False Then gbuttonpressed = 99
;If gbutton4(enl(x+w-12),enl(y+2),small_close_button,98,gbuttonpressed) = True Then gbuttonpressed = 98
;If gtextbar(enl(x+6),enl(y+h-18),enl(40),enl(17),"View",6) = False Then gbuttonpressed = 97
;If gbutton5(enl(x+40+6),enl(y+h-18),enl(40),enl(17),"Small",96,gbuttonpressed) = True Then gbuttonpressed = 96
;If gbutton5(enl(x+40+40+6),enl(y+h-18),enl(40),enl(17),"Medium",95,gbuttonpressed) = True Then gbuttonpressed = 95
;If gbutton5(enl(x+40+40+40+6),enl(y+h-18),enl(40),enl(17),"Large",94,gbuttonpressed) = True Then gbuttonpressed = 94
;If gbutton5(enl(x+166),enl(y+h-18),enl(64),enl(17),"Exit",93,gbuttonpressed) = True Then gbuttonpressed = 93
Select gbuttonpressed
Case 99
; Exit window
Case 98 : If gcheckarea(x+enl(w-12),y+enl(2),16,16) = True Then gexitwindow = True
Case 97 :
; Mini View
Case 96
	If gcheckarea(x+enl(40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		marketwindowscale = 0
		guiscale = 0
		x = 800/2-enl(400/2)
		y = 600/2-enl(300/2)
	End If
; Medium view
Case 95
	If gcheckarea(x+enl(40+40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		marketwindowscale = 40
		guiscale = 40
		x = 800/2-400/100*140/2
		y = 600/2-300/100*140/2
	End If
; Large view
Case 94
	If gcheckarea(x+enl(40+40+40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		marketwindowscale = 100
		guiscale = 100
		x = 800/2-enl(400)/2
		y = 600/2-enl(300)/2
	End If
; Ok and exit
Case 93 :
	If gcheckarea(x+enl(166),y+enl(h-14),enl(64),enl(12)) = True Then
		gexitwindow = True
	End If
End Select
;
gbuttonpressed = 0
End If

; Draw the mouse (not implented yet)
DrawImage mousepointer,MouseX(),MouseY()
Flip
Wend

FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
End Function


;
; Upgrade window
;
;
Function tUpgradewindow(x,y,city,setzoom)
Delay(200):FlushKeys() : FlushMouse()

; Set the scale of the gui
guiscale = setzoom

w = 400 ; default width
h = 300 ; default height

; Handle the autocentering if required
If x=-1 Then
x = 800/2-enl(w)/2
End If

If y=-1 Then
y = 600/2-enl(h)/2
End If



; Grab the foreground
SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()

FlushKeys:FlushMouse
gexitwindow = False
While KeyHit(1) = False And gexitwindow = False
Cls
DrawBlock gbackgroundimage,0,0
; Draw the window
gwindow(x,y,enl(w),enl(h),1,0,1)
gwindow(x+enl(26),y+enl(60),enl(w-52),enl(h-90),-1,-2,-1)

SetFont grequesterfont

; Draw the requester title
Color 0,0,0
Text x+5,y,gtitle$
; Draw the request text 
Text x+w/2,y+40,grequest1$,1,1
Text x+w/2,y+60,grequest2$,1,1
Text x+w/2,y+80,grequest3$,1,1
;
;Function gbutton1(x,y,w,h,t$,b1,b2)
SetFont gminifont
Color 255,255,255



If gtextbar(x,y,enl(w),enl(12),"Status Window",0) = False Then gbuttonpressed = 99
If gbutton4(x+enl(w-12),y+enl(2),small_close_button,98,gbuttonpressed) = True Then gbuttonpressed = 98

If gtextbar(x+enl(6),y+enl(h-14),enl(40),enl(12),"View",6) = False Then gbuttonpressed = 97
If gbutton5(x+enl(40+6),y+enl(h-14),enl(40),enl(12),"Small",96,gbuttonpressed) = True Then gbuttonpressed = 96
If gbutton5(x+enl(40+40+6),y+enl(h-14),enl(40),enl(12),"Medium",95,gbuttonpressed) = True Then gbuttonpressed = 95
If gbutton5(x+enl(40+40+40+6),y+enl(h-14),enl(40),enl(12),"Large",94,gbuttonpressed) = True Then gbuttonpressed = 94
If gbutton5(x+enl(166),y+enl(h-14),enl(64),enl(12),"Exit",93,gbuttonpressed) = True Then gbuttonpressed = 93

y = y - enl(20)
;
;
If gtextbar(x+enl(115),y+enl(44) ,enl(170),enl(32) ,"City of Danzig",7) = False Then gbuttonpressed = 1
;
;
;
If gtextbar(x+enl(61) ,y+enl(115),enl(69) ,enl(20) ,"Level",7) = False Then gbuttonpressed = 2
;
;
If gbutton5(x+enl(84) ,y+enl(137),enl(27) ,enl(18) ,"1",3,gbuttonpressed) = True Then gbuttonpressed = 3
;
;
If gtextbar(x+enl(53) ,y+enl(166),enl(87) ,enl(18) ,"City Income",7) = False Then gbuttonpressed = 4
;
;
If gbutton5(x+enl(81) ,y+enl(187),enl(31) ,enl(18) ,"100",5,gbuttonpressed) = True Then gbuttonpressed = 5
;
;
If gtextbar(x+enl(53) ,y+enl(214),enl(87) ,enl(18) ,"Citizens are :",7) = False Then gbuttonpressed = 6
;
;
If gbutton5(x+enl(71) ,y+enl(235),enl(53) ,enl(18) ,"Content",7,gbuttonpressed) = True Then gbuttonpressed = 7
If gbutton5(x+enl(225),y+enl(114),enl(86) ,enl(18) ,"Expand City",8,gbuttonpressed) = True Then gbuttonpressed = 8
;
;
If gtextbar(x+enl(205),y+enl(154),enl(125),enl(21) ,"City Expansion Requires",4) = False Then gbuttonpressed = 9
;
;
If gbutton5(x+enl(205),y+enl(178),enl(35) ,enl(22) ,"50",10,gbuttonpressed) = True Then gbuttonpressed = 10
If gtextbar(x+enl(245),y+enl(178),enl(84) ,enl(20) ,"gold",4) = False Then gbuttonpressed = 11
If gbutton5(x+enl(205),y+enl(178+1*24),enl(35) ,enl(22) ,"50",12,gbuttonpressed) = True Then gbuttonpressed = 12
If gtextbar(x+enl(245),y+enl(178+1*24),enl(84) ,enl(20) ,"gold",4) = False Then gbuttonpressed = 13
If gbutton5(x+enl(205),y+enl(178+2*24),enl(35) ,enl(22) ,"50",14,gbuttonpressed) = True Then gbuttonpressed = 14
If gtextbar(x+enl(245),y+enl(178+2*24),enl(84) ,enl(20) ,"gold",4) = False Then gbuttonpressed = 15
If gbutton5(x+enl(205),y+enl(178+3*24),enl(35) ,enl(22) ,"50",16,gbuttonpressed) = True Then gbuttonpressed = 16
If gtextbar(x+enl(245),y+enl(178+3*24),enl(84) ,enl(20) ,"gold",4) = False Then gbuttonpressed = 17

y=y+enl(20)

; Execute the buttons
If MouseDown(1) = False And gbuttonpressed > 0 Then

Select gbuttonpressed
Case 99
; Exit window
Case 98 : If gcheckarea(x+enl(w-12),y+enl(2),16,16) = True Then gexitwindow = True
Case 97 :
; Mini View
Case 96
	If gcheckarea(x+enl(40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		upgradewindowscale = 0
		guiscale = 0
		x = 800/2-enl(w/2)
		y = 600/2-enl(h/2)
	End If
; Medium view
Case 95
	If gcheckarea(x+enl(40+40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		upgradewindowscale = 40
		guiscale = 40
		x = 800/2-400/100*140/2
		y = 600/2-300/100*140/2
	End If
; Large view
Case 94
	If gcheckarea(x+enl(40+40+40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		upgradewindowscale = 100
		guiscale = 100
		x = 800/2-enl(400)/2
		y = 600/2-enl(300)/2
	End If
; Exit the window
Case 93 : ; 
	If gcheckarea(x+enl(166),y+enl(h-14),enl(64),enl(12)) = True Then
		gexitwindow = True
	End If

End Select
;
gbuttonpressed = 0
End If



; Draw the mouse (not implented yet)
DrawImage mousepointer,MouseX(),MouseY()
Flip
Wend

FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
End Function