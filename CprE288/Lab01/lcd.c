#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>
#include "lcd.h"
#include "util.h"


void lcd_init(void) {
	const char enable=0x40; //PA6 is tied to Enable
	const char rs=0x10;		//PA4 is tied to Register Select
	//Assumes Port A is dedicated to the LCD
	//Seven Pins needed, but will assume all 8 are used
	DDRA=0xFF; //Setting Port A for OutPut
	 //Preparing to put HD44780 into 4-bit Mod
	PORTA=0x03;

	PORTA|=enable;
	wait_ms(1);
	PORTA&=~enable;
	wait_ms(5);
	PORTA|=enable;
	wait_ms(1);
	PORTA&=~enable;
	PORTA|=enable;
	wait_ms(1);
	PORTA&=~enable;

	PORTA=0x02;	//setting controller to 4 bit mode
				//Need to set for 2 lines
	lcd_toggle_clear(1);

	PORTA|=0x00;  //setting disp on, cursor on, blink off
	lcd_toggle_clear(1);
	PORTA|=0x0E;
	lcd_toggle_clear(1);

	PORTA|=0x00; //increment cursor, no display shift
	lcd_toggle_clear(1);
	PORTA|=0x06;
	lcd_toggle_clear(1);


	
	PORTA|=0x00; //clear LCD
	lcd_toggle_clear(1);
	PORTA|=0x01;
	lcd_toggle_clear(1);

	PORTA|=rs;	//Setting Register select high to enable character mode
	
}

void lcd_toggle_clear(char delay) {
	const char enable=0x40; //PA6 is tied to Enable

	PORTA|=enable;
	wait_ms(delay);
	PORTA&=~enable;
	PORTA&=0xF0;	


}

void lcd_write_c(char data) {

	PORTA|=(data>>4);
	lcd_toggle_clear(1);
	PORTA|=(data & 0x0F);
	lcd_toggle_clear(1);

}

void lcd_command(char data) {
	const char rs=0x10;		//PA4 is tied to Register Select
	PORTA&=~rs;  //Setting register select low for command mode
	PORTA|=(data>>4);
	lcd_toggle_clear(2);
	PORTA|=(data & 0x0F);
	lcd_toggle_clear(2);
	PORTA|=rs;	//Setting register select high for character mode
}

void lcd_clear(void) {
	lcd_command(HD_LCD_CLEAR);
	wait_ms(10);
}

void lcd_home_line1(void) {

	lcd_command(HD_RETURN_HOME);
}

void lcd_home_line2(void) {
	int i;
	lcd_command(HD_RETURN_HOME);
	for (i=0;i<40; i++) {
		lcd_command(HD_CURSOR_MOVE_RIGHT);
	}
}



void lcd_puts (char * string) {

	int size=strlen(string);
	int i;

	for (i=0;i<size;i++) {		
		lcd_write_c(string[i]);
	}


}

void lcd_putc(char data) {

	PORTA|=(data>>4);
	lcd_toggle_clear(1);
	PORTA|=(data & 0x0F);
	lcd_toggle_clear(1);



}
