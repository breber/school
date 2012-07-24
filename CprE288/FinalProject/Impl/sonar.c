#include <avr/io.h>
#include <avr/interrupt.h>
#include "util.h"

/// The start value (rising edge)
unsigned long start;

/// The distance in cm of the object based on pulse width
unsigned long distance;

/// Interrupt Handler for Timer/Counter1, used for Sonar data collection
ISR (TIMER1_CAPT_vect) {
	unsigned long end;
	unsigned long width;
	unsigned long time;
	
	if (TCCR1B & (1<<ICES)) {
		start = ICR1;
	} else {
		end = ICR1;
		if (end > start) {
			width = end - start;
		} else {
			width = end + (65535 - start);
		}
		time = width * 16;
		distance = time * 17 / 1000;
	}
	
	TCCR1B ^= (1<<ICES);
}

/// Initialize the sonar sensor for input capture
/**
 *	Initializes the correct registers to set up the
 *	sonar device for capture
 */
void init_sonar() {
	// initialize the TCCRnA: Control Register A
	TCCR1A |= 0;
	
	// initialize the TCCRnB: Control Register B
	TCCR1B |= (1<<ICNC)|(1<<ICES)|(1<<CS12);
	
	// initialize the TCCRnC: Control Register C
	TCCR1C |= 0;
	
	// initialize the TIMSK: Timer/Counter Interrupt Mask
	TIMSK |= (1<<TICIE1);
	
	// change Port D direction to output
	DDRD |= (1<<PD4);
	// initialize port D to NOT be triggering an event
	PORTD=0;
	
	sei(); // enable interrupt
}

/// Gets the sonar distance for the current position
/**
 *	Gets the sonar distance for the current position
 *
 *	@return the distance measurement in mm for the current position
 */
unsigned long get_sonar_distance() {
	// change Port D direction to output
	DDRD = 1<<PD4;
	// Trigger an Input Capture event from Port D
	PORTD = 1<<PD4;
	wait_ms(1); // Hold the trigger high for 1ms
	// Unset the Input Capture event from Port D
	PORTD = 0;
	// change Port D direction to input
	DDRD = 0;
	wait_ms(20);
	return distance;
}
