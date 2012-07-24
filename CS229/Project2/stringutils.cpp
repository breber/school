/**
 * stringutils.cpp
 *
 * @author Brian Reber
 *
 * Provides some functions for parsing strings into Objects/Simulations/Properties
 */
#include "stringutils.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <list>

/*
 * Removes "//" comments from the given string
 *
 * @param str - the string to remove comments from
 * @return the string without comments
 */
string StringUtils::removeComments(const string& str) {
	int loc = str.find("//");
	
	if (!(loc < 0)) {
		return str.substr(0, loc);
	}
	
	return str;
}

/*
 * Trims trailing whitespace.
 *
 * @param str - the string to remove trailing whitespace from
 * @return the string without trailing whitespace
 */
string StringUtils::trim(const string& str) {
	size_t lastPos = str.find_last_not_of(' ');
	
	if (lastPos != string::npos && lastPos == str.size()) {
		return str;
	} else {
		return str.substr(0, lastPos + 1);
	}
}

/*
 * Converts the given string to upper case
 *
 * @param str - the string to convert to upper case
 * @return the string in all caps
 */
string StringUtils::toUpperCase(const string& str) {
	stringstream out;
	char temp[str.size()];
	for (unsigned int i = 0; i < str.size(); i++) {
		temp[i] = toupper(str[i]);
	}
	out << temp;
	return out.str();
}

/*
 * Converts the given string to lower case
 *
 * @param str - the string to convert to lower case
 * @return the string in all lower case
 */
string StringUtils::toLowerCase(const string& str) {
	stringstream out;
	char temp[str.size()];
	for (unsigned int i = 0; i < str.size(); i++) {
		temp[i] = tolower(str[i]);
	}
	out << temp;
	return out.str();
}

/**
 * Tokenizes the given string using the given delimiter
 *
 * @param str - the string to tokenize
 * @param delimiter - the delimiter to use
 * @return a queue of tokenized strings
 */
queue<string> * StringUtils::tokenize(const string& str, char delimiter) {
	queue<string> *toRet = new queue<string>();
	unsigned int i = 0;
	stringstream *temp = new stringstream();
	
	while (i < str.size()) {
		if (str[i] == delimiter) {
			toRet->push(temp->str());
			delete temp;
			temp = new stringstream();
		} else {
			(*temp) << str[i];
		}
		
		i++;
	}
	
	toRet->push(temp->str());
	
	return toRet;
}

/*
 * Parses the complete config file, and places the data into
 * its proper location in the given simulation
 * 
 * @param &file - the file stream that contains the data to parse
 * @param *sim - the Simulation to set the values for
 */
void StringUtils::parseConfigFile(ifstream &file, Simulation *sim) {
	vector<Object *> objects;
	vector<Property *> properties;
	queue<string> vals;
	string temp;
	
	// Read everything in, removing comments as we go
	// Push everyline into the queue
	while (!file.eof()) {
		getline(file, temp);
		string noComments = removeComments(temp);
		if (noComments.size() > 0) {
			vals.push(noComments);
		}
	}
	
	file.close();
	
	// Parse the simulation element of the queue
	parseSimulation(sim, &vals);
		
	// While the queue isn't empty, continue to parse object and
	// property elements and push them into the simulation
	while (!vals.empty()) {
		if (toUpperCase(vals.front()).find("<OBJECT>") != string::npos) {
			Object * obj = parseObject(&vals);

			// If the location isn't occupied, we will add it to the simulation
			if (!sim->isOccupied(obj->getXLoc(), obj->getYLoc())) {
				sim->addObject(obj);
			} else {
				cerr << "Two objects may not occupy the same location." << endl;
				throw exception();
			}
		} else if (toUpperCase(vals.front()).find("<PROPERTY>") != string::npos) {
			sim->addProperty(parseProperty(&vals));
		}
	}
}

/*
 * Parses the simulation element from the queue of string values
 * 
 * @param *sim - the Simulation to set the values for
 * @param *values - the queue containing the lines of the config file
 */
