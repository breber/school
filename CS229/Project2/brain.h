/**
 * brain.h
 *
 * @author Brian Reber
 *
 * An abstraction of a Robot brain
 */
#ifndef BRAIN_H
#define BRAIN_H

#include "stringutils.h"
#include <vector>

using namespace std;

#define T_WALL 0x01
#define B_WALL 0x02
#define R_WALL 0x04
#define L_WALL 0x08

class Brain {
	protected:
		Robot * robot;
		int simX;
		int simY;
		int numRobots;
		int probeWidth;
	public:	
		/**
		 * Create a new Brain
		 */
		Brain() {
			probeWidth = 0;
		}
	
		/**
		 * Provide a virtual destructor
		 */
		virtual ~Brain() { 
			delete robot;
		}
	
		/**
		 * Parses the initialization message from standard in, and updates the correct variables accordingly
		 */
		void parseInitialization();

		/**
		 * Parses the move request from standard in, and updates the correct variables accordingly
		 *
		 * @param objectsSeen - a list of the objects in the 8 surrounding locations
		 * @param *distances - an array of distances of probable objects probed on the last turn.
		 *				The size of this array is probeWidth.
		 * @param numTurns - the number of turns that have elapsed
		 */
		void parseMoveRequest(vector<Location *> *objectsSeen, int *distances, int numTurns);

		/**
		 * Checks to see which walls the robot is at
		 *
		 * @return an int representing the walls the robot is at
		 */
		int isAtWall() const;
			
		/**
		 * Given the robot's current position, and its surroundings, we make a decision about
		 * what the robot should do on the next turn.
		 *
		 * @param objectsSeen - a list of the objects in the 8 surrounding locations
		 * @param distances[] - an array of distances of probable objects probed on the last turn.
		 *				The size of this array is probeWidth.
		 */
		virtual void makeMoveDecision(vector<Location *> objectsSeen, int distances[]) = 0;
	
		/**
		 * Moves the robot forward
		 */
		void moveForward();
		
		/**
		 * Rotates the robot by the given number of degrees
		 *
		 * @param degrees - the number of degrees to rotate. Must be a poer
		 *			of 90 in order to work
		 */
		void rotate(int degrees);
	
		/**
		 * Probes with the given number of beams
		 *
		 * @param degrees - the number of beams (the width) to probe
		 */
		void probe(int width);
	
		/**
		 * Checks whether the given string matches one of our defined
		 * object types
		 *
		 * @param str - the string 'type' to check
		 * @return whether this is an object or not
		 */
		bool isObject(string str) const;
	
		/**
		 * Gets the width of the probe on the previous turn
		 *
		 * @return the width of the probe on the previous turn
		 */
		int getProbeWidth() const {
			return probeWidth;
		}

		/**
		 * Checks whether the given string matches one of our defined
		 * property types
		 *
		 * @param str - the string 'type' to check
		 * @return whether this is a property or not
		 */
		bool isProperty(string str) const;

		/**
		 * Checks whether the given string matches a type that has energy
		 *
		 * @param str - the string 'type' to check
		 * @return whether this has energy or not
		 */
		static bool hasEnergy(string str);
	
		/**
		 * Checks whether the given string matches a dangerous type
		 *
		 * @param str - the string 'type' to check
		 * @return whether this type is dangerous or not
		 */
		static bool isDangerous(string str);

};

#endif
