;
; HLife style windows ;  minimize drag Close
; Labels
;

Type window
	Field x,y,w,h,myox,myoy,mytbar,mylock
	Field win,canvas,wintitle$,mytitle$,myexit
	Field closebut1,closebut2
	Field closebut1x,closebut1y
	Field minbut1,minbut2
	Field minbut1x,minbut1y
	Field titlelabel
	Field titlelabelx,titlelabely
	Field labelactive[100]
	Field labelnum[100]
	Field label[100]
	Field labelpressed[100]
	Field labelx[100]
	Field labely[100]
	Field labelwidth[100]
	Field labelheight[100]
	Field labeltxt$[100]
	Field labelhover[100]
	Field labeltextcolor_Red
	Field labeltextcolor_Green
	Field labeltextcolor_Blue
	Field labeltextcolorpressed_Red
	Field labeltextcolorpressed_Green
	Field labeltextcolorpressed_Blue

End Type

Global win.window = New window

makemywin win,100,100,320,200,"Mywindow"

makelabel win,50,50,"qwe",1
refreshlabels win

While myExit = False
	we = WaitEvent()
	mywin we,win
Wend
End

Function makemywin(this.window,x,y,w,h,mytitle$)

	this\x 					= x
	this\y 					= y
	this\w 					= w
	this\h 					= h
	this\labeltextcolor_red 		= 240
	this\labeltextcolor_green 	= 240
	this\labeltextcolor_blue 	= 240
	this\labeltextcolorpressed_red 		= 255
	this\labeltextcolorpressed_green 	= 200
	this\labeltextcolorpressed_blue 		= 200

	this\mytitle$ 				= 	mytitle$
	this\win					= 	CreateWindow(this\mytitle$	,this\x,this\y,this\w,this\h,Desktop(),0)
	this\mytbar				=	CreateCanvas(		0,0,GadgetWidth(this\win),16,this\win)	
	this\canvas				= 	CreateCanvas(		0,0,GadgetWidth(this\win),GadgetHeight(this\win),this\win)
	this\closebut1				=	CreateImage(16,32)
	this\closebut2				=	CreateImage(16,32)
	this\minbut1				=	CreateImage(16,32)
	this\minbut2				=	CreateImage(16,32)
	this\titlelabel				=	CreateImage(100,32)
	
	SetBuffer ImageBuffer(this\titlelabel)
	ClsColor 70,100,70
	Cls
	Color 255,255,255
	Text 2,2,mytitle$
	
	SetBuffer CanvasBuffer(this\canvas)
	ClsColor 70,70,70
	Cls
	FlipCanvas this\canvas
	SetBuffer CanvasBuffer(this\mytbar)
	ClsColor 70,100,70
	Cls
	Color 255,255,255
	Text 10,2,this\wintitle$
	DrawImage this\titlelabel,0,0
	this\titlelabelx = 0
	this\titlelabely = 0
	FlipCanvas this\mytbar	
	
	SetBuffer ImageBuffer(this\closebut1)
	ClsColor 70,100,70
	Cls
	Color 255,255,255
	Text 3,3,"X"
	SetBuffer CanvasBuffer(this\mytbar)
	DrawImage this\closebut1,GadgetWidth(this\win)-16,0
	this\closebut1x = GadgetWidth(this\win)-16
	this\closebut1y = 0
	FlipCanvas this\mytbar
	
	SetBuffer ImageBuffer(this\closebut2)
	ClsColor 70,100,70
	Cls
	Color 255,220,220
	Text 3,3,"X"
		
	SetBuffer ImageBuffer(this\minbut1)
	ClsColor 70,100,70
	Cls
	Color 255,220,220
	Text 3,3,"_"	
	SetBuffer CanvasBuffer(this\mytbar)
	DrawImage this\minbut1,GadgetWidth(this\win)-32,0
	this\minbut1x = GadgetWidth(this\win)-32
	this\minbut1y = 0
	FlipCanvas this\mytbar
		
	SetBuffer ImageBuffer(this\minbut2)
	ClsColor 70,100,70
	Cls
	Color 255,220,220
	Text 3,3,"_"
	
	SetBuffer CanvasBuffer(this\canvas)
