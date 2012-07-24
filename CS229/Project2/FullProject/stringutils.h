/**
 * stringutils.h
 *
 * @author Brian Reber
 *
 * Provides some functions for parsing strings into Objects/Simulations/Properties
 */
#ifndef STRINGUTILS_H
#define STRINGUTILS_H

#include <string>
#include <queue>
#include "simulation.h"

#include "property.h"
#include "lava.h"
#include "water.h"
#include "mud.h"
#include "hole.h"
#include "fog.h"
#include "jammer.h"

#include "object.h"
#include "robot.h"
#include "earthrock.h"
#include "romulanrock.h"
#include "ball.h"
#include "block.h"
#include "energypill.h"


using namespace std;

class StringUtils {
	public:
		/*
		 * Removes "//" comments from the given string
		 *
		 * @param str - the string to remove comments from
		 * @return the string without comments
		 */
		static string removeComments(const string& str);

		/*
		 * Trims trailing whitespace.
		 *
		 * @param str - the string to remove trailing whitespace from
		 * @return the string without trailing whitespace
		 */
		static string trim(const string& str);

		/*
		 * Converts the given string to upper case
		 *
		 * @param str - the string to convert to upper case
		 * @return the string in all caps
		 */
		static string toUpperCase(const string& str);

		/*
		 * Converts the given string to lower case
		 *
		 * @param str - the string to convert to lower case
		 * @return the string in all lower case
		 */
		static string toLowerCase(const string& str);

		/**
		 * Tokenizes the given string using the given delimiter
		 *
		 * @param str - the string to tokenize
		 * @param delimiter - the delimiter to use
		 * @return a queue of tokenized strings
		 */
		static queue<string> *tokenize(const string& str, char delimiter);

		/*
		 * Parses the complete config file, and places the data into
		 * its proper location in the given simulation
		 * 
		 * @param &file - the file stream that contains the data to parse
		 * @param *sim - the Simulation to set the values for
		 */
		static void parseConfigFile(ifstream &file, Simulation *sim);

		/*
		 * Parses the simulation element from the queue of string values
		 * 
		 * @param *sim - the Simulation to set the values for
		 * @param *values - the queue containing the lines of the config file
		 */
		static void parseSimulation(Simulation *sim, queue<string> *values);

		/*
		 * Parses an object element from the queue of string values
		 * 
		 * @param *values - the queue containing the lines of the config file
		 * @return a pointer to an Object representation of the object element
		 */
		static Object * parseObject(queue<string> *values);

		/*
		 * Parses a property element from the queue of string values
		 * 
		 * @param *values - the queue containing the lines of the config file
		 * @return a pointer to a Property representation of the property element
		 */
		static Property * parseProperty(queue<string> *values);
};
	
#endif
