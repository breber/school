CPP=g++

FLAGS=-Wall

BRAINS=pilladdict sentry tribot
BRAINS_H=pilladdict.h sentry.h tribot.h
BRAINS_O=pilladdict.o sentry.o tribot.o

PROPS=property.o lava.o water.o mud.o hole.o fog.o jammer.o
PROPS_H=property.h lava.h water.h mud.h hole.h fog.h jammer.h

OBJECTS=object.o robot.o earthrock.o romulanrock.o ball.o block.o energypill.o
OBJECTS_H=object.h robot.h earthrock.h romulanrock.h ball.h block.h energypill.h

PROPS_OBJS=location.o energy.o ${PROPS} ${OBJECTS}
PROPS_OBJS_H=location.h energy.h ${PROPS_H} ${OBJECTS_H}

EXES=checkconf generateconf simulate ${BRAINS}

all: ${EXES}

#UTILITIES

stringutils.o: stringutils.cpp stringutils.h simulation.h ${PROPS_OBJS_H}
	${CPP} -o $@ -c $< ${FLAGS}

location.o: location.cpp location.h
	${CPP} -o $@ -c $< ${FLAGS}

energy.o: energy.cpp energy.h
	${CPP} -o $@ -c $< ${FLAGS}

simulation.o: simulation.cpp simulation.h object.h property.h robot.h
	${CPP} -o $@ -c $< ${FLAGS}


#EXES

checkconf: checkconf.o stringutils.o simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

checkconf.o: checkconf.cpp checkconf.h stringutils.h ${PROPS_OBJS_H}
	${CPP} -o $@ -c $< ${FLAGS}

generateconf: generateconf.o stringutils.o simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

generateconf.o: generateconf.cpp generateconf.h stringutils.h simulation.h ${PROPS_OBJS_H}
	${CPP} -o $@ -c $< ${FLAGS}

simulate: simulate.o stringutils.o simulation.o brain.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

simulate.o: simulate.cpp simulate.h stringutils.h simulation.h ${PROPS_OBJS_H}
	${CPP} -o $@ -c $< ${FLAGS}


#OBJECTS

ball.o: ball.cpp ball.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

block.o: block.cpp block.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

earthrock.o: earthrock.cpp earthrock.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

energypill.o: energypill.cpp energypill.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

object.o: object.cpp object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

robot.o: robot.cpp robot.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

romulanrock.o: romulanrock.cpp romulanrock.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}


#PROPERTIES

fog.o: fog.cpp fog.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

hole.o: hole.cpp hole.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

jammer.o: jammer.cpp jammer.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

lava.o: lava.cpp lava.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

mud.o: mud.cpp mud.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

property.o: property.cpp property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

water.o: water.cpp water.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}


#BRAINS

brain.o: brain.cpp brain.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

pilladdict: pilladdict.o brain.o stringutils.o simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

pilladdict.o: pilladdict.cpp pilladdict.h brain.h
	${CPP} -o $@ -c $< ${FLAGS}

sentry: sentry.o brain.o brain.o stringutils.o simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

sentry.o: sentry.cpp sentry.h brain.h
	${CPP} -o $@ -c $< ${FLAGS}

tribot: tribot.o brain.o brain.o stringutils.o simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

tribot.o: tribot.cpp tribot.h brain.h
	${CPP} -o $@ -c $< ${FLAGS}


#CLEAN

clean:
	rm -f *.o

cleandist:
	rm -f ${EXES}
	rm -f *.o
