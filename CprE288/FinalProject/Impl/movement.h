#ifndef __MOVEMENT_H__
#define __MOVEMENT_H__

#include "open_interface.h"

// Turns the robot to the left the specified number of degrees
/**
 * Takes degrees relative to left direction, ie. 90 degrees left rather than 270
 *
 * @param degrees - the number of degrees to rotate
 */
void turn_left(oi_t *self, int degrees);

// Turns the robot to the right the specified number of degrees
/**
 * Takes degrees relative to left direction
 *
 * @param degrees - the number of degrees to rotate
 */
void turn_right(oi_t *self, int degrees);

// Moves the robot forward the given number of centimeters
/**
 * Moves the robot the given number of centimeters
 *
 * @param *self - the robot object to move
 * @param distance - the distance in cm to move
 * @return the value of the sensor status
 */
int move_forward(oi_t *self, int distance);

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
int move_backward(oi_t *self, int distance);

#endif
