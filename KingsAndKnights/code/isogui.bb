;
; Gui interface code / / / / / By Nebula - R.v.Etten in 2001
;
; Made  for 800*600  screens. Glistbox  and  Gfilerequester  make  a
; copy of the frontbuffer and uses that as the background. You  only  need
; to add your own mouse pointer to the main loops (or background functions). 
; Use Search and look for the Rect commands which use the Mouse x/y 
; coordinates.
;
;
; This code will be updated..
;

;Graphics 800,600,16,1
;SetBuffer BackBuffer()








Global guiscale# = -25

Global cmarketscale# = 0
Global cupgradescale# = 0

Dim fonts(40)

For i=1 To 40
fonts(i) = LoadFont(verdana,i,True)
Next

Global gscalefont100 = LoadFont ("arial",23,True)
Global gscalefont50 = LoadFont ("arial",16,True)
Global gminifont25 = LoadFont ("arial",8,True)
Global gminifont20 = LoadFont ("arial",9,True)
Global gminifont15 = LoadFont ("arial",10,True)
Global gminifont = LoadFont ("arial",11,True)
Global gsmallfont = LoadFont ("arial",14,1)
Global grequesterfont = LoadFont("arial",18,1)
Global tutorialtitlefont = LoadFont("arial",32,1)

Global gback1 = LoadImage("graphics\gui\back1.bmp")
Global gback2 = LoadImage("graphics\gui\back2.bmp")
Global gback3 = LoadImage("graphics\gui\back3.bmp")
Global gback4 = LoadImage("graphics\gui\back4.bmp")
Global gback5 = LoadImage("graphics\gui\back5.bmp")
Global gback6 = LoadImage("graphics\gui\back6.bmp")
Global gback7 = LoadImage("graphics\gui\back7.bmp")
;Global gback2 = LoadImage("back2.bmp")
;Global gback3 = LoadImage("back3.bmp")





Const gmax_files = 500
Dim gfiles$(gmax_files)
Dim gfiletypes(gmax_files)


Global uparrow32 = LoadImage("graphics\buttons\up32.bmp")
Global leftarrow32 = LoadImage("graphics\buttons\left32.bmp")
Global downarrow32 = LoadImage("graphics\buttons\down32.bmp")
Global rightarrow32 = LoadImage("graphics\buttons\right32.bmp")
Global uparrow16 = LoadImage("graphics\buttons\up16.bmp")
Global leftarrow16 = LoadImage("graphics\buttons\left16.bmp")
Global downarrow16 = LoadImage("graphics\buttons\down16.bmp")
Global rightarrow16 = LoadImage("graphics\buttons\right16.bmp")

Global uparrowin32 = LoadImage("graphics\buttons\upin32.bmp")
Global leftarrowin32 = LoadImage("graphics\buttons\leftin32.bmp")
Global downarrowin32 = LoadImage("graphics\buttons\downin32.bmp")
Global rightarrowin32 = LoadImage("graphics\buttons\rightin32.bmp")
Global uparrowin16 = LoadImage("graphics\buttons\upin16.bmp")
Global leftarrowin16 = LoadImage("graphics\buttons\leftin16.bmp")
Global downarrowin16 = LoadImage("graphics\buttons\downin16.bmp")
Global rightarrowin16 = LoadImage("graphics\buttons\rightin16.bmp")

Global toparrow32 = LoadImage("graphics\buttons\top32.bmp")
Global bottomarrow32 = LoadImage("graphics\buttons\bottom32.bmp")
Global toparrowin32 = LoadImage("graphics\buttons\topin32.bmp")
Global bottomarrowin32 = LoadImage("graphics\buttons\bottomin32.bmp")
Global toparrow16 = LoadImage("graphics\buttons\top16.bmp")
Global bottomarrow16 = LoadImage("graphics\buttons\bottom16.bmp")
Global toparrowin16 = LoadImage("graphics\buttons\topin16.bmp")
Global bottomarrowin16 = LoadImage("graphics\buttons\bottomin16.bmp")

Global closebutton32 = LoadImage("graphics\buttons\Close32.bmp")
Global closebuttonin32 = LoadImage("graphics\buttons\Closein32.bmp")
Global closebutton16 = LoadImage("graphics\buttons\Close16.bmp")
Global closebuttonin16 = LoadImage("graphics\buttons\Closein16.bmp")


Const big_up_arrow = 0 ; 0 up 32
Const big_left_arrow = 1 ; 1 Left 32
Const big_down_arrow = 2 ; 2 down 32
Const big_right_arrow = 3 ; 3 right 32
;Const big_up_arrow = 4 ; 4 up in 32
;Const big_left_arrow = 5 ; 5 left in 32
;Const big_down_arrow = 6 ; 6 down in 32
;Const big_right_arrow = 7 ; 7 right in 32
Const small_up_arrow = 8 ; 8 up 16
Const small_left_arrow = 9 ; 9 left 16
Const small_down_arrow = 10 ; 10 down 16
Const small_right_arrow = 11 ; 11 right 16
;Const small_up_arrow = 12 ; 12 up in 16
;Const small_left_arrow = 13 ; 13 left in 16
;Const small_down_arrow = 14 ; 14 down in 16
;Const small_right_arrow = 15 ; 15 right in 16
Const big_top_button = 16 ; 16 top 32
Const big_bottom_button = 17 ; 17 bottom 32
;Const big_ = 18 ; 18 top in 32
;Const big_ = 19 ; 19 bottm in 32
Const small_top_button = 20 ; 20 top 16
Const small_bottom_button = 21 ; 21 bottom 16
;Const small_ = 22 ; 22 top in 16
;Const small_ = 23 ; 23 bottom in 16
Const big_close_button = 24
Const small_close_button = 26



Dim gfield$(100)


Dim glistfield$(1000)
Dim glistfieldint(1000)

Dim gtextfield1$(1024)
Dim gtextfield2$(2048)


;
; Input box
;
;
Function ginputbox$(x,y,w,h,gtitle$,gdefault$,gbutton1$)
SetFont grequesterfont

SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()


textthing$ = gdefault$
While KeyHit(1) = False
DrawBlock gbackgroundimage,0,0
gwindow(x,y,w,h,1,0,0)
gwindow(x+4,y+22,w-8,h-74,0,1,1)
Color 0,0,0 : Text x+10,y+2,gtitle$


gwindow(x+8,y+60,w-16,24,0,1,3)

Color 255,255,255
Text x+10,y+62,textthing$
Rect x+10+StringWidth(textthing$),y+64,10,14,1

n = GetKey()
If n>31 And n<256 Then
If StringWidth(textthing$)<w-32 Then
textthing$ = textthing$ + Chr(n)
End If
End If
If n = 8 And Len(textthing$)>0 Then 
textthing$ = Left(textthing$,Len(textthing$)-1)
End If
If n=13 Then 
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
If Len(textthing$) = 0 Then Return False
Return textthing$
End If

; Draw the button
If gbutton1(x+w/2-40,y+h-50,80,40,"Ok",1,gbuttonpressed) = True Then gbuttonpressed = 1
SetFont grequesterfont
; Execute the button
If mousedown(1) = False And gbuttonpressed>0 Then
If gcheckarea(x+w/2-40,y+h-50,80,40)=True And gbuttonpressed = 1 Then
FreeImage gbackgroundimage
gwait(200):FlushKeys:FlushMouse
If Len(textthing$) = 0 Then Return False
Return textthing$
End If
gbuttonpressed = 0
End If

; Draw the mouse
Color 0,0,0
Rect MouseX(),MouseY(),10,10,1

Flip
Wend
Return False
FreeImage gbackgroundimage
gwait(100) : FlushKeys:FlushMouse
End Function


;
; request box 
;
;
Function grequest(x,y,w,h,gtitle$,grequest1$,grequest2$,grequest3$,gbutton1$,gbutton2$)

; Grab the foreground
SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()

FlushKeys:FlushMouse

While KeyHit(1) = False
Cls
DrawBlock gbackgroundimage,0,0
; Draw the window
gwindow(x,y,w,h,1,0,0)
gwindow(x+3,y+23,w-6,h-80,0,1,2)

