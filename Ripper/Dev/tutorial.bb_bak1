;
; Tutorial window
;

Dim tutmessages$(25)
Dim tutmessagesname$(25)
Global tutmessage = 0

Global tutwin
Global tutactive
Global tutok
Global tutbegin
Global tutprev
Global tutnext
Global tutend
Global tuttext


createtutmessages()

;
; This handles the tutorial
;
Function dotutorial(theevent,source)
Select theevent
	Case $401
	; If pressed the ok button
	If Source = tutok Then
		FreeGadget tutwin
		Return
	End If
	; If pressed the begin button
	If Source = tutbegin Then
		tutmessage = 0
		showtutorial("first message")
		Return
	End If
	; If pressed the end button
	If Source = tutend Then
		Notify "Insert last message first"
		Return
	End If
	; If pressed the next message
	If Source = tutnext Then
		If tutmessage < 25 Then 
			tutmessage = tutmessage + 1
			showtutorial(tutmessagesname$(tutmessage))
			Return
		End If
	End If
	; If pressed the previous message
	If Source = tutprev Then
		If tutmessage > 0 Then 
			tutmessage = tutmessage - 1
			showtutorial(tutmessagesname$(tutmessage))
			Return
		End If
	End If

End Select
End Function


Function showtutorial(Stringding$)

FreeGadget tutwin ; First free the tut window

;Create the turorial window
tutwin = CreateWindow("Tutorial Window",51,51,401,300+20,win,1)
Tutactive = CreateButton("Stop Bugging me", 13,247,130,18,tutwin,2 )
tutok = CreateButton("Ok",254,247,130,18,tutwin)
tutbegin = CreateButton("<<",84,221,50,18,tutwin)
Tutprev = CreateButton("<",142,221,50,18,tutwin)
tutnext = CreateButton(">",201,221,50,18,tutwin)
tutend = CreateButton(">>",260,221,50,18,tutwin)
tuttext = CreateTextArea( 18,14,365,200,tutwin )

; Select the message
For i=0 To 25
	If Lower(stringding$) = Lower(tutmessagesname$(i)) Then
		AddTextAreaText tuttext,tutmessages$(i)
	End If
Next

End Function

Function createtutmessages()
num = 0
;tutmessagesname$(num) = "first message"
;				  tutmessages$(num) = "Welcome to Ripper"+Chr(13)+Chr(10)+Chr(13)+Chr(10)
;tutmessages$(num) = tutmessages$(num) + "This program is a custom tool to design interfaces with."+Chr(13)+Chr(10)+Chr(13)+Chr(10)
;tutmessages$(num) = tutmessages$(num) + "There are two edit modes. One selection mode"+Chr(13)+Chr(10)
;tutmessages$(num) = tutmessages$(num) + "where you can select objects and one Creation mode where"+Chr(13)+Chr(10)
;tutmessages$(num) = tutmessages$(num) + "you can create objects."+Chr(13)+Chr(10)+Chr(13)+Chr(10);
;tutmessages$(num) = tutmessages$(num) + "Feel free to try it out."


counter = -1
file = ReadFile(CurrentDir$()+"help\tutorial.txt")
	If file = 0 Then Notify "Tutorial file not found" : End
	While Eof(file) = False
		
		a$ = ReadLine(file)
		
		If Left(a$,1) = "@" Then
			counter = counter + 1
			tutmessagesname$(counter) = Replace(a$,"@","")
			a$ = ""
		End If
		
		tutmessages$(counter) = tutmessages$(counter) + a$+Chr(13)+Chr(10)
		
	Wend
CloseFile file


End Function


















