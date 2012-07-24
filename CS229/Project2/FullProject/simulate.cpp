/**
 * simulate.cpp
 *
 * @author Brian Reber
 *
 * The simulation that gets run and interacts with the robot brains
 */
#include "simulate.h"
#include "brain.h"
#include "energy.h"
#include <iostream>
#include <sstream>

int main(int argc, char *argv[]) {
	Simulate simulate;
	ifstream file;
	string mode;
	int temp;
	int numRobots = 0;
	
	// Files for communicating with robots
	vector<RobotIO> robots;
	vector<RollingObject> rollingObjs;
		
	if (argc >= 3) {
		// Config file
		file.open(argv[1]);
		
		if (!file.is_open()) {
			cerr << "Invalid config file location" << endl;
			return 0;
		}
		
		// number of turns
		stringstream ss(argv[2]);
		ss >> temp;
		simulate.setNumTurns(temp);

		// quiet or verbose
		mode = argv[3];
		
		// Open the file descriptors
		int i = 4, num = 0;
		while (i < argc) {
			int fd;
			
			sscanf(argv[i], "%d", &fd);
			
			if ((i % 2) == 0) {
				RobotIO temp;
				robots.push_back(temp);
				
				robots[num].outFile = fdopen(fd, "w");
			} else {
				robots[num++].inFile = fdopen(fd, "r");
			}
						
			i++;
		}
		
	} else {
		cerr << "Missing command line argument" << endl;
		return 0;
	}
	
	StringUtils::parseConfigFile(file, &simulate.getEditableCurrentSim());
	
	// Get a list of all the robots in the simulation
	vector<Object *> objs = simulate.getCurrentSim().getObjects();
	for (unsigned int i = 0, count = 0; i < objs.size(); i++) {
		if (objs[i]->getType() == "Robot") {
			numRobots++;
			robots[count].bot = ((Robot *) objs[i]);
			robots[count].successParam = NONE;
			robots[count].probed = 0;
			robots[count].turnsToWait = 0;
			robots[count].finished = false;
			robots[count].moved = false;
			count++;
		} else if (objs[i]->getType() == "Ball") {
			// Set up the Rolling Object
			struct RollingObject temp;
			temp.obj = objs[i];
			temp.dir = NORTH;
			temp.numTurns = 0;
			temp.xLoc = objs[i]->getXLoc();
			temp.yLoc = objs[i]->getYLoc();
			rollingObjs.push_back(temp);
		}
	}
		
	// START COMMUNICATION BY INITIALIZING
	for (unsigned int i = 0; i < robots.size(); i++) {
		simulate.sendInitializationRequest(robots[i], numRobots);
	}
	
	
	// GAME LOOP!
	while (simulate.getTurnNum() < simulate.getNumTurns()) {
		// Apply any punishments (such as being in lava) before the move
		for (unsigned int i = 0; i < robots.size(); i++) {
			vector<Property *> props = simulate.getCurrentSim().getProperties(robots[i].bot->getXLoc(), robots[i].bot->getYLoc());
			double totalCost = 0;
			
			for (unsigned int t = 0; t < props.size(); t++) {
				if (props[t]->getConfigType() == "lava") {
					totalCost += ((IEnergy *) props[t])->getEnergy();
				}
			}
			
			robots[i].bot->setEnergyContents(robots[i].bot->getEnergy() - totalCost);
			
			// If this punishment was strong enough to bring the robot
			// to 0 energy, it is finished
			if (robots[i].bot->getEnergy() == 0) {
				robots[i].finished = true;
			}
		}
		
		// SEND MOVE REQUEST
		for (unsigned int i = 0; i < robots.size(); i++) {
			if (!robots[i].finished) {
				simulate.sendMoveRequest(robots[i]);
			}
		}
		
		// RECEIVE MOVE DECISION
		for (unsigned int i = 0; i < robots.size(); i++) {
			if (!robots[i].finished) {
				simulate.receiveMoveDecision(robots[i]);
				
				// If this robot is stuck in mud or another
				// substance that requires it to wait, we will
				// receive their move decision, but deny it
				// right away (by setting its result to FAIL)
				if (robots[i].turnsToWait) {
					robots[i].successParam = FAIL;
					
					robots[i].turnsToWait--;
				}
			}
		}
		
		// Figure out if there are any problems with the movement
		// that the robots just did.  If no problems (collisions)
		// apply the changes.  If there are problems, react accordingly
		for (unsigned int i = 0; i < robots.size(); i++) {
			if (!robots[i].finished && robots[i].moved) {
				// If it isn't occupied, we can just say it is fine
				if (!simulate.getCurrentSim().isOccupied(robots[i].bot->getXLoc(), robots[i].bot->getYLoc())) {
					// Check against other robots' new locations
					for (unsigned int j = 0; j < robots.size(); j++) {
						// No need to check the current robot against itself
						if (i != j) {
							// If the current robot and the robot at position 'j' have the same location, set the
							// current robot's successParam to FAIL
							if (robots[j].bot->getXLoc() == robots[i].bot->getXLoc() && robots[j].bot->getYLoc() == robots[i].bot->getYLoc()) {
								robots[i].successParam = FAIL;
							}
						}
					}
				} else {
					// If it is occupied, then we need to check to see if it is movable
					Object * objInWay = simulate.getCurrentSim().getObject(robots[i].bot->getXLoc(), robots[i].bot->getYLoc());
					
					if (objInWay->isMovable()) {
						// Try to move object. If we can't move, set the successParam to FAIL
						bool canMove = simulate.getEditableCurrentSim().moveObject(objInWay, robots[i].bot->getDir());
						
						if (!canMove) {
							robots[i].successParam = FAIL;
						}
					} else if (objInWay->getConfigType() != "energy-pill") {
						// The object isn't movable, so just set the successParam to FAIL
						robots[i].successParam = FAIL;
					}
				}
			}
		}
		
		// Actually update objects
		for (unsigned int i = 0; i < robots.size(); i++) {
			if (!robots[i].finished && robots[i].moved) {
				simulate.updateObject(robots[i]);
			}
		}
		
		// If a rolling object still needs to roll, do that before the turn
		for (unsigned int i = 0; i < rollingObjs.size(); i++) {
			if (rollingObjs[i].xLoc != rollingObjs[i].obj->getXLoc() || rollingObjs[i].yLoc != rollingObjs[i].obj->getYLoc()) {
				// The location of the object doesn't match with where it was last turn
				// Therefore, it was pushed and needs to roll an extra turn
			
				// Set the direction it was moved in
				if (rollingObjs[i].xLoc == rollingObjs[i].obj->getXLoc() - 1) {
					rollingObjs[i].dir = EAST;
				} else if (rollingObjs[i].xLoc == rollingObjs[i].obj->getXLoc() + 1) {
					rollingObjs[i].dir = WEST;
				} else if (rollingObjs[i].yLoc == rollingObjs[i].obj->getYLoc() - 1) {
					rollingObjs[i].dir = SOUTH;
				} else if (rollingObjs[i].yLoc == rollingObjs[i].obj->getYLoc() + 1) {
					rollingObjs[i].dir = NORTH;
				}
				
				// Set the new local locations
				rollingObjs[i].xLoc = rollingObjs[i].obj->getXLoc();
				rollingObjs[i].yLoc = rollingObjs[i].obj->getYLoc();
				
				// Indicate it needs to move again
				rollingObjs[i].numTurns++;
			} else if (rollingObjs[i].numTurns > 0) {
				int tempX = rollingObjs[i].obj->getXLoc();
				int tempY = rollingObjs[i].obj->getYLoc();
				
				if (rollingObjs[i].dir == NORTH) {
					tempY--;
				} else if (rollingObjs[i].dir == SOUTH) {
					tempY++;
				} else if (rollingObjs[i].dir == WEST) {
					tempX--;
				} else if (rollingObjs[i].dir == EAST) {
					tempX++;
				}
								
				// If space is empty and within the bounds, move
				if (tempX >= 0 && tempX < simulate.getCurrentSim().getWidth() && tempY >= 0 && tempY < simulate.getCurrentSim().getHeight() && !simulate.getCurrentSim().isOccupied(tempX, tempY)) {
					simulate.getEditableCurrentSim().moveObject(rollingObjs[i].obj, rollingObjs[i].dir);
										
					// Update the local location (for checking to see if obj was moved)
					rollingObjs[i].xLoc = rollingObjs[i].obj->getXLoc();
					rollingObjs[i].yLoc = rollingObjs[i].obj->getYLoc();
				} else {
					// Revert movement
					if (rollingObjs[i].dir == NORTH) {
						rollingObjs[i].obj->setYLoc(rollingObjs[i].obj->getYLoc() + 1);
					} else if (rollingObjs[i].dir == SOUTH) {
						rollingObjs[i].obj->setYLoc(rollingObjs[i].obj->getYLoc() - 1);
					} else if (rollingObjs[i].dir == WEST) {
						rollingObjs[i].obj->setXLoc(rollingObjs[i].obj->getXLoc() + 1);
					} else if (rollingObjs[i].dir == EAST) {
						rollingObjs[i].obj->setXLoc(rollingObjs[i].obj->getXLoc() - 1);
					}
				}
				
				rollingObjs[i].numTurns--;
			}
		}
		
		// After every robot has had a turn, recharge the robots
		for (unsigned int i = 0; i < robots.size(); i++) {
			if (!robots[i].finished) {
				robots[i].bot->recharge();
			}
		}
		
		// If we are in verbose mode, print out the board
		if (mode == "-verbose") {
			cout << simulate.getCurrentSim().toString() << endl;

			// Wait for the user to press a button
			cin.get();
		}
		
		simulate.completedTurn();
	}
	
	// SEND TERMINATION REQUEST
	for (unsigned int i = 0; i < robots.size(); i++) {
		fprintf(robots[i].outFile, "2 ");
		fflush(robots[i].outFile);
	}
	
	// Print the Board and the information about occupied locations
	cout << simulate.getCurrentSim().toString() << endl;
	cout << simulate.getCurrentSim().printObjectData();
	
	return 0;
}

