#define F_CPU 16000000UL
#include <inttypes.h>
#include <avr/io.h>
#include <string.h>
#include "lcd.h"
#include "util.h"

#define MESSAGE_SIZE 100
#define FIRST_LENGTH 14

void initArray(char *arr, int len);

int test (void)
{
	// initialize the the LCD
	lcd_init();
	wait_ms(10);
	lcd_home_line1();
	
	char msg[MESSAGE_SIZE] = {"Microcontrollers are loads of fun!"};
	
	int i, length;
	lcd_command(0x27);
	
	if (strlen(msg) < 15) {
		length = strlen(msg);
	} else {
		length = 15;
	}

	while (1) {
		for (i = 0; i < strlen(msg); i++) {		
			if (i > FIRST_LENGTH) {
				lcd_command(0x18);
				lcd_putc(msg[i]);
				wait_ms(500);
			} else {
				lcd_putc(msg[i]);
				wait_ms(300);
			}
		}
		for (i = 0; i < length; i++) {
			lcd_command(0x18);
			lcd_putc(' ');
			wait_ms(500);
		}
		lcd_clear();
		wait_ms(500);
		i = 0;
	}
	return 0;
}
