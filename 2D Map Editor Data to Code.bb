Global win = CreateWindow("Tabber and Textarea and Canvas Example",100,100,800,600,0,1) 
Global txt = CreateTextArea(0,20,800,600,win) 
Global tab = CreateTabber(0,0,800,20,win)
Global can = CreateCanvas(0,20,800,600,win)

Global mw = 20
Global mh = 15
Global tw = 32
Global th = 32
Dim map(mw,mh)

Global brushindex=0
Global cmx
Global cmy

InsertGadgetItem tab,0,"TextArea",0
InsertGadgetItem tab,1,"Canvas",1


SetTextAreaText txt,"Press the tabber to show text or canvas."

Global timer = CreateTimer(60)

Repeat 
	we = WaitEvent()
	
	If we=$202
		If EventData() = 1
			If RectsOverlap(cmx,cmy,1,1,680,0,32,11*th)
			brushindex=cmy/th
			End If
		End If
	End If
	If we=$203
		If EventSource()=can
			cmx = EventX()
			cmy = EventY()
		End If
	End If
	If we=$401
		If EventSource() = tab
			sg = SelectedGadgetItem(tab)
			If sg = 0
				HideGadget can
				ShowGadget txt
			End If
			If sg = 1
				HideGadget txt
				ShowGadget can
			End If
		End If
	End If
	If we=$4001
		SetBuffer CanvasBuffer(can)
		Cls
		drawinterface
		FlipCanvas can
	End If	
	If we=$803 Then Exit 
Forever 
End

Function drawinterface()
	SetBuffer CanvasBuffer(can)
	Color 255,255,255
	For y=0 To mh
	For x=0 To mw
		Rect x*tw,y*th,33,33,False
	Next
	Next
	For y=0 To 10 
		Rect 680,y*th,33,33,False
		Text 680+16,y*th+16,y,True,True
		If brushindex = y
		Rect 681,y*th,31,31,False
		End If
	Next
End Function
