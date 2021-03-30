


Dim map(100,100)


Print "press any key to end"
While KeyDown(1) = False
fillmap(0,0)
Wend

Print "ok"
WaitKey()

End

Function fillmap(x,y)
If RectsOverlap(x,y,1,1,0,0,100,100) Then Return
If map(x,y) = 1 Then Return
If map(x,y) = 0 Then map(x,y) = 1


fillmap(x+1,y)
fillmap(x,y+1)
fillmap(x+1,y+1)
fillmap(x-1,y)
fillmap(x,y-1)
fillmap(x-1,y-1)

End Function