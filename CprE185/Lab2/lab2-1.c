/*
 *  lab2-1.c
 *  
 *
 *  Programmer: Brian Reber   Date: 9/11/09
 *  Instructor: Daniels      Class: CprE 185   Section: A
 *
 */


// LAB2-1.c : Prompts the user for two int values, performs an equation and prints out the value.


#include "stdio.h"

int main(int argc, char* argv[]) 
{
	int a, b, c;
	
	printf("Please enter an int for variable a: ");
	scanf("%d", &a);
	
	printf("Please enter an int for variable b: ");
	scanf("%d", &b);
	
	c = a*a/b +b*b/a;
	
	printf("Nathan Brinkman and Brian Reber's CprE 185 Lab programming project is almost done!\n");
	
	printf("C = %d\n", c);
	
	return 0;
}



