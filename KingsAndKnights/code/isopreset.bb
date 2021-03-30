;
; This include holds the preset information
;
;

;roadmap(1,10)=1

;
Const maxunitgraphs = 99
Dim unitsgraphsnames$(99)

loadunitsconfig()

screenwidth = 800
screenheight = 600

mapx = 0
mapy = 0

mapwidth = 100
mapheight = 100

grid = True

loadtribesnames()
loadcitynames()


; set the entire array to false
For i=0 To 1000
For ii=0 To 11
units(i,ii) = False
Next
Next

For i=0 To 200
For ii=0 To 10
cities(i,ii) = False
Next
Next

playergold(0) = 100
playergold(1) = 100
playergold(2) = 100
playergold(3) = 100
playergold(4) = 100
playergold(5) = 100
playergold(6) = 100
playergold(7) = 100
playergold(8) = 100


humanplays(1) = True ; true/false true = played by human / false played by computer
humanplays(2) = False
humanplays(3) = False
humanplays(4) = False
humanplays(5) = False
humanplays(6) = False
humanplays(7) = False
humanplays(8) = False

;units(0,0) = True  ; Active
;units(0,1) = 1     ; X pos
;units(0,2) = 1     ; Y pos
;units(0,3) = True  ; Ontop status
;units(0,4) = 1     ; Unit type 
;units(0,5) = 2     ; Attack
;units(0,6) = 1     ; Defense
;units(0,7) = 0     ; Damage (0 - no, 9 - max)
;units(0,8) = False ; Fortified
;units(0,9) = True  ; Veteran status 
;units(0,10) = 2    ; Moves left
;units(0,11) = 1    ; Belongs to player x
;units(0,12) = true ; False if units is on hold
;units(0,13) = 0    ; AI/settler method
;units(0,14) = False; Is in a city
;units(0,15) = 0    ; turns inactive
;units(0,16) = 0    ; Home city


units(1,0) = True  ; Active
units(1,1) = 12     ; X pos
units(1,2) = 12     ; Y pos
units(1,3) = True  ; Ontop status
units(1,4) = 3     ; Unit type 
units(1,5) = 2     ; Attack
units(1,6) = 1     ; Defense
units(1,7) = 0     ; Damage (0 - no , 99 - max)
units(1,8) = False ; Fortified
units(1,9) = False ; Veteran status 
units(1,10) = 2    ; Moves left
units(1,11) = 1    ; Belongs to player x
units(1,12) = False; true if units is on hold
units(1,13) = 0    ; AI method
units(1,14) = False; Unit is inside a city
units(1,15) = 0    ; turns inactive
units(1,16) = 0    ; Home city


units(2,0) = True  ; Active
units(2,1) = 12     ; X pos
units(2,2) = 12     ; Y pos
units(2,3) = False ; Ontop status
units(2,4) = 3     ; Unit type 
units(2,5) = 2     ; Attack
units(2,6) = 1     ; Defense
units(2,7) = 0     ; Damage (0 - no, 99 - max)
units(2,8) = False ; Fortified
units(2,9) = False ; Veteran status 
units(2,10) = 2    ; Moves left
units(2,11) = 1    ; Belongs to player x
units(2,12) = False; true if units is on hold
units(2,13) = 0    ; AI method
units(2,14) = False; Unit is inside a city
units(2,15) = 0    ; turns inactive
units(2,16) = 0    ; Home city

units(3,0) = True  ; Active
units(3,1) = 12     ; X pos
units(3,2) = 12     ; Y pos
units(3,3) = False ; Ontop status
units(3,4) = 3     ; Unit type 
units(3,5) = 2     ; Attack
units(3,6) = 1     ; Defense
units(3,7) = 0     ; Damage (0 - no, 99 - max)
units(3,8) = True  ; Fortified
units(3,9) = False ; Veteran status 
units(3,10) = 2    ; Moves left
units(3,11) = 1    ; Belongs to player x
units(3,12) = False; true if units is on hold
units(3,13) = 0    ; AI method
units(3,14) = False; Unit is inside a city
units(3,15) = 0    ; turns inactive
units(3,16) = 0    ; Home city

units(4,0) = True  ; Active
units(4,1) = 12     ; X pos
units(4,2) = 16     ; Y pos
units(4,3) = True  ; Ontop status
units(4,4) = 5     ; Unit type 
units(4,5) = 2     ; Attack
units(4,6) = 1     ; Defense
units(4,7) = 0     ; Damage (0 - no, 99 - max)
units(4,8) = False ; Fortified
units(4,9) = False ; Veteran status 
units(4,10) = 2    ; Moves left
units(4,11) = 1    ; Belongs to player x
units(4,12) = False; true if units is on hold
units(4,13) = 0    ; AI method
units(4,14) = False; Unit is inside a city
units(4,15) = 0    ; turns inactive
units(4,16) = 0    ; Home city

units(5,0) = True  ; Active
units(5,1) = 12     ; X pos
units(5,2) = 17     ; Y pos
units(5,3) = True  ; Ontop status
units(5,4) = 5     ; Unit type 

units(5,5) = 2     ; Attack
units(5,6) = 1     ; Defense
units(5,7) = 0     ; Damage (0 - no, 99 - max)
units(5,8) = False ; Fortified
units(5,9) = False ; Veteran status 
units(5,10) = 2    ; Moves left
units(5,11) = 1    ; Belongs to player x
units(5,12) = False; true if units is on hold
units(5,13) = 0    ; AI method
units(5,14) = False; Unit is inside a city
units(5,15) = 0    ; turns inactive
units(5,16) = 0    ; Home city

units(6,0) = True  ; Active
units(6,1) = 12     ; X pos
units(6,2) = 18     ; Y pos
units(6,3) = True  ; Ontop status
units(6,4) = 6     ; Unit type 
units(6,5) = 2     ; Attack
units(6,6) = 1     ; Defense
units(6,7) = 0     ; Damage (0 - no, 99 - max)
units(6,8) = False ; Fortified
units(6,9) = False ; Veteran status 
units(6,10) = 2    ; Moves left
units(6,11) = 1    ; Belongs to player x
units(6,12) = False; True If units is on hold
units(6,13) = 0    ; AI method
units(6,14) = False; Unit is inside a city
units(6,15) = 0    ; turns inactive
units(6,16) = 0    ; Home city

