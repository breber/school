/**
 * simulate.h
 *
 * @author Brian Reber
 *
 * The simulation that gets run and interacts with the robot brains
 */
#ifndef SIMULATE_H
#define SIMULATE_H

#include <fstream>
#include "stringutils.h"

using namespace std;

#define SUCCESS_STR "true "
#define FAIL_STR "false "
#define NONE_STR "null "

/**
 * Represents the status of the previous operation
 */
enum SuccessParam {
	SUCCESS,
	FAIL,
	NONE
};

/**
 * A simple wrapper class for a robot in the simulation.
 * Contains things such as success status, files to read/write to,
 * an actual Robot object, probing information
 */
class RobotIO {
	public:
		FILE * inFile;
		FILE * outFile;
		Robot * bot;
		int probed;
		int * probe;
		bool moved;
		int turnsToWait;
		SuccessParam successParam;
		bool finished;
};

/**
 * A simple wrapper struct for an object in the simulation that needs
 * to be able to move during a turn without being pushed during that
 * specific turn.  This is useful for objects like balls that will continue
 * to roll in the turns after it was pushed
 */
struct RollingObject {
	Object * obj;
	int xLoc;
	int yLoc;
	DIRECTION dir;
	int numTurns;
};

class Simulate {
	private:
		Simulation currentSim;
		int turnNum;
		int numTurns;
	public:
	
		/**
		 * Sets the number of turns to complete
		 *
		 * @param _numTurns - the number of turns to complete
		 */
		void setNumTurns(int _numTurns) {
			numTurns = _numTurns;
		}
	
		/**
		 * Gets the number of turns this simulation is to complete
		 *
		 * @return the number of turns to complete
		 */
		int getNumTurns() const {
			return numTurns;
		}
	
		/**
		 * Gets the current turn number
		 *
		 * @return the current turn number
		 */
		int getTurnNum() const {
			return turnNum;
		}
	
		/**
		 * Indicates a turn has been completed.
		 * Increments the turn num
		 */	
		void completedTurn() {
			turnNum++;
		}

		/**
		 * Gets the simulation
		 *
		 * @return the simulation
		 */
		const Simulation& getCurrentSim() const {
			return currentSim;
		}
	
		/**
		 * Gets an editable version of the simulation
		 *
		 * Sometimes, we need to be able to edit the simulation from a
		 * function call, so we provide this option. If no editing is needed, 
		 * use the other method
		 *
		 * @return an editable simulation
		 */
		Simulation& getEditableCurrentSim() {
			return currentSim;
		}
	
		/**
		 * Moves a robot back to where it was in the previous turn, and
		 * adjusts the energy appropriately
		 *
		 * @param robot - the robot to revert
		 */
		void undoMove(RobotIO &robot);

		/**
		 * Updates the object in the simulation. If the move isn't valid,
		 * un-does the move
		 *
		 * @param robot - the robot that needs updating
		 * @param currentSim - the simulation currently running
		 */
		void updateObject(RobotIO& robot);

		/**
		 * Sends the initialization request to the given robot's output file
		 *
		 * @param robot - the robot to send the commands to
		 * @param currentSim - the simulation currently running
		 * @param numRobots - the number of robots in the simulation
		 */
		void sendInitializationRequest(const RobotIO& robot, int numRobots);

		/**
		 * Sends a move request to the given robot's output file
		 *
		 * @param robot - the robot to send the commands to
		 * @param currentSim - the simulation currently running
		 * @param turnNum - the current turn
		 */
		void sendMoveRequest(const RobotIO& robot);

		/**
		 * Receives a move decision command from the robot's input file
		 *
		 * Implementation notes:
		 *
		 * -When a robot fires, it hurts the robot that was in front of it when it fired,
		 *	not after the current turn.
		 * -When a robot probes, the probe returns the distances as they were when the robot
		 *	decided to probe.  This means that the probe may not be accurate at the end of this
		 *	turn, after all robots and objects finish this turn
		 *
		 * @param robot - the robot to read the commands from
		 * @param currentSim - the simulation currently running
		 */
		void receiveMoveDecision(RobotIO& robot);

};
	
#endif
