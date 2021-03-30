;
; turn based movement on a isometric map. 
;
; Ingame keys :
;
; CTRL + G = grid on/off
; HOME/PAGEUP/END/PAGEDOWN/CURSUP/CURSDOWN/CURSLEFT/CURSRIGHT = unit movement
;
;
;
;
;
;
; By R.v.Etten in 2002
;
;
; Still much to do.
;
;Getkey
;
;While KeyDown(1) = False
;Print GetKey()
;Print False
;Delay 100
;Wend
;End
;
; Scancode
;
;While KeyDown(1) = False
;For i=0 To 255
;If KeyDown(i) = True Then Print i
;Next
;Wend
;End

Global release = False


If release = False Then ChangeDir("..")



Global set_intromessage$ = True
Global set_introaboutmessage$ = True
Global set_tutorialrequest$ = True
Global set_mousescrolling$ = True
Global set_mousescrollingbutton$	= 3
Global set_autoscrollingfullscreen$	= True
Global set_autoscrollingwindowed$	= False
Global set_quitrequest	 = False
Global set_showtitlescreen = True
Global set_tooltipdelay = 2500
Global set_musicenabled = True
Global set_sfxenabled = True
Global set_graphicsdepth = 32
loadsettings()


Global donotupdate = False

Dim pathmap(100,100)

SeedRnd MilliSecs()

Global fullscreen = False
Global showtitleimage = False
Global ingamemusic = False
Global TutorialActive = False


If release = True Then
	fullscreen = False	
	ingamemusic = True
	If set_showtitlescreen = True Then showtitleimage = True
	Else
	set_tutorialrequest = False
	tutorialactive = False
End If

If CommandLine$() = "-fullscreen" Then fullscreen = True

.gametiming
Dim mistertimer(5) ; for function mistertime(isoinclude)

Dim tooltipdelay(25) ; activated
Const numtooltips = 25



.gameinput
Dim moused(3) ; countains if the mouse is being held down




Global generalactive = False ; F12 - Copy of ai but for the player himself

Global screenshotsenabled = True

Global showdebuginfo = False
Global debugstring$ ="000"
Global showdebugframes = False

Global fogofwarenabled = False
Global tooltipsenabled = True
Global ingamesfx = True

Global showallbattles = True ; show all battles on the map

Global gotomodeactive = False

Global currentgame$ = "test.sav"

;
;
; For the map generator
;
;
;

Dim genmap(100,100)
Dim backmap(100,100)


Global numcontinents = 0

Dim sparemap(100,100)
Dim islandmap(100,100) ; holds the continents
Dim amap(100,100) ; holds the number of the continents
Dim beachmap(100,100) ; Holds the beach information
Dim continentmass(100) ; Holds the masses for the continents
Dim mg_citymap(100,100)
Dim bewoondterrein(100,100)

Dim mg_roadmap(100,100)

Dim citiesgrid(1000,5) ; player , x, y



;
; Holds the parsed information that is read from config giles / economy/units
;
;
Dim parse$(99)
;
; Holds the status of relations
;
Dim playerrelations(8,8,10) ; see isopreset
Const relationfriendly = 0
Const relationtrust = 1
Const relationfear = 2
;
; Holds if the players can trade.
;
Dim tradestatus(8,8)

;
; Player names
;
Dim Tribesnames$(10)
Dim TribesDescriptions$(10,10)

;
; Cities names
;
Dim citiesnames$(500) ; All the names for the cities / not for the cities themselfs
Global numcitiesnames = 0
;
;
; Tutorial Setup
;
;
;
Const Hint_contact = 1
Const Hint_DisableTutorial = 2
Const Hint_Edit = 3
Const Hint_Overview = 4
Const Tutorial_Disband = 5
Const Tutorial_Wait = 6
Const Tutorial_Pillage = 7
Const Tutorial_Fortify = 8
Const Tutorial_Roads = 9
Const Tutorial_Cities = 10
Const Tutorial_Units = 11
Const Tutorial_MainScreen = 12
Const Tutorial_Minimap = 13
Const Tutorial_fogofwar = 14
Const Tutorial_cityqueue = 15
Const Tutorial_citybaracks = 16


Dim visitedtutorial(20)


