#ifndef __IR_H__
#define __IR_H__

/// Gets the IR distance at the current servo angle
/**
 *	Samples the IR sensor 5 times (1 / ms), and calculates the distance
 *	from the sensor an object may be at.
 *
 *	@return the distance in millimeters an object is from the IR sensor
 */
unsigned int get_ir_distance();

#endif
