/* t2.c
   synchronize threads through mutex and conditional variable 
   To compile use: gcc -o t2 t2.c -lpthread 
*/ 

#include <stdio.h>
#include <pthread.h>

// define the routines called by threads
void 	hello();
void 	world();
void 	again();

/* global variable shared by threads */
pthread_mutex_t	mutex;  		// mutex
pthread_cond_t	done_hello, done_world; 	// conditional variable
int done = 0;	// testing variable

int main (int argc, char *argv[]){
    pthread_t 	tid_hello, tid_world, tid_again; 
    
    /*  initialization on mutex and cond variable  */ 
    pthread_mutex_init(&mutex, NULL);
    pthread_cond_init(&done_hello, NULL); 
    pthread_cond_init(&done_world, NULL);
    
    pthread_create(&tid_hello, NULL, (void*)&hello, NULL); //thread creation
    pthread_create(&tid_world, NULL, (void*)&world, NULL); //thread creation 
    pthread_create(&tid_again, NULL, (void*)&again, NULL); //thread creation 

    /* main waits for the three threads to finish */
    pthread_join(tid_hello, NULL);  
    pthread_join(tid_world, NULL);
    pthread_join(tid_again, NULL);

    printf("\n");
    return 0;
}

void hello() {
    pthread_mutex_lock(&mutex);
    printf("hello ");
    fflush(stdout); 	// flush buffer to allow instant print out
    done = 1;
    pthread_cond_signal(&done_hello);	// signal world() thread
    pthread_mutex_unlock(&mutex);	// unlocks mutex to allow world to print
    return ;
}


void world() {
    pthread_mutex_lock(&mutex);

    /* world thread waits until done == 1. */
    while (done != 1) 
			pthread_cond_wait(&done_hello, &mutex);

    printf("world ");
    fflush(stdout);
		done = 2;	// Update done so "again" knows that it is its turn
    pthread_cond_signal(&done_world);	// signal again() thread
    pthread_mutex_unlock(&mutex); // unlocks mutex

    return ;
}

void again() {
    pthread_mutex_lock(&mutex);

    /* again thread waits until done == 2. */
    while	(done != 2) 
			pthread_cond_wait(&done_world, &mutex);

    printf("again");
    fflush(stdout);
    pthread_mutex_unlock(&mutex); // unlocks mutex

    return ;
}
