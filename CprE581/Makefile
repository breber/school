CC=sslittle-na-sstrix-gcc
#CC=gcc
OUT=out
ALL=impl1 impl2 impl3 impl4 impl5 impl6 impl7

dircreate:
	mkdir -p ${OUT}
	mkdir -p results

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

impl7: dircreate
	${CC} -o ${OUT}/implementation7 implementation7.c

run: ${ALL}
	sim-outorder ${OUT}/implementation1 2> results/impl1_res.txt
	sim-outorder ${OUT}/implementation2 2> results/impl2_res.txt
	sim-outorder ${OUT}/implementation3 2> results/impl3_res.txt
	sim-outorder ${OUT}/implementation4 2> results/impl4_res.txt
	sim-outorder ${OUT}/implementation5 2> results/impl5_res.txt
	sim-outorder ${OUT}/implementation6 2> results/impl6_res.txt
	sim-outorder ${OUT}/implementation7 2> results/impl7_res.txt

all: ${ALL}

convert:
	convert foo.ppm -compress none out.ppm