SetFont grequesterfont

; Draw the requester title
Color 0,0,0
Text x+5,y,gtitle$
; Draw the request text 
Text x+w/2,y+40,grequest1$,1,1
Text x+w/2,y+60,grequest2$,1,1
Text x+w/2,y+80,grequest3$,1,1

; Execute the keyshortcuts
; If the left character is a 'o' and if 'o' is pressed
If Lower(Left(gbutton1$,1)) = "o" Then
If KeyHit(24) Then 
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return True
End If
End If
; if the left character is a 'n' and if 'n' is pressed
If Lower(Left(gbutton2$,1)) = "n" Then
If KeyHit(49) Then 
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return False
End If
End If
; if the left character is 'y' and the 'y' is pressed
If Lower(Left(gbutton1$,1))="y" Then
If KeyHit(21) Then 
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return True
End If
End If
; if the left character is 'c' and the 'c' is pressed
If Lower(Left(gbutton2$,1)) = "c" Then
If KeyHit(46) Then 
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return False
End If
End If
; If enter is pressed
If KeyHit(28) Then 
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return True
End If

; Draw the buttons
SetFont gsmallfont
If gbutton1$ > "" And gbutton2$ > "" Then
	If gbutton2(x+10,y+h-50,80,40,gbutton1$,1,1,gbuttonpressed)=True Then gbuttonpressed = 1
	If gbutton2((x+(w-80)-10),y+h-50,80,40,gbutton2$,1,2,gbuttonpressed) = True Then gbuttonpressed = 2
End If
If gbutton2$="" Then
	If gbutton2((x+w/2)-40,y+h-50,80,40,gbutton1$,1,1,gbuttonpressed)=True Then gbuttonpressed = 1
End If

; Execute the buttons
If mousedown(1) = False And gbuttonpressed > 0 Then
;
;If there are two buttons
If gbutton1$ > "" And gbutton2$ > "" Then
If gcheckarea(x+10,y+h-50,80,40)=True And gbuttonpressed = 1 Then 
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return True
End If
If gcheckarea((x+(w-80)-10),y+h-50,80,40)=True And gbuttonpressed = 2 Then
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return False
End If
End If
;If there is one button
If gbutton2$="" Then
If gcheckarea((x+w/2)-40,y+h-50,80,40)=True And gbuttonpressed = 1 Then 
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return True
End If
End If
;
gbuttonpressed = 0
End If

; Draw the mouse (not implented yet)
;Rect MouseX(),MouseY(),10,10,1
DrawImage mousepointer,MouseX(),MouseY()
Flip
Wend

FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
End Function

;
; This function positions the slider
;
;

Function gpositionslider(x,y,w,h,gvalue,gmax)
hh# = h-10
gmaxx# = gmax
gstep# = hh/gmaxx
gslidertop = gstep*gvalue

gwindow(x,y,w,h,1,1,2)
gwindow(x+1,y+gslidertop+1,w-3,10,0,0,0)

If MouseY()=<y-10 Then Return 0
If MouseY()=>y-10 And MouseY()<y+h-10 Then
gnewpos = MouseY()-(y+10)
gnewos = gnewpos/gstep
If gnewos<0 Then gnewos = 0
Return gnewos
End If
If MouseY()=>y+h-10 Then Return gmax


End Function

;
; This is a slider function it activates the slider if pressed on
;
;
;
Function gslider(x,y,w,h,gvalue,gmax)
hh# = h-10
gmaxx# = gmax
gstep# = hh/gmaxx

gslidertop = gstep*gvalue

gwindow(x,y,w,h,1,1,2)
gwindow(x+1,y+gslidertop+1,w-3,10,0,0,0)

If Not gvalue = 0 And gmax=0 Then 

If gcheckarea(x,y+10,w,h-10) = True And mousedown(1) = True Then
Return True
End If
End If

Return False
End Function


;
; Tutorial
;
;
Function gtutorial(x,y,w,h)

SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()

gendline = 0
gcurrentline = 0

; Get the endline 
For i=0 To 1024
If Left(gtextfield1$(i),1)="@" Then gendline = i - 1 : i=1024
Next

;
; Main loop ------------------------------------<<<<
slideractive = False
While KeyHit(1)=False
Cls
DrawBlock gbackgroundimage,0,0
; Draqw the windows 
gwindow(x,y,w,h,1,0,0)
drawoutline2(x+10,y+40,w-40,h-70,1,80,80,80)

SetFont tutorialtitlefont
Color 0,0,0
Text x+w/2,y+4,"Tutorial",1,0

; Draw the text
SetFont grequesterfont
Color 0,0,0
counter = 0
For i=y+14+32 To y+h-60 Step 20
If counter<1024 And counter+gcurrentline<=gendline Then
Text x+14,i,gtextfield1$(counter+gcurrentline)
End If
counter = counter + 1
Next

; Operate the key shortcuts
If KeyHit(24) = True Then ; 'O' key
Return
End If
If KeyHit(156) = True Or KeyHit(28) = True Then ; Return/enter
Return
End If
If KeyDown(200) = True Then ; cursor up
If gcurrentline>0 Then
gcurrentline = gcurrentline - 1
End If
gwait(20)
End If
If KeyDown(208) = True Then ; cursor down
If gcurrentline<=gendline-8 Then
gcurrentline = gcurrentline + 1
End If
gwait(20)
End If

; Here the slider segment is located
;
If slideractive = False Then slideractive = gslider(x+w-30,y+60+14,16,h-20-126,gcurrentline,gendline-7) 

If mousedown(1) = True And slideractive=True Then
gcurrentline = gpositionslider(x+w-30,y+60+14,16,h-20-126,gcurrentline,gendline-7)
End If
If mousedown(1) = False And slideractive=True Then
slideractive = False
gslider(x+w-30,y+60+14,16,h-20-126,gcurrentline,gendline-7)
End If



; Draw the buttons
SetFont gsmallfont
If gbutton2(x+w-110,y+h-30,70,20,"Ok",1,1,gcurrentbutton) And slideractive=False Then gcurrentbutton = 1

; Top button
If gbutton4(x+w-30,y+20+4+18,small_top_button,4,gcurrentbutton) And slideractive=False Then gcurrentbutton = 4
; scroll up
If gbutton4(x+w-30,y+20+16+4+18,small_up_arrow,2,gcurrentbutton) And slideractive=False Then gcurrentbutton = 2
; Scroll down
If gbutton4(x+w-30,y+h-90+18,small_down_arrow,3,gcurrentbutton) And slideractive=False Then gcurrentbutton = 3
;; bottom
If gbutton4(x+w-30,y+h-90+16+18,small_bottom_button,5,gcurrentbutton) And slideractive=False Then gcurrentbutton = 5

; Close

If gbutton4(x+w-26,y+4,big_close_button,6,gcurrentbutton) And slideractive=False Then gcurrentbutton = 6





; Operate the scroll buttons
If mousedown(1) = True And gcurrentbutton > 0 And slideractive=False Then ; Scroll up

; Scroll up
If gcheckarea(x+w-30,y+20+16+4+18,16,16) = True Then
If gcurrentline>0 Then
gcurrentline = gcurrentline - 1
End If
gwait(20)
End If

; Top button
If gcheckarea(x+w-30,y+20+4+18,16,16) = True Then
gcurrentline = 0
gwait(20)
End If

; Down
If gcheckarea(x+w-30,y+h-90+18,16,16) = True Then ; Scroll down
If gcurrentline<=gendline-8 Then
gcurrentline = gcurrentline + 1
End If
gwait(20)
End If

; bottom
If gcheckarea(x+w-30,y+h-90+16+18,16,16) = True Then ; bottom
If gcurrentline<=gendline-8 Then
gcurrentline = gendline-8
End If
End If
End If



; Operate the normal button
If mousedown(1) = False And gcurrentbutton > 0 And slideractive=False Then
; Ok
If gcheckarea(x+w-110,y+h-30,70,20) = True Then ; if pressed ok
gwait(100):FlushKeys:FlushMouse
Return
End If
; Close
If gcheckarea(x+w-26,y+4,32,32) = True Then ; if pressed ok
gwait(100):FlushKeys:FlushMouse
Return
End If