/**
 * Moves a robot back to where it was in the previous turn, and
 * adjusts the energy appropriately
 *
 * Details: This method essentially looks at the robot's orientation,
 * and moves 1 space in the opposite direction. It then adds a move
 * cost to its energy contents.
 *
 * @param robot - the robot to revert
 */
void Simulate::undoMove(RobotIO &robot) {
	// Undo move
	if (robot.bot->getDir() == NORTH) {
		robot.bot->setYLoc(robot.bot->getYLoc() + 1);
	} else if (robot.bot->getDir() == SOUTH) {
		robot.bot->setYLoc(robot.bot->getYLoc() - 1);
	} else if (robot.bot->getDir() == WEST) {
		robot.bot->setXLoc(robot.bot->getXLoc() + 1);
	} else if (robot.bot->getDir() == EAST) {
		robot.bot->setXLoc(robot.bot->getXLoc() - 1);
	}
	
	// Undo the energy subtraction
	robot.bot->setEnergyContents(robot.bot->getEnergyContents() + robot.bot->getMoveCost());
	
	// Set the move as failed, just to make sure
	robot.successParam = FAIL;
}

/**
 * Updates the object in the simulation. If the move isn't valid,
 * un-does the move
 *
 * @param robot - the robot that needs updating
 * @param currentSim - the simulation currently running
 */
