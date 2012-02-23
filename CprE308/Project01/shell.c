/**
 * Brian Reber
 * CprE308 Project 1
 * Lab Section D
 * shell.c
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#include <errno.h>
#include <fcntl.h>

#define FALSE 0
#define TRUE 1
#define MAX_ARGS 25

// Struct to hold Node information
typedef struct pid_name {
	char * name;
	pid_t pid;
	struct pid_name * next;
} pid_name;

void handleExec(char *, char *, char *);
pid_name * getAndRemovePid(pid_t);
void addPid(pid_t, char *);
void fixString(char *);

// The linked-list containing the information about
// child processes we spawn in the background
pid_name * nodes = NULL;

int main(int argc, char ** argv) {
	char * prompt;

	// Parse arguments
	if (argc < 2) {
		// The user didn't specify any arguments, so use the default values
		prompt = "308sh>";
	} else {
		int i;
		for (i = 1; i < argc; i++) {
			if (!strcmp("-p", argv[i])) {
				i++;
				prompt = (char *) malloc(strlen(argv[i]) * sizeof( char ));
				strcpy(prompt, argv[i]);
			} else {
				fprintf(stderr, "Unknown argument: %s\n", argv[i]);
			}
		}
	}

	// The main loop for our shell
	while (1) {
		int index = 0;
		int status = 0;
		pid_t processId = 0;
		size_t numBytes = 200;
		char * line = malloc(numBytes * sizeof(char));
		char * beginLine = line;
		char * fullLine = malloc(numBytes * sizeof(char));
		char * cmd;

		// Print out the promt, and wait for the user input
		printf("%s ", prompt);
		getline(&line, &numBytes, stdin);

		// Copy line into fullLine
		strcpy(fullLine, line);
		fixString(fullLine);

		// Get the command from the string
		cmd = strsep(&line, " ");

		// strsep will put a \n at the end of the string if it is the last
		// item on the current line. Remove it.
		fixString(cmd);

		if (cmd != NULL && strlen(cmd) > 0) {
			// All if/elseif are built-in commands
			// The else is any other commands
			if (!strcmp("exit", cmd)) {
				// user typed exit command
				exit(EXIT_SUCCESS);
			} else if (!strcmp("pid", cmd)) {
				// user typed pid command
				printf("%d\n", getpid());
			} else if (!strcmp("ppid", cmd)) {
				// user typed ppid command
				printf("%d\n", getppid());
			} else if (!strcmp("cd", cmd)) {
				// user typed cd command
				char * dir = strsep(&line, " ");
				
				if (dir != NULL) {
					fixString(dir);
					chdir(dir);
				} else {
					chdir(getenv("HOME"));
				}
			} else if (!strcmp("pwd", cmd)) {
				// user typed pwd command
				char * pwdBuf =	getcwd(NULL, 0);
				printf("%s\n", pwdBuf);
				
				if (pwdBuf != NULL) {
					free(pwdBuf);
				}
			} else if (!strcmp("set", cmd)) {
				// user typed set command
				char * varName = strsep(&line, " ");
				
				if (varName != NULL) {
					char * varVal = strsep(&line, " ");
					fixString(varName);
				
					if (varVal == NULL) {
						// Only one argument - clear that variable
						unsetenv(varName);
					} else {
						fixString(varVal);
						setenv(varName, varVal, 1);
					}
				} else {
					// User didn't specify an env var to get...
					fprintf(stderr, "Missing environmental variable to set...\n");
				}
			} else if (!strcmp("get", cmd)) {
				// user typed get command
				char * varName = strsep(&line, " ");
							
				if (varName != NULL) {
					char * val;
					fixString(varName);
					val = getenv(varName);
					
					if (val != NULL) {
						printf("%s\n", val);
					}
				} else {
					// User didn't specify an env var to get...
					fprintf(stderr, "Missing environmental variable...\n");
				}
			} else {
				// All other commands we will execute using the exec family
				// of functions
				handleExec(cmd, line, fullLine);
			}
		}

		// Check for background processes
		processId = waitpid(-1, &status, WNOHANG);

		// If we found one, print out its status
		if (processId > 0) {
			// Get the name from the global linked-list
			pid_name * proc = getAndRemovePid(processId);

			if (WIFSIGNALED(status)) {
				printf("[%d] %s Killed (%d)\n", processId, proc->name, status);
			} else {
				printf("[%d] %s Exit (%d)\n", processId, proc->name, status);
			}
			
			// Free the space we created for the name, and the actual
			// struct itself
			free(proc->name);
			free(proc);
		}

		// Free the memory we allocated for the command line text
		if (beginLine != NULL) {
			free(beginLine);
		}
		if (fullLine != NULL) {
			free(fullLine);
		}
	}
}

/**
 * Handle all commands other than the 'built-in' commands.
 *
 * Forks and execs a command, waiting for it to return
 * if not supposed to run as a background process.
 */
