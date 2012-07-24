/**
 * simulation.cpp
 *
 * @author Brian Reber
 *
 * An class representing the simulation board
 */
#include "simulation.h"
#include "robot.h"
#include <stdlib.h>
#include <iostream>
#include <sstream>

/**
 * Creates a new default simulation
 */
Simulation::Simulation() {
	initializedObjects = false;
	width = -1;
	height = -1;
}

/**
 * Destruct the malloc'd memory
 */
Simulation::~Simulation() {
	// Go through and delete any objects and properties
	for (int x = 0; x < width; x++) {
		for (int y = 0; y < height; y++) {
			// delete object at location
			delete objects[x][y];
			
			// Delete all properties at the location
			if (properties[x][y].size() > 0) {
				for (unsigned int i = 0; i < properties[x][y].size(); i++) {
					delete (properties[x][y])[i];
				}
			}
		}
		
		// Delete the array/vector
		delete objects[x];
	}
}

/**
 * Sets the width of the simulation board
 *
 * @param _width - the width of the simulation board
 */
void Simulation::setWidth(int _width) {
	if (_width > 0 && _width <= 35) {
		width = _width;
	}
}

/**
 * Sets the height of the simulation board
 *
 * @param _height - the height of the simulation board
 */
void Simulation::setHeight(int _height) {
	if (_height > 0 && _height <= 35) {
		height = _height;
	}
}

/**
 * Initializes the objects array
 */
void Simulation::initializeArrays() {
	objects = new Object* *[width];
	properties = new vector<Property*> *[width];
	
	for (int i = 0; i < width; i++) {
		objects[i] = new Object*[height];
		properties[i] = new vector<Property*>[height];
		
		for (int j = 0; j < height; j++) {
			objects[i][j] = NULL;
		}
	}
	
	initializedObjects = true;
}

/**
 * Adds the given object to this simulation
 *
 * @param the object to add
 */
void Simulation::addObject(Object *obj) {
	int x = (*obj).getXLoc();
	int y = (*obj).getYLoc();
	
	if (!initializedObjects && width > 0 && height > 0) {
		initializeArrays();
	} else if (width < 0 || height < 0) {
		cerr << "Must initialize height and width before adding an object" << endl;
		return;
	}
	
	objects[x][y] = obj;
}

/**
 * Removes the given object to this simulation
 *
 * @param the object to remove
 */
void Simulation::removeObject(Object *obj) {
	if (initializedObjects) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {				
				if (objects[x][y] == obj) {
					delete objects[x][y];
					objects[x][y] = NULL;
					break;
				}
			}
		}
	}
}

/**
 * Updates the given object in this simulation
 *
 * @param the object to update
 */
void Simulation::updateObject(Object *obj, bool hasEnergy) {
	if (initializedObjects) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {				
				if (objects[x][y] == obj) {
					// We have found the object that needs updating
					if (objects[x][y]->getXLoc() != x || objects[x][y]->getYLoc() != y) {
						// The locations don't match, which means that the robot was moved
						int xLocation = objects[x][y]->getXLoc();
						int yLocation = objects[x][y]->getYLoc();
						if (objects[xLocation][yLocation] == NULL) {
							// If we are moving into an empty space, just do it
							objects[xLocation][yLocation] = objects[x][y];
							objects[x][y] = NULL;
							break;
						} else if (objects[xLocation][yLocation]->getConfigType() == "energy-pill" && hasEnergy) {
							// If the location is occupied by an energy pill, grab the energy, and
							// then delete the energy pill, and move the robot
							Robot *bot = ((Robot *) objects[x][y]);
							bot->setEnergyContents(bot->getEnergy() + ((IEnergy *) objects[xLocation][yLocation])->getEnergy());
							removeObject(objects[xLocation][yLocation]);
							objects[xLocation][yLocation] = objects[x][y];
							objects[x][y] = NULL;
						}
					}
				}
			}
		}
	}
}

/**
 * Checks to see if the location is occupied by an object
 *
 * @param x - the x location to check
 * @param y - the y location to check
 * @return whether the location has an object in it
 */
