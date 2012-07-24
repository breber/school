/*********************************************************************
 * Hello World
 * Author: Zhao Zhang
 * Revision Date: 06/24/2008
 *
 * NOTE: This program is revised from mage128_gcc_test_user.c
 *
 * This program prints Hello world on the LCD screen of the  
 * VORTEX platform
 *
*********************************************************************/

#define F_CPU 16000000UL
#include <inttypes.h>
#include <avr/io.h>
#include <string.h>
#include "lcd.h"

int 
main (void)
{
	// initialize the the LCD

	lcd_init();
	lcd_home_line1();
	//	clear_display();  
	lcd_puts("Hello, world");

	return 0;
}
