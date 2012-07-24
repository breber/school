/* Lab 4 Wrapper Program
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

		/* CODE SECTION 0 */
		printf("Echoing output: %8.3lf, %7.4lf, %7.4lf, %7.4lf\n", t/1000.0, ax, ay, az);

		/* 	CODE SECTION 1 */
		printf("At %d ms, the acceleration's magnitude was: %f\n", t, mag(ax, ay, az));
		
		/* 	CODE SECTION 2 */
		printf("At %d minutes, %d seconds, and %d ms it was: %f\n", minutes(t), seconds(t), leftover(t), mag(ax,ay,az)); 

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