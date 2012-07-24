/**
 * romulanrock.cpp
 *
 * @author Brian Reber
 *
 * A Romulan Rock class, containing all the Romulan Rock specific information.
 */
#include "romulanrock.h"
#include "stringutils.h"
#include <iostream>
#include <sstream>

/**
 * Creates a romulan rock with all the parameters in the given
 * list.
 * 
 * @param params - a list of parameters according to the
 *			given spec.
 */
RomulanRock::RomulanRock(list<string> params) : Object::Object("Romulan Rock", "romulan-rock") {
	setColor("blue");
	setName("Object");
	setProbable(false);
	setMovable(false);
	
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
 * Gets the configuration string for this location
 *
 * @return the string representation of this location's configuration data
 */
string RomulanRock::getConfString() const {
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
	
	out << "</Object>" << endl;
	
	return out.str();
}

/**
 * Returns a string representation of this object
 *
 * @return a string representation of this object
 */
string RomulanRock::toString() const {
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
	
	return str.str();
}
