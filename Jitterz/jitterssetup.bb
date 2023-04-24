; jitters setup

; Shall we play sfx?
Global soundeffectsenabled = True
;
; Remove this in final version *minor
Dim levelhasplayerstart(256) ; holds which level has a start location

;
; Select the starting level
Global demoactive = True
; Set the starting level
Global currentlevel = 123
;
;If demoactive = True Then
;	Print "Jitterz - pre demo version by Nebula and Vertigo"
;	Print "Select level 1 to 256"
;	a = Input("please select : ")
;	If (Not a<1) Or (Not a>256) Then currentlevel = 0
;End If
;Delay(200) : FlushKeys()

Global showdebuggrid = False

;
; Game setup
Const maxbaddies = 10
Const maxbaddiesstuff = 3
;
; Baddies default values
Const baddies1_speedx = 1
Const baddies1_speedy = 1

;
; States for the baddies
Const baddies_walkingright = 0
Const baddies_walkingleft = 1
Const baddies_shoot = 2
;
; Lookup for the baddies array
Const baddies_active = 0
Const baddies_x = 1
Const baddies_y = 2
Const baddies_type = 3
Const baddies_state = 4
Const baddies_speedx = 5
Const baddies_speedy = 6
Const baddies_direction = 7
Const baddies_frame = 8
Const baddies_substate = 9
Const baddies_laststate = 10
Const baddies_bugcatch = 11 ; if this value gets to big then we take action
;
; Lookup for the baddies
Const bstuff_active = 0
Const bstuff_x = 1
Const bstuff_y = 2
Const bstuff_type = 3
Const bstuff_state = 4
Const bstuff_speedx = 5
Const bstuff_speedy = 6
Const bstuff_direction = 7
Const bstuff_frame = 8
.Baddiessetup
;
; The baddies and baddies stuff array setup
Global baddiescheckforplayertimer = MilliSecs()
Global baddiescheckforplayerdelay = 500 ; check every half second

Dim baddies(maxbaddies,11) ; 0 = active, x, y,type, state, speedx,speedy direction, frame,laststate
Dim baddiesmovementspeed#(maxbaddies)
Dim baddieslocation#(maxbaddies,1) ; x, y
Dim baddiesstuff(maxbaddies*maxbaddiesstuff,6) 	; active, x, y, type, state	
												;,speedx,speedy, direction, frame
Dim baddiesstufflocation(maxbadies*maxbaddiesstuff,1) ; x, y

.playeranimations
;
; Player animation and definitions
;
; spriteanimations - set up the pointers to the anim array
Const playerstandingright = 0
Const playerwalkingright = 1
Const playerjumpingright = 2
Const playerclimbingright = 3
Const playerthrowingright = 4
Const playerdyingright = 5
Const playerstandingleft = 6
Const playerwalkingleft = 7
Const playerjumpingleft = 8
Const playerclimbingleft = 9
Const playerthrowingleft = 10
Const playerdyingleft = 11
;
;
;
Dim playeranim(11,10)
Global playercurrentanim = playerclimbingright
Global playeranimdelay = 200
Global playeranimtimer = MilliSecs()
Global playeranimframe = 0
Global playeranimpointer = 0
Global playeranimdirection = 1 ;0 left - 1 right
;
;
; animation for the standing still to the right
playeranim(playerstandingright,0) = 0
playeranim(playerstandingright,1) = -1
; animation for the walking to the right
playeranim(playerwalkingright,0) = 1
playeranim(playerwalkingright,1) = 2
playeranim(playerwalkingright,2) = 3
playeranim(playerwalkingright,3) = 2
playeranim(playerwalkingright,4) = -1
; animation for the jumping to the right
playeranim(playerjumpingright,0) = 4
playeranim(playerjumpingright,1) = -1
; animation for the climbing to the right
playeranim(playerclimbingright,0) = 5
playeranim(playerclimbingright,1) = 6
playeranim(playerclimbingright,2) = -1
; animation for the throwing to the right
playeranim(playerthrowingright,0) = 7
playeranim(playerthrowingright,1) = 8
playeranim(playerthrowingright,2) = -1
; animation for dying to the right side
playeranim(playerdyingright,0) = 9
playeranim(playerdyingright,1) = -1
;
; Mirror from for the left side
;
; animation for the standing still to the right
playeranim(playerstandingleft,0) = 0+10
playeranim(playerstandingleft,1) = -1
; animation for the walking to the right
playeranim(playerwalkingleft,0) = 1+10
playeranim(playerwalkingleft,1) = 2+10
playeranim(playerwalkingleft,2) = 3+10
playeranim(playerwalkingleft,3) = 2+10
playeranim(playerwalkingleft,4) = -1
; animation for the jumping to the right
playeranim(playerjumpingleft,0) = 4+10
playeranim(playerjumpingleft,1) = -1
; animation for the climbing to the right
playeranim(playerclimbingleft,0) = 5+10
playeranim(playerclimbingleft,1) = 6+10
playeranim(playerclimbingleft,2) = -1
; animation for the throwing to the right
playeranim(playerthrowingleft,0) = 7+10
playeranim(playerthrowingleft,1) = 8+10
playeranim(playerthrowingleft,2) = -1
; animation for dying to the right side
playeranim(playerdyingleft,0) = 9+10
playeranim(playerdyingleft,1) = -1

