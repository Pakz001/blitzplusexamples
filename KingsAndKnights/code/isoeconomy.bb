;
;
; Economy
;
;

Const maxgoods = 199
Const maxproduction = 100
Global numgoods = 0
Global numproduction = 0
Dim productionstr$(maxproduction,20)
Dim production(maxproduction,20)
Dim goodsstr$(maxgoods,20)
Dim Goods(maxgoods,20)
Dim citiesgoods(500,maxgoods) ; Holds the supply of the cities store of the units
Dim citiesmarket(500,maxgoods) ; Holds the available goods for buying purposes
Dim citiesproduction(500,maxgoods)
Dim citiesrequirements(500,maxgoods)

For i=0 To 500
	For ii=0 To maxgoods
		citiesmarket(i,ii) = Rand(0,255)
		citiesproduction(i,ii) = Rand(0,7)
		citiesrequirements(i,ii) = Rand(0,2)
	Next
Next

Const production_active = 0
Const production_item = 1
Const production_requires = 10
Const production_cost = 11
Const production_produces = 12
Const production_upkeep =13

Const goods_active = 0
Const goods_item = 1
Const goods_origin = 2
Const goods_defaultprice = 3
Const goods_req1 = 4
Const goods_reqamount1 = 5
Const goods_req2 = 6
Const goods_reqamount2 = 7
Const goods_req3 = 8
Const goods_reqamount3 = 9

;Graphics 800,600,16,2

loadeconomy()

;Print "Ready"
;WaitKey()
;End

;
; Loads the economy configuration
;
Function loadeconomy()
Local economyfound = False
Local goodsfound = False
Local fileerror$ = ""
Local goodsrequierementsfound = False
Local productionfound = False
Local endfound = False
file = OpenFile("economy.txt")
If file = 0 Then eco_simplemessage("Cannot find economy.txt") : End
While Eof(file) = False
	a$ = ReadLine(file)
	If Lower(Left(a$,Len("@economy"))) = "@economy" Then 
		economyfound = True
		;
		While Eof(file) = False ; Find the next header
			a$ = ReadLine(file)
			;
			; ----------------------------------------
			;                Read in the goods from the config
			;
			;
			If Lower(Left(a$,Len("@goods"))) = "@goods" Then
				goodsfound = True
				counter = 0
				While Eof(file) = False	; Reading goods			
					a$ = ReadLine(file)
					; Parse the goods
					If a$<> "" And (Not Left(a$,1)= " ") And (Not Left(a$,1) = ";") And (Not Left(a$,1) = "@") Then
						parsegoods(counter,a$)
						counter = counter + 1
					End If
					;
					If Lower(Left(a$,Len("@"))) = "@" Then
						numgoods = counter-1
						Exit
					End If
				Wend
			End If
			;
			If Lower(Left(a$,Len("@goods_requirements"))) = "@goods_requirements" Then
				If goodsfound = True Then goodsrequirementsfound = True
				counter = 0
				While Eof(file) = False	; Reading goods			
					a$ = ReadLine(file)
					; Parse the goods requirements					
					If a$<> "" And (Not Left(a$,1)= " ") And (Not Left(a$,1) = ";") And (Not Left(a$,1) = "@") Then
						parsegoodsrequirements(counter,a$)
						counter = counter + 1
					End If

					;
					If Lower(Left(a$,Len("@"))) = "@" Then
						Exit
					End If
				Wend
			End If
			;
			If Lower(Left(a$,Len("@production"))) = "@production" Then
				If goodsrequirementsfound = True Productionfound = True
				counter = 0
				While Eof(file) = False	; Reading goods			
					a$ = ReadLine(file)
					; Parse the production
					If a$<> "" And (Not Left(a$,1)= " ") And (Not Left(a$,1) = ";") And (Not Left(a$,1) = "@") Then
						parseproduction(counter,a$)
						counter = counter + 1
					End If
					;
					If Lower(Left(a$,Len("@"))) = "@" Then
						numproduction = counter-1
						Exit
					End If
				Wend
			End If
			;
			If Lower(Left(a$,Len("@end"))) = "@end" Then
				endfound = True
			End If
			;
			;
		Wend		
		;
	End If
