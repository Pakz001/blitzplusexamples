;
; Chimp game editor by Rudy van Etten
;
;


;fileout = WriteFile("c:\windows\desktop\gendat.txt")
;For i=220 To 240
;WriteLine fileout,"Case " + Str(i) + " : Return 2"
;Next
;CloseFile(fileout)
;End

;While KeyDown(1) = False
;Delay(100)
;Print GetKey()
;Wend
;End
;

;While KeyDown(1) = False
;	For i=1 To 255
;		If KeyDown(i) = True Then Print i : Delay(100)
;	Next
;Wend
;End


Const erasetile = 0

Const playerstartpos = 200

Const levelscrollleft = 129
Const levelscrollright = 130
Const levelscrollup = 131
Const levelscrolldown = 132

Global gridactive = False

; getkey
Const keyminus = 45
Const keyis = 61
Const keysave = 115
Const keycursorup = 28
Const keycursordown = 29
Const keycursorleft = 31
Const keycursorright = 30
Const keydelete = 4

; keydown

Const keyenter = 28
Const keybackspace = 14
Const keyv = 47
Const keyc = 46
Const keyleftctrl = 29
Const keyrightctrl = 157

Const keycup = 200
Const keycdown = 208

Const keyleftshift = 42
Const keyrightshift = 54

Const keyloadlevels = 108

Const keymoveup = 61
Const keymovedown = 45

Const keyconsole = 57 ; space
Const keyhelp = 59 ; f1
Const keygrid = 15 ; tab

;
; Auto save setup
Global autosavetimer = MilliSecs()
Global autosavedelay = (1000*60)*10 ; autosave every 10 minutes

;
; Graphics setup
gmode = 3
Graphics 320,240,8,gmode
SetBuffer BackBuffer()


Global simage1 = CreateImage(320,240) ; for scaling the stuff
Global simage2 = CreateImage(320,240) ; for scaling the stuff

Dim levelbuffer(20,15,2) ; for copying levels

Global mainfont = LoadFont("font/Cbm-64",8)
SetFont mainfont

Global gametiles = LoadAnimImage("tiles.bmp",16,16,0,240)
MaskImage gametiles,96,0,184

Dim gamemap(256,320/16,240/16,2) ; map,x,y,layer
Dim gameactive(256) ; is there a start location in the map


Dim helptext$(99)
 helptext$(0) = "Help system - Press escape or space"
 helptext$(1) = "to Exit, use cursors to scroll."
 helptext$(2) = ""
 helptext$(3) = "* Press F1 for this window"
 helptext$(4) = "* Press tab to toggle the grid"
 helptext$(5) = "* Press space to toggle the edit"
 helptext$(6) = "  console"
 helptext$(7) = "* Press the right mouse button" 
 helptext$(8) = "  on a icon on the map to use it" 
 helptext$(9) = "  to draw with"
helptext$(10) = "* Press '-' '=' to cycle thru the"
helptext$(11) = "  tiles"
helptext$(12) = "* Press the left mouse button
helptext$(13) = "  to draw"
helptext$(14) = "* press del for the blank square"
helptext$(15) = "* Press left shift + right click"
helptext$(16) = "  to destroy the tile"
helptext$(17) = "* Use the arrow keys to move thru"
helptext$(18) = "  the levels"
helptext$(19) = "* Ctrl + c to copy the current level"
helptext$(20) = "* Ctrl + v to paste a level from memory"
helptext$(21) = "* Ctrl + backspace to delete the current level"
helptext$(21) = "(End of help text)"
Const helptextlength = 24

Global consolevisible = False

Global currentmap = 0
Local currentkey = 0
Dim currentcursor(2) ; x,y,number

loadlevels()

While KeyDown(1) = False
	Cls
	currentkey = GetKey()

	autosave()
	
	;
	If KeyHit(keyenter) = True Then
		Delay(200) : FlushKeys()
		;Locate 0,20
		a = Input("Select level : ")
		If a>0 And a<256 Then 
			currentmap = a
		End If
		Delay(500) : FlushKeys()
	End If 

	;68
