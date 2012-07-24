/*********************************************************************
* clock-intr.c: timer- and interrupt-based clock program
* 	This is skeleton code. You need to make it complete.
* Author: Zhao Zhang
* Data: 09/17/2007
*********************************************************************/


#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/sleep.h>
#include "lcd.h"
#include "stdio.h"
#include "util.h"

#define CLOCK_COUNT 15625
#define CHECK_COUNT 3125

int hours = 0;
int minutes = 0;
int seconds = 0;


void
timer_init(void)
{
	// set up timer 1: WGM1 bits = 0100, CS = 101, set OCR1A, set TIMSK
	TCCR1A = 0b00000000;		// WGM1[1:0]=00
	TCCR1B = 0b00001101;		// WGM1[3:2]=01, CS=101
	TCCR1C = 0b10000000;		// FOC1A=1, force OC on Channels A
	OCR1A = CLOCK_COUNT - 1; 	// counter threshold for clock
	TIMSK = _BV(OCIE1A);		// enable OC interrupt, timer 1, channel A

	// set up timer 3: WGM1 bits = 0100, CS = 101, set OCR3A, set TIMSK
	TCCR3A = 0b00000000;		// WGM1[1:0]=00
	TCCR3B = 0b00001101;		// WGM1[3:2]=01, CS=101
	TCCR3C = 0b10000000;		// FOC2A=1, force OC on Channels A
	OCR3A = CHECK_COUNT - 1; 	// counter threshold for checking push button
	ETIMSK = _BV(OCIE3A);		// enable OC interrupt, timer 3, channel A

	sei();
}

// Timer interrput source 1: the function will be called every one second (you need
// define CLOCK_COUNT correctly); for clock updating
ISR (TIMER1_COMPA_vect)
{
	seconds++;

	if (seconds > 59) {
		seconds = 0;
		minutes++;
	}
	if (minutes > 59) {
		minutes = 0;
		hours++;
	}
	if (hours > 23) {
		hours = 0;
	}
}

// Timer interrupt source 2: the function will be called in period determined by 
// CHECK_OUNT; for checking push button
ISR (TIMER3_COMPA_vect)
{
	int button_val = read_push_buttons();
	if (button_val == 1) {
		seconds--;
		
		if (seconds < 0) {
			seconds = 59;
		}
	} else if (button_val == 2) {
		seconds = (seconds + 1) % 60;
	} else if (button_val == 3) {
		minutes--;
		
		if (minutes < 0) {
			minutes = 59;
		}
	} else if (button_val == 4) {
		minutes++;
		
		if (minutes > 59) {
			minutes = 0;
		}
	} else if (button_val == 5) {
		hours--;
		
		if (hours < 0) {
			hours = 23;
		}
	} else if (button_val == 6) {
		hours++;
		
		if (hours > 23) {
			hours = 0;
		}
	}
}	

int
main2()
{
	int tempH = 0;
	int tempM = 0;
	int tempS = 0;
	char time_disp[30];

	lcd_init();
	timer_init();
	init_push_buttons();
	
	while (1) {
		// add your code here

		if (tempH != hours || tempM != minutes || tempS != seconds) {
			tempH = hours;
			tempM = minutes;
			tempS = seconds;

			sprintf(time_disp, "%02d:%02d %02d", hours, minutes, seconds);

			lcd_clear();
			wait_ms(10);
			lcd_puts(time_disp);
		}
	}
}

