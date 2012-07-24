#include "movement.h"
#include "util.h"
#include "irobotsensors.h"

// Turns the robot to the left the specified number of degrees
/**
 * Takes degrees relative to left direction, ie. 90 degrees left rather than 270
 *
 * @param degrees - the number of degrees to rotate
 */
void turn_left(oi_t *self, int degrees){
	oi_clear_angle(self);
	oi_set_wheels(100, -100);
	
	if (degrees != 0) {
		int angle = oi_current_angle(self);
		while(angle < (degrees / 2)) {
			angle = oi_current_angle(self);
		}	

		oi_set_wheels(0, 0);
	}
}

// Turns the robot to the right the specified number of degrees
/**
 * Takes degrees relative to left direction
 *
 * @param degrees - the number of degrees to rotate
 */
void turn_right(oi_t *self, int degrees){
	oi_clear_angle(self);
	oi_set_wheels(-100, 100);

	if(degrees != 0){
		int angle = oi_current_angle(self);
		while(angle>(360-degrees/2) || angle == 0) {
			angle = oi_current_angle(self);
		}
		oi_set_wheels(0,0);
	}
}

// Moves the robot forward the given number of centimeters
/**
 * Moves the robot the given number of centimeters
 *
 * @param *self - the robot object to move
 * @param distance - the distance in cm to move
 * @return the value of the sensor status
 */
int move_forward(oi_t *self, int distance){
	int sense = 0;
	oi_clear_distance(self);
	oi_set_wheels(200, 200);
	if (distance != 0) {
		while (oi_current_distance(self) < distance) {
			sense = checkSensors();
			if (sense) {
				return sense;
			}
		}
	}
	
	oi_set_wheels(0, 0);
	return 0;
}

// Moves the robot backward the given number of centimeters
/**
 * Moves the robot the given number of centimeters backwards.
 *
 * Note, this does not check sensor values, so be careful backing up!
 *
 * @param *self - the robot object to move
 * @param distance - the distance in cm to move
 * @return 0
 */
int move_backward(oi_t *self, int distance){
	int sense = 0;
	oi_clear_distance(self);
	oi_set_wheels(-100, -100);
	if (distance != 0){
		while (oi_current_distance(self) > distance);
	}
	
	oi_set_wheels(0, 0);
	return 0;
}

