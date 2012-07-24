// Nathan Brinkman and Brian Reber

// WII-MAZE Skeleton code written by Jason Erbskorn 2007
// Edited for ncurses 2008 Tom Daniels


// Headers
#include <stdio.h>
#include <math.h>
#include <ncurses/ncurses.h>
#include <unistd.h>
#include <stdlib.h>
#include <time.h>

// Mathematical constants
#define PI 3.14159

// Screen geometry
// Use LINES and COLS for the screen heigth and width (set by system)
// MAXIMUMS
#define SCREEN_WIDTH 100
#define SCREEN_HEIGHT 50

// Character definitions taken from the ASCII table
#define AVATAR 'A'
#define WALL '*'
#define EMPTY_SPACE ' '

// Number of samples taken to form an average for the accelerometer data
// Feel free to tweak this
#define NUM_SAMPLES 25 

// Not what you use for your delay.
#define LOOP_IDLE 15

// The value used to slow down the program
#define TIME_DELAY 45

// Tolerance for "Dead Zone"
#define TOLERANCE .15

// 2D character array which the maze is mapped into
char MAZE[SCREEN_WIDTH][SCREEN_HEIGHT];

// POST: Generates a random maze structure into MAZE[][]
void generate_maze(int difficulty);

// PRE: MAZE[][] has been initialized by generate_maze()
// POST: Draws the maze to the screen 
void draw_maze(void);

// PRE: 0 < x < SCREEN_WIDTH, 0 < y < SCREEN_HEIGHT, 0 < use < 255
// POST: Draws character use to the screen and position x,y
void draw_character(int x, int y, char use);

// PRE: -1.0 < y_mag < 1.0
// POST: Returns tilt magnitude scaled to -1.0 -> 1.0
float calc_pitch(float y_mag);

// The function that reads in the data from the wiimote
int read_acc(float* a_x, float* a_y, float* a_z, int* t, 
			 int* Button_one, int * Button_two, int* Button_B, int* Button_A);

// This function updates the average buffer
double updatebuffer(double buffer[], int length, double new_item);

// This function averages the values in the buffer array.
double avg(double buffer[], int num_items);

// This moves the avatar
int move_avatar(double avg_x, int* current_x, int* current_y);


// Main - Run with 'wiiwrap2.exe /T /A /B' piped into STDIN
int main(int argc, char* argv[])
{	int count = 0;                                 // acts as a counter for array input
	double x[NUM_SAMPLES];						   // an array for the averaging buffer
	int time_counter;							   // a counter to slow the program
	float a_x, a_y, a_z;					       // magnitude values of x, y, and z accelerations
	int b_a, b_b, b_home, t, b_1, b_2, b_junk;     // variables to hold the button statuses; b_junk is just a temp variable		
	int current_x = 50, current_y = 0;             // the values of the initial coordinates of the AVATAR
	double avg_x;						           // average values for x and y
	int difficulty;								   // The difficulty of the maze
	int stuck = 0;								   // a variable that allows the program to quit if the AVATAR is stuck
	
	// Get the difficulty from the command line
	if (argc>1)
	{
		sscanf(argv[1], "%d", &difficulty);
		printf("You entered a difficulty of %d\n", difficulty);
	}
	else
	{
		printf("Enter a difficulty  on the command line\n");
		return(-1);
	}
	if (difficulty < 0 || difficulty > 100) 
	{
		printf("Invalid Difficulty\n");
		return(-1);
	}
		
	// setup screen	
	initscr();
	refresh();
	
	// Generate and draw the maze, with initial avatar 
	generate_maze(difficulty);
	draw_maze();
	
	// AVATAR Placed and Waiting
	draw_character(current_x,current_y, AVATAR);       
	
	// Read accelerometer data to get ready for using moving averages.	
	// Loads the first buffer for x, y, and z arrays with data from the wiimote
	for(count = 0; count<NUM_SAMPLES; count++){
		read_acc(&a_x, &a_y, &a_z, &t, &b_1, &b_2, &b_b, &b_a);
		x[count] = a_x;
	}
	
	//Event loop
	do
	{
		// Read data, update average
		read_acc(&a_x, &a_y, &a_z, &t, &b_1, &b_2, &b_b, &b_a);
		
		// updates the buffers & computes average
		updatebuffer(x,NUM_SAMPLES,a_x);
		avg_x = avg(x, NUM_SAMPLES);
		
		// Begin Move?
		if((time_counter % TIME_DELAY) == 0)
			stuck = move_avatar(avg_x, &current_x, &current_y);   
		
		usleep(LOOP_IDLE); // leave this here to smooth out game play
		time_counter++;
		
	} while((current_y < (SCREEN_HEIGHT)) && !b_a && !stuck); // Did I win? Am I stuck?

	// Print the win message
	endwin();
	
	// If we are stuck we will let the user know.
	if(stuck)
		printf("YOU GOT STUCK!\n");
	// If we won, we will congratulate the user.
	if(current_y >= SCREEN_HEIGHT)
		printf("!CONGRATULATIONS!\n!!!! YOU WIN !!!!\n");
	// If the user exits out by pressing the A button this is displayed.
	else
		printf("YOU DIDN'T FINISH!!\nYOU LOST THE GAME!!!");
		
	return 0;
}



