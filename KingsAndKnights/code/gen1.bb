;
; Map generation work code - Experimental code
;
;
;

Graphics 800,600,16,2
SetBuffer BackBuffer()

Dim pathmap(100,100)

Include "isopath.bb"

Global numcontinents = 0

Dim sparemap(100,100)
Dim genmap(100,100)
Dim backmap(100,100)
Dim islandmap(100,100) ; holds the continents
Dim amap(100,100) ; holds the number of the continents
Dim beachmap(100,100) ; Holds the beach information
Dim continentmass(100) ; Holds the masses for the continents
Dim mg_citymap(100,100)
Dim bewoondterrein(100,100)
Dim mg_roadmap(100,100)

Dim citiesgrid(1000,5) ; player , x, y


SeedRnd MilliSecs()

Global backgroundmap = CreateImage(800,600)

;generatemap(40,True) ; landmass

Global hicolor = 120

generatecountry(150)


drawmap3

While KeyDown(1) = False
Cls
;If KeyDown(57) = True Then generatecountry()
If KeyDown(3) = True Then hicolor = hicolor + 1 Delay(20)  : drawmap3 : If hicolor > 255 Then hicolor = 255
If KeyDown(2) = True Then hicolor = hicolor - 1 Delay(20)  : drawmap3 : If hicolor < 0 Then hicolor = 0
If KeyDown(15) = True Then invertmap() : drawmap3
If KeyDown(57) = True Then generatecountry(150) : drawmap3
If KeyDown(59) = True Then mg_findcontinents(): drawmap3

DrawBlock backgroundmap,0,0

If MouseDown(1)= True Then drawmap4

; View some values on the amap array
;If MouseDown(1) = True Then imap()

Color 255,255,255
Text 0,256   ,"Hide color value : " + hicolor
Text 0,256+12,"Number of continents : " + numcontinents

Flip
Wend
End

Function generatecountry(intensity)

numcontinents = 0

timed = MilliSecs()
While numcontinents < 2 

For x=0 To 100
	For y=0 To 100
		backmap(x,y) = 0
		genmap(x,y) = 0
		mg_citymap(x,y) = 0
	Next	
Next

generatemap(Rand(20,60))
mg_makeforrestlayer()
mg_findcontinents()
;counter = counter + 1
Wend

; Show some info when the pc is slow :D
If timed - MilliSecs() < -150 Then showminorstuff = False Else showminorstuff = True
;Delay(200)
;FlushKeys
;mg_message("Making roads ( Press any key to continue ) - continents = " + numcontinents)
;Cls
;Flip
maakbewoonbaarterrein(10)


For i=1 To numcontinents


mg_numcities = continentmass(i) / intensity ;150
mg_numports = continentmass(i) / intensity/6 ;25

If mg_numcities < 4 Then mg_numcities = 4
If mg_numports < 2 Then mg_numcities = 2


;If continentmass(i) > 4 Then
;mg_message("loop "+i+" landmass : " + continentmass(i))
counter = 0
assignedcities = 0
dropvanvenco = 0
;For ii=0 To mg_numcities - 1
ii=0
loopdrip = 0
While ii < mg_numcities
	If loopdrip > 150 Then ii = mg_numcities
	If showminorstuff = True Then mg_progressbar("Creating City "+ii+" of " + mg_numcities,ii,mg_numcities)
	
	For x=0 To 100
	For y=0 To 100
;	If dropvanvenco < mg_numcities-1 + mg_numports - 1
	If amap(x,y) = i Then
	If bewoondterrein(x,y) = 1 Then

	If Rand(60) = 1 Then	
	If beachmap(x,y) = 1 Then
		; See if the direct surounding is already settled or if the terrain is ok
		; Scan the direct area
		isalreadysettled = False
		difficult = 0
		cansettle = False
		For x1=x-2 To x+2
		For y1=y-2 To y+2
		If RectsOverlap(x1,y1,1,1,0,0,100,100) = True Then
		If mg_citymap(x1,y1) > 0 Then isalreadysettled = True
		If Not genmap(x1,y1) = 3 Then difficult = difficult + 1
		End If
		Next
		Next
		;
		If genmap(x,y) = 3 And difficult > 3 Then cansettle = True 
		; Make the city
		;cansettle = True
		If isalreadysettled = False And cansettle = True Then
		dropvanvenco = dropvanvenco + 1
		mg_citymap(x,y) = amap(x,y)
		ii=ii+1
		End If
	End If
	End If
	
	If Rand(60) = 1 Then
	; See if the direct surounding is already settled
	isalreadysettled = False
	difficult = 0
	cansettle = False
	For x1=x-2 To x+2
	For y1=y-2 To y+2
	If RectsOverlap(x1,y1,1,1,0,0,100,100) = True Then
	If mg_citymap(x1,y1) > 0 Then isalreadysettled = True
	If Not genmap(x1,y1) = 3 Then difficult = difficult + 1
	End If
	Next
	Next
	If genmap(x,y) = 3 And difficult > 3 Then cansettle = True
