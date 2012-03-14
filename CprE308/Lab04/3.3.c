/*
 * Brian Reber
 * CprE 308, Section D
 * Lab04
 * 3.3.c
 */
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>

void my_routine() {
	printf("Caught a SIGFPE\n");
	exit(0);
}

int main() {
	// This statement must come before the division by zero
	// otherwise it won't be set up before the error occurs
	// and it won't be called
	signal(SIGFPE, my_routine);
	
	int a = 4;
	a /= 0;
}
