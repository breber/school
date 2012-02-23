/**
 * Brian Reber
 * CprE 308
 * Lab03 - Section D
 * Part 3.1
 */
#include <stdio.h>
#include <pthread.h>

void * thread1();
void * thread2();

int main() {
	// Create two pthread_t instances
	pthread_t i1;
	pthread_t i2;
	
	// Create and start the two threads - calling function thread1 and thread2 respectively
	pthread_create(&i1, NULL, thread1, NULL);
	pthread_create(&i2, NULL, thread2, NULL);
	
	// Wait for the two threads to finish before moving on
	pthread_join(i1, NULL);
	pthread_join(i2, NULL);	
	
	// After the two threads have finished, print out the following message
	printf("Hello, I am main process\n");
}

void * thread1() {
	// Sleep for 5 seconds
	sleep(5);
	
	// Print out our message
	printf("Hello, I am thread 1\n");
}

void * thread2() {
	// Sleep for 5 seconds
	sleep(5);

	// Print out our message
	printf("Hello, I am thread 2\n");
}
