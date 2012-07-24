/**
 * romulanrock.h
 *
 * @author Brian Reber
 *
 * A Romulan Rock class, containing all the Romulan Rock specific information.
 */
#ifndef ROMULANROCK_H
#define ROMULANROCK_H

#include "object.h"
#include <string>
#include <list>

using namespace std;

class RomulanRock : public Object {
	public:
		/**
		 * Creates a romulan rock with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		RomulanRock(list<string> params);
	
		/**
		 * Gets the configuration string for this location
		 *
		 * @return the string representation of this location's configuration data
		 */
		virtual string getConfString() const;
	
		/**
		 * Gets whether this object has energy or not
		 *
		 * @return whether this object has energy or not
		 */
		virtual bool hasEnergy() const {
			return false;
		}
	
		/**
		 * Returns a string representation of this object
		 *
		 * @return a string representation of this object
		 */
		string toString() const;
};

#endif
