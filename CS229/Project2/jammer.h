/**
 * jammer.h
 *
 * @author Brian Reber
 *
 * A Jammer class, containing all the Jammer specific information.
 */
#ifndef JAMMER_H
#define JAMMER_H

#include <string>
#include "property.h"
#include <list>

using namespace std;

class Jammer : public Property {
	public: 
		/**
		 * Creates a jammer with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		Jammer(list<string> params);
	
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
