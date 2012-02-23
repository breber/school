all: lab1.o message.o; gcc -o lab1 lab1.o message.o
	
lab1: lab1.c message.h message.c
	gcc -c lab1.c	

message.o: message.c message.h
	gcc -c message.c

clean:
	rm *.o lab1
