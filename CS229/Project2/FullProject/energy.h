/**
 * object.h
 *
 * @author Brian Reber
 *
 * An abstract Object class, containing information applicable to 
 * all types of objects
 */
#ifndef ENERGY_H
#define ENERGY_H

using namespace std;

class IEnergy {
	public: 
		virtual ~IEnergy() { }
		/**
		 * Gets whether this object is movable or not
		 *
		 * @return whether this object is movable or not
		 */
		virtual double getEnergy() const = 0;
};

#endif
