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
	vector<person> * people = new vector<person>;
	int numGuys = -1;
	int numGals = -1;
	
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

	// Read in the comparison file
	while (!inputfile.eof()) {
		int guy;
		int gal;
		inputfile >> guy;
		inputfile >> gal;
		
		dictionary->push_back(temp);
	}

	cout << "Num Guys: " << numGuys << endl;
	cout << "Num Gals: " << numGals << endl;

	for (int i = 0; i < dictionary->size(); i++) {
		cout << dictionary->at(i) << endl;
	}

	cout << endl;
	
	return 0;
}

