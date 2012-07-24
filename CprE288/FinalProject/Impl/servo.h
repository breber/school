#ifndef __SERVO_H__
#define __SERVO_H__

/// Initialize the servo so it is ready to move
/**
 *	Initialize the servo so it is ready to move
 */
void init_servo();

/// Moves the servo to the given degree
/**
 *	Moves the servo to the given degree
 *
 *	@param degrees - the degree to move the servo to
 */
void move_servo_degrees(double degrees);

#endif
