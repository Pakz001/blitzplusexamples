Global win1tab
Global win1pan1
Global win1pan2
Global objnamelabel
Global objectnamed
Global objectgroups
Global objnumlabel 
Global objectnumber 



Global win1
Global ObjectString
Global ObjectAreaLabel1
Global ObjectX1
Global ObjectY1
Global ObjectX2
Global ObjectY2
Global ObjectLabel2
Global ObjectLabel4
Global ObjectLabel3
Global ObjectLabel5
Global ObjectWidth
Global ObjectHeight
Global ObjectLabel6
Global ObjectLabel7
Global ObjectSlider
Global ObjectName
Global ObjectDelete
Global ObjectNew
Global ObjectRecord
Global ObjectRecordNew
Global ObjectUnhide
Global ObjectHide
Global ObjectType

; ----Function Begins----
Function CreateObjectswindow()
win1 = CreateWindow("Objects Editor",ClientWidth(win)-255,240,236,360,win,1)

win1tab = CreateTabber(0,0,236,20,win1)
AddGadgetItem win1tab,"Main"
AddGadgetItem win1tab,"Extra"

win1pan1 = CreatePanel(0,74,240,310,win1)

ObjectSlider = CreateSlider(60,44,100,20,win1)
SetGadgetLayout ObjectSlider,1,0,1,0
ObjectName = CreateLabel("Object #0",73,24,100,20,win1)
SetGadgetLayout ObjectName,1,0,1,0



ObjectString = CreateTextField(72,0,150,20,win1pan1,0)
SetGadgetText ObjectString,"Default"
SetGadgetLayout ObjectString,1,0,1,0
ObjectAreaLabel1 = CreateLabel("Area Text",8,59-57,60,20,win1pan1,0)
SetGadgetLayout ObjectAreaLabel1,1,0,1,0
ObjectX1 = CreateTextField(49,97-57,40,20,win1pan1,0)
SetGadgetText ObjectX1,""
SetGadgetLayout ObjectX1,1,0,1,0
ObjectY1 = CreateTextField(49,128-57,40,20,win1pan1,0)
SetGadgetText ObjectY1,""
SetGadgetLayout ObjectY1,1,0,1,0
ObjectX2 = CreateTextField(158,94-57,40,20,win1pan1,0)
SetGadgetText ObjectX2,""
SetGadgetLayout ObjectX2,1,0,1,0
ObjectY2 = CreateTextField(158,126-57,40,20,win1pan1,0)
SetGadgetText ObjectY2,""
SetGadgetLayout ObjectY2,1,0,1,0
ObjectLabel2 = CreateLabel("X1",18,99-57,20,20,win1pan1,0)
SetGadgetLayout ObjectLabel2,1,0,1,0
ObjectLabel4 = CreateLabel("Y1",19,129-57,20,20,win1pan1,0)
SetGadgetLayout ObjectLabel4,1,0,1,0
ObjectLabel3 = CreateLabel("X2",128,97-57,20,20,win1pan1,0)
SetGadgetLayout ObjectLabel3,1,0,1,0
ObjectLabel5 = CreateLabel("Y2",127,128-57,20,20,win1pan1,0)
SetGadgetLayout ObjectLabel5,1,0,1,0
ObjectWidth = CreateTextField(50,155-57,40,20,win1pan1,0)
SetGadgetText ObjectWidth,""
SetGadgetLayout ObjectWidth,1,0,1,0
ObjectHeight = CreateTextField(158,156-57,40,20,win1pan1,0)
SetGadgetText ObjectHeight,""
SetGadgetLayout ObjectHeight,1,0,1,0
ObjectLabel6 = CreateLabel("Width",7,156-57,40,20,win1pan1,0)
SetGadgetLayout ObjectLabel6,1,0,1,0
ObjectLabel7 = CreateLabel("Height",113,157-57,40,20,win1pan1,0)
SetGadgetLayout ObjectLabel7,1,0,1,0
ObjectRecordNew = CreateButton("Record+New",3,254-57,100,20,win1pan1,0)
SetGadgetLayout ObjectRecordNew,1,0,1,0
ObjectDelete = CreateButton("Delete Object",121,254-57,100,20,win1pan1,0)
SetGadgetLayout ObjectDelete,1,0,1,0
ObjectNew = CreateButton("New object",121,228-57,100,20,win1pan1,0)
SetGadgetLayout ObjectNew,1,0,1,0
ObjectRecord = CreateButton("Record Current",3,228-57,100,20,win1pan1,0)
SetGadgetLayout ObjectRecord,1,0,1,0
ObjectUnhide = CreateButton("Unhide All",124,282-57,100,20,win1pan1,0)
SetGadgetLayout ObjectUnhide,1,0,1,0
ObjectHide = CreateButton("Hide Object",11,281-57,100,20,win1pan1,2)
SetGadgetLayout ObjectHide,1,0,1,0
ObjectType = CreateComboBox(66,193-57,100,100,win1pan1,0)

;HideGadget win1pan1
; Second panel
win1pan2 = CreatePanel(0,74,240,340,win1)
;

objnumlabel = CreateLabel("Object Number",18,101-57-20,100,18,win1pan2)
objectnumber = CreateTextField(119,101-57-20,100,18,win1pan2)
objnamelabel = CreateLabel("Object Name",18,101-57,100,18,win1pan2)
objectnamed = CreateTextField(119,101-57,100,18,win1pan2)



Button5 = CreateLabel("Group To",18,131-57,100,18,win1pan2)
objectgroups = CreateComboBox(119,131-57,100,18,win1pan2)
Button3 = CreateTextArea(13,250-57,208,50,win1pan2)
Button4 = CreateLabel( "Note",14,225-57,81,18,win1pan2)



;
HideGadget win1pan2



exitloop = False
counter = 0
While exitloop = False
	If typedataint(counter,0) = True And counter < 99 Then
	AddGadgetItem ObjectType,typedata$(counter,0)
	counter = counter + 1
	Else
	exitloop = True
	End If
Wend
;
AddGadgetItem ObjectType,"Macro #1"
AddGadgetItem ObjectType,"Macro #2"
AddGadgetItem ObjectType,"Macro #3"
AddGadgetItem ObjectType,"Macro #4"
;
SelectGadgetItem ObjectType,0
SetGadgetLayout ObjectType,1,0,1,0



End Function