/**
 * lava.h
 *
 * @author Brian Reber
 *
 * A Lava class, containing all the Lava specific information.
 */
#ifndef LAVA_H
#define LAVA_H

#include <string>
#include "property.h"
#include "energy.h"
#include <list>

using namespace std;

class Lava : public Property, public IEnergy {
	private:
		int energyCost;
		bool energyCostDef;
	public: 
		/**
		 * Creates a lava with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		Lava(list<string> params);
	
		/**
		 * Provide a virtual Deconstructor to allow for the virtual method
		 */
		~Lava() { };
	
		/**
		 * Gets the energy cost of this lava
		 *
		 * @return the energy cost of this lava
		 */
		int getEnergyCost() const {
			return energyCost;
		}
		
		/**
		 * Sets the energy cost of this lava
		 *
		 * @param the energy cost of this lava
		 */
		void setEnergyCost(int en_cost);
	
		/**
		 * @Override
		 *
		 * Gets the energy contents of this energy pill
		 *
		 * @return the energy contents of this energy pill
		 */
		virtual double getEnergy() const {
			return energyCost;
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
