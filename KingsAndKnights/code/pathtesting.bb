; AS_outSize contains the size of the path
; AS_outPath(t) contains the path

; MAP Stuff


Dim isomap(100,100)
Dim pathresult(150,1)
Dim pathmap(100,100)
Dim map(100,100)

;Const map_width = 20
;Const map_height = 20
Global map_width = 100
Global map_height = 100

;Const MP_MAX_NODES = 32*32
;Const MP_MAX_WIDTH = 32
;Const MP_MAX_HEIGHT = 32

;Const MP_MAX_NODES = map_width*map_height
;Const MP_MAX_WIDTH = map_width
;Const MP_MAX_HEIGHT = map_height

Global MP_MAX_NODES = map_width*map_height
Global MP_MAX_WIDTH = map_width
Global MP_MAX_HEIGHT = map_height



;Graphics 800,600,16,1

;Global aisodesert = LoadImage ("isodesert.bmp")
;Global aisogrid = LoadImage ("isogrid.bmp")

;MaskImage aisogrid,255,255,255


;------------------------------------------------
; NOTE: Here are the 2 libraries you must include
;------------------------------------------------
Include "pqueuelib.bb"
Include "astarlib.bb"

;AS_Initialise()				; You must do this. It clears the priority queues,

;-------------------------------------------------------------
; NOTE: This ASTAR algorithm can draw as it searches. These fields must be defined to support this.
; Feel free to remove them, and associated code from the astarlib file
;-------------------------------------------------------------
;Const BLOCK_SIZE = 20				; Size of the squares to draw
Global DIST_CALC_METHOD = 0			; The method used to calculate the heuristic distance
									; 0 - Euclidean distance
									; 1 - Euclidean estimation
									; 2 - Manhattan abs(dx)+abs(dy)
									; 3 - Max dx,dy
;-------------------------------------------------------------

;; Our stuff to show a nice name for the type of distance calculation formula we will use
;Const MAX_DIST_CALC_METHODS = 4								

;-------------------------------------------------------------
; Editor stuff
;-------------------------------------------------------------
Type Block
	Field r,g,b
End Type
Const MAX_BLOCKS = 10
Dim block.Block(MAX_BLOCKS)

Const BLOCK_IMPASS = MAX_BLOCKS - 1
Const BLOCK_START = MAX_BLOCKS - 3
Const BLOCK_END = MAX_BLOCKS - 2
;Dim pathmap(MAP_WIDTH,MAP_HEIGHT)


;-------------------------------------------------------------
; Multiplier to make different blocks different costs
;
; NOTE this value, and costs in general are very closely tied with the heuristic algorithm.
; If your costs for edges is too small, so that the heuristic is much greater than your costs,
; then your costs will have little effect on it and you may find that obstacles and things with high
; costs don't matter and a path gets found straight through them.
Const COST_MULT = 3
Const MP_COST_IMPASS = COST_MULT*BLOCK_IMPASS+1

Global MP_width,MP_height

Dim MP_Map.PQ_Node(MP_MAX_WIDTH,MP_MAX_HEIGHT)			; My map is a grid of PQ_Nodes
Dim MP_xoff(8),MP_yoff(8)								; Just some directions used for finding neighbours


;-------------------------------------------------------------

InitBlocks()


;going = 1
Global curBlock = 1
;Global startX = 0
;Global startY = 0
;Global endX = 0
;Global endY = 0
Global level = 0
Global heuristic = 1			; heuristic multiplier

;findpath(8,5,15,14,0)
;drawmapasiso

;WaitKey
;End
Graphics 640,480,8,2
SetBuffer BackBuffer()

map_width=100
		map_height=100
		MP_MAX_NODES = map_width*map_height
		MP_MAX_WIDTH = map_width
		MP_MAX_HEIGHT = map_height
		;Dim pathmap(map_width,map_height)
		Dim MP_Map.PQ_Node(MP_MAX_WIDTH,MP_MAX_HEIGHT)

For x=0 To 100
For y=0 To 100
map(x,y)=3
Next
Next		

While KeyDown(1) = False
Cls
drawmap()
For i=0 To as_outsize
x = pathresult(i,0)
y = pathresult(i,1)
Color 255,255,0
Rect x*6,y*4,6,4,1
Next

Color 255,255,255 : Rect MouseX(),MouseY(),6,6,1
If KeyHit(57) = True Then
findpath(10,10,40,40,0)
For i=0 To as_outsize
map(pathresult(i,0),pathresult(i,1)) = 5
Next
End If
cursorx = MouseX()/6
cursory = MouseY()/4
If cursorx>100 Then cursorx=100
If cursory>100 Then cursory=100
If MouseDown(1) = True Then 
For y1=cursory-1 To cursory+1
For x1=cursorx-1 To cursorx+1
If x1>0 And y1>0 And x1<100 And y1<100 Then
map(x1,y1) = 10
End If
Next
Next
End If
Flip
Wend


	
Function findpath(sx,sy,ex,ey,method)
AS_Initialise()	