;	If KeyHit(2) = True Then
;	For i=0 To 256
;		For ii=0 To 2
;			For x=0 To 20
;				For y=0 To 15
;					If gamemap(i,x,y,ii) = 20 Then gamemap(i,x,y,ii) = 0
;				If gamemap(i,x,y,1) = 68 Then gamemap(i,x,y,2) = 68
;				Next
;			Next
;		Next
;	Next
;	simplemessage("map changed")
;	End If
	;
	
	; Do the editing
	If MouseDown(1) = True Or MouseDown(2) = True Then
		currentcursor(0) = MouseX()/16
		currentcursor(1) = MouseY()/16
		; Take the tile as our drawing tile
		If MouseDown(2) = True Then
			;
			drawselected = False
			; layer 2 takes priority
			If gamemap(currentmap,currentcursor(0),currentcursor(1),2) > 0 Then		
				currentcursor(2) = gamemap(currentmap,currentcursor(0),currentcursor(1),2)
				drawselected = True
			End If
			; layer 1 takes second priority
			If gamemap(currentmap,currentcursor(0),currentcursor(1),1) > 0 Then
				If drawselected = False Then
					currentcursor(2) = gamemap(currentmap,currentcursor(0),currentcursor(1),1)
					drawselected = True
				End If
			End If
			; layer 0
			If gamemap(currentmap,currentcursor(0),currentcursor(1),0) => 0
				If drawselected = False Then
					currentcursor(2) = gamemap(currentmap,currentcursor(0),currentcursor(1),0)
					drawselected = True
				End If
			End If
			;
		End If
		
		; Draw normally
		;
		;
		; Check if we dont go outside the map
		If currentcursor(0)=> 0 And currentcursor(0)=<20 And currentcursor(1)=>0 And currentcursor(1)=<15 Then
			If MouseDown(1) = True Then	
				;
				layer = returnlayer(currentcursor(2))
				;
				;If currentcursor(2) = 44 Then layer = 1
				;if currentcursor(2) = playerstartpos Then layer = 2 
				;
				gamemap(currentmap,currentcursor(0),currentcursor(1),layer) = currentcursor(2)
			Else If MouseDown(2) = True And KeyDown(keyleftshift) = True Then
				For i=0 To 2
					If i=0 Then tiletoerase = erasetile Else tiletoerase = 0
					gamemap(currentmap,currentcursor(0),currentcursor(1),i) = tiletoerase
				Next
				
			End If
		End If
	End If
	;
	; Delete the current level
	If KeyDown(keybackspace) = True Then
		If KeyDown(keyleftctrl) = True Or KeyDown(keyrightctrl) = True Then
			For i=0 To 2
				For x=0 To 20
					For y=0 To 15
						gamemap(currentmap,x,y,i) = erasetile
					Next
				Next
			Next
		End If
	End If
	;
	;
	; Copy a level into memory
	If KeyDown(keyc) = True Then
		If KeyDown(keyleftctrl) Or KeyDown(keyrightctrl) Then
		For i=0 To 2
			For x=0 To 20
				For y=0 To 15
					levelbuffer(x,y,i) = gamemap(currentmap,x,y,i)
				Next
			Next
		Next
		simplemessage("level copied")
		End If
	End If
	If KeyDown(keyv) = True Then
		If KeyDown(keyleftctrl) Or KeyDown(keyrightctrl) Then
		For i=0 To 2
			For x=0 To 20
				For y=0 To 15
					gamemap(currentmap,x,y,i) = levelbuffer(x,y,i)
				Next
			Next
		Next
		simplemessage("level pasted")
		End If
	End If

	
	
	; Move drawing tile cursor up
	If currentkey = keymoveup Then
		If currentcursor(2) < 240 Then currentcursor(2) = currentcursor(2) + 1
	End If
	; Move drawing tile cursor down
	If currentkey = keymovedown Then
		If currentcursor(2) > 0 Then currentcursor(2) = currentcursor(2) - 1
	End If
	
	; Load the levels
	If currentkey = keyloadlevels Then
		loadlevels()
	End If
	
	; Show the console
	If KeyDown(keyconsole) = True Then console() 
	
	; Move level up
	If currentkey = keycursorup Then
		If currentmap>15 Then 
			oldmap = currentmap
			currentmap = currentmap - 16
			showworldmap(oldmap)
		End If
	End If
	
	; Move level down
	If currentkey = keycursordown Then
		If currentmap<255-15 Then 
			oldmap = currentmap
			currentmap = currentmap + 16
			showworldmap(oldmap)
		End If
	End If
	; Move level left
	If currentkey = keycursorleft Then
		If currentmap>0 Then 
			oldmap = currentmap
			currentmap = currentmap - 1
			showworldmap(oldmap)
		End If
	End If
	
	; Move level right
	If currentkey = keycursorright Then
		If currentmap<255 Then 
			oldmap = currentmap
			currentmap = currentmap + 1
			If currentmap>255 Then currentmap = 255
			showworldmap(oldmap)
		End If
	End If

	
	
	; Select between the first 10 tiles
	If currentkey => 48 And currentkey =<57 Then
		mappos = currentkey - 48
		currentcursor(2) = mappos
	End If
	
	; Save the levels
	If currentkey = keysave Then
		exportlevel()
	End If

	; Toggle the grid
	If KeyHit(keygrid) = True Then
		If gridactive = True Then gridactive = False Else gridactive = True
		Delay(100)
	End If
	
	; Toggle the help
	If KeyHit(keyhelp) = True Then
		showhelp()
	End If

	; Draw the graphics
	drawmap(currentmap)
	drawgrid()

	; Show layer 0 mapcodes
	If KeyDown(keyrightshift) = True Then
		For x=0 To 20
			For y=0 To 15
				Color 255,255,255
				Rect x*16,y*16,17,17,0
				Text (x*16)+8,(y*16)+8,gamemap(currentmap,x,y,0),1,1
			Next
		Next
	End If
	
	; delete = cursor tile = 0
	If currentkey = keydelete Then
		currentcursor(2) = 0
	End If
	
	; Draw the mouse
	drawmouse()
	
	; Show some info
	Color 255,255,255
	Text 0,0,"Current level : " + currentmap
	Text 0,20,"currentcursor : " + currentcursor(2) + " layer " + returnlayer(currentcursor(2))
	simage1 = CreateImage(320,240)
	;GrabImage(simage1,0,0)
	;simage2 = CopyImage(simage1)
	;simage2 = ScaleImagefast(simage2,640,480)
	;DrawBlock simage2,40,40
	Flip
