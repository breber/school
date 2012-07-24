/**
 * tribot.cpp
 *
 * @author Brian Reber
 *
 * A robot brain that tries to get all blocks of its color into 
 * the endzone matching its color
 */
#include "tribot.h"
#include <iostream>

int main(int argc, char *argv[]) {
	string temp;
	int messageCode = -1;

	Brain *tribot = new TriBot();

	vector<Location *> objectsSeen;
	
	int numTurnsCompleted = 0;
	
	while (true) {
		cin >> messageCode;
		
		switch (messageCode) {
			// Initialization
			case 0: {
				tribot->parseInitialization();
				break;
			}
			// Move Request
			case 1: {
				int distances[tribot->getProbeWidth()];
				
				string success;
				cin >> success;
				
				tribot->parseMoveRequest(&objectsSeen, distances, numTurnsCompleted);
								
				numTurnsCompleted++;
				tribot->makeMoveDecision(objectsSeen, distances);
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
void TriBot::makeMoveDecision(vector<Location *> objectsSeen, int distances[]) {
	static queue<func> funcPoint;
	
	static int dirOfSide = 0;
	
	int dirOfClosestWall = findClosestWall();
	
	// If there are actions that we still need to complete, do those
	// first before starting to do other things
	if (funcPoint.size() > 0) {
		func toCall = funcPoint.front();
		funcPoint.pop();
		bool callFunction = true;
		
		if (callFunction) {
			(this->*toCall)();
			
			return;
		} else {
			while (!funcPoint.empty()) {
				funcPoint.pop();
			}
		}
	}
	
	// If we don't know which side is the one with our color
	// And we aren't poining to the closest side, rotate to point
	// to the closest side in order to move over there and
	// figure out if it is the correct side
	if (dirOfSide == 0 && dirOfClosestWall != robot->getDir()) {
		if ((dirOfClosestWall - robot->getDir()) % 360 == 180) {
			// We are 180 degrees away from the closest wall, so we need to make two 90 degree turns
			rotate(90);
			funcPoint.push(&TriBot::rotate90);
		} else {
			if ((dirOfClosestWall - robot->getDir()) == 270) {
				rotate(-90);
			} else {
				rotate(90);
			}
		}
		return;
	} else if (dirOfSide == 0 && dirOfClosestWall == robot->getDir()) {
		// We still don't know which side has our color, but
		// we are pointing in the direction of the wall, so try and move that way
		// in order to figure out if it is the correct side
				
		// We should be able to see the rocks if we are this close
		if (robot->getXLoc() == 1 || robot->getXLoc() == simX - 2) {
			for (unsigned int i = 0; i < objectsSeen.size(); i++) {
				if (objectsSeen[i]->getConfigType() == "earth-rock") {
					// We have found an earth rock. Now to check if it is the same
					// color as us
					
					if (((Object *) objectsSeen[i])->getColor() == robot->getColor()) {
						dirOfSide = robot->getDir();
					} else {
						if (robot->getDir() == EAST) {
							dirOfSide = WEST;
						} else if (robot->getDir() == WEST) {
							dirOfSide = EAST;
						}
					}
				}
			}
		}
		
		if (!dirOfSide) {
			// We still don't know which side has our color,
			// so move towards the closest side
			moveForward();
			return;
		} else {
			// We know which side has our color, so we will rotate 180
			rotate(90);
			funcPoint.push(&TriBot::rotate90);
		}
	}
	
	if (objectsSeen.size() > 0) {
		// We can see objects, so we will see if there are any
		// energy pills around us.  If so, we will definitely want
		// to pick them up.
		for (unsigned int i = 0; i < objectsSeen.size(); i++) {
			if (objectsSeen[i]->getConfigType() == "block") {
				if (objectsSeen[i]->getColor() == robot->getColor()) {
					// We have found a block of our color
				}
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
			funcPoint.push(&TriBot::moveForward);
		}
		return;
	}
	
	// If we make it to this point in the function, we didn't probe last
	// turn, so we should probably probe this time
	probe(3);
}

/**
 * Finds the direction of the closest wall (either EAST or WEST) to the robot
 *
 * @return the location (as defined in Object.h) of the closest wall
 */
int TriBot::findClosestWall() const {
	int dirLeast = 0;
	
	if (simX - robot->getXLoc() < robot->getXLoc()) {
		dirLeast = EAST;
	} else {
		dirLeast = WEST;
	}
	
	return dirLeast;
}
