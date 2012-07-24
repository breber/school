/**
 * robot.h
 *
 * @author Brian Reber
 *
 * A Robot class, containing all the Robot specific information.
 */
#ifndef ROBOT_H
#define ROBOT_H

#include "object.h"
#include "energy.h"
#include <string>
#include <list>

using namespace std;

class Robot : public Object, public IEnergy {
	private:
		double energyContents;
		int rechargeAmt;
		int movecost;
		int turncost;
		int probecost;
		double paramA;
		double paramB;
		double paramC;
		int dir;
	
		bool energy_contentsDef;
		bool rechargeDef;
		bool movecostDef;
		bool turncostDef;
		bool probecostDef;
		bool paramADef;
		bool paramBDef;
		bool paramCDef;
		bool dirDef;
	public:
		/**
		 * Creates a Robot with all the parameters in the given
		 * list.
		 * 
		 * @param params - a list of parameters according to the
		 *			given spec.
		 */
		Robot(list<string> params);
		
		/**
		 * Gets the direction of this robot
		 * 
		 * @return the direction this robot is pointing
		 */
		int getDir() const {
			return dir;
		}
	
		/**
		 * Sets the direction of this robot
		 *
		 * @param the direction of this robot
		 */
		void setDir(int dir);
	
		/**
		 * Gets the energy contents of this robot
		 *
		 * @return the energy contents of this robot
		 */
		double getEnergyContents() const {
			return energyContents;
		}
	
		/**
		 * Sets the energy contents of this robot
		 *
		 * @param the energy contents of this robot
		 */
		void setEnergyContents(double en_cont);
	
		/**
		 * Gets the recharge value of this robot
		 *
		 * @return the recharge value of this robot
		 */
		int getRecharge() const {
			return rechargeAmt;
		}
	
		/**
		 * Sets the recharge value of this robot
		 *
		 * @param the recharge value of this robot
		 */
		void setRecharge(int rechar);
	
		/**
		 * Gets the cost to move this robot
		 *
		 * @return the cost to move this robot
		 */
		int getMoveCost() const {
			return movecost;
		}
	
		/**
		 * Sets the cost to move this robot
		 *
		 * @param the cost to move this robot
		 */
		void setMoveCost(int cost);
	
		/**
		 * Gets the cost to turn this robot
		 *
		 * @return the cost to turn this robot
		 */
		int getTurnCost() const {
			return turncost;
		}
	
		/**
		 * Sets the cost to turn this robot
		 *
		 * @param the cost to turn this robot
		 */
		void setTurnCost(int cost);
	
		/**
		 * Gets the cost to probe
		 *
		 * @return the cost to probe
		 */
		int getProbeCost() const {
			return probecost;
		}
	
		/**
		 * Sets the cost to probe
		 *
		 * @param the cost to probe
		 */
		void setProbeCost(int cost);
	
		/**
		 * Gets the value of paramA
		 *
		 * @return the value of paramA
		 */
		double getParamA() const {
			return paramA;
		}
	
		/**
		 * Sets the value of paramA
		 *
		 * @param the value of paramA
		 */
		void setParamA(double param);
	
		/**
		 * Gets the value of paramB
		 *
		 * @return the value of paramB
		 */
		double getParamB() const {
			return paramB;
		}
	
		/**
		 * Sets the value of paramB
		 *
		 * @param the value of paramB
		 */
		void setParamB(double param);
	
		/**
		 * Gets the value of paramC
		 *
		 * @return the value of paramC
		 */
		double getParamC() const {
			return paramC;
		}
	
		/**
		 * Sets the value of paramC
		 *
		 * @param the value of paramC
		 */
		void setParamC(double param);
	
		/**
		 * Recharges the robot by the correct amount
		 */
		void recharge();
	
		/**
		 * Gets the cost to fire with the given energy
		 *
		 * @param e - the energy value
		 * @return the cost to fire
		 */
		double getFireCost(int e) const {
			return paramA + (paramB + e);
		}
	
		/**
		 * Moves the robot forward in direction pointing
		 */
		void moveForward(int xSim, int ySim);
	
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