units(7,0) = True  ; Active
units(7,1) = 14;15     ; X pos
units(7,2) = 36;20    ; Y pos
units(7,3) = True  ; Ontop status
units(7,4) = 7     ; Unit type 
units(7,5) = 2     ; Attack
units(7,6) = 1     ; Defense
units(7,7) = 0     ; Damage (0 - no, 99 - max)
units(7,8) = False ; Fortified
units(7,9) = False ; Veteran status 
units(7,10) = 2    ; Moves left
units(7,11) = 1    ; Belongs to player x
units(7,12) = False; true if units is on hold
units(7,13) = 0    ; AI method
units(7,14) = False; Unit is inside a city
units(7,15) = 0    ; turns inactive
units(7,16) = 0    ; Home city

units(8,0) = False ; Active
units(8,1) = 14     ; X pos
units(8,2) = 21    ; Y pos
units(8,3) = True  ; Ontop status
units(8,4) = 7     ; Unit type 
units(8,5) = 2     ; Attack
units(8,6) = 1     ; Defense
units(8,7) = 0     ; Damage (0 - no, 99 - max)
units(8,8) = False ; Fortified
units(8,9) = False ; Veteran status 
units(8,10) = 2    ; Moves left
units(8,11) = 2    ; Belongs to player x
units(8,12) = False; true if units is on hold
units(8,13) = 0    ; AI method
units(8,14) = False; Unit is inside a city
units(8,15) = 0    ; turns inactive
units(8,16) = 0    ; Home city

units(9,0) = False ; Active
units(9,1) = 13     ; X pos
units(9,2) = 21    ; Y pos
units(9,3) = True  ; Ontop status
units(9,4) = 7     ; Unit type 
units(9,5) = 2     ; Attack
units(9,6) = 1     ; Defense
units(9,7) = 0     ; Damage (0 - no, 99 - max)
units(9,8) = False ; Fortified
units(9,9) = False ; Veteran status 
units(9,10) = 2    ; Moves left
units(9,11) = 2    ; Belongs to player x
units(9,12) = False; true if units is on hold
units(9,13) = 0    ; AI method
units(9,14) = False; Unit is inside a city
units(9,15) = 0    ; turns inactive
units(9,16) = 0    ; Home city

units(10,0) = False ; Active
units(10,1) = 12    ; X pos
units(10,2) = 21    ; Y pos
units(10,3) = True  ; Ontop status
units(10,4) = 7     ; Unit type 
units(10,5) = 2     ; Attack
units(10,6) = 1     ; Defense
units(10,7) = 0     ; Damage (0 - no, 99 - max)
units(10,8) = False ; Fortified
units(10,9) = False ; Veteran status 
units(10,10) = 2    ; Moves left
units(10,11) = 2    ; Belongs to player x
units(10,12) = False; true if units is on hold
units(10,13) = 0    ; AI method
units(10,14) = False; Unit is inside a city
units(10,15) = 0    ; turns inactive
units(10,16) = 0    ; Home city

; player 3
units(11,0) = True  ; Active
units(11,1) = 14     ; X pos
units(11,2) = 42    ; Y pos
units(11,3) = True  ; Ontop status
units(11,4) = 7     ; Unit type 
units(11,5) = 2     ; Attack
units(11,6) = 1     ; Defense
units(11,7) = 0     ; Damage (0 - no, 99 - max)
units(11,8) = False ; Fortified
units(11,9) = False ; Veteran status 
units(11,10) = 2    ; Moves left
units(11,11) = 3    ; Belongs to player x
units(11,12) = False; true if units is on hold
units(11,13) = 0    ; AI method
units(11,15) = 0    ; turns inactive
units(11,16) = 0    ; Home city

units(12,0) = True  ; Active
units(12,1) = 14     ; X pos
units(12,2) = 43    ; Y pos
units(12,3) = True  ; Ontop status
units(12,4) = 7     ; Unit type 
units(12,5) = 2     ; Attack
units(12,6) = 1     ; Defense
units(12,7) = 0     ; Damage (0 - no, 99 - max)
units(12,8) = False ; Fortified
units(12,9) = False ; Veteran status 
units(12,10) = 2    ; Moves left
units(12,11) = 3    ; Belongs to player x
units(12,12) = False; true if units is on hold
units(12,13) = 0    ; AI method
units(12,15) = 0    ; turns inactive
units(12,16) = 0    ; Home city

units(13,0) = True  ; Active
units(13,1) = 14     ; X pos
units(13,2) = 44    ; Y pos
units(13,3) = True  ; Ontop status
units(13,4) = 7     ; Unit type 
units(13,5) = 2     ; Attack
units(13,6) = 1     ; Defense
units(13,7) = 0     ; Damage (0 - no, 99- max)
units(13,8) = False ; Fortified
units(13,9) = False ; Veteran status 
units(13,10) = 2    ; Moves left
units(13,11) = 3    ; Belongs to player x
units(13,12) = False; true if units is on hold
units(13,13) = 0    ; AI method
units(13,15) = 0    ; turns inactive
units(13,16) = 0    ; Home city

units(14,0) = True  ; Active
units(14,1) = 14     ; X pos
units(14,2) = 45    ; Y pos
units(14,3) = True  ; Ontop status
units(14,4) = 7     ; Unit type 
units(14,5) = 2     ; Attack
units(14,6) = 1     ; Defense
units(14,7) = 0     ; Damage (0 - no, 99 - max)
units(14,8) = False ; Fortified
units(14,9) = False ; Veteran status 
units(14,10) = 2    ; Moves left
units(14,11) = 3    ; Belongs to player x
units(14,12) = False; true if units is on hold
units(14,13) = 0    ; AI method
units(14,15) = 0    ; turns inactive
units(14,16) = 0    ; Home city
; 00000000000000000000000000000000

