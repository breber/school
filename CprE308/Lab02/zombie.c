#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
	int tmp = fork();
	
	if (tmp == 0) {
		printf("I am the child\n");
		sleep(1);
		printf("Finishing the child process\n");
	} else {
		sleep(5);
		int tmp1 = fork();
		int status;
		if (tmp1 == 0) {
			// Run a ps -l to see if our child is a zombie
			execl("/bin/ps", "ps", "-l", NULL);
		} else {
			// Wait for our ps -l command to finish
			waitpid(tmp1, &status, 0);
			// Wait on the initial child
			waitpid(tmp, &status, 0);
		}
	}
	
	return 0;
}
