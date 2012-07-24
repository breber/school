/**
 * hole.h
 *
 * @author Brian Reber
 *
 * A Hole class, containing all the Hole specific information.
 */
#ifndef HOLE_H
#define HOLE_H

#include <string>
#include "property.h"
#include <list>

using namespace std;

class Hole : public Property {
	public: 
		/**
		 * Creates a hole with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		Hole(list<string> params);
	
		/**
		 * Gets the configuration string for this location
		 *
		 * @return the string representation of this location's configuration data
		 */
		virtual string getConfString() const;
	
		/**
		 * Returns a string representation of this object
		 *
		 * @return a string representation of this object
		 */
		string toString() const;
};

#endif
