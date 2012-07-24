/**
 * location.cpp
 *
 * @author Brian Reber
 *
 * An abstract location class, containing information applicable to 
 * all types of locations
 */
#include "location.h"
#include <sstream>

/**
 * Sets the whether this location is probable
 *
 * @param prob - whether this location is probable
 */
void Location::setProbable(bool prob) {
	probable = prob;
}

/**
 * Sets the display string of this location
 *
 * @param disp - the display string of this location
 */
void Location::setDisplay(string disp) {
	display = disp;
}

/**
 * Sets the name of this object
 *
 * @param the name of this object
 */
void Location::setName(string nam) {
	name = nam;
}

/**
 * Sets whether the name of this object has been defined
 *
 * @param the name of this object has been defined
 */
void Location::setNameDef(bool name) {
	nameDef = name;
}

/**
 * Sets the x location of this location
 *
 * @param loc - the x location of this location
 */
void Location::setXLoc(int loc) {
	xloc = loc;
}

/**
 * Sets the y location of this location
 *
 * @param loc - the y location of this location
 */
void Location::setYLoc(int loc) {
	yloc = loc;
}

/**
 * Returns a string representation of this location
 *
 * @return a string representation of this location
 */
string Location::toString() const {
	stringstream str;
	
	str << "Type: " << getType() << endl;
	
	return str.str();
}