void Simulate::updateObject(RobotIO& robot) {
	if (robot.successParam == SUCCESS) {
		// If we just left a location with an energy cost, subtract that now
		vector<Property *> props;
		
		// Find the properties in the previous location (for costs that get applied as
		// the robot leaves the location)
		if (robot.bot->getDir() == NORTH) {
			props = currentSim.getProperties(robot.bot->getXLoc(), robot.bot->getYLoc() + 1);
		} else if (robot.bot->getDir() == SOUTH) {
			props = currentSim.getProperties(robot.bot->getXLoc(), robot.bot->getYLoc() - 1);
		} else if (robot.bot->getDir() == WEST) {
			props = currentSim.getProperties(robot.bot->getXLoc() + 1, robot.bot->getYLoc());
		} else if (robot.bot->getDir() == EAST) {
			props = currentSim.getProperties(robot.bot->getXLoc() - 1, robot.bot->getYLoc());
		}
		
		double totalCost = 0;
		// Count up the energy cost of each property
		for (unsigned int t = 0; t < props.size(); t++) {
			if (props[t]->getConfigType() == "mud") {
				totalCost += ((Mud *) props[t])->getEnergyCost();
			} else if (props[t]->getConfigType() == "water") {
				totalCost += ((Water *) props[t])->getEnergyCost();
			} else if (props[t]->getConfigType() == "hole") {
				// If a robot goes into a hole, it is finished
				robot.finished = true;
			}
		}
		
		// If there is enough energy in the robot, subtract it off
		if (robot.bot->getEnergy() >= totalCost) {
			// Update the simulation arrays so object is in the correct spot
			currentSim.updateObject(robot.bot, true);
			
			robot.bot->setEnergyContents(robot.bot->getEnergy() - totalCost);
			
			// Find the properties in the current location (for costs that apply
			// when the robot enters)
			props = currentSim.getProperties(robot.bot->getXLoc(), robot.bot->getYLoc());
			
			// Set the appropriate cost variables
			for (unsigned int t = 0; t < props.size(); t++) {
				if (props[t]->getConfigType() == "mud") {
					robot.turnsToWait = ((Mud *) props[t])->getTurnCost();
				}
			}						
		} else {
			// If there isn't enough energy, undo the move
			undoMove(robot);
		}
	} else {
		undoMove(robot);
	}
}

