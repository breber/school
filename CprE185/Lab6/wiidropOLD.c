/* Lab 6 Wiidrop Program
   Brian Reber & Nathan Brinkman
 */

#include <stdio.h>
#include <math.h>
#define TOL .1 //define tolerance for close to function

/* Put your function prototypes here */
double mag(float x, float y, float z);
int minutes(float ms);
int seconds(float ms);
int leftover(float ms);
int close_to(float tolerance, float point, float value);




int main(void) {
	int t;
	float  ax, ay, az;
	int run = 1;
	int a,b,plus,home,minus,one,two,dup,ddown,dleft,dright;
	int status =0;
	int stime  =0;
	int etime  =0;
	float tottime=0;
	float dist =0;
	
	/*
		status definitions
		0=Waiting 
		1=Falling	
	*/

	
	
	while (run) {
		scanf("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", &t, &ax, &ay, &az,
					&a,&b,&plus,&home,&minus,&one,&two,&dup,&ddown,&dleft,&dright);	
		
		if((close_to(TOL,1,mag(ax,ay,az))) && (status !=1))
			{		printf("Waiting"); 
					fflush(stdout);
					status = 1;
			}
		if((close_to(TOL,1,mag(ax,ay,az))) && (status ==1))
			{
					printf(".");
					fflush(stdout);
			}
		if((close_to(TOL,0,mag(ax,ay,az))) && (status != 2))
			{		printf("\nHELP! I'M FALLING");
					stime = t; status = 2;
			}
		if((close_to(TOL,0,mag(ax,ay,az))) && (status == 2))
			{
					printf("!");
					fflush(stdout);

			}
		if(((mag(ax,ay,az))> 1.5) && (status == 2))
			{
					etime = t; status =0;
					tottime = etime - stime;
					tottime = tottime/1000.0;
					dist = (9.8*(tottime*tottime))/2;
					printf("\nOUCH! I fell %4.5lf meters in %4.3lf seconds.\n", dist, tottime);
					tottime=0;
					stime=0;
					etime=0;
					dist=0;
					
			}
			
			// printf("%7.4lf %7.4lf %7.4lf %7.4lf \n", ax, ay, az, mag(ax,ay,az) );
			
		
		
		if(plus)
		{	run = 0;
			printf("PLUS PRESSED, EXITING PROGRAM.");
		}
	}

	return 0;
	
}

double mag(float x, float y, float z)
{
	return sqrt(x*x + y*y + z*z);
}

int close_to(float tolerance, float point, float value)
{
	return (value < point+tolerance && value > point-tolerance);
}

