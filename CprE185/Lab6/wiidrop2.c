/* Lab 6 Wiidrop Program
   Brian Reber & Nathan Brinkman
 */

#include <stdio.h>
#define TOL .15 //define tolerance for close to function
#define G 9.8

/* Put your function prototypes here */
double mag(float x, float y, float z);
int minutes(float ms);
int seconds(float ms);
int leftover(float ms);
int close_to(float tolerance, float point, float value);


int main(void) {

	float  ax, ay, az;
	int run = 1;
	int t,a,b,plus,home,minus,one,two,dup,ddown,dleft,dright;
	int status = 0;
	int stime  = 0;
	int etime  = 0;
	float tottime = 0;
	float dist = 0, dist2 = 0;
	int hit = 0;
	double velocity = 0;
	float t_f = 0, t_i = 0;
	
	
	//while (run) {

		printf("I'm Waiting");
		//Waiting loop
		do
		{
			scanf("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", &t, &ax, &ay, &az,
					&a,&b,&plus,&home,&minus,&one,&two,&dup,&ddown,&dleft,&dright);	
				
			if (status % 10 == 0)
			{
				printf(".");
				fflush(stdout);
			}
			status++;
			
			if(plus)
			{	
				run = 0;
				printf("PLUS PRESSED, EXITING PROGRAM.");
			}
		} while (!(close_to(TOL,0,mag(ax,ay,az))) && run);
		
		//reset status to 0 so that we can tell the first time in the next loop
		status = 0;
		if (run)
		{
			printf("\n\n\tHELP! I'M FALLING");
			stime = t;
			
			//Falling loop
			do
			{
				scanf("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", &t, &ax, &ay, &az,
						&a,&b,&plus,&home,&minus,&one,&two,&dup,&ddown,&dleft,&dright);	

				t_f = t/1000.0;
				
				if (status == 0)
					t_f = 0;
				
				velocity = velocity + (G - mag(ax,ay,az))*(t_f-t_i);
				dist2 = dist2 + velocity * (t_f-t_i);
				
				t_i = t/1000.0;
				
				if (status % 10 == 0)
				{
					printf("!");
					fflush(stdout);
				}
				status++;		
				
				if(plus)
				{	
					run = 0;
					printf("PLUS PRESSED, EXITING PROGRAM.");
				}
			} while (!(mag(ax,ay,az) > 1.5) && run);
		}
		if (run)
		{
			etime = t; 
			tottime = etime - stime;
			tottime = tottime/1000.0;
			dist = (G*(tottime*tottime))/2;
						
			printf("\n\n\t\tOUCH! I fell %4.5lf meters in %4.3lf seconds.\n\n", dist, tottime);
			printf("\t\tCompensating for air resistance, the fall was %4.5lf meters.\n", dist2);
			printf("\t\tThis is %.2lf%c less than computed before.\n", ((1-(dist2/dist))*100), 37);
		}
		
		tottime=0;
		stime=0;
		etime=0;
		dist=0;
		
		if(plus && run)
		{	
			run = 0;
			printf("PLUS PRESSED, EXITING PROGRAM.");
		}
	//}

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

