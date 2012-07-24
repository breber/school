/**
 * location.h
 *
 * @author Brian Reber
 *
 * An abstract location class, containing information applicable to 
 * all types of locations
 */
#ifndef LOCATION_H
#define LOCATION_H

#include <string>

using namespace std;

class Location {
	private:
		bool probable;
		string configType;
		string type;
		string display;
		int xloc;
		int yloc;
	
		string name;
		bool nameDef;
	public: 
		/**
		 * Creates a new location with the given type
		 *
		 * @param _type - the type of the location
		 */
		Location(string _type, string _configType) {
			type = _type;
			configType = _configType;
			xloc = -1;
			yloc = -1;
		}
	
		/**
		 * Provice a virtual Deconstructor to allow for the virtual method
		 * Doesn't need to do anything because we don't have any pointers
		 */
		virtual ~Location() { };
	
		/**
		 * Gets whether this location is probable or not
		 *
		 * @return whether this location is probable or not
		 */
		bool isProbable() const {
			return probable;
		}

		/**
		 * Sets the whether this location is probable
		 *
		 * @param prob - whether this location is probable
		 */
		void setProbable(bool prob);
	
		/**
		 * Gets the display string of this location
		 *
		 * @return the display string of this location
		 */
		string getDisplay() const {
			return display;
		}
	
		/**
		 * Sets the display string of this location
		 *
		 * @param disp - the display string of this location
		 */
		void setDisplay(string disp);
	
		/**
		 * Gets the name of this location
		 *
		 * @return the name of this location
		 */
		string getName() const {
			return name;
		}
		
		/**
		 * Sets the name of this location
		 *
		 * @param the name of this location
		 */
		void setName(string nam);
		
		/**
		 * Gets whether the name of this location has been defined
		 *
		 * @return whether the name of this location has been defined
		 */
		bool isNameDef() const {
			return nameDef;
		}
		
		/**
		 * Sets whether the name of this location has been defined
		 *
		 * @param the name of this location has been defined
		 */
		void setNameDef(bool name);
	
		/**
		 * Gets the x location of this location
		 *
		 * @return the x location of this location
		 */
		int getXLoc() const {
			return xloc;
		}
	
		/**
		 * Sets the x location of this location
		 *
		 * @param loc - the x location of this location
		 */
		void setXLoc(int loc);
	
		/**
		 * Gets the x location of this location
		 *
		 * @return the x location of this location
		 */
		int getYLoc() const {
			return yloc;
		}
	
		/**
		 * Sets the y location of this location
		 *
		 * @param loc - the y location of this location
		 */
		void setYLoc(int loc);
	
		/**
		 * Gets the type of this location
		 *
		 * @return the type of this location
		 */
		string getType() const {
			return type;
		}
	
		/**
		 * Checks whether this location is an object or not
		 */
		virtual bool isObject() const = 0;
		
		/**
		 * Checks whether this location is a property or not
		 */
		virtual bool isProperty() const = 0;
	
		/**
		 * Gets the configuration type of this location
		 *
		 * @return the configuration type of this location
		 */
		string getConfigType() const {
			return configType;
		}
	
		/**
		 * Gets the configuration string for this location
		 *
		 * @return the string representation of this location's configuration data
		 */
		virtual string getConfString() const = 0;
	
		/**
		 * Returns a string representation of this location
		 *
		 * @return a string representation of this location
		 */
		virtual string toString() const = 0;
};

#endif
