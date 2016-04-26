; main code ..............>>

; map width
Global mw = 48
Global mh = 48
; tile width
Global tw = 640/mw
Global th = 480/mh

; closed list (floodfill)
Type ol
	Field x
	Field y
End Type
Type cl
	Field x
	Field y
End Type

; for the undo
Type unre
	Field map[48*48] ; sprite sheet width x height <<<<<<<<<
	Field cols[12*3]
End Type
Global undoredo.unre = New unre

Global win = CreateWindow("Sprite Editor 48x48 Example",100,100,800,600,0,1) 
Global txt = CreateTextArea(0,20,800,520,win) 
Global txt2 = CreateTextArea(0,20,800,520,win)
Global tab = CreateTabber(0,0,800,20,win)
Global can = CreateCanvas(0,20,800,600,win)

HideGadget(txt2)
HideGadget(can)

Dim cols(10,3)
; This array contains the colors that are protected
; true/false
Dim protcol(10)


Dim map(mw,mh)
Global canim = CreateImage(800,600)

Global brushindex=4
Global brushsize=4
Global lcmx ; previous mouse pos
Global lcmy
Global cmx
Global cmy

Global tileim = CreateImage(tw,th,11)
Global tileimbig = CreateImage(32,32,11)

refreshtileimages(True)

InsertGadgetItem tab,0,"MonkeyX Array",0
InsertGadgetItem tab,1,"Visual Editor",1
InsertGadgetItem tab,2,"Color Data",2


Global mytxt$

Global screen$="txt"

Global scatter = False
Global explode = False
Global normal = True
Global smudge = False
Global smudgeint = 5 ; smudge intensity (higher = less)


Global linemode = False
Global linedrawn = False
Global lsx1
Global lsy1
Global lsx2
Global lsy2

makemonkeycode
makecolorcode

Global timer = CreateTimer(60)

