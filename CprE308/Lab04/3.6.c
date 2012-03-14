/*
 * Brian Reber
 * CprE 308, Section D
 * Lab 04
 * 3.6.c
 */
#include <stdio.h>
#include <signal.h>

void my_routine();

int ret;

int main() {
	ret = fork();
	signal(SIGINT, my_routine);
	printf("Entering infinite loop\n");
	while (1) {
		sleep(10);
	}
	printf("How did you get here?\n");
}

void my_routine() {
	printf("Return value from fork = %d\n", ret);
}	