Wend
End

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
	Case 160 : Return 0
	Case 161 : Return 0
	Case 162 : Return 0
	Case 163 : Return 0
	Case 164 : Return 0
	Case 165 : Return 0
	Case 166 : Return 0
	Case 167 : Return 0
	Case 168 : Return 0
	Case 169 : Return 0
	Case 170 : Return 0
	Case 171 : Return 0
	Case 172 : Return 0
	Case 173 : Return 0
	Case 174 : Return 0
	Case 175 : Return 0
	Case 176 : Return 0
	Case 177 : Return 0
	Case 178 : Return 0
	Case 179 : Return 0
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


Function drawmouse()
	
	Color 200,200,200
	Rect MouseX(),MouseY(),16,16,0
	
	If currentcursor(2) < 240 Then
		DrawImage gametiles,MouseX(),MouseY(),currentcursor(2)
	End If
	
	If currentcursor(2) = playerstartpos Then
		Color 0,255,0
		Rect MouseX(),MouseY(),16,16,0
		Text MouseX()+8,MouseY()+8,"S",1,1
	End If
	
	Color 255,255,0
	Line MouseX()-10,MouseY(),MouseX()+10,MouseY()
	Line MouseX(),MouseY()-10,MouseX(),MouseY()+10
	
End Function



Function drawmap(map)
x1 = 0
y1 = 0
For x=0 To 20
	For y=0 To 15
		pos = gamemap(map,x,y,0)
		pos2 = gamemap(map,x,y,1)
		pos3 = gamemap(map,x,y,2)
		;
		; Draw layer 0
		If pos <240 Then
			DrawImage gametiles,x*16,y*16,pos
		End If
		; Draw layer 1
		If pos2<240 Then
			If Not pos2 = 0 Then DrawImage gametiles,x*16,y*16,pos2
		End If
		; Draw layer 2
		If pos3 =< 240 Then
			If Not pos3 = 0 Then DrawImage gametiles,x*16,y*16,pos3

