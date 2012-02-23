/**
 * Brian Reber
 * CprE308 Project 2
 * Lab Section D
 * appserver.c
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include "Bank.h"

#define FALSE 0
#define TRUE 1

struct account {
	pthread_mutex_t lock;
	int value;
};

struct node {
	char * cmd;
	struct timeval startTime;
	struct node * next;
};

pthread_mutex_t queue_mutex;
int globReqNum;

struct node * commands;
struct account * accounts;
pthread_t * cons_tid;
FILE * outputFile;

void initEverything(int, int);
void * workerThread();
void fixString(char * str);

int main(int argc, char ** argv) {
	int i;
	int numWorkerThreads;
	int numAccounts;
	
	// Parse arguments
	if (argc < 4) {
		fprintf(stderr, "Not enough arguments\n");
		fprintf(stderr, "\t./appserver <# of worker threads> <# of accounts> <output file>;\n");
		exit(1);
	} else {
		numWorkerThreads = atoi(argv[1]);
		numAccounts = atoi(argv[2]);
		
		outputFile = fopen(argv[3], "w");
	}
	
	// Initialize accounts and mutexes
	initEverything(numAccounts, numWorkerThreads);

	// Command buffer loop
	while (1) {
		size_t numBytes = 200;
		char * line = malloc(numBytes * sizeof(char));
		
		getline(&line, &numBytes, stdin);
		
		struct node * tmpCmd = malloc(sizeof( struct node ));
		tmpCmd->cmd = line;
		gettimeofday(&tmpCmd->startTime, NULL);
		tmpCmd->next = NULL;
		
		pthread_mutex_lock(&queue_mutex);
		if (commands == NULL) {
			// We have an empty buffer
			commands = tmpCmd;
		} else {
			struct node * cur = commands;
			
			// Find the last element of the queue
			while (cur->next != NULL) {
				cur = cur->next;
			}
			
			// cur is the last element of the queue
			cur->next = tmpCmd;
		}
		
		pthread_mutex_unlock(&queue_mutex);

		// Stop accepting commands if the previous command was END
		if (strcmp("END\n", line) == 0) {
			break;
		}
	}

	// When we exit the infinite loop, we know we were sent an END command
	// so we need to wait for all the children threads to finish
	for (i = 0; i < numWorkerThreads; i++) {
		pthread_join(cons_tid[i], NULL);
	}
}

void initEverything(int numAccounts, int numThreads) {
	int i;
	
	initialize_accounts(numAccounts);
	pthread_mutex_init(&queue_mutex, NULL);
	globReqNum = 1;
	commands = NULL;
	accounts = malloc(numAccounts * sizeof(struct account));
	cons_tid = malloc(numThreads * sizeof(pthread_t));
	
	// Initialize all the mutexes for each account
	for (i = 0; i < numAccounts; i++) {
		pthread_mutex_init(&accounts[i].lock, NULL);
	}

	// create worker threads
  for (i = 0; i < numThreads; i++) {
    pthread_create(&cons_tid[i], NULL, workerThread, NULL);
  }
}

void * workerThread() {
	while (1) {
		struct node * currentCmd = NULL;
		char * cmd;
		int requestNum = 0;

		// TODO: this is busy waiting...(polling)
		while (currentCmd == NULL) {
			pthread_mutex_lock(&queue_mutex);

			// If there are any commands, get the head node,
			// and update the list to point to the next element
			if (commands != NULL) {
				// Return out of this process if the command is END
				// Leave that command in the queue so that all other threads
				// can learn about the end command
				if (strcmp("END\n", commands->cmd) == 0) {
					fprintf(stderr, "Received END command\n");
					return NULL;
				} else {
					// Otherwise get the command and update the queue
					currentCmd = commands;
					commands = commands->next;
					requestNum = globReqNum;
					globReqNum++;
				}
			}

			pthread_mutex_unlock(&queue_mutex);
		}

		// Get the command from the string
		cmd = strsep(&currentCmd->cmd, " ");

		// We now have the command. Now parse it and process it
		if (cmd != NULL && strlen(cmd) > 0) {
			if (!strcmp("CHECK", cmd)) {
				// Balance Check
				int account = 0;
				char * accountNum = strsep(&currentCmd->cmd, " ");

				if (accountNum != NULL) {
					account = atoi(accountNum);
					int accountBalance = read_account(account);
					pthread_mutex_lock(&accounts[account].lock);
					
					printf("ID %d\n", requestNum);
					
					flockfile(outputFile);
					fprintf(outputFile, "%d BAL %d", requestNum, accountBalance);
					
					struct timeval end;
					gettimeofday(&end);
					fprintf(outputFile, " TIME %d.%06d %d.%06d\n", currentCmd->startTime.tv_sec, currentCmd->startTime.tv_usec,
																												 end.tv_sec, end.tv_usec);
																												 
					fflush(outputFile);
					funlockfile(outputFile);

					pthread_mutex_unlock(&accounts[account].lock);
				} else {
					printf("ERROR: Unknown account number\n");
				}
			} else if (!strcmp("TRANS", cmd)) {
				// Transaction
				int i;
				int actualNumAccounts = 0;
				int insufficientFunds = 0;
				int account[10];
				int amount[10];
				int balances[10];
				char * accountNum[10];
				char * amountStr[10];
				
				for (i = 0; i < 10; i++) {
					accountNum[i] = NULL;
					amountStr[i] = NULL;
				}
				
				i = 0;
				
				// Get all account numbers and amounts
				while (((accountNum[i] = strsep(&currentCmd->cmd, " ")) != NULL) &&
							 ((amountStr[i]	 = strsep(&currentCmd->cmd, " ")) != NULL)) {
					fixString(amountStr[i]);
					
					account[i] = atoi(accountNum[i]);
					amount[i] = atoi(amountStr[i]);
					
					i++;
					actualNumAccounts++;
				}
				
				printf("ID %d\n", requestNum);
				
				// TODO: maybe sort by account number so that all threads
				// acquire in the same order, hopefully preventing a deadlock
				
				// Acquire all locks
				for (i = 0; i < actualNumAccounts; i++) {
					pthread_mutex_lock(&accounts[account[i]].lock);
				}
				
				// We have all locks - now perform Transactions
				for (i = 0; i < actualNumAccounts; i++) {
					balances[i] = (read_account(account[i]) + amount[i]);
					
					if (amount[i] < 0) {
						// We have a negative (withdrawal) value
						// Check to see if we have enough
						if (balances[i] < 0) {
							insufficientFunds = account[i];
							break;
						}
					}
				}
				
				flockfile(outputFile);
				if (!insufficientFunds) {
					// The whole transaction is good, so go through and 
					// commit the transaction
					for (i = 0; i < actualNumAccounts; i++) {
						write_account(account[i], balances[i]);
					}
					
					fprintf(outputFile, "%d OK", requestNum);
				} else {
					fprintf(outputFile, "%d ISF %d", requestNum, insufficientFunds);
				}

				struct timeval end;
				gettimeofday(&end);
				fprintf(outputFile, " TIME %d.%06d %d.%06d\n", currentCmd->startTime.tv_sec, currentCmd->startTime.tv_usec,
																											 end.tv_sec, end.tv_usec);

				fflush(outputFile);
				funlockfile(outputFile);
				
				// Release all locks
				for (i = 0; i < actualNumAccounts; i++) {
					pthread_mutex_unlock(&accounts[account[i]].lock);
				}
			} else {
				printf("ERROR: Invalid command\n");
			}
		}

		// Free the command string, and the command struct
		free(currentCmd->cmd);
		free(currentCmd);
	}
}

/**
 * Fix strings created by strsep.
 *
 * If this string ends with a \n, remove it so that it is
 * easier to work with.
 */
void fixString(char * str) {
	if ((str != NULL) && (str[strlen(str) - 1] == '\n')) {
		// strsep adds a \n to the end of the string
		str[strlen(str) - 1] = '\0';
	}
}
