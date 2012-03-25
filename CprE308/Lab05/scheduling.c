/*******************************************************************************
*
* CprE 308 Scheduling Lab
*
* scheduling.c
*******************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>
#define NUM_PROCESSES 20

struct process
{
  /* Values initialized for each process */
  int arrivaltime;  /* Time process arrives and wishes to start */
  int runtime;      /* Time process requires to complete job */
  int priority;     /* Priority of the process */

  /* Values algorithm may use to track processes */
  int starttime;
  int endtime;
  int flag;
  int remainingtime;
};

/* Forward declarations of Scheduling algorithms */
void first_come_first_served(struct process *proc);
void shortest_remaining_time(struct process *proc);
void round_robin(struct process *proc);
void round_robin_priority(struct process *proc);

int main()
{
  int i;
  struct process proc[NUM_PROCESSES],      /* List of processes */
                 proc_copy[NUM_PROCESSES]; /* Backup copy of processes */

  /* Seed random number generator */
  srand(time(0)); /* Use this seed to test different scenarios */
  //srand(0xC0FFEE);     /* Used for test to be printed out */

  /* Initialize process structures */
  for(i=0; i<NUM_PROCESSES; i++)
  {
    proc[i].arrivaltime = rand()%100;
    proc[i].runtime = (rand()%30)+10;
    proc[i].priority = rand()%3;
    proc[i].starttime = 0;
    proc[i].endtime = 0;
    proc[i].flag = 0;
    proc[i].remainingtime = 0;
  }

  /* Show process values */
  printf("Process\tarrival\truntime\tpriority\n");
  for(i=0; i<NUM_PROCESSES; i++)
    printf("%d\t%d\t%d\t%d\n", i, proc[i].arrivaltime, proc[i].runtime,
           proc[i].priority);

  /* Run scheduling algorithms */
  printf("\n\nFirst come first served\n");
  memcpy(proc_copy, proc, NUM_PROCESSES * sizeof(struct process));
  first_come_first_served(proc_copy);

  printf("\n\nShortest remaining time\n");
  memcpy(proc_copy, proc, NUM_PROCESSES * sizeof(struct process));
  shortest_remaining_time(proc_copy);

  printf("\n\nRound Robin\n");
  memcpy(proc_copy, proc, NUM_PROCESSES * sizeof(struct process));
  round_robin(proc_copy);

  printf("\n\nRound Robin with priority\n");
  memcpy(proc_copy, proc, NUM_PROCESSES * sizeof(struct process));
  round_robin_priority(proc_copy);

  return 0;
}

void first_come_first_served(struct process *proc)
{
	/* Implement scheduling algorithm here */
	int done = 0;
	int currentTime = 1;
	int sum = 0;

	while (!done) {
		int minTime = INT_MAX;
		int minIndex = -1;
		int i;

		for (i = 0; i < NUM_PROCESSES; i++) {
			if (!proc[i].flag) {
				if (proc[i].arrivaltime < minTime) {
					minTime = proc[i].arrivaltime;
					minIndex = i;
				}
			}
		}

		if (minIndex == -1) {
			done = 1;
			continue;
		}

		proc[minIndex].flag = 1;

		printf("Process %d started at time %d\n", minIndex, currentTime);
		currentTime += proc[minIndex].runtime;
		printf("Process %d finished at time %d\n", minIndex, currentTime);

		sum += (currentTime - minTime);
	}

	printf("Average time from arrival to finish is %d seconds\n", sum / NUM_PROCESSES);
}

void shortest_remaining_time(struct process *proc)
{
	/* Implement scheduling algorithm here */
	int currentTime = 1;
	int sum = 0;
	int completed = 0;
	int i;
	for (i = 0; i < NUM_PROCESSES; i++) {
		proc[i].remainingtime = proc[i].runtime;
	}

	while (completed < NUM_PROCESSES) {
		int minRemaining = INT_MAX;
		int minIndex = -1;

		for (i = 0; i < NUM_PROCESSES; i++) {
			if (!proc[i].flag) {
				// If the process hasn't finished, has arrived already
				// and has the least remaining time, store it
				if (proc[i].arrivaltime <= currentTime &&
					proc[i].remainingtime < minRemaining) {
					minRemaining = proc[i].remainingtime;
					minIndex = i;
				}
			}
		}

		// If the minIndex is -1, we couldn't find a process that has started
		// yet that still has time remaining
		if (minIndex == -1) {
			currentTime++;
			continue;
		}

		// Update the process's remaining time
		proc[minIndex].remainingtime--;

		// If we don't have a start time, set it to the current time
		if (proc[minIndex].starttime == 0) {
			proc[minIndex].starttime = currentTime;

			printf("Process %d started at time %d\n", minIndex, currentTime);
		}

		// Increment the current time
		currentTime++;

		// If we have one second left, set the flag since we will finish
		// before the next swap time period. Also, set the end time and
		// update the sum.
		if (proc[minIndex].remainingtime == 0) {
			proc[minIndex].flag = 1;
			proc[minIndex].endtime = currentTime;
			sum += (currentTime - proc[minIndex].starttime);
			completed++;

			printf("Process %d finished at time %d\n", minIndex, currentTime);
		}
	}

	printf("Average time from arrival to finish is %d seconds\n", sum / NUM_PROCESSES);
}