;			Color 0,255,0
;			Rect x*16,y*16,16,16,0
;			Color 255,255,255
;			Text (x*16)+8,(y*16)+8,"S",1,1
		End If
		y1=y1+1 : If y1=15 Then y1=0
	Next
	x1=x1+1
Next
End Function


Function drawgrid()
If gridactive = False Then Return
Color 255,255,255
For x=0 To 20 
	For y=0 To 15
		Rect x*16,y*16,17,17,0
	Next
Next
End Function



Function exportlevel()

;Local savestring$

;fileout = WriteFile("leveldata.txt")
;
;For i=0 To 256 ; loop thru all levels
;	;
;	WriteLine(fileout,"; Level data from level "+Str(i))
;	For ii=0 To 2
;		;
;		WriteLine(fileout,"; Layer "+Str(ii))
;		;
;		For y=0 To 15
;			;
;			savestring$ = "Data "
;			For x=0 To 20
;				savestring$ = savestring + Str(gamemap(i,x,y,ii)) 
;				If x < 20 Then
;					savestring$=savestring$+ ","
;				End If
;			Next
;			;
;			WriteLine(fileout,savestring$)
;			;
;		Next
;		;
;	Next
;	;
;	WriteLine(fileout,"; End of level")
;	;
;Next
;
;CloseFile(fileout)

fileout = WriteFile("leveldata.map")
;
For i=0 To 256 ; loop thru all levels
	;
	For ii= 0 To 2
		For x=0 To 20
			For y=0 To 15
				WriteInt(fileout,gamemap(i,x,y,ii))
			Next
		Next
	Next
	;
Next

CloseFile(fileout)


simplemessage("levels saved")

End Function



Function loadlevels()
filein = ReadFile("leveldata.map")

For i=0 To 256
	For ii=0 To 2
		For x=0 To 20
			For y=0 To 15
				gamemap(i,x,y,ii) = ReadInt(filein)
				; If there is a start location in the map then set the flag to true
				If gamemap(i,x,y,ii) = 200 Then gameactive(i) = True
			Next
		Next
	Next
Next

CloseFile(filein)

simplemessage("levels loaded")
End Function




Function simplemessage(message$)
Local x1 = 320/2
Local y1 = 240/2
Local width = 200
Local height = 80
Local x = x1-width/2
Local y = y1-height/2
;
While KeyDown(1) = False And MouseDown(1) = False And MouseDown(2) = False And KeyDown(57) = False And KeyDown(28) = False
	Cls
	drawmap(0)
	Color 255,255,255
	Rect x,y,width,height,1
	Color 0,0,0
	Text x1,y1,message$,1,1
	Text x1,y1+25,"Press stuff to exit",1,1
	Flip
Wend
Delay(200) : FlushKeys():FlushMouse()

End Function

;
; This is the console window
;
;

Function console()
;
Local timedelay = 1
Local timedelaycounter = MilliSecs() 
Local consoleheight = 16*12
Local hovertile = 0
;
Local y = 0
;
Local timer = CreateTimer(60)
Delay(200)
FlushKeys()
;
;While y<consoleheight
;	WaitTimer(timer)
;	Cls
;	drawmap(currentmap)
;	drawgrid()
;	y = y + 5
;	Color 0,0,0
;	Rect 0,0,320,y,1
;	;
;	Flip
;Wend
;
; This is the actual code for the console
;
Local exitloop = False
;
While KeyDown(1) = False And KeyDown(keyconsole) = False And exitloop = False
;	WaitTimer(timer)
	Cls
	drawmap(currentmap)
	drawgrid()
	Color 0,0,0
	Rect 0,0,320,consoleheight,1
	Color 155,155,155
	Rect 0,0,320,consoleheight,0
	;
	Color 200,200,200
	For y=0 To 11
		For x=0 To 19
			DrawImage gametiles,x*16,y*16,x+(y*20)
			Rect x*16,y*16,17,17,0
			If MouseDown(1) = True Then
				If RectsOverlap(x*16,y*16,16,16,MouseX(),MouseY(),1,1) = True Then
					currentcursor(2) = x+(y*20)
					exitloop = True
				End If
				Else
				If RectsOverlap(x*16,y*16,16,16,MouseX(),MouseY(),1,1) = True Then
					hovertile = x+(y*20)
				End If

			End If
		Next
	Next
	;
	; Here we show and execute the extra stuff
	;
