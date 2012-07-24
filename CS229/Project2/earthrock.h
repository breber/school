/**
 * earthrock.h
 *
 * @author Brian Reber
 *
 * An Earth Rock class, containing all the Earth Rock specific information.
 */
#ifndef EARTHROCK_H
#define EARTHROCK_H

#include "object.h"
#include <string>
#include <list>

using namespace std;

class EarthRock : public Object {
	public:
		/**
		 * Creates an earth rock with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		EarthRock(list<string> params);
	
		/**
		 * Gets whether this object has energy or not
		 *
		 * @return whether this object has energy or not
		 */
		virtual bool hasEnergy() const {
			return false;
		}
	
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
