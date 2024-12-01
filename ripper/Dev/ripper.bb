;Const TPM_RETURNCMD =	$100


AppTitle "Interface Ripper/Builder" 

Global win = CreateWindow("Ripper version 0.2beta - By Rudy 'Nebula' van Etten in 2003",0,0,ClientWidth(Desktop()),ClientHeight(Desktop())-24,Desktop())


a$ = Replace$(CurrentDir$(),"\Dev\","")
ChangeDir a$

;hwnd = FindWindow("GX_WIN32_CLASS","Ripper version 0.2beta - By Rudy 'Nebula' van Etten in 2003") 

; Load a set of fonts

Dim defonts(20)

For i=1 To 20
defonts(i) = LoadFont("verdana",i)
Next


Global pancan = CreatePanel(0,40,ClientWidth(win)-20,ClientHeight(win)-60,win)
SetGadgetLayout pancan,1,1,1,1
Global can = CreateCanvas(0,0,ClientWidth(pancan),ClientHeight(pancan),pancan)

Global winvslider = CreateSlider(ClientWidth(win)-20,40,20,ClientHeight(win)-60,win,2)
SetGadgetLayout winvslider,0,2,1,1
SetSliderRange winvslider,400,1024*2

Global winhslider = CreateSlider(0,ClientHeight(win)-20,ClientWidth(pancan),20,win,1)
SetGadgetLayout winhslider,1,1,0,2
SetSliderRange winhslider,200,768*2


Global pan = CreatePanel(0,0,ClientWidth(win),40,win)

Global infolabel = CreateLabel("Mouse Canvas Coordinates",0,0,640,20,pan)
Global piclabel = CreateLabel("Picture Coordinates",0,20,320,20,pan)
HideGadget infolabel
HideGadget piclabel

SetGadgetLayout win,2,1,2,1
SetGadgetLayout can,1,0,1,0
SetGadgetLayout pan,1,1,1,0

Global toolbar = CreateToolBar("gfx/icons.bmp",0,0,60,20,pan )
; activate the toolbars
EnableToolBarItem toolbar,0
EnableToolBarItem toolbar,1
EnableToolBarItem toolbar,2
EnableToolBarItem toolbar,3



SetGadgetLayout infolabel,1,0,1,0
SetGadgetLayout piclabel,1,0,1,0

Global mainimage = CreateImage(ClientWidth(Desktop()),ClientHeight(Desktop()))

men = CreateMenu("File",0,WindowMenu(win))
men01 = CreateMenu("Open Image",1,men)
men02 = CreateMenu("Clear Image",2,men)
men03 = CreateMenu("Set Background Color",3,men)
men03 = CreateMenu("",3003,men)
mena3 = CreateMenu("Load Settings",4,men)
mena3 = CreateMenu("Save Settings",5,men)
mena3 = CreateMenu("",79,men)
mena4 = CreateMenu("About",89,men)
mena5 = CreateMenu("End",99,men)

men2 = CreateMenu("View",100,WindowMenu(win))
men201 = CreateMenu("Show",150,men2)
men202 = CreateMenu("Hide",151,men2)
men203 = CreateMenu("Grid",152,men2)
mena7 = CreateMenu("Show Grid",120,men203)
mena7 = CreateMenu("Hide Grid",121,men203)
mena7 = CreateMenu("Show All",101,men201)
mena8 = CreateMenu("Hide All",102,men202)
mena9 = CreateMenu("Show Object Window",103,men201)
mena10= CreateMenu("Hide Object Window",104,men202)
mena9 = CreateMenu("Show Overview Window",105,men201)
mena10= CreateMenu("Hide Overview Window",106,men202)
mena9 = CreateMenu("Show Edit Window",107,men201)
mena10= CreateMenu("Hide Edit Window",108,men202)



men3 = CreateMenu("Selection",200,WindowMenu(win))
men300 = CreateMenu("Display",210,men3)
men301 = CreateMenu("Select Display Color",201,men300)
men301 = CreateMenu("Select Active Color",202,men300)
men301 = CreateMenu("Select Font Color",203,men300)
men310 = CreateMenu("Selected",211,men3)
men311 = CreateMenu("Copy selected",215,men310)
men311 = CreateMenu("Paste selected",216,men310)
men311 = CreateMenu("Hide selected",212,men310)
men311 = CreateMenu("Resize selected",217,men310)
men311 = CreateMenu("", 1000,men310)
men311 = CreateMenu("Delete selected",214,men310)


men4 = CreateMenu("Objects",300,WindowMenu(win))
mena13 = CreateMenu("Show All",301,men4)
mena13 = CreateMenu("Hide All",302,men4)
mena13 = CreateMenu("Invert",303,men4)
mena13 = CreateMenu("Delete All",304,men4)
mena14 = CreateMenu("Move To Front",305,men4)
mena15 = CreateMenu("Move To Back",306,men4)
mena15 = CreateMenu("",3006,men4)
mena15 = CreateMenu("Load Objects",307,men4)
mena15 = CreateMenu("Save Objects",308,men4)

men5 = CreateMenu("Picture",400,WindowMenu(win))
men501 = CreateMenu("Set to 0,0",401,men5)
men502 = CreateMenu("Set transparent color",402,men5)
men503 = CreateMenu("UnLock Picture",403,men5)
;men504 = CreateMenu("Display Grid",404,men5)
men6 = CreateMenu("Generator",500,WindowMenu(win))
mena15 = CreateMenu("Generate Window",501,men6)





UpdateWindowMenu win

; Objects
Global lastobjectstring$ = "New"
; Types Data
Dim typedataint(99,15)
Dim typedata$(99,15)
typedataint(0,0) = True
  typedata$(0,0) = "Button"
  typedata$(0,1) = "name = CreateButton(String,X1,Y1,Width,Height,group)"
typedataint(1,0) = True
  typedata$(1,0) = "Window"
  typedata$(1,1) = "name = CreateWindow(String,X1,Y1,Width,Height+28,desktop(),1)"
typedataint(2,0) = True
  typedata$(2,0) = "Label"
  typedata$(2,1) = "name = CreateLabel(string,x1,y1,width,height,group)"
typedataint(3,0) = True
  typedata$(3,0) = "Textfield" 
typedata$(3,1) = "name = CreateTextField( x1,y1,width,height,group )"
typedataint(4,0) = True
  typedata$(4,0) = "Text Area" 
typedata$(4,1) = "name = CreateTextArea( x1,y1,width,height,group )"
typedataint(5,0) = True
  typedata$(5,0) = "Combobox" 
typedata$(5,1) = "name = CreateComboBox( x1,y1,width,height,group )"
typedataint(6,0) = True
  typedata$(6,0) = "Listbox" 
typedata$(6,1) = "name = CreateListBox( x1,y1,width,height,group )"
typedataint(7,0) = True
  typedata$(7,0) = "Tabber" 
typedata$(7,1) = "name = CreateTabber( x1,y1,width,height,group )"
typedataint(8,0) = True
  typedata$(8,0) = "Hslider" 
typedata$(8,1) = "name = CreateSlider( x1,y1,width,height,group,0 )"
typedataint(9,0) = True
  typedata$(9,0) = "VSlider" 
typedata$(9,1) = "name = CreateSlider( x1,y1,width,height,group,1 )"
typedataint(10,0) = True
  typedata$(10,0) = "Panel" 
typedata$(10,1) = "name = CreatePanel( x1,y1,width,height,group )"
typedataint(11,0) = True
  typedata$(11,0) = "Canvas" 
typedata$(11,1) = "name = CreateCanvas( x1,y1,width,height,group )"
typedataint(12,0) = True
  typedata$(12,0) = "Checkbox" 
typedata$(12,1) = "name = CreateButton(string, x1,y1,width,height,group,2 )"
typedataint(13,0) = True
  typedata$(13,0) = "RadioButton" 
typedata$(13,1) = "name = CreateButton(string, x1,y1,width,height,group,3 )"






















; Edit settings
Global Editmode = 0 ; 0 = selection 1 = creation
Global rectgrouping = False
Global rectgroupmove = False
Global groupx = 0
Global groupy = 0
Global groupoldx = 0
Global groupoldy = 0

; Grid settings
Global Gridvisible = True
Global gridredraw = True
Global gridimage = CreateImage(ClientWidth(can)+100,ClientHeight(can)+100)

; Overview window
Global overviewvisible = True

.screendrag
Global offsetx = 0
Global offsety = 0
Global oldoffsetx = 0
Global oldoffsety = 0
; Scrolling Dragging Offset of the screen
Global screendrag = False
Global sdstartx = 0
Global sdstarty = 0
Global sdendx = 0
Global sdendy = 0

Global backr = 100
Global backg = 100
Global backb = 100

; Picture location
Global picturelocked = True
Global gridactive = True
Global gridstepx = 5
Global gridstepy = 5
Global px = 0
Global py = 0
Global pxold = 0
Global pyold = 0
; Picture moving
Global movepicture = False
Global pstartx = 0
Global pstarty = 0
Global pendx = 0
Global pendy = 0
; Picture transparancy
Global ptransr = 0
Global ptransg = 0
Global ptransb = 0

; Rectangle dbase setup
Global numrects = 128
Const rectvisible = 8
Const rectselected = 9
Const recttype = 7
Const rectactive = 0
Const rectx1 = 1
Const recty1 = 2
Const rectx2 = 3
Const recty2 = 4
Const rectwidth = 5
Const rectheight = 6
Const rectgroup = 10
Const rectnumber = 11
Dim rects(numrects,15) 	; (0)active,(1)x1,(2)y1,(3)x2,(4)y2,(5)w,(6)h,(7)type,(8)visible,(9)selected
						; (10)group,(11)number
Dim rectsstr$(numrects,5) ; (0)labelstring,(1)Name,(2) = group
Dim backrects(numrects,15) ; backup
Dim backrectsstr$(numrects,5) ; bakcup
Dim copyrects(numrects,15) ; Copy buffer
Dim copyrectsstr$(numrects,5) ; copy buffer

; Rectangle Color values
Global rectnormalr = 255
Global rectnormalg = 0
Global rectnormalb = 0
; Selected
Global rectselectedr = 255
Global rectselectedg = 255
Global rectselectedb = 0

; Font color
Global fontr = 255
Global fontg = 255
Global fontb = 255

; Drawing of the selection (rectangle)

Global nocurrentselection = True ; Is there a current selection
Global risdrawn = False ; is the selection currently being drawn
Global moveselection = False ; 
Global rstartx
Global rstarty
Global rwidth
Global rheight
Global rendx
Global rendy


Include "objectswindow.bb"
Include "generatorwindow.bb"
Include "overview.bb"
Include "edit.bb"
Include "tutorial.bb"
Include "selmodwin.bb"

; Set the buffer to canvas
SetBuffer CanvasBuffer(can)

createobjectswindow()

SetSliderRange objectslider,1,numrects

MaskImage mainimage,ptransr,ptransg,ptransb
ClsColor backr,backg,backb
updatecanvas()

.mainloop
exitloop = False



;hmnu = GetMenu(hwnd)
;hmnu = GetSubMenu(hmnu,2) ;0 = File, 1 = Edit, etc... 


showtutorial("first message")

;selmodwin()

While exitloop = False 

WaitEvent()

dotutorial(EventID(),EventSource())


;ActivateGadget win1

Select EventID()

	Case $803 ; Closebutton
		If EventSource() = win Then exitloop = True
		If EventSource() = win1 Then HideGadget win1
		If EventSource() = overviewwin Then HideGadget overviewwin
		If EventSource() = editwin Then HideGadget editwin
		If EventSource() = tutwin Then HideGadget tutwin
.MouseDowner
	Case $201 ; mouse down
		If EventData() = 3 And EventSource() = can Then
			If nocurrentselection = True Then
				If picturelocked = True Then
					If rectgrouping = False Then
					screendrag = True
					brstartx = MouseX(can) 
					brstarty = MouseY(can)
					
					oldoffsetx = offsetx
					oldoffsety = offsety
					
					; Move the picture					
					;movepicture = True
					;pstartx = MouseX(can) - px
					;pstarty = MouseY(can) - py
					End If
				End If
			End If
		End If
	
		; Update the Overview window
		If EventData() = 1 And EventSource() = overviewcan Then
			updateoverview(MouseX(overviewcan),MouseY(overviewcan),1)
			updatecanvas()
		End If
		
		; start of the current rectangle
		If EventData() = 1 And EventSource() = can Then
			If KeyDown(56) = False And KeyDown(184) = False Then
				If risdrawn = False Then
					risdrawn = True
					rstartx = MouseX(can) 
					rstarty = MouseY(can) 
					rwidth = 0
					rheight = 0
					rendx = MouseX(can) 
					rendy = MouseY(can) 
				End If
			End If
		End If
		; If the middle mouse button is pressed then move along the selection
		If EventData() = 3 And nocurrentselection = False Then
			moveselection = True
		End If
		; If the middle mouse button is pressed then move along the background image
		If EventData() = 3 And nocurrentselection = True And picturelocked = False Then
			movepicture = True
			pstartx = MouseX(can) - px
			pstarty = MouseY(can) - py
		End If
		If EventData() = 3 And rectgrouping = True Then
			rectgroupmove = True
			groupoldx = MouseX(can)
			groupoldy = MouseY(can)
		End If
.mouseup
	Case $202 ; mouse up
		; Update the overview window1
		updateoverview(MouseX(overviewcan),MouseY(overviewcan),0)
		;
		screendrag = False
		;
		;If EventData() = 2 And editmode = 0 And nocurrentselection = False Then
		;
	    ;index = TrackPopupMenuEx (hmnu,TPM_RETURNCMD,MouseX(),MouseY(),hwnd,0)

		;
		;End If
		
		;
		; end of the current rectangle
		If EventData() = 1 And EventSource() = can Then


			; If pressed ALT and mouse 1 then select the highest rectangle under the mouse
			If risdrawn = False Then
				If KeyDown(56) = True Or KeyDown(184) = True Then
					findcollision(MouseX(can),MouseY(can))
				End If
			End If

			If risdrawn = True Then
				nocurrentselection = False
				risdrawn = False
				rendx = MouseX(can) 
				rendy = MouseY(can) 
				translaterect() ; make logical rectangle coordinates
				;
				; If the selection is to small do not show the selection
				If rwidth<2 And rheight<2 Then nocurrentselection = True
				If rwidth<2 And rheight<5 Then nocurrentselection = True
				If rwidth<5 And rheight<2 Then nocurrentselection = True
				;
				If rectgrouping = True Then deselectrects() : rectgrouping = False
				; If select mode is active then activate all rects below the selection
				If editmode = 0 Then selectrects() : nocurrentselection = True
				;
				updatecanvas()
				;
			End If
		End If
		; mouse 2
		If EventData() = 2 And EventSource() = can  Then
			;
			;
			nocurrentselection = True
			rstartx = 0
			rstarty = 0
			rendx = 0
			rendy = 0
			rwidth = 0
			rheight = 0
			updatecanvas()
		End If
		; Mouse 3 (middle)
		If EventData() = 3 And EventSource() = can Then
			movepicture = False
			moveselection = False
			; If a grouping is true and it is being moved then stop moving it
			If rectgrouping = True Then rectgroupmove = False 
			;
			oldpx = px
			oldpy = py
			;
			updatecanvas()

		End If
.mousemove
	Case $203 ; mouse move

			updateinfo()
	
			; Update the current rectangle
			If risdrawn = True Then
				;
				; Update the canvas
				Cls
				drawtheimage(px,py)
				drawgrid()
				drawrects()
				
							
				;drawcurrentrect()
				realdrawcurrentrect()
				FlipCanvas can
			
			End If

			If  screendrag = True Then
			;End
				
				brendx = MouseX(Can)
				brendy = MouseY(can)
				
				movedx = 0
				movedy = 0
				
				; Move the canvas
				If brendx > brstartx Then offsetx = offsetx + brendx-brstartx : movedx = brendx-brstartx
				If brendx < brstartx Then offsetx = offsetx - brstartx+brendx : movedx = brstartx+brendx
				
				; Limit the canvas
				If -offsetx < 0 Then offsetx = 0 
				If -offsetx > 1336 Then offsetx = -1336
				
				
				; Move the canvas
				If brendy > brstarty Then offsety = offsety + brendy-brstarty 
				If brendy < brstarty Then offsety = offsety - brstarty+brendy
				
				; Limit the canvas
				If -offsety < 0 Then offsety = 0 
				If -offsety > 1648 Then offsety = -1648
							; Limit the canvas
				If -offsetx < 0 Then offsetx = 0 
				If -offsetx > 1336 Then offsetx = -1336	
				
				px = offsetx + px
				py = offsety + py

				brstartx = brendx
				brstarty = brendy
				
				; Move the background picture with the screen
				px = pxold + offsetx
				py = pyold + offsety
				
							;
				; Update the canvas
				Cls
				drawtheimage(px,py)
				drawgrid()
				drawrects()
				updateoverview(0,0,0)
				updatecanvas()
				;drawcurrentrect()
				;realdrawcurrentresizerect
				FlipCanvas can
			End If

			; Move the groupings
			If rectgrouping = True And rectgroupmove = True Then
				;
				diffx = groupoldx - EventX()
				diffy = groupoldy - EventY()
				groupoldx = EventX()
				groupoldy = EventY()			
				;
				For i=0 To numrects
				If rects(i,rectactive) = True Then
				If rects(i,rectvisible) = False Then
				If rects(i,rectselected) = True Then
				rects(i,rectx1) = rects(i,rectx1) - diffx
				rects(i,recty1) = rects(i,recty1) - diffy
				rects(i,rectx2) = rects(i,rectx2) - diffx
				rects(i,recty2) = rects(i,recty2) - diffy
				End If
				End If
				End If
				Next
				
				; Update the canvas
				Cls
				drawtheimage(px,py)
				drawgrid()
				drawrects()
				;drawcurrentrect()
;				realdrawcurrentresizerect
				FlipCanvas can
			End If



			
			; Move the selected rectangle
			If moveselection = True Then
				rstartx = MouseX(can)-rwidth/2 
				rstarty = MouseY(can)-rheight/2 
				rendx = MouseX(can) + rwidth / 2 
				rendy = MouseY(can) + rheight / 2 
							;
				; Update the canvas
				Cls
				drawtheimage(px,py)
				drawgrid()
				drawrects()
				;drawcurrentrect()
				realdrawcurrentresizerect
				FlipCanvas can
			End If
			
			; Move the background image
			If risdrawn = False And moveselection = False And movepicture = True Then
				px = EventX() - pstartx
				py = EventY() - pstarty
				pxold = px ; some buffer
				pyold = py ; some buffer
				updatecanvas()
			End If

.theKeyDown
	Case $101 ; keydown
		updateinfo()
		; Modify the current rectangle
		resizecurrentrectangle()
		movecurrentrectangle(EventData())	
		movecurrentpicture()
		updatecanvas()

		; If delete is pressed
		If EventData() = 211 And rectgrouping = True Then 
			For i=0 To numrects
			If rects(i,rectactive) = True Then
			If rects(i,rectvisible) = False Then
			If rects(i,rectselected) = True Then
				rects(i,rectactive) = False
			End If
			End If
			End If
			Next
			sortobjects()
			rectgrouping = False
			updatecanvas()
		End If	
		
.gadgets			
	Case $401 ; gadget event

		; Update the overview window1
		updateoverview(MouseX(overviewcan),MouseY(overviewcan),0)

		recordobject(EventSource()) ; Take the current active rectangle into the current object
		Unhideobjects() ; Unhide all the objects
		newobject(EventSource())
		switchobjects(EventSource()) ; Slide thru the objects


		If EventSource() = toolbar Then
			Select EventData()
				Case 0 ;New
				For i=0 To numrects
					rects(i,0) = False
				Next	
				nocurrentselection = True
				updategroups()
				switchobjects(objectslider)
				updatecanvas()

				Case 1 ;delete selection
				deleteselection()
				Case 2 ;copy selection
				copyselection()
				Case 3 ;past selection
				pasteselection()
			End Select
		End If
		
		; If switching thru object types then record the current object
		If EventSource() = objecttype Then
			If nocurrentselection = False Then
			recordobject(objectrecord)
			switchobjects(objectslider)
			End If
		End If

		; Select Mode
		If EventSource() = editselect Then
			editmode = 0
			nocurrentselection = True
			updatecanvas()
			updateinfo
		End If		
		; Create Mode
		If EventSource() = editcreate Then
			editmode = 1
			updateinfo
			deselectrects
			updatecanvas
		End If

		; Win1 tab
		If EventSource() = win1tab Then
			If SelectedGadgetItem(win1tab) = 0 Then
				HideGadget win1pan2
				ShowGadget win1pan1
			End If
			If SelectedGadgetItem(win1tab) = 1 Then
				HideGadget win1pan1
				ShowGadget win1pan2
			End If

		End If
	

		; Move the sliders	
		If EventSource() = winhslider Then ; horizontal slider moves
			offsetx = -SliderValue(winhslider)
			If picturelocked = True Then px = pxold + offsetx
			updatecanvas()
			updateoverview(0,0,0)
		End If 
		If EventSource() = winvslider Then ; horizontal slider moves
			offsety = -SliderValue(winvslider)
			If picturelocked = True Then py = pyold + offsety
			updatecanvas()
			updateoverview(0,0,0)
		End If 
		
		
		; Record and New object
		If EventSource() = objectrecordnew Then
			recordobject(objectrecord)
			newobject(objectnew)
		End If
		
		; Change the number of the object
		If EventSource() = objectnumber Then
			If EventData() = 13 Then
				a = SliderValue(objectslider)
				rects(a,rectnumber) = TextFieldText(objectnumber)
			End If
		End If

		
		; Give object a name after enter pressed
		If EventSource() = objectnamed Then
			If EventData() = 13 Then
				rectsstr$(SliderValue(objectslider),1) = TextFieldText(objectnamed)
				updategroups()
				switchobjects(objectslider)
			End If
		End If
		
		; Group the object
		If EventSource() = objectgroups Then
			rectsstr$(SliderValue(objectslider),2) = GadgetItemText(objectgroups,SelectedGadgetItem(objectgroups))
		End If
		
		
		; Change string of a object
		If EventSource() = objectstring Then
			If EventData() = 13 Then
				a = SliderValue(objectslider)
				rectsstr$(a,0) = TextFieldText(objectstring)
				lastobjectstring$ = rectsstr$(a,0)
				switchobjects(objectslider)
				updatecanvas()
			End If
		End If

		; Change edit values of objects
		If EventSource() = objectx1
			If EventData() = 13 Then
				a = SliderValue(objectslider)
				rects(a,1) = TextFieldText(objectx1)
				rstartx = rects(a,1)
				rects(a,5) = rects(a,3) - rects(a,1) ; set width
				switchobjects(objectslider)
				updatecanvas()
			End If
		End If
		If EventSource() = objecty1
			If EventData() = 13 Then
				a = SliderValue(objectslider)
				rects(a,2) = TextFieldText(objecty1)
				rstarty = rects(a,2)
				rects(a,6) = rects(a,4) - rects(a,2) ; set height

				switchobjects(objectslider)
				updatecanvas()
			End If
		End If
		If EventSource() = objectx2
			If EventData() = 13 Then
				a = SliderValue(objectslider)
				rects(a,3) = TextFieldText(objectx2)
				rects(a,5) = rects(a,3) - rects(a,1) ; set width
				rendx = rects(a,3)
				rwidth = rects(a,5)
				switchobjects(objectslider)
				updatecanvas()
			End If
		End If
		If EventSource() = objecty2
			If EventData() = 13 Then
				a = SliderValue(objectslider)
				rects(a,4) = TextFieldText(objecty2)
				rects(a,6) = rects(a,4) - rects(a,2) ; set height
				rendy = rects(a,4)
				rheight = rects(a,6)
				switchobjects(objectslider)
				updatecanvas()
			End If
		End If
		If EventSource() = objectwidth
			If EventData() = 13 Then
				a = SliderValue(objectslider)
				rects(a,5) = TextFieldText(objectwidth)
				rects(a,3) = rects(a,1) + rects(a,5)
				rendx = rects(a,3)
				rwidth = rects(a,5)
				switchobjects(objectslider)
				updatecanvas()
			End If
		End If
		If EventSource() = objectheight
			If EventData() = 13 Then
				a = SliderValue(objectslider)
				rects(a,6) = TextFieldText(objectheight)
				rects(a,4) = rects(a,2) + rects(a,6)
				rendy = rects(a,4)
				rheight = rects(a,6)
				switchobjects(objectslider)
				updatecanvas()
			End If
		End If



		
		
		If EventSource() = objecthide Then
			rects(SliderValue(objectslider),8) = ButtonState(objecthide)
			updatecanvas()
		End If
		
		If EventSource() = objectdelete Then
		
		rects(SliderValue(objectslider),0) = False
		SetGadgetText objectx1,""
		SetGadgetText objecty1,""
		SetGadgetText objectx2,""
		SetGadgetText objecty2,""
		SetGadgetText objectwidth,""
		SetGadgetText objectheight,""
		nocurrentselection = True
		
		; Erase backup copy
		For i=0 To numrects
			For ii=0 To 15
				backrects(i,ii) = ""
			Next
			For ii=0 To 5
				backrectsstr$(i,ii) = ""
			Next
		Next
		;Make new backup and sort  
		counter = 0
		For i=0 To numrects
			If rects(i,0) = True Then
				For ii=0 To 15
					backrects(counter,ii) = rects(i,ii)
				Next
				For ii=0 To 5
					backrectsstr$(counter,ii) = rectsstr$(i,ii)
				Next
				counter = counter + 1
			End If
		Next
		; Copy backup into regular arrays
		For i=0 To numrects
			For ii=0 To 15
				rects(i,ii) = backrects(i,ii)
			Next
			For ii=0 To 5
				rectsstr$(i,ii) = backrectsstr$(i,ii)
			Next
		Next
		
		; Reset the objects window
		switchobjects(objectslider)
		
		
		
		; Returns with a canvas update
		updatecanvas
		
		End If
.menus		
	Case $1001; Menu event
		; Open a picture
		If EventData() = 1 Then 
			opentheimage()
			updatecanvas()
		End If
		; Clear the image
		If EventData() = 2 Then
			FreeImage mainimage
			mainimage = CreateImage(ClientWidth(Desktop()),ClientHeight(Desktop()))
			updatecanvas()
		End If
		; Set background color
		If EventData() = 3 Then
			a = RequestColor(backr,backg,backb)
			If a>0 Then
				backr = RequestedRed()
				backg = RequestedGreen()
				backb = RequestedBlue()
				ClsColor backr,backg,backb
			End If
			updatecanvas()
		End If
		
		If EventData() = 4 Then ; Load settings
			az$ = RequestFile("Load File","rcf")
			If FileType(az$) = 1 Then
				file = ReadFile(az$)
				If ReadString(file) = "Ripper v0.1 Config" Then
					; Write the Type Arrays
					For i=0 To 99
						For ii=0 To 15
							;WriteInt file,typedataint(i,ii)
							typedataint(i,ii) = ReadInt(file)
						Next
						For ii=0 To 15
							;WriteString file,typedata$(i,ii)
							typedata$(i,ii) = ReadString(file)
						Next
					Next
					; Write the picture location
					px = ReadInt(file)
					py = ReadInt(file)
			
					pxold = ReadInt(file); Insert <<<<<<<<<<<<<<<<<<
					pyold = ReadInt(file)
				
					lastobjectstring$ = ReadString(file)
				
					offsetx = ReadInt(file)
					offsety = ReadInt(file)
					oldoffsetx  = ReadInt(file)
					oldoffsety  = ReadInt(file)
				
					; Picture location
					picturelocked  = ReadInt(file)
					gridactive = ReadInt(file)
					gridstepx  = ReadInt(file)
					gridstepy  = ReadInt(file)
					; Picture moving
					movepicture = ReadInt(file)
					pstartx = ReadInt(file)
					pstarty = ReadInt(file)
					pendx  = ReadInt(file)
					pendy  = ReadInt(file)
				
					; Edit settings
					Editmode = ReadInt(file)

					; Grid settings
					Gridvisible  = ReadInt(file)
					gridredraw  = ReadInt(file)
					; End inserty <<<<<<<<<<<<
				
					; Write the color settings
					fontr = ReadInt(file)
					fontg = ReadInt(file)
					fontb = ReadInt(file)
					; Write the background settings
					backr = ReadInt(file)
					backg = ReadInt(file)
					backb = ReadInt(file)
					; Write the picture Transparancy
					ptransr = ReadInt(file)
					ptransg = ReadInt(file)
					ptransb = ReadInt(file)
					; Write the normal rectangular colors
					rectnormalr = ReadInt(file)
					rectnormalg = ReadInt(file)
					rectnormalb = ReadInt(file)
					; Write the selected rectangular colors
					rectselectedr = ReadInt(file)
					rectselectedg = ReadInt(file)
					rectselectedb = ReadInt(file)
					;
				End If
				CloseFile(file)
				;
				; Reload the object gadget
				ClearGadgetItems objecttype
				; 
				exitaloop = False
				counter = 0
				While exitaloop = False
					If typedataint(counter,0) = True And counter < 99 Then
						AddGadgetItem ObjectType,typedata$(counter,0)
						counter = counter + 1
					Else
						exitaloop = True
					End If
				Wend
				;
				AddGadgetItem ObjectType,"Macro #1"
				AddGadgetItem ObjectType,"Macro #2"
				AddGadgetItem ObjectType,"Macro #3"
				AddGadgetItem ObjectType,"Macro #4"
				;
				; Read in the correct values for display
				switchobjects(objectslider)
				;
				ClsColor backr,backg,backb
				updategroups()
				updatecanvas()
			End If
		

		End If
		
		If EventData() = 5 Then ; Save settings
			az$ = RequestFile("Save File As","rcf",1)
			;aSetGadgetText win,az$
			If az$<>"" Then
				file = WriteFile(az$)
				WriteString(file,"Ripper v0.1 Config")
				; Write the Type Arrays
				For i=0 To 99
					For ii=0 To 15
						WriteInt file,typedataint(i,ii)
					Next
					For ii=0 To 15
						WriteString file,typedata$(i,ii)
					Next
				Next
				; Write the picture location
				WriteInt file,px
				WriteInt file,py
				
				WriteInt file,pxold ; Insert <<<<<<<<<<<<<<<<<<
				WriteInt file,pyold
				
				WriteString file,lastobjectstring$
				
				WriteInt file,offsetx
				WriteInt file,offsety
				WriteInt file,oldoffsetx 
				WriteInt file,oldoffsety 
				
				; Picture location
				WriteInt file,picturelocked 
				WriteInt file,gridactive
				WriteInt file,gridstepx 
				WriteInt file,gridstepy 
				; Picture moving
				WriteInt file,movepicture
				WriteInt file,pstartx
				WriteInt file,pstarty
				WriteInt file,pendx 
				WriteInt file,pendy 
				
				; Edit settings
				WriteInt file,Editmode

				; Grid settings
				WriteInt file,Gridvisible
				WriteInt file,gridredraw
				
				; End insert <<<<<<<<<<<<<<<<<<
				
				; Write the color settings
				WriteInt file,fontr
				WriteInt file,fontg
				WriteInt file,fontb
				; Write the background settings
				WriteInt file,backr
				WriteInt file,backg
				WriteInt file,backb
				; Write the picture Transparancy
				WriteInt file,ptransr
				WriteInt file,ptransg
				WriteInt file,ptransb
				; Write the normal rectangular colors
				WriteInt file,rectnormalr
				WriteInt file,rectnormalg
				WriteInt file,rectnormalb
				; Write the selected rectangular colors
				WriteInt file,rectselectedr
				WriteInt file,rectselectedg
				WriteInt file,rectselectedb
				;
				CloseFile(file)
			End If
		End If
		
		If EventData() = 89 Then ; About/Info
		Notify "Interface Ripper version 0.1"+Chr(13)+Chr(10)+"Programmed by Rudy 'Nebula' van Etten in 2003"
		End If
	
		If EventData() = 99 Then ; End
			End
		End If

		If EventData() = 101 ; show all
			ShowGadget win1
			ShowGadget overviewwin
			ShowGadget editwin
		End If
		If EventData() = 102 ; hide all
			HideGadget win1
			HideGadget overviewwin
			HideGadget editwin
		End If


		If EventData() = 103 ; show object window
			ShowGadget win1
		End If
		If EventData() = 104 ; hide object window
			HideGadget win1
		End If
		
		If EventData() = 105 ; show object window
			ShowGadget overviewwin
		End If
		If EventData() = 106 ; hide object window
			HideGadget overviewwin
		End If
		
		If EventData() = 107 ; show edit window
			ShowGadget editwin
		End If
		If EventData() = 108 ; hide edit window
			HideGadget editwin
		End If

		

		If EventData() = 120 ; show grid
			gridvisible = True
			updatecanvas()
		End If
		If EventData() = 121 ; hide grid
			gridvisible = False
			updatecanvas()
		End If
		
		If EventData() = 201 Then ; change normal rect color
			If RequestColor(rectnormalr,rectnormalg,rectnormalb) > 0 Then
				rectnormalr = RequestedRed()
				rectnormalg = RequestedGreen()
				rectnormalb = RequestedBlue()
				updatecanvas()
			End If
		End If
		If EventData() = 202 Then ; change selected rect color
			If RequestColor(rectselectedr,rectselectedg,rectselectedb) > 0 Then
				rectselectedr = RequestedRed()
				rectselectedg = RequestedGreen()
				rectselectedb = RequestedBlue()
				updatecanvas()
			End If
		End If
		If EventData() = 203 Then ; change rect font color
			If RequestColor(fontr,fontg,fontb) > 0 Then
				fontr = RequestedRed()
				fontg = RequestedGreen()
				fontb = RequestedBlue()
				updatecanvas()
			End If
		End If
		
		If EventData() = 212 Then ; Hide selection
			hideselection()
			switchobjects(objectslider)
			updatecanvas
		End If
		
		If EventData() = 214 Then ; delete selection
			deleteselection()
			updatecanvas()
		End If
		
		If EventData() = 215; Copy selection
			copyselection()
			updatecanvas()
		End If
		If EventData() = 216; Paste selection
			pasteselection()
			updatecanvas()
		End If
		If EventData() = 217; Resize selection
			selmodwin()
			updatecanvas()
		End If

		
		
		If EventData() = 301 Then ; show All objects
			For i=0 To numrects
				If rects(i,0) = True Then
					rects(i,8) = 0
				End If
			Next	
		End If
		If EventData() = 302 Then ; hide All Objects
			For i=0 To numrects
				If rects(i,0) = True Then
					rects(i,8) = 1
				End If
			Next
			updategroups()
			updatecanvas()
		End If
		If EventData() = 303 Then ; Invert Visibility of Objects
			For i=0 To numrects
				If rects(i,0) = True Then
					If rects(i,8) = 0 Then rects(i,8) = 1 Else rects(i,8) = 0
				End If
			Next
			updategroups()
			updatecanvas()
		End If
		
		If EventData() = 304 Then ; Delete All Objects
			For i=0 To numrects
				For ii=0 To 15
					rects(i,ii) = False
				Next
				For ii=0 To 5
					rectsstr$(i,ii) = ""
				Next
			Next
			updategroups()
			switchobjects(objectslider)
			updatecanvas()
		End If


		If EventData() = 305 Then ; Move Object to Front
			; Clear buffer
			For i=0 To numrects
				For ii=0 To 15
					backrects(i,ii) = ""
				Next
				For ii=0 To 5
					backrectsstr$(i,ii) = ""
				Next
			Next
			; Copy to Buffer and sort it straight away
			counter = 0
			For i=0 To numrects
				If rects(i,0) = True And (Not SliderValue(objectslider) = i) Then
					For ii=0 To 15
						backrects(counter,ii) = rects(i,ii)
					Next
					For ii=0 To 5
						backrectsstr$(counter,ii) = rectsstr$(i,ii)
					Next
					counter = counter + 1
				End If
			Next
			For i=0 To 15
				backrects(counter,i) = rects(SliderValue(objectslider),i)
			Next
			For i=0 To 5
				backrectsstr$(counter,i) = rectsstr$(SliderValue(objectslider),i)
			Next
			; Copy the final result to the main array and update
			
			For i=0 To numrects
				For ii=0 To 15
					rects(i,ii) = backrects(i,ii)
				Next
				For ii=0 To 5
					rectsstr$(i,ii) = backrectsstr$(i,ii)
				Next
			Next
			
			SetSliderValue objectslider,counter
			switchobjects(objectslider)
			updatecanvas()
		End If



		If EventData() = 306 Then ; Move Object to Back
			; Clear buffer
			For i=0 To numrects
				For ii=0 To 15
					backrects(i,ii) = ""
				Next
				For ii=0 To 5
					backrectsstr$(i,ii) = ""
				Next
			Next
			; Copy to Buffer and sort with current selection to front
			For i=0 To 15
				backrects(0,i) = rects(SliderValue(objectslider),i)
			Next
			For i=0 To 5
				backrectsstr$(0,i) = rectsstr$(SliderValue(objectslider),i)
			Next
			
			counter = 1
			For i=0 To numrects
				If rects(i,0) = True And (Not i = SliderValue(objectslider) )Then
					For ii=0 To 15
						backrects(counter,ii) = rects(i,ii)
					Next
					For ii=0 To 5
						backrectsstr$(counter,ii) = rectsstr$(i,ii)
					Next
					counter = counter + 1
				End If
			Next
			; Copy the buffer in the normal arrays and update everything
			counter = 0
			For i=0 To numrects
				For ii=0 To 15
					rects(i,ii) = backrects(i,ii)
				Next
				For ii=0 To 5
					rectsstr$(i,ii) = backrectsstr$(i,ii)
				Next
			Next
			;switchobjects
			updatecanvas()
			
		End If
		If EventData() = 307 Then ; Load Objects
			;
			SetSliderValue objectslider,0
			;
			az$ = RequestFile("Select Objects file","rob")
			If FileType(az$) = 1 Then
				file = ReadFile(az$)
				
				; Write the header
				;WriteString file,"Ripper v0.1 Objects"
				If ReadString$(file) = "Ripper v0.1 Objects" Then
					; Write the number of objects
					numrects = ReadInt(file)
					;
					; Write the objects
					For i=0 To numrects
						For ii=0 To 15
							rects(i,ii) = ReadInt(file)
						Next
						For ii=0 To 5
							rectsstr$(i,ii) = ReadString(file)
						Next
					Next
				End If
				CloseFile(file)
			End If
			;
			updategroups()
			switchobjects(objectslider)
			updatecanvas()
			;
		End If
		If EventData() = 308 Then ; Save Objects
			az$ = RequestFile("Select Location and Filename","rob",1)
			If az$<>"" Then
				file = WriteFile(az$)
				
				; Write the header
				WriteString file,"Ripper v0.1 Objects"
				
				; Write the number of objects
				WriteInt file,numrects
				
				; Write the objects
				For i=0 To numrects
					For ii=0 To 15
						WriteInt file,rects(i,ii)
					Next
					For ii=0 To 5
						WriteString file,rectsstr$(i,ii)
					Next
				Next

				CloseFile(file)
			End If
		End If

		If EventData() = 401 Then ; Reset picture to origin
			;aSetGadgetText win,px + " offsetx " + offsetx
			pxold = 0
			pyold = 0
			px = pxold + offsetx
			py = pyold + offsety
			updatecanvas()
		End If
		
		If EventData() = 402 Then ; Set transparent color
			RequestColor(ptransr,ptransg,ptransb)	
			If (Not RequestedRed() = ptransr) And (Not RequestedGreen() = ptransg) And (Not RequestedBlue() = ptransb) Then
				ptransr = RequestedRed()
				ptransg = RequestedGreen()
				ptransb = RequestedBlue()
				MaskImage mainimage,ptransr,ptransg,ptransb
				updatecanvas()
			End If
		End If

		If EventData() = 403 Then ; Lock image
			If picturelocked = False Then
				picturelocked = True
				SetMenuText men503,"Unlock Picture"
				UpdateWindowMenu(win)
				pxold = px
				pyold = py
				px = pxold - offsetx
				py = pyold - offsety
				Else
				SetMenuText men503,"Lock Picture"
				UpdateWindowMenu(win)
				picturelocked = False
			End If
		End If


		If EventData() = 501 Then ; Activate the generation window
			creategenerator()
		End If

End Select

Wend
End
.endofloop

;
;
; This pastes the current copy buffer onto the screen
Function pasteselection()
;
; Remove current selection
;
For i=0 To numrects
If rects(i,rectactive) = True Then
	If rects(i,rectselected) = True Then
		rects(i,rectselected) = False
	End If
End If
Next
;
; Copy the new selection
counter = 0
For i=0 To numrects
If copyrects(i,rectactive) =  True Then
If copyrects(i,rectselected) = True Then
For ii=0 To numrects
If rects(ii,rectsactive) = False Then
For iii=0 To 15
rects(ii,iii) = copyrects(i,iii)
Next
For iii=0 To 5
rectsstr$(ii,iii) = copyrectsstr$(i,iii)
Next
Exit
End If
Next
End If
End If
Next

End Function

; This copies the current selection into the copybuffer
Function copyselection()
; First counter the number of selected objects and used objects
counter = 0 ; contains the 
counter2 = 0
For i=0 To numrects
	If rects(i,rectsactive) = True Then
		counter2 = counter2 + 1
		If rects(i,rectsselected) = True Then
			counter = counter + 1
		End If
	End If
Next
If counter = 0 Then Notify("Nothing to copy.") : Return
;
; Erase the copybuffer
For i=0 To numrects
	For ii=0 To 15
		copyrects(i,ii) = 0
	Next
	For ii=0 To 5
		copyrectsstr$(i,ii) = ""
	Next
Next
;
; Copy the objects to the buffer
loopcounter = 0 
For i=0 To numrects
If rects(i,rectsactive) = True Then
If rects(i,rectsselected) = True Then
For ii=0 To 15
copyrects(i,ii) = rects(i,ii)
Next
For ii=0 To 5
copyrectsstr$(i,ii) = rectsstr$(i,ii)
Next
End If
End If
Next
;
End Function

;
; This function inserts all object names into the group combobox
;
Function updategroups()

ClearGadgetItems objectgroups
AddGadgetItem objectgroups,"None"
For i=0 To numrects
	If rects(i,rectactive) = True Then
		;If rects(i,rectvisible) = False Then
			If rectsstr$(i,1) <> "" Then
				AddGadgetItem objectgroups,rectsstr$(i,1)
			End If
		;End If
	End If
Next

End Function

;
; This deletes the current selection
;
Function deleteselection()
For i=0 To numrects
	If rects(i,rectactive) = True Then
		If rects(i,rectvisible) = False Then
			If rects(i,rectselected) = True Then
				rects(i,rectactive) = False
			End If
		End If
	End If
Next
sortobjects()
nocurrentselection = True
rectgrouping = False
updatecanvas()
End Function

;
; hide all selected objects
;
Function hideselection()
For i=0 To numrects
If rects(i,rectactive) = True Then
If rects(i,rectselected) = True Then
rects(i,rectvisible) = True
End If
End If
Next
rectgrouping = False
updatecanvas()
End Function

;
; Sorts the objects
;
;
Function sortobjects()
; Erase backup copy
	For i=0 To numrects
		For ii=0 To 15
			backrects(i,ii) = ""
		Next
		For ii=0 To 5
			backrectsstr$(i,ii) = ""
		Next
	Next
	;Make new backup and sort  
	counter = 0
	For i=0 To numrects
		If rects(i,0) = True Then
			For ii=0 To 15
				backrects(counter,ii) = rects(i,ii)
			Next
			For ii=0 To 5
				backrectsstr$(counter,ii) = rectsstr$(i,ii)
			Next
			counter = counter + 1
		End If
	Next
	; Copy backup into regular arrays
	For i=0 To numrects
		For ii=0 To 15
			rects(i,ii) = backrects(i,ii)
		Next
		For ii=0 To 5
			rectsstr$(i,ii) = backrectsstr$(i,ii)
		Next
	Next

switchobjects(objectslider)
End Function


; Deselect all the groupings
Function deselectrects()
For i=0 To numrects
	If rects(i,rectactive) = True Then
		rects(i,rectselected) = False
	End If
Next
rectgrouping = False
End Function
; This function sets the selection flag of the rectangles of all rectangles below the selection

Function selectrects()
rstartx = rstartx - offsetx
rstarty = rstarty - offsety

For i=0 To numrects
	If rects(i,rectactive) = True Then
	If rects(i,rectvisible) = False Then
		If RectsOverlap(rects(i,rectx1),rects(i,recty1),1,1,rstartx,rstarty,rwidth,rheight) = True Then
		If RectsOverlap(rects(i,rectx1)+rects(i,rectwidth),rects(i,recty1),1,1,rstartx,rstarty,rwidth,rheight) = True Then
		If RectsOverlap(rects(i,rectx1),rects(i,recty1)+rects(i,rectheight),1,1,rstartx,rstarty,rwidth,rheight) = True Then
		If RectsOverlap(rects(i,rectx1)+rects(i,rectwidth),rects(i,recty1)+rects(i,rectheight),1,1,rstartx,rstarty,rwidth,rheight) = True Then
			rects(i,rectselected) = True
			rectgrouping = True
		End If
		End If
		End If
		End If
	End If
	End If
Next

rstartx = rstartx + offsetx
rstarty = rstarty + offsety


End Function

; Show info on the screen
Function updateinfo()
	SetGadgetText infolabel,"Mouse Coordinates [ X : " + MouseX(can) + " Y : " + MouseY(can) + " ]" + " Sel X : " + rstartx + " Sel Y : " + rstarty + " Sel Width : " + rwidth + " Sel Height : " + rheight 
	SetGadgetText piclabel,"Picture Coordinates [ X : " + px + " Y : " + py + " ]" + " Offsetx : " + offsetx + " Offsety : " + offsety
	
	b$= ""
	b$ = b$ + "Current Mode : "
	If editmode = 1 Then b$ = b$ + "Creation " Else b$ = b$ + "Selection " 
	b$=b$ + String(" ",10)
	
	If Len(b$) < 50 Then b$ = b$ + String(" ",50-Len(b$))
	
	c$ = ""
	c$ = c$ + " Canvas : " + MouseX(can) + " , " +  MouseY(can)
	c$=c$ + String(" ",10)	
	
	If Len(c$) < 50 Then c$ = c$ + String(" ",50-Len(c$))
	
	d$ = ""
	d$ = d$ + " Real : " + (MouseX(can)-offsetx) + " , " +  (MouseY(can)-offsety)
	d$=d$ + String(" ",10)	
	
	If Len(d$) < 50 Then d$ = d$ + String(" ",50-Len(d$))

	
	SetStatusText win,b$+c$+d$
End Function

Function updateoverview(x,y,theevent)
x1# = -offsetx
y1# = -offsety
w1# = ClientWidth(overviewcan)
h1# = ClientHeight(overviewcan)
SetBuffer CanvasBuffer(overviewcan)
Cls
stepx# = w1#/(1024*2)
stepy# = h1#/(768*2)/2

w2# = ClientWidth(win) / stepx# / 25
h2# = ClientHeight(win) / stepy# / 25

Color 255,255,255

Rect stepx#*x1#,stepy#*y1#,w2#*stepx#,h2#*stepy#,0

; If the mouse was pressed
If theevent = 1 Then
offsetx = -(x*(w2/w1))*2.8
offsety = -(y*(h2/h1))*2.1
;
SetSliderValue(winhslider,-offsetx)
SetSliderValue(winvslider,-offsety)

If picturelocked = True Then px = offsetx : py=offsety : pxold = offsetx : pyold = offsety
;
End If

;Rect 0,0,10,10,1

FlipCanvas overviewcan
SetBuffer CanvasBuffer(can)

End Function


Function newobject(theevent)
	If theevent = objectnew Then
		nonefound = True
		For i=0 To numrects
			If rects(i,0) = False Then
				SetSliderValue objectslider,i
				updategroups()
				switchobjects(objectslider)
				nonefound = False
				Exit
			End If
		Next
		If nonefound = True Then Notify "No more Objects Left"
	End If
End Function


Function findcollision(x,y)

foundit = False
For i=0 To numrects
If rects(i,0) = True Then
If rects(i,8) = 0 Then
x1 = rects(i,1)
y1 = rects(i,2)
w = rects(i,5)
h = rects(i,6)
If RectsOverlap(x1+offsetx,y1+offsety,w,h,x,y,1,1) = True Then 
ontopshit = i
foundit = True
End If
End If
End If
Next

If foundit = True Then
	SetSliderValue objectslider,ontopshit
	switchobjects(objectslider)
End If

End Function

Function unhideobjects()
; This function unhides all objects
If EventSource() = objectunhide Then
	For i=0 To numrects
		rects(i,8) = 0
	Next
	SetButtonState objecthide,0
	updategroups()
	updatecanvas()
End If
End Function


Function updatecanvas()
	;
	; Update the canvas
	Cls
	SetSliderValue winhslider,-offsetx
	SetSliderValue winvslider,-offsety
	drawtheimage(px,py)
	drawgrid()
	drawrects()
	drawcurrentrect()
	FlipCanvas can
End Function


;
; Show/Build/Scroll the grid
Function drawgrid()

;Global Gridvisible = True
;Global gridredraw = True
;Global gridimage = CreateImage(ClientWidth(can),ClientHeight(can))

If gridredraw = True Then
gridredraw = False
SetBuffer ImageBuffer(gridimage)
x1 = 0
y1 = 0
While y1<ImageHeight(gridimage)
	Color 255,255,255
	Rect x1,y1,51,51,0
	If x1 > ClientWidth(can) Then x1 = -50 : y1=y1+50
	x1 = x1 + 50
Wend

x1 = 0
y1 = 0

While y1<ImageHeight(gridimage)
	Color 0,0,0
	Plot x1-1,y1
	Plot x1,y1
	Plot x1+1,y1
	Plot x1,y1-1
	Plot x1,y1+1
	If x1 > ImageWidth(gridimage) Then x1 = -5 : y1=y1+5
	x1 = x1 + 5
Wend


x1 = 0
y1 = 0
While y1<ImageHeight(gridimage)
	Color 255,255,0
	Plot x1+5,y1+5
	If x1 > ImageWidth(gridimage) Then x1 = -10 : y1=y1+10
	x1 = x1 + 10
Wend

SetBuffer CanvasBuffer(can)
End If

; Create scrolling offset
offsx = (offsetx / 50) * 50
offsy = (offsety / 50) * 50
offsx = offsetx - offsx 
offsy = offsety - offsy

; Display with scrolling offset
If gridvisible = True Then DrawImage gridimage,offsx,offsy


End Function

Function switchobjects(theevent)
		; Switch between objects
		If theevent = objectslider Then
			SetGadgetText objectname,"Object #"+SliderValue(objectslider)
			;
			If SliderValue(objectslider) < numrects Then ;rects(SliderValue(objectslider),0) = True Then
				; Switch coordinates
				SetGadgetText objectx1,rects(SliderValue(objectslider),1)
				SetGadgetText objecty1,rects(SliderValue(objectslider),2)
				SetGadgetText objectx2,rects(SliderValue(objectslider),3)
				SetGadgetText objecty2,rects(SliderValue(objectslider),4)
				SetGadgetText objectwidth,rects(SliderValue(objectslider),5)
				SetGadgetText objectheight,rects(SliderValue(objectslider),6)
				; Read the Label
				SetGadgetText objectstring,rectsstr(SliderValue(objectslider),0)
				; Read the type
				SelectGadgetItem objecttype,rects(SliderValue(objectslider),7)
				; Read the visible status
				SetButtonState objecthide,rects(SliderValue(objectslider),8)
				;
				SetGadgetText objectnamed,rectsstr$(SliderValue(objectslider),1)
				
				; Find the item group index
				
				returnvalue = 0
				If CountGadgetItems(objectgroups) > 0
					For ii=0 To CountGadgetItems(objectgroups)-1
					If rectsstr$(SliderValue(objectslider),2) = GadgetItemText(objectgroups,ii) Then returnvalue = ii : Exit
					Next
				End If
				SelectGadgetItem objectgroups,returnvalue
				;
.shit			; set the modifyable number from the array
				SetGadgetText objectnumber,rects(SliderValue(objectslider),rectnumber)
				;
				; If the current object is active/in use
				If rects(SliderValue(objectslider),rectactive) = True Then
					; Re activate the current rectangle
					rstartx = rects(SliderValue(objectslider),1) + offsetx
					rstarty = rects(SliderValue(objectslider),2) + offsety
					rendx = rects(SliderValue(objectslider),3) + offsetx
					rendy = rects(SliderValue(objectslider),4) + offsety
					rwidth = rects(SliderValue(objectslider),5)
					rheight = rects(SliderValue(objectslider),6)
					; 
					nocurrentselection = False
				End If
				;
				updatecanvas()
			Else
				SetGadgetText objectx1,""
				SetGadgetText objectx2,""
				SetGadgetText objecty1,""
				SetGadgetText objecty2,""
				SetGadgetText objectwidth,""
				SetGadgetText objectheight,""
				; Read the Label
				SetGadgetText objectstring,lastobjectstring$
			End If
			;
		End If

End Function

Function recordobject(theevent)
		; Record the current selected Rectangle into the displayed object
		If theevent = objectrecord Then 
			If nocurrentselection = False Then
				; Copy the coordinates
				SetGadgetText objectx1,rstartx - offsetx
				SetGadgetText objectx2,rendx - offsetx
				SetGadgetText objecty1,rstarty - offsety
				SetGadgetText objecty2,rendy - offsety
				SetGadgetText objectwidth,rwidth
				SetGadgetText objectheight,rheight
				; Fill coordinates into the array
				rects(SliderValue(objectslider),0) = True
				rects(SliderValue(objectslider),1) = rstartx - offsetx
				rects(SliderValue(objectslider),2) = rstarty - offsety
				rects(SliderValue(objectslider),3) = rendx - offsetx
				rects(SliderValue(objectslider),4) = rendy - offsety
				rects(SliderValue(objectslider),5) = rwidth
				rects(SliderValue(objectslider),6) = rheight
				; Fill object name into the array
				rectsstr(SliderValue(objectslider),0) = TextFieldText(objectstring)
				; Record the type
				rects(SliderValue(objectslider),7) = SelectedGadgetItem(objecttype)
				; Record the hide status
				rects(SliderValue(objectslider),8) = ButtonState(objecthide)
				updategroups()
				updatecanvas()
				switchobjects(objectslider)
			End If
		End If
		End Function
		;
; Move picture and canvas

Function movecurrentpicture()						
		; Move picture
		If KeyDown(157) = False And KeyDown(29) = False Then ; ctrsls
			If KeyDown(42) = False And KeyDown(54) = False Then ; shifts

				If EventData() = 203 Then px = px + 5 : offsetx = offsetx + 5 ; Right
				If EventData() = 208 Then py = py - 5 : offsety = offsety - 5 ; up
				If EventData() = 200 Then py = py + 5 : offsety = offsety + 5 ;down
				If EventData() = 205 Then px = px - 5 : offsetx = offsetx - 5 ; Left

				; Limit the canvas
				If -offsety < 0 Then offsety = 0 : py=pyold Else pyold = py 
				If -offsety > 1648 Then offsety = -1648 : py=pyold Else pyold = py
							; Limit the canvas
				If -offsetx < 0 Then offsetx = 0 : px=pxold Else pxold = px
				If -offsetx > 1336 Then offsetx = -1336	: px=pxold Else pxold = px
				
			End If
		End If
End Function 

		Function movecurrentrectangle(theevent)
		; Move rectangle
		If KeyDown(157) = True Or KeyDown(29) = True Then ; ctrls
			If KeyDown(42) = False And KeyDown(54) = False Then ; shifts
				If theevent=205 Then 				; right
					rstartx = rstartx + 1
					rendx = rendx + 1
					rstarty = rstarty + 0
					rendy = rendy + 0
				End If
				If theevent = 200 Then 				; up
					rstartx = rstartx + 0
					rendx = rendx + 0
					rstarty = rstarty - 1
					rendy = rendy - 1
				End If
				If theevent = 208 Then 				; down
					rstartx = rstartx + 0
					rendx = rendx + 0
					rstarty = rstarty + 1
					rendy = rendy + 1
				End If
				If theevent = 203 Then 				; Left
					rstartx = rstartx - 1
					rendx = rendx - 1
					rstarty = rstarty + 0
					rendy = rendy + 0
				End If
			End If
		End If
		translaterect()
		End Function



Function resizecurrentrectangle()
		; Resize rectangle
		If KeyDown(157) = True Or KeyDown(29) = True Then ; ctrls
			If KeyDown(42) = True Or KeyDown(54) = True Then ; shifts
				If EventData() = 205 Then 				; right
					rstartx = rstartx + 0
					rendx = rendx + 1
					rstarty = rstarty + 0
					rendy = rendy + 0
				End If
				If EventData() = 200 Then 				; up
					rstartx = rstartx + 0
					rendx = rendx + 0
					rstarty = rstarty - 0
					rendy = rendy - 1
				End If
				If EventData() = 208 Then 				; down
					rstartx = rstartx + 0
					rendx = rendx + 0
					rstarty = rstarty + 0
					rendy = rendy + 1
				End If
				If EventData() = 203 Then 				; Left
					rstartx = rstartx - 0
					rendx = rendx - 1
					rstarty = rstarty + 0
					rendy = rendy + 0
				End If			
			End If
		End If
End Function



Function translaterect()
If rstartx > rendx Then 
a = rstartx
b = rendx
rendx = a
rstartx = b
End If
If rstarty > rendy Then
a = rstarty
b = rendy
rendy = a
rstarty = b
End If

rwidth = rendx - rstartx
rheight = rendy - rstarty

End Function

Function drawcurrentrect()
	If nocurrentselection = True Then Return
	Color rectselectedr,rectselectedg,rectselectedb
	a = rstartx
	b = rstarty
	c = rendx
	d = rendy
	If rstartx > c Then a = rendx : c = rstartx
	If rstarty > d Then b = rendy : d = rstarty
	w = c - a
	h = d - b
	Rect a,b,w,h,0
End Function

Function realdrawcurrentresizerect()
	Color rectselectedr,rectselectedg,rectselectedb
	a = rstartx
	b = rstarty
	c = rendx
	d = rendy
	If rstartx > c Then a = rendx : c = rstartx
	If rstarty > d Then b = rendy : d = rstarty
	w = c - a
	h = d - b
	Rect a,b,w,h,0
End Function



Function realdrawcurrentrect()
	Color rectselectedr,rectselectedg,rectselectedb
	a = rstartx 
	b = rstarty 
	c = MouseX(can) 
	d = MouseY(can) 
	If rstartx > c Then a = MouseX(can) : c = rstartx
	If rstarty > d Then b = MouseY(can) : d = rstarty
	w = c - a
	h = d - b
	Rect a,b,w,h,0
End Function

Function drawrects()
For i=0 To numrects
	If rects(i,0) = True And rects(i,8) = 0 Then
	
	If rects(i,rectselected) = True Then
		Color rectselectedr,rectselectedg,rectselectedb
		Else
		Color rectnormalr,rectnormalg,rectnormalb
	End If
	
	
	Rect rects(i,1)+offsetx,rects(i,2)+offsety,rects(i,5),rects(i,6),0
	;
	a = rects(i,1) + offsetx 
	b = rects(i,2) + offsety
	w = rects(i,5) 
	h = rects(i,6) 
	
	xmod1 = 0
	ymod1 = 0
	If rects(i,7) = 1 Then 
		xmod1 = 0
		ymod1 = 1
	End If
	
	SetFont defonts(20)
	If StringWidth(rectsstr(i,0)) > w Then
	For zi=20 To 1 Step -1
		SetFont defonts(zi)
		If StringWidth(rectsstr(i,0)) < w Then Exit
	Next
	End If


	; Draw the label
	For x1=-1 To 1
	For y1=-1 To 1
		Color 0,0,0
		If ymod1 = 0 Then 
	
			Text (a+w/2)+x1,(b+h/2)+y1,rectsstr(i,0),1,1
	
		Else
	
			Text (a+w/2)+x1,(b+6)+y1,rectsstr(i,0),1,1
	
		End If
	Next
	Next
	Color fontr,fontg,fontb
	If ymod1 = 0 Then
		Text (a+w/2),(b+h/2),rectsstr(i,0),1,1
		Else
		Text (a+w/2),(b+6),rectsstr(i,0),1,1
	End If
	;
	
	End If
Next
End Function

Function opentheimage()
a$ = RequestFile("Select the image","*.*")
If FileType(a$) = 1 Then 
mainimage = LoadImage(a$)
End If

End Function

Function DrawtheImage(x,y)
DrawImage mainimage,x,y
End Function

Function rgb(r,g,b) 
Return (b Or (g Shl 8) Or (r Shl 16) Or ($ff000000)) 
End Function