units(15,0) = True  ; Active
units(15,1) = 15     ; X pos
units(15,2) = 15     ; Y pos
units(15,3) = False ; Ontop status
units(15,4) = 1     ; Unit type 
units(15,5) = 2     ; Attack
units(15,6) = 1     ; Defense
units(15,7) = 0     ; Damage (0 - no, 99 - max)
units(15,8) = False ; Fortified
units(15,9) = False ; Veteran status 
units(15,10) = 2    ; Moves left
units(15,11) = 1    ; Belongs to player x
units(15,12) = False; True If units is on hold
units(15,13) = 0    ; AI method
units(15,14) = True ; Unit is inside a city
units(15,15) = 0    ; turns inactive
units(15,16) = 0    ; Home city

units(16,0) = True  ; Active
units(16,1) = 15     ; X pos
units(16,2) = 15     ; Y pos
units(16,3) = False ; Ontop status
units(16,4) = 2     ; Unit type 
units(16,5) = 2     ; Attack
units(16,6) = 1     ; Defense
units(16,7) = 0     ; Damage (0 - no, 99 - max)
units(16,8) = False ; Fortified
units(16,9) = False ; Veteran status 
units(16,10) = 2    ; Moves left
units(16,11) = 1    ; Belongs to player x
units(16,12) = False; true if units is on hold
units(16,13) = 0    ; AI method
units(16,14) = True ; Unit is inside a city
units(16,15) = 0    ; turns inactive
units(16,16) = 0    ; Home city

units(17,0) = True  ; Active
units(17,1) = 15     ; X pos
units(17,2) = 15     ; Y pos
units(17,3) = False ; Ontop status
units(17,4) = 3     ; Unit type 
units(17,5) = 2     ; Attack
units(17,6) = 1     ; Defense
units(17,7) = 0     ; Damage (0 - no, 99 - max)
units(17,8) = False ; Fortified
units(17,9) = False ; Veteran status 
units(17,10) = 2    ; Moves left
units(17,11) = 1    ; Belongs to player x
units(17,12) = False; true if units is on hold
units(17,13) = 0    ; AI method
units(17,14) = True ; Unit is inside a city
units(17,15) = 0    ; turns inactive
units(17,16) = 0    ; Home city

units(18,0) = True  ; Active
units(18,1) = 15     ; X pos
units(18,2) = 15     ; Y pos
units(18,3) = False ; Ontop status
units(18,4) = 4     ; Unit type 
units(18,5) = 2     ; Attack
units(18,6) = 1     ; Defense
units(18,7) = 0     ; Damage (0 - no, 99 - max)
units(18,8) = False ; Fortified
units(18,9) = False ; Veteran status 
units(18,10) = 2    ; Moves left
units(18,11) = 1    ; Belongs to player x
units(18,12) = False; true if units is on hold
units(18,13) = 0    ; AI method
units(18,14) = True ; Unit is inside a city
units(18,15) = 0    ; turns inactive
units(18,16) = 0    ; Home city

units(19,0) = True  ; Active
units(19,1) = 15     ; X pos
units(19,2) = 15     ; Y pos
units(19,3) = False ; Ontop status
units(19,4) = 5     ; Unit type 
units(19,5) = 2     ; Attack
units(19,6) = 1     ; Defense
units(19,7) = 0     ; Damage (0 - no, 99 - max)
units(19,8) = False ; Fortified
units(19,9) = False ; Veteran status 
units(19,10) = 2    ; Moves left
units(19,11) = 1    ; Belongs to player x
units(19,12) = False; true if units is on hold
units(19,13) = 0    ; AI method
units(19,14) = True ; Unit is inside a city
units(19,15) = 0    ; turns inactive
units(19,16) = 0    ; Home city

units(20,0) = True  ; Active
units(20,1) = 15     ; X pos
units(20,2) = 15     ; Y pos
units(20,3) = False ; Ontop status
units(20,4) = 1     ; Unit type 
units(20,5) = 2     ; Attack
units(20,6) = 1     ; Defense
units(20,7) = 0     ; Damage (0 - no, 99 - max)
units(20,8) = False ; Fortified
units(20,9) = False ; Veteran status 
units(20,10) = 2    ; Moves left
units(20,11) = 1    ; Belongs to player x
units(20,12) = False; True If units is on hold
units(20,13) = 0    ; AI method
units(20,14) = True ; Unit is inside a city
units(20,15) = 0    ; turns inactive
units(20,16) = 0    ; Home city

units(21,0) = True  ; Active
units(21,1) = 15     ; X pos
units(21,2) = 15     ; Y pos
units(21,3) = False ; Ontop status
units(21,4) = 2     ; Unit type 
units(21,5) = 2     ; Attack
units(21,6) = 1     ; Defense
units(21,7) = 0     ; Damage (0 - no, 99 - max)
units(21,8) = False ; Fortified
units(21,9) = False ; Veteran status 
units(21,10) = 2    ; Moves left
units(21,11) = 1    ; Belongs to player x
units(21,12) = False; true if units is on hold
units(21,13) = 0    ; AI method
units(21,14) = True ; Unit is inside a city
units(21,15) = 0    ; turns inactive
units(21,16) = 0    ; Home city

units(22,0) = True  ; Active
units(22,1) = 15     ; X pos
units(22,2) = 15     ; Y pos
units(22,3) = False ; Ontop status
units(22,4) = 3     ; Unit type 
units(22,5) = 2     ; Attack
units(22,6) = 1     ; Defense
units(22,7) = 0     ; Damage (0 - no, 99 - max)
units(22,8) = False ; Fortified
units(22,9) = False ; Veteran status 
units(22,10) = 2    ; Moves left
units(22,11) = 1    ; Belongs to player x
units(22,12) = False; true if units is on hold
units(22,13) = 0    ; AI method
units(22,14) = True ; Unit is inside a city
units(22,15) = 0    ; turns inactive
units(22,16) = 0    ; Home city

