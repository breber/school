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
		int alignment = sequenceAlignment(strings->at(i), comparisonWord);
		
		if (alignment < minAlignment) {
			minAlignment = alignment;
			minIndex = i;
		}
	}
	
	// RECONSTRUCTION
	int otherLength = strings->at(minIndex).length();
	string otherString = strings->at(minIndex);
	
	int ** data = (int **) malloc((otherLength + 1) * sizeof(int *));
	int ** penalties = (int **) malloc((otherLength + 1) * sizeof(int *));
	for (int i = 0; i <= otherLength; i++) {
		data[i] = (int *) malloc((comparisonWord.length() + 1) * sizeof(int *));
		penalties[i] = (int *) malloc((comparisonWord.length() + 1) * sizeof(int *));
	}
	sequenceAlignment(strings->at(minIndex), comparisonWord, true, data, penalties);
	
	cout << "Best Match: " << strings->at(minIndex) << endl;
	cout << "Cost: " << minAlignment << endl;
	
	for (int i = 0; i < otherLength; i++) {
		for (int j = 0; j < comparisonWord.length(); j++) {
			printf("%4d", penalties[i][j]);
		}
		
		cout << endl;
	}
	
	// Print out Alignment
	cout << "Alignment:" << endl;
	reconstructAlignment(strings->at(minIndex), comparisonWord, data, penalties, 
							otherLength - 1, comparisonWord.length() - 1);

	// Print out Dynamic Programming Sub-problem Cost
	cout << "Dynamic Programming Sub-problem Cost:" << endl;
	cout << "        ";
	for (int i = 0; i < comparisonWord.length(); i++) {
		printf("%4d", (i + 1));
	}
	
	cout << "\n        ";
	for (int i = 0; i < comparisonWord.length(); i++) {
		printf("%4c", comparisonWord[i]);
	}
	cout << endl;
	
	for (int i = 1; i <= otherLength; i++) {
		printf("%4d%4c", i, otherString[i - 1]);

		for (int j = 1; j <= comparisonWord.length(); j++) {
			printf("%4d", data[i][j]);
		}
		
		cout << endl;
	}
	
	cout << endl;
}

int sequenceAlignment(string & x, string & y, bool copyVals, int ** data, int ** penalties) {
	int M[x.length() + 1][y.length() + 1];
	
	for (int i = 0; i <= x.length(); i++) {
		M[i][0] = i * GAP_PENALTY;

		if (copyVals) {
			data[i][0] = M[i][0];
			penalties[i][0] = 0;//GAP_PENALTY;
		}
	}
	
	for (int i = 0; i <= y.length(); i++) {
		M[0][i] = i * GAP_PENALTY;
		
		if (copyVals) {
			data[0][i] = M[0][i];
			penalties[0][i] = 0;//GAP_PENALTY;
		}
	}
	
	for (int i = 1; i <= x.length(); i++) {
		for (int j = 1; j <= y.length(); j++) {
			int penalty = getPenalty(x[i - 1], y[j - 1]);
			int case1 = penalty + M[i - 1][j - 1];
			int case2 = GAP_PENALTY + M[i - 1][j];
			int case3 = GAP_PENALTY + M[i][j - 1];

			int min1 = MIN(case1, case2);
			int finalMin = MIN(min1, case3);

			M[i][j] = finalMin;
			
			if (copyVals) {
				data[i][j] = finalMin;
				penalties[i][j] = penalty;
			}
		}
	}

	return M[x.length()][y.length()];
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

void reconstructAlignment(string & x, string & y, int ** M, int ** penalties, int i, int j) {
	if (i == 0) {
		for (int jj = 0; jj < j; jj++) {
			cout << "-_" << y[jj] << " (" << GAP_PENALTY << ")-" << i << j << endl;
		}
	}
	if (j == 0) {
		for (int ii = 0; ii < i; ii++) {
			cout << x[ii] << "_-" << " (" << GAP_PENALTY << ")--" << i << j << endl;
		}
	}
	
	if ((i == 0 || j == 0)) {		
		cout << x[i] << "_" << y[j] << " (" << penalties[i][j] << ")#" << i << j << endl;
		return;
	}
	/*if ((i == 0 || j == 0) && (M[i][j] == penalties[i][j])) {		
		cout << x[i] << "_" << y[j] << " (" << penalties[i][j] << ")#" << i << j << endl;
		return;
	}*/
	
	if (M[i][j] == (penalties[i][j] + M[i - 1][j - 1])) {
		reconstructAlignment(x, y, M, penalties, i - 1, j - 1);
		cout << x[i] << "_" << y[j] << " (" << penalties[i][j] << ")#" << i << j << endl;
	} else if (M[i][j] == (GAP_PENALTY + M[i - 1][j])) {
		reconstructAlignment(x, y, M, penalties, i - 1, j);
		cout << x[i] << "_-" << " (" << GAP_PENALTY << ")##" << i << j << endl;
	} else if (M[i][j] == (GAP_PENALTY + M[i][j - 1])) {
		reconstructAlignment(x, y, M, penalties, i, j - 1);
		cout << "-_" << y[j] << " (" << GAP_PENALTY << ")###" << i << j << endl;
	}
}
