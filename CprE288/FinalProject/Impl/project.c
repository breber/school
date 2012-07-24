/*
 * Final Project 288
 * Team iBambi
 *
 * @author Brian Reber
 * @author Josh Peters
 * @author Jeremy Bruecken
 * @author Todd Lyon
 */

#include "project.h"

/// The value of the sensor status
volatile int sense = 0;

/// Main method that starts the robot
/**
 *	This contains the main loop that controls the robot and checks for keyboard input
 *  and runs the commands based on input and sensor data.
 */
int main(void){
	// Variables
	char str[30];
	int j = 0;

	lcd_init();
	usart_init();
	init_servo(); 	

	iBambi = oi_alloc();
  	oi_init(iBambi);
	
	usart_transmit("iBambi Ready for Manual Traversal\n\r");

  	oi_clear_distance(iBambi);
  	oi_clear_angle(iBambi);
	move_servo_degrees(90);

	while (1) {
		if (cmd != 0) {
			switch(cmd) {
				case EMERGENCY_STOP: 
					sprintf(str, "Stopping\n\r");
					usart_transmit(str);
					lcd_clear();
					wait_ms(5);
					lcd_puts("Stop");
					oi_set_wheels(0, 0);
					break;
				case MOVE_FORWARD:
					sprintf(str, "Moving Forward\n\r");
					usart_transmit(str);
					lcd_clear();
					wait_ms(5);
					lcd_puts("Forward");
					move_forward(iBambi, 10);
					break;
				case MOVE_BACKWARD:
					sprintf(str, "Moving Backward\n\r");
					usart_transmit(str);
					lcd_clear();
					wait_ms(5);
					lcd_puts("Backward");
					move_backward(iBambi, -5);
					break;
				case TURN_RIGHT:
					sprintf(str, "Turning Right\n\r");
					usart_transmit(str);
					lcd_clear();
					wait_ms(5);
					lcd_puts("Right");
					turn_right(iBambi,45);
					break;
				case TURN_LEFT:
					sprintf(str, "Turning Left\n\r");
					usart_transmit(str);
					lcd_clear();
					wait_ms(5);
					lcd_puts("Left");
					turn_left(iBambi,45);
					break;
				case TURN_RIGHT_SMALL:
					sprintf(str, "Turning Right Small\n\r");
					usart_transmit(str);
					lcd_clear();
					wait_ms(5);
					lcd_puts("Right Small");
					turn_right(iBambi,10);
					break;
				case TURN_LEFT_SMALL:
					sprintf(str, "Turning Left Small\n\r");
					usart_transmit(str);
					lcd_clear();
					wait_ms(5);
					lcd_puts("Left Small");
					turn_left(iBambi, 10);
					break;
				case SWEEP:
					sprintf(str, "Sweeping\n\r");
					usart_transmit(str);
					performSweepAnalysis();
					move_servo_degrees(90);
					break;
				case RETRIEVAL:
					sprintf(str, "Moving to Retrieval Zone\n\r");
					usart_transmit(str);
					move_forward(iBambi, 50);
					lcd_clear();
					wait_ms(5);
					lcd_puts("come get me!");
					sprintf(str, "Ready for Retrieval\n\r");
					usart_transmit(str);
					for(j=0;j<50;j++){
						oi_set_leds(1,1,000,200);
						wait_ms(50);
						oi_set_leds(1,1,200,200);
						wait_ms(50);
					}
					break;
				case COMEGETME:			
					lcd_clear();
					wait_ms(5);
					lcd_puts("Come get me!");
					sprintf(str, "Ready for Retrieval\n\r");
					usart_transmit(str);
					for (j = 0; j < 50; j++) {
						oi_set_leds(1,1,000,200);
						wait_ms(50);
						oi_set_leds(1,1,200,200);
						wait_ms(50);
					}					
					break;
			}

			cmd = 0;
			sense = 0;
		}
		
		wait_ms(40);

		if (!sense) {
			sense = checkSensors();
		}

	}
}
