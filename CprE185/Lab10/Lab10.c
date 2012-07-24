// Nathan Brinkman and Brian Reber

// WiiTalker

// Headers
#include <stdio.h>
#include <math.h>
#include <ncurses.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

// Mathematical constants
#define PI 3.14159

// Screen geometry
// Use LINES and COLS for the screen heigth and width (set by system)
// MAXIMUMS
#define SCREEN_WIDTH 100
#define SCREEN_HEIGHT 50

// defines sizes for the number of rows and columns for the word array
#define WORDCOL 11
#define WORDROW 100


// PRE: 0 < x < SCREEN_WIDTH, 0 < y < SCREEN_HEIGHT, 0 < use < 255
// POST: Draws character use to the screen and position x,y
void draw_character(int x, int y, char use);


// The function that reads in the data from the wiimote
int read_buttons(int* Button_A,int* Button_B,int* plus,int* home,int* minus,int* Button_one,int* Button_two,int* d_up,int* d_down,int* d_left,int* d_right);

// This moves the avatar
void move_avatar(int* b_d_up,int* b_d_down,int* b_d_left,int* b_d_right, int* current_x, int* current_y, int *downp, int *upp, int *rightp, int *leftp);

void append_space_word(char* word, int current_x, int current_y, int position[WORDROW][3], char wordarray[WORDROW][WORDCOL]);


// Main - Run with 'wiiwrap2.exe /T /A /B' piped into STDIN
int main(int argc, char* argv[])
{	int i=0;                                       // acts as a counter for loops
	int b_a, b_b, b_plus, b_home=0, b_minus;
	int b_1, b_2, b_d_up=0, b_d_down=0, b_d_left=0, b_d_right=0;// variables to hold the button statuses; b_junk is just a temp variable		
	int current_x =0, current_y =0;               // the values of the initial coordinates of the cursor
	char filename[20];						           
	FILE* input;
	char wordarray[WORDROW][WORDCOL];
	int position[WORDROW][3];						//this array holds the coordinates of each word
	int downpress=0,uppress=0,rightpress=0,leftpress=0; //variables for keeping track of if the button has been pressed to get better accuracy
	char word[SCREEN_WIDTH];
	
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
  	
	// setup screen	
	initscr();
	refresh();
	
	// Opens the file and gets the word list.
	input = fopen(filename, "r");
	for(i=0; i<WORDROW && !feof(input); i++)
		fscanf(input, "%s", wordarray[i]);
	
	int numlines = i;
	
	// this is the x coordinate (horizontal) - starts at -15 because we 
	//add 15 before we print out to the screen	
	int j = -14;  
	for(i=0; i<numlines; i++)
	{
		mvprintw(i/5,j+=15,"%d %s",i,wordarray[i]);
		refresh();
		position[i][0] = i;
		position[i][1] = i/5;
		position[i][2] = j;
		if (j >= 60) j = -14;
	}
	move(current_x, current_y);
	refresh();
	i = 0;
	//Event loop
	do
	{
		i++;
		
		//read_buttons(&b_a, &b_b, &b_plus, &b_home, &b_minus, &b_1, &b_2, &b_d_up, &b_d_down, &b_d_left, &b_d_right);
		//move_avatar(&b_d_up,&b_d_down,&b_d_left,&b_d_right,&current_x,&current_y, &downpress, &uppress, &rightpress, &leftpress);
		
		if(i%1000)
		{
			append_space_word(word, current_x, current_y, position, wordarray);
		}
		
	} while(!b_home); 

	// Print the win message
	endwin();
	
	
	return 0;
}

// PRE: 0 < x < SCREEN_WIDTH, 0 < y < SCREEN_HEIGHT, 0 < use < 255
// POST: Draws character use to the screen and position x,y
void draw_character(int x, int y, char use)
{
	mvaddch(x,y,use);	
	refresh();
}

int read_buttons(int* Button_A,int* Button_B,int* plus,int* home,int* minus,int* Button_one,
						int* Button_two,int* d_up,int* d_down,int* d_left,int* d_right){
	scanf("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d",
		Button_A,Button_B,plus,home,minus,Button_one,Button_two,d_up,d_down,d_left,d_right);
	return 0;
	} 			

//we chose to use the buttons to move the cursor because the buttons 
void move_avatar(int* b_d_up,int* b_d_down,int* b_d_left,int* b_d_right, int* current_x, 
					int* current_y, int *downp, int *upp, int *rightp, int *leftp)
{		
	if (*b_d_down && !*downp)
	{	*current_x += 1;
		draw_character(*current_x, *current_y,'*');
		draw_character(*current_x-1, *current_y,' ');
		*downp =1, *upp=0, *rightp=0, *leftp=0;
	}
	else if (*b_d_up && !*upp)
	{	*current_x -= 1;
		if (*current_x < 0) *current_x = 0;
		draw_character(*current_x, *current_y,'*');
		draw_character(*current_x+1, *current_y,' ');
		*downp =0, *upp=1, *rightp=0, *leftp=0;
	}
	else if (*b_d_right && !*rightp)
	{	*current_y += 15;
		draw_character(*current_x, *current_y,'*');
		draw_character(*current_x, *current_y-15,' ');
		*downp =0, *upp=0, *rightp=1, *leftp=0;
	}
	else if (*b_d_left && !*leftp)
	{	*current_y -= 15;
		if (*current_y < 0) *current_y = 0;
		draw_character(*current_x, *current_y,'*');
		draw_character(*current_x, *current_y+15,' ');
		*downp =0, *upp=0, *rightp=0, *leftp=1;
	}
	/*else
	{	*downp =0;
		*upp=0;
		*rightp=0;
		*leftp=0;	
	}*/
	refresh();
}

void append_space_word(char* word, int current_x, int current_y, int position[WORDROW][3], char wordarray[WORDROW][WORDCOL])
{	
	int temp = 0, i = 0;
	for (i = 0; i < WORDROW; i++)
	{
		if (position[i][1] == current_y && position[i][2] == current_x)
			temp = i;
	}
	strcat(word, " ");
	strcat(word, wordarray[temp]);
	//printf("%s", word);
	mvprintw(25,0,"%s",word);
}