;	Color 255,255,25
;	Rect 0,16*9,16,16,0
;	Text 0+8,16*9+8,"S",1,1
	;
;	If RectsOverlap(MouseX(),MouseY(),1,1,0,16*9,16,16) = True Then
;		If MouseDown(1) = True Then
;			currentcursor(2) = levelscrollleft
;			exitloop = True
;		End If
;	End If
	;
;	y= 16*9
;	y = 16
;	Color 255,255,25
;	Rect x,y,16,0
;	Text x+8,x+8,"L",1,1
;	If RectsOverlap(MouseX(),MouseY(),1,1,x,y,16,16) = True Then
;		If MouseDown(1) = True Then
;			currentcursor(2) = 129
;			exitloop = True
;		End If
;	End If
;
	;
	; Draw the mouse info
;	Rect MouseX(),MouseY(),5,5,1
	Color 255,255,0
	Line MouseX()-10,MouseY(),MouseX()+10,MouseY()
	Line MouseX(),MouseY()-10,MouseX(),MouseY()+10

	Text MouseX(),MouseY()+16,hovertile,1,1
	;
	Flip
Wend


;y = consoleheight
;While y>0 
;	WaitTimer(timer)
;	Cls
;	drawmap(currentmap)
;	drawgrid()
;	If timedelaycounter+timedelay < MilliSecs() Then
;	y = y - 5
;	timedelaycounter = MilliSecs()
;	End If
;	Color 0,0,0
;	Rect 0,0,320,y,1
;	Flip
;Wend


Delay(200):FlushKeys():FlushMouse()

End Function

Function showhelp()
;
Local helpwidth = 320
Local helpheight = 180
Local helpleft = 320/2-helpwidth/2
Local helptop = 240/2-helpheight/2
;
Local helpcursor = 0
Local linecursor = 0
;
Delay(200) : FlushKeys() : FlushMouse()
While KeyDown(1) = False And KeyDown(57) = False And MouseDown(2) = False And KeyDown(keyhelp) = False
	Cls
	drawmap(currentmap)
	drawgrid()
	drawmouse()
	;
	Color 255,255,255
	Rect helpleft,helptop,helpwidth,helpheight,1
	;
	If KeyDown(keycup) = True Then 
		If helpcursor>0 Then helpcursor = helpcursor - 1 : Delay(100)
	End If
	If KeyDown(keycdown) = True Then
		If helpcursor+(helpheight/20)=<helptextlength Then helpcursor = helpcursor + 1 : Delay(100)
	End If
	;
	linecursor = helpcursor
	Color 0,0,0
	;
	If linecursor > 0 Then 
		Text helpleft+helpwidth-25,helptop,"(<)"
	End If
	;
	y = 0
	While y<helpheight
		Text helpleft,helptop+y,helptext$(linecursor)
		y=y+20 : linecursor = linecursor + 1
	Wend
	;
	If linecursor < helptextlength Then
		Text helpleft+helpwidth-25,helptop+helpheight-20,"(>)"
	End If
	;
	Flip
Wend 
Delay(200):FlushKeys():FlushMouse()
End Function

;
; This function shows all the maps in the level on one screen
;
Function showworldmap(oldpos)
;
Local x=0
Local y=0
Local i=0
; get the position
For i=0 To 256
	If currentmap = i Then Exit
	x=x+1 : If x=16 Then x=0 : y=y+1