units(23,0) = True  ; Active
units(23,1) = 15     ; X pos
units(23,2) = 15     ; Y pos
units(23,3) = False ; Ontop status
units(23,4) = 4     ; Unit type 
units(23,5) = 2     ; Attack
units(23,6) = 1     ; Defense
units(23,7) = 0     ; Damage (0 - no, 99 - max)
units(23,8) = False ; Fortified
units(23,9) = False ; Veteran status 
units(23,10) = 2    ; Moves left
units(23,11) = 1    ; Belongs to player x
units(23,12) = False; true if units is on hold
units(23,13) = 0    ; AI method
units(23,14) = True ; Unit is inside a city
units(23,15) = 0    ; turns inactive
units(23,16) = 0    ; Home city

units(24,0) = True  ; Active
units(24,1) = 15     ; X pos
units(24,2) = 15     ; Y pos
units(24,3) = False ; Ontop status
units(24,4) = 5     ; Unit type 
units(24,5) = 2     ; Attack
units(24,6) = 1     ; Defense
units(24,7) = 0     ; Damage (0 - no, 99 - max)
units(24,8) = False ; Fortified
units(24,9) = False ; Veteran status 
units(24,10) = 2    ; Moves left
units(24,11) = 1    ; Belongs to player x
units(24,12) = False; true if units is on hold
units(24,13) = 0    ; AI method
units(24,14) = True ; Unit is inside a city
units(24,15) = 0    ; turns inactive
units(24,16) = 0    ; Home city

; Settler unit
units(25,0) = True  ; Active
units(25,1) = 15     ; X pos
units(25,2) = 16     ; Y pos
units(25,3) = True  ; Ontop status
units(25,4) = 10    ; Unit type 
units(25,5) = 1     ; Attack
units(25,6) = 1     ; Defense
units(25,7) = 0     ; Damage (0 - no, 99 - max)
units(25,8) = False ; Fortified
units(25,9) = False ; Veteran status 
units(25,10) = 1    ; Moves left
units(25,11) = 1    ; Belongs to player x
units(25,12) = False; true if units is on hold
units(25,13) = 0    ; AI method/unit method
units(25,14) = False; Unit is inside a city
units(25,15) = 0    ; turns inactive
units(25,16) = 0    ; Home city



;Setup the first city
cities(1,0) = True ; Active true or false
cities(1,1) = 15 ; City x location
cities(1,2) = 15 ; City y location
citiesstring$(1,0) = "Valhalla" ; city name
cities(1,3) = 1 ; City type/level
cities(1,4) = 1 ; Belongs to player x
cities(1,5) = 11; tax income

;Setup the second city
cities(2,0) = True ; Active true or false
cities(2,1) = 14 ; City x location
cities(2,2) = 22; City y location
citiesstring$(2,0) = "Rhinotown" ; city name
cities(2,3) = 1 ; City type/level
cities(2,4) = 3 ; Belongs to player 
cities(2,5) = 13; tax income

;Setup the third city
cities(3,0) = True ; Active true or false
cities(3,1) = 12 ; City x location
cities(3,2) = 42; City y location
citiesstring$(3,0) = "Doncastle" ; city name
cities(3,3) = 1 ; City type/level
cities(3,4) = 3 ; Belongs to player x
cities(3,5) = 12; tax income


; These contains the names of the units
; Now handled from text files
;unitsname$(1) = "Workforce"
;unitsname$(2) = "Peasants"
;unitsname$(3) = "Soldiers"
;unitsname$(4) = "Spearmen"
;unitsname$(5) = "Axemen"
;unitsname$(6) = "Archers"
;unitsname$(7) = "Mounted Soldiers"
;unitsname$(8) = "Knights"
;unitsname$(9) = "Ballista's"
;unitsname$(10)= "Catapults"
;unitsname$(11)= "Siege Towers"
;unitsname$(12)= "Wolfs"
;unitsname$(13)= "Bears"
;unitsname$(14)= "Carts"
;unitsname$(15)= "Sail"
;unitsname$(16)= "Medium Ship"
;unitsname$(17)= "Cargo Ship"




; Workforce
;unitdefault(1,0) = 1 ; attack
;unitdefault(1,1) = 1 ; defense
;unitdefault(1,2) = 1 ; movement
;unitdefault(1,3) = 1 ; hitpoints
;unitdefault(1,4) = 35; cost
;unitdefault(1,5) = 0 ; can carry no unit

; Peasant
;unitdefault(2,0) = 1 ; attack
;unitdefault(2,1) = 1 ; defense
;unitdefault(2,2) = 1 ; movement
;unitdefault(2,3) = 1 ; hitpoints
;unitdefault(2,4) = 15; cost
;unitdefault(2,5) = 0 ; can carry no unit


; Soldier
;unitdefault(3,0) = 3 ; attack
;unitdefault(3,1) = 2 ; defense
;unitdefault(3,2) = 1 ; movement
;unitdefault(3,3) = 1 ; hitpoints
;unitdefault(3,4) = 35; cost
;unitdefault(3,5) = 0 ; can carry no unit


; Spearman
;unitdefault(4,0) = 4 ; attack
;unitdefault(4,1) = 8 ; defense
;unitdefault(4,2) = 1 ; movement
;unitdefault(4,3) = 1 ; hitpoints
;unitdefault(4,4) = 40; cost
;unitdefault(4,5) = 0 ; can carry no unit


; Axeman
;unitdefault(5,0) = 6 ; attack
;unitdefault(5,1) = 3 ; defense
;unitdefault(5,2) = 1 ; movement
;unitdefault(5,3) = 1 ; hitpoints
;unitdefault(5,4) = 40; cost
;unitdefault(5,5) = 0 ; can carry no unit


