/**
 * tribot.h
 *
 * @author Brian Reber
 *
 * A robot brain that tries to get all blocks of its color into 
 * the endzone matching its color
 */
#ifndef TRIBOT_H
#define TRIBOT_H

#include "brain.h"
#include <iostream>

using namespace std;

class TriBot : public Brain {
	private:
		typedef void (TriBot::*func)();
	public:
		virtual ~TriBot() { }
	
		/**
		 * Given the robot's current position, and its surroundings, we make a decision about
		 * what the robot should do on the next turn.
		 *
		 * @param objectsSeen - a list of the objects in the 8 surrounding locations
		 * @param distances[] - an array of distances of probable objects probed on the last turn.
		 *				The size of this array is probeWidth.
		 */
		virtual void makeMoveDecision(vector<Location *> objectsSeen, int distances[]);
		
		/**
		 * Finds the direction of the closest wall to the robot
		 *
		 * @return the location (as defined in Object.h) of the closest wall
		 */
		int findClosestWall() const;
	
		/**
		 * A simple function that will rotate the robot by 90 degrees
		 */
		void rotate90() {
			rotate(90);
		}
	
		/**
		 * A simple function that will rotate the robot by -90 degrees
		 */
		void rotateN90() {
			rotate(-90);
		}
};

#endif
