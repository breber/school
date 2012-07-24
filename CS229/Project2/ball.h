/**
 * ball.h
 *
 * @author Brian Reber
 *
 * A Ball class, containing all the Ball specific information.
 */
#ifndef BALL_H
#define BALL_H

#include "object.h"
#include <string>
#include <list>

using namespace std;

class Ball : public Object {
	public:
		/**
		 * Creates a ball with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		Ball(list<string> params);
	
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
