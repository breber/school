/**
 * brain.cpp
 *
 * @author Brian Reber
 *
 * An abstraction of a Robot brain
 */
#include "brain.h"
#include <sstream>
#include <iostream>

/**
 * Parses the initialization message from standard in, and updates the correct variables accordingly
 */
void Brain::parseInitialization() {
	list<string> temp;
	string *read = new string();
	
	stringstream *ss = new stringstream();
	cin >> *read;
	(*ss) << "energy-contents = " << *read;
	temp.push_back(ss->str());	
	delete ss;
	delete read;
	
	read = new string();
	ss = new stringstream();
	cin >> *read;
	(*ss) << "xloc = " << *read;
	temp.push_back(ss->str());
	delete ss;
	delete read;
	
	read = new string();
	ss = new stringstream();
	cin >> *read;
	(*ss) << "yloc = " << *read;
	temp.push_back(ss->str());
	delete ss;
	delete read;
	
	read = new string();
	ss = new stringstream();
	cin >> *read;
	(*ss) << "color = " << *read;
	temp.push_back(ss->str());
	delete ss;
	delete read;
	
	ss = new stringstream();
	(*ss) << "display = RO";
	temp.push_back(ss->str());
	delete ss;
	
	cin >> simY;
	cin >> simX;
	cin >> numRobots;
	
	robot = new Robot(temp);
}

/**
 * Parses the move request from standard in, and updates the correct variables accordingly
 *
 * @param objectsSeen - a list of the objects in the 8 surrounding locations
 * @param *distances - an array of distances of probable objects probed on the last turn.
 *				The size of this array is probeWidth.
 * @param numTurns - the number of turns that have elapsed
 */
void Brain::parseMoveRequest(vector<Location *> *objectsSeen, int *distances, int moveNum) {
	int numSeen;
	cin >> numSeen;
	
	// Clear out the objects seen vector
	while (!objectsSeen->empty()) {
		objectsSeen->pop_back();
	}
	
	// Grab the seen items from the message
	for (int i = 0; i < numSeen; i++) {
		string type;
		list<string> tempVals;
		stringstream *ss = new stringstream();
		cin >> type;
		(*ss) << "type = " << type;
		tempVals.push_back(ss->str());
		delete ss;
		
		ss = new stringstream();
		string xloc;
		cin >> xloc;
		(*ss) << "xloc = " << xloc;
		tempVals.push_back(ss->str());
		delete ss;
		
		ss = new stringstream();
		string yloc;
		cin >> yloc;
		(*ss) << "yloc = " << yloc;
		tempVals.push_back(ss->str());
		delete ss;
		
		if (isObject(type)) {
			ss = new stringstream();
			string color;
			cin >> color;
			(*ss) << "color = " << color;
			tempVals.push_back(ss->str());
			delete ss;
		}
		if (hasEnergy(type)) {
			ss = new stringstream();
			string encost;
			cin >> encost;
			(*ss) << "energy-cost = " << encost;
			tempVals.push_back(ss->str());
			delete ss;
		}
		
		tempVals.push_back("display = XX");
		
		if (type.find("robot") != string::npos) {
			objectsSeen->push_back(new Robot(tempVals));
		} else if (type.find("earth-rock") != string::npos) {
			objectsSeen->push_back(new EarthRock(tempVals));
		} else if (type.find("romulan-rock") != string::npos) {
			objectsSeen->push_back(new RomulanRock(tempVals));
		} else if (type.find("ball") != string::npos) {
			objectsSeen->push_back(new Ball(tempVals));
		} else if (type.find("block") != string::npos) {
			objectsSeen->push_back(new Block(tempVals));
		} else if (type.find("energy-pill") != string::npos) {
			objectsSeen->push_back(new EnergyPill(tempVals));
		} else if (type.find("lava") != string::npos) {
			objectsSeen->push_back(new Lava(tempVals));
		} else if (type.find("water") != string::npos) {
			objectsSeen->push_back(new Water(tempVals));
		} else if (type.find("mud") != string::npos) {
			objectsSeen->push_back(new Mud(tempVals));
		} else if (type.find("hole") != string::npos) {
			objectsSeen->push_back(new Hole(tempVals));
		} else if (type.find("fog") != string::npos) {
			objectsSeen->push_back(new Fog(tempVals));
		} else if (type.find("jammer") != string::npos) {
			objectsSeen->push_back(new Jammer(tempVals));
		} else {
			cerr << "object not added because it is a " << type << endl;
		}
	}
	
	// If the probe width is greater than zero, we know we probed
	// last turn, so there should be data here
	if (probeWidth > 0) {
		for (int i = 0; i < probeWidth; i++) {
			cin >> distances[i];
		}
	}
	
	// Every fifth turn, the energy contents will be there
	if ((moveNum % 5) == 0) {
		int en;
		cin >> en;
		robot->setEnergyContents(en);
	}
}

