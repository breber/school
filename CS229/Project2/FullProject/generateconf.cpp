/**
 * generateconf.cpp
 *
 * @author Brian Reber
 *
 * A runner for parsing the config file
 */
#include "generateconf.h"
#include <stdlib.h>
#include <iostream>
#include <sstream>

int main(int argc, char *argv[]) {	
	Simulation currentSim;
	
	if (argc >= 3) {
		// If we have more than 2 arguments, assume we don't have 
		// any special arguments, so this is the normal case
		queue<string> vals;
		int i = 3;
		int temp;
		
		// First arg is sim width
		stringstream ss(argv[1]);
		ss >> temp;
		currentSim.setWidth(temp);
		
		// second arg is sim height
		stringstream ss1(argv[2]);
		ss1 >> temp;
		currentSim.setHeight(temp);
		
		// The rest of the args are objects/properties
		while (i < argc) {
			string temp(argv[i]);
			if ((temp  == "-threeblock") || (temp == "-fiveitem")) {
				cerr << "Can't mix normal tags with '-threeblock' or '-fiveitem'" << endl;
				return 0;
			}
			vals.push(argv[i]);
			i++;
		}

		// Parse the args we just got from the command line
		parseArgs(&currentSim, vals);
		
	} else if (argc == 2) {
		// If we have 2 args, assume that it is one of the special cases
		string temp(argv[1]);
		
		if (temp == "-threeblock") {
			// If it is threeblock, parse the constant defined arg string
			queue<string> *vals = StringUtils::tokenize(THREEBLOCK, ' ');
			
			int temp;
			
			stringstream ss(vals->front());
			vals->pop();
			
			ss >> temp;
			currentSim.setWidth(temp);
			
			stringstream ss1(vals->front());
			ss1 >> temp;
			vals->pop();
			currentSim.setHeight(temp);
			
			parseArgs(&currentSim, *vals);
		} else if (temp == "-fiveitem") {
			// If it is fiveitem, parse the constant defined arg string
			queue<string> *vals = StringUtils::tokenize(FIVEITEM, ' ');
			
			int temp;
			
			stringstream ss(vals->front());
			vals->pop();
			
			ss >> temp;
			currentSim.setWidth(temp);
			
			stringstream ss1(vals->front());
			ss1 >> temp;
			vals->pop();
			currentSim.setHeight(temp);
			
			parseArgs(&currentSim, *vals);
		}
	} else {
		// If we don't have any args, print out a usage message
		printUsage();
		return 0;
	}
	
	// Print the Board and the information about occupied locations
	cout << currentSim.getConfString() << endl;
	
	return 0;
}

/**
 * Parses the command line arguments from the queue and places
 * the appropriate objects and properties into the simulation
 *
 * @param *sim - the simulation to add the elements to
 * @param args - the queue containing the command line arguments to parse
 */
void parseArgs(Simulation *sim, queue<string> args) {
	while (!args.empty()) {
		queue<string> tempVals;
		
		do {
			tempVals.push(args.front());
			args.pop();
		} while (!args.empty() && !isObject(args.front()) && !isProperty(args.front()));
		
		
		if (isObject(tempVals.front())) {
			addObject(sim, tempVals);
		} else if (isProperty(tempVals.front())) {
			addProperty(sim, tempVals);
		}
	}
}

/**
 * Parses the attributes in the queue, and puts the results into the list.
 * This formats the attributes in the queue into a format the Object/Property
 * constructors can handle.
 * 
 * Note: the first item in the list will be how many instances to create. If
 *		a location was not specified in the properties, then the second item
 *		in the list will be a flag saying so.
 *
 * @param props - the queue containing the command line arguments to parse
 * @param *tempVals - the list to put the properties in
 */
