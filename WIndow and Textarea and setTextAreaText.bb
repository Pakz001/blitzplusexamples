Global win = CreateWindow("SetTextAreaText Example",0,100,800,600,0,1) 
Global txtbox = CreateTextArea(0,0,800,600,win) 

SetTextAreaText txtbox,"Here you set the entire text of the text area."

Repeat 
If WaitEvent()=$803 Then Exit 
Forever 

End ; bye! 