bool Simulation::isOccupied(int x, int y) const {
	if (!initializedObjects) {
		return false;
	}
	
	if (x >= 0 && x < width && y >= 0 && y < height) {
		return (objects[x][y] != NULL);
	}
	
	cerr << "Invalid location in isOccupued {" << x << ", " << y << "}" << endl;
	throw exception();
}

/**
 * Gets the object at the given location
 *
 * @param x - the x location to check
 * @param y - the y location to check
 * @return the object at the given locatio, NULL if none
 */
Object * Simulation::getObject(int x, int y) const {
	return objects[x][y];
}

/**
 * Gets a vector of properties at the given location
 *
 * @param x - the x location to check
 * @param y - the y location to check
 * @return a vector of properties that are in the given location
 */
const vector<Property *> Simulation::getProperties(int x, int y) const {
	return properties[x][y];
}

/**
 * Adds the given property to this simulation
 *
 * @param the property to add
 */
void Simulation::addProperty(Property *prop) {
	int x = (*prop).getXLoc();
	int y = (*prop).getYLoc();
	
	if (!initializedObjects && width > 0 && height > 0) {
		initializeArrays();
	} else if (width < 0 || height < 0) {
		cerr << "Must initialize height and width before adding a property" << endl;
		return;
	}
	
	properties[x][y].push_back(prop);
}

/**
 * Gets a list of objects
 *
 * @return the object currently in the simulation
 */
const vector<Object *> Simulation::getObjects() const {
	vector<Object *> objs;
	
	if (initializedObjects) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {				
				if (objects[x][y] != NULL) {
					objs.push_back(objects[x][y]);
				}
			}
		}
	}
	
	return objs;
}

/**
 * Gets a list of properties
 *
 * @return the object currently in the simulation
 */
const vector< vector<Property *> > Simulation::getProperties() const {
	vector< vector<Property *> > props;
	
	if (initializedObjects) {		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				if (properties[x][y].size() > 0) {
					for (unsigned int i = 0; i < properties[x][y].size(); i++) {
						props.push_back(properties[x][y]);
					}
				}
				
			}
		}
	}
	
	return props;
}

/**
 * Gets a list of items that can be seen at the given location
 *
 * @param xloc - the x location to look at
 * @param yloc - the y location to look at
 * @return list of items that can be seen at the given location
 */
const vector <const Location *> Simulation::getSeenItems(int xloc, int yloc) const {
	vector<const Location *> seen;
	
	// Straight to left
	if ((xloc - 1) >= 0) {
		if (objects[xloc - 1][yloc] != NULL) {
			seen.push_back(objects[xloc - 1][yloc]);
		}
		
		if (properties[xloc - 1][yloc].size() > 0) {
			for (unsigned int i = 0; i < properties[xloc - 1][yloc].size(); i++) {
				seen.push_back(properties[xloc - 1][yloc][i]);
			}
		}
	}
	// Upper left
	if ((xloc - 1) >= 0 && (yloc - 1) > 0) {
		if (objects[xloc - 1][yloc - 1] != NULL) {
			seen.push_back(objects[xloc - 1][yloc - 1]);
		}
		
		if (properties[xloc - 1][yloc - 1].size() > 0) {
			for (unsigned int i = 0; i < properties[xloc - 1][yloc - 1].size(); i++) {
				seen.push_back(properties[xloc - 1][yloc - 1][i]);
			}
		}
	}
	// Straight Up
	if ((yloc - 1) >= 0) {
		if (objects[xloc][yloc - 1] != NULL) {
			seen.push_back(objects[xloc][yloc - 1]);
		}
		
		if (properties[xloc][yloc - 1].size() > 0) {
			for (unsigned int i = 0; i < properties[xloc][yloc - 1].size(); i++) {
				seen.push_back(properties[xloc][yloc - 1][i]);
			}
		}
	}
	// Upper Right
	if ((xloc + 1) < width && (yloc - 1) >= 0) {
		if (objects[xloc + 1][yloc - 1] != NULL) {
			seen.push_back(objects[xloc + 1][yloc - 1]);
		}
		
		if (properties[xloc + 1][yloc - 1].size() > 0) {
			for (unsigned int i = 0; i < properties[xloc + 1][yloc - 1].size(); i++) {
				seen.push_back(properties[xloc + 1][yloc - 1][i]);
			}
		}
	}
	// Right
	if ((xloc + 1) < width) {
		if (objects[xloc + 1][yloc] != NULL) {
			seen.push_back(objects[xloc + 1][yloc]);
		}
		
		if (properties[xloc + 1][yloc].size() > 0) {
			for (unsigned int i = 0; i < properties[xloc + 1][yloc].size(); i++) {
				seen.push_back(properties[xloc + 1][yloc][i]);
			}
		}
	}
	// Lower Right
	if ((xloc + 1) < width && (yloc + 1) < height) {
		if (objects[xloc + 1][yloc + 1] != NULL) {
			seen.push_back(objects[xloc + 1][yloc + 1]);
		}
		
		if (properties[xloc + 1][yloc + 1].size() > 0) {
			for (unsigned int i = 0; i < properties[xloc + 1][yloc + 1].size(); i++) {
				seen.push_back(properties[xloc + 1][yloc + 1][i]);
			}
		}
	}
	// Lower
	if ((yloc + 1) < height) {
		if (objects[xloc][yloc + 1] != NULL) {
			seen.push_back(objects[xloc][yloc + 1]);
		}
		
		if (properties[xloc][yloc + 1].size() > 0) {
			for (unsigned int i = 0; i < properties[xloc][yloc + 1].size(); i++) {
				seen.push_back(properties[xloc][yloc + 1][i]);
			}
		}
	}
	// Lower Left
	if ((xloc - 1) >= 0 && (yloc + 1) < height) {
		if (objects[xloc - 1][yloc + 1] != NULL) {
			seen.push_back(objects[xloc - 1][yloc + 1]);
		}
		
		if (properties[xloc - 1][yloc + 1].size() > 0) {
			for (unsigned int i = 0; i < properties[xloc - 1][yloc + 1].size(); i++) {
				seen.push_back(properties[xloc - 1][yloc + 1][i]);
			}
		}
	}
	
	return seen;
}

