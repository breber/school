CC=gcc
BASE=-ansi -pedantic -Wall
OPTIMIZE=-O2
CFLAGS=${BASE}

JNI_INCLUDE=/usr/java/jdk1.6.0_17/include
JNI=-fPIC -I${JNI_INCLUDE} -I${JNI_INCLUDE}/linux/

all: imagestats makesquare cs2ppm ppm2cs darken lighten rotate flip stitch convolve MatrixMultiplier.class DisplayRunner.class

imagestats: imagestats.o imageutil.o
	${CC} -o imagestats imagestats.o imageutil.o ${CFLAGS}

imagestats.o: imagestats.c imagestats.h
	${CC} -c imagestats.c ${CFLAGS}

makesquare: makesquare.o imageutil.o
	${CC} -o makesquare makesquare.o imageutil.o ${CFLAGS}

makesquare.o: makesquare.c makesquare.h
	${CC} -c makesquare.c ${CFLAGS}
	
cs2ppm: cs2ppm.o imageutil.o
	${CC} -o cs2ppm cs2ppm.o imageutil.o ${CFLAGS}

cs2ppm.o: cs2ppm.c
	${CC} -c cs2ppm.c ${CFLAGS}
	
ppm2cs: ppm2cs.o imageutil.o
	${CC} -o ppm2cs ppm2cs.o imageutil.o ${CFLAGS}

ppm2cs.o: ppm2cs.c ppm2cs.h
	${CC} -c ppm2cs.c ${CFLAGS}

imageutil.o: imageutil.c imageutil.h
	${CC} -fPIC -o imageutil.o -c imageutil.c ${CFLAGS}

darken: darken.o imageutil.o
	${CC} -o darken darken.o imageutil.o ${CFLAGS}

darken.o: darken.c darken.h
	${CC} -o darken.o -c darken.c ${CFLAGS}
	
lighten: lighten.o imageutil.o
	${CC} -o lighten lighten.o imageutil.o ${CFLAGS}

lighten.o: lighten.c lighten.h
	${CC} -o lighten.o -c lighten.c ${CFLAGS}

rotate: rotate.o imageutil.o
	${CC} -o rotate rotate.o imageutil.o ${CFLAGS}

rotate.o: rotate.c rotate.h
	${CC} -o rotate.o -c rotate.c ${CFLAGS}
	
flip: flip.o imageutil.o
	${CC} -o flip flip.o imageutil.o ${CFLAGS}

flip.o: flip.c flip.h
	${CC} -o flip.o -c flip.c ${CFLAGS}
	
stitch: stitch.o imageutil.o
	${CC} -o stitch stitch.o imageutil.o ${CFLAGS}

stitch.o: stitch.c stitch.h
	${CC} -o stitch.o -c stitch.c ${CFLAGS}
	
convolve: convolve.o imageutil.o
	${CC} -o convolve convolve.o imageutil.o ${CFLAGS}

convolve.o: convolve.c convolve.h
	${CC} -fPIC -o convolve.o -c convolve.c ${CFLAGS}

libmatrix.so: matrix.o
	${CC} -o libmatrix.so -shared ${CFLAGS} ${JNI} matrix.o

matrix.o: matrix.c MatrixMultiplier.h
	${CC} -o matrix.o -c ${CFLAGS} ${JNI} matrix.c

MatrixMultiplier.class: MatrixMultiplier.java libmatrix.so
	javac MatrixMultiplier.java
	
libconvolvejni.so: convolvejni.o convolve.o imageutil.o
	gcc -o libconvolvejni.so -shared ${CFLAGS} ${JNI} convolvejni.o convolve.o imageutil.o

convolvejni.o: convolvejni.h convolvejni.c
	${CC} -o convolvejni.o -c ${CFLAGS} ${JNI} convolvejni.c

CS229Image.class: CS229Image.java
	javac CS229Image.java
	
DisplayRunner.class: DisplayRunner.java CS229Image.class libconvolvejni.so
	javac DisplayRunner.java

clean:
	rm -f ./*.o

cleandist:
	rm -f imagestats makesquare cs2ppm ppm2cs darken lighten rotate flip stitch convolve
	rm -f ./*.o
	rm -f ./*.so
	rm -f ./*.class