/**
 * Checks to see which walls the robot is at
 *
 * @return an int representing the walls the robot is at
 */
int Brain::isAtWall() const {
	int toRet = 0;
	
	if (robot->getXLoc() == 0) {
		toRet |= L_WALL;
	} 
	
	if (robot->getXLoc() == (simX - 1)) {
		toRet |= R_WALL;
	}
	
	if (robot->getYLoc() == 0) {
		toRet |= T_WALL;
	}
	
	if (robot->getYLoc() == (simY - 1)) {
		toRet |= B_WALL;
	}
	
	return toRet;
}

/**
 * Moves the robot forward
 */
void Brain::moveForward() {
	cout << "0" << endl;
	robot->moveForward(simX, simY);
	probeWidth = 0;
}

/**
 * Rotates the robot by the given number of degrees
 *
 * @param degrees - the number of degrees to rotate. Must be a poer
 *			of 90 in order to work
 */
void Brain::rotate(int degrees) {
	if (degrees == 90) {
		cout << "1 +90" << endl;
		robot->setDir(robot->getDir() + 90);
	} else if (degrees == -90) {
		cout << "1 -90" << endl;
		robot->setDir(robot->getDir() + 270);
	}
	
	probeWidth = 0;
}

/**
 * Probes with the given number of beams
 *
 * @param degrees - the number of beams (the width) to probe
 */
void Brain::probe(int width) {
	if (width > 0) {
		cout << "3 " << width << endl;
		probeWidth = width;
	}
}

/**
 * Checks whether the given string matches one of our defined
 * object types
 *
 * @param str - the string 'type' to check
 * @return whether this is an object or not
 */
bool Brain::isObject(string str) const {
	// Remove the - at the beginning of the argument
	if (str.at(0) == '-') {
		str = str.substr(1);
	}
	
	return str == "ball" ||
		str == "block" ||
		str == "earth-rock" ||
		str == "energy-pill" || 
		str == "robot" ||
		str == "romulan-rock";
}

/**
 * Checks whether the given string matches one of our defined
 * property types
 *
 * @param str - the string 'type' to check
 * @return whether this is a property or not
 */
bool Brain::isProperty(string str) const {
	// Remove the - at the beginning of the argument
	if (str.at(0) == '-') {
		str = str.substr(1);
	}
	
	return str == "fog" ||
		str == "hole" ||
		str == "jammer" ||
		str == "lava" || 
		str == "mud" ||
		str == "water";
}

/**
 * Checks whether the given string matches a type that has energy
 *
 * @param str - the string 'type' to check
 * @return whether this has energy or not
 */
bool Brain::hasEnergy(string str) {
	// Remove the - at the beginning of the argument
	if (str.at(0) == '-') {
		str = str.substr(1);
	}
	
	return str == "energy-pill" ||
		str == "robot" || str == "mud" || 
		str == "water";
}

/**
 * Checks whether the given string matches a dangerous type
 *
 * @param str - the string 'type' to check
 * @return whether this type is dangerous or not
 */
bool Brain::isDangerous(string str) {
	// Remove the - at the beginning of the argument
	if (str.at(0) == '-') {
		str = str.substr(1);
	}
	
	return str == "hole";
}
