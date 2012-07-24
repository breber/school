#ifndef __SONAR_H__
#define __SONAR_H__

/// Initialize the sonar sensor for input capture
/**
 *	Initializes the correct registers to set up the
 *	sonar device for capture
 */
void init_sonar();

/// Gets the sonar distance for the current position
/**
 *	Gets the sonar distance for the current position
 *
 *	@return the distance measurement in mm for the current position
 */
unsigned long get_sonar_distance();

#endif
