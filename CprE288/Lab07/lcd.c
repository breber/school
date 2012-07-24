#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>
#include "lcd.h"
#include "util.h"

void lcd_toggle_clear(char delay);
void lcd_write_c(char data);
void lcd_home_line2(void);
void lcd_home_line3(void);
void lcd_home_line4(void);
void lcd_home_anyloc(unsigned char location);

// Initializes PORTA to communicate with LCD controller
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
	lcd_home_line1();
}



// Triggers loading of bits by LCD controller and clears bits after toggle
void lcd_toggle_clear(char delay) {
	const char enable=0x40; //PA6 is tied to Enable

	PORTA|=enable;
	wait_ms(delay);
	PORTA&=~enable;
	PORTA&=0xF0;	
}



// Displays one character on LCD
void lcd_write_c(char data) {
	PORTA|=(data>>4);
	lcd_toggle_clear(1);
	PORTA|=(data & 0x0F);
	lcd_toggle_clear(1);
}



// Submits command to LCD controller
void lcd_command(char data) {
	const char rs=0x10;		//PA4 is tied to Register Select
	PORTA&=~rs;  //Setting register select low for command mode
	PORTA|=(data>>4);
	lcd_toggle_clear(2);
	PORTA|=(data & 0x0F);
	lcd_toggle_clear(2);
	PORTA|=rs;	//Setting register select high for character mode
}



// Clears the LCD
void lcd_clear(void) {
	lcd_command(HD_LCD_CLEAR);
	wait_ms(1);
}



// Sets character position to first line first position
void lcd_home_line1(void) {
	lcd_command(HD_RETURN_HOME);
}



// Sets character position to second line first position
void lcd_home_line2(void) {
	lcd_command(0xC0); //First bit relevant to the set DDRAM address instruction. The remaining bits designate address, which is 0x40.
}



// Sets character position to third line first position
void lcd_home_line3(void) {
	lcd_command(0x94);	//First bit relevant to the set DDRAM address instruction. The remaining bits designate address, which is 0x14.
}



// Sets character position to fourth line first position
void lcd_home_line4(void){ //First bit relevant to the set DDRAM address instruction. The remaining bits designate address, which is 0x54.
	lcd_command(0xD4); 
}



// Sets character position to any valid location
void lcd_home_anyloc(unsigned char location) {
	if (location < 0x68) { //Addresses are limitted to 0x00 - 0x67
		location |= 0x80; //Setting the first bit to one, thus forming a DDRAM set address instruction
		lcd_command(location);
	} 
}



// Shift display content left
void lcd_display_shift_left(void) {
	lcd_command(HD_DISPLAY_SHIFT_LEFT);
}



// Prints string to lcd, starting at the current cursor position
void lcd_puts (char * string) {
	int size=strlen(string);
	int i;

	for (i=0;i<size;i++) {		
		lcd_write_c(string[i]);
	}
}



// Prints one character at the current cursor position
void lcd_putc(char data) {
	PORTA|=(data>>4);
	lcd_toggle_clear(1);
	PORTA|=(data & 0x0F);
	lcd_toggle_clear(1);
}