/**
 * Gets the robot that is straight ahead
 *
 * @param xloc - the x location to look at
 * @param yloc - the y location to look at
 * @param direction - the direction to look in
 * @return the robot that is straight ahead, NULL if none, or another object is in the way
 */
Object * Simulation::getRobotStraightAhead(int xloc, int yloc, int direction) const {
	if (direction == NORTH) {
		for (int i = yloc; i >= 0; i--) {
			if (objects[xloc][i] != NULL && objects[xloc][i]->getConfigType() == "robot") {
				return objects[xloc][i];
			} else if (objects[xloc][i] != NULL) {
				return NULL;
			}
		}
	} else if (direction == SOUTH) {
		for (int i = yloc; i < height; i++) {
			if (objects[xloc][i] != NULL && objects[xloc][i]->getConfigType() == "robot") {
				return objects[xloc][i];
			} else if (objects[xloc][i] != NULL) {
				return NULL;
			}
		}
	} else if (direction == WEST) {
		for (int i = xloc; i >= 0; i--) {
			if (objects[i][yloc] != NULL && objects[i][yloc]->getConfigType() == "robot") {
				return objects[i][yloc];
			} else if (objects[i][yloc] != NULL) {
				return NULL;
			}
		}
	} else if (direction == EAST) {
		for (int i = xloc; i < width; i++) {
			if (objects[i][yloc] != NULL && objects[i][yloc]->getConfigType() == "robot") {
				return objects[i][yloc];
			} else if (objects[i][yloc] != NULL) {
				return NULL;
			}
		}
	}
	
	return NULL;
}

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
int * Simulation::probe(int xloc, int yloc, int dir, int probeWidth) const {
	int *list = (int *) malloc(probeWidth * sizeof(int));
	
	// Initialize the array we are going to return
	for (int i = 0; i < probeWidth; i++) {
		list[i] = 0;
	}
	
	for (int i = -(probeWidth / 2), count = 0; i <= (probeWidth / 2); i++, count++) {
		
		if (dir == NORTH || dir == SOUTH) {
			if (dir == NORTH) {
				int x = xloc + i;
				
				bool cont = true;
				// For each location north of the current location
				for (int y = yloc - 1; cont && y > 0; y--) {
					
					// If we find a location with properties
					if (properties[x][y].size() > 0) {
						// For each property on that location
						for (unsigned int j = 0; j < properties[x][y].size(); j++) {
							// If we ran into a jammer, set dist to 0 and break out of this
							// column
							if (properties[x][y][j]->getType() == "jammer") {
								list[count] = 0;
								cont = false;
								break;
							} else if (properties[x][y][j]->isProbable()) {
								// If didn't run into a jammer, and this property is probable,
								// we will set the distance to the current distance
								list[count] = yloc - y;
							}
						}
					}
					// If we find didn't find a property that would prevent us to check
					// objects, we will check to see if there is a probably object here.
					// If there is a probable object, we set the distance to this location,
					// and then stop looking in this column
					if (cont && objects[x][y] != NULL && objects[x][y]->isProbable()) {
						list[count] = yloc - y;
						break;
					}
				}
			} else {
				int x = xloc - i;
				bool cont = true;
				// For each location south of the current location
				for (int y = yloc + 1; cont && y < height; y++) {
					// If we find a location with properties
					if (properties[x][y].size() > 0) {
						// For each property on that location
						for (unsigned int j = 0; j < properties[x][y].size(); j++) {
							// If we ran into a jammer, set dist to 0 and break out of this
							// column
							if (properties[x][y][j]->getType() == "jammer") {
								list[count] = 0;
								cont = false;
								break;
							} else if (properties[x][y][j]->isProbable()) {
								// If didn't run into a jammer, and this property is probable,
								// we will set the distance to the current distance
								list[count] = y - yloc;
							}
						}
					}
					// If we find didn't find a property that would prevent us to check
					// objects, we will check to see if there is a probably object here.
					// If there is a probable object, we set the distance to this location,
					// and then stop looking in this column
					if (cont && objects[x][y] != NULL && objects[x][y]->isProbable()) {
						list[count] = y - yloc;
						break;
					}
				}
			}
			
		} else if (dir == EAST || dir == WEST) {
			if (dir == WEST) {
				int y = yloc - i;
				
				bool cont = true;
				// For each location west of the current location
				for (int x = xloc - 1; cont && x > 0; x--) {
					
					// If we find a location with properties
					if (properties[x][y].size() > 0) {
						// For each property on that location
						for (unsigned int j = 0; j < properties[x][y].size(); j++) {
							// If we ran into a jammer, set dist to 0 and break out of this
							// column
							if (properties[x][y][j]->getType() == "jammer") {
								list[count] = 0;
								cont = false;
								break;
							} else if (properties[x][y][j]->isProbable()) {
								// If didn't run into a jammer, and this property is probable,
								// we will set the distance to the current distance
								list[count] = xloc - x;
							}
						}
					}
					// If we find didn't find a property that would prevent us to check
					// objects, we will check to see if there is a probably object here.
					// If there is a probable object, we set the distance to this location,
					// and then stop looking in this column
					if (cont && objects[x][y] != NULL && objects[x][y]->isProbable()) {
						list[count] = xloc - x;
						break;
					}
				}
			} else {
				int y = yloc + i;
				
				bool cont = true;
				// For each location east of the current location
				for (int x = xloc + 1; cont && x < width; x++) {
					// If we find a location with properties
					if (properties[x][y].size() > 0) {
						// For each property on that location
						for (unsigned int j = 0; j < properties[x][y].size(); j++) {
							// If we ran into a jammer, set dist to 0 and break out of this
							// column
							if (properties[x][y][j]->getType() == "jammer") {
								list[count] = 0;
								cont = false;
								break;
							} else if (properties[x][y][j]->isProbable()) {
								// If didn't run into a jammer, and this property is probable,
								// we will set the distance to the current distance
								list[count] = x - xloc;
							}
						}
					}
					// If we find didn't find a property that would prevent us to check
					// objects, we will check to see if there is a probably object here.
					// If there is a probable object, we set the distance to this location,
					// and then stop looking in this column
					if (cont && objects[x][y] != NULL && objects[x][y]->isProbable()) {
						list[count] = x - xloc;
						break;
					}
				}
			}
		}
	}
	
	return list;
}

