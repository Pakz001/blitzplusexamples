;
;
;
; Listbox - By Nebula (Rudy van Etten)
;
; version 1.0
;
;

;Graphics 640,480,16
;SetBuffer BackBuffer()

Const lb_listmax = 1024
Const lb_numlistboxes = 12
Dim lb_listitem$(lb_numlistboxes,lb_listmax)

;lb_setupdata()

Dim lb_cursor(99)
Dim lb_start(99)
Dim lb_end(99)

;While KeyDown(1) = False
;Cls

;Text 0,0,lb_cursor(0)
;Text 100,0,lb_cursor(1)
;Text 200,0,lb_cursor(2)
;Text 300,0,lb_cursor(3)

;a = listbox(0,10,50,200,200,500,1,False,True)
;Color 255,255,255 : Text 0,20,a
;a = listbox(1,210,50,420,200,500,4,False,True)
;Color 255,255,255 : Text 100,20,a
;a = listbox(2,10,250,200,200,500,3,False,True)
;Color 255,255,255 : Text 200,20,a
;a =  listbox(3,210,250,420,200,2,1,True,False)
;Color 255,255,255 : Text 300,20,a


;Color 0,0,0
;Oval MouseX()-1,MouseY()-1,10,10
;Color 255,255,255
;Oval MouseX(),MouseY(),8,8



;Flip
;Wend
;End

Function listbox(number,lb_left,lb_top,lb_width,lb_height,lb_itemcount,lb_colls,lb_keyactive,lb_buttonsactive,lb_border)

SetFont gsmallfont

; Set the box location values
;lb_left = 10
;lb_top = 50
;lb_width = 165
;lb_height = 200

; Open the window
If lb_border = True Then lb_window(lb_left,lb_top,lb_width,lb_height,0)
; Draw the background
If lb_border = True Then
	Color 255,255,255
	Rect lb_left+4,lb_top+4,lb_width-8,lb_height-6,1
	Else
	Color 255,255,255
	Rect lb_left,lb_top,lb_width,lb_height,1
End If


; Set the true location values
If lb_border = True Then
lb_left = lb_left + 6
lb_top = lb_top + 6
lb_width = lb_width-16
lb_height = lb_height-12
End If



; How many items are y vertical
lb_yitems = (lb_height/12)

; How many collumns do we want
;lb_colls = 2

; How many items are there in the list total
lb_totalsheetitems = lb_colls*lb_yitems

lb_end(number) = lb_totalsheetitems + lb_start(number) 

; Return true if the mouse was pressed on a item
lb_returnvalue = False

;Bound checking for the offset
If lb_start(number) < 0 Then lb_start(number) = 0
If lb_start(number) > lb_itemcount Then lb_start(number) = lb_itemcount



; Calculate the collumnwidth if needed
If lb_colls > 1 Then
	lb_collwidth = lb_width / lb_colls
	Else
	lb_collwidth = lb_width
End If

;Text 0,0,Abs(MouseZSpeed())



