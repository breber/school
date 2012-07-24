/**
 * object.cpp
 *
 * @author Brian Reber
 *
 * An abstract Object class, containing information applicable to 
 * all types of objects
 */
#include "object.h"
#include <sstream>

/**
 * Sets the whether this object is movable
 *
 * @param mov - whether this object is movable
 */
void Object::setMovable(bool mov) {
	movable = mov;
}

/**
 * Sets whether the color of this object has been defined
 *
 * @param the color of this object has been defined
 */
void Object::setColorDef(bool col) {
	colorDef = col;
}

/**
 * Sets the color of this object
 *
 * @param the color of this object
 */
void Object::setColor(string col) {
	color = col;
}

/**
 * Returns a string representation of this object
 *
 * @return a string representation of this object
 */
string Object::toString() const {
	return Location::toString();
}
