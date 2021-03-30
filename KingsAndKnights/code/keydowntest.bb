;
While KeyDown(1) = False
For i=2 To 255
If KeyHit(i) = True Then 
Print i
End If
Next
Wend
.skipout
WaitKey