updateinterface
addundo
.main
Repeat 
	we = WaitEvent()
	If we=$102
		If screen="canvas"
			If EventData()=24;o outline colorarea				
				outlinefill
				updateinterface
				addundo
			End If
			If EventData()=22;u undo
				undoback
				refreshtileimages
				updateinterface
			End If			
			If EventData()=33;f floodfill
				floodfill()
				updateinterface
				addundo
			End If		
			If EventData() = 50;m s(m)udge
				If smudge=True Then smudge = False Else smudge=True
				If smudge = True
					scatter=False
					explode=False
					normal=False
					Else
					normal = True
				End If
				updateinterface				
			End If
			If EventData()=49;n normal brush
				normal = True
				scatter = False
				explode = False
				updateinterface
			End If
			If EventData()=38;l line mode
				If linemode = True Then linemode = False Else linemode = True
				If linemode = True Then linedrawn=True
				updateinterface
			End If
			If EventData()=18;e
				If explode = False Then explode=True Else explode = False
				If explode = True Then scatter = False
				If explode = True Then normal = False
				If explode = False Then normal = True
				updateinterface
			End If
			If EventData()=25 ;p pick color
				If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
					brushindex = map(cmx/tw,cmy/th)
					updateinterface
				End If
			End If
			If EventData() = 31 ; s scatter
				If scatter = False Then scatter = True Else scatter = False
				If scatter = True Then explode = False
				If scatter = True Then normal = False
				If scatter = False Then normal = True
				updateinterface
			End If
			If EventData()>=2 And EventData()<=10 ; 1 to 9
				brushsize = EventData()-1
				updateinterface
			End If
		End If
	End If
	If we=$201;mosuedown
		If screen = "canvas"
		If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
			lsx1 = cmx
			lsy1 = cmy
			linedrawn=False
		End If
		End If
	End If
	If we=$202;mouseup
		If EventSource() = can
			If EventData() = 1
				If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				If linemode = True
					lsx2 = cmx
					lsy2 = cmy
					makeline
					updateinterface
					linedrawn=True
				End If
				Else
					linedrawn=True
				End If
				; undestructable checkboxes
				If RectsOverlap(cmx,cmy,1,1,712,0,16,11*32)					
					If protcol(cmy/32) = True
						protcol(cmy/32) = False
						Else
						protcol(cmy/32) = True
					End If					
					updateinterface
				End If				
				If RectsOverlap(cmx,cmy,1,1,680,0,32,11*32)
					brushindex=cmy/32
					updateinterface
				End If
				If linemode=False
				If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
					brushdown(cmx,cmy,brushindex)
					updateinterface
				End If
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
						addundo
					End If
				End If
				; undestructable checkboxes
				If RectsOverlap(cmx,cmy,1,1,712,0,16,11*32)					
					For i=0 To 10
					protcol(i)=False
					Next
					If protcol(cmy/32) = True
						protcol(cmy/32) = False
						Else
						protcol(cmy/32) = True
					End If
					updateinterface
				End If					
				If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
					brushdown(cmx,cmy,0)
					updateinterface
				End If
			End If
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				addundo
			End If			
		End If
	End If
	If we=$203;mousemove
		If EventSource()=can
			lcmx = cmx
			lcmy = cmy
			cmx = EventX()
			cmy = EventY()			
			If MouseDown(1) = True
			If linemode=False
			If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)
				brushdown(cmx,cmy,brushindex)
				updateinterface
			End If
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
	If we=$401;gadgetaction
		If EventSource() = tab
			sg = SelectedGadgetItem(tab)
			If sg = 0
				screen="txt"
				makemonkeycode
				HideGadget can
				HideGadget txt2
				ShowGadget txt
			End If
			If sg = 1
				screen="canvas"
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
				screen="txt2"
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
		; the indestructable checkbox
		Rect 712,y*32,16,32,False
		If protcol(y) = True
			Rect 714,y*32,12,30,True
		End If		
	Next
	For y=0 To 47
	For x=0 To 47
		a = map(x,y)
		Color cols(a,0),cols(a,1),cols(a,2)
		Rect x*2+660,y*2+370,2,2
	Next
	Next
	; draw the line
	If linemode = True And linedrawn = False
	Color 255,255,0
	Line lsx1,lsy1,cmx,cmy
	End If
	; draw the brush view
	Color 255,255,255
	Rect cmx-brushsize*tw/2,cmy-brushsize*th/2,brushsize*tw,brushsize*th,False	
	If normal = True
		Text 10,480,"Brush normal on (n)"
		Else
		Text 10,480,"Brush normal off (n)"		
	End If
	If scatter = True Then
		Text 180,480,"Brush Scatter on (s)"
		Else
		Text 180,480,"Brush Scatter off (s)"
	End If
	If smudge = True Then
		Text 180,495,"Brush Smudge On (m)"
		Else
		Text 180,495,"Brush Smudge Off (m)"
	End If
	If explode = True
		Text 380,480,"Brush Explode on (e)"
		Else
		Text 380,480,"Brush Explode off (e)"		
	End If
	If linemode = True
		Text 10,495,"Line Mode On (l)"
		Else
		Text 10,495,"Line Mode Off (l)"	
	End If
	Text 10,510,"Outline at pos (o)"
	Text 380,495,"Pick color (p)"
	Text 580,480,"Flood fill (f)"
	Text 580,495,"Undo (u)"
	Text 10,525,"Press rmb on colors to change them."
	Text 580,525,"Size :"+brushsize
	Text 580,510,"brushsize (1/9)"
	SetBuffer CanvasBuffer(can)
	Cls
	DrawImage canim,0,0
	FlipCanvas can
End Function


Function addundo()
	undoredo.unre = New unre
	cnt=0
	For y=0 To 47
	For x=0 To 47
		undoredo\map[cnt] = map(x,y)
		cnt=cnt+1
	Next
	Next
	cnt3=0
	For i=0 To 11*3
		undoredo\cols[cnt3] = cols(cnt1,cnt2)
		cnt2=cnt2+1
		If cnt2>3 Then cnt2 =0:cnt1=cnt1+1
		cnt3=cnt3+1
	Next
	cnt=0
	For ii.unre = Each unre
	cnt=cnt+1
	Next
	DebugLog "num undo "+cnt
End Function