Next
;
Local blocksize = 14
;
counter = 0
	For y1=0 To 15
	For x1=0 To 15
		Color 100,100,100
		Rect x1*blocksize,y1*blocksize,blocksize,blocksize,0
		Color 0,0,0
		If x = x1 And y = y1 Then Color 255,255,0
		Rect x1*blocksize,y1*blocksize,blocksize,blocksize,1
		Color 255,255,255
		Rect x1*blocksize,y1*blocksize,blocksize,blocksize,0
		If gameactive(counter) = True Then
			Color 255,0,0
			Rect (x1*blocksize)+4,(y1*blocksize)+4,blocksize-8,blocksize-8,1
		End If
		If counter = oldpos Then
			Color 0,255,0
			Rect x1*blocksize,y1*blocksize,blocksize,blocksize,1
		End If
		Color 255,255,255
		Text (x1*blocksize)+8,(y1*blocksize)+8,counter,1,1
		counter=counter+1
	Next
	Next
	Flip
Delay(300)
;

End Function


;
; This is a auto save routine
;
Function autosave()
	If autosavetimer+autosavedelay<MilliSecs() Then
		autosavetimer = MilliSecs()
		simplemessage("Auto saving")
		exportlevel()
	End If
End Function


; These functions were NOT written by me! Someone posted them somewhere ages ago,
; but didn't put their name in the source. I have a vague feeling the guy's name
; might have been Daniel something, but I may just be hallucinating. If you wrote
; these, let me know!

; I have just added two lines to each function to store and restore the current
; graphics buffer and made this crappy little demo -- use the mouse buttons
; to big up/smallify...

;adder = 16
;
;Graphics 640,480
;SetBuffer BackBuffer ()
;
;image = LoadImage ("F:\My Documents\Development\Blitz 2D Sources\boing.bmp")
;
;scalex# = ImageWidth (image)
;scaley# = ImageHeight (image)
;
;While Not KeyHit(1)
;	
;    Cls
;
;    If MouseDown(1)
;		scalex = scalex + adder
;		scaley = scaley + adder
;	Else
;	    If MouseDown(2)
;			scalex = scalex - adder: If scalex <= 2 Then scalex = 2
;			scaley = scaley - adder: If scaley <= 2 Then scaley = 2
;		EndIf
;	EndIf
;
;    scaledimage = ScaleImageFast (image, scalex, scaley)
;
;	MidHandle scaledimage
;    DrawImage scaledimage, MouseX (), MouseY ()
;    FreeImage scaledimage
;
;    Flip
;
;Wend
;
;___________________________________________________________________

Function ScaleImageFast(image,newwidth,newheight,frame=0)

	tbuffer = GraphicsBuffer ()
	
    oldwidth = ImageWidth(image)
    oldheight = ImageHeight(image)

    newwidth=newwidth-1
    newheight=newheight-1
    ni=CreateImage(newwidth+1,oldheight)
    dest = CreateImage(newwidth+1,newheight+1)
    SetBuffer ImageBuffer(ni)
    For x = 0 To newwidth
        LineRef = Floor((oldwidth*x)/newwidth)
        DrawBlockRect image,x,0,LineRef,0,1,oldheight,frame
    Next
    SetBuffer ImageBuffer(dest)
    For y = 0 To newheight
        LineRef = Floor((oldheight*y)/newheight)
        DrawBlockRect ni,0,y,0,LineRef,newwidth,1
    Next 
    FreeImage ni

	SetBuffer tbuffer
	
    Return dest
End Function

Function ScaleImageFast1(image,newwidth#,newheight#,frame=0)

	tbuffer = GraphicsBuffer ()
	
    oldwidth# = ImageWidth(image)
    oldheight# = ImageHeight(image)

    newwidth=newwidth-1
    newheight=newheight-1
    ni=CreateImage(newwidth+1,oldheight)
    dest = CreateImage(newwidth+1,newheight+1)
    SetBuffer ImageBuffer(ni)
    For x# = 0 To newwidth
        LineRef# = Floor((oldwidth*x)/newwidth)
        DrawBlockRect image,x,0,LineRef,0,1,oldheight,frame
    Next
    SetBuffer ImageBuffer(dest)
    For y# = 0 To newheight
        LineRef = Floor((oldheight*y)/newheight)
        DrawBlockRect ni,0,y,0,LineRef,newwidth,1
    Next 
    FreeImage ni
	SetBuffer tbuffer
	
    Return dest
End Function





