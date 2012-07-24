#include "scan.h"
#include "usart.h"
#include "servo.h"
#include "sonar.h"
#include "ir.h"
#include "util.h"
#include <stdio.h>
#include <math.h>

/// Raw data from IR sensor
unsigned int ir[181];
/// Raw data from sonar sensor
unsigned int sonar[181];

/// The number of sonar objects found
int sonarobject = 0;
/// The number of IR objects found
int irobject = 0;

/// Sonar object starting locations
int sostart[10];
/// Sonar object widths
int sowidth[10];
/// Sonar object distances
int sodistance[10];

/// IR object starting locations
int irstart[10];
/// IR object widths
int irwidth[10];
/// IR object distances
int irdistance[10];

/// Clears all necessary variables for the sweep
/**
 *	Clears all necessary variables for the sweep
 */
void clearData() {
	int i;
	
	for (i = 0; i < 181; i++) {
		ir[i] = 0;
		sonar[i] = 0;
	}
	
	sonarobject = 0;
	irobject = 0;
	
	for (i = 0; i < 10; i++) {
		sostart[i] = 0;
		sowidth[i] = 0;
		sodistance[i] = 0;
		irstart[i] = 0;
		irwidth[i] = 0;
		irdistance[i] = 0;
	}
}

/// Performs a sweep of the servo, collecting sonar and IR data, finds the objects, analyzes them, and sends necessary data back via bluetooth
/**
 *	Sweeps 180 degrees on the servo, collects and analyzes sonar and IR data,
 *	and sends back information about it via bluetooth
 */
void performSweepAnalysis() {
	int degreeOfSmallest = 90;

	// Clear out arrays
	clearData();
	
	// Collect data
	sweep();

	// Find objects
	sonar_analyze();
	ir_analyze();

	// Find the smallest objects
	degreeOfSmallest = find_smallest();

	// Send data back via bluetooth
	send_found_objects();
}

/// Performs a sweep of the servo and collects data at each degree
/**
 *	Performs a sweep of the servo and collects data at each degree
 */
void sweep() {
	init_servo();
	move_servo_degrees(0);
	init_sonar();

	wait_ms(500);
	
	int i;
	for(i = 0; i < 181; i++) {
		move_servo_degrees(i);
		wait_ms(50);
		sonar[i] = get_sonar_distance();
		ir[i] = get_ir_distance();
	}
}

/// Analyzes the collected Sonar data in order to find objects
/**
 *	Analyzes the collected Sonar data to find objects
 */
void sonar_analyze() {
	int i;
	//discover objects
	for (i = 0; i < 181; i++) {
		if (sonar[i] < 80) {
			int k = get_sonar_duration(i);
			if (k > 5) {
				sostart[sonarobject] = i;
				sowidth[sonarobject] = k;
				sodistance[sonarobject] = get_sonar_average(i, i + k);
				i += k;
				sonarobject++;
			}
		}
	}
}

/// Analyzes the collected IR data in order to find objects
/**
 *	Analyzes the collected IR data to find objects
 */
void ir_analyze() {
	int i;
	//discover objects
	for(i = 0; i < 181; i++) {
		if (ir[i] < 80) {
			int k = get_ir_duration(i);
			if (k > 2) {
				irstart[irobject] = i;
				irwidth[irobject] = k;
				irdistance[irobject] = get_ir_average(i, i + k);
				i += k;
				irobject++;
			}
		}
	}
}

/// Determines the number of degrees wide an object is based on IR
/**
 *	Starting at the start degree, analyze the IR data for the current position
 *	and determine whether it is still considered an object, or whether it has
 *	passed the edge of the object.
 *
 *	@param start - the degree to start analyzing at
 *	@return the number of degrees wide the object is
 */
int get_ir_duration(int start) {
	int end = start;
	while(end < 180 && (ir[end] < 80 && ir[end+1] < 80)) {
		end++;
	}
	return end - start;
}

/// Determines the number of degrees wide an object is based on Sonar
/**
 *	Starting at the start degree, analyze the Sonar data for the current position
 *	and determine whether it is still considered an object, or whether it has
 *	passed the edge of the object.
 *
 *	@param start - the degree to start analyzing at
 *	@return the number of degrees wide the object is
 */
int get_sonar_duration(int start) {
	int end = start;
	while(end < 180 && (sonar[end] < 80 && sonar[end + 1] < 80)) {
		end++;
	}
	return end - start;
}

/// Calculates the average IR value over the given range
/**
 *	Calculates the average IR value over the given range
 *
 *	@param start - the starting angle
 *	@param end - the ending angle
 *	@return the average distance recorded by the IR sensor
 */
int get_ir_average(int start, int end) {
	int total = 0;
	int i = start;
	while (i <= end) {
		total += ir[i];
		i++;
	}
	
	return total / (end - start + 1);
}

/// Calculates the average Sonar value over the given range
/**
 *	Calculates the average Sonar value over the given range
 *
 *	@param start - the starting angle
 *	@param end - the ending angle
 *	@return the average distance recorded by the sonar sensor
 */
int get_sonar_average(int start, int end) {
	int total = 0;
	int i = start;
	while(i <= end) {
		total += sonar[i];
		i++;
	}
	return total / (end - start + 1);
}

/// Finds the smallest object based on both the sonar and IR data, and points the servo to the middle of that object
/**
 *	Finds the smallest object based on both the sonar and IR data, and
 *	points the servo to the middle of that object
 *
 *	@return the degree of the smallest object
 */
int find_smallest() {
	//determine smallest object
	int i;
	int smallest = 0;
	double value = 1.0 * irwidth[0] * sonar[irstart[0] + irwidth[0] / 2];
	
	for(i = 1; i < irobject; i++) {
		if(value > 1.0 * irwidth[i] * sonar[irstart[i] + irwidth[i] / 2]) {
			smallest = i;
			value = 1.0 * irwidth[i] * sonar[irstart[i] + irwidth[i] / 2];
		}
	}

	//point to smallest object
	move_servo_degrees(irstart[smallest]+irwidth[smallest]/2);

	return irstart[smallest]+irwidth[smallest]/2;
}

/// Gets the width of an IR object based on its distance and width in degrees
/**
 *	Gets the width in centimeters of an IR object based on its width in degrees and its
 *	discance in centimeters
 *
 *	@param object_num - the IR object number to find the width of
 *	@return the width in centimeters of the IR object at the given index
 */
int get_width_in_cm(int object_num) {
	return irwidth[object_num] * 3.1415 / 180 * (sonar[irstart[object_num] + irwidth[object_num] / 2] + 6);
}

/// For each found object in both IR and Sonar, send back the width, angle, and distance of the object via bluetooth
/**
 *	For each found object in both IR and Sonar, send back the width,
 *	angle, and distance of the object via bluetooth
 */
void send_found_objects() {
	usart_init();
	
	int i;

	usart_transmit("IR DATA\n\r");

	for(i = 0; i < irobject; i++) {
		char transmit[100];
		sprintf(transmit, "IRObject %3d: WidthD %3d  WidthCM %3d  Angle %3d   Dist %3d\n\r", i, irwidth[i], get_width_in_cm(i), irstart[i] + irwidth[i] / 2, irdistance[i]);
		usart_transmit(transmit);
	}

	usart_transmit("SONAR DATA\n\r");

	for(i = 0; i < sonarobject; i++) {
		char transmit[100];
		sprintf(transmit, "SoObject %3d: WidthD %3d   Angle %3d   Dist %3d\n\r", i, sowidth[i], sostart[i] + sowidth[i] / 2, sodistance[i]);
		usart_transmit(transmit);
	}
}