Function undoback()
	Local c=0
	For ii.unre = Each unre
	c=c+1
	Next
	DebugLog "initialize undo "+c
	If c<3 Then Return
	DebugLog "undoing'
	Local cnt=0
	undoredo.unre = Last unre
	Delete undoredo
	undoredo.unre = Last unre
	For y=0 To 47
	For x=0 To 47
		map(x,y) = undoredo\map[cnt]
		cnt=cnt+1			
	Next
	Next	
	cnt1=0
	cnt2=0
	cnt3=0
	For i=0 To 11*3
		cols(cnt1,cnt2) = undoredo\cols[cnt3]
		cnt2=cnt2+1
		If cnt2>3 Then cnt2 =0:cnt1=cnt1+1
		cnt3=cnt3+1
	Next
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
.paintfuncs
Function outlinefill()
	For this.ol = Each ol
		Delete this
	Next
	For that.cl = Each cl
		Delete that
	Next
	
	Local fillc

	Local st[10]
	st[0] = 0
	st[1] = -1
	st[2] = 1
	st[3] = 0
	st[4] = 0
	st[5] = 1
	st[6] = -1
	st[7] = 0
	If screen="canvas"
	If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)	
		
		Local sx = cmx/tw
		Local sy = cmy/th	

		ffaddlist(sx,sy)
		fillc = map(sx,sy)
		Local xm
		Local my
		While fflistopen() = True
			this.ol = Last ol
			mx = this\x
			my = this\y
			Delete this
			a.cl = New cl
			a\x = mx
			a\y = my
			For i=0 To 6 Step 2
				x = st[i]
				y = st[i+1]
				If mx+x >=0 And mx+x<=mw
				If my+y >=0 And my+y<=mh
				If isonclosedlist(mx+x,my+y)=False
				If map(mx+x,my+y) = fillc				
					z.ol = New ol
					z\x = mx+x
					z\y = my+y
				End If
				End If
				End If
				End If
			Next
		Wend
	End If
	End If
	For e.cl = Each cl
		x1 = e\x
		y1 = e\y
		For i=0 To 6 Step 2
			x2 = st[i]
			y2 = st[i+1]
			If x1+x2 >=0 And x1+x2<=mw
			If y1+y2 >=0 And y1+y2<=mh
				If map(x1+x2,y1+y2)<>fillc
					map(x1+x2,y1+y2)=brushindex
				End If
			End If
			End If
		Next
	Next

End Function


Function isonclosedlist(x,y)
	For this.cl = Each cl
		If this\x = x And this\y = y Then Return True
	Next
	Return False
End Function


Function olistopen()
	Local cnt=0
	For this.ol = Each ol
	cnt=cnt+1
	If cnt>0 Then Return True
	Next
	Return False
End Function


Function floodfill()
	For aa.ol = Each ol
		Delete aa		
	Next
	For bb.cl = Each cl
		Delete	bb
	Next
	Local st[10]
	st[0] = 0
	st[1] = -1
	st[2] = 1
	st[3] = 0
	st[4] = 0
	st[5] = 1
	st[6] = -1
	st[7] = 0
	If screen="canvas"
	If RectsOverlap(cmx,cmy,1,1,0,0,(mw+1)*tw,(mh+1)*th)	
		Local sx = cmx/tw
		Local sy = cmy/th	
		Local fillc
		ffaddlist(sx,sy)
		fillc = map(sx,sy)
		If brushindex = fillc Then Return
		Local xm
		Local my
		While fflistopen() = True
			this.ol = Last ol
			mx = this\x
			my = this\y
			map(mx,my) = brushindex
			ffremlist(mx,my)
			For i=0 To 6 Step 2
				x = st[i]
				y = st[i+1]
				If mx+x >=0 And mx+x<=mw
				If my+y >=0 And my+y<=mh
				If map(mx+x,my+y) = fillc
					ffaddlist(mx+x,my+y)
				End If
				End If
				End If
			Next
		Wend
	End If
	End If
End Function
Function ffremlist(x,y)
	For this.ol = Each ol
		If this\x = x And this\y = y
			Delete this
			Return
		End If
	Next
End Function
Function fflistopen()
	Local cnt=0
	For this.ol = Each ol
		cnt=cnt+1
	Next
	If cnt>0 Then Return True
	Return False
End Function
Function ffaddlist(x,y)	
	this.ol = New ol
	this\x = x
	this\y = y