;	; Make the city
	If isalreadysettled = False And cansettle = True Then
	dropvanvenco = dropvanvenco + 1
	mg_citymap(x,y) = amap(x,y)
	ii=ii+1
	End If
	End If

	End If
	End If
;	End If
	Next
	Next
	loopdrip = loopdrip + 1
Wend
;Next

;End If

Next


mg_makeroadnetwork()






End Function

Function maakbewoonbaarterrein(level)
If hicolor = 0 Then hicolor = 128

; Erase the map
For x=0 To 100
	For y=0 To 100
		bewoondterrein(x,y) = 0
	Next
Next

; Assign the livable space
For x=0 To 100
	For y=0 To 100
		If backmap(x,y) > hicolor And backmap(x,y) < hicolor + level Then
			bewoondterrein(x,y) = 1
		End If
	Next
Next

End Function

;
; Create continents and isolate beaches
;
;
Function mg_findcontinents()
; Erase the needed maps
For x=0 To 100
	For y=0 To 100
		islandmap(x,y) = 0
		amap(x,y) = 0
		beachmap(x,y) = 0
	Next
Next
; erase the continent map
For x=0 To 100
	For y=0 To 100
		If backmap(x,y) > hicolor Then islandmap(x,y) = 1
	Next
Next

; Isolate each continent and assign them a value
counter = 1
For x=0 To 100
	For y=0 To 100
		If islandmap(x,y) = 1 Then
			If amap(x,y) = 0 Then
				rc(x,y,counter)
				counter = counter + 1
			End If
		End If
	Next
Next


; Count the continents masses and delete those who are to small
counter2 = 0
lastlargecont = 0
For i=1 To counter
	counter2 = 0
	For x=0 To 100
		For y=0 To 100
			If amap(x,y) = i Then counter2 = counter2 + 1
		Next
	Next
	; Erase the continent
	If counter2 < 15 Then
		; Erase it
		For x=0 To 100
			For y=0 To 100
				If amap(x,y) = i Then amap(x,y) = 0
			Next
		Next
			
	Else
		; Copy last
		continentmass(lastlargecont+1) = counter2
		lastlargecont = lastlargecont + 1
		For x=0 To 100
			For y=0 To 100
				If amap(x,y) = i Then amap(x,y) = lastlargecont
			Next
		Next
		
	End If
Next

; Count the continents
counter = 0
a = 0
For x=0 To 100
	For y=0 To 100
		If a < amap(x,y) Then a = amap(x,y) 
	Next
Next
numcontinents = a

;mg_message("New continents : " + (lastlargecont))

; Make the beach map
For x=0 To 100
	For y=0 To 100
		leverworst = 0
		For x1 = x-1 To x+1
			For y1 = y-1 To y+1
				If RectsOverlap(x1,y1,1,1,0,0,100,100) = True Then
				
				If islandmap(x,y) > 0 Then
					If islandmap(x1,y1) = 0 Then leverworst = leverworst + 1
				End If
				
				End If
			Next
		Next
		If leverworst > 0 Then beachmap(x,y) = 1
	Next
Next

;imap() ; large map view
End Function

; Floodfill
Function rc(x,y,cont)
If RectsOverlap(x,y,1,1,0,0,100,100) = False Then Return
If islandmap(x,y) = 0 Then Return
If amap(x,y) = cont Then Return
amap(x,y) = cont