gcurrentbutton = 0
End If

;Rect MouseX(),MouseY(),10,10,1
DrawImage mousepointer,MouseX(),MouseY()

Flip
Wend

gwait(100)
FlushKeys : FlushMouse
FreeImage gbackgroundimage
End Function






;
; Views text from gtextfield1$
;
;
Function gtextviewer(x,y,w,h)

SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()

gendline = 0
gcurrentline = 0

; Get the endline 
For i=0 To 1024
If Left(gtextfield1$(i),1)="@" Then gendline = i - 1 : i=1024
Next

;
; Main loop ------------------------------------<<<<
slideractive = False
While KeyHit(1)=False
Cls
DrawBlock gbackgroundimage,0,0
; Draqw the windows 
gwindow(x,y,w,h,1,0,0)
drawoutline2(x+10,y+10,w-50,h-50,1,80,80,80)

; Draw the text
SetFont grequesterfont
Color 0,0,0
counter = 0
For i=y+14 To y+h-60 Step 20
If counter<1024 And counter+gcurrentline<=gendline Then
Text x+14,i,gtextfield1$(counter+gcurrentline)
End If
counter = counter + 1
Next

; Operate the key shortcuts
If KeyHit(24) = True Then ; 'O' key
Return
End If
If KeyHit(156) = True Or KeyHit(28) = True Then ; Return/enter
Return
End If
If KeyDown(200) = True Then ; cursor up
If gcurrentline>0 Then
gcurrentline = gcurrentline - 1
End If
gwait(20)
End If
If KeyDown(208) = True Then ; cursor down
If gcurrentline<=gendline-8 Then
gcurrentline = gcurrentline + 1
End If
gwait(20)
End If

; Here the slider segment is located
;
If slideractive = False Then slideractive = gslider(x+w-30,y+60+14,16,h-20-126,gcurrentline,gendline-7) 

If mousedown(1) = True And slideractive=True Then
gcurrentline = gpositionslider(x+w-30,y+60+14,16,h-20-126,gcurrentline,gendline-7)
End If
If mousedown(1) = False And slideractive=True Then
slideractive = False
gslider(x+w-30,y+60+14,16,h-20-126,gcurrentline,gendline-7)
End If



; Draw the buttons
SetFont gsmallfont
If gbutton2(x+w-110,y+h-30,70,20,"Ok",1,1,gcurrentbutton) And slideractive=False Then gcurrentbutton = 1
; Top button
If gbutton4(x+w-30,y+20+4+18,small_top_button,4,gcurrentbutton) And slideractive=False Then gcurrentbutton = 4
; scroll up
If gbutton4(x+w-30,y+20+16+4+18,small_up_arrow,2,gcurrentbutton) And slideractive=False Then gcurrentbutton = 2
; Scroll down
If gbutton4(x+w-30,y+h-90+18,small_down_arrow,3,gcurrentbutton) And slideractive=False Then gcurrentbutton = 3
; bottom
If gbutton4(x+w-30,y+h-90+16+18,small_bottom_button,5,gcurrentbutton) And slideractive=False Then gcurrentbutton = 5

; Close
If gbutton4(x+w-26,y+4,big_close_button,6,gcurrentbutton) And slideractive=False Then gcurrentbutton = 6




; Operate the scroll buttons
If mousedown(1) = True And gcurrentbutton > 0 And slideractive=False Then ; Scroll up

; Scroll up
If gcheckarea(x+w-30,y+20+16+4+18,16,16) = True Then
If gcurrentline>0 Then
gcurrentline = gcurrentline - 1
End If
gwait(20)
End If

; Top button
If gcheckarea(x+w-30,y+20+4+18,16,16) = True Then
gcurrentline = 0
gwait(20)
End If

; Down
If gcheckarea(x+w-30,y+h-90+18,16,16) = True Then ; Scroll down
If gcurrentline<=gendline-8 Then
gcurrentline = gcurrentline + 1
End If
gwait(20)
End If

; bottom
If gcheckarea(x+w-30,y+h-90+16+18,16,16) = True Then ; bottom
If gcurrentline<=gendline-8 Then
gcurrentline = gendline-8
End If
End If




End If

; Operate the normal button
If mousedown(1) = False And gcurrentbutton > 0 And slideractive=False Then
; Ok
If gcheckarea(x+w-110,y+h-30,70,20) = True Then ; if pressed ok
gwait(100):FlushKeys:FlushMouse
Return
End If
; Close
If gcheckarea(x+w-26,y+4,32,32) = True Then ; if pressed ok
gwait(100):FlushKeys:FlushMouse
Return
End If



gcurrentbutton = 0
End If

;Rect MouseX(),MouseY(),10,10,1
DrawImage mousepointer,MouseX(),MouseY()

Flip
Wend

gwait(100)
FlushKeys : FlushMouse
FreeImage gbackgroundimage
End Function

;
; Read text from a file
;
;
Function greadtext(gfilename$,gfield)
; Read in the entire text file
counter = 0
filein = ReadFile(gfilename$)
While Not Eof(filein) Or counter=2047
gtextfield2$(counter) = ReadLine$(filein)
counter=counter+1
Wend
CloseFile filein

; Read in the message
For i=0 To counter
If Left(gtextfield2$(i),1) = "@" Then
q$ = Mid(gtextfield2$(i),2,Len(gtextfield2$(i))-1)
q = q$
If q = gfield Then 
For ii=i+1 To counter
If Left(gtextfield2$(ii),1) = "@" Then gendline = ii-1 : ii=counter
Next
counter2=0
For ii=i+1 To gendline
gtextfield1$(counter2)=gtextfield2$(ii)
counter2=counter2+1
Next
gtextfield1$(counter2)="@"
End If
End If
Next
End Function

;
; Window function
;
;
;

Function gwindow(x,y,w,h,gstyle,gborder,gbackground)
If gstyle=0 Then

ElseIf gstyle > 0
Color 0,0,0
Rect x,y,w,h,1
x=x+1:y=y+1
w=w-2:h=h-1
End If
;
If gborder = 0 Then
drawoutline(x,y,w,h,0)
ElseIf gborder > 0
drawoutline(x,y,w,h,1)
ElseIf gborder = -1 Then
drawoutline(x,y,w,h,-1)
ElseIf gborder = -2 Then
drawoutline(x,y,w,h,-2)
End If
;
Viewport x+2,y+2,w-4,h-4
;
If gbackground = 0 Then
TileBlock gback1,x,y

Else If gbackground = -1 Then
Color 0,0,0
Rect x,y,w,h,1

Else If gbackground = 1 Then
TileBlock gback2,x,y
Else If gbackground = 2 Then
Color 105,105,105
Rect x,y,w,h,1
ElseIf gbackground = 2 Then
Color 0,0,0
Rect x,y,w,h,1
ElseIf gbackground = 3 Then
TileBlock gback3,x,y
ElseIf gbackground = 4 Then
TileBlock gback4,x,y
ElseIf gbackground = 5 Then
TileBlock gback5,x,y
ElseIf gbackground = 6 Then
TileBlock gback6,x,y
ElseIf gbackground = 7 Then
TileBlock gback7,x,y


End If
Viewport 0,0,800,600

End Function

;
; This function is for a listbox
;
; Returns -1 is canceled
;
Function glistbox(x,y,w,h,name$,fieldsize)

SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0

gcursorpos = 0
glistpos = 0
numberofitems = (h-80-6)/14-1

SetBuffer BackBuffer()

gcurrentbutton = 0
; Main loop <------------------------------
slideractive=False
While KeyHit(1) = False
Cls ; clear the backbuffer
DrawBlock gbackgroundimage,0,0

; Draw the complete surface with borders
;drawoutline(x,y,w,h,0)
gwindow(x,y,w,h,0,0,0)
; Draw the list surface with borders
drawoutline(x+6,y+30,(w-12)-25,h-70,1)
Color 160,160,160
Rect (x+6)+2,(y+30)+2,((w-12)-25)-4,(h-70)-4,1
;gwindow(x+6,y+30,(w-12)-25,h-70,0,1,1)

