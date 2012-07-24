/**
 * sentry.cpp
 *
 * @author Brian Reber
 *
 * A robot brain that finds the edge of the simulation, and moves
 * around it until it reaches its starting point, at which time it 
 * rotates 180 degrees, and repeats.
 */
#include "sentry.h"
#include <iostream>

int main(int argc, char *argv[]) {
	string temp;
	int messageCode = -1;

	Brain *brain = new Sentry();
	
	vector<Location *> objectsSeen;
	
	int numTurnsCompleted = 0;
	
	while (true) {
		cin >> messageCode;
		
		switch (messageCode) {
			// Initialization
			case 0: {
				brain->parseInitialization();
				break;
			}
			// Move Request
			case 1: {
				int distances[brain->getProbeWidth()];
				
				string success;
				cin >> success;
				
				brain->parseMoveRequest(&objectsSeen, distances, numTurnsCompleted);
				
				numTurnsCompleted++;
				brain->makeMoveDecision(objectsSeen, distances);
				
				break;				
			} 
			// Termination Signal
			case 2: {
				return 0;
			} default:
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
void Sentry::makeMoveDecision(vector<Location *> objectsSeen, int distances[]) {
	// Find the closest wall
	int dirOfClosestWall = findClosestWall();
	static int state = 0;
	
	// The location we started moving around the edges
	static int startX = 0;
	static int startY = 0;
	static int turnNum = 0;
	
	static int passingState = 0;
	
	int wallTest = isAtWall();
	
	if (wallTest || passingState > 0) {
		switch (state) {
			case START: {
				// First time we hit a wall, we will rotate +90
				rotate(90);
								
				// Save ths starting position
				startX = robot->getXLoc();
				startY = robot->getYLoc();
				state = MOVE;
				turnNum++;
				break;
			}
			case MOVE: {
				if (turnNum > 2 && robot->getXLoc() == startX && robot->getYLoc() == startY) {
					// We are back to the starting position, start turning
					state = AT_START;
					rotate(90);
				} else if ((wallTest & T_WALL) && (wallTest & L_WALL)) {
					// Upper Left
					if (robot->getDir() == NORTH) {
						rotate(90);
					} else if (robot->getDir() == WEST) {
						rotate(-90);
					} else {
						moveForward();
					}
				} else if ((wallTest & T_WALL) && (wallTest & R_WALL)) {
					// Upper Right
					if (robot->getDir() == NORTH) {
						rotate(-90);
					} else if (robot->getDir() == EAST) {
						rotate(90);
					} else {
						moveForward();
					}
				} else if ((wallTest & B_WALL) && (wallTest & R_WALL)) {
					// Bottom Right
					if (robot->getDir() == SOUTH) {
						rotate(90);
					} else if (robot->getDir() == EAST) {
						rotate(-90);
					} else {
						moveForward();
					}
				} else if ((wallTest & B_WALL) && (wallTest & L_WALL)) {
					// Bottom Left
					if (robot->getDir() == SOUTH) {
						rotate(-90);
					} else if (robot->getDir() == WEST) {
						rotate(90);
					} else {
						moveForward();
					}
				} else {
					// Check to see if we have an obstacle in front of us
					int nextLocX = (robot->getDir() == NORTH || robot->getDir() == SOUTH) ? robot->getXLoc() :
						((robot->getDir() == WEST) ? (robot->getXLoc() - 1): (robot->getXLoc() + 1));
					int nextLocY = (robot->getDir() == WEST || robot->getDir() == EAST) ? robot->getYLoc() :
						((robot->getDir() == NORTH) ? (robot->getYLoc() - 1): (robot->getYLoc() + 1));
					bool goForward = true;
					
					for (unsigned int i = 0; i < objectsSeen.size(); i++) {
						if (objectsSeen[i]->getXLoc() == nextLocX && objectsSeen[i]->getYLoc() == nextLocY) {
							goForward = false;
						}
					}
					
					if (goForward) {
						// It is safe to move forward
						moveForward();
					} else {
						// Something is preventing us from moving forward (obstacle).
						// We will try to pass it
						if (canMoveRight(objectsSeen)) {
							rotate(90);
							state = PASSING_R;
						} else {
							rotate(-90);
							state = PASSING_L;
						}
					}
				}
				
				turnNum++;
				
				break;
			}
			case AT_START: {
				// We have made it to the starting position, so we will rotate
				// 90 degrees, and then the next turn we will rotate the other 
				// 90 degrees to start making our way back
				rotate(90);
				state = FINISHED_AT_START;
				break;
			}
			case FINISHED_AT_START: {
				// We are halfway through our turn, so finish the turn,
				// and then set the state so we are ready to move forward
				moveForward();
				state = MOVE;
				break;
			}
			case PASSING_L: {
				// We are passing an object by turning left, so we need to 
				// move forward one space, and then rotate left
				if (passingState == 0) {
					moveForward();
					passingState++;
				} else if (passingState == 1) {
					rotate(90);
					passingState++;
				} else if (passingState == 2) {
					moveForward();
					passingState++;
				} else if (passingState == 3) {
					moveForward();
					passingState++;
				} else if (passingState == 4) {
					rotate(90);
					passingState++;
				} else if (passingState == 5) {
					moveForward();
					passingState++;
				} else if (passingState == 6) {
					rotate(-90);
					passingState = 0;
					state = MOVE;
				} 
				break;
			}
			case PASSING_R: {
				// We are passing an object by turning right, so we need to 
				// move forward one space, and then rotate left
				if (passingState == 0) {
					moveForward();
					passingState++;
				} else if (passingState == 1) {
					rotate(-90);
					passingState++;
				} else if (passingState == 2) {
					moveForward();
					passingState++;
				} else if (passingState == 3) {
					moveForward();
					passingState++;
				} else if (passingState == 4) {
					rotate(-90);
					passingState++;
				} else if (passingState == 5) {
					moveForward();
					passingState++;
				} else if (passingState == 6) {
					rotate(90);
					passingState = 0;
					state = MOVE;
				} 
				break;
			}
		}
	} else if (dirOfClosestWall == robot->getDir()) {
		// Start moving forward
		moveForward();
	} else if ((dirOfClosestWall - robot->getDir()) % 360 == 180) {
		// We are 180 degrees away from the closest wall, so we need to make two 90 degree turns
		rotate(90);
	} else {		
		if ((dirOfClosestWall - robot->getDir()) == 270) {
			rotate(-90);
		} else {
			rotate(90);
		}
	}
}

/**
 * Checks to see if it is a good idea for the robot to turn right when trying to pass an
 * obstacle that is in the way.
 *
 * @param objectsSeen - a list of the objects in the 8 surrounding locations
 */
bool Sentry::canMoveRight(vector<Location *> objectsSeen) const {
	if (robot->getDir() == NORTH) {
		if (robot->getXLoc() != (simX - 1)) {
			for (unsigned int i = 0; i < objectsSeen.size(); i++) {
				if (objectsSeen[i]->getXLoc() == (robot->getXLoc() + 1)  && objectsSeen[i]->getYLoc() == robot->getYLoc()) {
					return false;
				}
			}
		} else {
			return false;
		}
	} else if (robot->getDir() == SOUTH) {
		if (robot->getXLoc() != 0) {
			for (unsigned int i = 0; i < objectsSeen.size(); i++) {
				if (objectsSeen[i]->getXLoc() == (robot->getXLoc() - 1)  && objectsSeen[i]->getYLoc() == robot->getYLoc()) {
					return false;
				}
			}
		} else {
			return false;
		}
	} else if (robot->getDir() == WEST) {
		if (robot->getYLoc() != 0) {
			for (unsigned int i = 0; i < objectsSeen.size(); i++) {
				if (objectsSeen[i]->getXLoc() == robot->getXLoc()  && objectsSeen[i]->getYLoc() == (robot->getYLoc() - 1)) {
					return false;
				}
			}
		} else {
			return false;
		}
	} else if (robot->getDir() == EAST) {
		if (robot->getYLoc() != (simY - 1)) {
			for (unsigned int i = 0; i < objectsSeen.size(); i++) {
				if (objectsSeen[i]->getXLoc() == robot->getXLoc()  && objectsSeen[i]->getYLoc() == (robot->getYLoc() + 1)) {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	return true;
}

/**
 * Finds the direction of the closest wall to the robot
 *
 * @return the location (as defined in Object.h) of the closest wall
 */
int Sentry::findClosestWall() const {
	int least = -1;
	int dirLeast = 0;
	
	if (simX - robot->getXLoc() < robot->getXLoc()) {
		dirLeast = EAST;
		least = (simX - robot->getXLoc());
	} else {
		dirLeast = WEST;
		least = robot->getXLoc();
	}
	
	if ((simY - robot->getYLoc()) < robot->getYLoc() && (simY - robot->getYLoc()) < least) {
		dirLeast = SOUTH;
		least = (simY - robot->getYLoc());
	} else if (robot->getYLoc() < least) {
		dirLeast = NORTH;
		least = robot->getYLoc();
	}
	
	return dirLeast;
}
