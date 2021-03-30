;-----------------------------------------------------------------------------
; ASTAR LIBRARY
; by Aaron Koolen
;
; Feel free to use this code for whatever purposes you want
;
;-----------------------------------------------------------------------------


Global AS_rootNode.PQ_Node				; Keeps track of the start node in the search
Dim AS_OutPath.PQ_Node(PQ_MAX_NODES)	; The generated path
Global AS_outSize						; Keeps track of the size of the final path

Function AS_Initialise()
	AS_outSize = 0						; Nothing in the outpath
	PQ_InitialisePQueue()				; Clear everything
End Function

;-------------------------------------------------------------
; AStar
;
; Each PQ_Node in the AStar graph has a list of neighbours.
; You make these neighbours any way you see fit. See the demo
; InitialiseMap() function. In that demo I don't bother 
; adding blocks that are IMPASSABLE as neighbours, that way
; we never see them in the path finding routine.
;-------------------------------------------------------------
Type AS_Neighbour
	Field nextNeigh.AS_Neighbour
	Field neigh.PQ_Node
	Field edgeCost					; Cost to get to this neighbour from it's parent neighbour
End Type
;-------------------------------------------------------------



;-------------------------------------------------------------
; Pathfinding initialisation
;
;
; This routine could also be changed to quit as soon as it finds
; the destination node, rather than keep searching for a better path.
; It's up to you!
;
;-------------------------------------------------------------

Function AS_MakeSuccessors(node.PQ_Node,endNode.PQ_Node)
	done = 0
	
	nn.AS_Neighbour = node\firstNeigh
	While nn <> Null
		newNode.PQ_Node = nn\neigh					; Get the actual node of this neighbour
		If newNode <> Null And newNode <> AS_rootNode And newNode\inClosed = 0 Then ;\root = 0 Then
			done = 1
			cost = nn\edgeCost + node\cost					; cost calculated here is basically the total
														  	; cost so far to get to this neighbour from 
															; the start. We keep a track of these costs
															; as we search so we can tell if we've found
															; a better path. It's made up of our parent's
															; cost so far, plus the cost to get to this
															; neighbour from the node we are looking at.
														
			
				goalDist = AS_DistTo(newNode,endNode)		; Heuristic to help our guess
				
				goalDist = goalDist * heuristic				; A multipler for our demo. This can be useful
															; as the higher it is the faster it will generally
															; be to find a path, but less accurate. So if your
															; game is in need of a rough guess, or it's running slow
															; then just up this value to speed up pathfinding at the
															; expense of accuracy
				
				f = goalDist + cost							; Our guess of TOTAL cost of this node


				DrawInfo(newNode,0,0,255)
			
; If this neighbour isn't in any lists (Not visited at all) then put it in the list of nodes to visit.
			If newNode\inClosed = 0 And newNode\inOpen = 0 Then
				newNode\parent = node
				newNode\key = f
				newNode\cost = cost
				PQ_Insert(newNode)
				newNode\inOpen = True
			Else
