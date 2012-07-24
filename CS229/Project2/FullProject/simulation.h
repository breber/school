/**
 * simulation.h
 *
 * @author Brian Reber
 *
 * An class representing the simulation board
 */
#ifndef SIMULATION_H
#define SIMULATION_H

#include "object.h"
#include "property.h"
#include <vector>

using namespace std;

class Simulation {
	private:
		bool initializedObjects;
		int width;
		int height;
		Object ***objects;
		vector<Property *> **properties;
		
		/**
		 * Initializes the object and property arrays
		 */
		void initializeArrays();
	public: 
		/**
		 * Creates a new default simulation
		 */
		Simulation();
		
		/**
		 * Destruct the malloc'd memory
		 */
		~Simulation();
	
		/**
		 * Sets the width of the simulation board
		 *
		 * @param _width - the width of the simulation board
		 */
		void setWidth(int _width);

		/**
		 * Gets the width of the simulation board
		 *
		 * @return the width of the simulation board
		 */
		int getWidth() const {
			return width;
		}
	
		/**
		 * Sets the height of the simulation board
		 *
		 * @param _height - the height of the simulation board
		 */
		void setHeight(int _height);
	
		/**
		 * Gets the height of the simulation board
		 *
		 * @return the height of the simulation board
		 */
		int getHeight() const {
			return height;
		}
	
		/**
		 * Adds the given object to this simulation
		 *
		 * @param the object to add
		 */
		void addObject(Object *obj);
	
		/**
		 * Removes the given object to this simulation
		 *
		 * @param the object to remove
		 */
		void removeObject(Object *obj);
	
		/**
		 * Updates the given object in this simulation
		 *
		 * @param the object to update
		 */
		void updateObject(Object *obj, bool hasEnergy);
	
		/**
		 * Checks to see if the location is occupied by an object
		 *
		 * @param x - the x location to check
		 * @param y - the y location to check
		 * @return whether the location has an object in it
		 */
		bool isOccupied(int x, int y) const;
	
		/**
		 * Gets the object at the given location
		 *
		 * @param x - the x location to check
		 * @param y - the y location to check
		 * @return the object at the given locatio, NULL if none
		 */
		Object * getObject(int x, int y) const;
		
		/**
		 * Gets a vector of properties at the given location
		 *
		 * @param x - the x location to check
		 * @param y - the y location to check
		 * @return a vector of properties that are in the given location
		 */
		const vector<Property *> getProperties(int x, int y) const;
	
		/**
		 * Adds the given property to this simulation
		 *
		 * @param the property to add
		 */
		void addProperty(Property *prop);
	
		/**
		 * Gets a list of objects
		 *
		 * @return the object currently in the simulation
		 */
		const vector<Object *> getObjects() const;
	
		/**
		 * Gets a list of properties
		 *
		 * @return the object currently in the simulation
		 */
		const vector< vector<Property *> > getProperties() const;
	
		/**
		 * Gets a list of items that can be seen at the given location
		 *
		 * @param xloc - the x location to look at
		 * @param yloc - the y location to look at
		 * @return list of items that can be seen at the given location
		 */
		const vector <const Location *> getSeenItems(int xloc, int yloc) const;
	
		/**
		 * Gets the robot that is straight ahead
		 *
		 * @param xloc - the x location to look at
		 * @param yloc - the y location to look at
		 * @param direction - the direction to look in
		 * @return the robot that is straight ahead, NULL if none, or another object is in the way
		 */
		Object * getRobotStraightAhead(int xloc, int yloc, int direction) const;
	
		/**
		 * Probes starting at the given location, in the given direction,
		 * with a probe width of the given amount
		 *
		 * @param xloc - the x location to look at
		 * @param yloc - the y location to look at
		 * @param dir - the direction to probe in
		 * @param probeWidth - the width of the probe
		 * @return distances of items probed
		 */
		int * probe(int xloc, int yloc, int dir, int probeWidth) const;
	
		/**
		 * Tries to move the given object in the direction given.
		 *
		 * @param *objInWay - the object that is to be moved
		 * @param direction - the direction to move the object in
		 * @return whether the object could be moved or not
		 */
		bool moveObject(Object * objInWay, int direction);
	
		/**
		 * Gets the configuration string for this simulation
		 *
		 * @return the string representation of this simulation's configuration data
		 */
		string getConfString() const;
	
		/**
		 * Prints out the necessary object data
		 *
		 * @return the string representation of all the objects in the simulation
		 */
		string printObjectData() const;
	
		/**
		 * Returns a string representation of this simulation
		 *
		 * @param a string representation of this simulation
		 */
		string toString() const;
};

#endif