If lb_keyactive = True Then
	
	mzs = MouseZSpeed()
	
	; Move the cursor with keys or scrollbutton
	If KeyDown(208) = True Or mzs<0 Then ; down
		azy = 1
		azx = Abs(mzs)
		If azx > 0 Then azy = azx
		;DebugLog azy
		For i=1 To azy
			If lb_cursor(number)<lb_itemcount Then lb_cursor(number) = lb_cursor(number) + 1
		Next
		If lb_cursor(number) => lb_end(number) Then
			lb_start(number) = lb_start(number)+lb_yitems
		End If
		;Delay(50)
		lb_returnvalue = True
	End If
	If KeyDown(200) = True Or mzs>0 Then ; up
		azy = 1
		azx = mzs
		If azx > 0 Then azy = azx
			For i=1 To azy
				lb_cursor(number) = lb_cursor(number) - 1
				If lb_cursor(number)<0 Then lb_cursor(number) = 0
			Next
			If lb_cursor(number)<lb_start(number) Then
				lb_start(number) = lb_start(number) - lb_yitems
				If lb_start(number)<0 Then lb_start(number)=0
			End If
		;Delay(50)
		lb_returnvalue = True
	End If
	If KeyDown(203) = True Then ; left
		If lb_cursor(number)=>lb_yitems Then
			lb_cursor(number) = lb_cursor(number) - lb_yitems
			If lb_cursor(number) <lb_start(number) Then
				lb_start(number) = lb_start(number) - lb_yitems
				If lb_start(number)<0 Then lb_start(number)=0
			End If
			;Delay(90)
		End If
		lb_returnvalue = True
	End If
	If KeyDown(205)=True Then ; right
		If lb_cursor(number)<lb_itemcount Then 
			lb_cursor(number) = lb_cursor(number) + lb_yitems
			If lb_cursor(number) => lb_end(number) Then
				; aza contains the number of items visible on the screen
				; if the count is smaller then a full screen then dont add to
				; lb_start
				aza = lb_itemcount - lb_start(number)
				If aza>lb_totalsheetitems Then
					lb_start(number) = lb_start(number)+lb_yitems
				End If
			End If
			;Delay(90)
			If lb_cursor(number)>lb_itemcount Then 
				lb_cursor(number) = lb_itemcount 	
			End If
		End If
		lb_returnvalue = True
	End If
	
End If ; end if keyactive






counter = lb_start(number) 

; Draw the listbox contents
For x1=0 To lb_colls-1
	For y1=lb_top To lb_top+lb_height-11 Step 12
		
		; Copy the listvalue into a$
		a$ = lb_listitem(number,counter)
		; Copy the value into b$ to
		b$ = a$
		
		; Set a counter to 0
		counter2 = 0
		; If the stringwidth is longer then the width of the collumn then adjust the size
		If StringWidth(b$) > lb_collwidth Then
			While StringWidth(b$) > lb_collwidth ; Start a loop if the string is bigger then the collumnwidth
				b$ = Mid(a$,1,Len(a$)-counter2) ; make a new string
				counter2 = counter2 + 1 ; increase the counter
			Wend
		End If
		
		; Draw the cursor
		If lb_cursor(number) = counter Then
				If lb_border = True Then
					Color 0,0,200
					Rect (x1+lb_left)+lb_collwidth*x1,y1,lb_collwidth,12,1
					Color 255,255,255
				Else
					Color 0,0,200
					Rect ((x1+lb_left)+lb_collwidth*x1),y1,lb_collwidth,12,1
					Color 255,255,255
		
				End If
				
			Else
				Color 0,0,0
		End If
		
		; Draw the string on the screen
		Text (x1+lb_left)+lb_collwidth*x1,y1,b$
		; if the mouse is pressed on a item
		If MouseDown(1) = True Then
			If RectsOverlap(MouseX(),MouseY(),1,1,(x1+lb_left)+lb_collwidth*x1,y1,lb_collwidth,12) = True Then
				lb_cursor(number) = counter
				lb_returnvalue = True
			End If
		End If
		; Increase the counter
		If counter = lb_itemcount Then Goto skipout
		counter = counter + 1
	Next
Next















.skipout

; Control/page buttons
;
;
;

; If we want buttons 
If lb_buttonsactive = True Then


w4 = lb_width/6
x1 = lb_left
;For x1=lb_left To  lb_left+lb_width Step w4
counter = 0
While counter < 6
If MouseDown(1) = True And RectsOverlap(MouseX(),MouseY(),1,1,x1,lb_top+lb_height-7,w4,10) = True Then
lb_window(x1,lb_top+lb_height-7,w4,10,1)
Select counter
Case 0: ; Move to the start of the list
lb_cursor(number) = 0
lb_start(number) = 0
Case 1: ; Move two pages to the left
For i=0 To 1
If lb_cursor(number)=>lb_yitems Then
lb_cursor(number) = lb_cursor(number) - lb_yitems
If lb_cursor(number) <lb_start(number) Then
lb_start(number) = lb_start(number) - lb_yitems
If lb_start(number)<0 Then lb_start(number)=0
End If
End If
Next
;Delay(90)

