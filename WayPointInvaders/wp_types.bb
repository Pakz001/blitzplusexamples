; Types
;

Type Tracer
 Field x#
 Field y#
 Field speed#
 Field direction# ; angle of movement
 Field LastWayPoint%
 Field distance#
 Field R%
 Field G%
 Field B%
End Type


Type TWayPoint
 Field X#
 Field Y#
 Field PathOrder%
End Type

Type explosionfield
	Field frame
	Field x
	Field y
	Field timer
	Field gx
	Field gy
End Type

Type laserfield
	Field x#
	Field y#
	Field tx#
	Field ty#
	Field rot
	Field ox#
	Field oy#
	Field timer
End Type

Type rtstarfield
	Field x#
	Field y#
	Field mx#
	Field my#
End Type

Type bigexplosion
	Field x#
	Field y#
	Field mx#
	Field my#
	Field tp
	Field frame
	Field timer
	Field waitawhile
End Type

Type galaxytype
	Field cursortimer
	Field cursortimerdelay
	Field cursorstate
	Field cursorwidth
	Field cursorheight
	;
	Field planetselectx
	Field planetselecty
	Field currentplanetx
	Field currentplanety
End Type
Type galaxyobjects
Field x
Field y
Field w
Field h
Field down
Field inhoud$
End Type
Type planettype
	Field name$
	Field id
	Field x
	Field y
	Field inrange
End Type
Type galaxymessage
	Field message$
	Field timeout
	Field scroll
End Type

Type galaxynodes
Field id
Field x1
Field y1
Field x2
Field y2
Field active
End Type