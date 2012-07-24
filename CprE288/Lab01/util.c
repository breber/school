#include <inttypes.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include "util.h"



//Globals used for interrupt driven delay functions
volatile unsigned int timer2_tick;
int timer2_start(char unit) {
        timer2_tick=0;
        if ( unit == 0 ) { //unit = 0 is for slow 
                TCCR2=0b00001011;       //WGM:CTC, COM:OC2 disconnected,pre_scaler = 64
                TIMSK|=0b10000000;       //Enabling O.C. Interrupt for Timer2
        }
        if (unit == 1) { //unit = 1 is for fast
                TCCR2=0b00001001;       //WGM:CTC, COM:OC2 disconnected,pre_scaler = 1
                TIMSK|=0b10000000;       //Enabling O.C. Interrupt for Timer2
        }
        sei();
        return 0;
}

int timer2_stop() {
        TIMSK=0b00000000;       //Disabling O.C. Interrupt for Timer2
        TCCR2&=0b01111111;       //Clearing O.C. settings
        return 0;
}
int wait_ms(unsigned int time_val) {
        //Seting OC value for time requested
        OCR2=250; //1 ms at a prescaler of 64
        timer2_tick=0;
        timer2_start(0);


        //Waiting for time
        while(timer2_tick != time_val);

        timer2_stop();
        return 1;
}

ISR (TIMER2_COMP_vect) {
        timer2_tick++;
}