Wend
.skipout
If fileerror$ <> "" Then eco_simplemessage(fileerror$) : End
If economyfound = False Then eco_simplemessage("Header '@economy' not found") : End
If goodsfound = False Then eco_simplemessage("Goods header '@goods' not found") : End
If goodsrequirementsfound = False Then eco_simplemessage("Header goods requirements '@goods_requirements' not found") : End
If productionfound = False Then eco_simplemessage("Header production '@production' not found") : End
If endfound = False Then eco_simplemessage("Header End '@End' not found") : End
End Function

;
; Translate the production from the diplomacy.txt file into the game arrays
;
;
Function parseproduction(counter,in$)
num = parsestring(in$)
;
;Dim productionstr$(100)
;Dim production(100,20)

If parse$(0) = "" Then eco_simplemessage("Error in 'Economy.txt' (production)")

production(counter,production_active) = True
productionstr$(counter,production_item) = parse$(0) ; Move the name into the string$
productionstr$(counter,production_requires) = parse$(1) ; Required good/production/flexible
production(counter,production_cost) = parse$(2)
production(counter,production_produces) = parse$(3)
production(counter,production_upkeep) = parse$(4)


;Print parse$(0)+","+parse$(1)+","+parse$(2)+","+parse$(3)+","+parse$(4)

;
End Function

;
; This fill in the goodsrequirements into the arrays
;
;
Function parsegoodsrequirements(counter,in$)
	; num contains the number of parsed things
	;
	num = parsestring(in$)
	;	
	; Read in the first
	a = returngoodheaderid(parse$(0))
	If a > -1 Then
		goodsstr$(a,goods_req1) = parse$(1)
		goods(a,goods_reqamount1) = parse$(2)
		goodsstr$(a,goods_req2) = parse$(3)
		goods(a,goods_reqamount2) = parse$(4)
		goodsstr$(a,goods_req3) = parse$(5)
		goods(a,goods_reqamount3) = parse$(6)
		;Print goodsstr$(a,goods_item) + "," + goodsstr$(a,goods_req1) + "," + goods(a,goods_reqamount1)+ "," + goodsstr$(a,goods_req2) + "," + goods(a,goods_reqamount2)+ "," + goodsstr$(a,goods_req3) + "," + goods(a,goods_reqamount3)
		Else
		;Print "Good "+parse$(0)+ " not found. Error in 'Economy.txt'"
		eco_simplemessage("Good "+parse$(0)+ " not found. Error in 'Economy.txt'")
	End If
	
End Function

;
; Translate the goods into the arrays
;
Function parsegoods(counter,in$)
	; Parse the current string
	parsestring(in$)
	;
	If parse$(0) = "" Then eco_simplemessage("Error in 'economy.txt") : End
	;
	goodsstr$(counter,goods_active) = True ; Active the next good
	goodsstr$(counter,goods_item) = parse$(0) ; Move the name into the slot
	goodsstr$(counter,goods_origin) = parse$(1) ; Move the origin into the slot
	goods(counter,goods_defaultprice) = parse$(2); Move the default price into the slot
	
	;Print goodsstr$(counter,goods_item)+","+goodsstr$(counter,goods_origin)+","+goods(counter,goods_defaultprice)
End Function 


;
;
; This returns the id of where the good is located
Function returngoodheaderid(in$)
	For i=0 To maxgoods
		If goodsstr$(i,goods_item) = in$ Then Return i
	Next
	Return -1
End Function




Function eco_simplemessage(s$)
exitmes$ = "Press any key to continue"
w = StringWidth(s)
If StringWidth(exitmes)>w Then w = StringWidth(exitmes)
;
Color 0,0,0
Rect screenwidth/2-(w/2+20),screenheight/2-100,w+40,200,1
x = 800/2-(w/2+20)
y = 600/2-100
w1 = w+40
h1 = 200
;gwindow(x,y,w1,h1,1,0,0)
;gwindow(x+5,y+10,w1-10,h1-20,0,1,1)
Color 255,255,255
Text 800/2,600/2-40,s,1,1
Text 800/2,600/2,exitmes,1,1
Flip
Delay(50)
FlushKeys()
While KeyDown(1) = False And MouseDown(1)=False And MouseDown(2) = False And KeyDown(57)=False And KeyDown(28) = False 
Delay(1)
Wend
Delay(200)
FlushKeys()
End Function

