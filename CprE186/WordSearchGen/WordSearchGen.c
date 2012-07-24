/*
 *  WordSearchGen.c
 *  
 *
 *  Programmer: Brian Reber & Steve Leblanc  Date: 1/20/10
 *  Instructor: Daniels      Class: CprE186
 *
 *  Generates a random word search from words in a file
 */

#include <stdio.h>
#include <string.h>

#define NUMWORDS 20
#define HORIZ 0
#define VERT 1
#define TRUE 1
#define FALSE 0

int main(int argc, char* argv[])
{
	FILE* input;
	char filename[20];
	
	// Get the filename from the command line
	if (argc>1)
	{
		sscanf(argv[1], "%s", filename);
		printf("You entered a filename of %s\n", filename);
	}
	else {
		printf("Enter a file name on the command line\n");
		return(-1);
	}

	// Default words - in case there are too few words in the user's file
	char  words[NUMWORDS][15] = {"brian", "steve", "computer", "paperclip", "mangos","starwars","banana",
								"lincoln","tea","wiimote","calculus","clock","food","drawer","friley",
								"iowa","state","university","ames","school"};
	// Lengths of the words - matches our default list.  Will be overriden when it reads in from a file
	int wordLengths[NUMWORDS] = {5,5,8,9,6,8,6,7,3,7,8,5,4,6,6,4,5,10,4,6};
	int longestWord = 0;
	int i,j;
	int dimension = 35;
	int randomX,randomY, randomDir;
	
	// Get words from file
	input = fopen(filename, "r");
	for(i=0; i<NUMWORDS && !feof(input); i++)
		fscanf(input, "%s", words[i]);
	
	// Go through and get lengths of words
	for (i=0; i<NUMWORDS; i++) {
		wordLengths[i] = strlen(words[i]);
		if (wordLengths[i]>longestWord) {
			longestWord = wordLengths[i];
		}
	}
	
	// We set the dimensions to the longest word, if it is bigger than 15
	if (longestWord > 15) {
		dimension = longestWord + 20;
	}
	
	char grid[dimension][dimension];
	
	// Initializes our grid
	for (i=0; i<dimension; i++) {
		for (j=0; j<dimension; j++) {
			grid[i][j] = '!';
		}
	}
	
	for (i = 0; i< NUMWORDS; i++) {
		int canFit = TRUE;
		int spaceFree = TRUE;
		
		// Generate random x and y coordinate
		srand(time(0));	
		randomX = rand() % dimension;
		randomY = rand() % dimension;
		randomDir = rand() % 2;
		
		// Check to see if it goes outside puzzle
		if (randomDir == HORIZ)
		{
			if (wordLengths[i] > (dimension - randomX)) {
				canFit = FALSE;
			}
			else {
				canFit = TRUE;
			}
		}
		else {
			if (wordLengths[i] > (dimension - randomY)){
				canFit = FALSE;
			}
			else {
				canFit = TRUE;
			}
		}
		
		// Check to see if there is a word in the way
		for (j = 0; j < wordLengths[i]; j++)
		{
			if (randomDir == HORIZ) {
				if ((grid[randomY][randomX+j] != '!') && (grid[randomY][randomX+j] != words[i][j]))
					spaceFree = FALSE;
			}
			if (randomDir == VERT) {
				if ((grid[randomY+j][randomX] != '!') && (grid[randomY+j][randomX] != words[i][j]))
					spaceFree = FALSE;
			}
		}
		
		// If it can fit, we fill in the word into the grid
		if (canFit && spaceFree) {
			if (randomDir == HORIZ) {
				for (j = 0; j < wordLengths[i]; j++) {
					grid[randomY][randomX+j] = words[i][j];
				}
			}
			if (randomDir == VERT) {
				for (j = 0; j < wordLengths[i]; j++) {
					grid[randomY+j][randomX] = words[i][j];
				}
			}
		}
		// If it can't fit, we go back and generate a new coordinate
		else {
			i--;
		}

	}
	
	// Fill in the empty spaces with a random letter
	for (i=0; i<dimension; i++) {
		for (j=0; j<dimension; j++) {
			if (grid[i][j] == '!')
			{
				grid[i][j] = ('a' + (rand()%26));
			}
		}
	}
						
	// Print out the final grid
	for (i=0; i<dimension; i++) {
		for (j=0; j<dimension; j++) {
			printf("%c ",grid[i][j]);
		}
		printf("\n");
	}
	
	printf("\nWords to Find:\n");

	for (i=0; i<NUMWORDS; i++) {
		printf("%s\t", words[i]);
	}
	
	printf("\n");
}