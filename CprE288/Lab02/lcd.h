#include <inttypes.h>
#include <avr/io.h>
#include <avr/interrupt.h>


#define HD_LCD_CLEAR 0x01
#define HD_RETURN_HOME 0x02
#define HD_CURSOR_SHIFT_DEC 0x05
#define HD_CURSOR_SHIFT_INC 0x07
#define HD_DISPLAY_CONTROL 3
#define HD_DISPLAY_ON 2
#define HD_CURSOR_ON 1
#define HD_BLINK_ON 0
#define HD_CURSOR_MOVE_LEFT 0x10
#define HD_CURSOR_MOVE_RIGHT 0x14
#define HD_DISPLAY_SHIFT_LEFT 0x18
#define HD_DISPLAY_SHIFT_RIGHT 0x1C


//Prints string to lcd
void lcd_puts (char * string);

//Initializes LCD
void lcd_init(void);

//Triggers loading of bits by LCD controller and clears bits after toggle
void lcd_toggle_clear(char delay);

//Displays one character on LCD
void lcd_write_c(char data);

//Submits command to LCD controller
void lcd_command(char data);

//Clears the LCD
void lcd_clear(void);

//Sets character position to first line first position
void lcd_home_line1(void);

//Sets character position to second line first position
void lcd_home_line2(void);

//Sets character position to third line first position
void lcd_home_line3(void);

//Sets character position to fourth line first position
void lcd_home_line4(void);

//Sets character position to any valid location
void lcd_home_anyloc(unsigned char location);

//Shift display content left
void lcd_display_shift_left(void);

//Prints one character
void lcd_putc(char data);
