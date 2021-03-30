Dim coordbuffer#(500,4)


Graphics 800,600,16,1
SetBuffer BackBuffer()


Global isodesert = LoadImage("isodesert.bmp")

Global screen_mapwidth = 10
Global screen_mapheight = 19

Global framecounter
Global framerate
Global frameratetimer

Global maplength

Color 0,0,0

framerate = 24
frameratetimer = MilliSecs()

makebuffer

Type buffer
Field ux,uy,dx,dy
End Type


While KeyDown(1) = False
Cls

;isox = 0 : isoy=10:ux = 0 : uy = 0
;For y=0 To screen_mapheight
;For x=0 To screen_mapwidth
;ux = isox + x:uy = isoy - x
;dx = x*64 : dy = y*32
;DrawImage isodesert,dx,dy ; Text dx+5,dy+5,ux+","+uy
;ux=ux+1
;dx = dx+32 : dy=dy+16
;DrawImage isodesert,dx,dy ; Text dx+5,dy+5,ux+","+uy
;Next
;isox=isox+1:isoy=isoy+1
;Next

;For i=0 To maplength-1
;ux = coordbuffer(i,0)
;uy = coordbuffer(i,1)
;dx = coordbuffer(i,2)
;dy = coordbuffer(i,3)
;DrawImage isodesert,dx,dy ; Text dx+5,dy+5,ux+","+uy
;ux=ux+1:dx=dx+32:dy=dy+16
;DrawImage isodesert,dx,dy ; Text dx+5,dy+5,ux+","+uy
;Next

For mine.buffer = Each buffer
ux = mine\ux
uy = mine\uy
dx = mine\dx
dy = mine\dy
DrawImage isodesert,dx,dy ; Text dx+5,dy+5,ux+","+uy
ux=ux+1:dx=dx+32:dy=dy+16
DrawImage isodesert,dx,dy ; Text dx+5,dy+5,ux+","+uy
Next

doframerate
Color 255,255,255:Text 0,0,framerate:Color 0,0,0

Flip
Wend

WaitKey
End


Function doframerate()
If frameratetimer+1000<MilliSecs() Then
frameratetimer = MilliSecs()
framerate = framecounter
framecounter = 0
Else
frameratetimer=frameratetimer+1
framecounter=framecounter+1
End If
End Function

Function makebuffer()
mine.buffer = First buffer
maplength=0:isox = 0 : isoy=10:ux = 0 : uy = 0
For y=0 To screen_mapheight
For x=0 To screen_mapwidth

ux = isox + x:uy = isoy - x
dx = x*64 : dy = y*32
coordbuffer(counter,0) = ux
coordbuffer(counter,1) = uy
coordbuffer(counter,2) = dx
coordbuffer(counter,3) = dy

mine.buffer = New buffer

mine\ux = ux
mine\uy = uy
mine\dx = dx
mine\dy = dy

counter=counter+1 :maplength=maplength+1
Next
isox=isox+1:isoy=isoy+1
Next


End Function