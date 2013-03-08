# Brian Reber (breber@iastate.edu)
# CprE 315X, Spring 2012
# Node/Edge File Parsing

all: prims kruskals

prims: src/prims.cpp nodes.o
	g++ -o prims src/prims.cpp nodes.o
	
kruskals: src/kruskals.cpp nodes.o unionfind.o
	g++ -o kruskals src/kruskals.cpp nodes.o unionfind.o
	
unionfind.o: src/unionfind.h src/unionfind.cpp
	g++ -c src/unionfind.cpp -o unionfind.o
	
nodes.o: src/nodes.cpp src/nodes.h
	g++ -c src/nodes.cpp -o nodes.o

clean:
	rm prims kruskals
	rm *.o
