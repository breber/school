/**********************************************************
 * forward.c -- part of Lab 2
 *
 * Makes the iRobot move for 5 seconds, then displays the 
 * distance it travelled on the LCD
 *
 *   Author: Brian Reber
 *     Date: 1/24/2011
 */

#include "open_interface.h"
#include "util.h"
#include "lcd.h"
#include <stdio.h>

int main() {
  /* Initialize sensors */
  oi_t *sensor_status;
  sensor_status = oi_alloc();
  oi_init(sensor_status);
  lcd_init();
  /* End initialize sensors */
  
  oi_clear_distance(sensor_status);
  
  oi_set_speed(250, 0); // Set the linear speed to 250 mm/s
  
  wait_ms(5000); // Wait 5 seconds
  
  oi_set_speed(0, 0); // Stop the movement
  
  int dist = oi_current_distance(sensor_status); // Get the current distance
  
  char str[10];
  
  sprintf(str, "%d", dist); // Fill the string with the distance
  
  lcd_puts(str); // Put the distance on the lcd
  
  return 0;
}