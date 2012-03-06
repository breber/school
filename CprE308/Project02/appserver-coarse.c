/**
 * Brian Reber
 * CprE308 Project 2
 * Lab Section D
 * appserver-coarse.c
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include "Bank.h"

#define FALSE 0
#define TRUE 1

struct trans_account {
	int accountNum;
	int transAmount;
	int balance;
};

struct node {
	char * cmd;
	struct timeval startTime;
	struct node * next;
};

pthread_mutex_t queue_mutex;
pthread_mutex_t bank_mutex;
int globReqNum;

struct node * commands;
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

/**
 * Initialize all of the accounts and mutexes
 */
void initEverything(int numAccounts, int numThreads) {
	int i;
	
	initialize_accounts(numAccounts);
	pthread_mutex_init(&queue_mutex, NULL);
	pthread_mutex_init(&bank_mutex, NULL);
	globReqNum = 1;
	commands = NULL;
	cons_tid = malloc(numThreads * sizeof(pthread_t));

	// create worker threads
	for (i = 0; i < numThreads; i++) {
		pthread_create(&cons_tid[i], NULL, workerThread, NULL);
	}
}

/**
 * The worker threads that process the commands
 */
void * workerThread() {
	while (1) {
		struct node * currentCmd = NULL;
		char * cmd;
		int requestNum = 0;

		while (currentCmd == NULL) {
			pthread_mutex_lock(&queue_mutex);

			// If there are any commands, get the head node,
			// and update the list to point to the next element
			if (commands != NULL) {
				// Return out of this process if the command is END
				// Leave that command in the queue so that all other threads
				// can learn about the end command
				if (strcmp("END\n", commands->cmd) == 0) {                    
					pthread_mutex_unlock(&queue_mutex);
					return NULL;
				} else {
					// Otherwise get the command and update the queue
					currentCmd = commands;
					commands = commands->next;
					requestNum = globReqNum;
					globReqNum++;
				}
			} else {
				pthread_mutex_unlock(&queue_mutex);
			}
		}

		// Get the command from the string
		cmd = strsep(&currentCmd->cmd, " ");

		// We now have the command. Now parse it and process it
		if (cmd != NULL && strlen(cmd) > 0) {
			// Lock the entire bank
			pthread_mutex_lock(&bank_mutex);
			
			// We have locked the account, so it is now safe to 
			// unlock the queue mutex
			pthread_mutex_unlock(&queue_mutex);
			
			if (!strcmp("CHECK", cmd)) {
				// Balance Check
				int account = 0;
				char * accountNum = strsep(&currentCmd->cmd, " ");

				if (accountNum != NULL) {
					account = atoi(accountNum);

					int accountBalance = read_account(account);
					printf("ID %d\n", requestNum);
					
					flockfile(outputFile);
					fprintf(outputFile, "%d BAL %d", requestNum, accountBalance);
					
					struct timeval end;
					gettimeofday(&end);
					fprintf(outputFile, " TIME %d.%06d %d.%06d\n", currentCmd->startTime.tv_sec, currentCmd->startTime.tv_usec, end.tv_sec, end.tv_usec);

					fflush(outputFile);
					funlockfile(outputFile);
				} else {
					printf("ERROR: Unknown account number\n");
				}
			} else if (!strcmp("TRANS", cmd)) {
				// Transaction
				int i = 0;
				int actualNumAccounts = 0;
				int insufficientFunds = 0;
				struct trans_account account[10];
				char * accountNum = NULL;
				char * amountStr = NULL;
								
				// Get all account numbers and amounts
				while ((i < 10) && 
						((accountNum = strsep(&currentCmd->cmd, " ")) != NULL) &&
						((amountStr = strsep(&currentCmd->cmd, " ")) != NULL)) {
					fixString(amountStr);
					
					account[i].accountNum = atoi(accountNum);
					account[i].transAmount = atoi(amountStr);
					
					i++;
					actualNumAccounts++;
				}

				printf("ID %d\n", requestNum);

				// We have all locks - now perform Transactions
				for (i = 0; i < actualNumAccounts; i++) {
					account[i].balance = (read_account(account[i].accountNum) + account[i].transAmount);
					
					if (account[i].balance < 0) {
						// We have a negative (withdrawal) value
						// Check to see if we have enough
						if (account[i].balance < 0) {
							insufficientFunds = account[i].accountNum;
							break;
						}
					}
				}
				
				flockfile(outputFile);
				if (!insufficientFunds) {
					// The whole transaction is good, so go through and 
					// commit the transaction
					for (i = 0; i < actualNumAccounts; i++) {
						write_account(account[i].accountNum, account[i].balance);
					}
					
					fprintf(outputFile, "%d OK", requestNum);
				} else {
					fprintf(outputFile, "%d ISF %d", requestNum, insufficientFunds);
				}

				struct timeval end;
				gettimeofday(&end);
				fprintf(outputFile, " TIME %d.%06d %d.%06d\n", currentCmd->startTime.tv_sec, currentCmd->startTime.tv_usec, end.tv_sec, end.tv_usec);

				fflush(outputFile);
				funlockfile(outputFile);
			} else {
				printf("ERROR: Invalid command\n");
			}
			
			// Unlock the entire bank
			pthread_mutex_unlock(&bank_mutex);
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
