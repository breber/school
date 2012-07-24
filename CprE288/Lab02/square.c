/**********************************************************
 * square.c -- part of Lab 2
 *
 * Makes the iRobot move in a square with a sidelength of
 * 50 cm.
 * 
 *   Author: Brian Reber
 *     Date: 1/24/2011
 */

#include "open_interface.h"
#include "util.h"
#include "lcd.h"

int main() {
  /* Initialize sensors */
  oi_t *sensor_status;
  sensor_status = oi_alloc();
  oi_init(sensor_status);
  lcd_init();
  /* End initialize sensors */
  
  oi_clear_distance(sensor_status);
  oi_clear_angle(sensor_status);
  
  int i;
  for (i = 0; i < 4; i++)
    // Move 50 cm forward
    oi_set_speed(250, 0); // Set the linear speed to 250 mm/s
    
    while (oi_current_distance(sensor_status) < 50); // While the current distance is less than 50 cm
    oi_clear_distance(sensor_status);
    
    oi_set_speed(0, 3.14159 / 4000); // Stop the linear movement, start rotational movement ( pi / 4 milliradians / s)
    
    while (oi_current_angle(sensor_status) < 90); // While the current angle is less than 90 degrees
    oi_clear_angle(sensor_status);
    
    oi_set_speed(0, 0);
  }
  
  lcd_puts("Done");
  
  return 0;
}