; Ok, it's on one of the lists, see if we have a better path to it by seeing if the cost to get to
; the neughbour node is more than the cost from our node to the neughbour. if it is then our path to
; this neighbour is better than that one it had.			
				If newNode\cost > cost Then 
				
					If newNode\inClosed Then 		; If this neighbour was in the closed list (IE we had
													; looked at it before, then remove it from the closed
													; list as it's going into the open list
						newNode\inClosed = False
					EndIf
					
					newNode\parent = node
					newNode\cost = cost
					newNode\key = f
					
					If newNode\inOpen Then			; If already in the open list, then remove it
													; THIS CAN BE OPTIMISED!!!
													; We could store where the position of the node in the
													; queue and simply add a DecreaseKey function to alter
													; a node's key. This way we wouldn't have to remove and
													; add it again. I'll do this later, or feel free to do
													; it yourself.
						p = PQ_Find(newNode)
						PQ_Remove(p)	
						newNode\inOpen = False		; For clarity, as it gets set again in a second					
					EndIf
					PQ_Insert(newNode)
					newNode\inOpen = True
					DrawInfo(newNode,0,0,255)
				EndIf
				
			EndIf							
		EndIf
		nn = nn\nextNeigh
	Wend
End Function


;-------------------------------------------------------------
; Main point of entry for the AStar algorithm. Simply pass
; in your start and end nodes and away you go.
;
; Will return -1 if it couldn't find a path to the destination.
; or 0 if it could. You can then pull the path nodes from the
; PQ_outPath array
;
;
; BIG NOTE:
; WHEN THIS ROUTINE EXITS OR AT SOME CONVENIENT TIME
; YOU SHOULD REINITIALISE THE NODE AS THEY MAY
; HAVE BEEN MODIFIED. IF YOU DONT RESET THESE VALUES THEN 
; ALL HELL COULD BREAK LOOSE.
; I DONT DO THIS BECAUSE IN THIS DEMO I REMAKE ALL THE NODES
; BEFORE A PATH FIND.
; YOU SHOULD ONLY HAVE TO REINITIALISE
;
; inOpen and inClosed both to 0
;-------------------------------------------------------------
Function AS_FindPath(startNode.PQ_Node,endNode.PQ_Node)

; Set up our starting node's information
	goalDist = AS_DistTo(startNode,endNode)
	startNode\cost = 0
	startNode\key = goalDist + startNode\cost
	startNode\parent = Null
	AS_rootNode = startNode
	PQ_Insert(startNode)

	startNode\inOpen = True

	While PQ_size > 0 
	
		node.PQ_Node = PQ_Pop()				; Remove the node with the best cost (node\key)
		node\inOpen = 0						; It's no longer in the open list

		Drawinfo(node,255,0,0)

;		Delay(SLOWMO * 100 )		
		;
;		Delay(100)
		;
;		If CheckKeys() Then Return -1		; ABORT
		
; Have we reached the end? If so make our final path
		If node = endNode Then
			AS_outSize = 0
			While node <> Null
				AS_OutPath(AS_outSize) = node
				node = node\parent
				AS_outSize = AS_outSize + 1
			Wend
			Return 0
		EndIf
; Make all the possible neighbours of this node, putting them into the priority queue
; in the right order.
		AS_MakeSuccessors(node,endNode)
		node\inClosed = True				; Put in closed list because we have visited this node

;		DrawInfo(node,0,255,0)
	Wend
	Return -1
End Function

;-----------------------------------------------------------------------------------------
; Simply function to compute the heuristic for our search. I simply use a version of the Euclidean
; distance.
;
; If this function returns a cost that is less than or at most equal to the REAL cost of the path
; between those 2 points, AStart will generate an optimal path. This will generally be a slower path to 
; generate also, so there is a trade off between speed and accuracy.
;-----------------------------------------------------------------------------------------
Function AS_DistTo(s.PQ_Node, e.PQ_Node)
	x1 = s\x
	y1 = s\y
	x2 = e\x
	y2 = e\y
	
	dx = Abs(x2 - x1)
	dy = Abs(y2 - y1)

	Select DIST_CALC_METHOD
		Case 0:	Return Sqr(dx*dx + dy*dy)				; Euclidean
		Case 1: Return dx*dx + dy*dy					; Pseudo Euclidean. Less accurate but faster
		Case 2: Return dx+dy							; Manhattan
		Case 3: If dx>dy Then Return dx Else Return dy	; Max d
		Default:End										; Catch for any mistakes
	End Select
End Function

Function CheckKeys()
	k = GetKey()
	If k = 32 Then While KeyDown(57):Wend:Return 1
	If k = Asc("p") Then
		While GetKey() <> Asc("p") :Wend
	EndIf
	Return 0
End Function

Function DrawInfo(node.PQ_Node,r,g,b)
	If DRAW_NODES Then
		Color r,g,b
		Rect node\x * BLOCK_SIZE, node\y * BLOCK_SIZE, BLOCK_SIZE - 1,BLOCK_SIZE - 1
	EndIf
	If SHOW_COSTS
		Color 255,255,255
		Text node\x * BLOCK_SIZE, node\y * BLOCK_SIZE,Str$(Int(node\cost))
		Text node\x * BLOCK_SIZE, 8+node\y * BLOCK_SIZE,Str$(Int(node\key))
	EndIf				
End Function