;  Archer
;unitdefault(6,0) = 6 ; attack
;unitdefault(6,1) = 2 ; defense
;unitdefault(6,2) = 1 ; movement
;unitdefault(6,3) = 1 ; hitpoints
;unitdefault(6,4) = 55; cost
;unitdefault(6,5) = 0 ; can carry no unit


; Mounted Soldier
;unitdefault(7,0) = 6 ; attack
;unitdefault(7,1) = 3 ; defense
;unitdefault(7,2) = 2 ; movement
;unitdefault(7,3) = 2 ; hitpoints
;unitdefault(7,4) = 65; cost
;unitdefault(7,5) = 0 ; can carry no unit


; Knight
;unitdefault(8,0) = 11; attack
;unitdefault(8,1) = 7 ; defense
;unitdefault(8,2) = 3 ; movement
;unitdefault(8,3) = 3 ; hitpoints
;unitdefault(8,4) = 100; cost
;unitdefault(8,5) = 0 ; can carry no unit


; Ballista
;unitdefault(9,0) = 12; attack
;unitdefault(9,1) = 2 ; defense
;unitdefault(9,2) = 2 ; movement
;unitdefault(9,3) = 1 ; hitpoints
;unitdefault(9,4) = 80; cost
;unitdefault(9,5) = 0 ; can carry no unit

; Catapult
;unitdefault(10,0) = 16 ; attack
;unitdefault(10,1) = 2 ; defense
;unitdefault(10,2) = 1 ; movement
;unitdefault(10,3) = 1 ; hitpoints
;unitdefault(10,4) = 80; cost
;unitdefault(10,5) = 0 ; can carry no unit

; Siege Tower
;unitdefault(11,0) = 20; attack
;unitdefault(11,1) = 2 ; defense
;unitdefault(11,2) = 1 ; movement
;unitdefault(11,3) = 2 ; hitpoints
;unitdefault(11,4) = 110; cost
;unitdefault(11,5) = 0 ; can carry no unit

; Wolf
;unitdefault(12,0) = 2 ; attack
;unitdefault(12,1) = 2 ; defense
;unitdefault(12,2) = 2 ; movement
;unitdefault(12,3) = 1 ; hitpoints
;unitdefault(12,4) = 35; cost
;unitdefault(12,5) = 0 ; can carry no unit

; Bear
;unitdefault(13,0) = 4 ; attack
;unitdefault(13,1) = 3 ; defense
;unitdefault(13,2) = 1 ; movement
;unitdefault(13,3) = 2 ; hitpoints
;unitdefault(13,4) = 40; cost
;unitdefault(13,5) = 0 ; can carry no unit

; Cart
;unitdefault(14,0) = 1 ; attack
;unitdefault(14,1) = 1 ; defense
;unitdefault(14,2) = 1 ; movement
;unitdefault(14,3) = 1 ; hitpoints
;unitdefault(14,4) = 20; cost
;unitdefault(14,5) = 0 ; can carry no unit


; Small boat
;unitdefault(15,0) = 2 ; attack
;unitdefault(15,1) = 1 ; defense
;unitdefault(15,2) = 4 ; movement
;unitdefault(15,3) = 1 ; hitpoints
;unitdefault(15,4) = 20; cost
;unitdefault(15,5) = 1 ; can carry no unit

; Boat 1
;unitdefault(16,0) = 3 ; attack
;unitdefault(16,1) = 2 ; defense
;unitdefault(16,2) = 3 ; movement
;unitdefault(16,3) = 1 ; hitpoints
;unitdefault(16,4) = 40; cost
;unitdefault(16,5) = 2 ; can carry no units

; Boat 2
;unitdefault(17,0) = 3 ; attack
;unitdefault(17,1) = 2 ; defense
;unitdefault(17,2) = 2 ; movement
;unitdefault(17,3) = 1 ; hitpoints
;unitdefault(17,4) = 35; cost
;unitdefault(17,5) = 3 ; can carry no units



; player colors
playercolor(1,0) = 200
playercolor(1,1) = 0
playercolor(1,2) = 0

playercolor(2,0) = 0
playercolor(2,1) = 200
playercolor(2,2) = 0

playercolor(3,0) = 0
playercolor(3,1) = 0
playercolor(3,2) = 200

playercolor(4,0) = 200
playercolor(4,1) = 200
playercolor(4,2) = 0

playercolor(5,0) = 200
playercolor(5,1) = 0
playercolor(5,2) = 200

playercolor(6,0) = 0
playercolor(6,1) = 200
playercolor(6,2) = 200

playercolor(7,0) = 255
playercolor(7,1) = 255
playercolor(7,2) = 255

playercolor(8,0) = 0
playercolor(8,1) = 0
playercolor(8,2) = 0




; First active unit
currentunit(0) = 2
currentunit(1) = 2
currentunit(2) = 0


roadscheme(1) = 1
roadscheme(2) = 5
roadscheme(3) = 2
roadscheme(4) = 6
roadscheme(5) = 3
roadscheme(6) = 7
roadscheme(7) = 4
roadscheme(8) = 8


;
; Warstate contains the war/peace status between AI vs AI, AI vs Player Player vs Player
;
.warstates
warstate(1,1) = False 		; Player x vs Player 1
warstate(1,2) = True		; Player x vs Player 2
warstate(1,3) = True		; Player x vs Player 3
warstate(1,4) = False		; Player x vs Player 4
warstate(1,5) = False		; Player x vs Player 5
warstate(1,6) = False		; Player x vs Player 6
warstate(1,7) = False		; Player x vs Player 7
warstate(1,8) = False		; Player x vs Player 8
warstate(1,9) = False		; Player x vs Player 9
warstate(1,10) = False		; Player x vs Player 10

warstate(2,1) = True 		; Player x vs Player 1
warstate(2,2) = False		; Player x vs Player 2
warstate(2,3) = False		; Player x vs Player 3
warstate(2,4) = False		; Player x vs Player 4
warstate(2,5) = False		; Player x vs Player 5
warstate(2,6) = False		; Player x vs Player 6
warstate(2,7) = False		; Player x vs Player 7
warstate(2,8) = False		; Player x vs Player 8
warstate(2,9) = False		; Player x vs Player 9
warstate(2,10) = False		; Player x vs Player 10

