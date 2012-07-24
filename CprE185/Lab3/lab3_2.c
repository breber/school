/*
 *  CprE 185: Lab 3
 *	lab3_2.c
 *  
 *
 *  Programmer: Brian Reber   Date: 9/18/09
 *  Instructor: Daniels       Class: CprE185
 *
 *  INSERT WHAT FILE DOES!
 */

#include <stdio.h>

int main()
{
	short a = 6427 + 1725;
	printf("6427 + 1725 = %d\n", a);
	
	short b = (6971 * 3925) - 95;
	printf("(6971 * 3925) - 95 = %d\n", b);
	
	float c = 79 + 12 / 5;
	printf("79 + 12 / 5 = %.2f\n", c);
	
	float d = 3640.0 / 107.9;
	printf("3640.0 / 107.9 = %.2f\n", d);
	
	short e = (22 / 3) * 3;
	printf("(22 / 3) * 3 = %d\n", e);
	
	int f = 22 / (3 * 3);
	printf("22 / (3 * 3) = %d\n", f);
	
	float g = 22 / (3 * 3);
	printf("22 / (3 * 3) = %.2f\n", g);
	
	float h = 22 / 3 * 3;
	printf("22 / 3 * 3 = %.2f\n", h);
	
	float i = (22.0 / 3) * 3.0;
	printf("(22.0 / 3) * 3.0 = %.2f\n", i);
	
	short j = 22.0 / (3.0 * 3.0);
	printf("22.0 / (3.0 * 3.0) = %d\n", j);
	
	float k = 22.0 / 3.0 * 3.0;
	printf("22.0 / 3.0 * 3.0 = %.2f\n", k);
	
	float radius = 23.567 / (2 * 3.14);
	float area = 3.14 * radius * radius;
	printf("The area of a circle with circumference 23.567 = %.2f\n", area);
	
	float feet = 14;
	float meters = feet * .3;
	printf("14 ft = %.2f meters \n", meters);
	
	float degrees = 76;
	float centigrade = (degrees - 32) / 1.8;
	printf("76 degrees Fahrenheit = %.2f degrees Centigrade\n", centigrade);

}