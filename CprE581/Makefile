#CC=sslittle-na-sstrix-gcc
CC=gcc
OUT=out
ALL=impl1 impl2 impl3 impl4 impl5 impl6

dircreate:
	mkdir -p ${OUT}

impl1: dircreate
	${CC} -o ${OUT}/implementation1 implementation1.c

impl2: dircreate
	${CC} -o ${OUT}/implementation2 implementation2.c

impl3: dircreate
	${CC} -o ${OUT}/implementation3 implementation3.c

impl4: dircreate
	${CC} -o ${OUT}/implementation4 implementation4.c

impl5: dircreate
	${CC} -o ${OUT}/implementation5 implementation5.c

impl6: dircreate
	${CC} -o ${OUT}/implementation6 implementation6.c

run: ${ALL}
	sim-outorder ${OUT}/implementation1
	sim-outorder ${OUT}/implementation2
	sim-outorder ${OUT}/implementation3
	sim-outorder ${OUT}/implementation4
	sim-outorder ${OUT}/implementation5
	sim-outorder ${OUT}/implementation6

all: ${ALL}

convert:
	convert foo.ppm -compress none out.ppm