Color 0,0,0
Text x+w/2,y+5,name$,1,0



; Draw the list contents and the cursor
For i=0 To (h-80-6)/14-1
If i+glistpos<=fieldsize Then
If i + glistpos = gcursorpos Then
Color 255,255,255
Rect x+8,(y+34)+i*14,w-12-30,14,1
Color 0,0,0
Else
;Color 255,255,255
Color 0,0,0
End If
If mousedown(1) = True Then
If gcheckarea(x+8,(y+34)+i*14,w-12-30,14) = True Then
If Not i+glistpos>fieldsize Then
gcursorpos = i+glistpos
End If
End If
End If
Text (x+8),(y+32)+i*14,glistfield$(i+glistpos)
End If
Next

;
; Here we read the keys for our listbox
;
If KeyDown(200) = True Then ; if pressed cursorup
If gcursorpos>0 Then
gcursorpos = gcursorpos - 1
If gcursorpos<glistpos Then glistpos = gcursorpos 
gwait(50)
End If
End If
If KeyDown(208) = True Then ; if pressed cursordown
If gcursorpos<fieldsize
gcursorpos = gcursorpos + 1
If gcursorpos>glistpos+numberofitems Then glistpos = glistpos + 1
gwait(50)
End If
End If
If KeyDown(201) = True Then ; if pressed page up
gcursorpos = gcursorpos - numberofitems
If gcursorpos<0 Then gcursorpos = 0
glistpos = gcursorpos
gwait(150)
End If
If KeyDown(209) = True Then ; if pressed page down
gcursorpos = gcursorpos + numberofitems
If gcursorpos > fieldsize Then gcursorpos = fieldsize
glistpos = gcursorpos 
gwait(150)
End If
If KeyDown(28) = True Then ; If pressed enter
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return gcursorpos
End If


; Here the slider code is located
;
If slideractive = False Then slideractive = gslider(x+w-27,y+80-18,16,h-80-90+35,glistpos,fieldsize) 

; Execute the slider code
If mousedown(1) = True And slideractive=True Then
glistpos = gpositionslider(x+w-27,y+80-18,16,h-80-90+35,glistpos,fieldsize)
End If
If mousedown(1) = False And slideractive=True Then
slideractive = False
gslider(x+w-27,y+80-18,16,h-80-90+35,glistpos,fieldsize)
End If


; Draw/operate the buttons


; Scroll buttons
;If gbutton1(x+w-12-15,(y+30),20,50,"",1,0) Then gcurrentbutton = 1
;If gbutton1(x+w-12-15,y+h-50-40,20,50,"",2,0) Then gcurrentbutton = 2
; Scroll buttons
; Up
If gbutton4(x+w-12-15,(y+30+16),small_up_arrow,1,0) Then gcurrentbutton = 1
; Top
If gbutton4(x+w-12-15,(y+30),small_top_button,5,0) Then gcurrentbutton = 5
; Down
If gbutton4(x+w-12-15,y+h-50-40+16,small_down_Arrow,2,0) Then gcurrentbutton = 2
; bottom
If gbutton4(x+w-12-15,y+h-50-40+32,small_bottom_button,6,0) Then gcurrentbutton = 6


If gbutton1((x+4)+4,(y+h-29)-4,75,25,"Ok",2,0) Then gcurrentbutton = 3
If gbutton1((x+w-79)-4,(y+h-29)-4,75,25,"Cancel",3,0) Then gcurrentbutton = 4
SetFont grequesterfont
; Close button
If gbutton4(x+w-26,y+2,big_close_button,7,gcurrentbutton) Then gcurrentbutton = 7


; Execute the scroll buttons
If gcurrentbutton>0 And mousedown(1) = True Then

	If gcurrentbutton = 1 And gcheckarea(x+w-12-15,(y+30),20,50) = True Then ; if scrollup
		If glistpos > 0 Then glistpos = glistpos - 1 : gwait(50)
	End If
	If gcurrentbutton = 2 And gcheckarea(x+w-12-15,y+h-50-40,20,50) = True Then ; if scrolldown
		If glistpos < fieldsize Then glistpos = glistpos + 1 : gwait(50)
	End If
	If gcurrentbutton = 5 And gcheckarea(x+w-12-15,(y+30),16,16) = True Then ; Top
		glistpos = 0
	End If
	If gcurrentbutton = 6 And gcheckarea(x+w-12-15,y+h-50-40+32,16,16) = True Then ; bottom
		glistpos = fieldsize 
	End If


End If

; Execute the normal buttons
If mousedown(1) = False And gcurrentbutton > 0 Then
If gcheckarea((x+4)+4,(y+h-29)-4,75,25) = True Then ; if pressed ok
FreeImage gbackgroundimage
Return gcursorpos
End If
If gcheckarea((x+w-79)-4,(y+h-29)-4,75,25) = True Then ; if pressed cancel
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return -1
End If
; Close
If gcurrentbutton = 7 And gcheckarea(x+w-26,y+2,32,32) = True Then ; bottom
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return -1
End If

gcurrentbutton = False
End If

; Draw a mouse replacement insert your own here.
;Rect MouseX(),MouseY(),5,5,1
DrawImage mousepointer,MouseX(),MouseY()

Flip
Wend
FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
Return -1
End Function

;
; This is a function for text boxes
;
;
;
Function gtextbox(x,y,w,h,t$,b1)
gmouseover = False
gbuttonpressed = False
If gcheckarea(x,y,w,h) Then gmouseover=True
If mousedown(1) = True And gmouseover=True Then gbuttonpressed=True
gfield$(b1) = t$

drawoutline(x,y,w,h,1)
Color 255,255,255
Text x+2,y+2,t$

If gbuttonpressed=True Then gfield$(b1) = typesomething(x+2,y+2,w-4,h-4,t$)

End Function

;
; This function draws the special buttons
; 0 up 32
; 1 left 32
; 2 down 32
; 3 right 32
; 4 up in 32
; 5 left in 32
; 6 down in 32
; 7 right in 32
; 8 up 16
; 9 left 16
; 10 down 16
; 11 right 16
; 12 up in 16
; 13 left in 16
; 14 down in 16
; 15 right in 16
;
; 16 top 32
; 17 bottom 32
; 18 top in 32
; 19 bottm in 32
; 20 top 16
; 21 bottom 16
; 22 top in 16
; 23 bottom in 16

;
;
Function drawspecialbutton(x,y,number)
Select number
Case 0  DrawBlock uparrow32,x,y
Case 1  DrawBlock leftarrow32,x,y
Case 2  DrawBlock downarrow32,x,y
Case 3  DrawBlock rightarrow32,x,y
Case 4  DrawBlock uparrowin32,x,y
Case 5  DrawBlock leftarrowin32,x,y
Case 6  DrawBlock downarrowin32,x,y
Case 7  DrawBlock rightarrowin32,x,y
Case 8  DrawBlock uparrow16,x,y
Case 9  DrawBlock leftarrow16,x,y
Case 10 DrawBlock downarrow16,x,y
Case 11 DrawBlock rightarrow16,x,y
Case 12 DrawBlock uparrowin16,x,y
Case 13 DrawBlock leftarrowin16,x,y
Case 14 DrawBlock downarrowin16,x,y
Case 15 DrawBlock rightarrowin16,x,y
Case 16 DrawBlock toparrow32,x,y
Case 17 DrawBlock bottomarrow32,x,y
Case 18 DrawBlock toparrowin32,x,y
Case 19 DrawBlock bottomarrowin32,x,y
Case 20 DrawBlock toparrow16,x,y
Case 21 DrawBlock bottomarrow16,x,y
Case 22 DrawBlock toparrowin16,x,y
Case 23 DrawBlock bottomarrowin16,x,y
Case 24 DrawBlock closebutton32,x,y
Case 25 DrawBlock closebuttonin32,x,y
Case 26 DrawBlock closebutton16,x,y
Case 27 DrawBlock closebuttonin16,x,y

End Select
End Function




