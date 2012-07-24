CPP=g++

FLAGS=-Wall

BRAINS=bin/pilladdict bin/sentry bin/tribot
BRAINS_H=pilladdict.h sentry.h tribot.h
BRAINS_O=bin/pilladdict.o bin/sentry.o bin/tribot.o

PROPS=bin/property.o bin/lava.o bin/water.o bin/mud.o bin/hole.o bin/fog.o bin/jammer.o
PROPS_H=property.h lava.h water.h mud.h hole.h fog.h jammer.h

OBJECTS=bin/object.o bin/robot.o bin/earthrock.o bin/romulanrock.o bin/ball.o bin/block.o bin/energypill.o
OBJECTS_H=object.h robot.h earthrock.h romulanrock.h ball.h block.h energypill.h

PROPS_OBJS=bin/location.o bin/energy.o ${PROPS} ${OBJECTS}
PROPS_OBJS_H=location.h energy.h ${PROPS_H} ${OBJECTS_H}

EXES=bin/checkconf bin/generateconf bin/simulate ${BRAINS}

all: ${EXES} copy

#UTILITIES

bin/stringutils.o: stringutils.cpp stringutils.h simulation.h ${PROPS_OBJS_H}
	${CPP} -o $@ -c $< ${FLAGS}

bin/location.o: location.cpp location.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/energy.o: energy.cpp energy.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/simulation.o: simulation.cpp simulation.h object.h property.h robot.h
	${CPP} -o $@ -c $< ${FLAGS}


#EXES

bin/checkconf: bin/checkconf.o bin/stringutils.o bin/simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

bin/checkconf.o: checkconf.cpp checkconf.h stringutils.h ${PROPS_OBJS_H}
	${CPP} -o $@ -c $< ${FLAGS}

bin/generateconf: bin/generateconf.o bin/stringutils.o bin/simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

bin/generateconf.o: generateconf.cpp generateconf.h stringutils.h simulation.h ${PROPS_OBJS_H}
	${CPP} -o $@ -c $< ${FLAGS}

bin/simulate: bin/simulate.o bin/stringutils.o bin/simulation.o bin/brain.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

bin/simulate.o: simulate.cpp simulate.h stringutils.h simulation.h ${PROPS_OBJS_H}
	${CPP} -o $@ -c $< ${FLAGS}


#OBJECTS

bin/ball.o: ball.cpp ball.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/block.o: block.cpp block.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/earthrock.o: earthrock.cpp earthrock.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/energypill.o: energypill.cpp energypill.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/object.o: object.cpp object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/robot.o: robot.cpp robot.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/romulanrock.o: romulanrock.cpp romulanrock.h object.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}


#PROPERTIES

bin/fog.o: fog.cpp fog.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/hole.o: hole.cpp hole.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/jammer.o: jammer.cpp jammer.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/lava.o: lava.cpp lava.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/mud.o: mud.cpp mud.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/property.o: property.cpp property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/water.o: water.cpp water.h property.h location.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}


#BRAINS

bin/brain.o: brain.cpp brain.h stringutils.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/pilladdict: bin/pilladdict.o bin/brain.o bin/stringutils.o bin/simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

bin/pilladdict.o: pilladdict.cpp pilladdict.h brain.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/sentry: bin/sentry.o bin/brain.o bin/brain.o bin/stringutils.o bin/simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

bin/sentry.o: sentry.cpp sentry.h brain.h
	${CPP} -o $@ -c $< ${FLAGS}

bin/tribot: bin/tribot.o bin/brain.o bin/brain.o bin/stringutils.o bin/simulation.o ${PROPS_OBJS}
	${CPP} -o $@ $^ ${FLAGS}

bin/tribot.o: tribot.cpp tribot.h brain.h
	${CPP} -o $@ -c $< ${FLAGS}


#COPY AND CLEAN
copy:
	cp *.cpp *.h ./FullProject

clean:
	rm -f bin/*.o

cleandist:
	rm -f ${EXES}
	rm -f bin/*.o
