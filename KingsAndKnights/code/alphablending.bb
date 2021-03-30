
Dim pixelData(500, 500)
;
;Graphics 800,600,16,2
;SetBuffer BackBuffer()
;
;Global isodesert = LoadImage("../graphics/isodesert.bmp")
;Global isocity = LoadImage("../graphics/city01.bmp")
;
;If isodesert = False Then End
;If isocity=False Then End
;
;
;poep1 = CreateImage(100,100,2)
;poep2 = CreateImage(100,100,2)
;zeik = CreateImage(100,100)
;
;SetBuffer ImageBuffer(poep1)
;ClsColor 100,100,100
;Cls
;Color 200,0,200
;Rect 0,0,100,20,1
;SetBuffer ImageBuffer(poep2)
;ClsColor 200,200,200
;Cls
;Color 50,0,150
;Rect 50,0,60,100
;
;SetBuffer BackBuffer()
;
;
;;Function drawimagealpha(src,dest,x,y,a#,maskr,maskg,maskb,op)
;
;fpscounter = 0
;fpstimer = MilliSecs()
;fps = 60
;
;;timer = CreateTimer(60)
;cnt# = .50
;Color 255,255,255
;stepzoep = 1
;While KeyDown(1) = False
;Cls
;;drawrotatedimage(isodesert,0+ImageWidth(isodesert)/2,0,-90)
;;drawscaledimage(isodesert,100,100,.50,.50)
;
;Color 100,100,100
;x = 100
;y = 100
;Rect x,y,100,100,0
;
;Text 0,0,rotstep
;
;Color 255,0,255
;Rect 50+x,50+y,2,2,1
;Color 100,200,250
;;If Rand(0,10) = 1 Then zoep = Rand(-180,180)
;zoep = zoep + stepzoep
;;zoep = -45
;If zoep > 180 Then stepzoep = -stepzoep
;If zoep <-180 Then stepzoep = -stepzoep
;For xx=0 To 100 Step 10
;For yy=0 To 100 Step 10
;For i=0 To 360 Step 20
;zzx# = rotatenumberx(0+xx,0+yy,zoep,100,100)/1.6;:Text 200,0,zzx
;zzy# = rotatenumbery(0+xx,0+yy,zoep,100,100)/1.6;:Text 200,20,zzy
;Oval  zzx+x+50,zzy+y+50,2,2
;Next
;Next
;Next
;
;;Oval zzx+x,zzy+y,ii,ii
;
;
;Delay(1)
;Flip(False)
;
;fpscounter = fpscounter + 1
;If MilliSecs()>fpstimer Then
;fpstimer = MilliSecs() + 1000
;fps = fpscounter
;fpscounter = 0
;End If
;Wend
;End
;;
;
;
; Wrapper for the alpha blit shit Limits to 100x50 images
;
Function drawunitalpha(source,destbuf,x,y,alpha#)
		SetBuffer ImageBuffer(alphabuffer)
		ClsColor 0,0,0:Cls
		DrawBlockRect destbuf,0,0,x,y,ImageWidth(source),ImageHeight(source)
		alphabitblt(source,alphabuffer,0,0,alpha#,135,83,135)								
		SetBuffer ImageBuffer(destbuf)
		DrawImage alphabuffer,x,y								
End Function

Function AlphaBitBlt(Image,Destbuffer, xOff, yOff, Alpha#,maskr,maskg,maskb)
	;This function alpha blits any image smaller than 100,50 onto a desitantion
	;buffer of equal or lesser size, It has no error handling to speed up the 
	;function.
	
	;DestBuffer is the pointer to the desitnation image.
	;ImgBuffer is the pointer to the image to be Alpha Blited.
	;xOff, yOff are the ofsets of the image on the destination image.
	;Alpha ranges from 0 to 1 and is the opacity of the image.

	If ImageWidth(image) > 100 Then Notify "Alphabitblt function error" : End
	If ImageWidth(destbuffer) >100 Then Notify "Alphabitblt function error" : End

	InvAlpha# 	= 1.0 - Alpha#
	ImgBuffer 	= ImageBuffer(Image)
	iWidth 		= ImageWidth(Image)
	iHeight 	= ImageHeight(Image)
	
	LockBuffer ImgBuffer
		For x = 0 To iWidth-1
			For y = 0 To iHeight-1
				pixelData(x, y) = ReadPixelFast(x,y,ImgBuffer)
			Next
		Next
	UnlockBuffer ImgBuffer
	LockBuffer ImageBuffer(DestBuffer)
		For x = 0 To iWidth-1
			For y = 0 To iHeight-1
				If Not (getr(pixeldata(x,y)) = maskr And getg(pixeldata(x,y)) = maskg And getb(pixeldata(x,y)) = maskb) Then
					PixA# = ReadPixelFast(x + xOff, y + yOff)
					PixB# = pixelData(x, y)
					
					;Calculate Alphas with the least number of computations
					aR = Int(PixA Sar 16)
					aRI = aR Shl 16
					aG = Int((PixA - aRI) Sar 8)
					aB = PixA - (aRI + (aG Shl 8))
	
					;Calculate Alphas with the least number of computations
					bR = Int(PixB Sar 16)
					bRI = bR Shl 16
					bG = Int((PixB - bRI) Sar 8)
					bB = PixB - (bRI + (bG Shl 8))
					
					;Apply Alphas
					If bR > 0 Or bG > 0 Or bB > 0
						nR = (aR * InvAlpha) + (bR * Alpha)
						nG = (aG * InvAlpha) + (bG * Alpha)
						nB = (aB * InvAlpha) + (bB * Alpha)
					
						;Write Pixel to Buffer
						WritePixelFast x + xOff, y + yOff, (nR Shl 16) + (nG Shl 8) + nB
					EndIf
				End If
			Next
		Next
	UnlockBuffer ImageBuffer(DestBuffer)
End Function


Function DrawRotatedImage(image,x#,y#,angle#,pixelation#=1,frame=0)

	;Draws an image at any rotation.
	;The pixelation property increases speed but decreases quality, by
	;copying larger chunks of rects. Experiment.
	
	Local u1#,u2#,p#=pixelation
	If p<1 Then p=1
	iw=ImageWidth(image) :xh = ImageXHandle(image)
	ih=ImageHeight(image) :yh = ImageYHandle(image)
	sang#=Sin(angle)
	cang#=Cos(angle)
	ix=0
	totaltime = MilliSecs()
	While ix<iw
		u1 = cang * (ix-xh):u2 = -sang * (ix-xh)
		iy=0
		While iy<ih
			DrawImageRect image,(u1+sang * (iy-yh))+x-p*.75,(u2+cang*(iy-yh))+y-p*.75,ix-p*.75,iy-p*.75,p*1.5,p*1.5,frame
			iy=iy+p
		Wend
		ix=ix+p
	Wend
	Return 0
	
End Function

Function rotatenumberx(x#,y#,angle#,w#,h#)
	Local u1#,u2#,p#=pixelation
	If p<1 Then p=1
	p=1
	iw=w;ImageWidth(image) :xh = ImageXHandle(image)
	ih=h;ImageHeight(image) :yh = ImageYHandle(image)
	xh# = w/2.5
	yh# = h/2.5
	sang#=Sin(angle)
	cang#=Cos(angle)
	ix=0	
	ix = x
	iy = y
	;While ix<iw
		u1 = cang * (ix-xh):u2 = -sang * (ix-xh)
	;	iy=0
		
	;	While iy<ih
	;		If ix = x And iy = y Then Return (u1+sang * (iy-yh))
	Return (u1+sang * (iy-yh))
			;DrawImageRect image,(u1+sang * (iy-yh))+x-p*.75,(u2+cang*(iy-yh))+y-p*.75,ix-p*.75,iy-p*.75,p*1.5,p*1.5,frame
	;		iy=iy+p
	;	Wend
	;	ix=ix+p
	;Wend
	;Return (u1+sang * (iy-yh))+x-p*.75
End Function

Function rotatenumbery(x#,y#,angle#,w#,h#)
	Local u1#,u2#,p#=pixelation
	If p<1 Then p=1
	p=1
	iw=w;ImageWidth(image) :
	ih=h;ImageHeight(image) :
	xh# = w/2.5
	yh# = h/2.5
	sang#=Sin(angle)
	cang#=Cos(angle)	
	ix=x
	iy=y
	;While ix<iw
		u1 = cang * (ix-xh):u2 = -sang * (ix-xh)
		;iy=0
	;	While iy<ih
			;If ix = x And iy = y Then Return (u2+cang*(iy-yh))
			Return (u2+cang*(iy-yh))
			;DrawImageRect image,(u1+sang * (iy-yh))+x-p*.75,(u2+cang*(iy-yh))+y-p*.75,ix-p*.75,iy-p*.75,p*1.5,p*1.5,frame
		;	iy=iy+p
	;	Wend
		;ix=ix+p
	;Wend
	;Return (u2+cang*(iy-yh))+y-p*.75

End Function


Function DrawScaledImage(image,x#,y#,sx#,sy#,pixelation=1,frame=0)
	;Draws an image at ANY scale. Scales upwards and downwards!
	;The pixelation property increases speed but decreases quality, by
	;copying larger chunks of rects. Experiment.

	If pixelation<1 Then pixelation=1
	iw=ImageWidth(image):xh = ImageXHandle(image)
	ih=ImageHeight(image):yh = ImageYHandle(image)
	ix=0
	While ix<iw*sx
		iy=0
		While iy<ih*sy
			DrawImageRect image,x+(ix)*Sgn(sx),y+(iy)*Sgn(sy),ix/Abs(sx),iy/Abs(sy),pixelation*1.5,pixelation*1.5
			iy=iy+pixelation
		Wend
		ix=ix+pixelation
	Wend
End Function
; ================================================
; Small Alpha routines by Mike of Sunteam Software
; ================================================

; Modified by Matt Sephton (July 2003)
;
;Global alphamw,alphamh
;
; drawimagealpha
;
; Params: srcimage handle (does not need locking and will be unlocked after),destbuffer
;(does Not need locking And will be unlocked After),x,y,alpha level (0-1 as Float), mask
;red, mask green, mask blue, operation (-1 For sub, 1 For add)
; NB: srcimage handle is an image handle not a buffer handle like dest buffer is!
; NBB: the mask is an int value in r,g,b representing the colour to be ignored (masked)
Function drawimagealpha(src,dest,x,y,a#,maskr,maskg,maskb,op)
	w = ImageWidth(src)-1
	h = ImageHeight(src)-1
	LockBuffer ImageBuffer(src)
	LockBuffer dest
	t.alpha = New alpha
	u.alpha = New alpha
	mr = maskr
	mg = maskg
	alphamw=GraphicsWidth()-1
	alphamh=GraphicsHeight()-1
	For o = 0 To h
		For n = 0 To w
			readargb(t,ImageBuffer(src),n,o)
			If Not (t\r = mr And t\g = mg And t\b = maskb)
				readargb(u,dest,x+n,y+o)
				
				If (op = 0) Then
					;multiply blend
					;dest = dest + (source - dest) * alpha
					u\r = u\r + (t\r - u\r) * a
					u\g = u\g + (t\g - u\g) * a
					u\b = u\b + (t\b - u\b) * a
				Else
					adjustalpha(t,a#,a#,a#)
					u\r = (u\r + (t\r*op))
					u\g = (u\g + (t\g*op))
					u\b = (u\b + (t\b*op))
				EndIf

				; check And remove any rollover
				;If u\r > $FF Then 
				;	u\r = $FF
				;ElseIf u\r < $0 
				;	u\r = $0
				;EndIf
				;If u\g > $FF Then
				;	u\g = $FF
				;ElseIf u\g < $0
				;	u\g = $0
				;EndIf
				;If u\b > $FF Then
				;	u\b = $FF
				;ElseIf u\b <$0
				;	u\b = $0
				;EndIf
				
				; faster rollover check
				u\r = u\r And $FF
				u\g = u\g And $FF
				u\b = u\b And $FF

				writeargb(u,dest,x+n,y+o)
			EndIf
		Next
	Next
	Delete t
	Delete u
	UnlockBuffer ImageBuffer(src)
	UnlockBuffer dest
End Function

; alpha type (red,green,blue as 2 bit ints)
Type alpha
	Field r,g,b
End Type


; readargb
;
; Params: a as an alpha type (see below), buffer to be read from (must be locked before), x position of pixel, y position of pixel
Function readargb(a.alpha,buffer,x,y)
	rrgb=ReadPixelFast(x,y,buffer)
	a\r = (rrgb And $FF0000) Shr 16
	a\g = (rrgb And $00FF00) Shr 8
	a\b = (rrgb And $0000FF)
End Function

; writeargb
;
; Params: a as an alpha type (see below), buffer to write to (must be locked before), x position of pixel, y position of pixel
Function writeargb(a.alpha,buffer,x,y)
	If x>=0 And y>=0 And x<=alphamw And y<=alphamh Then
		argb = (a\r Shl 16) + (a\g Shl 8) + a\b
		WritePixelFast x,y,argb,buffer
	EndIf
End Function

; adjustalpha
;
; Params: a as an alpha type, red value (0-1 as float), green value (0-1 as float), blue value (0-1 as float)
Function adjustalpha(a.alpha,r#,g#,b#)
	a\r = (a\r*r#)
	a\g = (a\g*g#)
	a\b = (a\b*b#)
End Function

;Standard functions for converting colour to RGB values, for WritePixelFast and ReadPixelFast
Function GetRGB(r,g,b)
	Return b Or (g Shl 8) Or (r Shl 16)
End Function

Function GetR(rgb)
    Return rgb Shr 16 And %11111111
End Function

Function GetG(rgb)
	Return rgb Shr 8 And %11111111
End Function

Function GetB(rgb)
	Return rgb And %11111111
End Function

