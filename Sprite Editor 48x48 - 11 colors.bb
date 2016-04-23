; press 1 to 9 on canvas to change brush size
; press right mouse button on color to change it

Global win = CreateWindow("Sprite Edit to Monkey array 48x48 Example",100,100,800,600,0,1) 
Global txt = CreateTextArea(0,20,800,600,win) 
Global txt2 = CreateTextArea(0,20,800,600,win)
Global tab = CreateTabber(0,0,800,20,win)
Global can = CreateCanvas(0,20,800,600,win)

HideGadget(txt2)

Dim cols(10,3)

Global mw = 48
Global mh = 48
Global tw = 640/mw
Global th = 480/mh

Dim map(mw,mh)
Global canim = CreateImage(800,600)

Global brushindex=0
Global brushsize=1
Global cmx
Global cmy

Global tileim = CreateImage(tw,th,11)
Global tileimbig = CreateImage(32,32,11)

refreshtileimages(True)

InsertGadgetItem tab,0,"MonkeyX Array",0
InsertGadgetItem tab,1,"Visual Editor",1
InsertGadgetItem tab,2,"Color Data",2


Global mytxt$

Global scatter = True

makemonkeycode
makecolorcode

Global timer = CreateTimer(60)

updateinterface
.main
Repeat 
	we = WaitEvent()
	If we=$102
		If EventData() = 31
			If scatter = False Then scatter = True Else scatter = False
			updateinterface
		End If
		If EventData()>=2 And EventData()<=10
			brushsize = EventData()-1
			updateinterface
			DebugLog brushsize
		End If
	End If
	If we=$202
		If EventSource() = can
			If EventData() = 1
				If RectsOverlap(cmx,cmy,1,1,680,0,32,11*32)
					brushindex=cmy/32
					updateinterface
				End If
				If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
					brushdown(cmx,cmy,brushindex)
					updateinterface
				End If

			End If
			If EventData() = 2
				If RectsOverlap(cmx,cmy,1,1,680,0,32,11*32)
					If RequestColor()=True
						brushindex=cmy/32
						cols(brushindex,0) = RequestedRed()
						cols(brushindex,1) = RequestedGreen()
						cols(brushindex,2) = RequestedBlue()
						refreshtileimages
						updateinterface
					End If
				End If
				If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
					brushdown(cmx,cmy,0)
					updateinterface
				End If
			End If
		End If
	End If
	If we=$203
		If EventSource()=can
			cmx = EventX()
			cmy = EventY()			
			If MouseDown(1) = True
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				brushdown(cmx,cmy,brushindex)
				updateinterface
			End If
			End If
			If MouseDown(2) = True
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				;map(cmx/tw,cmy/th) = 0
				brushdown(cmx,cmy,0)
				updateinterface
			End If
			End If
			updateinterface			
		End If
	End If
	If we=$401
		If EventSource() = tab
			sg = SelectedGadgetItem(tab)
			If sg = 0
				makemonkeycode
				HideGadget can
				HideGadget txt2
				ShowGadget txt
			End If
			If sg = 1
				readmonkeycode
				readcolorcode
				HideGadget txt
				HideGadget txt2
				ShowGadget can
				refreshtileimages
				updateinterface
				FlipCanvas can
			End If
			If sg = 2
				makecolorcode
				makemonkeycode
				HideGadget txt
				HideGadget can
				ShowGadget txt2
			End If
		End If
	End If
	If we=$4001
	End If	
	If we=$803 Then Exit 
Forever 
End

.funcs

Function updateinterface()
	SetBuffer ImageBuffer(canim)
	Cls
	Color 255,255,255
	For y=0 To mh-1
	For x=0 To mw-1
		DrawImage tileim,x*tw,y*th,map(x,y)
	Next
	Next
	For y=0 To 10 
		DrawImage tileimbig,680,y*32,y
		If brushindex = y
			Rect 681,y*32,31,31,False
		End If
	Next
	Color 255,255,255
	Rect cmx-brushsize*tw/2,cmy-brushsize*th/2,brushsize*tw,brushsize*th,False	
	If scatter = True Then
		Text 10,500,"Brush Scatter on (s)"
		Else
		Text 10,500,"Brush Scatter off (s)"
	End If
	Text 10,520,"Press right mouse button on colors to change them."
	SetBuffer CanvasBuffer(can)
	Cls
	DrawImage canim,0,0
	FlipCanvas can
End Function

