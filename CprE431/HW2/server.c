/**
 * Timing-based Covert Channel (Server)
 *
 * Brian Reber
 * CprE 431 HW 2
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define ITERATION_SIZE 5

int main(int argc, char ** argv) {
	if (argc == 1) {
		fprintf(stderr, "Missing parameter message\n");
		fprintf(stderr, "Usage: %s \"binary_string_to_send\"\n", argv[0]);
	}
	
	char * stringToSend = argv[1];
	int i;

	for (i = 0; i < strlen(stringToSend); i++) {
		char c = stringToSend[i];
		
		if ('1' == c) {
			time_t startTime = time(NULL) % 86400;
			while (abs(startTime - (time(NULL) % 86400)) < ITERATION_SIZE);
		} else {
			sleep(ITERATION_SIZE);
		}
	}
}
