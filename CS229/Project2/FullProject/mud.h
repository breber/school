/**
 * mud.h
 *
 * @author Brian Reber
 *
 * A Mud class, containing all the Mud specific information.
 */
#ifndef MUD_H
#define MUD_H

#include <string>
#include "property.h"
#include "energy.h"
#include <list>

using namespace std;

class Mud : public Property, public IEnergy {
	private:
		int energyCost;
		int turnCost;
		
		bool energyCostDef;
		bool turnCostDef;
	public: 
		/**
		 * Creates a mud with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		Mud(list<string> params);
	
		/**
		 * Gets the energy cost of this mud
		 *
		 * @return the energy cost of this mud
		 */
		int getEnergyCost() const {
			return energyCost;
		}
		
		/**
		 * Sets the energy cost of this mud
		 *
		 * @param the energy cost of this mud
		 */
		void setEnergyCost(int en_cost);
	
		/**
		 * Gets the turn cost of this mud
		 *
		 * @return the turn cost of this mud
		 */
		int getTurnCost() const {
			return turnCost;
		}
		
		/**
		 * Sets the turn cost of this mud
		 *
		 * @param the turn cost of this mud
		 */
		void setTurnCost(int turn_cost);
	
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