void StringUtils::parseSimulation(Simulation *sim, queue<string> *values) {
	string temp;
	
	// Check for the opening Simulation tag
	if (toUpperCase((*values).front()).find("<SIMULATION>") == string::npos) {
		cerr << "The simulation element has to be the first in the file " << (*values).front() << endl;
		return;
	}
	
	// Remove the opening <Simulation> tag from the queue
	(*values).pop();
	
	// Now check for width and height
	for (int i = 0; i < 2; i++) {
		temp = (*values).front();
		
		if (temp.find("width") != string::npos && temp.find("=") >= 0) {
			int width;
			stringstream ss(temp.substr(temp.find("=") + 1));
			ss >> width;
			(*sim).setWidth(width);
		} else if (temp.find("height") != string::npos && temp.find("=") >= 0) {
			int height;
			stringstream ss(temp.substr(temp.find("=") + 1));
			ss >> height;
			(*sim).setHeight(height);
		} else {
			cerr << "Height or width only options for Simulation tag\n" << endl;
		}
		(*values).pop();
	}
	
	// Check for the closing Simulation tag
	if (toUpperCase((*values).front()).find("</SIMULATION>") == string::npos) {
		cerr << "The simulation element can only have height and width elements " << (*values).front() << endl;
		return;
	}
	
	(*values).pop();
}

/*
 * Parses an object element from the queue of string values
 * 
 * @param *values - the queue containing the lines of the config file
 * @return a pointer to an Object representation of the object element
 */
Object * StringUtils::parseObject(queue<string> *values) {
	string temp;
	string type;
	list<string> tempVals;
	
	// Remove the <Object> tag
	(*values).pop();
	temp = (*values).front();
	
	// Find the type attribute, while putting the other attributes into
	// a temporary list. Remove the attributes from the original queue
	while (temp.substr(0, temp.find("=")).find("type") == string::npos) {
		tempVals.push_back(temp);
		(*values).pop();
		temp = (*values).front();
	}
	
	// We have found the type of the object, so we save that for 
	// later, and remove it from the original queue
	type = temp.substr(temp.find("=") + 2);
	
	// Finish adding the attributes from the Object element to our temporart
	// list, removing them from the queue in the process
	while (toUpperCase(temp).find("</OBJECT>") == string::npos) {
		tempVals.push_back(temp);
		(*values).pop();
		temp = (*values).front();
	}
	
	// Remove the closing object tag (</Object>)
	(*values).pop();
	
	// Differentiate what type of Object this is, and return a reference to an
	// instance of the appropriate type
	if (type.find("robot") != string::npos) {
		return new Robot(tempVals);
	} else if (type.find("earth-rock") != string::npos) {
		return new EarthRock(tempVals);
	} else if (type.find("romulan-rock") != string::npos) {
		return new RomulanRock(tempVals);
	} else if (type.find("ball") != string::npos) {
		return new Ball(tempVals);
	} else if (type.find("block") != string::npos) {
		return new Block(tempVals);
	} else if (type.find("energy-pill") != string::npos) {
		return new EnergyPill(tempVals);
	}
	
	// Throw an exception if not one of our types of objects
	throw exception();
}

/*
 * Parses a property element from the queue of string values
 * 
 * @param *values - the queue containing the lines of the config file
 * @return a pointer to a Property representation of the property element
 */
Property * StringUtils::parseProperty(queue<string> *values) {
	string temp;
	string type;
	list<string> tempVals;
	
	Property * toRet;
	
	// Remove the <Property> tag
	(*values).pop();
	temp = (*values).front();
	
	// Find the type attribute, while putting the other attributes into
	// a temporary list. Remove the attributes from the original queue
	while (temp.substr(0, temp.find("=")).find("type") == string::npos) {
		tempVals.push_back(temp);
		(*values).pop();
		temp = (*values).front();
	}
	
	// We have found the type of the object, so we save that for 
	// later, and remove it from the original queue
	type = temp.substr(temp.find("=") + 2);
	
	// Finish adding the attributes from the Property element to our temporart
	// list, removing them from the queue in the process
	while (toUpperCase(temp).find("</PROPERTY>") == string::npos) {
		tempVals.push_back(temp);
		(*values).pop();
		temp = (*values).front();
	}
	
	// Remove the closing Property tag (</Property>)
	(*values).pop();
	
	// Differentiate what type of property this is, and return a reference to an
	// instance of the appropriate type
	if (type.find("lava") != string::npos) {
		toRet = new Lava(tempVals);
	} else if (type.find("water") != string::npos) {
		toRet = new Water(tempVals);
	} else if (type.find("mud") != string::npos) {
		toRet =  new Mud(tempVals);
	} else if (type.find("hole") != string::npos) {
		toRet = new Hole(tempVals);
	} else if (type.find("fog") != string::npos) {
		toRet = new Fog(tempVals);
	} else if (type.find("jammer") != string::npos) {
		toRet = new Jammer(tempVals);
	} else {
		// Throw an exception if not one of our types of properties
		throw exception();
	}

	return toRet;
}