;
; Gbutton 4 - Special Button up/left/down/right
;
;

Function gbutton4(x,y,button_type,b1,b2)
gmouseover = False
gbuttonpressed = False

If (button_type > 7) And (button_type < 16) Then w = 16 : h = 16 
If (button_type => 0 ) And (button_type =< 7 ) Then w = 32 : h = 32
If (button_type > 19 ) And (button_type < 24) Then w = 16 : h = 16 
If (button_type => 16 ) And (button_type =< 19 ) Then w = 32 : h = 32
If (button_type => 24) And (button_type =<25) Then w = 32 : h = 32
If (button_type => 26 ) And (button_type =< 27) Then w = 16 : h = 16 

;
If MouseX()>x And MouseX()<x+w And MouseY()>y And MouseY()<y+h Then gmouseover=True
If mousedown(1) = True And gmouseover=True Then gbuttonpressed = True
;
If b2>0 Then
gbuttonpressed=False
End If
;
If b1=b2 Then 
gbuttonpressed=True
gmouseover=True
End If
;
If gbuttonpressed = False Then
;gwindow(x,y,w,h,0,0,0)
drawspecialbutton(x,y,button_type)
Else
If button_type < 15 Then drawspecialbutton(x,y,button_type+4)
If (button_type => 15 ) And (button_type =< 23 )  drawspecialbutton(x,y,button_type+2) 
If (button_type => 24) And (button_type =<27) Then drawspecialbutton(x,y,button_type+1) 

End If

; Mouseover effect
;If gmouseover=False Then
;	
;Else If gbuttonpressed = False
;	gwindow(x,y,w,h,0,0,1)
;End If

;Color 0,0,0
;Text x+(w/2),y+(h/2),t$,1,1
;
If gmouseover=True And gbuttonpressed = True Then Return True
Return False
End Function




;
; This function creates a button. Version 2
;
; x
; y
; width
; height
; text
; color
; buttonnumber
; pressed button
;
;
;
;
Function gbutton3(x,y,w,h,t$,player,b1,b2)
gmouseover = False
gbuttonpressed = False
;
If MouseX()>x And MouseX()<x+w And MouseY()>y And MouseY()<y+h Then gmouseover=True
If mousedown(1) = True And gmouseover=True Then gbuttonpressed = True
;
If b2>0 Then
gbuttonpressed=False
End If
;
If b1=b2 Then 
gbuttonpressed=True
gmouseover=True
End If
;
If gbuttonpressed = False Then
gwindow(x,y,w,h,0,0,0)
Else
gwindow(x,y,w,h,0,1,0)
End If

; Mouseover effect
If gmouseover=False Then
	
Else If gbuttonpressed = False
	gwindow(x,y,w,h,0,0,1)
End If

Color 0,0,0
Text x+(w/2),y+(h/2),t$,1,1
; Get the color from the player in a selection of short variables
;cr = playercolor(player,0)
;cg = playercolor(player,1)
;cb = playercolor(player,2)
;
Color cr,cg,cb
Oval (x+(w/2))-5,(y+(h/2))-5,10,10,1

;
If gmouseover=True And gbuttonpressed = True Then Return True
Return False
End Function


;
; This function creates a button. Version 2
;
; x
; y
; width
; height
; text
; Highlight character x
; buttonnumber
; pressed button
;
;
;
;
Function gbutton2(x,y,w,h,t$,ghlight,b1,b2)
gmouseover = False
gbuttonpressed = False
;
If MouseX()>x And MouseX()<x+w And MouseY()>y And MouseY()<y+h Then gmouseover=True
If mousedown(1) = True And gmouseover=True Then gbuttonpressed = True
;
If b2>0 Then
gbuttonpressed=False
End If
;
If b1=b2 Then 
gbuttonpressed=True
gmouseover=True
End If
;
If gbuttonpressed = False Then
gwindow(x,y,w,h,0,0,0)
Else
gwindow(x,y,w,h,0,1,0)
End If

; Mouseover effect
If gmouseover=False Then
	
Else If gbuttonpressed = False
	gwindow(x,y,w,h,0,0,1)
End If


; Here we print the text inside the button
;
xloc = (x+w/2)-(StringWidth(t$)/2)
For i=1 To Len(t$)
If i = ghlight Then Color 0,50,200 Else Color 0,0,0
Text xloc,y+h/2,Mid(t$,i,1),0,1
xloc=xloc + StringWidth(Mid(t$,i,1))
Next


;
If gmouseover=True And gbuttonpressed = True Then Return True
Return False
End Function

;
; Gbutton 1
;
;

Function gbutton1(x,y,w,h,t$,b1,b2)
gmouseover = False
gbuttonpressed = False
;

; Set the font type by seing how long the width is
For i=40 To 4 Step -1
SetFont fonts(i)
If StringWidth(t$) < w-10 And StringHeight(t$) < h Then Exit
Next

;
If MouseX()>x And MouseX()<x+w And MouseY()>y And MouseY()<y+h Then gmouseover=True
If mousedown(1) = True And gmouseover=True Then gbuttonpressed = True
;
If b2>0 Then
gbuttonpressed=False
End If
;
If b1=b2 Then 
gbuttonpressed=True
gmouseover=True
End If
;
If gbuttonpressed = False Then
gwindow(x,y,w,h,0,0,0)
Else
gwindow(x,y,w,h,0,1,0)
End If

; Mouseover effect
If gmouseover=False Then
	
Else If gbuttonpressed = False
	gwindow(x,y,w,h,0,0,1)
End If

Color 0,0,0
Text x+(w/2),y+(h/2),t$,1,1
;
If gmouseover=True And gbuttonpressed = True Then Return True
Return False
End Function

