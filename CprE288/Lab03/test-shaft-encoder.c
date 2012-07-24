/*********************************************************************
 *
 *
*********************************************************************/

#define F_CPU 16000000UL
#include <inttypes.h>
#include <avr/io.h>
#include <string.h>
#include <stdio.h>
#include "lcd.h"
#include "util.h"

int 
main2(void)
{
	// initialize the the LCD

	lcd_init();
	lcd_home_line1();
	shaft_encoder_init();

	wait_ms(2000);

	char str[30];
	int value = 10;
	int i;
	
	
	for (i = 0; i < 30; i++) {
		str[i] = ' ';
	}

	while (1) {
		value += ((int) read_shaft_encoder());
		sprintf(str, "%2d", value);
		lcd_puts(str);
		lcd_home_line1();
		wait_ms(10);
	}
	return 0;
}
