/**
 * fog.h
 *
 * @author Brian Reber
 *
 * A Fog class, containing all the Fog specific information.
 */
#ifndef FOG_H
#define FOG_H

#include <string>
#include "property.h"
#include <list>

using namespace std;

class Fog : public Property {
	public: 
		/**
		 * Creates a fog with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		Fog(list<string> params);
	
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
