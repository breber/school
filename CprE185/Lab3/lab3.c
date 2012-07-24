// CprE 185: Lab 3
// Problem 1: Mysterious Output

#include "stdio.h"

int main()
{
	short IntegerResult;
	float DecimalResult;
	
	IntegerResult = 1 / 5;
	printf("The value of 1/5 is %f\n", IntegerResult);
	
	IntegerResult = 2 + 3;
	printf("The value of 2+3 is %d\n");
	
	DecimalResult = 1.0 / 22.0;
	printf("The value 1.0/22.0 is %d\n", DecimalResult);
	
	return 0;
}
