#include "irobotsensors.h"

/// Checks all of the sensors on the iRobot body (Cliff, Bump, Wheel drop, Virtual Wall) and stops
/// the robot if it detects any of these sensors.
/**
 *	Checks the sensor son the iRobot and stops movement if it detects a condition where it
 *	needs to stop. If it detects such an event, it will transmit a description via bluetooth to the computer
 *
 *	@return the constant value (as defined in irobotsensors.h) defining which sensor caused the stop. 0 if
 *			the robot didn't stop
 */
int checkSensors() {
	oi_update(iBambi);
	char str[30];
	int sense = 0;

	if (iBambi->wheeldrop_right) {
		oi_set_wheels(0, 0);
		sprintf(str, "Wheel Drop Right\n\r");
		usart_transmit(str);
		sense = WHEEL_RIGHT;
	}

	if (iBambi->wheeldrop_left) {
		oi_set_wheels(0, 0);
		sprintf(str, "Wheel Drop Left\n\r");
		usart_transmit(str);
		sense = WHEEL_LEFT;
	}

	if (iBambi->wheeldrop_caster) {
		oi_set_wheels(0, 0);
		sprintf(str, "Wheel Drop Caster\n\r");
		usart_transmit(str);
		sense = WHEEL_CASTER;
	}

	if (iBambi->cliff_frontright) {
		oi_set_wheels(0, 0);
		sprintf(str, "Cliff Right Front\n\r");
		usart_transmit(str);
		sense = CLIFF_RIGHT;
	}

	if (iBambi->cliff_frontleft) {
		oi_set_wheels(0, 0);
		sprintf(str, "Cliff Left Front\n\r");
		usart_transmit(str);
		sense = CLIFF_LEFT;
	}

	if (iBambi->cliff_left) {
		oi_set_wheels(0, 0);
		sprintf(str, "Cliff Left\n\r");
		usart_transmit(str);
		sense = CLIFF_LEFT;
	}

	if (iBambi->cliff_right) {
		oi_set_wheels(0, 0);
		sprintf(str, "Cliff Right\n\r");
		usart_transmit(str);
		sense = CLIFF_RIGHT;
	}

	if (iBambi->virtual_wall) {
		oi_set_wheels(0, 0);
		sprintf(str, "Virt Wall\n\r");
		usart_transmit(str);
		sense = VIRTUAL_WALL;
	}

	if (iBambi->bumper_left && iBambi->bumper_right) {
		oi_set_wheels(0, 0);
		sprintf(str, "Front Bumper\n\r");
		usart_transmit(str);
		sense = BUMPER_CENTER;
	} else if (iBambi->bumper_left) {
		oi_set_wheels(0, 0);
		sprintf(str, "Left Bumper\n\r");
		usart_transmit(str);
		sense = BUMPER_LEFT;
	} else if (iBambi->bumper_right) {
		oi_set_wheels(0, 0);
		sprintf(str, "Right Bumper\n\r");
		usart_transmit(str);
		sense = BUMPER_RIGHT;
	}

	return sense;
}
