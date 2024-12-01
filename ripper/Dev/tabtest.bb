Global win1tab
Global win1pan1
Global win1pan2

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


Global win = CreateWindow("test",100,100,320,200,Desktop(),1)


createobjectswindow


While exitloop = False
	WaitEvent()
	If EventID() = $803 Then exitloop = True
	If EventID() = $401 Then 

	
	End If
Wend
End



; ----Function Begins----
Function CreateObjectswindow()
win1 = CreateWindow("Objects Editor",ClientWidth(win)-255,240,236,360,win,1)

win1tab = CreateTabber(0,0,236,20,win1)
AddGadgetItem win1tab,"Main"
AddGadgetItem win1tab,"Extra"

;win1pan1 = CreatePanel(0,15,240,310,win1)

ObjectSlider = CreateSlider(60,44,100,20,win1)
SetGadgetLayout ObjectSlider,1,0,1,0
ObjectName = CreateLabel("Object #0",73,24,100,20,win1)
SetGadgetLayout ObjectName,1,0,1,0


;HideGadget win1pan1
; Second panel
;win1pan2 = CreatePanel(0,20,240,340,win1)
;

;
;HideGadget win1pan2


End Function