void parseAttributes(queue<string> props, list<string> *tempVals) {
	bool addedNumToMake = false;
	bool addedLocation = false;
	
	while (!props.empty()) {
		if (props.front() == "-l") {
			// If the first item in the queue is a '-l', we know we are placing
			// the object at a specific location
			props.pop();
			
			if (props.front().find(",") == string::npos) {
				// If the comma is not in the first element, we will take the 
				// first element and take that as the x loc, and the second element
				// is the y loc
				stringstream ss;
				ss << "xloc = " << props.front();
				tempVals->push_back(ss.str());
				props.pop();
				
				stringstream ss1;
				ss1 << "yloc = " << props.front();
				tempVals->push_back(ss1.str());
				props.pop();
			} else {
				// There is a comma, so we will take what is before the comma as the
				// x loc, and what is after the comma as the y loc
				stringstream ss;
				ss << "xloc = " << props.front().substr(0, props.front().find(","));
				tempVals->push_back(ss.str());
				
				stringstream ss1;
				ss1 << "yloc = " << props.front().substr(props.front().find(",") + 1);
				tempVals->push_back(ss1.str());	
				
				props.pop();
			}
			addedLocation = true;
		} else if (props.front() == "-c") {
			// If the first thing in the queue is a '-c', we are making
			// a specific number of these objects
			props.pop();
			stringstream num;
			num << "numtomake = " << props.front();
			props.pop();
			tempVals->push_front(num.str());
			addedNumToMake = true;
		} else {
			stringstream ss;
			ss << props.front().substr(1) << " = ";
			if (ss.str().find("xloc") != string::npos || ss.str().find("yloc") != string::npos) {
				addedLocation = true;
			}
			props.pop();
			ss << props.front();
			tempVals->push_back(ss.str());
			props.pop();
		}
	}
	
	// If we didn't add a location, and we did add a number of items
	// to make, we need to push a message on saying we need location
	// for all the objects created based on this tempVals
	if (!addedLocation && addedNumToMake) {
		// Since the numtomake needs to be the first element
		// save it, remove it, add the needs loc message,
		// and readd the numtomake
		string temp = tempVals->front();
		tempVals->pop_front();
		tempVals->push_front("needsloc");
		tempVals->push_front(temp);
		
		return;
	} 
	
	// If we didn't add a location and we didn't add
	// a numtomake (so we just have a single object),
	// we will just push a needsloc onto the front
	if (!addedLocation && !addedNumToMake) {
		tempVals->push_front("needsloc");
	}
	
	// if we didn't add a numtomake, add numtomake = 1
	if (!addedNumToMake) {
		tempVals->push_front("numstomake = 1");
	}
}

/**
 * Adds an object to the simulation based on the properties passed in from
 * the queue parameter
 *
 * @param *sim - the simulation to add the objects to
 * @param props - the queue containing the command line arguments
 *			to parse and create an object out of
 */
void addObject(Simulation *sim, queue<string> props) {
	list<string> tempVals;
	string type = props.front().substr(1);
	props.pop();
	int numToMake = 1;
	int tempCount = 0;
	
	// Get the number of objects of the same type currently in the sim
	vector<Object *> objs = (*sim).getObjects();	
	for (unsigned int i = 0; i < objs.size(); i++) {
		if (StringUtils::toUpperCase((objs[i])->getConfigType()).find(StringUtils::toUpperCase(type)) != string::npos) {
			tempCount++;
		}
	}
	
	// Parse the attributes for the object
	parseAttributes(props, &tempVals);
	
	// After the parseAttributes call, the first element will be the number
	// of objects to create. We grab that from the queue
	stringstream ss(tempVals.front().substr(tempVals.front().find("=") + 1));
	ss >> numToMake;
	tempVals.pop_front();
	
	for (int i = 0; i < numToMake; i++) {
		// If we need to make more than one, or we need a location because it wasn't defined
		// explicitly, we will try and find a random, empty location
		if (numToMake > 1 || tempVals.front().find("needsloc") != string::npos) {
			int tempx = (rand() % (*sim).getWidth());
			int tempy = (rand() % (*sim).getHeight());
			
			if (sim->isOccupied(tempx, tempy)) {
				do {
					tempx = (rand() % (*sim).getWidth());
					tempy = (rand() % (*sim).getHeight());
				} while (sim->isOccupied(tempx, tempy));
			}
			
			stringstream xLoc;
			xLoc << "xloc = " << tempx;
			tempVals.push_back(xLoc.str());
			
			stringstream yLoc;
			yLoc << "yloc = " << tempy;
			tempVals.push_back(yLoc.str());
		}
		
		// Set the display attribute to a defualt. Put it at the front of the
		// list so that it can be overridden if there is one specified later
		// in the command line args
		stringstream displ;
		displ << "display = " << (char) toupper(type[0]) << "" << ((tempCount + i) % 10);
		if (tempVals.front().find("display = ") != string::npos) {
			tempVals.pop_front();
		}
		tempVals.push_front(displ.str());
		
		if (type.find("robot") != string::npos) {
			(*sim).addObject(new Robot(tempVals));
		} else if (type.find("earth-rock") != string::npos) {
			(*sim).addObject(new EarthRock(tempVals));
		} else if (type.find("romulan-rock") != string::npos) {
			(*sim).addObject(new RomulanRock(tempVals));
		} else if (type.find("ball") != string::npos) {
			(*sim).addObject(new Ball(tempVals));
		} else if (type.find("block") != string::npos) {
			(*sim).addObject(new Block(tempVals));
		} else if (type.find("energy-pill") != string::npos) {
			(*sim).addObject(new EnergyPill(tempVals));
		}
	}
}

