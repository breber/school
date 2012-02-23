#include <stdio.h>

int main() {
	fork(); // Main creates child
	fork(); // Main creates child2, child creates subchild
	usleep(1);
	printf("Process %d's parent process ID is %d\n", getpid(), getppid());
	//sleep(2);
	return 0;
}
