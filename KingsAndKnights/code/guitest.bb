Graphics 800,600,16,2
SetBuffer BackBuffer()
; Include the gui code
Include "isoeconomy.bb"
Include "isolistbox.bb"
Include "isogui.bb"



Global mousepointer = LoadImage("graphics\mousepointer2.bmp")

SetFont gsmallfont

Global output ; displays the output that (some) functions return
Global stringoutput$ ; display string output

; Initial button state
Global buttonpressed = 0
; Initial field state
Global fieldpressed = 0 


; Background color
ClsColor 75,75,255

; Needed for slider
slideractive = False
sliderpos = 0


cmarketscale = 100
cupgradescale = 100

fps = 60
fpstimer = MilliSecs()
fptcounter = 60

Repeat
	Cls	
	SetFont grequesterfont
	Text 0,0,output
	Text 0,20,stringoutput$
	
	If fpstimer < MilliSecs() Then
	fpstimer = MilliSecs() + 1000
	fps = fpscounter
	fpscounter = 0
	End If
	fpscounter = fpscounter + 1
	

	gwindow(0,0,800,600,0,0,4)
	
	showbuttons()	



	
	Text 200,190,"Textboxes"
	counter = 0 
	For y=0 To 3
	For x=0 To 3
	gtextbox(200+x*100,200+y*16,100,16,gfield$(counter),counter)
	counter = counter + 1
	Next
	Next
	
	;
	; Slider
	;
	If slideractive = False Then slideractive = gslider(50,100,14,100,sliderpos,100) 
	
	If MouseDown(1) = True And slideractive=True Then
	sliderpos = gpositionslider(50,100,14,100,sliderpos,100)
	output = sliderpos
	End If
	If MouseDown(1) = False And slideractive=True Then
	slideractive = False
	gslider(50,100,14,100,sliderpos,100)
	End If
	
	
	
	Rect MouseX(),MouseY(),5,5,1
	SetFont fonts(15)
	Text 40,40,"Frames : " + fps
	Flip
Until KeyHit(1)=True
End

