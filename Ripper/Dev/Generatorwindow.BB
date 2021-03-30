Global reqwinoption = 0
Global reqwin
Global reqbutton1
Global reqbutton2
Global reqtextfield
Global Generator
Global GenOutput
Global GenFormula
Global GenType
Global GenAddNew
Global GenLabel1
Global GenLabel2
Global GenMod1
Global GenMod2
Global GenGenerate
Global Gengeneratesel
Global exitgenloop
; ----Function Begins----
Function CreateGenerator()

Generator = CreateWindow("Code Generator",111,182,522,413,win,15)
;
reqwin = CreateWindow("Please Give New Name",ClientWidth(win)/2-348/2,ClientHeight(win)/2-170/2,348,170,generator,1)
reqtextfield = CreateTextField(32,54,282,24,reqwin)
reqButton1 = CreateButton("Ok",35,104,104,25,reqwin)
reqButton2 = CreateButton("Cancel",205,103,104,25,reqwin)
HideGadget reqwin

;
GenOutput = CreateTextArea(6,198,500,170,Generator,0)
SetGadgetText GenOutput,"Text"
SetGadgetLayout GenOutput,2,2,2,2
GenFormula = CreateTextArea(7,46,500,80,Generator,0)
SetGadgetText GenFormula,"Text"
SetGadgetLayout GenFormula,2,2,2,2
GenType = CreateComboBox(10,11,100,100,Generator,0)

counter = 0
counter2 =0 
exitloop = False

While exitloop = False
	If typedataint(counter2,0) = True And counter < 99-4 Then
		If typedata$(counter2,0) <> "" Then AddGadgetItem gentype,typedata$(counter,0)
		counter = counter + 1
		;Else
		;exitloop = True
	End If
	counter2= counter2 + 1
	If counter2 = 99-4 Then exitloop = True
Wend
AddGadgetItem GenType,"Macro #1"
AddGadgetItem GenType,"Macro #2"
AddGadgetItem GenType,"Macro #3"
AddGadgetItem GenType,"Macro #4"

SelectGadgetItem GenType,0
SetGadgetLayout GenType,2,2,2,2
GenAddNew = CreateButton("Add New",120,11,100,20,Generator,0)
SetGadgetLayout GenAddNew,2,2,2,2
Genrename = CreateButton("Rename",220,11,100,20,Generator,0)
SetGadgetLayout Genrename,2,2,2,2

;genLabel1 = CreateLabel("Modifier X",13,136,60,20,Generator,0)
;SetGadgetLayout GenLabel1,1,0,1,0
;GenLabel2 = CreateLabel("Modifier Y",14,167,60,20,Generator,0)
;SetGadgetLayout GenLabel2,1,0,1,0
;GenMod1 = CreateTextField(83,135,40,20,Generator,0)
;SetGadgetText GenMod1,"0"
;SetGadgetLayout GenMod1,1,0,1,0
;GenMod2 = CreateTextField(83,162,40,20,Generator,0)
;SetGadgetText GenMod2,"0"
;SetGadgetLayout GenMod2,1,0,1,0
GenGenerate = CreateButton("Generate All",304,171,100,20,Generator,0)
SetGadgetLayout GenGenerate,2,0,2,0
GenGeneratesel = CreateButton("Generate Selection",404,171,100,20,Generator,0)
SetGadgetLayout GenGeneratesel,2,2,2,2

;
DisableGadget win
DisableGadget win1

SetGadgetText Genformula,typedata$(0,1)

exitgenloop = False
While exitgenloop = False

	WaitEvent()
	SetTextAreaColor genformula,0,0,0
	Select EventID()
		Case $803 ; Close window
			If EventSource() = generator Then exitgenloop = True
			If EventSource() = reqwin Then HideGadget reqwin : EnableGadget generator : ActivateGadget generator
		Case $401 ; GadgetEvent
			; If the add new is pressed
			If EventSource() = genaddnew Then
				reqwinoption = 0
				ShowGadget reqwin
				DisableGadget generator
				ActivateGadget reqtextfield
			End If
			;
			If EventSource() = reqbutton1 Then ; Ok in requester
				Select reqwinoption
					Case 0
					Addnewtype(TextFieldText$(reqtextfield))
					SelectGadgetItem gentype,CountGadgetItems(gentype)-1
					Case 1
						az = SelectedGadgetItem(gentype)
						RemoveGadgetItem gentype,SelectedGadgetItem(gentype)
						InsertGadgetItem gentype,az,TextFieldText$(reqtextfield)
						SelectGadgetItem gentype,az
						Typedataint(az,0) = True
						Typedata$(az,0) = TextFieldText$(reqtextfield)
				End Select
				HideGadget reqwin
				EnableGadget generator
				ActivateGadget generator
			End If
			
			If EventSource() = reqbutton2 Then ; Cancel in requester
				HideGadget reqwin
				EnableGadget generator
				ActivateGadget generator
			End If
			
			If EventSource() = genrename Then
				reqwinoption = 1
				ShowGadget reqwin
				DisableGadget generator
				ActivateGadget reqtextfield

			End If

			
			; If the type pulldown is used
			If EventSource() = gentype Then
				SetGadgetText genformula,typedata$(SelectedGadgetItem(gentype),1)
			End If
			; If the forula window is modified
			If EventSource() = genformula Then
				typedata$(SelectedGadgetItem(gentype),1) = TextAreaText(genformula)
				typedata$(SelectedGadgetItem(gentype),0) = GadgetItemText(gentype,SelectedGadgetItem(gentype))
			End If
			;
			If EventSource() = gengeneratesel
				generatecode(0)
			End If
			; If the generate Button is pressed
			If EventSource() = gengenerate Then
				generatecode(1)
			End If			
			
	End Select

