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


// initialize push buttions: set PORTC, which is used for push
// buttons, as inputs

void init_push_buttons(void)

{
	DDRC &= ~0x3F; //Setting PC0-PC5 to input
	PORTC |= 0x3F; //Setting pins' pull up resistors
}



// Return the position of button being pushed. If multiple
// buttons are pushed, the position of the leftmost button
// should be returned. If no button is pused, 0 is returned.

char read_push_buttons(void)
{
	if (~PINC & 0x20) {
		return '6';
	} else if (~PINC & 0x10) {
		return '5';
	} else if (~PINC & 0x08) {
		return '4';
	} else if (~PINC & 0x04) {
		return '3';
	} else if (~PINC & 0x02) {
		return '2';
	} else if (~PINC & 0x01) {
		return '1';
	}

	return '0';
}


void shaft_encoder_init(void) {
	DDRC &= ~0xC0;	//Seting PC6-PC7 to input
	PORTC |= 0xC0; //Setting pins' pull-up resistors
}

signed char read_shaft_encoder(void) {

	char shaft_encoder_value=0xFF;//log:kaming: changed 0xC0 to 0xFF(this value depends on initialization)
	int i;
	
	i=0;
	//MCU will check PC6 and PC7 for a state other than the default inactive state
	//The this loop will check 1000 times before giving up.  This value is fairly arbitrary.
	//The MCU can poll the pins very quickly and polling only once is very likely to return
	//an inactive state value, so polling will occur many times to avoid missing an actual 
	//active state value.  The loop counter value is a balance between being patient and
	//waiting for something that isn't going to happen.
	while((shaft_encoder_value == 0xFF) && (i < 1000)) {//log:kaming: changed 0xC0 to 0xFF(this value depends on initialization)
    	shaft_encoder_value = PINC | 0x3F; //You need to complete this line
     	i++;
	}

//Logic for reading clockwise rotation
	if(shaft_encoder_value != 0xFF) {//log:kaming: changed 0xC0 to 0xFF(this value depends on initialization)
		if(shaft_encoder_value == 0x7F) { //What value should be compared to here
			while(shaft_encoder_value != 0xBF) //What value don't we want to see here
				shaft_encoder_value = PINC | 0x3F;//You need to complete this line
			return (1); //What value should be returned?
		}
//Logic for reading counter-clockwise rotation
		if(shaft_encoder_value == 0xBF) { //What value should be compared to here (the opposite from above)
			while(shaft_encoder_value != 0x7F)//What value don't we want to see here (the opposite from above)
				shaft_encoder_value = PINC | 0x3F;//You need to complete this line
			return (-1);//What value should be returned?
		}	
	}
	else return(0); //The shaft encoder did not rotate
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
//kaming:
//clockwise pattern(PE7,PE6,PE5,PE4):
//0001
//0010
//0100
//1000
//0001
//anticlockwise pattern(PE7,PE6,PE5,PE4):
//1000
//0100
//0010
//0001
//1000
void  move_stepper_motor_by_step(int num_steps, int direction) {
 int i=0;
 char full_byte;
 static int coil_position= 0b0001;
 //clockwise
 if(direction==1) {//What value should be compared to here
  for(i=0;i<num_steps;i++) {
   if (coil_position==0b1000) {//What value should be compared to here
    coil_position = 0b0001; //You need to complete this line
   }
   else {
    coil_position = coil_position << 1; //What operator should be use here?
   }
   PORTE &= 0x0F;//What mask should be used here so that only one PORTE pin is active two statements 
			//from this one and P0-P3 are unaffected by this masking?
   full_byte=coil_position << 4;//How many left shifts are needed to properly position the coil pattern
   PORTE |= full_byte;
   wait_ms(2);
  }   
 }
  
 //counterclockwise
 else if(direction == -1) { //What value should be compared to here
  for(i=0;i<num_steps;i++) {
   if (coil_position==0b0001) {//What value should be compared to here
    coil_position = 0b1000;//You need to complete this line
   }
   else {
    coil_position = coil_position >> 1;//What operator should be use here?
   }
   PORTE &= 0x0F; //What mask should be used here so that only one PORTE pin is active two statements 
			//from this one and P0-P3 are unaffected by this masking?
   full_byte=coil_position << 4;//How many left shifts are needed to properly position the coil pattern
   PORTE |= full_byte; //Setting pin values for output, which in turn sets the signal lines going to
						//motor controller chip.
   wait_ms(2);
  }   
 }
 PORTE &= 0x0F; //What mask is needed to clear PE4-PE7 and not affect P0-P3?
}