/**
 * Adds a property to the simulation based on the properties passed in from
 * the queue parameter
 *
 * @param *sim - the simulation to add the properties to
 * @param props - the queue containing the command line arguments
 *			to parse and create a property out of
 */
void addProperty(Simulation *sim, queue<string> props) {
	list<string> tempVals;
	string type = props.front().substr(1);
	props.pop();
	int numToMake = 1;
	int tempCount = 0;
	
	vector< vector<Property *> > properties = (*sim).getProperties();
	for (unsigned int i = 0; i < properties.size(); i++) {
		vector<Property *> row = properties[i];
		for (unsigned int j = 0; j < row.size(); j++) {
			if (StringUtils::toUpperCase(properties[i][j]->getConfigType()).find(StringUtils::toUpperCase(type)) != string::npos) {
				tempCount++;
			}
		}
	}

	parseAttributes(props, &tempVals);
	stringstream ss(tempVals.front().substr(tempVals.front().find("=") + 1));
	ss >> numToMake;
	tempVals.pop_front();
	
	for (int i = 0; i < numToMake; i++) {
		if (numToMake > 1 || tempVals.front().find("needsloc") != string::npos) {
			stringstream xLoc;
			xLoc << "xloc = " << (rand() % (*sim).getWidth());
			tempVals.push_back(xLoc.str());
			
			stringstream yLoc;
			yLoc << "yloc = " << (rand() % (*sim).getHeight());
			tempVals.push_back(yLoc.str());
		}
		
		// Set the display attribute to a defualt. Put it at the front of the
		// list so that it can be overridden if there is one specified later
		// in the command line args
		stringstream displ;
		displ << "display = " << (char) toupper(type[0]) << "" << ((tempCount + i) % 10);
		if (tempVals.front().find("display = ") != string::npos) {
			tempVals.pop_front();
		}
		tempVals.push_front(displ.str());
		
		if (type.find("lava") != string::npos) {
			(*sim).addProperty(new Lava(tempVals));
		} else if (type.find("water") != string::npos) {
			(*sim).addProperty(new Water(tempVals));
		} else if (type.find("mud") != string::npos) {
			(*sim).addProperty(new Mud(tempVals));
		} else if (type.find("hole") != string::npos) {
			(*sim).addProperty(new Hole(tempVals));
		} else if (type.find("fog") != string::npos) {
			(*sim).addProperty(new Fog(tempVals));
		} else if (type.find("jammer") != string::npos) {
			(*sim).addProperty(new Jammer(tempVals));
		}
	}
}

/**
 * Checks whether the given string matches one of our defined
 * object types
 *
 * @param str - the string 'type' to check
 * @return whether this is an object or not
 */
bool isObject(const string& str) {
	string temp = str;
	
	// Remove the - at the beginning of the argument
	if (str.at(0) == '-') {
		temp = str.substr(1);
	}
	
	return temp == "ball" ||
		temp == "block" ||
		temp == "earth-rock" ||
		temp == "energy-pill" || 
		temp == "robot" ||
		temp == "romulan-rock";
}

/**
 * Checks whether the given string matches one of our defined
 * property types
 *
 * @param str - the string 'type' to check
 * @return whether this is a property or not
 */
bool isProperty(const string& str) {
	string temp = str;
	
	// Remove the - at the beginning of the argument
	if (str.at(0) == '-') {
		temp = str.substr(1);
	}
	
	return temp == "fog" ||
		temp == "hole" ||
		temp == "jammer" ||
		temp == "lava" || 
		temp == "mud" ||
		temp == "water";
}

/**
 * Prints a usage message for this program
 */
void printUsage() {
	cerr << "Missing command line arguments" << endl;
	cerr << "\tgenerateconf width height [-type [<-l x,y>, <-c numInstances>]]" << endl;
	cerr << "\n\tgenerateconf 20 25 -robot -l 5,6 -robot -l 16,17 -turncost 5 -energy-pill -c 37 -block -l 0,0" << endl;
}