;
;  For the poop sprites
;
Dim poopanim(4,10)
Dim poopanimdelay(maxplayerobjects) ; flying poop, fragmenting shit
Dim poopanimtimer(maxplayerobjects) ; time for flying poop, timer for fragmenting poop
Dim poopanimpointer(maxplayerobjects) ; holds the current animation frame for the poop

; Delay for the flying poop throwing speed
Const defaultpoopanimdelay = 50
; Delay for the smashed poop
Const defaultsmashingpoopanimdelay = 200

; If one of these values is true then a after a certain time the 
; poop gets thrown
Global delayedpoopright = False
Global delayedpoopleft = False
Global delayedpoopdelay = 200
Global delayedpooptimer = MilliSecs()

; normal poop flying
poopanim(0,0) = 140
poopanim(0,1) = 141
poopanim(0,2) = -1
; normal poop smashed
poopanim(1,0) = 142
poopanim(1,1) = 143
poopanim(1,2) = 144
poopanim(1,3) = -1
; fire poop flying
poopanim(2,0) = 145
poopanim(2,1) = 146
poopanim(2,2) = -1
; fire poop smashed
poopanim(3,0) = 147
poopanim(3,1) = 148
poopanim(3,2) = 149
poopanim(3,3) = -1


.gamesetup

Global mmx = 0 ; holds the mouse coordinates
Global mmy = 0 ; holds the mouse coordinates


;Dim springtimer(2) ; holds the time/x/y when the spring should be reset

Const ispushblock = 69
Const isspring = 49 
Const isspring2 = 48

Const ispoop = 60
Const isboot1 = 61
Const isboot2 = 62
Const isboot3 = 63
Const isboot4 = 64
Const ishammer = 65
Const isheart = 66
Const isbanana = 68
Const isblockedbanana = 76
Const isswitch = 74
Const isblockedswitch =75
Const iskey = 73
Const isdiamond = 77
Const isnugget = 78
Const isgold = 79
; Layer 0 preset
Const isblockedkey = 72




Dim Pal(15,3) ; snarty's pallete


Global win
Global can
Global winwidth = 320
Global winheight = 240+32+2

.Graphicssetup
If demoactive = False Then Graphics 320,240,8,2
ShowPointer()
win = CreateWindow("Window",ClientWidth(Desktop())/2-winwidth/2-10000,ClientHeight(Desktop())/2-winheight/2,winwidth,winheight,0,32)
;win = CreateWindow("",0,0,640,480,0,32)
HideGadget win
can = CreateCanvas(0,0,320,240+32+2,win)
SetBuffer CanvasBuffer(can)

;Graphics 320,240,8,3
;SetBuffer BackBuffer()

; Set the c64 font
Global mainfont = LoadFont("font/Cbm-64",8)
SetFont mainfont

.loadtheimages
; Load the game tiles
Global gametiles = LoadAnimImage("tiles.bmp",16,16,0,240)
MaskImage gametiles,96,0,184

; Load the sprites
Global gamesprites = LoadAnimImage("sprites.bmp",16,16,0,160)
MaskImage gamesprites,0,252,248