; visibility setting (not yet implemented
Dim vismap(100,100,8)

;
; Here we set the player personalities (ai settings)
;
Dim directattacktactic(8)
directattacktactic(1) = 1 ; 50 percent chance of direct assault tactic
directattacktactic(2) = 1
directattacktactic(3) = 2 ; 33 percent chance
directattacktactic(4) = 2
directattacktactic(5) = 3 ; 25 percent chance
directattacktactic(6) = 3
directattacktactic(7) = 4 ; 20 percent chance
directattacktactic(8) = 4


; This is for the player attacking function
;
; The code below explains how the missions are chosen
; 
; 
;aidestinationmode = 5
;a=rand(100)
;If a=>0 And a=<40 Then aidestinationmode = 1 ; retake conquered cities
;If a>40 And a=<60 Then aidestinationmode = 2 ; attack nearby cities
;If a>60 And a=<70 Then aidestinationmode = 3 ; attack nearby fortress
;If a>70 And a=<90 Then aidestinationmode = 4 ; attack nearby units
;If a>90 And a=<100 Then aidestinationmode = 5 ; attack at random

; Set up a mission lookup array

Dim missionselection(8,15,1) ; player 1to 8 and low value and high value
Dim missionselectiondefault(8,15,1) ; holds the default strategy
Dim missionselectionpreset(5,15,1) ; holds temporary strategies
Dim missiontarget(8)
; Player 1 mission selection
missionselection(1,0,0) = 0  : missionselection(1,0,1) = 40 ; retake conquered cities
missionselection(1,1,0) = 40 : missionselection(1,1,1) = 60 ; attack nearby cities
missionselection(1,2,0) = 60 : missionselection(1,2,1) = 70 ; attack nearby fortress
missionselection(1,3,0) = 70 : missionselection(1,3,1) = 90 ; attack nearby units
missionselection(1,4,0) = 90 : missionselection(1,4,1) = 100 ; attack at random
; Player 2 mission selection
missionselection(2,0,0) = 0  : missionselection(2,0,1) = 50 ; retake conquered cities
missionselection(2,1,0) = 50 : missionselection(2,1,1) = 60 ; attack nearby cities
missionselection(2,2,0) = 60 : missionselection(2,2,1) = 70 ; attack nearby fortress
missionselection(2,3,0) = 70 : missionselection(2,3,1) = 90 ; attack nearby units
missionselection(2,4,0) = 90 : missionselection(2,4,1) = 100 ; attack at random
; Player 3 mission selection
missionselection(3,0,0) = 0  : missionselection(3,0,1) = 20 ; retake conquered cities
missionselection(3,1,0) = 20 : missionselection(3,1,1) = 30 ; attack nearby cities
missionselection(3,2,0) = 30 : missionselection(3,2,1) = 40 ; attack nearby fortress
missionselection(3,3,0) = 40 : missionselection(3,3,1) = 90 ; attack nearby units
missionselection(3,4,0) = 90 : missionselection(3,4,1) = 100 ; attack at random
; Player 4 mission selection
missionselection(4,0,0) = 0  : missionselection(4,0,1) = 20 ; retake conquered cities
missionselection(4,1,0) = 20 : missionselection(4,1,1) = 30 ; attack nearby cities
missionselection(4,2,0) = 30 : missionselection(4,2,1) = 50 ; attack nearby fortress
missionselection(4,3,0) = 50 : missionselection(4,3,1) = 70 ; attack nearby units
missionselection(4,4,0) = 70 : missionselection(4,4,1) = 100 ; attack at random
; Player 5 mission selection
missionselection(5,0,0) = 0  : missionselection(5,0,1) = 70 ; retake conquered cities
missionselection(5,1,0) = 70 : missionselection(5,1,1) = 75 ; attack nearby cities
missionselection(5,2,0) = 75 : missionselection(5,2,1) = 80 ; attack nearby fortress
missionselection(5,3,0) = 80 : missionselection(5,3,1) = 90 ; attack nearby units
missionselection(5,4,0) = 90 : missionselection(5,4,1) = 100 ; attack at random
; Player 6 mission selection
missionselection(6,0,0) = 0  : missionselection(6,0,1) = 10 ; retake conquered cities
missionselection(6,1,0) = 10 : missionselection(6,1,1) = 20 ; attack nearby cities
missionselection(6,2,0) = 25 : missionselection(6,2,1) = 30 ; attack nearby fortress
missionselection(6,3,0) = 30 : missionselection(6,3,1) = 40 ; attack nearby units
missionselection(6,4,0) = 40 : missionselection(6,4,1) = 100 ; attack at random
; Player 7 mission selection
missionselection(7,0,0) = 0  : missionselection(7,0,1) = 10 ; retake conquered cities
missionselection(7,1,0) = 10 : missionselection(7,1,1) = 20 ; attack nearby cities
missionselection(7,2,0) = 20 : missionselection(7,2,1) = 30 ; attack nearby fortress
missionselection(7,3,0) = 30 : missionselection(7,3,1) = 50 ; attack nearby units
missionselection(7,4,0) = 50 : missionselection(7,4,1) = 100 ; attack at random
; Player 8 mission selection
missionselection(8,0,0) = 0  : missionselection(8,0,1) = 10 ; retake conquered cities
missionselection(8,1,0) = 10 : missionselection(8,1,1) = 20 ; attack nearby cities
missionselection(8,2,0) = 20 : missionselection(8,2,1) = 50 ; attack nearby fortress
missionselection(8,3,0) = 50 : missionselection(8,3,1) = 90 ; attack nearby units
missionselection(8,4,0) = 90 : missionselection(8,4,1) = 100 ; attack at random

; Copy the values into the default array
For i=1 To 8
For ii=0 To 15
For iii=0 To 1
missionselectiondefault(i,ii,iii) = missionselection(i,ii,iii)
Next
Next
Next

; Ai setting 0 - Concentrate 80 percent of new forces to retake lost cities
missionselectionpreset(0,0,0) = 0  : missionselectionpreset(0,0,1) = 80 ; retake conquered cities
missionselectionpreset(0,1,0) = 80 : missionselectionpreset(0,1,1) = 81 ; attack nearby cities
missionselectionpreset(0,2,0) = 81 : missionselectionpreset(0,2,1) = 90 ; attack nearby fortress
missionselectionpreset(0,3,0) = 90 : missionselectionpreset(0,3,1) = 99 ; attack nearby units
missionselectionpreset(0,4,0) = 99 : missionselectionpreset(0,4,1) = 100 ; attack at random

; Ai setting 1 - Retake cities and attack nearby units
missionselectionpreset(1,0,0) = 0  : missionselectionpreset(1,0,1) = 40 ; retake conquered cities
missionselectionpreset(1,1,0) = 40 : missionselectionpreset(1,1,1) = 45 ; attack nearby cities
missionselectionpreset(1,2,0) = 45 : missionselectionpreset(1,2,1) = 50 ; attack nearby fortress
missionselectionpreset(1,3,0) = 50 : missionselectionpreset(1,3,1) = 99 ; attack nearby units
missionselectionpreset(1,4,0) = 99 : missionselectionpreset(1,4,1) = 100 ; attack at random
 
; Ai setting 2 - Attack every nearby unit
missionselectionpreset(2,0,0) = 0  : missionselectionpreset(2,0,1) = 10 ; retake conquered cities
missionselectionpreset(2,1,0) = 10 : missionselectionpreset(2,1,1) = 20 ; attack nearby cities
missionselectionpreset(2,2,0) = 20 : missionselectionpreset(2,2,1) = 25 ; attack nearby fortress
missionselectionpreset(2,3,0) = 25 : missionselectionpreset(2,3,1) = 99 ; attack nearby units
missionselectionpreset(2,4,0) = 99 : missionselectionpreset(2,4,1) = 100 ; attack at random

; Ai setting 3 - Attack nearby units and attack nearby fortresses
missionselectionpreset(3,0,0) = 0  : missionselectionpreset(3,0,1) = 10 ; retake conquered cities
missionselectionpreset(3,1,0) = 10 : missionselectionpreset(3,1,1) = 20 ; attack nearby cities
missionselectionpreset(3,2,0) = 20 : missionselectionpreset(3,2,1) = 50 ; attack nearby fortress
missionselectionpreset(3,3,0) = 50 : missionselectionpreset(3,3,1) = 99 ; attack nearby units
missionselectionpreset(3,4,0) = 99 : missionselectionpreset(3,4,1) = 100 ; attack at random




;visibilitieactive = True

Dim minemap(100,100,5) ; 0=active;1=owner;2=income
Const mine_active = 0
Const mine_alignment = 1
Const mine_income = 2

; AI settings
Global difficultylevel = 20 ; lower is harder (currently handled in the game)
Dim playerdifficultylevel(8)
For i=1 To 8
playerdifficultylevel(i) = 20
Next
Global roadclearance = 4 ; the higher the number the fewer attacks thru the road will follow


; For the defend player function
Global player1homeforceupkeep = 3
Global player2homeforceupkeep = 2
Global player3homeforceupkeep = 2
Global player4homeforceupkeep = 3
Global player5homeforceupkeep = 2

Global player6homeforceupkeep = 3
Global player7homeforceupkeep = 3
Global player8homeforceupkeep = 2

;
;


Global delayafterplayermovement = False ; when true the delay will go into effect(inloop)
Global delayafterplayermovementcenter = False
Global delayafterplayermovementtime = 400 ; the milliseconds to delay with
Global delayafterplayermovementcountdown = 2; delays the end of movement delay for a number of frames
Global delayafterplayermovementcountdowndefault = 2

; Contains the width height divider for the minimap
Global mmstepx#
Global mmstepy#

; End of turn flicker timer
Global endoftimetimer = MilliSecs()
Global endoftimedelay = 300
Global endoftime = 1


Global minimapdragactive = False
Global currentminimapzone
Global numminimapzones = 6
Dim minimapzones(numminimapzones,4) ; 0,0 = active, 1,2,3,4 = xyx2y2


;minimapzones(0,0) = True
;minimapzones(0,1) = 10
;minimapzones(0,2) = 10
;minimapzones(0,3) = 30
;minimapzones(0,4) = 30

Dim playerlostbattles(8,100,100)


; For switching the music at a certain time
Global mainchannel ; The channel the music is played in
Global musicplayingtimer = MilliSecs()
Global musicplayingdelay = 180000
Global musicplaying = 6

; For diplaying a message after a cerain time
Global delayedmessagetimer
Global delayedmessagedelay = 1000
Global delayedmessage

Global afterbattleflag = False
Global afterbattletimer 

; Unit buttonoperator
Global unitcurrentbutton = 0

; End of turn button thingy
Global endofturnbutton = 0

; Buttonoperator For the game menu/options
Global gameoptionbutton = 0 ; for the game options

; Editmode setup
Global editcreateunittype = 1 ; (0 to numberofunitgraphics)
Global editcreateunitplayer = 1 ; (1 to 8)

Global editcurrentbutton = 0 ; for the edit buttons

Global editmode = False ; active in game or not active

Const maxcities = 500
Const maxunits = 2000
Const maxplayers = 8

;
; Player vs player/ai state (war/peace)
;
;
Dim warstate(10,10)
;

;
; Buttons id for the units selection gui window
;
Global suacbutton = 0

;
; This type contains all the onscreen coordinates
;
Type screencoords1
Field x,y
End Type
;

Dim coordinatebuffer(30,30,3)
Dim coordinatebuffer2(1000,1)

; This one will contain the binary data used to create preset roads
Dim roadimage(8)
Dim roadmapstring$(100,100)
Dim roadmap(100,100)
Dim roadscheme(10)

; This array is used to return which value is highest
Dim values(100,1)

; This one contains the current human playing
Global currentplayer

; This one contains the gold
Dim playergold(8)

; Array that holds temporary units for displaying on a window
; 0 = units number / 1 = active or not active / 2 = unit type
; 3 = x loc / 4 = y loc
Dim tempunits(maxunits,3)

;holds the turn of the game
Global gameturn
; holds the value if all units have moved
Global endofturn

; This is for the flickering of the active unit
Global flickertimer
Global flickerswitch
Dim flickerstat(1)

; holds preset screen coordinates
Dim groundlayer(1000,3)
Dim layer1(10,19,1,1) 
Dim layer2(10,19,1,1)

; FPS 
Global fpstimer
Global fpscounter
Global fpscounter2

; True/False displayes the grid (ctrl+g to activate)
Global grid

; Holds the tiles
Dim map(102,102)
;
;Case 0 ; gras
;Case 1 ; hills
;Case 2 ; moutain
;Case 3 ; pines
;Case 4 ; swamp
;Case 5 ; trees
;Case 6 ; goldmountain
;Case 7 ; mine
;Case 8 ; crops
;Case 9 ; trees and beaver
;Case 64 ; water
;
;
;
;
;
;
Dim coastlinemap(204,204) ; 2*2 - 4 coasttiles per squeare
Dim fortressmap(100,100)

; Last unit moved
Global lastunit = -1

; Location top/left location
Global mapx,mapy
Global oldmapx,oldmapy

; Holds the cursor position
Dim cursor(1)
; This shows the visible cursor when no city or unit is below the cusor(x)
Global visiblecursor = False ; active true false


; Holds the width of the playing field
Global mapwidth = 100
Global mapheight = 100

; x,y,0 = unit type, x,x,1 = fortified ; x,y,2 = building ; x,y,3 = sentry
; x,y,4 = contains a fortress
Dim tempmap(32,41,6) ; this one contains the visible units (6 - stacked counter)
Dim playermap(32,41) ; this one contains the owner of the units ea player 1/2/3/4
Dim citymap(32,41,1) ; this one contains the visible cities
Dim playercitymap(32,41) ; this one contains the ownder of the cities

; Units setup
Dim units(maxunits,32) ; holds almost all the unit information ( see isopreset for more info)
Const unit_active = 0
Const unit_x = 1
Const unit_y = 2
Const unit_ontop = 3
Const unit_type = 4
Const unit_attack = 5
Const unit_defense = 6
Const unit_damage = 7
Const unit_fortified = 8
Const unit_veteran = 9
Const unit_moves = 10
Const unit_alignment = 11
Const unit_onhold = 12
Const unit_ai = 13
Const unit_incity = 14
Const unit_turnsinactive = 15
Const unit_homecity = 16
Const unit_cancarry = 17 ; Number of units that he can carry
Const unit_carried = 18 ; Contains the unit that it is carrying
Const unit_invisible = 19 ; not visible for the player (being carried
Const unit_terraintype = 20 ; land/sea or air
Const unit_canbecarried = 21 ; true or false
Const unit_debug = 32


; This array holds the starting position for the last found unit, used in findnextmovableunit(player)
Global lastunitstartx = -1 
Global lastunitstarty = -1

Dim unitsmove#(maxunits) ; hold the movement float
Dim unitstring$(maxunits,5) ; holds string information on the units (not yet used)

;
; Holds the names of the units - Loaded in isopreset
;
Dim unitsname$(17)

Dim unitdefault(17,32) ; holds the default values for the units
;
Const default_attack = 0
Const default_defense = 1
Const default_movement = 2
Const default_hitpoints = 3 ; not implemented
Const default_cost = 4;/ gold cost
Const default_cancarry = 5 ;/ x = amount carried
Const default_terraintype = 6;/ 012/land/sea/air
Const default_canbecarried = 7; true/or/false
Const default_reqid1 = 10
Const default_reqamount1 = 11
Const default_reqid2 = 12
Const default_reqamount2 = 13
Const default_reqid3 = 14
Const default_reqamount3 = 15
Const default_reqid4 = 16
Const default_reqamount4 = 17

;unitdefault(8,0) = 11; attack
;unitdefault(8,1) = 7 ; defense
;unitdefault(8,2) = 3 ; movement
;unitdefault(8,3) = 3 ; hitpoints
;unitdefault(8,4) = 100; cost
;unitdefault(8,5) = 0 ; can carry x units
;unitdefault(8,6) = 1 ; 0 = land - 1sea - 2air
;
;
;
;
;
;


; This contains which players are played by the human player
Dim humanplays(10)

; This one holds the integer data for the cities
Dim cities(maxcities,20)
Const city_active = 0
Const city_x = 1
Const city_y = 2
Const city_level = 3
Const city_alignment = 4
Const city_taxincome = 5

;
; Holds the cities units build queue, each turn a unit is build
;
Dim citiesunitsproduction(maxcities,5)

; This one holds the string data for the cities
Dim citiesstring$(maxcities,0)

Dim currentunit(2) ; x,y,unit
;Global playerplays = 3

; Holds the getkey keypress
Global currentkey

; Holds the screen resolution
Global screenwidth 
Global screenheight

; this one contains the player colors
Dim playercolor(8,2)

Dim battlemod(3) ; a array to store battle values ; checkforenemies ; dobattle



; Statistics / ai lookup table
;
;
.ailookup
Global ai_retakecities = 20
Global ai_attacknearbycities = 20
Global ai_attackatrandom = 20
Global ai_attacknearbyunits = 20
Global ai_attacknearbyfortress = 20

Dim playercityconquest(maxcities,2) ; 0 = original owner - 1 = new owner - 2 = turn lost

;
; Cities conquered works like this. The array stores which city is lost to which player
;
;




; Graphic resources setup

; these two contain the unit and city graphics
Dim unitgraphics(16)
Dim citygraphics(10)

; Thise one contains the buttons
Dim buttons(10)

;
; Special screnario flags
;
Global settlerbuildroads = True
Global settlerbuildcities = True
Global settlerbuildfotress = True

;
; These hold the values for the last inputed text in the createcity routine
;
Dim createcitydefault$(10)
createcitydefault$(0) = "City"
createcitydefault$(1) = "1"
createcitydefault$(2) = "1"
createcitydefault$(3) = "1"


.pathfindingcontainers
Global numshortpaths = 250
Global nummediumpaths = 100
Global numlongpaths = 50

Dim shortpathmapactive(250)
Dim shortpathmap(250,32,1)
Dim mediumpathmapactive(100)
Dim mediumpathmap(100,200,1)
Dim longpathmapactive(50)
Dim longpathmap(50,1000,1)

.aisetup
Dim citiesdestinations(maxcities,1) ; holds the citydestionations for the 
Dim areaunitmap(12,12,2) ; active, x, y
Dim unitsdestinations(maxunits,1)
Dim fortressdestinations(100*100,1)
Const maxmissions = 500
Dim aimission(maxmissions) ; What is the current mission
Dim aimissionpathpointer(maxmissions,1) ; points to the path
								; medium or long
Dim aimissionwashlist(maxmissions,10) 	; our complete ai unit strategy
Dim aimissionpointer(maxmissions) ; location in the path for the mission

;
; for the auto moving of untis
;
Global numpaths = 250
Dim pathdestination(maxunits,2) ; 0=x,1=y,2=pointer to which buffer
Dim pathbuffer(numpaths,1000,2) ; 250 containers of 1000 sets of 2 coordinates
Dim pathbufferactive(numpaths)
Dim pathbufferpointer(numpaths)

.setGraphicsmode
If fullscreen = True Then
	Graphics 800,600,32,1
Else
	Graphics 800,600,32,2
End If

SetBuffer BackBuffer()

Global alphabuffer = CreateImage(100,100) ; for alpha blending
MaskImage alphabuffer,0,0,0

;
; Show the title image
;
If showtitleimage = True Then
	md= MilliSecs() + 1000
	fl = False
	titleimage = LoadImage("graphics/title.png")
	timer = CreateTimer(40)
	While KeyDown(1) = False And MouseDown(1) = False And MouseDown(2) = False And KeyDown(57) = False 
		Cls
		If fl=False And MilliSecs() > md Then playdamusic(14) : fl = True
		WaitTimer(timer)
		DrawBlock titleimage,0,0
	Flip
	Wend
	Cls
	Flip
	FreeImage titleimage
	Delay(200)
	FlushKeys()
	FlushMouse()
End If


Include "isolistbox.bb"
Include "isogui.bb"
Include "isoinclude.bb"
Include "isoeconomy.bb"
Include "isopreset.bb" ; requires data from isoeconomy
Include "isopath.bb"
Include "isomapgen.bb"
Include "isoai.bb"
Include "isocity.bb"
Include "isoloadsave.bb"
Include "isoedit.bb"
Include "isodiplomacy.bb"
Include "alphablending.bb"

amap(0,0) = 0
;
;Global isobackground1 = LoadImage("background1.bmp")
;Global isobackground2 = LoadImage("background2.bmp")

Global isodesert = LoadImage("graphics/isodesert.bmp")
;
Global isocoastlines = LoadAnimImage("graphics/coastlines.bmp",32,16,0,48)
;
Global isowater = LoadImage("graphics/water.bmp")
Global isogras = LoadImage("graphics/isogras.bmp")
Global isohills = LoadImage("graphics/isohills.bmp")
Global isomountain = LoadImage("graphics/isomountain.bmp")
Global isopinetrees = LoadImage("graphics/isopinetrees.bmp")
Global isoswamp = LoadImage("graphics/isoswamp.bmp")
Global isotrees = LoadImage("graphics/isotrees.bmp")
; v1.3+ gameplay expansion
Global isogoldmountain = LoadImage("graphics/isomountainwithgold.bmp")
Global isocrops = LoadImage("graphics/isocrops.bmp")
Global isobeaver = LoadImage("graphics/isobeaver.bmp")
Global isomine = LoadImage("graphics/isomine.bmp")
MaskImage isogoldmountain,255,0,255
MaskImage isocrops,255,0,255
MaskImage isobeaver,255,0,255
MaskImage isomine,255,0,255
;

MaskImage isowater,0,0,0
MaskImage isopinetrees,255,0,255
;
Global isogrid = LoadImage("graphics/isogrid.bmp")
Global isocursor = LoadImage("graphics/isolight6432.bmp")
Global mousepointer = LoadImage("graphics/mousepointer2.bmp")
Global singlepixel = LoadImage("graphics/pixel.bmp")

Global playerunit = LoadImage("graphics/player.bmp")
Global barbarianunit = LoadImage("graphics/barbarian.bmp")

Global minifont = LoadFont ("arial",12,1)
Global mainfont = LoadFont ("arial",18,1)

;
; Here we load the units
;
;simplemessage2("|graphics/"+unitsgraphsnames$(0)+".bmp|") : End
For i=0 To 16
	unitgraphics(i) = LoadImage("graphics\"+unitsgraphsnames$(i)+".bmp")
	If unitgraphics(i) = 0 Then simplemessage2("Unit graphic(s) " + i + "not found - Check 'units.txt'") : End
Next

; Set the mask image for the units
For i=0 To 16
MaskImage unitgraphics(i),135,83,135
Next



Const numberofunitgraphics = 17

; Here we load the units
citygraphics(0) = LoadImage("graphics/city01.bmp")
citygraphics(1) = LoadImage("graphics/city02.bmp")
citygraphics(2) = LoadImage("graphics/city03.bmp")
citygraphics(3) = LoadImage("graphics/city04.bmp")

; Here we load in the buttons
buttons(0) = LoadImage("graphics/buttonexit.bmp")
buttons(1) = LoadImage("graphics/buttoncursup.bmp")
buttons(2) = LoadImage("graphics/buttoncursright.bmp")
buttons(3) = LoadImage("graphics/buttoncursdown.bmp")
buttons(4) = LoadImage("graphics/buttoncursleft.bmp")
buttons(5) = LoadImage("graphics/buttonexitsmall.bmp")

roadimage(0) = LoadImage("graphics/road01.bmp")
roadimage(1) = LoadImage("graphics/road02.bmp")
roadimage(2) = LoadImage("graphics/road03.bmp")
roadimage(3) = LoadImage("graphics/road04.bmp")
roadimage(4) = LoadImage("graphics/road05.bmp")
roadimage(5) = LoadImage("graphics/road06.bmp")
roadimage(6) = LoadImage("graphics/road07.bmp")
roadimage(7) = LoadImage("graphics/road08.bmp")
roadimage(8) = LoadImage("graphics/road09.bmp")

.soundsetup

Global debugchannel

; Load the sfx
;
Global victorysound = LoadSound("sounds/victory.wav")
Global battlesound = LoadSound("sounds/battle.wav")
Global boosound = LoadSound("sounds/boo.wav")
Global clicksound = LoadSound("sounds/click.wav")
Global closewindowsound = LoadSound("sounds/closewindow.wav")
Global createunitsound = LoadSound("sounds/createunit.wav")
Global moveunitsound = LoadSound("sounds/moveunit.wav")
Global coinssound = LoadSound("sounds/coins.wav")




; Here we jump to the setup of the images
setuproad()


; Here wel load the fortified image
Global fortified = LoadImage("graphics/fortified.bmp")
Global building = LoadImage("graphics/building.bmp")
Global buildingroad = LoadImage("graphics/buildingroad.bmp")
Global sentry = LoadImage("graphics/sentry.bmp")
Global fortress = LoadImage("graphics/fortress.bmp")
Global fortified2 = LoadImage("graphics/fortified2.bmp")

; Here we load the fighting graphics anim image 4*16*16
;Global fighting = LoadAnimImage("fighting.bmp",16,16,0,4)

; Here we load the stacked unit graphics
Global stackedgraphics = LoadAnimImage("graphics/stackedimages.bmp",10,10,0,5)
MaskImage stackedgraphics,255,255,255

For i=0 To 3
MaskImage citygraphics(i),135,83,135
Next

MaskImage isogrid,255,255,255

Global minmap = CreateImage(158,158)
Global minmapter = CreateImage(158,158)
MaskImage minmapter,0,0,0
MaskImage minmap,0,0,0
Global roadbufferimage = CreateImage(800,600)
Global mapbuffer = CreateImage(800,600)
MaskImage mapbuffer,155,255,55
Global mapbuffer2 = CreateImage(800,600)
MaskImage mapbuffer2,155,255,55

Global intromessage = 0



musicplayingsystem()

presetmap
validateactiveunits ; update the units with the preset values
drawminimap : maketempmap
grid=False

SeedRnd MilliSecs()

flickertimer = MilliSecs()
flickerswitch = True

currentplayer = 1

loadgame("savegames/screnario#1.sav")

;mg_setupmap(40)

drawminimap

endofturn = False

holdwaitingunits = True

MoveMouse screenwidth/2,screenheight/2 ; center the mouse on the screen

drawmap

; Needed for the flicker of the unit
afterbattleflag = False

;gameturn = 0
;playergold(1) = 5000
gameloop = True

;Global mainchannel = PlayMusic("music/ingame01.mod")

For zzi=1 To 8
createbasevisibleareamap(zzi)
Next



maketempmap : drawmap : drawminimapterrain : drawminimap

;For i=0 To maxcities
;If cities(i,0) = True Then
;For ii=0 To maxunits
;If units(ii,0) = True Then
;If units(ii,1) = cities(i,1) And units(ii,2) = cities(i,2) Then
;units(ii,11) = cities(i,4)
;End If
;End If
;Next
;End If
;Next

; For the unit automovement
Global playerfindpath = False
Global playerpathstackmove = False
Global playerpathstartx = False
Global playerpathstarty = False
Global playerpathunit = False


Global gametimer = CreateTimer(60)
;


firsttutorialtimer = MilliSecs()
firsttutorialvisited = False


Global terraineditchangeeffect = MilliSecs()
Global terraineditchangeeffectactive = False

.maingameloop


; ---------------------------------------------------------------------------------------

timer = CreateTimer(60)


While (gameloop = True) Or (we<>$803)

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
			currentkey = GetKey() ; copy the pressed key in currentkey
			Cls ; clear the screen	
			scrollingsystem2()	
			mainloopoptimalizations
			gamestartmessages()
			tutorialfunction()
			mapscrolling()	
			musicplayingsystem()
			mainloopinput()			
			drawmapscreen()		
			drawgamescreen ; draw the righter game screen		
			endoftimesystem() ; run the end of time function
			unitcontrols()
			moveunitcontrol()
			unitsoptimalization()
			isocollision ; check mouse vs tile/unit
			gotofunction()	
			dominimap ; draw the minimap		
			mainloopextensions()
			showdebuginfo()	
			mainlooptooltips()
			;
			SetFont minifont : Text 0,0,"Vid mem : " + TotalVidMem() + " free : " + AvailVidMem() + " Used : " + (TotalVidMem()-AvailVidMem())
			;
			DrawImage mousepointer,MouseX(),MouseY() ; lastly draw the mousepointer		
			Flip :	screenshotfunction()
		Else
		
		End If
		;
End Select; (B+ loop)

Wend

.maingameend

End


;
;
;
;

;
; This function lets the player scroll the map
;
;
Function mapscrolling()

If fullscreen = False And set_autoscrollingwindowed = False Then Return
If fullscreen = True And set_autoscrollingfullscreen = False Then Return

If moused(1) = True Then Return
If moused(2) = True Then Return
If moused(3) = True Then Return

	; Switch a value
	yohoe = False
	; Game screen scrolling
	If MouseY() = 0 Then
	mapy=mapy - 1
	mapx=mapx - 1
	yohoe = True
	End If
	If MouseY() > 595 Then
	mapy=mapy + 1
	mapx=mapx + 1
	yohoe = True
	End If
	If MouseX() = 0 Then
	mapx = mapx - 1
	mapy = mapy + 1
	yohoe = True
	End If
	If MouseX() > 795 Then
	mapx = mapx + 1
	mapy = mapy - 1
	yohoe = True
	End If

	; Update the screen if we have moved the map
    If yohoe = True Then maketempmap : drawmap

	; Keep the map insode a boundry
	If mapx<-19 Then mapx=-19
	If mapx>82 Then mapx=82
	If mapy<-19 Then mapy=-19
	If mapy>82 Then mapy=82
End Function
; End of scrolling system

;
; This moves all auto movable units until their moves are gone
;
;
Function automoveunits(player)
For i=0 To maxunits
	If units(i,0) = True Then
	If units(i,unit_alignment) = player Then
		If units(i,13) => 30000 And units(i,13)<39999 Then
			;
			;
			exitloop = False
			While unitsmove(i) => .33 And exitloop = False
				;Global numpaths = 250
				;Dim pathdestination(maxunits,2) ; 0=x,1=y,2=pointer to which buffer
				;Dim pathbuffer(numpaths,1000,2) ; 250 containers of 1000 sets of 2 coordinates
				;Dim pathbufferactive(numpaths)
				;Dim pathbufferpointer(numpaths)
				;
				slot = units(i,13) - 30000
				If slot =< 0 Then simplemessage("pathslot error in automoveunits")
				;
				x = pathbuffer(slot,pathbufferpointer(slot),0)
				y = pathbuffer(slot,pathbufferpointer(slot),1)
				;
				; If we have arived at the destination
				If x = -1 Then 
					units(i,13) = 0;
					pathbufferactive(slot) = False					
					pathbufferpointer(slot) = 0
					exitloop = True;
				End If
				; Do the moving while we have not arived at the location
				If x > -1 Then
					unitsmove(i) = unitsmove(i) - .33
					units(i,1) = x
					units(i,2) = y
					maketempmap():drawmap()
				End If
				; Increase the movement position
				If x>-1 Then
					pathbufferpointer(slot) = pathbufferpointer(slot) + 1
				End If
				
				;
			Wend
			; Set the moves to zero if we cannot move any more
			If unitsmove(i) < 0.33 Then unitsmove(i) = 0
			;
		End If
	End If
	End If
Next
.exitautomoveunits
End Function



Function moveunit(unit)


; First check if we have the right key input
continue = False

If currentkey = 28 Then continue=True
If currentkey = 29 Then continue=True
If currentkey = 30 Then continue=True
If currentkey = 31 Then continue=True
If currentkey = 5  Then continue=True
If currentkey = 6  Then continue=True
If currentkey = 2  Then continue=True
If currentkey = 1  Then continue=True
If continue = False Then Goto skipout

If KeyDown(42) = True Or KeyDown(54) = True Then manualgroupmove = True Else manualgroupmove = False

; Play the movement sound
playsfx(6)

; reset the flicker so we can see the unit move
flickerswitch=1:flickertimer=MilliSecs()

; Check if the user can move, if not then exit this routine
;If units(currentunit(2),10)=<0 Then
;Goto skipout
;End If
; Check if the user can move, if not then exit this routine
If unitsmove(currentunit(2))=<0 Then
	Goto skipout
End If




; Copy the unit coordinates into x and y
x = units(unit,1)
y = units(unit,2)
; Copy the unit coordinates into previousx and previousy
previousx = x
previousy = y


; Do the movement
Select currentkey
	Case 28
		x=x-1:y=y-1
	Case 5
		y=y-1
	Case 30
		x=x+1
		y=y-1
	Case 6
		x=x+1
	Case 29
		y=y+1
		x=x+1
	Case 2
		y=y+1
	Case 31
		x=x-1
		y=y+1
	Case 1
		x=x-1
End Select

; Do a basic border check
If x<0 Then x=0:Goto skipout
If y<0 Then y=0:Goto skipout
If x>mapwidth Then x=mapwidth 
If y>mapheight Then y=mapheight

; Check if the destination is a zone of control
If zoneofcontrol(unit,x,y)=True Then
	simplemessage("You cannot move here. (Zone Of Control)")
	Goto skipout
End If

; If the tile is water then abandon
watermove = False
If unitdefault(units(unit,unit_type),default_terraintype) = 1 Then watermove = True
;If units(unit,unit_type) = 15 Then watermove = True
;If units(unit,unit_type) = 16 Then watermove = True
;If units(unit,unit_type) = 17 Then watermove = True
If watermove = False Then
	If map(x,y) = 64 And unitcancarry(x,y) = -1 Then
		Goto skipout
	End If
	Else
	If map(x,y) <> 64 And getcitynumber(x,y) = -1 Then
		If unitcarries(unit) > 0 Then
			units(unit,unit_onhold) = True
			units(unit,unit_ontop) = False
			units(unit,15) = 1
			activatecarriedunits(unit)
		End If
		findnextmovableunit(currentplayer)
		Goto skipout
	End If
End If
.boat
; If moved on a boat or thing
If units(unit,unit_cancarry) = False Then
	If unitdefault(units(unit,unit_type),default_canbecarried) = True Then
		a = unitcancarry(x,y)
		If map(x,y) = 64 And a >-1 Then
		units(unit,unit_x) = x
		units(unit,unit_y) = y
		unitsmove(unit) = unitsmove(unit) - 1
		units(unit,unit_carried) = a
		units(unit,unit_invisible) = True
		units(unit,unit_ontop) = False
		units(a,unit_ontop) = True
		findnextmovableunit(currentplayer)
		Goto skipout
		End If
	End If
End If
;


; Check if the destination square is a road
If roadmap(x,y) = 1 Then
	moveonroad = True
	Else 
	moveonroad = False
End If

; Remove the sentry mode from non player units
removesentryinarea(units(unit,11),x,y)


; Check if the unit is moving in enemy occupied area
; if so then do battle

If isunitonenemycity(unit,x,y)=False And fortressmap(x,y) = False Then
	If checkforenemies(unit,x,y)=True Then
		If warstate(findenemyplayer(x,y),currentplayer) = False Then simplemessage("You are now at war")
	    setwarstate(findenemyplayer(x,y))
		If unitsmove#(unit)<1 Then
			simplemessage("The men refuse to fight")
			Goto skipout
			Else ; enough moves for a battle
			unitsmove(unit) = unitsmove(unit) - 1
			; Do the battle
			
			; Do the stacked battle thing
			stackedbat = False
			If KeyDown(42) = True Or KeyDown(54) Then
			
				dostackedbattle(unit,x,y)
				stackedbat = True
			
			End If
			
			If dobattle(unit,x,y)=False And stackedbat = False Then ; if the battle is lost
				units(unit,0)=False
				currentunit(0)=-1
				currentunit(1)=-1
				currentunit(2)=-1
				;showbattleresults(2)
				makedefendingunitveteran(x,y) ; make the defending unit a veteren (1vs3)			
				; set new coordinates
				afterbattleflag = True
				afterbattletimer = MilliSecs()
				flickerswitch = 1
				flickertimer = MilliSecs()
				playsfx(2)
				simplemessage("You have lost the battle")
				;findnextmovableunit(1)
				; Bugfix ** move unit ontop on previous location
				If isthereanyunitpresent(previousx,previousy) = True Then
					createontopunit(currentplayer,previousx,previousy)
				End If	
				Return
					
				Else ; if we have won the battle
				removeontopunit(x,y)
				;
				;
				simplemessage("you have won the battle")
				If isthereanyunitpresent(x,y) = True Then
					;
					;simplemessage("units present")
					; place another unit ontop
					For i=0 To maxunits
					If units(i,0) = True Then
					If units(i,1) = x And units(i,2) = y Then
					units(i,3) = True 
					Exit
					End If
					End If
					Next
					;
					x = previousx
					y = previousy
					units(unit,1) = x
					units(unit,2) = y
					currentunit(0) = x
					currentunit(1) = y
					If Rand(50) = 1 Then simplemessage("The battle is not over yet")
				End If
				
			End If;showbattleresults(1)
		End If
	End If
End If

	; check If the unit is moving against a fort
	If fortressmap(x,y) = True Then
		If checkforenemies(unit,x,y) = True Then
		If warstate(findenemyplayer(x,y),currentplayer) = False Then simplemessage("You are now at War")
			setwarstate(findenemyplayer(x,y))

			; Do the stacked battle thing
			stackedbat = False
			If KeyDown(42) = True Or KeyDown(54) Then
			
				dostackedbattle(unit,x,y)
				stackedbat = True
			
			End If

			If dobattle(unit,x,y) = True And stackedbat = False Then
				simplemessage("You have won the battle")
				makehumanunitveteran
				removeontopunit(x,y)
				unitsmove(unit) = unitsmove(unit) - 1
				If checkforenemies(unit,x,y) = False Then
					deleteallunitsattile(units(getunitnumber(x,y),unit_alignment),x,y)
					simplemessage("You have captured a fort")
					; set new coordinates
					units(unit,1) = x
					units(unit,2) = y
					currentunit(0) = x
					currentunit(1) = y
					updatevismap(currentplayer,x,y)
				End If
				;
				Goto movselectnewunit
				Else
				playsfx(2)
				simplemessage("You have lost the battle")
				units(unit,0) = False
				currentunit(0) = -1:currentunit(1) = -1:currentunit(2) = -1
				makedefendingunitveteran(x,y)
				If isthereanyunitpresent(previousx,previousy) = True Then
				createontopunit(currentplayer,previousx,previousy)
				End If
				findnextmovableunit(currentplayer)
				Goto skipout
			End If
		End If
	End If

	; Check if the unit is moving into a enemy city.
	;
	If isunitonenemycity(unit,x,y)=True Then
		If checkforenemies(unit,x,y)=True Then
		If warstate(findenemyplayer(x,y),currentplayer) = False Then simplemessage("You are now at war")
			setwarstate(findenemyplayer(x,y))

			; Do the stacked battle thing
			stackedbat = False
			If KeyDown(42) = True Or KeyDown(54) Then
			
				dostackedbattle(unit,x,y)
				stackedbat = True
			
			End If


			If dobattle(unit,x,y) = True And stackedbat = False Then ; if we won the battle
				playsfx(0)
				simplemessage("You have won the battle")
				makehumanunitveteran
				removeontopunit(x,y)
				;units(unit,10) = units(unit,10)-1
				unitsmove(unit) = unitsmove(unit)-1
				Goto movselectnewunit
			
				Else ; if we have lost the battle
			
				playsfx(2)
				simplemessage("You have lost the battle")
				units(unit,0) = False
				currentunit(0) = -1
				currentunit(1) = -1
				currentunit(2) = -1
				makedefendingunitveteran(x,y)
				; bugfix ** create ontop unit on previous location
				If isthereanyunitpresent(previousx,previousy) = True Then
					createontopunit(currentplayer,previousx,previousy)
				End If
				findnextmovableunit(currentplayer)
				Goto skipout
			End If

			Else ; if we get here it means that there are no enemy units inside the city
			
			player = cities(getcitynumber(x,y),city_alignment)
			deleteallunitsattile(player,x,y)
			cities(getcitynumber(x,y),4) = units(unit,11)
			playercityconquest(getcitynumber(x,y),0) = player
			playercityconquest(getcitynumber(x,y),1) = currentplayer
			playercityconquest(getcitynumber(x,y),2) = gameturn
			simplemessage("You have captured a city")
		End If
	End If


	; Center the screen on the unit
	If centerscreencheck(x,y) = True Then
	centerscreen(x,y)
	End If

	; set new coordinates
	

	If manualgroupmove = True Then 

		manualmovegroup(unit,x,y,moveonroad)	
	End If

	
	
	units(unit,1) = x
	units(unit,2) = y
	currentunit(0) = x
	currentunit(1) = y
	units(unit,unit_invisible) = False
	units(unit,unit_carried) = 0

	movecarriedunits()
	unloadcarriedunits(unit)
	updatevismap(currentplayer,x,y)


	; Check if the unit is stepping inside a city
	;If isunitoncity(unit)=True Then units(unit,14) = True


	; If there are more units underneath the new position then set the ontop to false
	For i=0 To maxunits
		If units(i,0) = True
			If units(i,1) = x And units(i,2) = y Then
				units(i,3) = False
			End If
		End If
	Next	
	; Set moved unit ontop = true
	units(unit,3) = True

	; Check the previous coordinates for more units
	For i=0 To maxunits
		If units(i,0) = True Then
			If units(i,1) = previousx
				If units(i,2) = previousy
					If Not units(i,14) = True
						units(i,3) = True : Goto mu2skipout
					End If
				End If
			End If
		End If
	Next


.mu2skipout

;Cls:flickerswitch=1:maketempmap:drawmap : DrawImage mapbuffer,0,0:drawgamescreen:Flip:wacht(200):flickertimer=MilliSecs()

.movselectnewunit
; here we handle the movement decreasing for the unit
;units(currentunit(2),10)=units(currentunit(2),10)-1
If moveonroad = True Then
	unitsmove(currentunit(2)) = unitsmove(currentunit(2)) - .33
	Else
	unitsmove(currentunit(2)) = unitsmove(currentunit(2)) - 1
End If
; If the unit has no moves left
;If units(currentunit(2),10) =< 0 Or units(unit,14) = True Then
;findnextmovableunit(1):drawminimap:maketempmap
;End If

; If there are no or not enough moves left then set moves to zero 
If unitsmove(currentunit(2))<=0.30 Or units(unit,14) = True Then
	unitsmove(currentunit(2)) = 0
	;
	; If a battle wasn't fought then find a new unit to move
	If afterbattleflag = False Then
		delayafterplayermovement = True
		findnextmovableunit(currentplayer)
	End If
End If



.skipout
fixontopunits()
End Function

;
; Disable the carrying of the units in the boat (if entered city)
;
Function unloadcarriedunits(unit)
For i=0 To maxunits
If units(i,0) = True Then
If units(i,unit_carried) = unit Then
For ii=0 To maxcities
If cities(ii,0) = True Then
If cities(ii,city_x) = units(unit,unit_x) Then
If cities(ii,city_y) = units(unit,unit_y) Then
units(i,unit_carried) = -1
units(i,unit_visible) = True
End If
End If
End If
Next
End If
End If
Next
End Function

Function manualmovegroup(unit,x,y,moveonroad)
For i=0 To maxunits
If units(i,0) = True Then
If units(i,unit_x) = units(unit,unit_x) Then
	If units(i,unit_y) = units(unit,unit_y) Then
	If Not i = unit Then
	If unitsmove(i) > 0 Then
	If units(i,unit_invisible) = False Then
		If units(i,unit_onhold) = False Then
		If units(i,unit_ai) = 0 Then
		If units(i,unit_turnsinactive) = 0 Then
		If units(i,unit_fortified) = False Then
			If units(i,unit_terraintype) = units(unit,unit_terraintype) Then
			If Not map(units(unit,unit_x),units(unit,unit_y)) = 64 Then
				units(i,unit_x) = x
				units(i,unit_y) = y
				If moveonroad = True Then az# = .33 Else az# = 1
				unitsmove(i) = unitsmove(i) - az
				If unitsmove(i)<=0.30 Then unitsmove(i) = 0
				units(i,unit_invisible) = False
				units(i,unit_carried) = 0
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
End Function

;
; This function takes care of the loading and saving....
;
;
Function loadsave(currentkey)
ChangeDir(CurrentDir()+"savegames/")
	If Chr(currentkey) = "s" Then 
		savename$ = gfilerequester(100,100,CurrentDir$(),currentgame,".sav")
		If Not savename$ = 0 Then
			If Not Right(savename$,4) = ".sav" Then savename$ = savename$+".sav"
			savegame(savename$)
			;
			; Remove the directory from the filename
			counter = 0
			If Len(loadname$) > 1 Then
				For i=Len(loadname$) To 1 Step -1
					If Mid(loadname$,i,1) = "/" Or Mid(loadname$,i,1) = "\" Then Exit					
					counter = counter + 1
				Next
				counter = counter - 1 
				currentgame$ = Mid(loadname$,Len(loadname$)-counter,Len(loadname$)-counter)
			End If
			;currentgame$ = savename$
			;			
			simplemessage("Game saved")
		End If
		wacht(500)
	End If
	
	If Chr(currentkey) = "l" Then 
		loadname$ = gfilerequester(100,100,CurrentDir$(),currentgame,".sav")
		If Not loadname$ = 0 Then
			If Right(loadname$,4) = ".sav" Then
				If FileType(loadname$) = 1 Then
					loadgame(loadname$)
					; Remove the directory from the filename
					counter = 0
					If Len(loadname$) > 1 Then
			  			For i=Len(loadname$) To 1 Step -1
						If Mid(loadname$,i,1) = "/" Or Mid(loadname$,i,1) = "\" Then Exit					
						counter = counter + 1
						Next
						counter = counter - 1
						currentgame$ = Mid(loadname$,Len(loadname$)-counter,Len(loadname$)-counter)
					End If
					;currentgame$ = loadname$
					drawminimap
					maketempmap
					simplemessage("Game loaded")
				End If
			End If
		End If
		drawminimapterrain()
		drawminimap()
		wacht(500)
	End If
	ChangeDir("..")
End Function

;
; This function is used for some experimenting with a* routine
;
;
Function createunitpath(player,destx,desty,sourcex,sourcey)
map_width=100
map_height=100
MP_MAX_NODES = map_width*map_height
MP_MAX_WIDTH = map_width
MP_MAX_HEIGHT = map_height
;Dim pathmap(map_width,map_height)
Dim MP_Map.PQ_Node(MP_MAX_WIDTH,MP_MAX_HEIGHT)


; First we convert the destination and start location
startx = sourcex;units(7,1)
starty = sourcey;units(7,2)
pathmap(startx,starty)=1
destx = destx ; cursor(0)
desty = desty ; cursor(1)


; Find info on the path (can we get there by roads)
routetoroadavailable = False
fillpathmap(9)
fillpathwater()
fillpathroads(1)
fillpathcities(player,9)
findpath(startx,starty,destx,desty,0)
If as_outsize > 0 Then routetoroadavailable = True : as_outsize = 0 : DebugLog "routetoroadavailable"

If routetoroadavailable = False Then
simplemessage("Point not reachable by road")
Return
End If

fillpathmap(9)
;fillpathterrain(8)
;fillpathwater()
fillpathroads(1)
fillpathcities(player,9)
findpath(startx,starty,destx,desty,0)
If as_outsize >0 Then DebugLog "path found"


	
		;DebugLog as_outsize
		
;		If as_outsize = 1 Then minusman = 1 Else minusman = 2

;
; Place the path into the ai's path finding arrays
;
;Dim pathdestination(maxunits,2) ; 0=x,1=y,2=pointer to which buffer
;Dim pathbuffer(250,1000,2) ; 250 containers of 1000 sets of 2 coordinates
;dim pathbufferactive(250) ; is this one being used
		If as_outsize>0 Then		
			If as_outsize=<32 Then
				;a = findshortpatharray()
				freeslot = False
				;
				For i=1 To numpaths
					If pathbufferactive(i) = False Then freeslot = i : Exit
				Next
				If freeslot = False Then
					simplemessage("Programmer add more paths")
					Return
				End If
				
				; If we have free space in the array			
				If freeslot > 0 Then
					For i=0 To as_outsize
		;				roadmap(pathresult(i,0),pathresult(i,1)) = 1
			;			updateroadmap(pathresult(i,0),pathresult(i,1)) 
			;			DebugLog pathresult(i,0) + " " + pathresult(i,1)
						pathbuffer(freeslot,i,0) = pathresult(i,0)
						pathbuffer(freeslot,i,1) = pathresult(i,1)
		;				shortpathmap(a,i,0) = pathresult(i,0)
		;				shortpathmap(a,i,1) = pathresult(i,1)
					Next
					pathbuffer(freeslot,i-1,0) = -1
					pathbufferactive(freeslot) = True
					pathbufferpointer(freeslot) = 0
					;shortpathmap(a,i-1,0) = -1
					;shortpathmapactive(a) = True
					Return freeslot
				End If
			End If
		End If
		
	
End Function



;
; This function is used for some experimenting with a* routine
;
;
Function createpath(player,destx,desty,sourcex,sourcey)
		map_width=100
		map_height=100
		MP_MAX_NODES = map_width*map_height
		MP_MAX_WIDTH = map_width
		MP_MAX_HEIGHT = map_height
		;Dim pathmap(map_width,map_height)
		Dim MP_Map.PQ_Node(MP_MAX_WIDTH,MP_MAX_HEIGHT)
		
		;If sourcex = -1 Then Return False
		;If sourcey = -1 Then Return False
		;If destx = -1 Then Return False
		;If desty = -1 Then Return False
		;If player >8 Or player <1 Then Return False
		;If destx=0 And desty=0 Then Return False
		;If sourcex = 0 And sourcey=0 Then Return False
		
		;
		; This was added to fix a bug, this only sends missions with presence on the map
		
;		citypresent = False
;		unitpresent = False
;		fortresspresent = False
;		For i=0 To maxcities
;			If cities(i,0) = True Then
;				If cities(i,1) = destx And cities(i,2) = desty Then citypresent = True
;			End If
;		Next
;		For i=0 To maxunits
;			If units(i,0) = True Then
;				If units(i,1) = destx And units(i,2) = desty Then unitpresent = True
;			End If
;		Next
;		If fortressmap(destx,desty) = True Then fortresspresent = True
		
;		If citypresent = False And unitpresent = False And fortresspresent = False Then
;		Return False
;		End If
		
;		If cursor(0)=>0 And cursor(1)=>0 And cursor(0)=<100 And cursor(1)=<100 Then
	
	;If player = 4 Then DebugLog ("player4dest : "+destx + " player4desty : "+desty)
	
	;
	; Fill in map data into the pathmap
	;
;	For x=0 To 100
;	For y=0 To 100
;	Select map(x,y)
;	Case 1 ; grass
;	pathmap(x,y) = 4
;	Case 2 ; hills
;	pathmap(x,y) = 8 
;	Case 3 ; mountain
;	pathmap(x,y) = 8
;	Case 4 ; pines
;	pathmap(x,y) = 8
;	Case 5 ; swamp
;	pathmap(x,y) = 8
;	Case 6 ; trees
;	pathmap(x,y) = 8
;	End Select
;	Next
;	Next




;	For x=0 To 100
;	For y=0 To 100
;	Select roadmap(x,y)
;	Case 1
;	pathmap(x,y) = 1
;	End Select
;	Next
;	Next

;	For i=0 To maxunits
;	If units(i,0) = True Then
;	If units(i,11) = currentplayer Then
;	If units(i,1)>0 And units(i,2) > 0 And units(i,1)<100 And units(i,2) < 100 Then
;	pathmap(units(i,1),units(i,2)) = 8
;	End If
;	End If
;	End If
;	Next

		; First we convert the destination and start location
		startx = sourcex;units(7,1)
		starty = sourcey;units(7,2)
		pathmap(startx,starty)=1
		destx = destx ; cursor(0)
		desty = desty ; cursor(1)


; Find info on the path (can we get there by roads)
routetoroadavailable = False
fillpathmap(9)
fillpathwater()
fillpathroads(1)
fillpathcities(player,9)
If startx = 0 And starty = 0 Then simplemessage("start wrong") : Delay(2000) 
If destx = 0 And desty = 0 Then simplemessage("End wrong") : Delay(2000) 

findpath(startx,starty,destx,desty,0)
If as_outsize > 0 Then routetoroadavailable = True : as_outsize = 0 : DebugLog "routetoroadavailable"

; Decide the method of attack	
assault = False
If directattacktactic(player) = 1 Then assault = True


		
If as_outsize = 0 And assault = False Then
	; See if we can get there thru roads
	fillpathmap(9)
	fillpathwater()
	fillpathroads(1)
	fillpathunits(9)
	fillpathcities(player,9)
	findpath(startx,starty,destx,desty,0)
	If as_outsize > 0 Then DebugLog "Strategy #1"
End If

; If not then check if we can get there by avoiding obstacles and enemy units
If as_outsize = 0 And assault = False Then
	fillpathmap(1)
	fillpathwater()
	fillpathterrain(9)
	fillpathroads(1)
	fillpathunits(9)
	fillpathcities(player,9)
	findpath(startx,starty,destx,desty,0)
	If as_outsize >0 Then DebugLog "strategy #2"
End If

; If not then go thru obstacles while avaoiding enemy units
If as_outsize = 0 And assault = False Then
	fillpathmap(1)
	fillpathwater()
	fillpathterrain(8)
	fillpathroads(1)
	fillpathunits(9)
	fillpathcities(player,9)
	fillpathrect(destx-1,desty-1,3,3,1)
	findpath(startx,starty,destx,desty,0)
	If as_outsize >0 Then DebugLog "Strategy #3"
End If

; If not then then assault the lines
If assault = True Then
	If routetoroadavailable = True Then
		fillpathmap(9)
		;fillpathterrain(8)
		fillpathroads(1)
		fillpathwater()
		fillpathcities(player,9)
		findpath(startx,starty,destx,desty,0)
		If as_outsize >0 Then DebugLog "Strategy #4"
		;simplemessage("Attack via road - reported")
	Else
		fillpathmap(1)
		fillpathwater()
		fillpathterrain(8)
		fillpathroads(1)
		fillpathunits(8)
		fillpathcities(player,9)
		findpath(startx,starty,destx,desty,0)
		If as_outsize >0 Then DebugLog "Strategy #5"
	End If
End If

	
		;DebugLog as_outsize
		
;		If as_outsize = 1 Then minusman = 1 Else minusman = 2

;
; Place the path into the ai's path finding arrays
;

		If as_outsize>0 Then
		
			If as_outsize=<32 Then
			a = findshortpatharray()
			If Not a=-1 Then
			For i=0 To as_outsize-1
;			If pathresult(i,0) = 0 And pathresult(i,1) = 0 Then simplemessage("shit1" + i + " pathsize : " + (as_outsize-1)) : Delay(500)
;			roadmap(pathresult(i,0),pathresult(i,1)) = 1
;			updateroadmap(pathresult(i,0),pathresult(i,1)) 
;			DebugLog pathresult(i,0) + " " + pathresult(i,1)
			shortpathmap(a,i,0) = pathresult(i,0)
			shortpathmap(a,i,1) = pathresult(i,1)
			Next
			shortpathmap(a,i,0) = -1
			shortpathmapactive(a) = True
			Return 1000+a
			End If
			End If
;		
			If as_outsize>32 And as_outsize<100 Then
			a = findmediumpatharray()
			If Not a=-1 Then
			For i=0 To as_outsize-1
;			If pathresult(i,0) = 0 And pathresult(i,1) = 0 Then simplemessage("shit1" + i + " pathsize : " + (as_outsize-1)) : Delay(500)			
;			roadmap(pathresult(i,0),pathresult(i,1)) = 1
;			updateroadmap(pathresult(i,0),pathresult(i,1)) 
			mediumpathmap(a,i,0) = pathresult(i,0)
			mediumpathmap(a,i,1) = pathresult(i,1)
			Next
			mediumpathmap(a,i,0) = -1
			mediumpathmapactive(a) = True
			Return 2000+a
			End If
			End If
;		
			If as_outsize=>100 And as_outsize< 1000 Then
			a = findlongpatharray()
			If Not a=-1 Then
			For i=0 To as_outsize-1
;			If pathresult(i,0) = 0 And pathresult(i,1) = 0 Then simplemessage("shit1" + i + " pathsize : " + (as_outsize-1) ) : Delay(500)
;			roadmap(pathresult(i,0),pathresult(i,1)) = 1
;			updateroadmap(pathresult(i,0),pathresult(i,1)) 
			longpathmap(a,i,0) = pathresult(i,0)
			longpathmap(a,i,1) = pathresult(i,1)
			Next
			longpathmap(a,i,0) = -1
			longpathmapactive(a) = True
			Return 3000+a
			End If
			End If
		
			;For i=0 To as_outsize
			;If pathresult(i,0) = 0 And pathresult(i,1) = 0 Then 
			;DebugLog "as_outsize : " + as_outsize
			;DebugLog "i : "+i
			;End
			;End If
			;Next
			;Next
			;For i=0 To as_outsize
			;If pathresult(i,0) = 0 And pathresult(i,1) = 0 Then
			;End
			;End If
			;Next
		
		
			Else ; If the outsize is zero
			Return False		

			End If
;			maketempmap:drawmap :wacht(200)
		
		
End Function



; This function fills all water on the path map
Function fillpathwater()
For x=0 To 100
For y=0 To 100
If map(x,y) = 64 Then pathmap(x,y) = 9
Next
Next
End Function

; this fill in a box on the path map
Function fillpathrect(x,y,w,h,number)
	For x1=x-1 To x+1
		For y1=y-1 To y+1
			If x1>0 And x1<mapwidht And y1>0 And y1<mapheight Then
				pathmap(x1,y1)=number
			End If
		Next
	Next
End Function

; this function creates the cities in the path map
Function fillpathcities(player,dacost)
For i=0 To maxcities
	If cities(i,0) = True Then
		For ii=1 To 8
			If warstate(player,ii) = False Then pathmap(cities(i,1),cities(i,2)) = 9
			If warstate(player,ii) = True Then pathmap(cities(i,1),cities(i,2)) = 1
		Next
		If cities(i,4) = player Then
			pathmap(cities(i,1),cities(i,2)) = 1
		End If
	End If
Next
End Function

Function fillpathterrain(number)
	For x=0 To 100
	For y=0 To 100
	Select map(x,y)
	Case 1 ; grass
	pathmap(x,y) = 4
	Case 2 ; hills
	pathmap(x,y) = number
	Case 3 ; mountain
	pathmap(x,y) = number
	Case 4 ; pines
	pathmap(x,y) = number
	Case 5 ; swamp
	pathmap(x,y) = number
	Case 6 ; trees
	pathmap(x,y) = number
	Case 7 ; mine
	pathmap(x,y) = number
	Case 8 ; crops
	pathmap(x,y) = number
	Case 9 ; beaver
	pathmap(x,y) = number
	End Select
	Next
	Next
End Function

Function fillpathmap(number)
For x=0 To 100
	For y=0 To 100
		pathmap(x,y)=number
	Next
Next
End Function

Function fillpathroads(number)
	For x=0 To 100
		For y=0 To 100
			Select roadmap(x,y)
			Case 1
				pathmap(x,y) = number
		End Select
		Next
	Next
End Function

;
; Fill in the units in the pathmap
;
Function fillpathunits(number)
For i=0 To maxunits
	If units(i,0) = True Then
		If units(i,11) = currentplayer Then
			If units(i,1)>0 And units(i,2) > 0 And units(i,1)<100 And units(i,2) < 100 Then
				entreevalue = False
				For ii=0 To maxcities
					If cities(ii,0) = True Then
						If cities(ii,1) = units(i,1) And cities(ii,2) = units(i,2) Then
							entreevalue = True
						End If
					End If
				Next
				If entreevalue = False Then
					For x=units(i,1)-1 To units(i,1)+1
						For y=units(i,2)-1 To units(i,2)+1
							If x>0 And x<mapwidth And y>0 And y<mapheight Then
								pathmap(x,y) = number
							End If
						Next
					Next
				End If
			End If
		End If
	End If
Next
End Function

;
; These functions return if the path map is empty/inactive
;
;
Function findshortpatharray()
For i=0 To 250
	If shortpathmapactive(i) = False Then Return i
Next
Return -1
End Function

Function findmediumpatharray()
For i=0 To 100
	If mediumpathmapactive(i) = False Then Return i
Next
Return -1
End Function

Function findlongpatharray()
For i=0 To 50
	If longpathmapactive(i) = False Then Return i
Next
Return -1
End Function

;
; This function is used for some experimenting with a* routine
;
;
Function findpathtest()
		map_width=100
		map_height=100
		MP_MAX_NODES = map_width*map_height
		MP_MAX_WIDTH = map_width
		MP_MAX_HEIGHT = map_height
		Dim pathmap(map_width,map_height)
		Dim MP_Map.PQ_Node(MP_MAX_WIDTH,MP_MAX_HEIGHT)
		
		
		If cursor(0)=>0 And cursor(1)=>0 And cursor(0)=<100 And cursor(1)=<100 Then
	
	
	For x=0 To 100
	For y=0 To 100
	Select map(x,y)
	Case 1 ; grass
	pathmap(x,y) = 5
	Case 2 ; hills
	pathmap(x,y) = 7 
	Case 3 ; mountain
	pathmap(x,y) = 8
	Case 4 ; pines
	pathmap(x,y) = 7
	Case 5 ; swamp
	pathmap(x,y) = 7
	Case 6 ; trees
	pathmap(x,y) = 7
	End Select
	Select roadmap(x,y)
	Case 1
	pathmap(x,y) = 2
	End Select
	Next
	Next
	For i=0 To maxunits
	If units(i,0) = True Then
	If Not units(i,11) = 1 Then
	pathmap(units(i,1),units(i,2)) = 8
	End If
	End If
	Next
		
		; First we convert the destination and start location
		startx = 13;units(7,1)
		starty = 13;units(7,2)
		pathmap(startx,starty)=1
		destx = cursor(0)
		desty = cursor(1)
	
		findpath(startx,starty,destx,desty,0)
	
		;DebugLog as_outsize
		
		If as_outsize = 1 Then minusman = 1 Else minusman = 2
		
		If as_outsize>0 Then
		For i=0 To as_outsize
;		roadmap(pathresult(i,0),pathresult(i,1)) = 1
;		updateroadmap(pathresult(i,0),pathresult(i,1))

		Next
		;If diffx=>0 Then ; if the difference is positive
		;units(7,1) = pathresult(as_outsize-minusman,0)-Abs(diffx)
		;Else ; if the difference is negative
		;units(7,1) = pathresult(as_outsize-minusman,0)+Abs(diffx)
		;End If
		;If diffy=>0 Then ; if the difference is positive
		;units(7,2) = pathresult(as_outsize-minusman,1)-Abs(diffy)
		;Else ; if the difference is negative
		;units(7,2) = pathresult(as_outsize-minusman,1)+Abs(diffy)
	;	End If
		
		End If
			maketempmap:drawmap :wacht(200)
		
	End If
End Function

;
; Draws the result path on the screen - currently not used in the game
;
;
;
Function drawpathfindingpath()
For i=0 To as_outsize-1
	If i=0 Then Color 255,255,255 Else Color 255,0,0
	If i=as_outsize-1 Then Color 255,255,0 Else Color 255,0,0
	Rect (pathresult(i,0)*5)+700,(pathresult(i,1)*5)+300,5,5,1
Next

Color 255,155,155
For y=0 To 20
	For x=0 To 20
		If pathmap(x,y) >3 Then
			Color 255,155,155
			Rect (x*5)+700,(y*5)+300,5,5,1
			If x=10 And y=10 Then Color 255,255,0 : Rect (x*5)+700,(y*5)+300,5,5,1
		End If
		If pathmap(x,y) = 1 ; road
			Color 0,0,255
			Rect (x*5)+700,(y*5)+300,5,5,1
		End If
	Next
Next
End Function

;
; Useless function but I will keep it until I clean up the code
;
; 
Function addleveldata()
cities(7,0) = True ; Active true or false
cities(7,1) = 4 ; City x location
cities(7,2) = 1; City y location
citiesstring$(7,0) = "Townsend" ; city name
cities(7,3) = 1 ; City type/level
cities(7,4) = 3 ; Belongs to player x
cities(7,5) = 12; tax income

cities(8,0) = True ; Active true or false
cities(8,1) = 1 ; City x location
cities(8,2) = 2 ; City y location
citiesstring$(8,0) = "Broken bow" ; city name
cities(8,3) = 1 ; City type/level
cities(8,4) = 3 ; Belongs to player x
cities(8,5) = 12; tax income

cities(9,0) = True ; Active true or false
cities(9,1) = 4 ; City x location
cities(9,2) = 22; City y location
citiesstring$(9,0) = "Rocksquare" ; city name
cities(9,3) = 1 ; City type/level
cities(9,4) = 3 ; Belongs to player x
cities(9,5) = 12; tax income

End Function

; ----------------------------------------------
;
; This function gives a game message
;
; ----------------------------------------------

Function gamemessage(m)
gwindow(640/2-150,600/2-75,300,150,0,0,1)
Color 0,0,0
Select m
	Case m
	Text 640/2,600/2-40,"VICTORY!!",1,1
	Text 640/2,600/2,"You have defeated the enemy",1,1
	Text 640/2,600/2+40,"<Press any key>",1,1
	Flip
End Select
Delay(150)
FlushKeys()
WaitKey
Delay(250)
FlushKeys()
End Function

;
; This function plays the games sound effects
;
;
Function playsfx(n)

	If set_sfxenabled = False Then Return
	
	; If the sfx are allowed
	If ingamesfx = True Then
		; Select the sfx
		Select n
			Case 0
				PlaySound(victorysound)
			Case 1
				PlaySound(battlesound)
			Case 2
				PlaySound(boosound)
			Case 3
				PlaySound(clicksound)
			Case 4
				PlaySound(closewindowsound)
			Case 5
				PlaySound(createunitsound)
			Case 6
				PlaySound(moveunitsound)
			Case 7
				PlaySound(coinssound)
		End Select
	End If
End Function

;
; Plays the music by its given number, edit this for your own tunes
;
;
Function PlaydaMusic(n)

If set_musicenabled = False Then return

If ingamemusic = True Then
	StopChannel(mainchannel)
	Select n
		Case 0
			mainchannel = PlayMusic("music/ingame01.mod")
		Case 1
			mainchannel = PlayMusic("music/ingame02.mod")
		Case 2
			mainchannel = PlayMusic("music/ingame03.mod")
		Case 3
			mainchannel = PlayMusic("music/ingame01.mod")
			;mainchannel = PlayMusic("music/ingame04.xm")
		Case 4
			mainchannel = PlayMusic("music/ingame02.mod")
			;mainchannel = PlayMusic("music/ingame05.xm")
		Case 5
			mainchannel = PlayMusic("music/ingame03.mod")
			;mainchannel = PlayMusic("music/ingame06.xm")
		Case 6
			mainchannel = PlayMusic("music/tribe1.mod")
		Case 7
			mainchannel = PlayMusic("music/tribe2.mod")
		Case 8
			mainchannel = PlayMusic("music/tribe3.mod")
		Case 9
			mainchannel = PlayMusic("music/tribe4.mod")
		Case 10
			mainchannel = PlayMusic("music/tribe5.mod")
		Case 11
			mainchannel = PlayMusic("music/tribe6.mod")
		Case 12
			mainchannel = PlayMusic("music/tribe7.mod")
		Case 13
			mainchannel = PlayMusic("music/tribe8.mod")
		Case 14
			mainchannel = PlayMusic("music/title.mod")

			
	End Select
End If
End Function


;
; Basically a playlist
;
;
Function musicplayingsystem()
If MilliSecs() > musicplayingtimer Then
	musicplaying = musicplaying + 1
	If musicplaying > 5 Then musicplaying = 0
	playdamusic(musicplaying)
	musicplayingtimer = MilliSecs() + musicplayingdelay
End If
End Function


;
; Flickering for the next turn text
;
;
Function endoftimesystem()
If MilliSecs() > endoftimetimer Then
	If endoftime = 1 Then endoftime = 0 Else endoftime=1
	endoftimetimer = MilliSecs() + endoftimedelay
End If
End Function



.tooltipfunctions
;
; These are the tooltips for then main loop
;
Function mainlooptooltips()
If RectsOverlap(MouseX(),MouseY(),1,1,800-160,0,160,160) Then
enabletooltip(0)
End If
If RectsOverlap(MouseX(),MouseY(),1,1,800-160+50,180,60,20) Then
enabletooltip(1)	
End If
If RectsOverlap(MouseX(),MouseY(),1,1,800-120,230,100,20) Then
enabletooltip(2)	
End If
If RectsOverlap(MouseX(),MouseY(),1,1,screenwidth-70,551,60,20) Then
enabletooltip(3)	
End If
End Function


Function enabletooltip(nr)	
	If tooltipdelay(nr) < 2 And mistertime(set_tooltipdelay/2,0) Then
		tooltipdelay(nr) = tooltipdelay(nr) + 1		
		ElseIf  tooltipdelay(nr)=2
		showtooltip(nr) ; minimap
	End If
End Function

Function freetooltipdelays()
For i=0 To numtooltips
	tooltipdelay(i) = False
Next
End Function
;
; Tooltips function
;
Function showtooltip(nr)
; If the tooltips are enabled
If tooltipsenabled = False Then Return
	; Move the mouse coordinates into two variables
 	x = MouseX()-160
	y = MouseY()-40
	;
	freetooltipdelays()
	tooltipdelay(nr) = 2
	;
 	Select nr
		Case 0
		  	sttdisplaytxt$ = "Minimap the units and cities on the map"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 1
			sttdisplaytxt$ = "Main game menu"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 2
			sttdisplaytxt$ = "Displays the treasury"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 3
			sttdisplaytxt$ = "Go on to the next turn"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 4
			sttdisplaytxt$ = "Order unit to wait"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 5
			sttdisplaytxt$ = "Order unit to Sleep"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 6
			sttdisplaytxt$ = "Order unit to fortify"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 7
			sttdisplaytxt$ = "Order unit to build a road"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 8
			sttdisplaytxt$ = "Disband the unit"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 9
			sttdisplaytxt$ = "Destroy square improvment"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 10
			sttdisplaytxt$ = "No orders until the next turn"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 11
			sttdisplaytxt$ = "500 for a city upgrade"
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
  End Select

	; Tribes description
	Select nr
		
		Case 101
			For i=0 To 10
				sttdisplaytxt$ = sttdisplaytxt$ + tribesdescriptions(1,i) + " "
			Next
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 102
			For i=0 To 10
				sttdisplaytxt$ = sttdisplaytxt$ + tribesdescriptions(2,i) + " "
			Next
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 103
	
			For i=0 To 10
				sttdisplaytxt$ = sttdisplaytxt$ + tribesdescriptions(3,i) + " "
			Next
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 104
			For i=0 To 10
				sttdisplaytxt$ = sttdisplaytxt$ + tribesdescriptions(4,i) + " "
			Next
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
	
		Case 105
			For i=0 To 10
				sttdisplaytxt$ = sttdisplaytxt$ + tribesdescriptions(5,i) + " "
			Next
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 106
			For i=0 To 10
				sttdisplaytxt$ = sttdisplaytxt$ + tribesdescriptions(6,i) + " "
			Next
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 107
			For i=0 To 10
				sttdisplaytxt$ = sttdisplaytxt$ + tribesdescriptions(7,i) + " "
			Next
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
		Case 108
			For i=0 To 10
				sttdisplaytxt$ = sttdisplaytxt$ + tribesdescriptions(8,i) + " "
			Next
			tooltipdisplay(sttdisplaytxt$,MouseX()-160,MouseY()-40,140)
	End Select

End Function

;
; Function for drawing the box underneath the tooltip text
;
Function tooltipbox(x,y,w,h)
Color 0,0,0
Rect x,y,140,h,1
Color 255,255,255
Rect x,y,140,h,0
End Function

;
; Tooltipdisplay
;
Function tooltipdisplay(ttdtxt$,x,y,w)
w=w-10
SetFont grequesterfont
Color 255,255,255
w2 = 0
y1 = y
t$=""
For i=1 To Len(ttdtxt$)
  endword = findstring(ttdtxt$,i," ")
  t$ = Mid(ttdtxt$,i,endword-i)
  i = endword
  If w2+StringWidth(t$) > w Then w2 = 0 : y=y+StringHeight(t$)
  ;Text x+w2+6,y,t$
  w2=w2+StringWidth(t$+" ")
Next

tooltipbox(x,y1,w,(y-y1)+20)

w2 = 0
y =y1
t$=""
For i=1 To Len(ttdtxt$)
  endword = findstring(ttdtxt$,i," ")
  t$ = Mid(ttdtxt$,i,endword-i)
  i = endword
  If w2+StringWidth(t$) > w Then w2 = 0 : y=y+StringHeight(t$)
  Text x+w2+6,y,t$
  w2=w2+StringWidth(t$+" ")
Next


End Function

;
; This function returns the next string occurence
;
Function findstring$(fsstring$,fsstart,fsfind$)
	For i=fsstart To Len(fsstring$)
		If Mid(fsstring$,i,1) = fsfind$  Then Return i
	Next
Return i
End Function 


;
;
; This function is a info screen that shows info on your kingdom
;
Function Showinfoscreen()

; Get the player income (slow routine!!)
Local p1income = getplayerincome(1)
Local p2income = getplayerincome(2)
Local p3income = getplayerincome(3)
Local p4income = getplayerincome(4)
Local p5income = getplayerincome(5)
Local p6income = getplayerincome(6)
Local p7income = getplayerincome(7)
Local p8income = getplayerincome(8)

While KeyHit(1) = False
	Cls
	; First draw the background
	DrawImage mapbuffer,0,0
	; Draw the game screen
	drawgamescreen
	
	SetFont gsmallfont
	
	x=40
	y=20
	
	; Draw the window
	gwindow(20,70,600,280,1,0,0)
	gwindow(20+x-2,80+y-2,560-x+4,200-y+4,0,1,1)
	gwindow(20+x-2,320-y-2,560-x+4,45,0,1,1)
	
	; Set the pen color
	Color 255,255,255
	
	SetFont grequesterfont
	Text 640/2,60+y,"Screnario info",1,1 
	Text 40+x,80+y,"Player"
	Text 40+x,100+y,"1"
	Text 40+x,120+y,"2"
	Text 40+x,140+y,"3"
	Text 40+x,160+y,"4"
	Text 40+x,180+y,"5"
	Text 40+x,200+y,"6"
	Text 40+x,220+y,"7"
	Text 40+x,240+y,"8"
	
	drawplayercolor(70+x,102+y,1)
	drawplayercolor(70+x,122+y,2)
	drawplayercolor(70+x,142+y,3)
	drawplayercolor(70+x,162+y,4)
	drawplayercolor(70+x,182+y,5)
	drawplayercolor(70+x,202+y,6)
	drawplayercolor(70+x,222+y,7)
	drawplayercolor(70+x,242+y,8)
	
	Color 255,255,255
	
	Text 120+x,80+y,"Units"
	Text 120+x,100+y,getplayerunits(1)
	Text 120+x,120+y,getplayerunits(2)
	Text 120+x,140+y,getplayerunits(3)
	Text 120+x,160+y,getplayerunits(4)
	Text 120+x,180+y,getplayerunits(5)
	Text 120+x,200+y,getplayerunits(6)
	Text 120+x,220+y,getplayerunits(7)
	Text 120+x,240+y,getplayerunits(8)
	
	Text 200+x,80+y,"Gold"
	Text 200+x,100+y,playergold(1)
	Text 200+x,120+y,playergold(2)
	Text 200+x,140+y,playergold(3)
	Text 200+x,160+y,playergold(4)
	Text 200+x,180+y,playergold(5)
	Text 200+x,200+y,playergold(6)
	Text 200+x,220+y,playergold(7)
	Text 200+x,240+y,playergold(8)
	
	Text 280+x,80+y,"Cities"
	Text 280+x,100+y,getplayercities(1)
	Text 280+x,120+y,getplayercities(2)
	Text 280+x,140+y,getplayercities(3)
	Text 280+x,160+y,getplayercities(4)
	Text 280+x,180+y,getplayercities(5)
	Text 280+x,200+y,getplayercities(6)
	Text 280+x,220+y,getplayercities(7)
	Text 280+x,240+y,getplayercities(8)
	
	Text 360+x,80+y,"Income"
	Text 360+x,100+y,p1income
	Text 360+x,120+y,p2income
	Text 360+x,140+y,p3income
	Text 360+x,160+y,p4income
	Text 360+x,180+y,p5income
	Text 360+x,200+y,p6income
	Text 360+x,220+y,p7income
	Text 360+x,240+y,p8income
	
	Text 440+x,80+y,"Relation
	Text 440+x,100+y,""
	Text 440+x,120+y,translatewarstate(2,currentplayer)
	Text 440+x,140+y,translatewarstate(3,currentplayer)
	Text 440+x,160+y,translatewarstate(4,currentplayer)
	Text 440+x,180+y,translatewarstate(5,currentplayer)
	Text 440+x,200+y,translatewarstate(6,currentplayer)
	Text 440+x,220+y,translatewarstate(7,currentplayer)
	Text 440+x,240+y,translatewarstate(8,currentplayer)
	
	Text 640/2,320,"(Press escape to close this window)",1,1
	
	DrawImage mousepointer,MouseX(),MouseY()
	Flip
Wend
.skipout3
Delay(500) : FlushKeys()

End Function


Function translatewarstate$(x,y)
If warstate(x,y) = True Then Return "At War" Else Return "At Peace"
End Function

;
; This function returns the money a player has
;
; 
Function getplayerincome(player)
;
; Setup a local value to calculate the income with
;
Local incomefigure = 0
;
; Get the income from the players cities
;
For i=0 To maxcities
	If cities(i,0) = True Then
		If cities(i,4) = player Then
			incomefigure = incomefigure + cities(i,5)
		End If
	End If
Next
;
; Get the money from the mines
;
For x=0 To 100
	For y=0 To 100
		If minemap(x,y,mine_alignment) = player Then
			incomefigure = incomefigure + minemap(x,y,mine_income)
		End If
	Next
Next
;
Return incomefigure
;
End Function

;
; This funtion return the number of units for the selected
;
Function getplayerunits(player)
numberofunits = 0
For i=0 To maxunits
	If units(i,0) = True Then
		If units(i,11) = player Then
			numberofunits = numberofunits + 1
		End If
	End If
Next
Return numberofunits
End Function

;
; This function return the number of cities for the player
;
Function getplayercities(player)
numberofcities = 0
For i=0 To maxcities
	If cities(i,0) = True Then
		If cities(i,4) = player Then
			numberofcities = numberofcities + 1
		End If
	End If
Next
Return numberofcities
End Function

;
; This function sets the war state
;
Function setwarstate(enemy)
warstate(enemy,currentplayer) = True
warstate(currentplayer,enemy) = True
End Function

;
; This function draws the playercolor as a small oval
;
Function drawplayercolor(x,y,player)
Color playercolor(player,0),playercolor(player,1),playercolor(player,2)
Oval x,y,14,14,1
End Function

;
; This function deletes all units at the given tile
;
Function deleteallunitsattile(player,x,y)
For i=0 To maxunits
	If units(i,0) = True Then
		If units(i,1) = x And units(i,2) = y Then
			If Not units(i,11) = currentplayer Then
				units(i,0) = False
			End If
		End If
	End If
Next
End Function

;
; This function creates visible area's around units
;
Function createbasevisibleareamap(player)
For i=0 To maxunits
	If units(i,0) = True Then
		If units(i,unit_alignment) = player Then
			If units(i,unit_ontop) = True Then
				;
				x = units(i,1):y = units(i,2)
				;
				If map(x,y) = 2 Then
					For x1=x-2 To x+2
						For y1=y-2 To y+2
							If x1=>0 And y1=<100 And x1=<100 And y1=>0 Then
								vismap(x1,y1,player) = True
							End If
						Next
					Next
					Else
					For x1=x-1 To x+1
						For y1=y-1 To y+1
							If x1=>0 And y1=<100 And x1=<100 And y1=>0 Then
								vismap(x1,y1,player) = True
							End If
						Next
					Next
				End If
				;
			End If
		End If
	End If
Next
;
; Update the vismap area around the cities
;
For i=0 To maxcities
	If cities(i,0) = True Then
		If cities(i,city_alignment) = player Then
		;
		x = cities(i,1) : y = cities(i,2)
		;
		For x1=x-2 To x+2
			For y1=y-2 To y+2
				If x1=>0 And y1=<100 And x1=<100 And y1=>0 Then
					vismap(x1,y1,player) = True
				End If
			Next
		Next
		;
		End If
	End If
Next

maketempmap:drawmap:drawminimap
End Function



;
; Update map around unit
;
Function updatevismap(player,x,y)
If map(x,y) = 2 Then 
	For x1=x-2 To x+2
		For y1=y-2 To y+2
			If x1=>0 And x1=<100 And y1=>0 And y1=<100 Then
				vismap(x1,y1,player) = True
			End If
		Next
	Next
	Else
	For x1=x-1 To x+1
		For y1=y-1 To y+1
			If x1=>0 And x1=<100 And y1=>0 And y1=<100 Then
				vismap(x1,y1,player) = True
			End If
		Next
	Next
End If
End Function


;
; Bugfix (mainly for the ai movement in ai1000 in isoai.bb
;
;
; This loops thru all units and checks if he is ontop. If he is not ontop then the routine
; checks the rest of the units if they are at the x and y position and return true if there
; is a unit there that is ontop. In fixontopuinits if this function returns false then the
; unit checked with is placed ontop.
;
;
;
Function fixontopunits()
For i=0 To maxunits
	If units(i,0) = True Then
		If isthereaontopunit(units(i,1),units(i,2)) = False Then
			units(i,3) = True
		End If
	End If
Next
End Function

;
; Sub function for the fixontopunits
;
Function isthereaontopunit(x,y)
For i=0 To maxunits
	If units(i,0) = True Then
		If units(i,1) = x And units(i,2) = y Then 
			If units(i,3) = True Then Return True
		End If
	End If
Next
End Function

;
; This function shows the buildlist for the enemy cities
;
Function showcitiesbuildlist()

Local scbcurrentbutton
Local currentplayerinfo = currentplayer

Local column1left = 640/10
Local column2left = (640/10)*2
Local column3left = (640/10)*3
Local column4left = (640/10)*4
Local column5left = (640/10)*5
Local column6left = (640/10)*6
Local column7left = (640/10)*7
Local column8left = (640/10)*8

Local column1top = 60


While KeyDown(1) = False And moused(2) = False And KeyDown(57) = False
Cls
; First draw the background
DrawImage mapbuffer,0,0
; Draw the gamescreen
drawgamescreen
; Draw the window
gwindow(0,0,640,600,0,1,0)
gwindow(column1left-4,column1top-4,640-((column1left-4)*2),600-column1top,0,1,1)

;
; Top buttons
;

; Draw/operate the buttons
If gbutton3(column1left,column1top,64,20,"",1,1,scbcurrentbutton) Then scbcurrentbutton = 1
If gbutton3(column2left,column1top,64,20,"",2,2,scbcurrentbutton) Then scbcurrentbutton = 2
If gbutton3(column3left,column1top,64,20,"",3,3,scbcurrentbutton) Then scbcurrentbutton = 3
If gbutton3(column4left,column1top,64,20,"",4,4,scbcurrentbutton) Then scbcurrentbutton = 4
If gbutton3(column5left,column1top,64,20,"",5,5,scbcurrentbutton) Then scbcurrentbutton = 5
If gbutton3(column6left,column1top,64,20,"",6,6,scbcurrentbutton) Then scbcurrentbutton = 6
If gbutton3(column7left,column1top,64,20,"",7,7,scbcurrentbutton) Then scbcurrentbutton = 7
If gbutton3(column8left,column1top,64,20,"",8,8,scbcurrentbutton) Then scbcurrentbutton = 8

; Execute the buttons
If moused(1) = False And scbcurrentbutton>0 Then

	; If pressed the first player button
	If gcheckarea(column1left,column1top,64,20) And scbcurrentbutton=1 Then
		currentplayerinfo = 1
	End If
	; If pressed the first player button
	If gcheckarea(column2left,column1top,64,20) And scbcurrentbutton=2 Then
		currentplayerinfo = 2
	End If
	; If pressed the first player button
	If gcheckarea(column3left,column1top,64,20) And scbcurrentbutton=3 Then
		currentplayerinfo = 3
	End If
	; If pressed the first player button
	If gcheckarea(column4left,column1top,64,20) And scbcurrentbutton=4 Then
		currentplayerinfo = 4
	End If
	; If pressed the first player button
	If gcheckarea(column5left,column1top,64,20) And scbcurrentbutton=5 Then
		currentplayerinfo = 5
	End If
	; If pressed the first player button
	If gcheckarea(column6left,column1top,64,20) And scbcurrentbutton=6 Then
		currentplayerinfo = 6
	End If
	; If pressed the first player button
	If gcheckarea(column7left,column1top,64,20) And scbcurrentbutton=7 Then
		currentplayerinfo = 7
	End If
	; If pressed the first player button
	If gcheckarea(column8left,column1top,64,20) And scbcurrentbutton=8 Then
		currentplayerinfo = 8
	End If

scbcurrentbutton = 0
End If

; Here we draw the units that are sceduled to be build the next turn
;
For i=1 To 8
	; setup
	numbuilds = countbuildingcities(i)
	y = 40
	If i=1 Then x = culumn1left
	If i=2 Then x = culumn2left
	If i=3 Then x = culumn3left
	If i=4 Then x = culumn4left
	If i=5 Then x = culumn5left
	If i=6 Then x = culumn6left
	If i=7 Then x = culumn7left
	If i=8 Then x = culumn8left
	; 
	If numbuilds > 0 Then
		stepy = 400/numbuilds
		For ii=0 To maxcities
			If cities(ii,cities_alignment) = i Then
				If citiesunitsproduction(ii,0) > -1 Then
					DrawImage unitgraphics(citiesunitsproduction(ii,0)-1),x+48+16,y+32
					y=y+stepy
				End If
			End If
		Next		
	End If
Next


DrawImage mousepointer,MouseX(),MouseY()
If screenshotsenabled=True And KeyHit(2) = True Then SaveBuffer(FrontBuffer(),"c:\screenshot.bmp"):End
Flip
Wend
Delay(500) : FlushKeys()
End Function

;
; Support function for showcitiesbuildlist
;
Function countbuildingcities(player)
counter = 0
For i=0 To maxcities
	If cities(i,cities_alignment) = player Then
		If citiesunitsproduction(i,0) > 0 Then counter = counter + 1
	End If
Next
Return counter
End Function


;
; Tutorial Function
;
;
Function tutorial(number)
If tutorialactive = False Then Return
;Const Hint_contact = 1
;Const Hint_DisableTutorial = 2
;Const Hint_Edit = 3
;Const Hint_Overview = 4
;Const Tutorial_Disband = 5
;Const Tutorial_Wait = 6
;Const Tutorial_Pillage = 7
;Const Tutorial_Fortify = 8
;Const Tutorial_Roads = 9
;Const Tutorial_Cities = 10
;Const Tutorial_Units = 11
;Const Tutorial_MainScreen = 12
;Const Tutorial_Minimap = 13
greadtext("tutorial.txt",number)
;gtextviewer(100,100,400,300)
gtutorial(100,100,350,300)

End Function

Function dostackedbattle(unit,x,y)

x1 = units(unit,x)
y1 = units(unit,y)
x2 = x
y2 = y

;For i=0 To maxunits
;If units(i,unit_active) = True Then
;If units(i,unit_x) = x Then
;If units(i,unit_y) = y Then
;If warstate(units(i,unit_alignment),units(unit,unit_alignment)) = True Then

;If dobattle(unit,units(unit,unit_x),units(unit,unit_y))

;End If
;End If
;End If
;End If


;Next

End Function

;
; Origin mainloop
;
;
Function showdebuginfo()
		; Here we draw some debug game info
	
	If showdebugframes = True Then
		drawframerate
		Color 255,0,0
		Text 0,0,"FPS : " + fpscounter
		Text 1,1,"FPS : " + fpscounter
	End If
	
	If showdebuginfo = True Then
		Color 255,0,0
		drawframerate
		Text 0,0,"FPS : " + fpscounter
		Text 1,1,"FPS : " + fpscounter
		Text 0,10,"Mapx location : " + mapx
		Text 1,11,"Mapx location : " + mapx
		Text 0,20,"Mapy location : " + mapy	
		Text 1,21,"Mapy location : " + mapy	
		Text 0,30,"Editmode : " + citiesstring$(7,0)
		Text 1,31,"Editmode : " + citiesstring$(7,0)
		Text 0,40,"Cursor(0) : " + cursor(0) + " Cursor(1) : " + cursor(1)
		Text 1,41,"Cursor(0) : " + cursor(0) + " Cursor(1) : " + cursor(1)
		Text 0,60,"Mousex : " + MouseX() + " Mousey : " + MouseY()
		Text 1,61,"Mousex : " + MouseX() + " Mousey : " + MouseY()
		Text 0,80,"debugstring : " + debugstring$
		Text 1,81,"debugstring : " + debugstring$
	
	
		; This one for debugging purposes
		;drawpathfindingpath()
	
		Text 0,180,"Currentunit(0) " + currentunit(0)
		Text 0,200,"Currentunit(1) " + currentunit(1)
		Text 0,220,"Currentunit(2) " + currentunit(2)
		; Show the keydown
		oldi = i
		For i=0 To 255 
		 If KeyDown(i) = True Then i2 = i
		Next
		Text 0,240,"keydown : " + i2
		i = oldi
		
		If currentunit(2) > -1 Then	Text 0,80,units(currentunit(2),11)
		Text 0,100,cursor(0) + "," + cursor(1)
		Text 1,101,cursor(0) + "," + cursor(1)
		Text 0,120,minimapzones(0,1) + "  " + minimapzones(i,2)
		Text 0,140,minimapzones(0,3) + "  " + minimapzones(i,4)
		Text 0,160,currentplayer
		End If


End Function


;
; Origin mainloop
;
;
Function screenshotfunction()
	If screenshotsenabled=True And KeyHit(2) = True Then SaveBuffer(FrontBuffer(),"c:\screenshot"+CurrentDate()+"-"+CurrentTime()+".bmp")
End Function



;
; Oldmainloop outcommented
;
Function oldpus()
			;If KeyDown(42)=True And KeyDown(46)=True Then
		;	automoveunits(currentplayer)
		;	simplemessage("Units moved")
		;End If


		;
		; Let the player move units automatically
		;Dim pathdestination(maxunits,2) ; 0=x,1=y,2=pointer to which buffer
		;Dim pathbuffer(250,1000,2) ; 250 containers of 1000 sets of 2 coordinates

	;If KeyDown(2) = True Then
	;warstate(1,1) = False
	;warstate(1,2) = True
	;warstate(1,3) = True
	;warstate(1,4) = True
	
	;warstate(2,1) = True
	;warstate(2,2) = False
	;warstate(2,3) = True
	;warstate(2,4) = True
	
	;warstate(3,1) = True
	;warstate(3,2) = True
	;warstate(3,3) = False
	;warstate(3,4) = True
	
	;warstate(4,1) = True
	;warstate(4,2) = True
	;warstate(4,3) = True
	;warstate(4,4) = False
	
	;simplemessage("warstate set")
	;End If
	   ;
	;If KeyDown(2) = True Then
	;For zzi=1 To 8
	;createbasevisibleareamap(zzi)
	;Next
	;For i=0 To maxunits
	;If units(i,1) = 10
	;If units(i,2) = 39
	;For ii=0 To 20
	;DebugLog units(i,ii)
	;Next
	;DebugLog ("  --")
	;End If
	;End If
	;Next
	;End
	;End If
	;For i=1 To 8
	;For ii=1 To 8
	;warstate(i,ii) = False
	;Next
	;Next
	;warstate(currentplayer,2) = False
	;warstate(2,currentplayer) = False
	;gameturn=1
	;End If
	
	; Scrolling system

End Function




;
	; Origin mainloop
	;
Function scrollingsystem2()
	;
	; New scrolling system
	;
	
	If KeyDown(29) = True Or KeyDown(157) = True Then
		If KeyDown(200) = True ; up
			If mapx > -1 Then mapx = mapx - 1 ; left up  ; up
			If mapy > -1 Then mapy = mapy - 1 ; right up ;  
			updatescreen()
			Delay(25)
		End If		
		If KeyDown(203) = True Then ; left
			If mapx > -1 Then mapx = mapx - 1 ; left
			If mapy <  101 Then mapy = mapy + 1 ; left
			updatescreen()
			Delay(25)
		End If		
		If KeyDown(205) = True Then ; right
			If mapx < 101 Then mapx = mapx + 1 ; right
			If mapy > -1 Then mapy = mapy - 1 ; 
			updatescreen()
			Delay(25)
		End If		
		If KeyDown(208) = True Then ; down
			If mapx < 101 Then mapx = mapx + 1 ; left up  ; down
			If mapy < 101 Then mapy = mapy + 1 ; right up ;  
			updatescreen()
			Delay(25)
		End If	
	End If

	;
	; Mouse scrolling with the right mouse button
	;
	a = set_mousescrollingbutton ; settings.txt
	If Moused(a) = True And set_mousescrolling = True Then		
		; Top
		If RectsOverlap(MouseX(),MouseY(),1,1,0,0,640/3,200) = True Then
			If mapx > -1 Then mapx = mapx - 1 ; Left up
			updatescreen()
			Delay(50)
		End If
		If RectsOverlap(MouseX(),MouseY(),1,1,215,0,640/3,200) = True Then
			If mapx > -1 Then mapx = mapx - 1 ; left up  ; up
			If mapy > -1 Then mapy = mapy - 1 ; right up ;  
			updatescreen()
			Delay(50)
		End If
		If RectsOverlap(MouseX(),MouseY(),1,1,400,0,640/3,200) = True Then
			If mapy < 101 Then mapy = mapy - 1 ; right up
			updatescreen()
			Delay(50)
		End If
		
		; Center
		If RectsOverlap(MouseX(),MouseY(),1,1,0,200,640/3,200) = True Then
			If mapx > -1 Then mapx = mapx - 1 ; left
			If mapy <  101 Then mapy = mapy + 1 ; left
			updatescreen()
			Delay(50)
		End If
		;If RectsOverlap(MouseX(),MouseY(),1,1,215,200,640/3,200) = True Then
		;	If mapx > -1 Then mapx = mapx - 1 ; left up  ; up
		;	If mapy > -1 Then mapy = mapy - 1 ; right up ;  
		;	updatescreen()
		;	Delay(50)
		;End If
		If RectsOverlap(MouseX(),MouseY(),1,1,400,200,640/3,200) = True Then
			If mapx < 101 Then mapx = mapx + 1 ; right
			If mapy > -1 Then mapy = mapy - 1 ; 
			updatescreen()
			Delay(50)
		End If		
		; bottom
		If RectsOverlap(MouseX(),MouseY(),1,1,0,400,640/3,200) = True Then
			If mapy > -1 Then mapy = mapy + 1 ; right up
			updatescreen()
			Delay(50)
		End If
		If RectsOverlap(MouseX(),MouseY(),1,1,215,400,640/3,200) = True Then
			If mapx < 101 Then mapx = mapx + 1 ; left up  ; down
			If mapy < 101 Then mapy = mapy + 1 ; right up ;  
			updatescreen()
			Delay(50)
		End If
		If RectsOverlap(MouseX(),MouseY(),1,1,400,400,640/3,200) = True Then
			If mapx < 101 Then mapx = mapx + 1 ; Left down
			updatescreen()
			Delay(50)
		End If
	End If
	
End Function

;
; Origin mainloop
;
;
Function tutorialfunction()
	;
	If tutorialactive= False Then Return
	; Tutorial
	;
	If tutorialactive = True And firsttutorialvisited = False Then
		If firsttutorialtimer+1500 <MilliSecs() Then
			tm = tutorial_mainscreen
			If visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
			firsttutorialvisited = True
		End If
	;Const Hint_contact = 1
	;Const Hint_DisableTutorial = 2
	;Const Hint_Edit = 3
	;Const Hint_Overview = 4
		If firsttutorialtimer+60000 <MilliSecs() Then
			tm = hint_overview
			If visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
			firsttutorialtimer=MilliSecs()
		End If
		If firsttutorialtimer+70000 <MilliSecs() Then
			tm = hint_edit
			If visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
			firsttutorialtimer=MilliSecs()
		End If
		If firsttutorialtimer+80000 <MilliSecs() Then
			tm = hint_disabletutorial
			If visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
			firsttutorialtimer=MilliSecs()
		End If
		If firsttutorialtimer+90000 <MilliSecs() Then
			tm = hint_contact
			If visitedtutorial(tm)=False Then tutorial(tm) : visitedtutorial(tm) = True
			firsttutorialtimer=MilliSecs()
		End If
	End If
	
	;
	;
	; Tutorial
	;
	tm = tutorial_units
	If tutorialactive = True Then
		If Not KeyDown(1) = True Then
			If visitedtutorial(tm)=False And currentkey>0 Then tutorial(tm) : visitedtutorial(tm) = True
		End If
	End If


	End Function

;
; Origin mainloop
;
Function mainloopinput()

		If KeyDown(23) = True Then
			showinfoscreen()
		End If	

		; This is a keyshortcut to the toggle grid (ctrl + g)
		togglegrid

			
		; Load save option........
		; If one of the ctrl keys is pressed
		If KeyDown(29) Or KeyDown(157) Then
			If KeyDown(31) Then currentkey = Asc("s")
			If KeyDown(38) Then currentkey = Asc("l")
			loadsave(currentkey)
			maketempmap : drawminimap : drawmap
			FlushMouse ; - removed flushkeys
		End If
		

		; If pressed 'c' Center to unit
		If KeyDown(46) = True Then
			If (Not KeyDown(29)) And (Not KeyDown(157))
				If currentunit(1) >-1 Then
					;currentunit(2) = 1 ; set unit back to active
					findnextmovableunit(currentplayer)
					centerscreen(currentunit(0),currentunit(1))
					maketempmap:drawminimap:drawmap
				End If
			End If
		End If

	; Function keys menus
	;
	If KeyHit(60) = True Then ; F2 - Temporary
	showcitiesbuildlist()
	End If
	If KeyHit(61) = True Then ; F3 Diplomacy
	diplomacy(-1,-1) 
	End If

		
		; If the user presses Escape in the main program then request if he wishes to exit the game
		If KeyDown(1) = True And release = True And set_quitrequest = True Then
			If grequest(800/2-150,600/2-80,300,170,"Request","","Exit game ?","","Ok","Cancel")=True Then gameloop=False
		End If
		
		If KeyHit(59) Then ; if pressed F1
			greadtext("game.txt",2)
			gtextviewer(100,100,500,300)
			FlushKeys() 
		End If
	
		; If pressed n
		If KeyHit(49) Then
		findnextmovableunit(currentplayer)
		End If

		; Here we trigger the editmode
		If currentkey = Asc("e")
			If editmode = False Then editmode=True Else editmode=False
		End If



		; Execute the general
	If KeyHit(88) = 25 Then
	
		simplemessage("Your trusted General is issuing orders")
		maketempmap : drawminimap : drawmap
		playerattackenemies(currentplayer)
		exitshit = False
		While exitshit = False ; while there are movable units left
		
			; Find if there are movable units left
			exitshit = False
			For i=0 To maxunits
			If units(i,0) = True Then
			If units(i,unit_alignment) = currentplayer Then
			If units(i,unit_fortified) = False Then
			If unitsmove(i) > 1 Then
			exitshit=True
			End If
			End If
			End If
			End If
			Next
		
			;loop thru all enemies
			For nui = 1 To 8
			wargames = False
				For nuii=1 To 8 
					If warstate(nui,nuii) = True Then
						wargames = True
					End If
				Next
				;
				If wargames = True Then
					;updatedefensiveforce(i)
			;		switchaistate(currentplayer)
					reassignai(currentplayer)
			;		
					aicreateattackmission(currentplayer)
					execute10000general()
					playerattackenemies(currentplayer)
				End If
			Next
			If moused(1) = True Then Exit
		Wend
	;	
		; Set all active units to zero movement
		For i=0 To maxunits
		If units(i,0) = True Then
		If units(i,unit_alignment) = currentplayer Then
		If units(i,unit_fortified) = False Then
		unitsmove(i) = 0
		End If
		End If
		End If
		Next
	
	
		maketempmap : drawminimap : drawmap : findnextmovableunit(currentplayer)	
	End If
	




End Function
		

;
; Origin mainloop
;

Function gotofunction()
		;
		; If the user presses G
		;
		If KeyHit(34) = True And playerfindpath = False Then
			If currentunit(2) > -1 Then
				; if we press either shifts then enable stack movement
				playerpathstackmove = False
				If KeyDown(42) = True Or KeyDown(54)= True Then
					playerpathstackmove=True
				End If
				;
				playerfindpath=True
				playerpathstartx = currentunit(0)
				playerpathstarty = currentunit(1)
				playerpathunit = currentunit(2)
				mousepointer = LoadImage("graphics/mousepointer2goto.bmp")
				gotomodeactive = True
			End If
		End If
		
		; If the user presses the right mouse button when the goto method is active then disable the mode
		; 
		If gotomodeactive = True And MouseHit(2) = True Then
			mousepointer = LoadImage("graphics/mousepointer2.bmp")
			gotomodeactive = False
			If playerfindpath= True Then
				playerfindpath = False
			End If
			Delay(200) : FlushKeys()
		End If
	;	If KeyDown(1) = True Then
	;		
	;	End If
	
		;
		; Make sure the unit flickering continues when in goto mode
		;
		If playerfindpath = True Then
		currentunit(0) = playerpathstartx
		currentunit(1) = playerpathstarty
		currentunit(2) = playerpathunit
		End If
		
		; If the use presses the left mouse and if playerfindpath=true
		;
		If MouseHit(1) = True And playerfindpath = True Then
			;If roadmap(cursor(0),cursor(1)) = False Then
			;	simplemessage("Destination must be reachable by road")
			;End If
			;
			
			If units(playerpathunit,13) => 30000 Then
				pathbufferactive(units(playerpathunit,13)-30000) = False
			End If
			a = createunitpath(currentplayer,playerpathstartx,playerpathstarty,cursor(0),cursor(1))
			If a>0 Then
				units(playerpathunit,13) = 30000+a
				;
				; Move the units along
				automoveunits(currentplayer)
				playerfindpath = False
				mousepointer = LoadImage("graphics/mousepointer2.bmp")
				gotomodeactive = False
				findnextmovableunit(currentplayer)
				fixontopunits()
				updatescreen()
			End If
			;
		End If
	End Function

;
; Origin mainloop
;
Function moveunitcontrol()
		; Test
		If afterbattleflag = False Then
			
			; Move the units or give units their commands
			If KeyHit(72) = True Then currentkey = 28
			If KeyHit(75) = True Then currentkey = 31
			If KeyHit(77) = True Then currentkey = 30
			If KeyHit(80) = True Then currentkey = 29
			If KeyHit(71) = True Then currentkey = 1
			If KeyHit(73) = True Then currentkey = 5
			If KeyHit(79) = True Then currentkey = 2
			If KeyHit(81) = True Then currentkey = 6
			If KeyHit(200) = True Then currentkey = 28
			If KeyHit(205) = True Then currentkey = 30
			If KeyHit(203) = True Then currentkey = 31
			If KeyHit(208) = True Then currentkey = 29
			
			If currentkey >0 Then
				If currentunit(2)>-1 Then
					If KeyDown(25) = False And KeyDown(157) = False Then ; 157
					
					moveunit(currentunit(2))
					maketempmap 
					drawminimap
					drawmap
					End If
				End If
			End If
			If currentkey >0 Then
				If currentunit(2)>-1 Then 
				unitcommands 
				maketempmap
				;drawminimap
				drawmap
				End If
			End If
		End If
End Function	

;
; Origin mainloop
;
;
Function mainloopoptimalizations()
	;
	; This code speeds up editing by only updating the slow terrain
	; drawing 1 second after the last edit. (Minimap)
	;	
	If terraineditchangeeffectactive = True Then
		If terraineditchangeeffect+1000 < MilliSecs() Then
			drawminimapterrain
			terraineditchangeeffectactive = False
		End If
	End If
End Function

;
; Origin mainloop
;
Function gamestartmessages()
		;
		; Game start introduction
		;
		If intromessage = 2 And release = True And set_introaboutmessage = True Then
			greadtext("game.txt",3)
			gtextviewer(100,100,500,300)
			FlushKeys()
			firsttutorialtimer = MilliSecs()
			intromessage = 3
			
			; Ask for tutorial
			;
			If set_tutorialrequest = True Then
				If grequest(800/2-150,600/2-70,300,170,"Kings and Knights","Do you wish to play in","tutorial mode","","Yes","No")  = 1 Then tutorialactive=True Else tutorialactive = False
			End If
		End If
		
		If intromessage < 3 Then intromessage = intromessage + 1	
End Function

;
; Origin mainloop
;
Function unitcontrols()

		;
		; If all units have moved then display this info
		If endofturn = True Then
			If endoftime = 1 Then
				; display the flickering end turn label
				Color 255,255,255
				Text 720,580,"< End of Turn >",1,1
			End If
			; if the player presses enter then set everything up for the next turn
			If KeyHit(28) Then 
				oldmapx = mapx
				oldmapy = mapy
				nextturn
		;		If currentunit(2) = -1 Then		
		;		simplemessage (currentunit(2)) 
				If currentunit(2) = -1 Then
					mapx = oldmapx
					mapy = oldmapy
					;Else 
					;centerscreen(currentunit(0),currentunit(1))
				End If
	        	currentkey = 0
				findnextmovableunit(currentplayer)			
				maketempmap
				drawminimap
				drawmap
			End If
		End If
End Function

;
; Origin mainloop
;
Function unitsoptimalization()


		If afterbattleflag = False Then
		  doflickertimer ; Do the flicker	
		End If		
		
		; Wait a while after a user unit movement
		If delayafterplayermovement = True Then
			delayafterplayermovementcountdown = delayafterplayermovementcountdown - 1
			If delayafterplayermovementcountdown < 0 Then
			;	simplemessage("hoi")
				delayafterplayermovementcountdown = delayafterplayermovementcountdowndefault
				Delay(delayafterplayermovementtime)
				FlushKeys()
				delayafterplayermovement = False
				;
				drawminimap ; 1.29 addition
				findnextmovableunit(currentplayer)
				;
			End If
		End If

				If afterbattleflag = True 
			If afterbattletimer+1000<MilliSecs() Then
				afterbattletimer = 0 : afterbattleflag = False
				findnextmovableunit(currentplayer)
				maketempmap : drawminimap : drawmap
			End If
		End If

End Function

;
; Origin mainloop
; editmode
Function mainloopextensions()

		; If the editmode is active then display it and activate it.
		If editmode = True Then
			editarea()
			editshortcuts()
		End If
End Function

;
; Origin mainloop
;
Function drawmapscreen()

			If flickerswitch = True Then DrawImage mapbuffer,0,0 Else DrawImage mapbuffer2,0,0
End Function

Function beep(Tip = 1)
	If tip = 1 Then
	If ChannelPlaying(debugchannel) = False Then debugchannel = PlaySound(victorysound)
	Else
	If ChannelPlaying(debugchannel) = False Then debugchannel = PlaySound(battlesound)
	End If
End Function





