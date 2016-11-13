all:
	sslittle-na-sstrix-gcc -o main main.c

pc:
	gcc -o main-pc main.c

run-pc: pc
	./main-pc

run: main
	sim-outorder main

convert:
	convert foo.ppm -compress none bar.ppm
