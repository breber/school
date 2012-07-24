/* Lab 4 Wrapper Program Final
   Brian Reber & Nathan Brinkman
 */

#include <stdio.h>
#include <math.h>

#define TRUE 1

/* Put your function prototypes here */
double mag(float x, float y, float z);
int minutes(float ms);
int seconds(float ms);
int leftover(float ms);


int main(void) {
	int t;
	float  ax, ay, az;


	while (TRUE) {
		scanf("%d,%f,%f,%f", &t, &ax, &ay, &az);	

		if (mag(ax,ay,az) > .9 && mag(ax,ay,az) < 1.10)
		{
			if (ay < -.9 && ay > -1.1)
				printf("The Wiimote is on its TAIL at %d minutes, %d seconds and %d miliseconds.\n", minutes(t), seconds(t), leftover(t));
			if (ay > .9 && ay < 1.1)
				printf("The Wiimote is on its FACE at %d minutes, %d seconds and %d miliseconds.\n", minutes(t), seconds(t), leftover(t)); 				
		}

	}

	return 0;
	
}

double mag(float x, float y, float z)
{
	return sqrt(x*x + y*y + z*z);
}

int minutes(float ms)
{
	return ms/60000;
}

int seconds(float ms)
{
	ms = ms - (minutes(ms)*60000);
	
	return ms/1000;
}

int leftover(float ms)
{
	return ms - (minutes(ms)*60000) - (seconds(ms)*1000);
}