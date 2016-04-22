Global win = CreateWindow("Tabber and Textarea and Canvas Example",100,100,800,600,0,1) 
Global txt = CreateTextArea(0,20,800,600,win) 
Global tab = CreateTabber(0,0,800,20,win)
Global can = CreateCanvas(0,20,800,600,win)

InsertGadgetItem tab,0,"TextArea",0
InsertGadgetItem tab,1,"Canvas",1


SetTextAreaText txt,"Press the tabber to show text or canvas."

Repeat 
	we = WaitEvent()
	
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
	If we=$803 Then Exit 
Forever 

End