warstate(3,1) = True 		; Player x vs Player 1
warstate(3,2) = False		; Player x vs Player 2
warstate(3,3) = False		; Player x vs Player 3
warstate(3,4) = False		; Player x vs Player 4
warstate(3,5) = False		; Player x vs Player 5
warstate(3,6) = False		; Player x vs Player 6
warstate(3,7) = False		; Player x vs Player 7
warstate(3,8) = False		; Player x vs Player 8
warstate(3,9) = False		; Player x vs Player 9
warstate(3,10) = False		; Player x vs Player 10

warstate(4,1) = False 		; Player x vs Player 1
warstate(4,2) = True		; Player x vs Player 2
warstate(4,3) = True		; Player x vs Player 3
warstate(4,4) = False		; Player x vs Player 4
warstate(4,5) = False		; Player x vs Player 5
warstate(4,6) = False		; Player x vs Player 6
warstate(4,7) = False		; Player x vs Player 7
warstate(4,8) = False		; Player x vs Player 8
warstate(4,9) = False		; Player x vs Player 9
warstate(4,10) = False		; Player x vs Player 10

warstate(5,1) = False 		; Player x vs Player 1
warstate(5,2) = True		; Player x vs Player 2
warstate(5,3) = True		; Player x vs Player 3
warstate(5,4) = False		; Player x vs Player 4
warstate(5,5) = False		; Player x vs Player 5
warstate(5,6) = False		; Player x vs Player 6
warstate(5,7) = False		; Player x vs Player 7
warstate(5,8) = False		; Player x vs Player 8
warstate(5,9) = False		; Player x vs Player 9
warstate(5,10) = False		; Player x vs Player 10

.labelTradestatus
tradestatus(1,1) = False
tradestatus(1,2) = False
tradestatus(1,3) = False
tradestatus(1,4) = False
tradestatus(1,5) = False
tradestatus(1,6) = False
tradestatus(1,7) = False
tradestatus(1,8) = False
;
tradestatus(2,1) = False
tradestatus(2,2) = False
tradestatus(2,3) = False
tradestatus(2,4) = False
tradestatus(2,5) = False
tradestatus(2,6) = False
tradestatus(2,7) = False
tradestatus(2,8) = False
;
tradestatus(3,1) = False
tradestatus(3,2) = False
tradestatus(3,3) = False
tradestatus(3,4) = False
tradestatus(3,5) = False
tradestatus(3,6) = False
tradestatus(3,7) = False
tradestatus(3,8) = False
;
tradestatus(4,1) = False
tradestatus(4,2) = False
tradestatus(4,3) = False
tradestatus(4,4) = False
tradestatus(4,5) = False
tradestatus(4,6) = False
tradestatus(4,7) = False
tradestatus(4,8) = False
;
tradestatus(5,1) = False
tradestatus(5,2) = False
tradestatus(5,3) = False
tradestatus(5,4) = False
tradestatus(5,5) = False
tradestatus(5,6) = False
tradestatus(5,7) = False
tradestatus(5,8) = False
;
tradestatus(6,1) = False
tradestatus(6,2) = False
tradestatus(6,3) = False
tradestatus(6,4) = False
tradestatus(6,5) = False
tradestatus(6,6) = False
tradestatus(6,7) = False
tradestatus(6,8) = False
;
tradestatus(7,1) = False
tradestatus(7,2) = False
tradestatus(7,3) = False
tradestatus(7,4) = False
tradestatus(7,5) = False
tradestatus(7,6) = False
tradestatus(7,7) = False
tradestatus(7,8) = False
;
tradestatus(8,1) = False
tradestatus(8,2) = False
tradestatus(8,3) = False
tradestatus(8,4) = False
tradestatus(8,5) = False
tradestatus(8,6) = False
tradestatus(8,7) = False
tradestatus(8,8) = False


.Setupunitsdata


;
; Loads the economy configuration
;
Function loadunitsconfig()
Local unitsfound = False
Local unitssetupfound = False
Local fileerror$ = ""
Local ubrfound = False
Local productionfound = False
Local endfound = False
file = OpenFile("units.txt")
If file = 0 Then simplemessage2("Cannot find units.txt") : End
While Eof(file) = False
	a$ = ReadLine(file)
	If Lower(Left(a$,Len("@units"))) = "@units" Then 
		unitsfound = True
		;
		While Eof(file) = False ; Find the next header
			a$ = ReadLine(file)
			;
			; ----------------------------------------
			;                Read in the goods from the config
			;
			;
			If Lower(Left(a$,Len("@Unit_setup"))) = "@unit_setup" Then
				unitssetupfound = True
				counter = 0
				While Eof(file) = False	; Reading units		
					a$ = ReadLine(file)
					;
					; Parse the goods
					;
					;
					If a$<> "" And (Not Left(a$,1)= " ") And (Not Left(a$,1) = ";") And (Not Left(a$,1) = "@") Then
						parseunitsconfig(counter,a$)
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
			If Lower(Left(a$,Len("@Unit_Build_Requirements"))) = "@unit_build_requirements" Then
				If unitssetupfound = True Then ubrfound = True
				counter = 0
				While Eof(file) = False	; Reading goods			
					a$ = ReadLine(file)
					; Parse the goods requirements					
					If a$<> "" And (Not Left(a$,1)= " ") And (Not Left(a$,1) = ";") And (Not Left(a$,1) = "@") Then
						parseunitrequirements(counter,a$)
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
						;parseproduction(counter,a$)
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
If fileerror$ <> "" Then simplemessage2(fileerror$) : End
If unitsfound = False Then simplemessage2("Header '@Units' not found") : End
If unitssetupfound = False Then simplemessage2("Units header '@Units_Setup' not found") : End
If ubrfound = False Then simplemessage2("Header '@units_build_requirements' not found") : End
;If productionfound = False Then simplemessage2("Header production '@production' not found") : End
If endfound = False Then simplemessage2("Header End '@End' not found") : End
End Function


