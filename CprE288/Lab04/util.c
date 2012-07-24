#include <inttypes.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include "util.h"


#define MAX_CHECKS 5

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
        TIMSK&=~0b10000000;       //Disabling O.C. Interrupt for Timer2
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


void shaft_encoder_init(void) {
	DDRC &= ~0xC0;	//Seting PC6-PC7 to input
	PORTC |= 0xC0; //Setting pins' pull-up resistors
}

signed char read_shaft_encoder(void) {

	char shaft_encoder_value=0xC0;
	int i;
	
	i=0;
	while((shaft_encoder_value == 0xC0) && (i < 1000)) {
    	shaft_encoder_value = PINC & 0xC0;
     	i++;
	}



	if(shaft_encoder_value != 0xC0) {
		if(shaft_encoder_value == 0x80) {
			while(shaft_encoder_value != 0x40)
				shaft_encoder_value = PINC & 0xC0;
			return(1);
		}
		if(shaft_encoder_value == 0x40) {
			while(shaft_encoder_value != 0x80) 
				shaft_encoder_value = PINC & 0xC0;
			return(-1);
		}	
	}
	else 
		return(0);

	return(0);
}


void stepper_init(void) {
 DDRE|=0xF0;  //Setting PE4-PE7 to output
 PORTE &= 0x8F;  //Initial postion (0b1000) PE4-PE7
 wait_ms(2);
 PORTE &= 0x0F;  //Clear PE4-PE7
}

//Stepper Motor Drive
//num_steps is a value between 1 and 200 steps
//direction is the direction 1 for cw and -1 for ccw
void  move_stepper_motor_by_step(int num_steps, int direction) {
 int i=0;
 char full_byte;
 static int coil_position=0b0001;
 //clockwise
 if(direction==1) {
  for(i=0;i<num_steps;i++) {
   if (coil_position==0b1000) {
    coil_position = 0b0001;
   }
   else {
    coil_position = coil_position << 1;
   }
   PORTE &= 0x0F;
   full_byte=coil_position << 4;
   PORTE |= full_byte;
   wait_ms(2);
  }   
 }
  
 //counterclockwise
 else if(direction == -1) { 
  for(i=0;i<num_steps;i++) {
   if (coil_position==0b0001) {
    coil_position = 0b1000;
   }
   else {
    coil_position = coil_position >> 1;
   }
   PORTE &= 0x0F;
   full_byte=coil_position << 4;
   PORTE |= full_byte;
   wait_ms(2);
  }   
 }
 PORTE &= 0x0F; //Clear PE4-PE7
}

void init_push_buttons(void)
{
	DDRC &= ~0x3F; //Setting PC0-PC5 to input
	PORTC |= 0x3F; //Setting pins' pull up resistors
}

// Return the position of button being pushed. If multiple
// buttons are pushed, the position of the leftmost button
// should be returned. If no button is pused, 0 is returned.
char
read_push_buttons(void)
{
	unsigned char reading,temp;
	int pos, mask;
	while(1) {
		reading = PINC & ~0xC0;  //Removing shaft encoder contribution
		temp = PINC & ~0xC0;
		if (reading == temp) {
			break;
		}
	}
	reading = ~reading; //Push buttons are active low
	if (reading != 0) {
		for (pos = 6; pos >= 1; pos--) {
			mask = (1 << (pos-1));
			if (reading & mask)
				return pos;
		}
	}

	return 0;
}
