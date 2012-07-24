/**
 * checkconf.cpp
 *
 * @author Brian Reber
 *
 * A runner for parsing the config file
 */
#include "checkconf.h"
#include "stringutils.h"
#include <fstream>
#include <iostream>

int main(int argc, char *argv[]) {
	Simulation currentSim;
	ifstream file;
	
	if (argc == 2) {
		// If we have enough arguments, open the file
		file.open(argv[1]);
		
		if (!file.is_open()) {
			cerr << "Invalid config file location" << endl;
			return 0;
		}
	} else {
		cerr << "Missing command line argument" << endl;
		return 0;
	}
	
	// Parse the config file
	// If there are errors, exit
	try {
		StringUtils::parseConfigFile(file, &currentSim);
	} catch (exception ex) { 
		return 0;
	}
	
	// Print the Board and the information about occupied locations
	cout << currentSim.toString() << endl;
	cout << currentSim.printObjectData();
	
	return 0;
}
