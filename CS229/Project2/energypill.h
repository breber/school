/**
 * energypill.h
 *
 * @author Brian Reber
 *
 * An Energy Pill class, containing all the Energy Pill specific information.
 */
#ifndef ENERGYPILL_H
#define ENERGYPILL_H

#include "object.h"
#include "energy.h"
#include <string>
#include <list>

using namespace std;

class EnergyPill : public Object, public IEnergy {
	private:
		int energyContents;
		bool energyDef;
	public:
		/**
		 * Creates an energy pill with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		EnergyPill(list<string> params);
	
		/**
		 * Gets the energy contents of this energy pill
		 *
		 * @return the energy contents of this energy pill
		 */
		int getEnergyContents() const {
			return energyContents;
		}
		
		/**
		 * Sets the energy contents of this energy pill
		 *
		 * @param the energy contents of this energy pill
		 */
		void setEnergyContents(int en_cont);
	
		/**
		 * @Override
		 *
		 * Gets the energy contents of this energy pill
		 *
		 * @return the energy contents of this energy pill
		 */
		virtual double getEnergy() const {
			return energyContents;
		}
	
		/**
		 * Gets whether this object has energy or not
		 *
		 * @return whether this object has energy or not
		 */
		virtual bool hasEnergy() const {
			return true;
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
