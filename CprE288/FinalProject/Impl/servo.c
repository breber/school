#include <avr/io.h>
#include "util.h"

/// Initialize the servo so it is ready to move
/**
 *	Initialize the servo so it is ready to move
 */
void init_servo() {
	
	// Set the port for output
	DDRE |= 1 << PE4;
	
	// Configure TCCRnA
	// Set the WGM bits and configure an output channel
	TCCR3A |= (1<<COM3B1)|(1<<WGM31)|(1<<WGM30);
	
	// Configure TCCRnB
	// Set the WGM bits and configure a prescaler
	TCCR3B |= (1<<ICNC)|(1<<ICES)|(1<<CS31)|(1<<WGM33)|(1<<WGM32);
	
	// Configure TCCRnC
	// Set this to zero
	TCCR3C |= 0;
	
	// Set your TOP value in OCR3A
	OCR3A = 43000;//0xA7F8;
}

/// Moves the servo to the given degree
/**
 *	Moves the servo to the given degree
 *
 *	@param degrees - the degree to move the servo to
 */
void move_servo_degrees(double degrees) {
	OCR3B = 18.68 * (degrees-10) + 1035;
	wait_ms(2);
}
