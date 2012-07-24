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
CFLAGS=${BASE} 
#${DEBUG}
#These are C++ specific flags
CXX_FLAGS=${CFLAGS}

all: main.o operations.o evaluate.o
	${CC} -o output main.o operations.o evaluate.o ${CFLAGS}
main.o: main.c
	${CC} -c main.c ${CFLAGS}
operations.o: operations.c
	${CC} -c operations.c ${CFLAGS}
evaluate.o: evaluate.c
	${CC} -c evaluate.c ${CFLAGS}
clean:
	rm main.o operations.o evaluate.o
distclean:
	rm main.o operations.o evaluate.o all
