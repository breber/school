/**
 * object.h
 *
 * @author Brian Reber
 *
 * An abstract Object class, containing information applicable to 
 * all types of objects
 */
#ifndef OBJECT_H
#define OBJECT_H

#include <string>
#include "location.h"

using namespace std;

/**
 * Represents the different directions an object can be facing
 */
enum DIRECTION {
	NORTH = 0,
	EAST = 90,
	SOUTH = 180,
	WEST = 270
};

class Object : public Location {
	private:
		bool movable;
	
		string color;
	protected:
		bool colorDef;
	public: 
		/**
		 * Creates a new object with the given type
		 *
		 * @param _type - the type of the object
		 */
		Object(string _type, string _configType) : Location (_type, _configType) {	}
		
		/**
		 * Provice a virtual Deconstructor to allow for the virtual method
		 */
		virtual ~Object() { }
	
		/**
		 * Checks whether this location is an object or not
		 *
		 * @return true
		 */
		virtual bool isObject() const {
			return true;
		}
		
		/**
		 * Checks whether this location is a property or not
		 *
		 * @return false
		 */
		virtual bool isProperty() const {
			return false;
		}
	
		/**
		 * Gets whether this object is movable or not
		 *
		 * @return whether this object is movable or not
		 */
		bool isMovable() const {
			return movable;
		}
	
		/**
		 * Sets the whether this object is movable
		 *
		 * @param mov - whether this object is movable
		 */
		void setMovable(bool mov);
	
		/**
		 * Gets the color of this object
		 *
		 * @return the color of this object
		 */
		string getColor() const {
			return color;
		}
		
		/**
		 * Sets the color of this object
		 *
		 * @param the color of this object
		 */
		void setColor(string col);
	
		/**
		 * Gets whether the color of this object has been defined
		 *
		 * @return whether the color of this object has been defined
		 */
		bool isColorDef() const {
			return colorDef;
		}
		
		/**
		 * Sets whether the color of this object has been defined
		 *
		 * @param the color of this object has been defined
		 */
		void setColorDef(bool col);
	
		/**
		 * Gets whether this object has energy or not
		 *
		 * @return whether this object has energy or not
		 */
		virtual bool hasEnergy() const = 0;
		
		/**
		 * Gets the configuration string for this location
		 *
		 * @return the string representation of this location's configuration data
		 */
		virtual string getConfString() const = 0;
	
		/**
		 * Returns a string representation of this object
		 *
		 * @return a string representation of this object
		 */
		virtual string toString() const = 0;
};

#endif
