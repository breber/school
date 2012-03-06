#
# Brian Reber
# CprE 308 Project 2
# Lab Section D
# makefile
#

all: appserver appserver-coarse

bank.o: Bank.c Bank.h
	gcc -o bank.o -c Bank.c
	
appserver: appserver.c bank.o
	gcc -o appserver appserver.c bank.o -lpthread

appserver-coarse: appserver-coarse.c bank.o
	gcc -o appserver-coarse appserver-coarse.c bank.o -lpthread

clean:
	rm *.o appserver appserver-coarse