Wend

;
FreeGadget generator
EnableGadget win
EnableGadget win1
ActivateGadget win
;
End Function


Function Generatecode(inp)
		SetGadgetText genoutput,""
		;
		; Check and read modifiers
		;mx = TextFieldText(genmod1)
		;my = TextFieldText(genmod2)
		;If mx<-1000 Or mx>1000 Then mx = 0
		;If my<-1000 Or my>1000 Then my = 0
		;
		counter = 0
		exitgen = False
		If rects(counter,0) = True Then
		
		While exitgen = False
		If rects(counter,0) = False Then exitgen = True : Exit
		;
		If rects(counter,rectselected) = True Or inp=1 Then
		
		gx2= findgroupx(counter,0)
		gy2= findgroupy(counter,0)
		gx = findgroupx(counter,1)
		gy = findgroupy(counter,1)
		;
		a$ = typedata$(rects(counter,7),1)
		; 
		a$ = Replace$(a$,"#ID",counter)
		a$ = Replace$(a$,"#Id",counter)
		a$ = Replace$(a$,"#id",counter)
		
		If gx2= -2 Then
			a$ = Replace$(a$,"X1",rects(counter,1)-gx)
			a$ = Replace$(a$,"x1",rects(counter,1)-gx)
			a$ = Replace$(a$,"X2",rects(counter,rectwidth)+rects(counter,1))			
			a$ = Replace$(a$,"x2",rects(counter,rectwidth)+rects(counter,1))
			Else
			a$ = Replace$(a$,"X1",rects(counter,1) - gx)
			a$ = Replace$(a$,"X2",rects(counter,3) - gx)			
			a$ = Replace$(a$,"x1",rects(counter,1) - gx)
			a$ = Replace$(a$,"x2",rects(counter,3) - gx)
		End If
		If gy2 = -2 Then						
			a$ = Replace$(a$,"Y2",rects(counter,rectheight)+rects(counter,4))
			a$ = Replace$(a$,"y2",rects(counter,rectheight)+rects(counter,4))
			a$ = Replace$(a$,"Y1",rects(counter,4))
			a$ = Replace$(a$,"y1",rects(counter,4))
			Else
			a$ = Replace$(a$,"Y2",rects(counter,4) - gy)
			a$ = Replace$(a$,"Y1",rects(counter,2) - gy)
			a$ = Replace$(a$,"y2",rects(counter,4) - gy)
			a$ = Replace$(a$,"y1",rects(counter,2) - gy)
		End If
		
		a$ = Replace$(a$,"#Num",rects(counter,rectnumber))
		a$ = Replace$(a$,"#NUM",rects(counter,rectnumber))
		a$ = Replace$(a$,"#num",rects(counter,rectnumber))						

		a$ = Replace$(a$,"WIDTH",rects(counter,5))
		a$ = Replace$(a$,"HEIGHT",rects(counter,6))
		a$ = Replace$(a$,"width",rects(counter,5))
		a$ = Replace$(a$,"height",rects(counter,6))
		a$ = Replace$(a$,"Width",rects(counter,5))
		a$ = Replace$(a$,"Height",rects(counter,6))

		a$ = Replace$(a$,"STRING",Chr(34) + rectsstr$(counter,0) + Chr(34))
		a$ = Replace$(a$,"string",Chr(34) + rectsstr$(counter,0) + Chr(34))
		a$ = Replace$(a$,"String",Chr(34) + rectsstr$(counter,0) + Chr(34))

		
		If rectsstr$(counter,1) <> "" Then
			a$ = Replace$(a$,"Name",rectsstr$(counter,1))
			a$ = Replace$(a$,"NAME",rectsstr$(counter,1))
			a$ = Replace$(a$,"name",rectsstr$(counter,1))
		End If

		If rectsstr$(counter,2) <> "" Then			
			a$ = Replace$(a$,"Group",rectsstr$(counter,2))
			a$ = Replace$(a$,"GROUP",rectsstr$(counter,2))
			a$ = Replace$(a$,"group",rectsstr$(counter,2))
		End If
		
		; 
		AddTextAreaText genoutput,a$+Chr(13)+Chr(10)
		;
		End If
		counter = counter + 1
	Wend
			
End If
End Function 



Function addnewtype$(detxt$)
	AddGadgetItem GenType,detxt$
	AddGadgetItem Objecttype,detxt$
	a = CountGadgetItems(gentype)
	typedataint(a,0) = True
	typedata$(a,0) = detxt$
End Function

; This returns the x location of the group (window or canvas and shit and stuff)
Function findgroupx(num,tp)
	If rectsstr$(num,2) <> "" Then
			For i=0 To numrects
			If rects(i,rectactive) = True Then
				If Lower(rectsstr$(i,1)) = Lower(rectsstr$(num,2)) Then 
					;For ii=5 To 0 Step -1
					a = -1
					If tp = 0 Then
						a = findgroupx(i,1) 
					End If
					;Next
					If a>-1 Then Return -2 Else Return rects(i,rectx1) : End
				End If
			End If
		Next
	End If
	Return - 1
End Function

Function findgroupy(num,Tp)
	If rectsstr$(num,2) <> "" Then
		For i=0 To numrects
			If rects(i,rectactive) = True Then
				If Lower(rectsstr$(i,1)) = Lower(rectsstr$(num,2)) Then 
					;For ii=5 To 0 Step -1
					a = -1
					If tp = 0 Then
						a = findgroupy(i,1) 
					End If
					;Next
					If a>-1 Then Return -2 Else Return rects(i,recty1)
				End If
			End If
		Next
	End If
	Return -1
End Function


