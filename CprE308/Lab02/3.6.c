#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

int main() {
	int i=0, pid;
	pid = fork();
	if (pid == 0) {
		while (1) {
			i++;
			printf("Child at count %d\n", i);
			usleep(10000);
		}
	} else {
		printf("Parent sleeping\n");
		sleep(10);
		kill(pid, SIGTERM);
		printf("Child has been killed. Waiting for it...\n");
		wait(NULL);
		printf("Done.\n");
	}
	
	return 0;
}