/**
 * Sends the initialization request to the given robot's output file
 *
 * @param robot - the robot to send the commands to
 * @param currentSim - the simulation currently running
 * @param numRobots - the number of robots in the simulation
 */
void Simulate::sendInitializationRequest(const RobotIO& robot, int numRobots) {
	fprintf(robot.outFile, "0 ");
	fprintf(robot.outFile, "%d ", (int) robot.bot->getEnergyContents());
	fprintf(robot.outFile, "%d ", robot.bot->getXLoc());
	fprintf(robot.outFile, "%d ", robot.bot->getYLoc());
	fprintf(robot.outFile, "%s ", robot.bot->getColor().c_str());
	fprintf(robot.outFile, "%d ", currentSim.getHeight());
	fprintf(robot.outFile, "%d ", currentSim.getWidth());
	fprintf(robot.outFile, "%d ", numRobots);
	fflush(robot.outFile);
}

/**
 * Sends a move request to the given robot's output file
 *
 * @param robot - the robot to send the commands to
 * @param currentSim - the simulation currently running
 * @param turnNum - the current turn
 */
void Simulate::sendMoveRequest(const RobotIO& robot) {
	fprintf(robot.outFile, "1 ");
	
	// Success param			
	if (robot.successParam == SUCCESS) {
		fprintf(robot.outFile, SUCCESS_STR);
	} else if (robot.successParam == FAIL) {
		fprintf(robot.outFile, FAIL_STR);
	} else if (robot.successParam == NONE) {
		fprintf(robot.outFile, NONE_STR);
	}
		
	// Number of seen items
	vector<const Location *> seen = currentSim.getSeenItems(robot.bot->getXLoc(), robot.bot->getYLoc());
	fprintf(robot.outFile, "%d ", (int) seen.size());
	
	// Seen item details
	for (unsigned int j = 0; j < seen.size(); j++) {
		fprintf(robot.outFile, "%s ", seen[j]->getConfigType().c_str());
		fprintf(robot.outFile, "%d ", seen[j]->getXLoc());
		fprintf(robot.outFile, "%d ", seen[j]->getYLoc());
		if (seen[j]->isObject()) {
			fprintf(robot.outFile, "%s ", ((Object *) seen[j])->getColor().c_str());
		}
		if (Brain::hasEnergy(seen[j]->getConfigType())) {
			fprintf(robot.outFile, "%d ", (int) ((IEnergy *) seen[j])->getEnergy());
		}
	}
		
	// If the robot probed last turn, we return their results
	if (robot.probed > 0) {
		for (int i = 0; i < robot.probed; i++) {
			fprintf(robot.outFile, "%d ", robot.probe[i]);
		}
	}
	
	// If we are on a turn we need to return energy, we do so
	if ((turnNum % 5) == 0) {
		fprintf(robot.outFile, "%d ", (int) robot.bot->getEnergy());
	}
		
	fflush(robot.outFile);
}

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
void Simulate::receiveMoveDecision(RobotIO& robot) {
	int actionCode = -1;
	
	fscanf(robot.inFile, "%d", &actionCode);
	
	robot.probed = false;
	robot.moved = false;
	
	switch (actionCode) {
		case 0: {
			// Move forward			
			if (robot.bot->getEnergy() >= robot.bot->getMoveCost()) {
				try {
					robot.bot->moveForward(currentSim.getWidth(), currentSim.getHeight());
				} catch (char const*s) {
					robot.successParam = FAIL;
				}
				
				robot.bot->setEnergyContents(robot.bot->getEnergy() - robot.bot->getMoveCost());
				robot.successParam = SUCCESS;
				robot.moved = true;
			} else {
				// If we didn't have enough energy, set the success param to FAIL
				robot.successParam = FAIL;
			}

			break;
		} case 1: {
			// Turn
			char degrees[5];
			fscanf(robot.inFile, "%s", degrees);
			
			// If we have enough energy (turn cost), we will turn
			if (robot.bot->getEnergy() >= robot.bot->getTurnCost()) {
				if (strcmp("-90", degrees) == 0) {
					robot.bot->setDir(robot.bot->getDir() + 270);
				} else if (strcmp("+90", degrees) == 0) {
					robot.bot->setDir(robot.bot->getDir() + 90);
				} else {
					cerr << "Improper direction" << endl;
					robot.successParam = FAIL;
					return;
				}
				
				// Subtract off the robot's turn cost
				robot.bot->setEnergyContents(robot.bot->getEnergy() - robot.bot->getTurnCost());

				robot.successParam = SUCCESS;
			} else {
				// If we didn't have enough energy, set the success param to FAIL
				robot.successParam = FAIL;
			}
			
			break;
		} case 2: {
			// Fire
			int energy;
			fscanf(robot.inFile, "%d", &energy);
			
			// If we have enough energy (fire cost), we will fire
			if (robot.bot->getEnergy() >= robot.bot->getFireCost(energy)) {
				// Get the robot right in front of us
				Robot * theEnemy = (Robot *) currentSim.getRobotStraightAhead(robot.bot->getXLoc(), robot.bot->getYLoc(), robot.bot->getDir());
				
				if (theEnemy != NULL) {
					// Subtract the energy from the robot
					theEnemy->setEnergyContents((theEnemy->getEnergy() - robot.bot->getParamC() * energy));
				}
				
				// Subtract the cost from ourself
				robot.bot->setEnergyContents((robot.bot->getEnergy() - robot.bot->getFireCost(energy)));
				robot.successParam = SUCCESS;
			} else {
				// If we didn't have enough energy, set the success param to FAIL
				robot.successParam = FAIL;
			}
			
			break;
		} case 3: {
			// Probe
			int probeWidth;
			fscanf(robot.inFile, "%d", &probeWidth);
	
			// If we have enough energy (probe cost * number of beams), 
			// we will probe
			if (robot.bot->getEnergy() >= (robot.bot->getProbeCost() * probeWidth)) {
				robot.probed = probeWidth;
				robot.probe = currentSim.probe(robot.bot->getXLoc(), robot.bot->getYLoc(), robot.bot->getDir(), probeWidth);
				robot.bot->setEnergyContents(robot.bot->getEnergy() - (robot.bot->getProbeCost() * probeWidth));
				robot.successParam = SUCCESS;
			} else {
				// If we didn't have enough energy, set the success param to FAIL
				robot.successParam = FAIL;
			}
			break;
		} case 4: {
			// Do nothing
			robot.successParam = NONE;
			break;
		} default: {
			cerr << "Improper Action Code" << endl;
			break;
		}
	}
}
