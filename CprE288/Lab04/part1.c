/*********************************************************************
* clock-intr.c: timer- and interrupt-based clock program
* 	This is skeleton code. You need to make it complete.
* Author: Zhao Zhang
* Data: 09/17/2007
*********************************************************************/


#include <avr/io.h>
#include "lcd.h"
#include "util.h"
#include <stdio.h>

int
main()
{
	lcd_init();
	init_push_buttons();

	int hours = 0;
	int minutes = 0;
	int seconds = 0;
	int button_val;
	int count = 0;

	char time_disp[30];
	
	while (1) {
		/* add your code here */
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
		
		for (count = 0; count < 5; count++) {
			button_val = read_push_buttons();
			if (button_val == 1) {
				seconds--;
				
				if (seconds < 0) {
					seconds = 59;
				}

				sprintf(time_disp, "%02d:%02d %02d", hours, minutes, seconds);	

				lcd_clear();
				wait_ms(10);
				lcd_puts(time_disp);
				wait_ms(190);
			} else if (button_val == 2) {
				seconds++;
				
				if (seconds > 59) {
					seconds = 0;
				}

				sprintf(time_disp, "%02d:%02d %02d", hours, minutes, seconds);

				lcd_clear();
				wait_ms(10);
				lcd_puts(time_disp);
				wait_ms(190);
			} else if (button_val == 3) {
				minutes--;
				
				if (minutes < 0) {
					minutes = 59;
				}

				sprintf(time_disp, "%02d:%02d %02d", hours, minutes, seconds);

				lcd_clear();
				wait_ms(10);
				lcd_puts(time_disp);
				wait_ms(190);
			} else if (button_val == 4) {
				minutes++;
				
				if (minutes > 59) {
					minutes = 0;
				}

				sprintf(time_disp, "%02d:%02d %02d", hours, minutes, seconds);

				lcd_clear();
				wait_ms(10);
				lcd_puts(time_disp);
				wait_ms(190);
			} else if (button_val == 5) {
				hours--;
				
				if (hours < 0) {
					hours = 23;
				}

				sprintf(time_disp, "%02d:%02d %02d", hours, minutes, seconds);

				lcd_clear();
				wait_ms(10);
				lcd_puts(time_disp);
				wait_ms(190);
			} else if (button_val == 6) {
				hours++;
				
				if (hours > 23) {
					hours = 0;
				}

				sprintf(time_disp, "%02d:%02d %02d", hours, minutes, seconds);

				lcd_clear();
				wait_ms(10);
				lcd_puts(time_disp);
				wait_ms(190);
			} else {
				sprintf(time_disp, "%02d:%02d %02d", hours, minutes, seconds);

				lcd_clear();
				wait_ms(10);
				lcd_puts(time_disp);
				wait_ms(190);
			}
			
		}
	}
}
