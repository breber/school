/** 
 * Brian Reber (breber@iastate.edu)
 * CprE 315X, Spring 2012
 * Spell Check Programming Assignment
 */
#include <iostream>
#include <fstream>
#include <vector>
#include "spell-check.h"

#define MIN(X,Y) ((X) < (Y) ? (X) : (Y))

#define IDENTICAL 0
#define SAMECHAR_DIFFCASES 1
#define DIFFCHAR_SAMECASES 2
#define DIFFCHAR_DIFFCASES 3
#define OTHER_CASE 4
#define GAP_PENALTY 3

using namespace std;

int main(int argc, char ** argv) {
	vector<string> * strings = new vector<string>;
	fstream inputfile("linux.words", fstream::in);
	string comparisonWord;
	string temp;
	int i;
	int minAlignment = INT_MAX;
	int minIndex = 0;
	
	// Get the comparison word from the command line
	if (argc < 2) {
		cout << "./spell-check word" << endl;
		exit(0);
	} else {
		comparisonWord = argv[1];
	}
	
	// Read in the comparison file
	while (!inputfile.eof()) {
		inputfile >> temp;
		strings->push_back(temp);
	}
	
	for (int i = 0; i < strings->size(); i++) {
		int alignment = sequenceAlignment(comparisonWord, strings->at(i));
		
		if (alignment < minAlignment) {
			minAlignment = alignment;
			minIndex = i;
		}
	}
	
	cout << "Best Match: " << strings->at(minIndex) << endl;
	cout << "Cost: " << minAlignment << endl;
}

int sequenceAlignment(string & x, string & y) {
	int M[x.length()][y.length()];
	
	for (int i = 0; i < x.length(); i++) {
		M[i][0] = i * GAP_PENALTY;
	}
	
	for (int i = 0; i < y.length(); i++) {
		M[0][i] = i * GAP_PENALTY;
	}
	
	for (int i = 1; i < x.length(); i++) {
		for (int j = 1; j < y.length(); j++) {
			int penalty = getPenalty(x[i], y[j]);
			int case1 = penalty + M[i - 1][j - 1];
			int case2 = GAP_PENALTY + M[i - 1][j];
			int case3 = GAP_PENALTY + M[i][j - 1];

			int min1 = MIN(case1, case2);
			int finalMin = MIN(min1, case3);
			
			M[i][j] = finalMin;
		}
	}
	
	return M[x.length() - 1][y.length() - 1];
}

int getPenalty(char x, char y) {
	int penalty = 0;
	
	if (x == y) {
		penalty = IDENTICAL;
	} else if (x >= 'A' && x <= 'z' && y >= 'A' && y <= 'z') {
		// Check the cases where we have two English letters
		if (abs(x - y) == 32) {
			penalty = SAMECHAR_DIFFCASES;
		} else if ((x >= 'a' && y >= 'a') || (x < 'a' && y < 'a')) {
			penalty = DIFFCHAR_SAMECASES;
		} else if ((x >= 'a' && y < 'a') || (x < 'a' && y >= 'a')) {
			penalty = DIFFCHAR_DIFFCASES;
		}
	} else {
		penalty = OTHER_CASE;
	}
	
	return penalty;
}