End Function
Function makeline()	
	Local x1=lsx1/tw
	Local y1=lsy1/th
	Local x2=lsx2/tw
	Local y2=lsy2/th
    Local dx
	Local dy
	Local sx
	Local sy
	Local e
      dx = Abs(x2 - x1)
      sx = -1
      If x1 < x2 Then sx = 1      
      dy = Abs(y2 - y1)
      sy = -1
      If y1 < y2 Then sy = 1
      If dx < dy Then 
          e = dx / 2 
      Else 
          e = dy / 2          
      End If
      Local exitloop=False
      While exitloop = False 
        ; map(x1,y1) = brushindex
		brushdown(x1*tw,y1*th,brushindex)
        If x1 = x2 
            If y1 = y2
                exitloop = True
            End If
        End If
        If dx > dy Then
            x1 = x1 + sx :			e = e - dy 
              If e < 0 Then e = e + dx : y1 = y1 + sy
        Else
            y1 = y1 + sy : e = e - dx 
            If e < 0 Then e = e + dy : x1 = x1 + sx
        EndIf
	Wend
End Function
Function brushdown(cmx,cmy,ind)
	If brushsize=1 Then 
		If protcol(map(cmx/tw,cmy/th)) = False
			map(cmx/tw,cmy/th) = ind
		End If
	End If
	If brushsize>1 Then
		Local cnt
		Local brushbuffer[10*10]
		If smudge=True
			If lcmx<>cmx And lcmy<>cmy	
			; put data under brush (prev pos)
			; in temp array
			cnt=0
			For y=-brushsize/2 To brushsize/2
			For x=-brushsize/2 To brushsize/2
				brushbuffer[cnt]=-1
				If lcmx/tw+x >=0 And lcmx/tw+x <=mw
				If lcmy/th+y >=0 And lcmy/th+y <=mh			
				brushbuffer[cnt] = map(lcmx/tw+x,lcmy/th+y)
				End If
				End If
				cnt=cnt+1
			Next
			Next
			; copy temp array data to
			; current brush position
			; uses intensity(rand)
			cnt=0
			For y=-brushsize/2 To brushsize/2
			For x=-brushsize/2 To brushsize/2
				If cmx/tw+x >=0 And cmx/tw+x <=mw
				If cmy/th+y >=0 And cmy/th+y <=mh			
				If brushbuffer[cnt]>-1
				If Rnd(smudgeint)<2
				If protcol(map(cmx/tw+x,cmy/th+y)) = False
				map(cmx/tw+x,cmy/th+y) = brushbuffer[cnt]
				End If
				End If
				End If
				End If
				End If
				cnt=cnt+1
			Next
			Next
			
			;
			EndIf			
		End If
		If normal=True
			For y=-brushsize/2 To brushsize/2
			For x=-brushsize/2 To brushsize/2 
				If cmx/tw+x >=0 And cmx/tw+x <=mw
				If cmy/th+y >=0 And cmy/th+y <=mh
				If protcol(map(cmx/tw+x,cmy/th+y)) = False
				map(cmx/tw+x,cmy/th+y) = ind
				End If
				End If
				End If
			Next
			Next
		End If
		If scatter=True
			For y=-brushsize/2 To brushsize/2
			For x=-brushsize/2 To brushsize/2 
				If cmx/tw+x >=0 And cmx/tw+x <=mw
				If cmy/th+y >=0 And cmy/th+y <=mh
				If Rnd(brushsize)<2
				If protcol(map(cmx/tw+x,cmy/th+y)) = False
				map(cmx/tw+x,cmy/th+y) = ind
				End If
				End If
				End If
				End If
			Next
			Next
		End If
		If explode = True
			For y=-brushsize/2 To brushsize/2
			For x=-brushsize/2 To brushsize/2 
				If cmx/tw+x >=0 And cmx/tw+x <=mw
				If cmy/th+y >=0 And cmy/th+y <=mh
					If Rnd(brushsize)<2
					b = Rnd(cmx/tw-brushsize/2,cmx/tw+brushsize/2)
					c = Rnd(cmy/th-brushsize/2,cmy/th+brushsize/2)
					If b>=0 And b<=mw And c>=0 And c<=mh
					If protcol(map(cmx/tw+x,cmy/th+y)) = False
					a = map(b,c)
					map(cmx/tw+x,cmy/th+y) = a
					End If
					End If
					End If
				End If
				End If			
			Next
			Next			
		End If
	End If
End Function
