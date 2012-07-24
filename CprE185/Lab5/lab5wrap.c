/* Lab 5 Wrapper Program
   Brian Reber & Nathan Brinkman
 */

#include <stdio.h>
#include <math.h>

/* Put your function prototypes here */
double mag(float x, float y, float z);
int minutes(float ms);
int seconds(float ms);
int leftover(float ms);
int close_to(float tolerance, float point, float value);
int whatsup(float ax, float ay, float az, int orient, int t);



int main(void) {
	int t;
	float  ax, ay, az;
	
	int a,b,plus,home,minus,one,two,dup,ddown,dleft,dright;
	
	int true = 1;
	
	int orient, previous;
	/*
		0 = Front
		1 = Tail
		2 = Top
		3 = Bottom
		4 = Left
		5 = Right
	*/
	
	while (true) {
		scanf("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", &t, &ax, &ay, &az,
					&a,&b,&plus,&home,&minus,&one,&two,&dup,&ddown,&dleft,&dright);	

		if (close_to(.05,1,mag(ax,ay,az)))
		{
			previous = orient;
			orient = whatsup(ax,ay,az, orient, t);
			if (!(orient == previous))
			{
				if (orient ==0)
					printf("Front\n");
				if (orient ==1)
					printf("Tail\n");
				if (orient ==2)
					printf("Top\n");
				if (orient ==3)
					printf("Bottom\n");
				if (orient ==4)
					printf("Left\n");
				if (orient ==5)
					printf("Right\n");
			}
		}
		
		if(two)
			true = 0;

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

int whatsup(float ax, float ay, float az, int orient, int t)
{
	if (close_to(.05,-1,ay))
	{
		orient = 0;//Front
	}
	if (close_to(.05,1,ay))
	{
		orient = 1;//Tail
	}			
	if (close_to(.05,-1,az))
	{
		orient = 3;//Bottom
	}
	if (close_to(.05,1,az))
	{
		orient = 2;//Top
	}
	if (close_to(.05,-1,ax))
	{
		orient = 5;//Right
	}
	if (close_to(.05,1,ax))
	{
		orient = 4;//Left
	}
	
	return orient;
}