;
; Do the buttons
;
Function showbuttons()
	SetFont gsmallfont
	; Here the buttons are drawn and are put to work
	If gbutton2(100,100,100,50,"File Requester",1,1,buttonpressed) = True Then buttonpressed=1
	If gbutton1(700,0,100,50,"quit",2,buttonpressed) = True Then buttonpressed=2
	If gbutton1(100,300,100,50,"Listbox",3,buttonpressed) = True Then buttonpressed=3
	If gbutton1(100,350,100,50,"Message",4,buttonpressed) = True Then buttonpressed=4	
	If gbutton1(100,400,100,50,"Requester m1",5,buttonpressed) = True Then buttonpressed=5
	If gbutton1(100,450,100,50,"Requester m2",6,buttonpressed) = True Then buttonpressed=6
	If gbutton1(100,500,100,50,"Inputbox",7,buttonpressed) = True Then buttonpressed = 7

	If gbutton4(300,400,big_up_arrow,8,buttonpressed) = True Then buttonpressed = 8
	If gbutton4(330,400,big_left_arrow,9,buttonpressed) = True Then buttonpressed = 9
	If gbutton4(360,400,big_down_arrow,10,buttonpressed) = True Then buttonpressed = 10
	If gbutton4(390,400,big_right_arrow,11,buttonpressed) = True Then buttonpressed = 11

	If gbutton4(300,440,small_up_arrow,12,buttonpressed) = True Then buttonpressed = 12
	If gbutton4(330,440,small_left_arrow,13,buttonpressed) = True Then buttonpressed = 13
	If gbutton4(360,440,small_down_arrow,14,buttonpressed) = True Then buttonpressed = 14
	If gbutton4(390,440,small_right_arrow,15,buttonpressed) = True Then buttonpressed = 15

	If gbutton4(300,480,big_top_button,16,buttonpressed) = True Then buttonpressed = 16
	If gbutton4(330,480,big_bottom_button,17,buttonpressed) = True Then buttonpressed = 17
	If gbutton4(360,480,small_top_button,18,buttonpressed) = True Then buttonpressed = 18
	If gbutton4(390,480,small_bottom_button,19,buttonpressed) = True Then buttonpressed = 19

	If gbutton4(430,480,big_close_button,20,buttonpressed) = True Then buttonpressed = 20
	If gbutton4(462,480,small_close_button,21,buttonpressed) = True Then buttonpressed = 21

	If gbutton1(480,400,100,50,"Market",22,buttonpressed) = True Then buttonpressed=22
	If gbutton1(480,450,100,50,"City Upgrade",23,buttonpressed) = True Then buttonpressed=23
	If gbutton1(480,500,100,50,"Production",24,buttonpressed) = True Then buttonpressed = 24
	If gbutton1(480,550,100,50,"Diplomacy",25,buttonpressed) = True Then buttonpressed = 25
	If gbutton1(580,550,100,50,"Prodution",25,buttonpressed) = True Then buttonpressed = 26




	
	; Here the buttons are executed
	If MouseDown(1)=False And buttonpressed>0 Then
	; button 22
	If gcheckarea(480,400,100,50) = True And buttonpressed = 22 Then 
		marketwindow(-1,-1,10,cmarketscale)
	End If
	; button 23
	If gcheckarea(480,450,100,50) = True And buttonpressed = 23 Then 
		upgradewindow(-1,-1,10,cupgradescale)
	End If
	; button 25
	If gcheckarea(480,550,100,50) = True And buttonpressed = 25 Then 
		gDiplomacy(-1,-1)
	End If
	; Button 26
	If gcheckarea(580,550,100,50) = True And buttonpressed = 26 Then
		gproduction(-1,-1)
	End If

		
	; button 1
	If gcheckarea(100,100,100,50) = True And buttonpressed = 1 Then 
		SetFont grequesterfont
		filename$ = gfilerequester(100,100,"c:\program files\blitz basic","untitled.map","*.*")
		SetFont gsmallfont
	End If
	; button 2
	If gcheckarea(700,0,100,50) = True And buttonpressed = 2 Then End					
	; button 3 ( listbox )
	If gcheckarea(100,300,100,50) = True And buttonpressed = 3 Then
		SetFont grequesterfont
		For i=0 To 50
		glistfield$(i) = i
		Next
		output = glistbox(0,0,300,400,"Select item",50)
		SetFont gsmallfont
	End If
	
	; button 4
	If gcheckarea(100,350,100,50) = True And buttonpressed = 4 Then
	; textviewer has it's own font assignment (grequesterfont and gsmallfont)
	greadtext("tutorial.txt",Rand(0,13))
	;gtextviewer(100,100,400,300)
	gtutorial(100,100,350,300)

	End If
	
	
	
	
	; Button 5
	If gcheckarea(100,400,100,50) = True And buttonpressed = 5 Then
	output = grequest(100,100,300,170,"Message","You are going","to select a city","Continue ?","Yes","No")
	End If
	;Button 6
	If gcheckarea(100,450,100,50) = True And buttonpressed = 6 Then
	output =grequest(100,100,300,170,"Message","","The End","","Ok","")
	End If
	; button 7
	If gcheckarea(100,500,100,50) = True And buttonpressed = 7 Then
	stringoutput$ = ginputbox(100,100,300,170,"Please input your name :","John Smith","Ok")
	End If		

	buttonpressed=0
	End If
End Function


;
; Here a function to give the user some information
;
;
Function simplemessage(s$)
exitmes$ = "Press any key to continue"
w = StringWidth(s)
If StringWidth(exitmes)>w Then w = StringWidth(exitmes)
;
;Color 0,0,0
;Rect screenwidth/2-(w/2+20),screenheight/2-100,w+40,200,1
x = 800/2-(w/2+20)
y = 600/2-100
w1 = w+40
h1 = 200
gwindow(x,y,w1,h1,1,0,0)
gwindow(x+5,y+10,w1-10,h1-20,0,1,1)
Color 0,0,0
Text 800/2,600/2-40,s,1,1
Text 800/2,600/2,exitmes,1,1
Flip
Delay(50)
FlushKeys()
;WaitKey
While KeyDown(1) = False And MouseDown(1)=False And MouseDown(2) = False And KeyDown(57)=False And KeyDown(28) = False 
Delay(1)
Wend
Delay(200)
FlushKeys()
End Function