#include <stdio.h>
#include <unistd.h>

int main() {
	execl("/bin/pwd", "pwd", NULL);
	printf("What happened?\n");
}
