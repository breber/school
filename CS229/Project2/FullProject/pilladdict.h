/**
 * pilladdict.h
 *
 * @author Brian Reber
 *
 * A robot brain that navigates the simulation in order to find pills
 */
#ifndef PILLADDICT_H
#define PILLADDICT_H

#include "brain.h"
#include <iostream>

using namespace std;

class PillAddict : public Brain {
	private:
		/**
		 * Define a type of function pointers for the current class
		 */
		typedef void (PillAddict::*func)();
	public:
		/**
		 * Provide a virtual deconstructor for the virtual method
		 * Doesn't need to do anything because there aren't any variables
		 */
		virtual ~PillAddict() { }
	
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
		 * Turns the robot towards the given location
		 *
		 * @param xloc - the x location to point the front towards
		 * @param yloc - the y location to point the front towards
		 * @param &funPoint - a queue of function pointers, representing
		 *				calls that need to be made on the following turns. If turning 
		 *				towards the pill requires more than one turn, we will add the
		 *				appropriate function pointer to the queue.
		 */
		void turnTowardsPill(int xloc, int yloc, queue<func> &funcPoint);
	
		/**
		 * Checks for 'dangerous' obstacles immediately in front of the robot,
		 * and provides advice as to whether to go forward or not
		 *
		 * @param objectsSeen - a list of seen objects
		 * @return whether or not it recommends moving forward
		 */
		bool checkForObstacles(vector<Location *> objectsSeen) const;
	
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