DIST_CALC_METHOD = method

startx = sx
starty = sy
endx = ex
endy = ey
If startX <> -1 And endX <> -1 Then 
	map(startX,startY) = 0
	map(endX,endY) = 0
	InitialiseMap()	
	If Not AS_FindPath(MP_map(startX,startY),MP_map(endX,endY)) =  0 Then Return False	
	map(startX,startY) = BLOCK_START
	map(endX,endY) = BLOCK_END
EndIf

If AS_outSize > 0 
	For t = 0 To AS_outSize	- 1
		n.PQ_Node = AS_outPath(t)
		x = n\x
		y = n\y
		pathresult(counter,0) = n\x
		pathresult(counter,1) = n\y
		counter=counter+1
		Rect x * 6, y * 4, 6,4 - 1						
	Next
EndIf

End Function

	

;----------------------------------------------------
; Compute the cost of a block
;----------------------------------------------------
Function BlockCost(b) 
	Return COST_MULT*b+1
End Function


Function InitBlocks()
Restore colours
	For t = 0 To MAX_BLOCKS - 1
		block(t) = New Block
		Read block(t)\r
		Read block(t)\g
		Read block(t)\b
	Next

Restore offsData
For t = 0 To 7: Read MP_xoff(t): Next
For t = 0 To 7: Read MP_yoff(t): Next
End Function



Function InitialiseMap()

; NOTE: Reset of nodes. If you weren't remaking the map each time like I am doing, you don't have to
; delete the PQ_Nodes or AS_Neighbours, but you would have to initialise the node's inClosed and inOpen
; fields so that the node's start not in any list.
Delete Each PQ_Node						; Remember to do this if you're reinitialising your PQ_Node map
Delete Each AS_Neighbour				; Same with this!

MP_width = MAP_WIDTH
MP_height = MAP_height
	
;NOTE: Here is where I make the nodes for my graph (map). I also calculate the cost of the nodes here
; also	
For y = 0 To MP_height - 1
	For x = 0 To MP_width - 1
		unit = pathmap(x,y)
		MP_map(x,y) = New PQ_Node
		MP_map(x,y)\cost = BlockCost(unit)
		If pathmap(x,y)<> BLOCK_IMPASS Then MP_map(x,y)\cost = 0
		MP_map(x,y)\x = x
		MP_map(x,y)\y = y
	Next
Next
	
For y = 0 To MP_height - 1
	For x = 0 To MP_width - 1
		sb = pathmap(x,y)
		For t = 0 To 7
			nx = x + MP_xoff(t)				; Positions of neighbours using offsets
			ny = y + MP_yoff(t)
			If nx >= 0 And ny >=0 And nx < MP_width And ny < MP_height Then
				
; NOTE: Here is how you make neighbours for your nodes						
					If MP_map(nx,ny)\cost < MP_COST_IMPASS Then			; Only ones with no walls
;					If map(nx,ny) <> BLOCK_IMPASS Then			; Only ones with no walls
						nb = pathmap(nx,ny)
						m.PQ_Node = MP_map(x,y)
						neigh.PQ_Node = MP_map(nx,ny)		; This neighbour
						
						newNeigh.AS_Neighbour = New AS_Neighbour
						newNeigh\nextNeigh = m\firstNeigh	; Insert at front of list
						newNeigh\neigh = neigh				; Point to neighbour
						
; NOTE: Edge cost calculation. AStar needs this
						newNeigh\edgeCost = (BlockCost(sb)+BlockCost(nb))/2
						
						
						m\firstNeigh = newNeigh				; New one is at front
					EndIf
				EndIf
			Next			
		Next
	Next
	
End Function

.offsData
Data 0,1,1,1,0,-1,-1,-1
Data -1,-1,0,1,1,1,0,-1


.colours
Data 0,0,0
Data 255*.1,255*.1,255*.1
Data 255*.2,255*.2,255*.2
Data 255*.3,255*.3,255*.3
Data 255*.4,255*.4,255*.4
Data 255*.5,255*.5,255*.5
Data 255*.6,255*.6,255*.6
Data 0,255,0
Data 255,0,0
Data 255,255,255


Function drawmap()
For y=0 To 100
For x=0 To 100
Color map(x,y)*25,0,0
Rect x*6,y*4,10,10,1
Next
Next
Color 255,255,255
End Function

