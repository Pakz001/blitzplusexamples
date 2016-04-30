; This tool is a map editor that turns the data into a 
; piece of copyable code. Its in the form of an
; multidimensional array.
;
;
;
SeedRnd MilliSecs()
.setup
Global win = CreateWindow("Make Monkey array (tilemap) Example",100,100,800,600,0,1) 
Global txt = CreateTextArea(0,20,800,600,win) 
Global txt2 = CreateTextArea(0,20,800,600,win)
Global tab = CreateTabber(0,0,800,20,win)
Global can = CreateCanvas(0,20,800,600,win)
HideGadget txt2
Global mw = 30
Global mh = 20
Global tw = 22
Global th = 22
Dim map(mw,mh)
Global canim = CreateImage(800,600)
SetBuffer ImageBuffer(canim)
font=LoadFont ("verdana.ttf",12)
SetFont font

Dim cols(16,3)
For i=0 To 15
	cols(i,0) = Rand(0,255)
	cols(i,1) = Rand(0,255)
	cols(i,2) = Rand(0,255)
Next

Global screen$="txt"
Global brushindex=1
Global cmx
Global cmy

Global tileim = CreateImage(32,32,16)
Global selectindexim = CreateImage(32,16*32)

updategraphics



InsertGadgetItem tab,0,"Monkey code",0
InsertGadgetItem tab,1,"Blitz basic code",1
InsertGadgetItem tab,2,"Canvas",2


Global mytxt$

makemonkeycode

Global timer = CreateTimer(60)

updateinterface

.mainloop
Repeat 
	we = WaitEvent()
	If we=$102 ; keyup
		If screen="canvas"
			If EventData()=46;c - clear
				For y=0 To mh
				For x=0 To mw
				map(x,y)=0
				Next
				Next
				updateinterface
			End If
			If EventData()=200;keu up
				If brushindex>0 Then brushindex = brushindex-1
				updateinterface
			End If
			If EventData()=208;key down
				If brushindex<15 Then brushindex = brushindex+1
				updateinterface
			End If
		End If
	End If
	If we=$201;MouseDown
		If EventSource() = can
			If EventData() = 1
			End If
		End If
	End If
	If we=$202;mouseup
		If EventSource() = can
			If EventData() = 1
				If RectsOverlap(cmx,cmy,1,1,680,0,32,16*32)
					brushindex=cmy/32
					updateinterface
				End If
			End If
			If EventData() = 2
				If RectsOverlap(cmx,cmy,1,1,680,0,32,16*32)
					brushindex=cmy/32
					If RequestColor( cols(brushindex,0) , cols(brushindex,1) , cols(brushindex,2)) Then
						cols(brushindex,0) = RequestedRed()
						cols(brushindex,1) = RequestedGreen()
						cols(brushindex,2) = RequestedBlue()					
						updategraphics		
					End If			
					updateinterface
				End If			
			End If
		End If
	End If
	If we=$203;mousemove
		If EventSource()=can
			cmx = EventX()
			cmy = EventY()
			If MouseDown(1) = True
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				map(cmx/tw,cmy/th) = brushindex
				updateinterface
			End If
			End If
			If MouseDown(2) = True
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				map(cmx/tw,cmy/th) = 0
				updateinterface
			End If
			End If
		End If
	End If
	If we=$401;gadgetaction
		If EventSource() = tab
			sg = SelectedGadgetItem(tab)
			If sg = 0
				makemonkeycode				
				makeblitzcode
				HideGadget txt2				
				HideGadget can
				ShowGadget txt
				screen="txt"
			End If
			If sg = 1
				makeblitzcode
				makemonkeycode
				HideGadget txt
				HideGadget can
				ShowGadget txt2
				screen="txt2"
			End If
			If sg = 2
				readmonkeycode
				HideGadget txt
				HideGadget txt2
				ShowGadget can
				updateinterface
				FlipCanvas can
				screen="canvas"
			End If
		End If
	End If
	If we=$4001
	End If	
	If we=$803 Then Exit 
Forever 
End

.screendraw
Function updateinterface()
	SetBuffer ImageBuffer(canim)
	Cls
	Color 255,255,255
	For y=0 To mh-1
	For x=0 To mw-1
		DrawImage tileim,x*tw,y*th,map(x,y)
	Next
	Next
	DrawImage selectindexim,680,0
	Color 255,255,0
	Rect 681,brushindex*32,31,31,False	
	Rect 682,brushindex*32,29,29,False	
	Text 5,480,"Mapw:"+mw+" maph:"+mh
	Text 5,490,"Left mouse = put"
	Text 5,500,"Right mouse = put 0"
	Text 5,510,"c to clear"
	Text 5,520,"curs up/down brush index"
	SetBuffer CanvasBuffer(can)
	Cls
	DrawImage canim,0,0
	FlipCanvas can
End Function
Function updategraphics()
	; palette index choice
	SetBuffer ImageBuffer(selectindexim)
	font = LoadFont("verdana.ttf",21,1)
	SetFont font
	For i = 0 To 16-1
		For y=0 To 32
			Color cols(i,0)/32*y,cols(i,1)/32*y,cols(i,2)/32*y
			Line 0,y+i*32,32,y+i*32
		Next	
		Color cols(i,0),cols(i,1),cols(i,2)
	;	Rect 0,i*32,32,32,True
		Color 0,0,0
		Rect 0,i*32,14,14
		Color 255,255,255
		Text 4,i*32+4,i	
	Next
	; tiles on the map
	SetBuffer ImageBuffer(tileim)
	For i = 0 To 15
		SetBuffer ImageBuffer(tileim,i)
		font=LoadFont ("verdana.ttf",16,1)
		SetFont font
		For y=0 To th
			Color cols(i,0)/th*y,cols(i,1)/th*y,cols(i,2)/th*y
			Line 0,y,tw,y
		Next
		Color cols(i,0),cols(i,1),cols(i,2)
		Rect 0,0,tw,th,False
		Color 0,0,0
		Rect 0,0,14,14
		Color 255,255,255
		Text 4,4,i
	Next
End Function
.codeinputoutput
Function readblitzcode()
	mytxt$ = TextAreaText(txt2)
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

Function makeblitzcode()
	mytxt$=".label"+Chr(13)+Chr(10)
	For y=0 To mh-1
	mytxt=mytxt+"Data "
	a$=""
	For x=0 To mw-1
	a$=a$+map(x,y)+","
	Next
	mytxt=mytxt+Left(a$,Len(a$)-1)
	mytxt=mytxt+Chr(13)+Chr(10)
	Next
	SetTextAreaText txt2,mytxt
End Function
Function makemonkeycode()
	mytxt$="Global map:Int[][] = ["+Chr(13)+Chr(10)
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