Function parseunitrequirements(counter,in$)
num = parsestring(in$)

a = findunitsid(parse$(0))

If a = -1 Then simplemessage2("Unit graphic not found in units.txt in '@units_requirements : " + parse$(0)) : End

counter2 = 0
For i=1 To num

	b = returngoodheaderid(parse$(i))

	; Main requirement 'Gold'
	If Lower(parse$(i)) = "gold" Then 
		unitdefault(counter+1,default_cost) = parse$(i-1)
	End If
	
	; Other requirements from the goods list
	If b>-1 Then
		Select counter2
		Case 0
			unitdefault(counter+1,default_req1) = b
			unitdefault(counter+1,default_reqamount1) = parse$(i-1)
		Case 1
			unitdefault(counter+1,default_req2) = b
			unitdefault(counter+1,default_reqamount2) = parse$(i-1)
		Case 2
			unitdefault(counter+1,default_req3) = b
			unitdefault(counter+1,default_reqamount3) = parse$(i-1)
		Case 3
			unitdefault(counter+1,default_req4) = b
			unitdefault(counter+1,default_reqamount4) = parse$(i-1)
		End Select
	End If
	
Next

End Function



Function findunitsid(in$)
For i=0 To maxunitgraphs
If in$ = unitsgraphsnames$(i) Then Return i
Next
Return -1
End Function

;
; Loads the units information from the units.txt file
;
;
Function parseunitsconfig(counter,in$)
num = parsestring(in$) ; Get the amount of parsed configuration

unitsgraphsnames$(counter) = parse$(0)					; the graphic name

counter = counter + 1 ; units start at 1

If Lower(parse$(1)) = "land" Then a = 0
If Lower(parse$(1)) = "sea" Then a = 1
If Lower(parse$(1)) = "air" Then a = 2
unitdefault(counter,default_terraintype) = a			; the terrain type
unitsname$(counter) = parse$(2)							; the game name

a = parse$(3)
If Lower(parse$(3)) = "none" Then a = 0
unitdefault(counter,default_cancarry) = a				; x carried


If Lower(parse$(4)) = "yes" Then a = True Else a = False

unitdefault(counter,default_canbecarried) = a			; can be carried

unitdefault(counter,default_attack) = parse$(5)			; attack
unitdefault(counter,default_defense) = parse$(6)		; defense
unitdefault(counter,default_movement) = parse$(7)		; movement

unitdefault(counter,default_hitpoints) = parse$(8)		; hitpoints

End Function




.setupplayerrelations

For i=1 To 8
For ii=1 To 8
For iii=0 To 3
playerrelations(i,ii,iii) = 100
Next
Next
Next


; Here we set up a temporary road map

;For y=0 To 100
;For x=0 To 100
;roadmap(x,y) = 1
;Next
;Next

.LoadingSettings

Function loadcitynames()
Local error = True
file = OpenFile("cities.txt")
If file = 0 Then simplemessage2("Missing Cities list") : End

While Eof(file) = False
a$ = ReadLine(file)
If Lower(Left(a$,Len("@cities"))) = "@cities" Then
While Eof(file) = False 
a$ = ReadLine(file)
citiesnames$(counter) = Trim(a$)
counter = counter + 1
If Lower(Left(a$,Len("@end"))) = "@end" Then error = False : Exit
Wend
End If
Wend
numcitiesnames = counter
If error = True Then simplemessage2("Error in cities names file") : End
End Function

Function loadtribesnames()
Local config$ = ""
Local error = True
file = ReadFile("tribes.txt")
If file = 0 Then simplemessage2("Missing Tribes file") : End

While Eof(file) = False
	a$ = ReadLine$(file)
	
	If Lower(Left(a$,7)) = "@tribes" Then
		error = False
		While Eof(file) = False
			;
			a$ = ReadLine$(file)
			
			;
			If Lower(Left(a$,8)) = "@player1" Then
			a$ = ReadLine$(file)
			tribesnames$(1) = a$
			lasttribe = 1
			End If
			If Lower(Left(a$,8)) = "@player2" Then
			a$ = ReadLine$(file)
			tribesnames$(2) = a$
			lasttribe = 2
			End If
			If Lower(Left(a$,8)) = "@player3" Then
			a$ = ReadLine$(file)
			tribesnames$(3) = a$
			lasttribe = 3
			End If
			If Lower(Left(a$,8)) = "@player4" Then
			a$ = ReadLine$(file)
			tribesnames$(4) = a$
			lasttribe = 4
			End If
			If Lower(Left(a$,8)) = "@player5" Then
			a$ = ReadLine$(file)
			tribesnames$(5) = a$
			lasttribe = 5
			End If
			If Lower(Left(a$,8)) = "@player6" Then
			a$ = ReadLine$(file)
			tribesnames$(6) = a$
			lasttribe = 6
			End If
			If Lower(Left(a$,8)) = "@player7" Then
			a$ = ReadLine$(file)
			tribesnames$(7) = a$
			lasttribe = 7
			End If
			If Lower(Left(a$,8)) = "@player8" Then
			a$ = ReadLine$(file)
			tribesnames$(8) = a$
			lasttribe = 8
			End If
			; Read the tribes description
			If Lower(Left(a$,13)) = "@description"
				counter = 0
				Repeat
					a$ = ReadLine(file)
					If Lower(Left(a$,17)) = "@end description" Then Exit
					tribesdescriptions$(lasttribe,counter) = a$
					counter = counter + 1
					If Eof(file) = True Then simplemessage2("Error in Tribes config") : End
				Forever
			
			End If
			;
			If Lower(Left(a$,6)) = "@end  " Then error = False :Exit
		Wend
	End If
Wend

CloseFile file

If error = True Then simplemessage2("Error in tribes file") : End

End Function


;
; Load the keysettings
;
Function loadsettings()

