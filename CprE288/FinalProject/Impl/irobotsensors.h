#ifndef __IROBOTSENSORS_H__
#define __IROBOTSENSORS_H__

#include "open_interface.h"
#include "usart.h"
#include "sonar.h"
#include <stdio.h>

/// Value returned from check sensors if a left cliff was detected
#define CLIFF_LEFT 100
/// Value returned from check sensors if a right cliff was detected
#define CLIFF_RIGHT 150
/// Value returned from check sensors if a virtual wall was detected
#define VIRTUAL_WALL 200
/// Value returned from check sensors if a right wheel drop was detected
#define WHEEL_RIGHT 250
/// Value returned from check sensors if a left wheel drop was detected
#define WHEEL_LEFT 300
/// Value returned from check sensors if a wheel caster drop was detected
#define WHEEL_CASTER 350
/// Value returned from check sensors if a right bumper was detected
#define BUMPER_RIGHT 400
/// Value returned from check sensors if a left bumper was detected
#define BUMPER_LEFT 450
/// Value returned from check sensors if a center bumper was detected
#define BUMPER_CENTER 500


/// The robot object used for communication/sensor reading
oi_t *iBambi;

/// Checks all of the sensors on the iRobot body (Cliff, Bump, Wheel drop, Virtual Wall) and stops
/// the robot if it detects any of these sensors.
/**
 *	Checks the sensor son the iRobot and stops movement if it detects a condition where it
 *	needs to stop. If it detects such an event, it will transmit a description via bluetooth to the computer
 *
 *	@return the constant value (as defined in irobotsensors.h) defining which sensor caused the stop. 0 if
 *			the robot didn't stop
 */
int checkSensors();

#endif

