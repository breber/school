/**
 * pilladdict.cpp
 *
 * @author Brian Reber
 *
 * A robot brain that navigates the simulation in order to find pills
 */
#include "pilladdict.h"
#include <iostream>

int main(int argc, char *argv[]) {
	string temp;
	int messageCode = -1;

	Brain *pill = new PillAddict();

	vector<Location *> objectsSeen;
	
	int numTurnsCompleted = 0;
	
	while (true) {
		cin >> messageCode;
		
		switch (messageCode) {
			// Initialization
			case 0: {
				pill->parseInitialization();
				break;
			}
			// Move Request
			case 1: {
				int distances[pill->getProbeWidth()];
				
				string success;
				cin >> success;
				
				pill->parseMoveRequest(&objectsSeen, distances, numTurnsCompleted);
								
				numTurnsCompleted++;
				pill->makeMoveDecision(objectsSeen, distances);
				
				break;				
			} 
			// Termination request
			case 2: {
				return 0;
			}
			default:
				break;
		}
		messageCode = 0;
	}
	
	return 0;
}

/**
 * Given the robot's current position, and its surroundings, we make a decision about
 * what the robot should do on the next turn.
 *
 * @param objectsSeen - a list of the objects in the 8 surrounding locations
 * @param distances[] - an array of distances of probable objects probed on the last turn.
 *				The size of this array is probeWidth.
 */
void PillAddict::makeMoveDecision(vector<Location *> objectsSeen, int distances[]) {
	static queue<func> funcPoint;
		
	// If there are actions that we still need to complete, do those
	// first before starting to do other things
	if (funcPoint.size() > 0) {
		func toCall = funcPoint.front();
		funcPoint.pop();
		bool callFunction = true;
		
		// If we see objects, check it first.
		// If there is one in the way, we will
		// clear the queue and continue on to the
		// rest of the function
		if (objectsSeen.size() > 0) {
			callFunction = checkForObstacles(objectsSeen);
		}
		
		if (callFunction) {
			(this->*toCall)();
			
			return;
		} else {
			while (!funcPoint.empty()) {
				funcPoint.pop();
			}
		}
	}
	
	if (objectsSeen.size() > 0) {
		// We can see objects, so we will see if there are any
		// energy pills around us.  If so, we will definitely want
		// to pick them up.
		for (unsigned int i = 0; i < objectsSeen.size(); i++) {
			if (objectsSeen[i]->getConfigType() == "energy-pill") {
				turnTowardsPill(objectsSeen[i]->getXLoc(), objectsSeen[i]->getYLoc(), funcPoint);
				return;
			}
		}
	}
	
	// Now that we have finished with all the calls in our queue, and finished
	// looking at all the cells around us, we need to start probing and finding 
	// the other pills on the board
	
	// For this first implementation, we will basically probe, and if we see anything
	// we will start moving towards it until we get close enough for the objects seen
	// part to handle it
	
	// We will first see if we probed last time, and check the results there
	if (probeWidth > 0) {
		int lowest = -1;
		
		// Find the smallest distance greater than 1
		for (int i = 0; i < probeWidth; i++) {
			if ((distances[i] < lowest || lowest == -1) && distances[i] > 1) {
				lowest = distances[i];
			}
		}
		
		// If we have a distance greater than 1, move forward
		// and continue on to the part where we put function 
		// calls into the queue
		if (lowest > 1) {
			moveForward();
		} else {
			// If we didn't find anything in this probe,
			// rotate 90 degrees, so that we can probe next time
			rotate90();
			return;
		}
		
		// Put the smallest distance number of
		// move forward calls into the function call
		// queue to be executed
		for (int i = 1; i < lowest - 1; i++) {
			funcPoint.push(&PillAddict::moveForward);
		}
		return;
	}
	
	// If we make it to this point in the function, we didn't probe last
	// turn, so we should probably probe this time
	probe(3);
}

/**
 * Turns the robot towards the given location
 *
 * @param xloc - the x location to point the front towards
 * @param yloc - the y location to point the front towards
 * @param &funPoint - a queue of function pointers, representing
 *				calls that need to be made on the following turns. If turning 
 *				towards the pill requires more than one turn, we will add the
 *				appropriate function pointer to the queue.
 */
