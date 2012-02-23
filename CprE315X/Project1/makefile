# Brian Reber (breber@iastate.edu)
# CprE 315X, Spring 2012
# Node/Edge File Parsing

all: prims kruskals

prims: prims.cpp nodes.o
	g++ -o prims prims.cpp nodes.o
	
kruskals: kruskals.cpp nodes.o unionfind.o
	g++ -o kruskals kruskals.cpp nodes.o unionfind.o
	
unionfind.o: unionfind.h unionfind.cpp
	g++ -c unionfind.cpp -o unionfind.o
	
nodes.o: nodes.cpp nodes.h
	g++ -c nodes.cpp -o nodes.o

clean:
	rm prims kruskals
	rm *.o
