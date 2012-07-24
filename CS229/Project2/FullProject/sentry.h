/**
 * sentry.h
 *
 * @author Brian Reber
 *
 * A robot brain that finds the edge of the simulation, and moves
 * around it until it reaches its starting point, at which time it 
 * rotates 180 degrees, and repeats.
 */
#ifndef SENTRYBRAIN_H
#define SENTRYBRAIN_H

#include "brain.h"

#define START 0
#define MOVE 1
#define AT_START 2
#define FINISHED_AT_START 3
#define PASSING_R 4
#define PASSING_L 5

using namespace std;

class Sentry : public Brain {
	public:
		/**
		 * Provide a virtual deconstructor for the virtual method.
		 * Doesn't need to do anything because we don't have any variables
		 */
		virtual ~Sentry() { }
	
		/**
		 * Given the robot's current position, and its surroundings, we make a decision about
		 * what the robot should do on the next turn.
		 *
		 * @param objectsSeen - a list of the objects in the 8 surrounding locations
		 * @param distances[] - an array of distances of probable objects probed on the last turn.
		 *				The size of this array is probeWidth.
		 */
		void makeMoveDecision(vector<Location *> objectsSeen, int distances[]);

		/**
		 * Checks to see if it is a good idea for the robot to turn right when trying to pass an
		 * obstacle that is in the way.
		 *
		 * @param objectsSeen - a list of the objects in the 8 surrounding locations
		 */
		bool canMoveRight(vector<Location *> objectsSeen) const;
	
		/**
		 * Finds the direction of the closest wall to the robot
		 *
		 * @return the location (as defined in Object.h) of the closest wall
		 */
		int findClosestWall() const;
};

#endif
