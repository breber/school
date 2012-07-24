/*********************************************************************
 *
 *
*********************************************************************/

#define F_CPU 16000000UL
#include <inttypes.h>
#include <avr/io.h>
#include <string.h>
#include "lcd.h"
#include "util.h"

int 
main0 (void)
{
	// initialize the the LCD

	lcd_init();
	lcd_home_line1();
	init_push_buttons();

	wait_ms(2000);

	char str[30];

	int i;
	for (i = 0; i < 30; i++) {
		str[i] = ' ';
	}

	while (1) {

		sprintf(str, "Button %c", read_push_buttons());
		lcd_puts(str);
		wait_ms(1000);
		lcd_clear();
		wait_ms(250);
		for (i = 0; i < 30; i++) {
			str[i] = ' ';
		}
	}
	return 0;
}