/**
 * Tries to move the given object in the direction given.
 *
 * @param *objInWay - the object that is to be moved
 * @param direction - the direction to move the object in
 * @return whether the object could be moved or not
 */
bool Simulation::moveObject(Object * objInWay, int direction) {
	// If objInWay is null, there isn't anything there, so 
	// we are able to move
	if (objInWay == NULL) {
		return true;
	}
	
	// If the current object isn't movable, return false
	if (!objInWay->isMovable()) {
		return false;
	}
	
	// Since we know the object isn't null, and it is movable, 
	// we will recursively check the next square. If the same is true
	// about the next square, we will actually perform the move
	
	if (direction == NORTH) {
		if (objInWay->getYLoc() - 1 >= 0 && moveObject(getObject(objInWay->getXLoc(), objInWay->getYLoc() - 1), direction)) {
			// Actually move the object
			objInWay->setYLoc(objInWay->getYLoc() - 1);
			updateObject(objInWay, objInWay->hasEnergy());
			return true;
		}
	} else if (direction == SOUTH) {
		if (objInWay->getYLoc() + 1 < height && moveObject(getObject(objInWay->getXLoc(), objInWay->getYLoc() + 1), direction)) {
			// Actually move the object
			objInWay->setYLoc(objInWay->getYLoc() + 1);
			updateObject(objInWay, objInWay->hasEnergy());
			return true;
		}
	} else if (direction == WEST) {
		if (objInWay->getXLoc() - 1 >= 0 && moveObject(getObject(objInWay->getXLoc() - 1, objInWay->getYLoc()), direction)) {
			// Actually move the object
			objInWay->setXLoc(objInWay->getXLoc() - 1);
			updateObject(objInWay, objInWay->hasEnergy());
			return true;
		}
	} else if (direction == EAST) {
		if (objInWay->getXLoc() + 1 < width && moveObject(getObject(objInWay->getXLoc() + 1, objInWay->getYLoc()), direction)) {
			// Actually move the object
			objInWay->setXLoc(objInWay->getXLoc() + 1);
			updateObject(objInWay, objInWay->hasEnergy());
			return true;
		}
	}
	
	// If we get here, that means that we reached an edge, or eventually an immovable object
	return false;
}

