;
;
;
;
;

Graphics 800,600,16,2
SetBuffer BackBuffer()

Dim genmap(100,100)
Dim backmap(100,100)

SeedRnd MilliSecs()

generatemap(40) ; landmass



While KeyDown(1) = False
Cls
If KeyDown(2) = True Then generatemap(Rand(25,90)) 
drawmap()
;drawmap2()
Flip
Wend
End


Function makeforrestlayer()
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
If genmap(x,y) = 3 And backmap(x,y) = 0 Then genmap(x,y) = 4
Next
Next



End Function

Function generatemap(landmass)
generaterawmap(landmass)
makeforrestlayer()
End Function

Function generaterawmap(landmass)
	
	While exitloop = False
		
		For x=0 To 100
			For y=0 To 100
				genmap(x,y) = 0
			Next
		Next
		
		genmap(0,0) = 128;    {Set up the 4 corners of the screen} 
		genmap(100,0) = 128; 
		genmap(100,100) = 128; 
		genmap(0,100) = 128; 
		 
		subdivide(0,0,100,100)
		
		counter = 0
		For x1=0 To 100
			For y1=0 To 100
				If genmap(x1,y1) > 128 Then counter = counter + 1
			Next
		Next
		
		If counter > (landmass*100)-250 And counter < (landmass*100)+250 Then
			exitloop = True
		End If
		
	Wend

	highval = gethigh()
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

Function gethigh()
For x=0 To 100
For y=0 To 100
If rv < genmap(x,y) Then rv = genmap(x,y)
Next
Next
Return rv
End Function

Function drawmap2()
For x=0 To 800 Step 4
For y=0 To 600 Step 3
If backmap(x/4,y/3) > 0 Then
Rect x,y,4,3,1
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

;If z = 10 Then a = 211 : b = 0 : c = 0 ; trees
;If z = 11 Then a = 251 : b = 0 : c = 0 ; trees
;If z = 12 Then a = 255 : b = 0 : c = 0 ; trees


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

