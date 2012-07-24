#put the compiler names in a variable for easy switching
CC=gcc
CXX=g++
#Save the flags in groups so you can easily make optimized builds
#Basic flags used by all builds
BASE=-ansi -pedantic -Wall
#Debug specific flags
DEBUG=-O0 -g -ftest-coverage -fprofile-arcs
#Opitmized release flags
OPTIMIZE=-O2
#Default is Debug build (only need to change this line to make release)
CFLAGS=${BASE} ${DEBUG}
#These are C++ specific flags
CXX_FLAGS=${CFLAGS}
#JNI compile options
JNI_INCLUDE=
JNI=-fPIC -I${JNI_INCLUDE} -I${JNI_INCLUDE}/linux/

libHelloWorld.so : HelloWorld.o
	${CC} -shared ${CFLAGS} ${JNI} -o $@ $<

HelloWorld.o : HelloWorld.c HelloWorld.h
	${CC} -c ${CFLAGS} ${JNI} -o $@ $<

clean :
	rm -f ./*.so
	rm -f ./*.o
