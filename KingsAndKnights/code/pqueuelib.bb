;-----------------------------------------------------------
; PRIORITY QUEUE INCLUDE
; by Aaron Koolen
;
; Feel free to use this code for whatever purposes you want
;
;-----------------------------------------------------------

;-------------------------------------------------------------
; PQUEUE Stuff
; YOU NEED THIS BLOCK!
;-------------------------------------------------------------
Const PQ_MAX_NODES = 32*32				; Change for maximum nodes on your queue
Dim PQ_Nodes.PQ_Node(PQ_MAX_NODES)		; Our graph of nodes
Global PQ_size							; The # of nodes in the queue (open list for astar)
;-------------------------------------------------------------

;-------------------------------------------------------------
; PRIORITY QUEUE AND ASTAR STUFF
;-------------------------------------------------------------


Type PQ_Node
;-------------------------------------------------------
; FIELDS THAT ASTAR NEEDS. DO NOT CHANGE UNTIL YOU KNOW
; WHAT YOU ARE DOING
;-------------------------------------------------------
							
	Field key#					; This is our guestimated of this node
								; This is the key that the priority queue uses to keep the queue sorted
								; The lower the better node we think it is. Really only used in the queue
								; 'cost' is the value that decides if a node is ACTUALLY better or not
								
								
	Field cost#					; Secondary cost. This is basically the cost to get to this node.
								; from the start
	Field parent.PQ_Node		; Points to our parent. This is so once we have found the destination
								; we can trace back through our parents to build the final path to take
	
	Field firstNeigh.AS_Neighbour	; All the neighbours of this node
	Field inOpen,inClosed			; What list this node is in. 
									
;-------------------------------------------------------
; YOUR NODE ADDITIONS GO HERE
;-------------------------------------------------------

; Here is where you stick your own additions to the Nodes. For instance you might want to stick the
; X and y positions of the node in here. I do this so when I have found a path I can quickly grab each
; node's x and y and draw it
	Field x,y					; Where in the map this node is
End Type


Function PQ_InitialisePQueue()
	PQ_size = 0
End Function

;-------------------------------------------------
; Insert's a node into the priority queue
;-------------------------------------------------
Function PQ_Insert(newNode.PQ_Node)
	PQ_size = PQ_size + 1
	PQ_Nodes(PQ_size) = newNode
	PQ_ReheapUp(PQ_size)
End Function

;-------------------------------------------------
; Returns the index of the parent of a queue element
;-------------------------------------------------
Function PQ_Parent(index%)
	Return index / 2
End Function

;-------------------------------------------------
; Returns the left child of a queue element
;-------------------------------------------------
Function PQ_Left(index%)
	Return index * 2
End Function

;-------------------------------------------------
; Returns the right child of a queue element
;-------------------------------------------------
Function PQ_Right(index%)
	Return index * 2 + 1
End Function
	
	
;-------------------------------------------------
; Takes an element and moves it upwards in the heap
;-------------------------------------------------
Function PQ_ReheapUp(index)
	parent = PQ_Parent(index)
	ok = False
	While( index > 1 And ok = 0 )
		If PQ_Nodes(parent)\key < PQ_Nodes(index)\key Then
			ok = True
		Else
			t.PQ_Node = PQ_Nodes(parent)
			PQ_Nodes(parent) = PQ_Nodes(index)
			PQ_Nodes(index) = t
			index = parent
			parent = PQ_Parent(index)
		EndIf
	Wend
	Return index
End Function


;-------------------------------------------------
; Takes an element and moves it down in the heap
;-------------------------------------------------
Function PQ_ReheapDown(root%, bottom%)

	ok = False
	maxChild = 0
	
	While root * 2 <= bottom And ok = 0
		If PQ_Left(root) = bottom
			maxChild = PQ_Left(root)
		Else
			If PQ_Nodes(PQ_Left(root))\key < PQ_Nodes(PQ_Right(root))\key 
				maxChild = PQ_Left(root)
			Else
				maxChild = PQ_Right(root)
			EndIf				
		EndIf
		If Not (PQ_Nodes(root)\key < PQ_Nodes(maxChild)\key ) Then
			t.PQ_Node = PQ_Nodes(root)
			PQ_Nodes(root) = PQ_Nodes(maxChild)
			PQ_Nodes(maxChild) = t
			root = maxChild
		Else
			ok = True
		EndIf
	Wend
	
	Return root

End Function


;-------------------------------------------------
; Removes an element from the heap
;-------------------------------------------------
Function PQ_Remove(index%)
	PQ_Nodes(index) = PQ_Nodes(PQ_size)
	PQ_size = PQ_size - 1
	PQ_ReheapDown(index,PQ_size)
End Function

;-------------------------------------------------
; Find's an element in the heap
;-------------------------------------------------
Function PQ_Find( find.PQ_Node )
	For t = 1 To PQ_size
		If find = PQ_Nodes(t) Then Return t
	Next
	Return 0
End Function

;-------------------------------------------------
; Returns the top element of the heap, removing it
; and reordering the heap.
;-------------------------------------------------
Function PQ_Pop.PQ_Node()
	ret.PQ_Node = PQ_Nodes(1)
	PQ_Nodes(1) = PQ_Nodes(PQ_size)
	PQ_size = PQ_size - 1
	PQ_ReheapDown(1,PQ_size)
	Return ret
End Function