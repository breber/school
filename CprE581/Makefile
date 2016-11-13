all:
	sslittle-na-sstrix-gcc -o main main.c

pc:
	gcc -o main-pc main.c

run-pc: pc
	sim-outorder main-pc 25

run: main
	sim-outorder main 25