void handleExec(char * cmd, char * line, char * fullLine) {
	int status;
	int child = fork();
	if (child == 0) {
		int ptrCnt = 0;
		char * args[MAX_ARGS];
		char * tmp = NULL;
		char * fileName = NULL;
		int fileBool = FALSE;
	
		for (ptrCnt = 0; ptrCnt < MAX_ARGS; ptrCnt++) {
			args[ptrCnt] = NULL;
		}
	
		// The first argument should be the command itself
		ptrCnt = 0;
		args[ptrCnt++] = cmd;

		// Read parameters and add them to the argument array
		while ((tmp = strsep(&line, " ")) && (ptrCnt < MAX_ARGS)) {
			fixString(tmp);
			
			if (strcmp(tmp, ">") == 0) {
				// We found a > in our command - start handling redirection
				// logic by setting a flag saying that the next token will
				// be the name of the file to redirect to
				fileBool = TRUE;
			} else if (fileBool) {
				// We are past the > for redirection
				// Get the file name and then set fileBool to FALSE
				fileName = tmp;
				fileBool = FALSE;
			} else {
				args[ptrCnt++] = tmp;
			}
		}

		// If the last argument is &, strip it
		if (strcmp(args[ptrCnt - 1], "&") == 0) {
			args[--ptrCnt] = NULL;
		}

		// Print out the pid of this process, and the command to execute
		printf("[%d] %s\n", getpid(), cmd);
	
		// If we have a valie fileName pointer, open the file, and redirect
		// the output from stdout (of this process) to the file
		if (fileName != NULL) {
			int file = open(fileName, O_WRONLY | O_TRUNC | O_CREAT);
			int result = dup2(file, fileno(stdout));
			if (result == -1) {
				fprintf(stderr, "Error redirecting: %s\n", strerror( errno ));
			}
		}
		
		// Try to execute the command
		execvp(cmd, args);
	
		// If we get here, there was a problem
		// Print out that there was a problem, and exit with a Failure status
		fprintf(stderr, "Cannot exec %s\n", cmd);
		exit(EXIT_FAILURE);
	} else {
		// Wait for our child to finish, and print out the status
		char * indexOfAmp = strrchr(fullLine, '&');
		int actualIndex = indexOfAmp - fullLine + 1;

		if ((indexOfAmp == NULL) || (actualIndex != strlen(fullLine))) {
			do {
			 	waitpid(child, &status, 0);
			} while (!WIFEXITED(status) && !WIFSIGNALED(status));
		
			printf("[%d] %s Exit (%d)\n", child, cmd, status);
		} else {
			// Add the background process to our list
			addPid(child, cmd);

			// Sleep for a second to let the background process
			// print out its pid
			sleep(1);
		}
	}
}

/**
 * Get the pid_name that corresponds to the given pid
 * from our global linked list. Update the linked list
 * to no longer contain a reference to the returned pid_name.
 *
 * NOTE: it is up to the caller to free the pid_name->name and
 *		 the pid_name itself.
 */
pid_name * getAndRemovePid(pid_t pid) {
	pid_name * current = nodes;
	pid_name * previous = NULL;

	if (current == NULL) {
		return NULL;
	}

	while (current != NULL) {
		if (current->pid == pid) {
			if (previous != NULL) {
				previous->next = current->next;
			} else {
				nodes = current->next;
			}

			return current;
		}
		
		previous = current;
		current = current->next;
	}

	return NULL;
}

/**
 * Add a pid to our global linked list
 */
void addPid(pid_t pid, char * name) {
	if (nodes == NULL) {
		nodes = malloc(sizeof(pid_name));
		nodes->pid = pid;
		// We have to copy the name onto the heap
		nodes->name = malloc(strlen(name) * sizeof(char));
		strcpy(nodes->name, name);
		nodes->next = NULL;
	} else {
		pid_name * current = nodes;
		while (current->next != NULL) {
			current = current->next;
		}

		current->next = malloc(sizeof(pid_name));
		current->next->pid = pid;

		// Copy the name onto the heap
		current->next->name = malloc(strlen(name) * sizeof(char));
		strcpy(current->next->name, name);
		current->next->next = NULL;
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