Function makemonkeycode()
	mytxt$="Global sprite:Int[][] = ["+Chr(13)+Chr(10)
	For y=0 To mh-1
	mytxt$=mytxt$+"["
	For x=0 To mw-1
		mytxt$=mytxt$+map(x,y)
		mytxt$=mytxt$+","
	Next
		mytxt$=Left(mytxt$,Len(mytxt$)-1)
		mytxt$=mytxt$+"]"
		mytxt$=mytxt$+","
		mytxt$=mytxt$+Chr(13)+Chr(10)
	Next
	mytxt$=Left(mytxt$,Len(mytxt$)-3)
	mytxt$=mytxt$+"]"	
	SetTextAreaText txt,mytxt$
End Function

Function readmonkeycode()
	mytxt$ = TextAreaText(txt)
	Local cnt=0
	Local stp=1
	Local exitloop=False
	While exitloop=False
		stp=Instr(mytxt$,",",stp)
		If stp=0 Then exitloop=True
		stp=stp+1
		cnt=cnt+1
	Wend
	If cnt <> ((mw)*(mh)) Then Notify "Not valid map data"
	Local mytxt2$
	Local a$=""
	Local b$=""
	Local c$=""
	For i = 1 To Len(mytxt$)
		a$=Mid(mytxt$,i,1)
		If a$="," Then b$=b$+a$		
		If Asc(a$) >= 48 And Asc(a$)<= 57 Then b$=b$+a$
	Next
	For i=1 To Len(b$)
		a$=Mid(b$,i,1)
		If Asc(a$)>=48 And Asc(a$)<=57 
			c$=c$+a$
		End If
		If a$="," Then
			map(x,y) = Int(c)
			c$=""
			x=x+1
			If x>=mw Then x=0:y=y+1
		End If
	Next
End Function


Function readcolorcode()
	Local lst[1000]
	a$ = TextAreaText(txt2)
	a$ = Replace(a$,Chr(13),",")
	a$ = Replace(a$,Chr(10),"")
	For i=1 To Len(a$)
		b$=Mid(a$,i,1)
		If b$=Chr(13)
			lst[cnt] = c$
			cnt=cnt+1
			c$=""
		End If
		If b$=","
			lst[cnt] = c$
			cnt=cnt+1
			c$=""
		End If
		If Asc(b$) >= 48 And Asc(b$) <= 57
			c$=c$+b$
		End If
	Next
	cnt2=0
	For i=0 To 10
		cols(i,0) = lst[cnt2]
		cnt2=cnt2+1
		cols(i,1) = lst[cnt2]
		cnt2=cnt2+1
		cols(i,2) = lst[cnt2]
		cnt2=cnt2+1		
	Next
End Function

Function makecolorcode()
	a$ = ""
	For i=0 To 10
		a$=a$ + cols(i,0) + ","
		a$=a$ + cols(i,1) + ","
		a$=a$ + cols(i,2)
		a$=a$ + Chr(13)+Chr(10)
	Next
	SetTextAreaText(txt2,a$)
End Function

Function refreshtileimages(init=False)
	For i = 0 To 10
		SetBuffer ImageBuffer(tileim,i)
		If init=True
			Color 200+i*3,10+i*20,10
			cols(i,0) = ColorRed()
			cols(i,1) = ColorGreen()
			cols(i,2) = ColorBlue()			
		End If
		Color cols(i,0),cols(i,1),cols(i,2)
		Rect 0,0,tw,th,True
		SetBuffer ImageBuffer(tileimbig,i)
		Color cols(i,0),cols(i,1),cols(i,2)
		Rect 0,0,32,32
		For x=-1 To 1
		For y=-1 To 1
			Color 4,4,4
			Text tw/2+x,th/2+y,i,1,1
		Next
		Next
		Color 255,255,255
		Text tw/2,th/2,i,1,1
	Next
End Function

.brushfuncs
Function brushdown(cmx,cmy,ind)
	If brushsize=1 Then map(cmx/tw,cmy/th) = ind
	If brushsize>1 Then
		For y=-brushsize/2 To brushsize/2
		For x=-brushsize/2 To brushsize/2 
			If cmx/tw+x >=0 And cmx/tw+x <=mw
			If cmy/th+y >=0 And cmy/th+y <=mh
			If scatter = False
			map(cmx/tw+x,cmy/th+y) = ind
			Else
			If Rnd(brushsize)<2
			map(cmx/tw+x,cmy/th+y) = ind
			End If
			End If
			End If
			End If
		Next
		Next
	End If
End Function				
