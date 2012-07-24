/**
 * property.cpp
 *
 * @author Brian Reber
 *
 * An abstract Property class, containing information applicable to 
 * all types of Properties
 */
#include "property.h"
#include <sstream>

/**
 * Returns a string representation of this property
 *
 * @return a string representation of this property
 */
string Property::toString() const {
	return Location::toString();
}
