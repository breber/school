/**
 * generateconf.cpp
 *
 * @author Brian Reber
 *
 * A runner for parsing the config file
 */
#ifndef GENERATECONF_H
#define GENERATECONF_H

#include <vector>
#include "stringutils.h"

#define THREEBLOCK "30 27 -robot -l 0,26 -color blue -robot -l 29,26 -color red -block -l 4,6 -color red -block -l 4,13 -color red -block -l 4,20 -color red -block -l 25,6 -color blue -block -l 25,13 -color blue -block -l 25,20 -color blue"

#define FIVEITEM "30 35 -robot -l 0,34 -color blue -robot -l 29,34 -color red -ball -l 4,5 -color red -block -l 4,11 -color red -ball -l 4,17 -color red -block -l 4,23 -color red -ball -l 4,29 -color red -ball -l 25,5 -color blue -block -l 25,11 -color blue -ball -l 25,17 -color blue -block -l 25,23 -color blue -ball -l 25,29 -color blue"

using namespace std;

/**
 * Parses the command line arguments from the queue and places
 * the appropriate objects and properties into the simulation
 *
 * @param *sim - the simulation to add the elements to
 * @param args - the queue containing the command line arguments to parse
 */
void parseArgs(Simulation *sim, queue<string> args);

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
void parseAttributes(queue<string> props, list<string> *tempVals);

/**
 * Adds an object to the simulation based on the properties passed in from
 * the queue parameter
 *
 * @param *sim - the simulation to add the objects to
 * @param props - the queue containing the command line arguments
 *			to parse and create an object out of
 */
void addObject(Simulation *sim, queue<string> props);

/**
 * Adds a property to the simulation based on the properties passed in from
 * the queue parameter
 *
 * @param *sim - the simulation to add the properties to
 * @param props - the queue containing the command line arguments
 *			to parse and create a property out of
 */
void addProperty(Simulation *sim, queue<string> props);

/**
 * Checks whether the given string matches one of our defined
 * object types
 *
 * @param str - the string 'type' to check
 * @return whether this is an object or not
 */
bool isObject(const string& str);

/**
 * Checks whether the given string matches one of our defined
 * property types
 *
 * @param str - the string 'type' to check
 * @return whether this is a property or not
 */
bool isProperty(const string& str);

/**
 * Prints a usage message for this program
 */
void printUsage();

#endif