set_graphicsdepth = readini$("settings.txt","setup","set_graphicsdepth","32")
If Not (set_graphicsdepth = 8 Or set_graphicsdepth = 16 Or set_graphicsdepth = 32) Then set_graphicsdepth = 16
set_intromessage = readini$("settings.txt","setup","set_intromessage","True")
set_introaboutmessage	= readini$("settings.txt","setup","set_intromessage","True")
set_tutorialrequest = readini$("settings.txt","setup","set_intromessage","True")
set_quitrequest = readini$("settings.txt","setup","set_quitrequest","True")
set_showtitlescreen = readini$("settings.txt","setup","set_showtitlescreen","True")

set_tooltipdelay = readini$("settings.txt","interface","set_tooltipdelay","2500")

set_sfxenabled = readini$("settings.txt","sound","set_sfxenabled","yes")
set_musicenabled = readini$("settings.txt","sound","set_musicenabled","yes")

set_mousescrolling = readini$("settings.txt","controls","set_mousescrolling","True") 
set_mousescrollingbutton = readini$("settings.txt","controls","set_mousescrollingbutton","True")
set_autoscrollingfullscreen = readini$("settings.txt","controls","set_autoscrollingfullscreen","True")
set_autoscrollingwindowed = readini$("settings.txt","controls","set_autoscrollingwindowed","True")
End Function

Function ReadINI$(filename$,category$,property$,defaultvalue$="")
	;Reads a value from an INI file
	;Category$	- the name specified in square brackets in the INI eg [MAIN]
	;			  [ ] not needed
	;Property$  - the name of the property to be read
	;defaultvalue$ is the value given if the property/category/filename does not exist	
	foundcategory=0
	f=ReadFile(filename$)
	If f>0
		While Not Eof(f)
			thisline$ = ReadLine(f)			
			If Left(thisline,1)="[" And Right(thisline,1)="]"
				foundcategory=(Upper(Trim(thisline$)) = "[" + Upper(category) + "]")
			End If
			If Lower(Trim(iniparsestring(thisline,"=",1)))=Lower(property) And foundcategory
				CloseFile f
				a$ = Lower(Trim(iniparsestring(thisline,"=",2)))
				If a$="true" Then Return True
				If a$ ="false" Then Return False				
				Return a$
			End If
		Wend
		CloseFile f		
		Return defaultvalue$
	Else	
		Return defaultvalue$
	End If
End Function

Function WriteINI(filename$,category$,property$,value$)
	;Updates an INI file with a category, property and value
	;Category$	- the name specified in square brackets in the INI eg [MAIN]
	;			  [ ] not needed
	;Property$  - the name of the property to be read
	;defaultvalue$ is the value given if the property/category/filename does not exist
	
	tempstr$="":thisline$=""
	foundcategory=0:foundvalue=0
	f=ReadFile(filename)
	If f=0
		tempstr=tempstr+"["+category+"]"+Chr(13)+Chr(10)
		tempstr=tempstr+Lower(property)+"="+value+Chr(13)+Chr(10)
	Else
		While Not Eof(f)
			thisline=ReadLine(f)
			If Left(thisline,1)="[" And Right(thisline,1)="]"
				If foundcategory And foundvalue=0
					tempstr=tempstr+Lower(property)+"="+value+Chr(13)+Chr(10)
					foundvalue=1
				Else
					foundcategory = ( Upper(Trim(thisline$)) = "[" + Upper(category$) + "]")
				End If
				tempstr=tempstr+Chr(13)+Chr(10)
			End If
			If Lower(Trim(iniparsestring(thisline,"=",1)))=Lower(property) And foundcategory
				tempstr=tempstr+Lower(property)+"="+value+Chr(13)+Chr(10)
				foundvalue=1
			Else
				If thisline<>"" tempstr=tempstr+thisline+Chr(13)+Chr(10)
			End If
		Wend
		CloseFile f
		If foundvalue=0
			tempstr=tempstr+Chr(13)+Chr(10)+"["+category+"]"+Chr(13)+Chr(10)
			tempstr=tempstr+Lower(property)+"="+value+Chr(13)+Chr(10)
		End If
	End If
	f=WriteFile(filename)
	If f>0
		For i=1 To Len(tempstr)
			WriteByte f,Asc(Mid(tempstr,i,1))
		Next
		CloseFile f
		Return 1
	Else
		Return 0
	End If
	
End Function


Function iniParseString$(inputstr$,separator$,index)
	;Returns the index'th string inside inputstr$, separated by separator$
	;eg ParseString$("Abba Boo Chuck D" , " " , 3) = "Chuck"
	
	i=1
	If index<1 Then Return ""
	inputstr$ = inputstr$ + separator$
	For j=2 To index
		i=Instr(inputstr,separator,i)+1
		If i=1 Then Return ""
	Next
	Return Mid(inputstr,i,Instr(inputstr,separator,i)-i)
End Function

Function RightOff$(inpstr$,i)
	;Knocks i amount of characters off the right of a string
	;RightOff$("Hello",1) = "Hell"
	Return Left(inpstr,Len(inpstr)-i)
End Function

Function LeftOff$(inpstr$,i)
	;Knocks i amount of characters off the left of a string
	;LeftOff$("Georgey",1) = "eorgey"
	Return Right(inpstr,Len(inpstr)-i)
End Function

Function LoseChars$(inpstr$,pos,num=1)
	;Subtracts a certain amount of characters from a place in a string
	;LoseChars$("Bogey",3,2) = "Boy"
	If pos<=0 Or num<=0 Then Return inpstr
	Return Left(inpstr,pos-1)+Right(inpstr,Len(inpstr)-pos-num+1)
End Function

Function AddChars$(inpstr$,pos,chars$)
	;Inserts a string inside another string at position "pos"
	;AddChars$("Bogey",4,"gl") = "Boggley"
	If pos<=0 Then Return inpstr
	Return Left(inpstr,pos-1)+chars+Right(inpstr,Len(inpstr)-pos+1)
End Function

