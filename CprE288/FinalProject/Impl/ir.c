#include <math.h>
#include "util.h"
#include <avr/io.h>
#include <avr/interrupt.h>

/// Gets the IR distance at the current servo angle
/**
 *	Samples the IR sensor 5 times (1 / ms), and calculates the distance
 *	from the sensor an object may be at.
 *
 *	@return the distance in millimeters an object is from the IR sensor
 */
unsigned int get_ir_distance() {

	    int avgTemp = 0;
		int i, temp;

		ADCSRA = 0b11100111; //initiates ADC, selects free running mode, and sets pre scaler to 128 for 16MHz/128 = 125kHz clock
		ADMUX  = 0b11000010; // sets VREF to 2.56V, ADLAR to zero, and reads from ADC2

        for(i = 0; i < 5; i++) { //averages 5 inputs from every 50ms
			temp = ADCL; 
	        temp = temp | ADCH << 8; 
			
			avgTemp += temp;
	        wait_ms(1); 
        }
       
		avgTemp /= 5;

		return 56728 * pow((6 / 5) * avgTemp, -1.272); //solves for distance by equation;
}
