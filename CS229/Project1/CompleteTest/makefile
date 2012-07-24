CC=gcc
BASE=-ansi -pedantic -Wall
CFLAGS=${BASE} 

JNI_INCLUDE=/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers
JNI=-fPIC -I${JNI_INCLUDE}

all: bin/imagestats bin/makesquare bin/cs2ppm bin/ppm2cs bin/darken bin/lighten bin/rotate bin/flip bin/stitch bin/convolve bin/MatrixMultiplier.class bin/DisplayRunner.class

bin/imagestats: bin/imagestats.o bin/imageutil.o
	${CC} -o bin/imagestats bin/imagestats.o bin/imageutil.o ${CFLAGS}

bin/imagestats.o: imagestats.c imagestats.h
	${CC} -o bin/imagestats.o -c imagestats.c ${CFLAGS}

bin/makesquare: bin/makesquare.o bin/imageutil.o
	${CC} -o bin/makesquare bin/makesquare.o bin/imageutil.o ${CFLAGS}

bin/makesquare.o: makesquare.c makesquare.h
	${CC} -o bin/makesquare.o -c makesquare.c ${CFLAGS}
	
bin/cs2ppm: bin/cs2ppm.o bin/imageutil.o
	${CC} -o bin/cs2ppm bin/cs2ppm.o bin/imageutil.o ${CFLAGS}

bin/cs2ppm.o: cs2ppm.c
	${CC} -o bin/cs2ppm.o -c cs2ppm.c ${CFLAGS}
	
bin/ppm2cs: bin/ppm2cs.o bin/imageutil.o
	${CC} -o bin/ppm2cs bin/ppm2cs.o bin/imageutil.o ${CFLAGS}

bin/ppm2cs.o: ppm2cs.c ppm2cs.h
	${CC} -o bin/ppm2cs.o -c ppm2cs.c ${CFLAGS}
	
bin/imageutil.o: imageutil.c imageutil.h
	${CC} -o bin/imageutil.o -c imageutil.c ${CFLAGS}
	
bin/darken: bin/darken.o bin/imageutil.o
	${CC} -o bin/darken bin/darken.o bin/imageutil.o ${CFLAGS}

bin/darken.o: darken.c darken.h
	${CC} -o bin/darken.o -c darken.c ${CFLAGS}
	
bin/lighten: bin/lighten.o bin/imageutil.o
	${CC} -o bin/lighten bin/lighten.o bin/imageutil.o ${CFLAGS}

bin/lighten.o: lighten.c lighten.h
	${CC} -o bin/lighten.o -c lighten.c ${CFLAGS}
	
bin/rotate: bin/rotate.o bin/imageutil.o
	${CC} -o bin/rotate bin/rotate.o bin/imageutil.o ${CFLAGS}

bin/rotate.o: rotate.c rotate.h
	${CC} -o bin/rotate.o -c rotate.c ${CFLAGS}
	
bin/flip: bin/flip.o bin/imageutil.o
	${CC} -o bin/flip bin/flip.o bin/imageutil.o ${CFLAGS}

bin/flip.o: flip.c flip.h
	${CC} -o bin/flip.o -c flip.c ${CFLAGS}
	
bin/stitch: bin/stitch.o bin/imageutil.o
	${CC} -o bin/stitch bin/stitch.o bin/imageutil.o ${CFLAGS}

bin/stitch.o: stitch.c stitch.h
	${CC} -o bin/stitch.o -c stitch.c ${CFLAGS}
	
bin/convolve: bin/convolve.o bin/imageutil.o
	${CC} -o bin/convolve bin/convolve.o bin/imageutil.o ${CFLAGS}

bin/convolve.o: convolve.c convolve.h
	${CC} -o bin/convolve.o -c convolve.c ${CFLAGS}
	
bin/libmatrix.jnilib: bin/matrix.o
	${CC} -o bin/libmatrix.jnilib -shared ${CFLAGS} ${JNI} bin/matrix.o

bin/matrix.o: matrix.c MatrixMultiplier.h
	${CC} -o bin/matrix.o -c ${CFLAGS} ${JNI} matrix.c

bin/MatrixMultiplier.class: MatrixMultiplier.java bin/libmatrix.jnilib
	javac MatrixMultiplier.java
	mv *.class bin
	
bin/libconvolvejni.jnilib: bin/convolvejni.o bin/convolve.o bin/imageutil.o
	gcc -o bin/libconvolvejni.jnilib -shared ${CFLAGS} ${JNI} bin/convolvejni.o bin/convolve.o bin/imageutil.o

bin/convolvejni.o: convolvejni.h convolvejni.c
	${CC} -o bin/convolvejni.o -c ${CFLAGS} ${JNI} convolvejni.c

bin/DisplayRunner.class: DisplayRunner.java bin/CS229Image.class bin/libconvolvejni.jnilib
	javac DisplayRunner.java
	mv *.class bin

bin/CS229Image.class: CS229Image.java
	javac CS229Image.java
	mv *.class bin

clean:
	rm -f bin/imagestats bin/makesquare bin/cs2ppm bin/ppm2cs bin/darken bin/lighten bin/rotate bin/flip bin/stitch bin/convolve
	rm -f bin/*.o
	rm -f bin/*.jnilib
	rm -f bin/*.class
