/**
 * property.h
 *
 * @author Brian Reber
 *
 * An abstract Property class, containing information applicable to 
 * all types of Properties
 */
#ifndef PROPERTY_H
#define PROPERTY_H

#include <string>
#include "location.h"

using namespace std;

class Property : public Location {
	public: 
		/**
		 * Creates a lava with all the parameters in the given
		 * list.
		 */
		Property(string type, string configType) : Location(type, configType) { }
	
		/**
		 * Provide a virtual Deconstructor to allow for the virtual method
		 */
		virtual ~Property() {	}
		
		/**
		 * Checks to see if this Location is an object or not
		 *
		 * @return false
		 */
		virtual bool isObject() const {
			return false;
		}
	
		/**
		 * Checks to see if this Location is a property or not
		 *
		 * @return true
		 */
		virtual bool isProperty() const {
			return true;
		}
	
		/**
		 * Gets the configuration string for this location
		 *
		 * @return the string representation of this location's configuration data
		 */
		virtual string getConfString() const = 0;
	
		/**
		 * Returns a string representation of this property
		 *
		 * @return a string representation of this property
		 */
		virtual string toString() const = 0;
};

#endif