End Function

Function makelabel(this.window,x,y,txt$,label)
	this\label[label] 		= 	CreateImage(StringWidth(txt$)+6,StringHeight(txt$)+6 )
	this\labelpressed[label] =  CreateImage(StringWidth(txt$)+6,StringHeight(txt$)+6 )
	this\labelx[label] 		= 	x
	this\labely[label] 		= 	y
	this\labelwidth[label] 	= 	StringWidth(txt$)
	this\labelheight[label] 	= 	StringHeight(txt$)
	this\labeltxt$[label] 	= 	txt$
	this\labelnum[label] 	= 	label
	this\labelactive[label] 	= 	True
	SetBuffer ImageBuffer(this\label[label])
	Color this\labeltextcolor_red,this\labeltextcolor_green,this\labeltextcolor_blue	
	Text 3,3,txt$
	SetBuffer ImageBuffer(this\labelpressed[label])
	Color this\labeltextcolorpressed_red,this\labeltextcolorpressed_green,this\labeltextcolorpressed_blue	
	Text 3,3,txt$	
	SetBuffer CanvasBuffer(this\canvas)
End Function

Function updatelabels(this.window)

	For i = 0 To 100		
		If this\labelactive[i] = True And this\labelhover[i] = True Then
			If RectsOverlap(this\labelx[i],this\labely[i],ImageWidth(this\label[i]),ImageHeight(this\label[i]),this\myox,this\myoy,1,1) = False Then
				this\labelhover[i] = False
				n=True
				refreshlabels this
			End If
		End If
	Next

	For i = 0 To 100		
		If this\labelactive[i] = True Then
			If RectsOverlap(this\labelx[i],this\labely[i],ImageWidth(this\label[i]),ImageHeight(this\label[i]),this\myox,this\myoy,1,1) = True Then
				If this\labelhover[i] = False
					n=True
					this\labelhover[i] = True
				End If
			End If
		End If
	Next
	
	If n=True Then refreshlabels this
	
End Function

Function refreshlabels(this.window)
	SetBuffer CanvasBuffer(this\canvas)
	Color 200,0,0
;	Rect 100,100,23,32,False
	For i = 0 To 100
		If this\labelactive[i] = True Then
			If this\labelhover[i] = False Then
				DrawImage this\label[i],this\labelx[i],this\labely[i]
				Else
				DrawImage this\labelpressed[i],this\labelx[i],this\labely[i]
			End If
		End If
	Next
	FlipCanvas this\canvas
End Function

Function mousebuttoncollide(this.window)
	If ImageRectCollide(this\closebut1,this\closebut1x,this\closebut1y,0,this\myox,this\myoy,1,1) = True
		End
	End If
	If ImageRectCollide(this\minbut1,this\minbut1x,this\minbut1y,0,this\myox,this\myoy,1,1) = True
		MinimizeWindow(this\win)
	End If
End Function

Function mywin(action,this.window)
	timer = CreateTimer(10)
	
	Local amx,amy
	
	Select action
		Case $401 ; button
		Case $201 ; mousedown
			If EventSource() = this\mytbar
				this\mylock = True
				this\myox = EventX()
				this\myoy = EventY()
				mousebuttoncollide(this)
			End If
		Case $202 ; mouseup
			If EventSource() = this\mytbar
				this\mylock = False
			End If
		Case $203 ; mousemove
				If EventSource() = this\canvas
				If  EventX()<> amx And EventY() <> amy					
					this\myox = EventX()
					this\myoy = EventY()
				End If
				End If
			If EventSource() = this\mytbar
				If this\mylock = True Then
					SetGadgetShape this\win,MouseX()-this\myox,MouseY()-this\myoy,ClientWidth(this\win),ClientHeight(this\win)
				End If
			End If
		Case $206;mouseleave
			If EventSource() = this\mytbar
				this\mylock=False
			End If
		Case $4001
			updatelabels(this)
	End Select
End Function


