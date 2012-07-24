// wiiqualizer.c
//
// Brian Reber and Nathan Brinkman
//
// This is the outline for your program
// Please implement the functions given by the prototypes below and 
// complete the main function to make the program complete.
// You must implement the functions which are prototyped below exactly
//  as they are requested.

#include <stdio.h>
#include <math.h>
#define PI 3.141592653589
#define SCREENWIDTH 80

//PRE: Arguments must point to double variables
//This function scans a line of wiimote data, and returns
//  True when either HOME or A is pressed
//  False Otherwise
//POST: it modifies its arguments to return values read from the input line.
int read_acc(float* a_x, float* a_y, float* a_z, int *t, int* Button_one, int * Button_two, int* Button_B, int* Button_A);

// PRE: -1.0 <= x_mag <= 1.0
// This function computes the roll of the wiimote in radians
// POST: -PI/2 <= return value <= PI/2
double roll(double x_mag);

// PRE: -1.0 <= x_mag <= 1.0
// This function computes the pitch of the wiimote in radians
// if x_mag outside of -1 to 1, treat it as if it were 1 or -1
// POST: -PI/2 <= return value <= PI/2
double pitch(double y_mag);


// PRE: -PI/2 <= rad <= PI/2
// This function scales the roll value to fit on the screen 
// POST: -(1/2 screen width - 1) <= return value <= (1/2 screen width -1)
int scaleRadsForScreen(double rad);

// PRE: num >= 0 and use = a valid character
// This function prints the character use to the screen num times
// POST: nothing is returned, but use has been printed num times
void print_chars(int num, char use);

//PRE: -39 <= 0 <=39
// Uses print_chars to graph a number from -39 to 39 on the screen.
// You may assume that the screen is 80 characters wide.   
void graph_line(int number);

int main()
{
	int t;   //time
	int run = 0; //will be false unless read_acc returns true
	int rollRad = 2; //2 means we will print out the graph for roll, 1 means pitch
	float ax, ay, az;					// magnitude values of x, y, and z accelerations
	int b_a, b_b, b_home, b_1, b_2, b_junk, b_previous;	// variables to hold the button statuses; b_junk is just a temp variable
	double roll_rad, pitch_rad;					// value of the roll measured in radians
	int scaled_value; 	// value of the roll adjusted to fit screen display

	
	//insert any beginning needed code here
	
	do
		{
		// Get line of input
		run = read_acc(&ax,&ay,&az,&t,&b_1,&b_2, &b_b, &b_a);
		
		
		// calculate roll and pitch
		roll_rad = roll(ax);
		pitch_rad = pitch(ay);
		
		// switch between roll and pitch(1 or 2 button)
		if (b_1)
		{
			rollRad = 1;
		}
		if (b_2)
		{
			rollRad = 2;
		}
		if (b_b)//if the b button is pressed
		{
			//check to see that the b button wasn't pressed the last run through
			//if it was pressed last time, it will not change the mode
			if (!b_previous){
				if (rollRad == 1) rollRad = 2;
				else if (rollRad == 2) rollRad = 1;
			}
			
			//set it so that it knows that the previous time through, the b button was pressed
			b_previous = 1;
		}
		
		//resets b_previous if the b button was not pressed (resets it)
		if (!b_b && b_previous)
			b_previous = 0;
		
		// Scale your output value
		if (rollRad == 2)
			scaled_value = scaleRadsForScreen(roll_rad);
		if (rollRad == 1)
			scaled_value = scaleRadsForScreen(pitch_rad);
		
		// Output your graph line	
		graph_line(scaled_value);
		
		} while (!run); // Modify to stop when A is pressed
	return 0;
}

int read_acc(float* a_x, float* a_y, float* a_z, int *t, int* Button_one, int * Button_two, int* Button_B, int* Button_A)
{
	int b_junk;
	int b_home;
	scanf("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", t,a_x, a_y, a_z,
					Button_A,Button_B,&b_junk,&b_home,&b_junk,Button_one,Button_two,&b_junk,&b_junk,&b_junk,&b_junk);

	return (*Button_A || b_home);
		
}

double roll(double x_mag)
{
	if (x_mag > 1.0)
		x_mag = 1.0;
	if (x_mag < -1.0)
		x_mag = -1.0;
	return asin(x_mag);
}

double pitch(double y_mag)
{
	if (y_mag > 1.0)
		y_mag = 1.0;
	if (y_mag < -1.0)
		y_mag = -1.0;
	return asin(y_mag);
}

int scaleRadsForScreen(double rad)
{
	return rad*(((1.0/2.0)*SCREENWIDTH-1)/(PI/2));
}

void print_chars(int num, char use)
{
	int count;
	for (count = 0; count < num; count++)
	{
		printf("%c", use);
	}
}

void graph_line(int number)
{
	int spaceChars = SCREENWIDTH/2 -1;
	if (number == 0)
	{
		print_chars(spaceChars, ' ');
		print_chars(1,'0');
	}
	if (number > 0)
	{
		print_chars(spaceChars, ' ');
		print_chars(number, 'r');
	}
	if (number < 0)
	{
		print_chars((spaceChars+number), ' ');
		print_chars((-number), 'l');
	}
	printf("\n");
	fflush(stdout);
}