void PillAddict::turnTowardsPill(int xloc, int yloc, queue<func> &funcPoint) {
	if (yloc == robot->getYLoc() && xloc == (robot->getXLoc() + 1) && robot->getDir() != EAST) {
		// To the right
		rotate(90);
	} else if (yloc == robot->getYLoc() && xloc == (robot->getXLoc() - 1) && robot->getDir() != WEST) {
		// To the left
		rotate(-90);
	} else if (yloc == (robot->getYLoc() + 1) && xloc == robot->getXLoc() && robot->getDir() != SOUTH) {
		// Below
		rotate(-90);
		funcPoint.push(&PillAddict::rotateN90);
		funcPoint.push(&PillAddict::moveForward);
	} else if (yloc == (robot->getYLoc() + 1) && xloc == (robot->getXLoc() + 1)) {
		// Bottom Right
		if (robot->getDir() == EAST) {
			moveForward();
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == NORTH) {
			rotate90();
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == WEST) {
			rotate90();
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == SOUTH) {
			rotateN90();
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
		} 
	} else if (yloc == (robot->getYLoc() + 1) && xloc == (robot->getXLoc() - 1)) {
		// Bottom Left
		if (robot->getDir() == WEST) {
			moveForward();
			funcPoint.push(&PillAddict::rotateN90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == NORTH) {
			rotateN90();
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotateN90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == EAST) {
			rotate90();
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotateN90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == SOUTH) {
			rotate90();
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotateN90);
			funcPoint.push(&PillAddict::moveForward);
		} 
	} else if (yloc == (robot->getYLoc() - 1) && xloc == (robot->getXLoc() - 1)) {
		// Upper Left
		if (robot->getDir() == WEST) {
			moveForward();
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == NORTH) {
			rotateN90();
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == EAST) {
			rotate90();
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == SOUTH) {
			rotate90();
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
		} 
	} else if (yloc == (robot->getYLoc() - 1) && xloc == (robot->getXLoc() + 1)) {
		// Upper Right
		if (robot->getDir() == EAST) {
			moveForward();
			funcPoint.push(&PillAddict::rotateN90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == NORTH) {
			rotate90();
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotateN90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == WEST) {
			rotate90();
			funcPoint.push(&PillAddict::rotate90);
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotateN90);
			funcPoint.push(&PillAddict::moveForward);
		} else if (robot->getDir() == SOUTH) {
			rotateN90();
			funcPoint.push(&PillAddict::moveForward);
			funcPoint.push(&PillAddict::rotateN90);
			funcPoint.push(&PillAddict::moveForward);
		} 
	}
}

/**
 * Checks for 'dangerous' obstacles immediately in front of the robot,
 * and provides advice as to whether to go forward or not
 *
 * @param objectsSeen - a list of seen objects
 * @return whether or not it recommends moving forward
 */
bool PillAddict::checkForObstacles(vector<Location *> objectsSeen) const {
	for (unsigned int i = 0; i < objectsSeen.size(); i++) {
		if (robot->getDir() == NORTH) {
			if ((objectsSeen[i]->getXLoc() == robot->getXLoc()) && (objectsSeen[i]->getYLoc() < robot->getYLoc())) {
				if (Brain::isDangerous(objectsSeen[i]->getConfigType())) {
					return false;
				}
			}
		}
		
		if (robot->getDir() == SOUTH) {
			if ((objectsSeen[i]->getXLoc() == robot->getXLoc()) && (objectsSeen[i]->getYLoc() > robot->getYLoc())) {
				if (isDangerous(objectsSeen[i]->getConfigType())) {
					return false;
				}
			}
		}
		
		if (robot->getDir() == WEST) {
			if ((objectsSeen[i]->getXLoc() < robot->getXLoc()) && (objectsSeen[i]->getYLoc() == robot->getYLoc())) {
				if (isDangerous(objectsSeen[i]->getConfigType())) {
					return false;
				}
			}
		}
		
		if (robot->getDir() == EAST) {
			if ((objectsSeen[i]->getXLoc() > robot->getXLoc()) && (objectsSeen[i]->getYLoc() == robot->getYLoc())) {
				if (isDangerous(objectsSeen[i]->getConfigType())) {
					return false;
				}
			}
		}
	}
	
	return true;
}