// PRE: 0 < x < SCREEN_WIDTH, 0 < y < SCREEN_HEIGHT, 0 < use < 255
// POST: Draws character use to the screen and position x,y
void draw_character(int x, int y, char use)
{
	mvaddch(y,x,use);	
	refresh();
}

void draw_maze(void){
	int i;
	int j;
	for(i=0; i<SCREEN_HEIGHT; i++){
		for(j=0; j<SCREEN_WIDTH; j++){
			draw_character(j,i,MAZE[j][i]);
		}
	}
}

void generate_maze(int difficulty){
	int i;
	int j;
	int random;
	
	// seeds the random function with time from the computer
	srand(time(0));  
	
	// Initializes the Maze array with spaces
	for(i=0; i<SCREEN_HEIGHT; i++)
	{
		for(j=0; j<SCREEN_WIDTH; j++)
		{
			MAZE[j][i] = EMPTY_SPACE;
		}
	}
	
	for(i=0; i<SCREEN_HEIGHT-1; i++)
	{
		for(j=0; j<SCREEN_WIDTH; j++)
		{
			random = rand() % 100;
			
			// Uses difficulty entered by user
			if(random<difficulty)                    
				MAZE[j][i] = WALL;
			else 
				MAZE[j][i] = EMPTY_SPACE;
		}
	}
	
	// makes sure the AVATAR's beginning position is clear
	MAZE[50][0] = EMPTY_SPACE;						 
}

int read_acc(float* a_x, float* a_y, float* a_z, int* t, int* Button_one, 
			 int * Button_two, int* Button_B, int* Button_A)
{
	int junk; // a junk variable for unneeded buttons
	int HOME; // variable for home button
	scanf("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", 
		  t,a_x,a_y,a_z,Button_A,Button_B,&HOME,&junk,&junk,Button_one,
		  Button_two,&junk,&junk,&junk,&junk);
	return 0;

} 			

double updatebuffer(double buffer[], int length, double new_item)
{
	int i;
	for(i=0; i<length; i++)
		buffer[i] = buffer[i+1];
	buffer[length] = new_item;
}

double avg(double buffer[], int num_items)
{
	int i;
	double sum = 0;
	double avg_value;
	
	for(i=0; i<num_items; i++)
	{
		sum = sum + buffer[i];
	}
	
	avg_value = sum/num_items;
	
	return avg_value;		
}

int move_avatar(double avg_x, int* current_x, int* current_y)
{	
	int original_x = *current_x;
	int original_y = *current_y;
	
	// Erase Previous AVATAR
	draw_character(*current_x, *current_y, EMPTY_SPACE);
	
	// Left Move?
	if(avg_x < -TOLERANCE)
		// Update X
		*current_x = *current_x - 1;    
		
	// Right Move?
	else if(avg_x > TOLERANCE)
		// Update X
		*current_x = *current_x +1;    
		
	// Update Y
	else *current_y = *current_y + 1; 
	
	// Can I Fall? (Check to see if the space we are moving to contains a WALL)
	if( MAZE[*current_x][*current_y] == WALL || *current_x < 0 || *current_x > SCREEN_WIDTH-1)
	{
		*current_x = original_x;
		*current_y = original_y;
	}
	
	// Place new Avatar
	draw_character(*current_x, *current_y, AVATAR);
	
	// If the AVATAR is stuck this allows the program to end
	if (MAZE[*current_x-1][*current_y] == WALL && 
			MAZE[*current_x+1][*current_y] == WALL && 
			MAZE[*current_x][*current_y+1] == WALL)
	{
		return 1;	
	}
	
	// Returns 0 if we are able to successfully move
	return 0;
}
