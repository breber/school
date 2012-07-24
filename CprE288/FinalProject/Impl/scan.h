#ifndef __SCAN_H__
#define __SCAN_H__

/// Clears all necessary variables for the sweep
/**
 *	Clears all necessary variables for the sweep
 */
void clearData();

/// Performs a sweep of the servo, collecting sonar and IR data, finds the objects, analyzes them, and sends necessary data back via bluetooth
/**
 *	Sweeps 180 degrees on the servo, collects and analyzes sonar and IR data,
 *	and sends back information about it via bluetooth
 */
void performSweepAnalysis();

/// Performs a sweep of the servo and collects data at each degree
/**
 *	Performs a sweep of the servo and collects data at each degree
 */
void sweep();

/// Analyzes the collected Sonar data in order to find objects
/**
 *	Analyzes the collected Sonar data to find objects
 */
void sonar_analyze();

/// Analyzes the collected IR data in order to find objects
/**
 *	Analyzes the collected IR data to find objects
 */
void ir_analyze();

/// Determines the number of degrees wide an object is based on IR
/**
 *	Starting at the start degree, analyze the IR data for the current position
 *	and determine whether it is still considered an object, or whether it has
 *	passed the edge of the object.
 *
 *	@param start - the degree to start analyzing at
 *	@return the number of degrees wide the object is
 */
int get_ir_duration(int start);

/// Determines the number of degrees wide an object is based on Sonar
/**
 *	Starting at the start degree, analyze the Sonar data for the current position
 *	and determine whether it is still considered an object, or whether it has
 *	passed the edge of the object.
 *
 *	@param start - the degree to start analyzing at
 *	@return the number of degrees wide the object is
 */
int get_sonar_duration(int start);

/// Calculates the average IR value over the given range
/**
 *	Calculates the average IR value over the given range
 *
 *	@param start - the starting angle
 *	@param end - the ending angle
 *	@return the average distance recorded by the IR sensor
 */
int get_ir_average(int start, int end);

/// Calculates the average Sonar value over the given range
/**
 *	Calculates the average Sonar value over the given range
 *
 *	@param start - the starting angle
 *	@param end - the ending angle
 *	@return the average distance recorded by the sonar sensor
 */
int get_sonar_average(int start, int end);

/// Finds the smallest object based on both the sonar and IR data, and points the servo to the middle of that object
/**
 *	Finds the smallest object based on both the sonar and IR data, and
 *	points the servo to the middle of that object
 *
 *	@return the degree of the smallest object
 */
int find_smallest();

/// Gets the width of an IR object based on its distance and width in degrees
/**
 *	Gets the width in centimeters of an IR object based on its width in degrees and its
 *	discance in centimeters
 *
 *	@param object_num - the IR object number to find the width of
 *	@return the width in centimeters of the IR object at the given index
 */
int get_width_in_cm(int object_num);

/// For each found object in both IR and Sonar, send back the width, angle, and distance of the object via bluetooth
/**
 *	For each found object in both IR and Sonar, send back the width,
 *	angle, and distance of the object via bluetooth
 */
void send_found_objects();

#endif