Case 2: ; Move one page to the left
If lb_cursor(number)=>lb_yitems Then
lb_cursor(number) = lb_cursor(number) - lb_yitems
If lb_cursor(number) <lb_start(number) Then
lb_start(number) = lb_start(number) - lb_yitems
If lb_start(number)<0 Then lb_start(number)=0
End If
;Delay(90)
End If
Case 3: ; Move one page to the right
If lb_cursor(number)<lb_itemcount Then 
lb_cursor(number) = lb_cursor(number) + lb_yitems
If lb_cursor(number) => lb_end(number) Then
; aza contains the number of items visible on the screen
; if the count is smaller then a full screen then dont add to
; lb_start
aza = lb_itemcount - lb_start(number)
If aza>lb_totalsheetitems Then
lb_start(number) = lb_start(number)+lb_yitems
End If
End If
;Delay(90)
If lb_cursor(number)>lb_itemcount Then 
lb_cursor(number) = lb_itemcount 
End If
End If

Case 4: ;Move 2 pages To the right
For i=0 To 1
If lb_cursor(number)<lb_itemcount Then 
lb_cursor(number) = lb_cursor(number) + lb_yitems
If lb_cursor(number) => lb_end(number) Then
; aza contains the number of items visible on the screen
; if the count is smaller then a full screen then dont add to
; lb_start
aza = lb_itemcount - lb_start(number)
If aza>lb_totalsheetitems Then
lb_start(number) = lb_start(number)+lb_yitems
End If
End If
If lb_cursor(number)>lb_itemcount Then 
lb_cursor(number) = lb_itemcount 
End If
End If
Next
;Delay(90)
Case 5: ; Move to the end of the list
lb_cursor(number) = lb_itemcount
aza = lb_itemcount / lb_totalsheetitems
azb = aza * lb_totalsheetitems
azc = lb_itemcount - azb
lb_start(number) = lb_itemcount-azc
End Select
Else
lb_window(x1,lb_top+lb_height-7,w4,10,0)
End If

x1 = x1 + w4
counter = counter + 1
Wend

; Draw the small scrollbar
azib# = (lb_width-16)
azic# = lb_itemcount
azia# = azib#/azic#
anak# = lb_width/lb_itemcount
Color 0,0,0
Rect lb_left+8,lb_top-5,lb_width-14,3
Color 255,0,0
Oval ((lb_left+8)+lb_cursor(number)*azia),lb_top-5,3,3,1

End If ; end if buttons active

Return lb_returnvalue
End Function


Function lb_window(x,y,w,h,t)
If t=0 Then Color 255,255,255 Else Color 0,0,0
Rect x,y,w,h,1
If t=0 Then Color 0,0,0 Else Color 255,255,255
Line x,y+h-1,x+w-1,y+h-1
Line x+w-1,y,x+w-1,y+h-1
If t=0 Then Color 100,100,100 Else Color 255,255,255
Line x+1,y+h-2,x+w-2,y+h-2
Line x+w-2,y+1,x+w-2,y+h-2
Color 150,150,150
Rect x+2,y+2,w-4,h-4,1
End Function

;Function lb_setupdata()
;For number = 0 To 2
;For i=0 To 1024
;Select number
;Case 0
;lb_listitem(number,i) = "Hello  " + i
;Case 1
;lb_listitem(number,i) = "Hello  " + Rand(100)
;Case 2
;lb_listitem(number,i) = Rand(100)
;End Select
;Next
;Next
;lb_listitem(3,0) = "Welcome to this"
;lb_listitem(3,1) = "Listbox example"
;End Function