void round_robin(struct process *proc)
{
	/* Implement scheduling algorithm here */
	int currentTime = 1;
	int sum = 0;
	int completed = 0;
	int i;
	int prevCurrentTime = currentTime;

	for (i = 0; i < NUM_PROCESSES; i++) {
		proc[i].remainingtime = proc[i].runtime;
	}

	while (completed < NUM_PROCESSES) {
		prevCurrentTime = currentTime;

		for (i = 0; i < NUM_PROCESSES; i++) {
			if (!proc[i].flag) {
				// If the process hasn't finished, has arrived already
				// and has the least remaining time, store it
				if (proc[i].arrivaltime <= currentTime) {
					// Handle the start time initialization
					if (proc[i].starttime == 0) {
						proc[i].starttime = currentTime;
						printf("Process %d started at time %d\n", i, currentTime);
					}

					proc[i].remainingtime--;
					currentTime++;

					if (proc[i].remainingtime == 0) {
						proc[i].flag = 1;
						sum += (currentTime - proc[i].starttime);
						completed++;

						printf("Process %d finished at time %d\n", i, currentTime);
					}
				}
			}
		}

		// No processes ran in the previous iteration, so just increment current time
		if (prevCurrentTime == currentTime) {
			currentTime++;
		}
	}

	printf("Average time from arrival to finish is %d seconds\n", sum / NUM_PROCESSES);
}

void round_robin_priority(struct process *proc)
{
	/* Implement scheduling algorithm here */
	int currentTime = 1;
	int sum = 0;
	int completed = 0;
	int i;
	int prevCurrentTime = currentTime;

	for (i = 0; i < NUM_PROCESSES; i++) {
		proc[i].remainingtime = proc[i].runtime;
	}

	while (completed < NUM_PROCESSES) {
		int maxPriority = INT_MIN;
		int tempDone = 0;
		int secondMaxPriorityStart = INT_MAX;

		prevCurrentTime = currentTime;

		// Find the max priority of all the processes that have arrived so far
		for (i = 0; i < NUM_PROCESSES; i++) {
			if (!proc[i].flag && 
				(proc[i].arrivaltime <= currentTime) &&
				(proc[i].priority > maxPriority)) {
	
				maxPriority = proc[i].priority;
			}
		}

		// Find the next highest priority (if one exists) in all future processes
		// This should allow us to know how long we can run the current highest priority processes
		for (i = 0; i < NUM_PROCESSES; i++) {
			if (!proc[i].flag && 
				(proc[i].arrivaltime > currentTime) &&
				(proc[i].priority > maxPriority)) {

				// If this process with a higher priority is the earliest, keep it
				if ((proc[i].arrivaltime - currentTime) < secondMaxPriorityStart) {
					secondMaxPriorityStart = proc[i].arrivaltime;
				}
			}
		}

		// While our time is less than the start of the next highest priority level's start time
		// and we aren't done with the current priority level, do the round robin on the
		// current level's processes
		while ((currentTime < secondMaxPriorityStart) && !tempDone) {
			int countWorkedOn = 0;
			for (i = 0; i < NUM_PROCESSES; i++) {
				if (!proc[i].flag &&
					(proc[i].arrivaltime <= currentTime) &&
					(proc[i].priority == maxPriority)) {

					countWorkedOn++;
					// DO ROUND ROBIN...
					if (proc[i].starttime == 0) {
						proc[i].starttime = currentTime;
						printf("Process %d started at time %d\n", i, currentTime);
					}

					proc[i].remainingtime--;
					currentTime++;

					if (proc[i].remainingtime == 0) {
						proc[i].flag = 1;
						sum += (currentTime - proc[i].starttime);
						completed++;

						printf("Process %d finished at time %d\n", i, currentTime);
					}
				}
			}

			tempDone = (countWorkedOn > 0);
		}

		// No processes were ready, so just increment currentTime
		if (prevCurrentTime == currentTime) {
			currentTime++;
		}
	}

	printf("Average time from arrival to finish is %d seconds\n", sum / NUM_PROCESSES);
}

