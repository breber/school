/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Match Programming Assignment
 */
#include <iostream>
#include <fstream>
#include <vector>
#include "match.h"

using namespace std;

int main(int argc, char ** argv) {
	fstream inputfile;
	
	edge ** edges = NULL;
	int numGuys = 0;
	int numGals = 0;
	
	// Parse the command line argument
	if (argc != 2) {
		cout << "Wrong number of arguments" << endl;
		cout << "\t./match inputfile" << endl;
		
		return 0;
	} else {
		inputfile.open(argv[1], fstream::in);
	}

	
	// Get the number of guys and gals from the first two lines
	inputfile >> numGuys;
	inputfile >> numGals;

	edges = (edge **) new edge[numGuys];
	memset(edges, numGuys * sizeof(int *), 0);
	
	for (int i = 0; i < numGuys; i++) {
		edges[i] = new edge[numGals];
		memset(edges[i], numGals * sizeof(int *), 0);
	}

	// Read in the comparison file
	while (!inputfile.eof()) {
		int guy;
		int gal;
		inputfile >> guy;
		inputfile >> gal;
		
		edges[guy - 1][gal - 1].forward = INT_MAX;
		edges[guy - 1][gal - 1].reverse = 0;
	}

	cout << "Num Guys: " << numGuys << endl;
	cout << "Num Gals: " << numGals << endl;

	for (int i = 0; i < numGuys; i++) {
		for (int j = 0; j < numGals; j++) {
			printf("{%10d,%10d}", edges[i][j].forward, edges[i][j].reverse);
		}
		
		cout << endl;
	}
	
	
	return 0;
}

