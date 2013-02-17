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
#define ITERATION_SIZE 1
#define THRESHOLD 10000000

int main(int argc, char ** argv) {
	char messageReceived[1000];
	int i = 0;
	
	memset(messageReceived, 0, sizeof(messageReceived));

	while (TRUE) {
		int count = 0;
		time_t startTime = time(NULL) % 86400;
		while (abs(startTime - (time(NULL) % 86400)) < ITERATION_SIZE) {
			count++;
		}
		
		printf("count = %d\n", count);
		if (count < THRESHOLD) {
			messageReceived[i++] = '1';
		} else {
			messageReceived[i++] = '0';
		}

		printf("Message: %s\n", messageReceived);
	}
}
