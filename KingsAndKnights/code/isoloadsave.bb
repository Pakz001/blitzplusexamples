;
; Include for load / save functions
;
;
; 
; Version 0.1
;
;
;
;


;
; Save the game
;
Function savegame(name$)
fileout = WriteFile(name$)

; Header
WriteString(fileout,"codedbyrudyvanettenin2001")

; Playerplays
WriteInt(fileout,currentplayer)

; Gameturn
WriteInt(fileout,gameturn)

; End of turn
WriteInt(fileout,endofturn)

; Playergold
For i=0 To 8
WriteInt(fileout,playergold(i))
Next


; map view coordinates
WriteInt(fileout,mapx)
WriteInt(fileout,mapy)

; cursor location
WriteInt(fileout,cursor(0))
WriteInt(fileout,cursor(1))

; mapwidth/height
WriteInt(fileout,mapwidth)
WriteInt(fileout,mapheight)

;Currentunit x,y,unit
WriteInt(fileout,currentunit(0))
WriteInt(fileout,currentunit(1))
WriteInt(fileout,currentunit(2))
;

; All units
For i=0 To maxunits
For ii=0 To 32
WriteInt(fileout,units(i,ii))
Next
Next
;

; Unit move float
For i=0 To maxunits
WriteFloat(fileout,unitsmove#(i))
Next
;

; Unit string$
For i=0 To maxunits
For ii=0 To 5
WriteString(fileout,unitstring$(i,ii))
Next
Next
;

; Cities
For i=0 To maxcities
For ii=0 To 20
WriteString(fileout,cities(i,ii))
Next
Next

; Citystring
For i=0 To maxcities
WriteString(fileout,citiesstring$(i,0))
Next

; Roadmap
For i=0 To mapwidth
For ii = 0 To mapheight
WriteInt(fileout,roadmap(i,ii))
Next
Next
; Roadmapstring
For i=0 To mapwidth
For ii=0 To mapheight
WriteString(fileout,roadmapstring$(i,ii))
Next
Next

; Map
For i=0 To mapwidth
For ii=0 To mapheight
WriteInt(fileout,map(i,ii))
Next
Next

; Fortresses
For i=0 To mapwidth
For ii=0 To mapheight
WriteInt(fileout,fortressmap(i,ii))
Next
Next

;warstate
For i=0 To 8
For ii=0 To 8
WriteInt(fileout,warstate(i,ii))
Next
Next

; Fog of war
For i=0 To 8
For x=0 To 100
For y=0 To 100
WriteInt(fileout,vismap(x,y,i))
Next
Next
Next

; Mines
For x=0 To 100
For y=0 To 100
For i=0 To 5
WriteInt(fileout,minemap(x,y,i))
Next
Next
Next

; Player city conquest history
For i=0 To maxcities
For ii=0 To 2
WriteInt(fileout,playercityconquest(i,ii))
Next
Next

;
; Coast lines
For x = 0 To mapwidth*2
For y= 0 To mapheight*2
WriteInt(fileout,coastlinemap(x,y))
Next
Next

; Trade status
For i=1 To 8
For ii=1 To 8
WriteInt(fileout,tradestatus(i,ii))
Next
Next

; Player relations
For i=1 To 8
For ii=1 To 8
For iii=0 To 10
WriteInt(fileout,playerrelations(i,ii,iii))
Next
Next
Next

; Fog of war
WriteInt(fileout,fogofwarenabled)
For x=0 To mapwidth
For y=0 To mapheight
For i=1 To 8
WriteInt(fileout,vismap(x,y,i))
Next
Next
Next

; Write the map data
For x=0 To 100
	For y=0 To 100
		WriteInt(fileout,amap(x,y))
		WriteInt(fileout,beachmap(x,y))
		WriteInt(fileout,islandmap(x,y))
		WriteInt(fileout,mg_citymap(x,y))
		WriteInt(fileout,bewoondterrein(x,y))
	Next
Next


CloseFile(fileout)

End Function

;
; Load the game
;
Function loadgame(name$)
filein = ReadFile(name$)

header$ = ReadString(filein)
If Not header$ = "codedbyrudyvanettenin2001" Then 
simplemessage("Not a valid savegame")
Return
End If

; Free the stuff in memory
freegamedata()


; Player plays
currentplayer = ReadInt(filein)

; Gameturn
gameturn = ReadInt(filein)

; End of turn
endofturn = ReadInt(filein)

; Playergold
For i=0 To 8
playergold(i) = ReadInt(filein)
Next

; map view coordinates
mapx = ReadInt(filein)
mapy = ReadInt(filein)

; cursor location
cursor(0) = ReadInt(filein)
cursor(1) = ReadInt(filein)

; mapwidth/height
mapwidth = ReadInt(filein)
mapheight = ReadInt(filein)

;Currentunit x,y,unit
currentunit(0) = ReadInt(filein)
currentunit(1) = ReadInt(filein)
currentunit(2) = ReadInt(filein)
;

; All units
For i=0 To maxunits
For ii=0 To 32
units(i,ii) = ReadInt(filein)
; Temporary reseting of ai
If units(i,unit_ai) > 100 Then units(i,unit_ai) = False
Next
Next
;

; Unit move float
For i=0 To maxunits
unitsmove#(i) = ReadFloat(filein)
Next
;

; Unit string$
For i=0 To maxunits
For ii=0 To 5
unitstring$(i,ii) = ReadString(filein)
Next
Next
;

; Cities
For i=0 To maxcities
For ii=0 To 20
cities(i,ii) = ReadString(filein)
Next
Next

; Citystring
For i=0 To maxcities
citiesstring$(i,0) = ReadString(filein)
Next

; Roadmap
For i=0 To mapwidth
For ii = 0 To mapheight
roadmap(i,ii) = ReadInt(filein)
Next
Next

; Roadmapstring
For i=0 To mapwidth
For ii=0 To mapheight 
roadmapstring$(i,ii) = ReadString(filein)
Next
Next

; Map
For i=0 To mapwidth
For ii=0 To mapheight
map(i,ii) = ReadInt(filein)
Next
Next

For i=0 To mapwidth
For ii=0 To mapheight
fortressmap(i,ii) = ReadInt(filein)
Next
Next


For i=0 To 8
For ii=0 To 8
warstate(i,ii) = ReadInt(filein)
Next
Next


; Fog of war
For i=0 To 8
For x=0 To 100
For y=0 To 100
vismap(x,y,i) = ReadInt(filein)
Next
Next
Next

; Mines
For x=0 To 100
For y=0 To 100
For i=0 To 5
minemap(x,y,i) = ReadInt(filein)
Next
Next
Next

; Player city conquest history
For i=0 To maxcities
For ii=0 To 2
playercityconquest(i,ii) = ReadInt(filein)
Next
Next

; Coast lines
For x=0 To mapwidth*2
For y=0 To mapheight*2
coastlinemap(x,y) = ReadInt(filein)
Next
Next

; Trade status
For i=1 To 8
For ii=1 To 8
tradestatus(i,ii) = ReadInt(filein)
Next
Next

; Player relations
For i=1 To 8
For ii=1 To 8
For iii=0 To 10
playerrelations(i,ii,iii) = ReadInt(filein)
Next
Next
Next


; Fog of war
fogofwarenabled = ReadInt(filein)
For x=0 To mapwidth
For y=0 To mapheight
For i=1 To 8
vismap(x,y,i) = ReadInt(filein)
Next
Next
Next


; Write the map data
For x=0 To 100
	For y=0 To 100
		amap(x,y) = ReadInt(filein)
		beachmap(x,y) = ReadInt(filein)
		islandmap(x,y) = ReadInt(filein)
		mg_citymap(x,y) = ReadInt(filein)
		bewoondterrein(x,y) = ReadInt(filein)
	Next
Next





CloseFile(filein)
End Function


;
; This function resets the game data
;
Function freegamedata()

;Free the mine incomes
For x=0 To mapwidth
For y=0 To mapheight
minemap(x,y,0) = False
Next
Next

; Free the path arrays

For i=0 To numshortpaths
shortpathmapactive(i) = False
Next
For i=0 To nummediumpaths
mediumpathmapactive(i) = False
Next
For i=0 To numlongpaths
longpathmapactive(i) = False
Next

; Reset this just to be sure ( also related to the path arrays
For i=0 To maxmissions
aimission(i) = False
aimissionpathpointer(i,0) = False
aimissionpathpointer(i,1) = False
Next

; Erase the city conquest history list
For i=0 To maxcities
For ii=0 To 2
playercityconquest(i,ii) = False
Next
Next



End Function