Global statusbarimage = LoadImage("statusbar.bmp")
MaskImage statusbarimage,0,252,248

.LoadSounds
;
; Load the sound effects
Global playerjumpsound = LoadSound("soundfx/jump.wav")
Global playerfallsound = LoadSound("soundfx/fall.wav")
Global playerspringsound = LoadSound("soundfx/spring.wav")
Global playerpushstonesound = LoadSound("soundfx/push stone.wav")
Global playergetbanannasound = LoadSound("soundfx/get bananna.wav")
Global playergethealthsound = LoadSound("soundfx/get health.wav")
Global playerhurtsound = LoadSound("soundfx/hurt.wav")
Global playeritemgetsound = LoadSound("soundfx/item get.wav")
Global playerdeadsound = LoadSound("soundfx/dead.wav")
Global playerdooropensound = LoadSound("soundfx/door open.wav")
Global poopsplatsound = LoadSound("soundfx/poopsplat.wav")
;

Global channelfall  ; for the falling sound
Global channeljump

Dim gamemap(256,20,15,2)

; For the moving blocks and stuff
Global Lastblockstate = False ; holds the previous state of the player moving a block (aligning purpose)
Global movingblock = False ; if the player is moving a block then this flag is true
Global currentmovingblock = 0 ; holds the blocknumber of the current moving block

Const maxobjects = 20
Dim objects(256,maxobjects,2) ; tilenumber, x, y

; For the player poop
Const maxplayerobjects = 20
Dim playerobjects(maxplayerobjects,6) ; active,x,y,speed,type,frame, state


; These are the cls level color codes
Const levelcls_color1 = 12
Const levelcls_color2 = 0
Const levelcls_color3 = 5
Const levelcls_color4 = 9
; This array is filled up with cls values for each level
Dim levelclscolor(256)


.keysetup
; Keys for keydown
;Const keys = 31
Const keytab = 15
Const keyenter = 28
Const keyp = 25
Const key1 = 3
Const keyf1 = 59
Const keycursorup = 200
Const keycursorleft = 203
Const keycursorright = 205
Const keycursordown = 208
Const keyrightctrl = 157
Const keyleftctrl = 29
Const keyleftshift = 42
Const keyrightshift = 54
Const keyspace = 57
Const keyleftalt = 56
Const keyrightalt = 184
Const keyw = 17
Const keya = 30
Const keyd = 32
Const keys = 31
Const keyminus = 12
Const keyis = 13
Const keym = 50

; player controls
Const playerjump = keyleftctrl
Const playeraltjump = keyrightctrl
Const playerleft = keycursorleft
Const playeraltleft = keya
Const playerright = keycursorright
Const playeraltright = keyd
Const playerup = keycursorup
Const playeraltup = keyw
Const playerdown = keycursordown
Const playeraltdown = keys
Const playerfire = keyrightalt
Const playeraltfire = keyleftalt
Const playerpullblock = keyleftshift

setpalette() ; set palette
; Load the level
temploadlevels()

;
;
Global statusbaractive = True
Global statusbarx = 0
Global statusbary = 0

Global playercanjump = True

Global playergravityactive = False
Global playerfallingspeed# = 0

.playersetup
Global playerx#
Global playery#
Global inclimb = False ; if we are climbing this value is true
Global jumpfromclimb = False ; If we are on a ladder and move this value is true
Global iceslidevalue# = 0
Global iceslidedelay = -1 ; infinite slide
Global iceslideairdelay = 100 ; continue moving in the air;
Global iceslideairtimer = iceslideairdelay
Global iceslidetimer = MilliSecs()

Global fps = 60
Global fps2 = 0
Global fpstimer = MilliSecs()
Global fpsdelay = 1000

; shows the player as mario then set to true
Global marioactive = False

Global simage = CreateImage(320,240,2)

;Global timer = CreateTimer(60)

createlevelai()
setplayerstartlocation(4)


.Startgame

If demoactive = True Then
		SetGadgetShape win,GadgetX(win)+10000-320/2,GadgetY(win)-240/2,640,480-56
	Else
		SetGadgetShape win,0,0,640,480-56
End If
SetGadgetShape can,0,0,640,480
ShowGadget win