;
; Gbutton 5 ( Buttons For Market, Production and Upgrade
;
;

Function gbutton5(x,y,w,h,t$,b1,b2)
gmouseover = False
gbuttonpressed = False
;
If MouseX()>x And MouseX()<x+w And MouseY()>y And MouseY()<y+h Then gmouseover=True
If mousedown(1) = True And gmouseover=True Then gbuttonpressed = True
;
If b2>0 Then
gbuttonpressed=False
End If
;
If b1=b2 Then 
gbuttonpressed=True
gmouseover=True
End If
;
If gbuttonpressed = False Then
gwindow(x,y,w,h,0,0,0)
Else
gwindow(x,y,w,h,0,1,0)
End If

; Mouseover effect
If gmouseover=False Then
	
Else If gbuttonpressed = False
	gwindow(x,y,w,h,0,0,0)
End If

Color 50,50,50
For zx = -1 To 1
For zy = -1 To 1
Text x+(w/2)-zx,y+(h/2)-zy,t$,1,1
Next
Next

Color 255,255,255

Text x+(w/2),y+(h/2),t$,1,1

;
If gmouseover=True And gbuttonpressed = True Then Return True
Return False
End Function

;
; Textbar ( Buttons For Market, Production and Upgrade
;
;

Function gtextbar(x,y,w,h,t$,tbback)
gmouseover = False
gbuttonpressed = False
;
; Set the font type by seing how long the width is
For i=40 To 4 Step -1
SetFont fonts(i)
If StringWidth(t$) < w-10 And StringHeight(t$) < h Then Exit
Next


;
If MouseX()>x And MouseX()<x+w And MouseY()>y And MouseY()<y+h Then gmouseover=True
If mousedown(1) = True And gmouseover=True Then gbuttonpressed = True
;
If b2>0 Then
gbuttonpressed=False
End If
;
If b1=b2 Then 
gbuttonpressed=True
gmouseover=True
End If
;
If gbuttonpressed = False Then
gwindow(x,y,w,h,-1,-1,tbback)
Else
gwindow(x,y,w,h,-1,-1,tbback)
End If

; Mouseover effect
If gmouseover=False Then
	
Else If gbuttonpressed = False
	gwindow(x,y,w,h,-1,-1,tbback)
End If

Color 50,50,50
For zx = -1 To 1
For zy = -1 To 1
Text x+(w/2)-zx,y+(h/2)-zy,t$,1,1
Next
Next

Color 255,255,255

Text x+(w/2),y+(h/2),t$,1,1

;
If gmouseover=True And gbuttonpressed = True Then Return True
Return False
End Function





;
; This function operates a filerequester
;
;
;
Function gfilerequester$(x,y,path$,defaultname$,extension$)

If Right(path$,1)="\" Then path$ = Left(path$,Len(path$)-1)

SetFont grequesterfont
SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0

If extension$ = "*.*" Then extensionactive=False Else extensionactive=True

filecounter = 0

pathname$ = path$
filename$ = defaultname$

FlushMouse:FlushKeys

;Read the dir into the array

.gfrreaddir

mydir = ReadDir(pathname$)

numfiles = 0
Repeat
	file$ = NextFile$(mydir)
	If file$ = "" Then Exit
	If FileType(pathname$+"\"+file$) = 2 Then
		gfiles$(numfiles) = file$
		gfiletypes(numfiles) = 2 ; it is a dir
		numfiles=numfiles+1
	Else
		If extensionactive=True Then
		If Right(file$,4) = extension$ Then
		gfiles$(numfiles) = file$
		gfiletypes(numfiles) = 1 ; it is a file
		numfiles=numfiles+1
		End If
		End If
		If extensionactive = False Then
		gfiles$(numfiles) = file$
		gfiletypes(numfiles) = 1 ; it is a file
		numfiles=numfiles+1
		End If
	End If
	;numfiles=numfiles+1
Forever
CloseDir mydir
numfiles=numfiles-1

filecounter = 0
currentbutton= 0
SetBuffer BackBuffer()
; <Main loop ------------------------<<<<<<<<<<
slideractive = False
sliderpos = 0
While KeyHit(1) = False
Cls

DrawBlock gbackgroundimage,0,0
;Text 0,0,"Pathname$ : " + pathname$
;Text 0,20,"Filename$ : " + filename$

; Requester outline
;drawoutline(x,y,300,400,0)
gwindow(x,y,300,400,1,0,0)
; Dir outline
drawoutline(x+5,y+50,250,260,1)
Color 170,170,170
Rect (x+5)+2,(y+50)+2,(250)-4,(260)-4,1
;gwindow(x+5,y+50,250,260,0,1,1)

; Draw the files and directories
;
counter = filecounter
For dy=0 To 13
If gfiletypes(counter) = 2 Then Color 255,255,0 Else Color 0,0,0
;Rect x+10,(y+50)+(dy*18),250,18,0
If Not counter>numfiles Then
Text x+10,(y+50)+(dy*18),gfiles(counter)
If gfiletypes(counter) = 2 Then Text (x+10)+1,((y+50)+(dy*18))+1,gfiles(counter)

End If
counter=counter+1
Next

If slideractive = False Then slideractive = gslider(x+260,y+76+36,32,200-68,filecounter,numfiles-13) 

If mousedown(1) = True And slideractive=True Then
filecounter = gpositionslider(x+260,y+76+36,32,200-68,filecounter,numfiles-13)
End If
If mousedown(1) = False And slideractive=True Then
slideractive = False
gslider(x+260,y+76+36,32,200-68,filecounter,numfiles-13)
End If



; Draw and operate the buttons
; ok button
If gbutton1(x+5,y+353,100,40,"OK",1,currentbutton) And slideractive = False Then  currentbutton = 1
; cancel button
If gbutton1(x+195,y+353,100,40,"CANCEL",2,currentbutton) And slideractive = False Then currentbutton = 2
;; move up directory list
;If gbutton1(x+260,y+50,30,20,"",3,currentbutton) And slideractive = False Then currentbutton=3
;; move down directory list
;If gbutton1(x+260,y+290,30,20,"",4,currentbutton) And slideractive = False Then currentbutton=4

; move top directory list
If gbutton4(x+260,y+50,Big_top_button,6,currentbutton) And slideractive = False Then currentbutton=6

; move up directory list
If gbutton4(x+260,y+50+32,Big_up_arrow,3,currentbutton) And slideractive = False Then currentbutton=3
; move down directory list
If gbutton4(x+260,y+276-32,Big_down_arrow,4,currentbutton) And slideractive = False Then currentbutton=4
; move bottom directory list
If gbutton4(x+260,y+276,Big_bottom_button,7,currentbutton) And slideractive = False Then currentbutton=7

; Root
If gbutton1(x+5,y+5,100,40,"Root",5,currentbutton) And slideractive = False Then currentbutton=5
;

; Close
If gbutton4(x+300-28,y+5,Big_Close_Button,8,currentbutton) And slideractive = False Then currentbutton=8
;
SetFont grequesterfont


; Execute buttons
If currentbutton>0 And MouseHit(1) = False And slideractive=False Then
; ok
If currentbutton = 1 Then ; ok button
FreeImage gbackgroundimage
gwait(200):FlushKeys:FlushMouse
Return pathname$+"\"+filename$
End If
;cancel
If currentbutton = 2 Then ; cancel button
FreeImage gbackgroundimage
gwait(200):FlushKeys:FlushMouse
Return False
End If
;move up directory list
If currentbutton = 3 Then ; move up in list
If filecounter>0 Then filecounter=filecounter-1 : gwait(50)
End If
; move down directory list
If currentbutton = 4 Then ; move down in list
If filecounter<numfiles-13 Then filecounter=filecounter+1:gwait(50)
End If
; Go to the root directory
If currentbutton = 5 Then ; goto root
pathname$ = path$ : Goto gfrreaddir
End If
;
If currentbutton = 6 Then ; move to top
filecounter = 0
End If
;
If currentbutton = 7 Then ; move to bottom
filecounter = numfiles-13
End If

If currentbutton = 8 Then ; CloseButton
	FreeImage gbackgroundimage
	gwait(100) : FlushKeys:FlushMouse
	Return False
End If


currentbutton=0
End If

If KeyHit(28) Then
FreeImage gbackgroundimage
gwait(200) : FlushKeys:FlushMouse
Return pathname$+"\"+filename$
End If


; select something from the filelist
If mousedown(1) = True Then
	If gcheckarea(x+5,y+50,250,260) = True Then
	counter = filecounter
	For dy=0 To 13
		If gcheckarea(x+10,((y+50)+(dy*18)),250,18) = True Then
		If Not counter>numfiles Then
		If gfiletypes(counter) = 2 Then 
			If Not gfiles$(counter)="." Then
			If Not gfiles$(counter)=".." Then
			pathname$ = pathname$+"\"+gfiles$(counter)  
			;
			gwait(150) : Goto gfrreaddir
			End If
			End If
			Else
			filename$ = gfiles$(counter)
			gwait(150)
		End If
		End If
		End If
		counter=counter+1
	Next
	gwait(100)
	End If
End If


;
; Draw the filename
;
; Name outline
drawoutline(x+60,y+320,190,24,0)
Color 0,0,0:Text x+5,y+320,"Name :":Color 255,255,255
Text x+62,y+322,filename$
If mousedown(1) = True And gcheckarea(x+60,y+320,190,20)=True Then
filename$ = typesomething(x+62,y+322,186,20,filename$)
If filename$ = -1 Then filename$=""
End If


;Rect MouseX(),MouseY(),5,5,1
DrawImage mousepointer,MouseX(),MouseY()

Flip
Wend

gwait(200):FlushMouse : FlushKeys
FreeImage gbackgroundimage
Return False

End Function

;
; This function lets you type in some text
;
;
Function typesomething$(x,y,w,h,defstring$)

SetBuffer FrontBuffer()

typestring$ = defstring$

Color 0,0,0
Rect x,y,w,h,1
Color 255,255,255
Text x,y,typestring$+"_"

Repeat
t = GetKey()
If t=13 Then SetBuffer BackBuffer() : FlushKeys:Return typestring$
If t=32 Then typestring$=typestring$+" "
If KeyHit(1) = True Then SetBuffer BackBuffer() : Return defstring$
;
If t>0 Then 
	; If pressed backspace then delete last character
	If t = 8 And Len(typestring)>0 Then typestring = Left(typestring,Len(typestring)-1)
	; If valid characters
	If t=>33 And t=<127 Then
		; If max length not reached
		If StringWidth(typestring)<w-15 Then 
			; add new character
			typestring = typestring+Chr(t)
		End If
	End If
	; Draw the text on the screen
	Color 0,0,0
	Rect x,y,w,h,1
	Color 255,255,255
	Text x,y,typestring$ + "_"
End If
Forever

End Function

;
; this function draws a outline
;
Function drawoutline(x,y,w,h,state)

x=x+1
y=y+1
w=w-2
h=h-2

If state = -2 Then
	Color 200,200,200
	Rect x,y,w,h,0 : Return
End If

If state = -1 Then
	Color 0,0,0
	Rect x,y,w,h,0 : Return
End If

If state=0 Then
	Color 0,0,0
	Rect x,y,w,h,1
End If

If state = 2 Then state = 0
;
; left top lines
If state = 0 Then Color 255,255,255 Else Color 10,10,10
Rect x,y,w,h,0
Rect x+1,y+1,w-1,h-1,0
;
; Right border lines
If state = 0 Then Color 0,0,0 Else Color 255,255,255
Line x+w-1,y,x+w-1,y+h
Line x,y+h-1,x+w,y+h-1
;
; Right borders inner lines
If state = 0 Then Color 100,100,100 Else Color 255,255,255
Line x+w-2,y+1,x+w-2,y+h-1
Line x+1,y+h-2,x+w-1,y+h-2
Color 255,255,255
End Function

;
; this function draws a outline
;
Function drawoutline2(x,y,w,h,state,r,g,b)
If state=<1 Then
Color r,g,b
Rect x,y,w,h,1
End If

If state = 2 Then state = 0
;
; left top lines
If state = 0 Then Color 255,255,255 Else Color 10,10,10
Rect x,y,w,h,0
Rect x+1,y+1,w-1,h-1,0
;
; Right border lines
If state = 0 Then Color 0,0,0 Else Color 255,255,255
Line x+w-1,y,x+w-1,y+h
Line x,y+h-1,x+w,y+h-1
;
; Right borders inner lines
If state = 0 Then Color 100,100,100 Else Color 255,255,255
Line x+w-2,y+1,x+w-2,y+h-1
Line x+1,y+h-2,x+w-1,y+h-2
Color 255,255,255
End Function




;
; This function returns if the mouse is inside a zone
;
;
Function gcheckarea(x,y,w,h)
If MouseX()>x And MouseX()<x+w And MouseY()>y And MouseY()<y+h Then Return True Else Return False
End Function

;
; This function for a little slowdown
;
;
Function gwait(ms)
t = MilliSecs()+ms
While MilliSecs()<t:Wend
End Function


;
; Dimplomacy window
;
;
Function gproduction(x,y)

; Set the scale of the gui
guiscale = setzoom

;w = 400 ; default width
;h = 300 ; default height

; Handle the autocentering if required
If x=-1 Then
	x = 800/2-(600/2)
End If

If y=-1 Then
	y = 600/2-(400/2)
End If

; Grab the foreground
SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()
FlushKeys() : FlushMouse()
gexitwindow = False
While KeyHit(1) = False And gexitwindow = False
	Cls
	DrawBlock gbackgroundimage,0,0
	;
	;
	; Insert Gui view here >>>>>
	gwindow(x,y,600,400,0,0,0)
	
	;Function listbox(num,left,top,width,height,itemcount,colls,keyactive,buttonsactive,border)
	
	; Read in the production facilities
	For i=0 To maxproduction
		;lb_listitem$(0,i) = productionsstr$(i,production_item)
	Next
	;Max production
	

	
	
	gtextbar(x,y+30,100,20,"Goods",1)
	a = listbox(0,x    ,y+50,100,100,numgoods,1,True,False,False)
	b = listbox(1,x+100,y+50,50,100,numgoods,1,True,False,False)
	c = listbox(2,x+150,y+50,50,100,numgoods,1,True,False,False)
	d = listbox(3,x+200,y+50,50,100,numgoods,1,True,False,False)
	e = listbox(4,x+250,y+50,50,100,numgoods,1,True,False,False)
	
	f = listbox(5,x    ,y+250,50,100,numgoods,1,True,False,False)
	g = listbox(6,x+100,y+250,50,100,numgoods,1,True,False,False)

	
	If a = True Then ecoupdatelistboxes(x,y,0,numgoods)
	If b = True Then ecoupdatelistboxes(x,y,1,numgoods)
	If c = True Then ecoupdatelistboxes(x,y,2,numgoods)
	If d = True Then ecoupdatelistboxes(x,y,3,numgoods)
	If e = True Then ecoupdatelistboxes(x,y,4,numgoods)
	
	
	; Draw the mouse (not implented yet)
	Rect MouseX(),MouseY(),10,10,1
	Flip
Wend

FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
End Function

;
; Crappy listbox needs crappy coding - Workaround for updating the visual part of the listbox
; And aligning the various linked listboxes
;
;
Function ecoupdatelistboxes(x,y,num,numgoods)
	;Select num
	;	Case 0 : lb_start(1) = lb_start(0) : lb_cursor(1) = lb_cursor(0)
	;	Case 1 : lb_start(0) = lb_start(1) : lb_cursor(0) = lb_cursor(1)
	;End Select
	For i=num+1 To 10
		lb_start(i) = lb_start(num)
		lb_cursor(i) = lb_cursor(num)
	Next
	For i=num-1 To 0 Step -1
		lb_start(i) = lb_start(num)
		lb_cursor(i) = lb_cursor(num)
	Next
	
	listbox(0,x    ,y+50,100,100,numgoods,1,False,False,False)
	listbox(1,x+100,y+50,100,100,numgoods,1,False,False,False)
	listbox(2,x+200,y+50,100,100,numgoods,1,False,False,False)
	listbox(3,x+300,y+50,100,100,numgoods,1,False,False,False)
	listbox(4,x+400,y+50,100,100,numgoods,1,False,False,False)
	
	For i=0 To 255 
		If KeyDown(i) = True Then
			Delay(50) : Exit 
		End If
	Next

End Function


;
; Dimplomacy window
;
;
Function gDiplomacy(x,y)

; Set the scale of the gui
guiscale = setzoom

;w = 400 ; default width
;h = 300 ; default height

; Handle the autocentering if required
If x=-1 Then
x = 800/2-(600/2)
End If

If y=-1 Then
y = 600/2-(400/2)
End If

; Grab the foreground
SetBuffer FrontBuffer()
gbackgroundimage = CreateImage(800,600)
GrabImage gbackgroundimage,0,0
SetBuffer BackBuffer()
FlushKeys():FlushMouse()
gexitwindow = False
While KeyHit(1) = False And gexitwindow = False
Cls
DrawBlock gbackgroundimage,0,0
;
;
; Insert Gui view here >>>>>
gwindow(x,y,600,400,0,0,0)

gtextbar(x+309,y+82,212,26,"At Peace",0)
gtextbar(x+5,y+3,212,26,"Diplomacy",0)
gtextbar(x+11,y+119,172,42,"Warfare",0)
gtextbar(x+411,y+119,172,42,"Offer",0)
gtextbar(x+210,y+119,172,42,"Request",0)
gtextbar(x+93,y+82,212,26,"The Teutons",0)


If gbutton1(x+11,y+243,172,26,"Declare War",1,gbuttonpressed) = True Then gbuttonpressed=1
;gcheckarea(x+11,y+243,172,26)
If gbutton1(x+11,y+275,172,26,"End hostilities",2,gbuttonpressed) = True Then gbuttonpressed=2
;gcheckarea(x+11,y+275,172,26)
If gbutton1(x+209,y+173,172,26,"Gold",6,gbuttonpressed) = True Then gbuttonpressed=6
;gcheckarea(x+209,y+173,172,26)
If gbutton1(x+209,y+207,172,26,"Unit",7,gbuttonpressed) = True Then gbuttonpressed=7
;gcheckarea(x+209,y+207,172,26)
If gbutton1(x+209,y+242,172,26,"Trade",8,gbuttonpressed) = True Then gbuttonpressed=8
;gcheckarea(x+209,y+242,172,26)
If gbutton1(x+209,y+276,172,26,"City",9,gbuttonpressed) = True Then gbuttonpressed=9
;gcheckarea(x+209,y+276,172,26)
If gbutton1(x+209,y+310,172,26,"Remove Units",10,gbuttonpressed) = True Then gbuttonpressed=10
;gcheckarea(x+209,y+310,172,26)
If gbutton1(x+220,y+4,50,26,"Int",11,gbuttonpressed) = True Then gbuttonpressed=11
;gcheckarea(x+220,y+4,50,26)
If gbutton4(x+570,y+2,Big_close_button,13,gbuttonpressed) = True Then gbuttonpressed = 13
;gcheckarea(x+570,y+2,25,22)
If gbutton1(x+499,y+366,92,27,"Ok",14,gbuttonpressed) = True Then gbuttonpressed=14
;gcheckarea(x+499,y+366,92,27)
;If gbutton1(x+401,y+366,92,27,"Cancel",15,gbuttonpressed) = True Then gbuttonpressed=15
;gcheckarea(x+401,y+366,92,27)
If gbutton1(x+11,y+172,172,26,"Request Alliance",16,gbuttonpressed) = True Then gbuttonpressed=16
;gcheckarea(x+11,y+172,172,26)
If gbutton1(x+11,y+208,172,26,"Request Assistance",17,gbuttonpressed) = True Then gbuttonpressed=17
;gcheckarea(x+11,y+208,172,26)
If gbutton1(x+411,y+173,172,26,"Gold",18,gbuttonpressed) = True Then gbuttonpressed=18
;gcheckarea(x+411,y+173,172,26)
If gbutton1(x+411,y+207,172,26,"Unit",19,gbuttonpressed) = True Then gbuttonpressed=19
;gcheckarea(x+411,y+207,172,26)
If gbutton1(x+411,y+242,172,26,"Trade",20,gbuttonpressed) = True Then gbuttonpressed=20
;gcheckarea(x+411,y+242,172,26)
If gbutton1(x+411,y+276,172,26,"City",21,gbuttonpressed) = True Then gbuttonpressed=21
;gcheckarea(x+411,y+276,172,26)
If gbutton1(x+411,y+310,172,26,"Remove Units",22,gbuttonpressed) = True Then gbuttonpressed=22
;gcheckarea(x+411,y+310,172,26)
If gbutton1(x+94,y+64,50,16,"P1",23,gbuttonpressed) = True Then gbuttonpressed=23
;gcheckarea(x+94,y+64,50,16)
If gbutton1(x+148,y+64,50,16,"P2",24,gbuttonpressed) = True Then gbuttonpressed=24
;gcheckarea(x+148,y+64,50,16)
If gbutton1(x+201,y+64,50,16,"P3",25,gbuttonpressed) = True Then gbuttonpressed=25
;gcheckarea(x+201,y+64,50,16)
If gbutton1(x+255,y+64,50,16,"P4",26,gbuttonpressed) = True Then gbuttonpressed=26
;gcheckarea(x+255,y+64,50,16)
If gbutton1(x+310,y+64,50,16,"P5",27,gbuttonpressed) = True Then gbuttonpressed=27
;gcheckarea(x+310,y+64,50,16)
If gbutton1(x+364,y+64,50,16,"P6",28,gbuttonpressed) = True Then gbuttonpressed=28
;gcheckarea(x+364,y+64,50,16)
If gbutton1(x+417,y+64,50,16,"P7",29,gbuttonpressed) = True Then gbuttonpressed=29
;gcheckarea(x+417,y+64,50,16)
If gbutton1(x+471,y+64,50,16,"P8",30,gbuttonpressed) = True Then gbuttonpressed=30
;gcheckarea(x+471,y+64,50,16)
; End Gui interface insertion <<<<<<
;
; Execute the buttons
If mousedown(1) = False And gbuttonpressed > 0 Then

Select gbuttonpressed
;
Case 13
If gcheckarea(x+570,y+2,25,22) = True Then
gexitwindow = True
End If
;
Case 14
If gcheckarea(x+499,y+366,92,27) = True Then
gexitwindow = True
End If
;
End Select
;
gbuttonpressed = 0
End If



; Draw the mouse (not implented yet)
;Rect MouseX(),MouseY(),10,10,1
DrawImage mousepointer,MouseX(),MouseY()
Flip
Wend

FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
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
Function Marketwindow(x,y,city,setzoom)
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
;If textbar(x+enl(8),  y+enl(122),enl(271),enl(16),"City Market",7) = False Then gbuttonpressed = 9
;If textbar(x+enl(280),y+enl(122),enl(105),enl(16),"City Stores",7) = False Then gbuttonpressed = 10
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
If mousedown(1) = False And gbuttonpressed > 0 Then
;
;If textbar(enl(x),enl(y),enl(w),enl(12),"City Of Danzig Market Window",0) = False Then gbuttonpressed = 99
;If gbutton4(enl(x+w-12),enl(y+2),small_close_button,98,gbuttonpressed) = True Then gbuttonpressed = 98
;If textbar(enl(x+6),enl(y+h-18),enl(40),enl(17),"View",6) = False Then gbuttonpressed = 97
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
		cmarketscale = -15
		guiscale = -15
		x = 800/2-enl(400/2)
		y = 600/2-enl(300/2)
	End If
; Medium view
Case 95
	If gcheckarea(x+enl(40+40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		cmarketscale = 40
		guiscale = 40
		x = 800/2-400/100*140/2
		y = 600/2-300/100*140/2
	End If
; Large view
Case 94
	If gcheckarea(x+enl(40+40+40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		cmarketscale = 100
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
;Rect MouseX(),MouseY(),10,10,1
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
Function Upgradewindow(x,y,city,setzoom)

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
If mousedown(1) = False And gbuttonpressed > 0 Then

Select gbuttonpressed
Case 99
; Exit window
Case 98 : If gcheckarea(x+enl(w-12),y+enl(2),16,16) = True Then gexitwindow = True
Case 97 :
; Mini View
Case 96
	If gcheckarea(x+enl(40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		cupgradescale = -15
		guiscale = -15
		x = 800/2-enl(w/2)
		y = 600/2-enl(h/2)
	End If
; Medium view
Case 95
	If gcheckarea(x+enl(40+40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		cupgradescale = 40
		guiscale = 40
		x = 800/2-400/100*140/2
		y = 600/2-300/100*140/2
	End If
; Large view
Case 94
	If gcheckarea(x+enl(40+40+40+6),y+enl(h-14),enl(40),enl(12)) = True Then
		cupgradescale = 100
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
;Rect MouseX(),MouseY(),10,10,1
DrawImage mousepointer,MouseX(),MouseY()
Flip
Wend

FreeImage gbackgroundimage
gwait(100):FlushKeys:FlushMouse
End Function







;
;
; This function enlarges or schrinks a display
;
;
;

Function enl#(gval)

If guiscale = 0 Then Return gval

enl# = guiscale#

If enl# => 95 Then SetFont gscalefont100
If enl# => 50 And enl <95 Then SetFont gscalefont50
If enl# =< -0 And enl =>-10 Then SetFont gminifont15
If enl# =< -15 And enl =>-25 Then SetFont gminifont20
If enl# <= -25 Then SetFont gminifont25

ab# = gval
ac# = ab#/100

Return ac# * (100 + enl#)

End Function

;
;
;
;
;
Function gdrawgrid(x,y,w,h,sx,sy,hx,hy)

Color 0,0,50

x1 = x
y1 = y
counter = 0
While y1<y+h
	If counter = hy Then Color 0,0,155 Else Color 0,0,50
	Line x1,y1,x1+w,y1
	y1=y1+sy
	counter = counter + 1 If counter > hy Then counter = 0
Wend
x1 = x
y1 = y
counter = 0
While x1<x+w
	If counter = hx Then Color 0,0,155 Else Color 0,0,50
	Line x1,y1,x1,y1+h
	x1=x1+sx
	counter = counter + 1 If counter > hx Then counter = 0
Wend




End Function