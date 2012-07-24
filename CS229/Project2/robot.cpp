/**
 * robot.cpp
 *
 * @author Brian Reber
 *
 * A Robot class, containing all the Robot specific information.
 */
#include "robot.h"
#include "stringutils.h"
#include <iostream>
#include <sstream>

/**
 * Creates a Robot with all the parameters in the given
 * list.
 * 
 * @param params - a list of parameters according to the
 *			given spec.
 */
Robot::Robot(list<string> params) : Object::Object("Robot", "robot") {
	setColor("blue");
	setName("Object");
	dir = NORTH;
	energyContents = 100;
	rechargeAmt = 1;
	movecost = 2;
	turncost = 1;
	probecost = 1;
	paramA = .1;
	paramB = 1.6;
	paramC = 1.3;
	setProbable(true);
	setMovable(true);
	
	setColorDef(false);
	setNameDef(false);
	
	while (!params.empty()) {
		string front = params.front();
		string label = front.substr(0, front.find("="));
		string val = StringUtils::trim(front.substr(front.find("=") + 2));

		if (label.find("color") != string::npos) {
			setColor(val);
			setColorDef(true);
		} else if (label.find("name") != string::npos) {
			setName(val);
			setNameDef(true);
		} else if (label.find("display") != string::npos) {
			if (val.size() != 2) {
				cerr << "Display attribute must be 2 characters" << endl;
				return;
			}
			
			setDisplay(val);
		} else if (label.find("energy-contents") != string::npos) {
			stringstream ss(val);
			ss >> energyContents;
		} else if (label.find("recharge") != string::npos) {
			stringstream ss(val);
			ss >> rechargeAmt;
		} else if (label.find("movecost") != string::npos) {
			stringstream ss(val);
			ss >> movecost;
		} else if (label.find("turncost") != string::npos) {
			stringstream ss(val);
			ss >> turncost;
		} else if (label.find("probecost") != string::npos) {
			stringstream ss(val);
			ss >> probecost;
		} else if (label.find("paramA") != string::npos) {
			stringstream ss(val);
			ss >> paramA;
		} else if (label.find("paramB") != string::npos) {
			stringstream ss(val);
			ss >> paramB;
		} else if (label.find("paramC") != string::npos) {
			stringstream ss(val);
			ss >> paramC;
		} else if (label.find("xloc") != string::npos) {
			int xloc;
			stringstream ss(val);
			ss >> xloc;
			setXLoc(xloc);
		} else if (label.find("yloc") != string::npos) {
			int yloc;
			stringstream ss(val);
			ss >> yloc;
			setYLoc(yloc);
		}
		
		params.pop_front();
	}
}

/**
 * Sets the direction of this robot
 *
 * @param the direction of this robot
 */
void Robot::setDir(int _dir) {
	dir = (_dir % 360);
}

/**
 * Sets the energy contents of this robot
 *
 * @param the energy contents of this robot
 */
void Robot::setEnergyContents(double en_cont) {
	if (en_cont >= 0) {
		energyContents = en_cont;
	} else {
		energyContents = 0;
	}
}

/**
 * Sets the recharge value of this robot
 *
 * @param the recharge value of this robot
 */
void Robot::setRecharge(int rechar) {
	rechargeAmt = rechar;
}

/**
 * Sets the cost to move this robot
 *
 * @param the cost to move this robot
 */
void Robot::setMoveCost(int cost) {
	movecost = cost;
}

/**
 * Sets the cost to turn this robot
 *
 * @param the cost to turn this robot
 */
void Robot::setTurnCost(int cost) {
	turncost = cost;
}

/**
 * Sets the cost to probe
 *
 * @param the cost to probe
 */
void Robot::setProbeCost(int cost) {
	probecost = cost;
}

/**
 * Sets the value of paramA
 *
 * @param the value of paramA
 */
void Robot::setParamA(double param) {
	paramA = param;
}

/**
 * Sets the value of paramB
 *
 * @param the value of paramB
 */
void Robot::setParamB(double param) {
	paramB = param;
}

/**
 * Sets the value of paramC
 *
 * @param the value of paramC
 */
void Robot::setParamC(double param) {
	paramC = param;
}

/**
 * Recharges the robot by the correct amount
 */
void Robot::recharge() {
	energyContents += rechargeAmt;
}

/**
 * Moves the robot forward in direction pointing
 */
void Robot::moveForward(int xSim, int ySim) {
	switch (dir) {
		case NORTH:
			if ((getYLoc() - 1) >= 0) {
				setYLoc(getYLoc() - 1);
			} else {
				throw "This move would bring you off the board";
			}
			break;
		case SOUTH:
			if ((getYLoc() + 1) < ySim) {
				setYLoc(getYLoc() + 1);
			} else {
				throw "This move would bring you off the board";
			}
			break;
		case EAST:
			if ((getXLoc() + 1) < xSim) {
				setXLoc(getXLoc() + 1);
			} else {
				throw "This move would bring you off the board";
			}
			break;
		case WEST:
			if ((getXLoc() - 1) >= 0) {
				setXLoc(getXLoc() - 1);
			} else {
				throw "This move would bring you off the board";
			}
			break;
	}
}

/**
 * Gets the configuration string for this location
 *
 * @return the string representation of this location's configuration data
 */
string Robot::getConfString() const {
	stringstream out;
	
	out << "<Object>" << endl;
	
	out << "type = " << getConfigType() << endl;
	out << "xloc = " << getXLoc() << endl;
	out << "yloc = " << getYLoc() << endl;
	out << "display = " << getDisplay() << endl;
	
	if (isNameDef()) {
		out << "name = " << getName() << endl;
	}
	
	if (isColorDef()) {
		out << "color = " << getColor() << endl;
	}
	
	if (energy_contentsDef) {
		out << "energy-contents = " << (int) energyContents << endl;
	}
	
	if (rechargeDef) {
		out << "recharge = " << rechargeAmt << endl;
	}
	
	if (movecostDef) {
		out << "movecost = " << movecost << endl;
	}
	
	if (turncostDef) {
		out << "turncost = " << turncost << endl;
	}
	
	if (probecostDef) {
		out << "probecost = " << probecost << endl;
	}
	
	if (paramADef) {
		out << "paramA = " << paramA << endl;
	}
	
	if (paramBDef) {
		out << "paramB = " << paramB << endl;
	}
	
	if (paramCDef) {
		out << "paramC = " << paramC << endl;
	}
	
	out << "</Object>" << endl;
	
	return out.str();
}

/**
 * Returns a string representation of this object
 *
 * @return a string representation of this object
 */
string Robot::toString() const {
	stringstream str;
	
	str << Object::toString();
	if (isNameDef()) {
		str << "Name: " << getName() << endl;
	} else {
		str << "Name: " << getDisplay() << endl;
	}
	
	if (isColorDef()) {
		str << "Color: " << getColor() << endl;
	}
	
	str << "Energy Contents: " << getEnergyContents() << endl;
	
	return str.str();
}
