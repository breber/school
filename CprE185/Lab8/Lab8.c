/* Lab 8 - Moving Average
 Brian Reber & Nathan Brinkman
 */

#include <stdio.h>


#define MAXPOINTS 10000

	// compute the average of the first num_items of buffer
	double avg(double buffer[], int num_items);

	//return the max and min of the first num_items of array
	void maxmin(double array[], int num_items, double* max, double* min);

	//shift length-1 elements of the buffer to the left and put the 
	//new_item on the right.
	double updatebuffer(double buffer[], int length, double new_item);
	
int main(int argc, char* argv[]) {

	double x[MAXPOINTS], y[MAXPOINTS], z[MAXPOINTS];
	int lengthofavg = 0;
	int count = 0, t;
	float ax,ay,az;
	double max_x, min_x;
	double max_y, min_y;
	double max_z, min_z;


	int button_a, button_junk;
	
	//Read in the number from the command line
	if (argc>1)
		{
		sscanf(argv[1], "%d", &lengthofavg );
		printf("You entered a buffer length of %d\n", lengthofavg);
		}
	else {
		printf("Enter a length on the command line\n");
		return(-1);
		}
	if (lengthofavg < 1 || lengthofavg > MAXPOINTS) 
		{
		printf("Invalid length\n");
		return(-1);
		}

	//Fill in the user specified number in the array
	//This is our initial time through so we need to populate the 
	//array before we call the updatebuffer function
	for (count = 0; count < lengthofavg; count++)
	{
		scanf("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", &t, &ax, &ay, &az,
					&button_a,&button_junk,&button_junk,&button_junk,&button_junk,
					&button_junk,&button_junk,&button_junk,&button_junk,&button_junk,&button_junk);

		x[count] = ax;
		y[count] = ay;
		z[count] = az;
	}	
	
	//We then run through until the a button has been pressed
	do
	{
		scanf("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", &t, &ax, &ay, &az,
					&button_a,&button_junk,&button_junk,&button_junk,&button_junk,
					&button_junk,&button_junk,&button_junk,&button_junk,&button_junk,&button_junk);
		//We update the array
		updatebuffer(x,lengthofavg,ax);
		updatebuffer(y,lengthofavg,ay);
		updatebuffer(z,lengthofavg,az);
		
		//We then compute the max and min
		maxmin(x, lengthofavg, &max_x, &min_x);
		maxmin(y, lengthofavg, &max_y, &min_y);
		maxmin(z, lengthofavg, &max_z, &min_z);

		//Finally printing it out
		printf("%4.2lf, %4.2lf, %4.2lf, %4.2lf, %4.2lf, %4.2lf, %4.2lf, %4.2lf, %4.2lf, %4.2lf, %4.2lf, %4.2lf\n",  
			ax,ay,az,avg(x, lengthofavg),avg(y, lengthofavg), avg(z, lengthofavg), max_x, max_y, max_z, min_x, min_y, min_z);

	} while (!button_a);
		
	return 0;
}

/*
 param: buffer[] - this is the current array we want to find the average of
 param: num_items - this is the amount of items we need to scan through
 */
double avg(double buffer[], int num_items){
	int i;
	double avg = 0.0;
	for (i = 0; i < num_items; i++)
	{
		avg += buffer[i];
	}
	
	return avg/num_items;
}

/*
 param: array[] - this is the current array we want to find the max and min nums in of
 param: num_items - this is the amount of items we need to scan through
 param: double* max - this is the output param - we will use this to return the max 
		value in the array.
 param: double* min - this is the output param - we will use this to return the min 
		value in the array.
 */
void maxmin(double array[], int num_items, double* max, double* min){
	int i;
	double max_local = -50, min_local = 50;
	
	for (i = 0; i < num_items; i++)
	{
		if (array[i] > max_local) max_local = array[i];
		if (array[i] < min_local) min_local = array[i];
	}
	
	*max = max_local;
	*min = min_local;
}

/*
 param: buffer[] - this is the current array we want to update
 param: length - this is the amount of items we need to scan through
 param: new_item - this is the item we want to put at the end of the buffer array
 */
double updatebuffer(double buffer[], int length, double new_item){
	int i;
	for (i = 0; i < length-1; i++)
	{
		buffer[i] = buffer[i+1];
	}
	buffer[length-1] = new_item;
}