rc(x+1,y,cont)
rc(x+1,y+1,cont)
rc(x,y+1,cont)
rc(x-1,y,cont)
rc(x-1,y-1,cont)
rc(x,y-1,cont)

End Function


; Scrolling preview map (shows numbers assigned to continents
Function imap()
FlushMouse()
Delay(100)
mx = 0
my = 0
MoveMouse(0,0)
While KeyDown(1) = False
	Cls
	If MouseDown(1) = True Then Return
	mx = MouseX()/4
	my = MouseY()/4
	For x=0 To 100
	For y=0 To 100
		If RectsOverlap(x+mx,y+my,1,1,0,0,100,100) = True Then
		Color amap(x+mx,y+my),50,50
		Rect (x*25),(y*10),25,10
		Color 255,255,255
		Text (x*25),(y*10),amap(x+mx,y+my)
		End If
	Next
	Next
	Flip
Wend
Delay(200)
FlushKeys()
End Function

Function drawmap3()
SetBuffer ImageBuffer(backgroundmap)
Cls
For x=0 To 100
For y=0 To 100
; Draw the backmap
Color 0,backmap(x,y),0
If backmap(x,y) < hicolor Then Color 0,0,0
Rect x*2,y*2,2,2
; Draw the raw map
If islandmap(x,y) > 0 Then Color 255,255,255 
If islandmap(x,y) = 0 Then Color 0,0,0
Rect (x*2)+220,(y*2),2,2
; Draw the beaches on the continents map
If beachmap(x,y) > 0 Then
Color 255,255,0
Rect (x*2)+220,(y*2),2,2
End If



; Draw the continent map
If amap(x,y) = 1 Color 100,100,0 
If amap(x,y) = 2 Color amap(x,y)*40,0,amap(x,y)*20 
If amap(x,y) = 3 Color amap(x,y)*60,0,amap(x,y)*20 
If amap(x,y) = 4 Color amap(x,y)*20,0,amap(x,y)*40 
If amap(x,y) = 5 Color amap(x,y)*20,0,amap(x,y)*60 
If amap(x,y) = 0 Then Color 0,0,0
Rect (x*2)+440,(y*2),2,2


; Draw the civilized land
If backmap(x,y) > hicolor Then
Color 0,0,255
Rect (x*4)+240,(y*3)+240,2,2
End If

; Draw the civilized land
If backmap(x,y) > hicolor And backmap(x,y) < hicolor + 12
Color 255,0,0
Rect (x*4)+240,(y*3)+240,2,2
End If

; draw the roads
If mg_roadmap(x,y) > 0 Then
Color 255,255,0
Rect (x*4)+240,(y*3)+240,4,4
End If

;draw the cities
If mg_citymap(x,y) > 0 Then
Color 255,255,255
Rect (x*4)+240,(y*3)+240,4,4
End If

;		If genmap(x,y) = 0 Then backmap(x,y) = 64 ; water
;		If genmap(x,y) = 1 Then backmap(x,y) = 1 ; hills
;		If genmap(x,y) = 2 Then backmap(x,y) = 2 ; mountains
;		If genmap(x,y) = 3 Then backmap(x,y) = 0 ; grass/main surface
;		If genmap(x,y) = 4 Then backmap(x,y) = 5 ; Trees



Next
Next
SetBuffer BackBuffer()
End Function


Function drawmap4()
For x=0 To 100
For y=0 To 100
; Draw the actual map
If genmap(x,y) = 3 Then Color 0,255,0 ; grass
If genmap(x,y) = 0 Then Color 0,0,255 ; water
If genmap(x,y) = 1 Then Color 100,50,0 ; hills
If genmap(x,y) = 2 Then Color 170,170,170 ; mountains
If genmap(x,y) = 4 Then Color 0,90,0 ; trees

Rect (x*6),(y*6)+340,6,6

; Draw the cities on the last map
If mg_citymap(x,y) > 0 Then Color 255,0,0
Rect (x*6)-3,(y*6)-3,6,6
; Draw the roads on the last map
If mg_roadmap(x,y) = True Then Color 0,0,0
Rect (x*6)-3,(y*6)-3,6,6

Next
Next
End Function

Function invertmap()
For x=0 To 100
For y=0 To 100
backmap(x,y) = 255 - backmap(x,y)
Next
Next
End Function



Function mg_makeforrestlayer()
backmap(0,0) = 128;    {Set up the 4 corners of the screen} 
backmap(100,0) = 128; 
backmap(100,100) = 128; 
backmap(0,100) = 128; 
subdivide2(0,0,100,100)
;
For x=0 To 100
For y=0 To 100
a = backmap(x,y)
For i=40 To 256 Step 8
For ii=0 To 2
If a = i+ii Then backmap(x,y) = 0
Next
Next
If a <40 Then backmap(x,y) = 1
Next
Next

For x=0 To 100
For y=0 To 100
If Rand(3) = 1 = False And genmap(x,y) = 3 And backmap(x,y) = 0 Then genmap(x,y) = 4
Next
Next

For x=0 To 100
For y=0 To 100
backmap(x,y) = sparemap(x,y)
Next
Next

End Function


Function generatemap(landmass)
mg_generaterawmap(landmass)
End Function

Function mg_generaterawmap(landmass)
	
	While exitloop = False
		
		For x=0 To 100
			For y=0 To 100
				genmap(x,y) = 0
			Next
		Next
		
		If landmass < 30 Then

		For x=0 To 50 Step 25
		For y=0 To 50 Step 25
		x1 = Rand(0,10)+x
		y1 = Rand(0,10)+y
		x2=x1+Rand(25,33)+x
		y2=y1+Rand(25,33)+y
		
		If x2 > 100 Then x2 = 100 
		If y2 > 100 Then y2 = 100 
		If x1 > 100 Then x1 = 80 
		If y1 > 100 Then y1 = 80 

		genmap(x1,y1) = Rand(88,128);    {Set up the 4 corners of the screen} 
		genmap(x2,y1) = Rand(88,128); 
		genmap(x2,y2) = Rand(88,128); 
		genmap(x1,y2) = Rand(88,128); 
		 
		othersubdivide(x1,y1,x2,y2)

		Next
		Next
		
		Else
		
		genmap(0,0) = 128;    {Set up the 4 corners of the screen} 
		genmap(100,0) = 128; 
		genmap(100,100) = 128; 
		genmap(0,100) = 128; 
		 
		subdivide(0,0,100,100)
		
		End If
		
		counter = 0
		For x1=0 To 100
			For y1=0 To 100
				If genmap(x1,y1) > 128 Then counter = counter + 1
			Next
		Next
		If landmass > 30 Then
		If counter > (landmass*100)-250 And counter < (landmass*100)+250 Then
			exitloop = True
		End If
		Else
		exitloop = True
		End If
		
	Wend

	For x=0 To 100
	For y=0 To 100
	backmap(x,y) = genmap(x,y)
	sparemap(x,y) = genmap(x,y)
	Next
	Next

	highval = mg_gethigh()
	; Convert the map to usable map format
	For x=0 To 100
		For y=0 To 100
			z = genmap(x,y)
			
			If z<=128 Then genmap(x,y) = 0 ;water
			
			;If z=132 Then genmap(x,y) = 10
			;If z=133 Then genmap(x,y) = 11
			If z > 120 Then 
			;	If (Not z = 125 And z=126) Then
					genmap(x,y) = 3 ; land
			;	End If
			End If
			If z > highval-10 Then genmap(x,y) = 1 ; hills 1

			;
			If z > highval-5 Then genmap(x,y) = 2 ; mountains
			
			;
			For i=125 To highval-10 Step 10
			a = Rand(0,1)
			If z = i Then
				If a = 0 Then
				;genmap(x,y) = 4
				Else
				genmap(x,y) = Rand(1,2)
				End If
			End If
			If z+1 = i Then
				If a = 0 Then
				;genmap(x,y) = 4
				Else
				genmap(x,y) = Rand(1,2)
				End If
			End If
			If z+2 = i Then
				If a = 0 Then
				;genmap(x,y) = 4
				Else
				genmap(x,y) = Rand(1,2)
				End If
			End If



			Next
			
			;
			If z = 125 Then genmap(x,y) = 4 ; trees
			If z = 126 Then genmap(x,y) = 4 ; trees
		;	
		Next
	Next
End Function

Function mg_convertmap()
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
;0 water
;1 hills
;2 mountain
;3 land
;4 trees
For x=0 To 100
	For y=0 To 100
		If genmap(x,y) = 0 Then backmap(x,y) = 64 ; water
		If genmap(x,y) = 1 Then backmap(x,y) = 1 ; hills
		If genmap(x,y) = 2 Then backmap(x,y) = 2 ; mountains
		If genmap(x,y) = 3 Then backmap(x,y) = 0 ; grass/main surface
		If genmap(x,y) = 4 Then backmap(x,y) = 5 ; Trees
	Next
Next

End Function
; Gets the highest value from the genmap
Function mg_gethigh()
For x=0 To 100
For y=0 To 100
If rv < genmap(x,y) Then rv = genmap(x,y)
Next
Next
Return rv
End Function

Function drawmap2()
Color 255,255,255
For x=0 To 100 
For y=0 To 100 
If backmap(x,y) > 0 Then
Rect x*4,y*3,4,3,1
End If
Next
Next
End Function

Function drawmap()
For x=0 To 800 Step 8
For y=0 To 600 Step 6
z = genmap(x/(8),y/(6))
If z = 0 Then a = 0 : b = 0 : c = 100 ; water
If z = 1 Then a = 160 : b = 100 : c = 50 ; hills
If z = 2 Then a = 155 : b = 155 : c = 155 ; mountains
If z = 3 Then a = 30 : b = 190 : c = 30 ; land
If z = 4 Then a = 111 : b = 215 : c = 94 ; trees

If z = 10 Then a = 211 : b = 0 : c = 0 ; trees
If z = 11 Then a = 251 : b = 0 : c = 0 ; trees
If z = 12 Then a = 255 : b = 0 : c = 0 ; trees


Color a,b,c
Rect x,y,8,6,1
Next
Next
End Function




Function SubDivide(x1,y1,x2,y2); 
 If (x2-x1<2) And (y2-y1<2) Then Return; 
;  {If this is pointing at just on pixel, Exit because 
;   it doesn't need doing} 
 
  dist=(x2-x1+y2-y1); {Find distance between points.  Use when generating a random number} 
  hdist=dist / 2; 
 
  midx=(x1+x2) / 2; {Find Middle Point} 
  midy=(y1+y2) / 2; 
 
  c1=Genmap(x1,y1); {Get pixel colors of corners} 
  c2=Genmap(x2,y1); 
  c3=Genmap(x2,y2); 
  c4=Genmap(x1,y2); 
 
;  { If Not already defined, work out the midpoints of the corners of 
;   the rectangle by means of an average plus a random number. } 
  If Genmap(midx,y1)=0 Then Genmap(midx,y1)=((c1+c2+Rand(dist)-hdist) / 2); 
  If Genmap(midx,y2)=0 Then Genmap(midx,y2)=((c4+c3+Rand(dist)-hdist) / 2); 
  If Genmap(x1,midy)=0 Then Genmap(x1,midy)=((c1+c4+Rand(dist)-hdist) / 2); 
  If Genmap(x2,midy)=0 Then Genmap(x2,midy)=((c2+c3+Rand(dist)-hdist) / 2); 
 
;  { Work out the middle point... } 
  genmap(midx,midy) = ((c1+c2+c3+c4+Rand(dist)-hdist) / 4); 
 
;  { Now divide this rectangle into 4, And call again For Each smaller 
;   rectangle } 
  SubDivide(x1,y1,midx,midy); 
  SubDivide(midx,y1,x2,midy); 
  SubDivide(x1,midy,midx,y2); 
  SubDivide(midx,midy,x2,y2); 
End Function

Function SubDivide2(x1,y1,x2,y2); 
 If (x2-x1<2) And (y2-y1<2) Then Return; 
;  {If this is pointing at just on pixel, Exit because 
;   it doesn't need doing} 
 
  dist=(x2-x1+y2-y1); {Find distance between points.  Use when generating a random number} 
  hdist=dist / 2; 
 
  midx=(x1+x2) / 2; {Find Middle Point} 
  midy=(y1+y2) / 2; 
 
  c1=backmap(x1,y1); {Get pixel colors of corners} 
  c2=backmap(x2,y1); 
  c3=backmap(x2,y2); 
  c4=backmap(x1,y2); 
 
;  { If Not already defined, work out the midpoints of the corners of 
;   the rectangle by means of an average plus a random number. } 
  If backmap(midx,y1)=0 Then backmap(midx,y1)=((c1+c2+Rand(dist)-hdist) / 2); 
  If backmap(midx,y2)=0 Then backmap(midx,y2)=((c4+c3+Rand(dist)-hdist) / 2); 
  If backmap(x1,midy)=0 Then backmap(x1,midy)=((c1+c4+Rand(dist)-hdist) / 2); 
  If backmap(x2,midy)=0 Then backmap(x2,midy)=((c2+c3+Rand(dist)-hdist) / 2); 
 
;  { Work out the middle point... } 
  backmap(midx,midy) = ((c1+c2+c3+c4+Rand(dist)-hdist) / 4); 
 
;  { Now divide this rectangle into 4, And call again For Each smaller 
;   rectangle } 
  SubDivide2(x1,y1,midx,midy); 
  SubDivide2(midx,y1,x2,midy); 
  SubDivide2(x1,midy,midx,y2); 
  SubDivide2(midx,midy,x2,y2); 
End Function

Function otherSubDivide(x1,y1,x2,y2); 
 If (x2-x1<2) And (y2-y1<2) Then Return; 
;  {If this is pointing at just on pixel, Exit because 
;   it doesn't need doing} 

  dist=(x2-x1+y2-y1); {Find distance between points.  Use when generating a random number} 

  ;hdist=dist / 2; 
  hdist=dist/2;
 
  midx=(x1+x2) / 2; {Find Middle Point} 
  midy=(y1+y2) / 2; 
 
  c1=Genmap(x1,y1); {Get pixel colors of corners} 
  c2=Genmap(x2,y1); 
  c3=Genmap(x2,y2); 
  c4=Genmap(x1,y2); 
 
;  { If Not already defined, work out the midpoints of the corners of 
;   the rectangle by means of an average plus a random number. } 
  If Genmap(midx,y1)=0 Then Genmap(midx,y1)=((c1+c2+Rand(dist)-hdist) / 2); 
  If Genmap(midx,y2)=0 Then Genmap(midx,y2)=((c4+c3+Rand(dist)-hdist) / 2); 
  If Genmap(x1,midy)=0 Then Genmap(x1,midy)=((c1+c4+Rand(dist)-hdist) / 2); 
  If Genmap(x2,midy)=0 Then Genmap(x2,midy)=((c2+c3+Rand(dist)-hdist) / 2); 
 
;  { Work out the middle point... } 
  genmap(midx,midy) = ((c1+c2+c3+c4+Rand(dist)-hdist) / 4); 
 
;  { Now divide this rectangle into 4, And call again For Each smaller 
;   rectangle } 
  SubDivide(x1,y1,midx,midy); 
  SubDivide(midx,y1,x2,midy); 
  SubDivide(x1,midy,midx,y2); 
  SubDivide(midx,midy,x2,y2); 
End Function

; show some info
;
Function mg_message(a$)
Cls
Color 255,255,255
Text 0,0,a$
Flip
While exitloop = False
For i=0 To 255
If KeyDown(i) = True Then exitloop = True
Next
If MouseDown(1) = True Then exitloop = True
Wend
FlushKeys()
Delay(150)

End Function



Function mg_makeroadnetwork()

map_width=100
map_height=100
MP_MAX_NODES = map_width*map_height
MP_MAX_WIDTH = map_width
MP_MAX_HEIGHT = map_height
;Dim pathmap(map_width,map_height)
Dim MP_Map.PQ_Node(MP_MAX_WIDTH,MP_MAX_HEIGHT)


For x=0 To 100
For y=0 To 100
pathmap(x,y) = 0
mg_roadmap(x,y) = 0
Next
Next

For ii=1 To numcontinents

For i=0 To 1000
citiesgrid(i,0) = False
citiesgrid(i,1) = 0 
citiesgrid(i,2) = 0 
citiesgrid(i,3) = 0
Next


mg_fillpathterrain()
mg_fillpathcivilarea()
mg_fillpathroads()
mg_fillpathsea()

counter = 0
For x=0 To 50
For y=0 To 100
	If mg_citymap(x,y) > 0 Then
	If amap(x,y) = ii Then 
	citiesgrid(counter,0) = True
	citiesgrid(counter,1) = ii
	citiesgrid(counter,2) = x
	citiesgrid(counter,3) = y
	counter = counter + 1
	End If
	End If
Next
Next



For y=0 To 100
For x=100 To 50 Step -1
	If mg_citymap(x,y) > 0 Then
	If amap(x,y) = ii Then 
	citiesgrid(counter,0) = True
	citiesgrid(counter,1) = ii
	citiesgrid(counter,2) = x
	citiesgrid(counter,3) = y
	counter = counter + 1
	End If
	End If
Next
Next

For x=0 To 100
For y=0 To 100
	If mg_citymap(x,y) > 0 Then
	If amap(x,y) = ii Then 
	citiesgrid(counter,0) = True
	citiesgrid(counter,1) = ii
	citiesgrid(counter,2) = x
	citiesgrid(counter,3) = y
	counter = counter + 1
	End If
	End If
Next
Next


For y=0 To 100
For x=0 To 100
	If mg_citymap(x,y) > 0 Then
	If amap(x,y) = ii Then 
	citiesgrid(counter,0) = True
	citiesgrid(counter,1) = ii
	citiesgrid(counter,2) = x
	citiesgrid(counter,3) = y
	counter = counter + 1
	End If
	End If
Next
Next


For i=0 To counter-1 Step 2
	If citiesgrid(i,0) = True And citiesgrid(i,1) = ii Then
	mg_progressbar("Creating roads for player " + ii + " out of " + numcontinents,i,counter)
	x = citiesgrid(i,2)
	y = citiesgrid(i,3)
	x2 = citiesgrid(i+1,2)
	y2 = citiesgrid(i+1,3)
	findpath(x,y,x2,y2,0)
	mg_converttoroadmap()
	mg_fillpathroads()
	End If
Next


Next


End Function


Function mg_converttoroadmap()
If as_outsize > 0 Then
For i=0 To as_outsize-1
If pathresult(i,0) = 0 And pathresult(i,1) = 0 Then 
Return
End If
mg_roadmap(pathresult(i,0),pathresult(i,1)) = True
Next
Else
End If
End Function


Function mg_fillpathcivilarea()
For x=0 To 100
For y=0 To 100
If bewoondterrein(x,y) = 0 Then
pathmap(x,y) = 8
End If
Next
Next
End Function

Function mg_fillpathroads()
For x=0 To 100
For y=0 To 100
If mg_roadmap(x,y) = 1 Then
pathmap(x,y)=0
End If
Next
Next
End Function

Function mg_fillpathsea()
For x=0 To 100
For y=0 To 100
If islandmap(x,y) = 0 Then pathmap(x,y) = 10
Next
Next
End Function

Function mg_fillpathterrain()
	For x=0 To 100
	For y=0 To 100
	Select genmap(x,y)
	Case 3 ; grass ;1
	pathmap(x,y) = 1
	Case 1 ; hills
	pathmap(x,y) = 3
	Case 2 ; mountain;3
	pathmap(x,y) = 4
;	Case 4 ; pines
;	pathmap(x,y) = number
;	Case 5 ; swamp
;	pathmap(x,y) = number
	Case 4 ; trees;6
	pathmap(x,y) = 3
;	Case 7 ; mine
;	pathmap(x,y) = number
;	Case 8 ; crops
;	pathmap(x,y) = number
;	Case 9 ; beaver
;	pathmap(x,y) = number
	End Select
	Next
	Next
End Function


Function mg_progressbar(bericht$,position,max)

Cls

Color 255,255,255

Rect 100,600/2-25,600,50,0

If position > 0 Then
step1 = 600/max
End If

Text 400,600/2-40,bericht$,1,1
Rect 100,600/2-25,0+step1*(position),50,1

Flip

End Function

; Draw the actual map
;If genmap(x,y) = 3 Then Color 0,255,0 ; grass
;If genmap(x,y) = 0 Then Color 0,0,255 ; water
;If genmap(x,y) = 1 Then Color 100,50,0 ; hills
;If genmap(x,y) = 2 Then Color 170,170,170 ; mountains
;If genmap(x,y) = 4 Then Color 0,90,0 ; trees



