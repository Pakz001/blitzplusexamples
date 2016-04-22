Global win = CreateWindow("Create Listbox and Remove item from listbox Example",100,100,800,600,0,1) 
Global list = CreateListBox(10,10,200,300,win)
Global but = CreateButton("remove",250,10,100,20,win)


For i = 0 To 50
AddGadgetItem list,"Item "+i
Next


Repeat 
	we = WaitEvent()
	If we=$401
		If EventSource()=but
			index=SelectedGadgetItem(list)
			If index<>-1
				RemoveGadgetItem list,index
			End If
		End If
	End If
	If we=$803 Then Exit 
Forever 

End
