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

#define WORDS_PATH "linux.words"

#define IDENTICAL 0
#define SAMECHAR_DIFFCASES 1
#define DIFFCHAR_SAMECASES 2
#define DIFFCHAR_DIFFCASES 3
#define OTHER_CASE 4
#define GAP_PENALTY 3

using namespace std;

int main(int argc, char ** argv) {
	string word;
	fstream inputfile(WORDS_PATH, fstream::in);
	vector<string> * dictionary = new vector<string>;
	
	// Parse the command line argument
	if (argc != 2) {
		cout << "Wrong number of arguments" << endl;
		cout << "\t./spell-check word" << endl;
	} else {
		word = string(argv[1]);
	}
	
	// Read in the comparison file
	while (!inputfile.eof()) {
		string temp;
		inputfile >> temp;
		dictionary->push_back(temp);
	}
	
	int minIndex = INT_MAX;
	int minValue = INT_MAX;
	
	// Perform sequence alignment on all words in the dictionary
	for (int i = 0; i < dictionary->size(); i++) {
		int temp = sequenceAlignment(dictionary->at(i), word);
		
		if (temp < minValue) {
			minValue = temp;
			minIndex = i;
		}
	}
	
	cout << "Best match: " << dictionary->at(minIndex) << endl;
	cout << "Cost: " << minValue << endl;
	
	string dictionaryWord = dictionary->at(minIndex);
	int dictWordLength = dictionaryWord.length();
	int wordLength = word.length();
	
	int ** data = (int **) malloc((dictWordLength + 1) * sizeof(int *));
	
	for (int i = 0; i <= dictWordLength; i++) {
		data[i] = (int *) malloc((wordLength + 1) * sizeof(int));
		
		for (int j = 0; j <= wordLength; j++) {
			data[i][j] = 0;
		}
	}
	
	// Perform the sequence alignment for the best-match word, this
	// time giving it an array to put the subproblem values into
	sequenceAlignment(dictionary->at(minIndex), word, data);
	
	// Print out Alignment
	cout << "Alignment:" << endl;
	reconstructAlignment(dictionary->at(minIndex), word, data, dictWordLength, wordLength);
	
	// Print out Dynamic Programming Sub-problem Cost
	cout << "Dynamic Programming Sub-problem Cost:" << endl;
	cout << "        ";
	for (int i = 1; i <= wordLength; i++) {
		printf("%4d", i);
	}
	
	cout << "\n        ";
	for (int i = 0; i < wordLength; i++) {
		printf("%4c", word[i]);
	}
	cout << endl;
	
	for (int i = 1; i <= dictWordLength; i++) {
		printf("%4d%4c", i, dictionaryWord[i - 1]);

		for (int j = 1; j <= wordLength; j++) {
			printf("%4d", data[i][j]);
		}
		
		cout << endl;
	}
	
	cout << endl;
}

/**
 * Perform a sequence alignment on the two strings
 *
 * If data is not NULL, copy the M matrix into it
 *
 * @param x - the first string
 * @param y - the second string
 * @param data - the 2D array to copy M into
 * @return the cost of the best alignment of these two words
 */
int sequenceAlignment(string &x, string &y, int ** data) {
	int m = x.length();
	int n = y.length();
	
	int M[m + 1][n + 1];
	
	for (int i = 0; i <= m; i++) {
		M[i][0] = i * GAP_PENALTY;
	}
	
	for (int j = 0; j <= n; j++) {
		M[0][j] = j * GAP_PENALTY;
	}
	
	for (int i = 1; i <= m; i++) {
		for (int j = 1; j <= n; j++) {
			int penalty = getPenalty(x[i - 1], y[j - 1]);
			int case1 = penalty + M[i - 1][j - 1];
			int case2 = GAP_PENALTY + M[i - 1][j];
			int case3 = GAP_PENALTY + M[i][j - 1];
			
			int tempMin = MIN(case1, case2);
			int minPenalty = MIN(tempMin, case3);
			
			M[i][j] = minPenalty;
		}
	}
	
	if (data != NULL) {
		// If we have a valid pointer for data, copy
		// all of the values from M over to that array
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				data[i][j] = M[i][j];
			}
		}
	}

	return M[m][n];
}

/**
 * Gets the penalty based on the rules of this assignment
 *
 * @param x - the first character
 * @param y - the second character
 * @return the penalty of aligning these two characters together
 */
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

/**
 * Given the two strings, and their subproblem matrix, reconstruct the
 * actual alignment between the characters.
 *
 * @param x - the first string
 * @param y - the second string
 * @param M - the subproblem matrix computed by the sequence alignment algorithm
 * @param i - the current index into x
 * @param j - the current index into y
 */
void reconstructAlignment(string &x, string &y, int ** M, int i, int j) {
	if (i == 0) {
		for (int jj = 0; jj < j; jj++) {
			cout << "-_" << y[jj] << " (" << GAP_PENALTY << ")" << endl;
		}
	}
	if (j == 0) {
		for (int ii = 0; ii < i; ii++) {
			cout << x[ii] << "_-" << " (" << GAP_PENALTY << ")" << endl;
		}
	}
	
	if (i == 0 || j == 0) {
		return;
	}
	
	char c1 = x[i - 1];
	char c2 = y[j - 1];
	
	int penalty = getPenalty(c1, c2);
	
	if (M[i][j] == (penalty + M[i - 1][j - 1])) {
		reconstructAlignment(x, y, M, i - 1, j - 1);
		cout << c1 << "_" << c2 << " (" << penalty << ")" << endl;
	} else if (M[i][j] == (GAP_PENALTY + M[i - 1][j])) {
		reconstructAlignment(x, y, M, i - 1, j);
		cout << c1 << "_-" << " (" << GAP_PENALTY << ")" << endl;
	} else if (M[i][j] == (GAP_PENALTY + M[i][j - 1])) {
		reconstructAlignment(x, y, M, i, j - 1);
		cout << "-_" << c2 << " (" << GAP_PENALTY << ")" << endl;
	}
}