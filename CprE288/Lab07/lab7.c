#include <stdio.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include "util.h"
#include "lcd.h"

volatile unsigned currentValue = 0;
volatile unsigned counterOVF = 0;
volatile unsigned capture = 0;

void init_interrupts() {
    TCCR1A = 0b00000000;        // WGM1[1:0]=00
    TCCR1B = 0b11000011;        // Noise filter, set to rising edge
    TCCR1C = 0b00000000;        // No output compare
    TIMSK  = 0b00100100;        // Enable input capture and overflow interrupts
	sei();
}


void pulseSensor() {
	capture = 0;
	TIMSK  = 0; // disable input capture
	DDRD |= _BV(4); // set PD4 as output
	wait_ms(1);
	PORTD &= ~_BV(4); // set PD4 to low
	wait_ms(1); // wait
	PORTD |= _BV(4); // set PD4 to high
	wait_ms(1); // wait
	PORTD &= ~_BV(4); // set PD4 to low
	wait_ms(.2); // wait
	DDRD &= ~_BV(4); // set PD4 as input
	TCCR1B |= 0x40;
	TIMSK  = 0b00100100; // reenable input capture

	while (capture == 0);
}

int main() {
	
	int dist;
	char buf[30];
	init_interrupts();
	lcd_init();
	wait_ms(100);
	
	while(1) {
	
	    pulseSensor();
		dist = (currentValue / 2) * .136 - 10;
		sprintf(buf,"%u cm CurVal: %d", dist, currentValue);
		lcd_puts(buf);
		wait_ms(1000);
		lcd_clear();
		wait_ms(10);
	}

}

ISR (TIMER1_OVF_vect) {
	counterOVF++;
}

ISR (TIMER1_CAPT_vect) {
	unsigned int value = ICR1;
	

    if ((TCCR1B & 0b01000000)) {
		counterOVF = 0;
        currentValue = value;
		TCCR1B = 0b10000011;  //noise filter, set to trailing edge
    } 
	else {
        currentValue = (value + counterOVF*65535) - currentValue;  
		capture = 1;
    }
}

