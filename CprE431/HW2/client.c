/**
 * Timing-based Covert Channel (Client)
 *
 * Brian Reber
 * CprE 431 HW 2
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define TRUE 1
#define ITERATION_SIZE 5
#define THRESHOLD 100

int main(int argc, char ** argv) {
	char messageReceived[1000];
	
	while (TRUE) {
		int count = 0;
		time_t startTime = time(NULL) % 86400;
		while (abs(startTime - (time(NULL) % 86400)) < ITERATION_SIZE) {
			count++;
		}
		
		printf("count = %d\n", count);
	}
}
