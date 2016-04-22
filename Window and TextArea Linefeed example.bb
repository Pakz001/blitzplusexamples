Global win = CreateWindow("TextArea Linefeed Example",0,100,800,600,0,1) 
Global txtbox = CreateTextArea(0,0,800,600,win) 


thetext$="Hello"
thetext$=thetext$+Chr(10)+Chr(13)
thetext$=thetext$+"this is on another line."

SetTextAreaText txtbox,thetext$

Repeat 
If WaitEvent()=$803 Then Exit 
Forever 

End
