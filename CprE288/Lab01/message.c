#define F_CPU 16000000UL
#include <inttypes.h>
#include <avr/io.h>
#include <string.h>
#include "lcd.h"

#define MESSAGE_SIZE 100
#define FIRST_LENGTH 14

void initArray(char *arr, int len);
/*
int mtain (void)
{
	// initialize the the LCD
	lcd_init();
	lcd_home_line1();
	
	
	char msg[MESSAGE_SIZE] = {"Microcontrollers are loads of fun and take forever!"};
	char rotMsg[FIRST_LENGTH];
	
	initArray(rotMsg, 14);
	int i, j, maxLoop, maxInnerLoop;
	
	// We want to set the boundaries to the loops
	if (FIRST_LENGTH > strlen(msg)) {
		maxLoop = strlen(msg);
	} else {
		maxLoop = FIRST_LENGTH;
		maxInnerLoop = strlen(msg);
	}
	
	while (1) {
		// Print out first 14 characters
		for (i = 0; i < maxLoop; i++) {
			rotMsg[i] = msg[i];
			lcd_put_c(msg[i]);
			//printf("%s\n", rotMsg);
			wait_ms(300);
		}
		
		for (i = 0; i < maxInnerLoop; i++) {
			// Clear the lcd
			lcd_clear();
			wait_ms(10);
			
			// Shift the letters over in rotMsg
			for (j = 0; j < FIRST_LENGTH; j++) {
				rotMsg[j] = rotMsg[j + 1];
			}
			// And add the next character from the original msg
			rotMsg[FIRST_LENGTH - 1] = msg[FIRST_LENGTH + i];
			
			lcd_put_s(rotMsg);
			wait_ms(500);
			
			//printf("%s\n", rotMsg);
		}
		
		wait_ms(500);
	}
	
	return 0;
}*/

void initArray(char *arr, int len)
{
	int i;
	for (i = 0; i < len; i++) {
		arr[i] = 0;
	}
}
