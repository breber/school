/*********************************************************************
 *
 *
*********************************************************************/

#define F_CPU 16000000UL
#include <inttypes.h>
#include <avr/io.h>
#include <string.h>
#include <stdio.h>
#include "lcd.h"
#include "util.h"

int 
main (void)
{
	stepper_init();
	shaft_encoder_init();

	wait_ms(200);

	while (1) {
		// Get the value of the shaft encoder (1 or -1)
		// and then pass that into the move stepper motor
		// funtion to get it to move in the appropriate direction
		int value = ((int) read_shaft_encoder());

		move_stepper_motor_by_step(10, value);
	}

	return 0;
}
