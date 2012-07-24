#include <iostream>
#include <stdio.h>

using namespace std;

int main(int argc, char *argv[]) {
	
	FILE *fileWrite;
	FILE *fileRead;
	
	// Print out command line args for debugging purposes
	for (int i = 0; i < argc; i++) {
		cout << "ARGV[" << i << "] " << argv[i] << endl;
	}
	
	
	// Open the file descriptors given by Piper
	int fd1;
	int fd2;
	
	sscanf(argv[4], "%d", &fd1);
	fileWrite = fdopen(fd1, "a");
	cout << "OPENINGFILEFORWRITE " << fileWrite << endl;
	
	sscanf(argv[5], "%d", &fd2);
	fileRead = fdopen(fd2, "r");
	cout << "OPENINGFILEFORREAD " << fileRead << endl;
	
	
	// Write something to the Writable FILE * and
	// wait for a response on the readable FILE *
	for (int i = 0; i < 10; i++) {

		// Send a string to the writable FD
		char *temp;
		cout << "SENDINGSTRING" << endl;
		fprintf(fileWrite, "test_%d\n", i);
		fflush(fileWrite);
		cout << "SENTSTRING" << endl;
		
		// Read the response on the readable FD
		cout << "READINGSTRING" << endl;
		fscanf(fileRead, "%s", temp);
		cout << "COMPLETEDREADING" << endl;
		
		// Print out the received string
		printf("RECEIVEDSTRING = %s\n", temp);
	}
	
	fprintf(fileWrite, "stop");
	
	return 0;
}
