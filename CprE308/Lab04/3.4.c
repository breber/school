/*
 * Brian Reber
 * CprE 308, Section D
 * Lab04
 * 3.4.c
 */
#include <stdio.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>

char msg[100];

void my_alarm();

int main(int argc, char ** argv) {
	int time;
	if (argc < 3) {
		printf("not enough parameters\n");
		exit(1);
	}

	time = atoi(argv[2]);
	strcpy(msg, argv[1]);

	signal(SIGALRM, my_alarm);
	alarm(time);
	printf("Entering infinite loop\n");
	while (1) {
		sleep(10);
	}

	printf("How did you get here?\n");
}

void my_alarm() {
	printf("%s\n", msg);
	exit(0);
}