/**
 * Gets the configuration string for this simulation
 *
 * @return the string representation of this simulation's configuration data
 */
string Simulation::getConfString() const {
	stringstream out;
	
	out << "<Simulation>" << endl;
	out << "width = " << width << endl;
	out << "height = " << height << endl;
	out << "</Simulation>" << endl;
	
	for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			
			if (objects[x][y] != NULL) {
				out << (*objects[x][y]).getConfString();
			}
			
			if (properties[x][y].size() > 0) {
				for (unsigned int i = 0; i < properties[x][y].size(); i++) {
					out << (*(properties[x][y])[i]).getConfString();
				}
			}
		}
	}
	
	return out.str();
}

/**
 * Prints out the necessary object data
 *
 * @return the string representation of all the objects in the simulation
 */
string Simulation::printObjectData() const {
	stringstream out;
	
	for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			bool printedLoc = false;
			
			if (objects[x][y] != NULL) {
				out << "Location: " << (*objects[x][y]).getYLoc() << ", " << (*objects[x][y]).getXLoc() << endl;
				out << (*objects[x][y]).toString();
				
				printedLoc = true;
			}
			
			if (properties[x][y].size() > 0) {
				if (!printedLoc) {
					out << "Location: " << (*(properties[x][y])[0]).getYLoc() << ", " << (*(properties[x][y])[0]).getXLoc() << endl;
					printedLoc = true;
				}
				for (unsigned int i = 0; i < properties[x][y].size(); i++) {
					out << (*(properties[x][y])[i]).toString();
				}
			}
			
			if (printedLoc) {
				out << endl;
			}
		}
	}
	
	return out.str();
}

/**
 * Returns a string representation of this simulation
 *
 * @param a string representation of this simulation
 */
string Simulation::toString() const {
	stringstream out;
	
	for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			if (objects[x][y] != NULL) {
				out << (*objects[x][y]).getDisplay();
			} else {
				out << "__";
			}
			
			if (properties[x][y].size() == 1) {
				out << (*(properties[x][y])[0]).getDisplay();
			} else if (properties[x][y].size() > 1) {
				out << "XX";
			} else {
				out << "__";
			} 
			
			out << " ";
		}
		out << endl;
	}
	
	return out.str();
}
