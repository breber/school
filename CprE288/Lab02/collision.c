/**********************************************************
 * collision.c -- part of Lab 2
 *
 * Makes the iRobot move 2 meters, while accounting for 
 * data the bumper sensors send
 *
 *   Author: Brian Reber
 *     Date: 1/24/2011
 */

#include "open_interface.h"
#include "util.h"
#include "lcd.h"

void backup(oi_t *self);

int main() {
  /* Initialize sensors */
  oi_t *sensor_status;
  sensor_status = oi_alloc();
  oi_init(sensor_status);
  lcd_init();
  /* End initialize sensors */
  
  oi_clear_distance(sensor_status);
  oi_clear_angle(sensor_status);
  
  // Move 50 cm forward
  oi_set_speed(250, 0); // Set the linear speed to 250 mm/s
  
  while (oi_current_distance(sensor_status) < 200 || oi_bump_status(sensor_status) != 0);
  
  if (oi_current_distance(sensor_status) >= 200) {
    lcd_puts("Done");
  } else if (oi_bump_status(sensor_status) == 1) {
    backup(sensor_status);
    //Right bumper - rotate 90 degrees to left
    oi_set_speed(0, 3.14159 / 4 / 1000); // Stop the linear movement, start rotational movement ( pi / 4 milliradians / s)
    while (oi_current_angle(sensor_status) < 90); // While the current angle is less than 90 degrees
  } else if (oi_bump_status(sensor_status) == 2) {
    backup(sensor_status);
    //Left bumper - rotate 90 degrees to right
    oi_set_speed(0, -3.14159 / 4 / 1000); // Stop the linear movement, start rotational movement ( pi / 4 milliradians / s)
    while (oi_current_angle(sensor_status) < 90); // While the current angle is less than 90 degrees
  } else {
    backup(sensor_status);
    //Right bumper - rotate 90 degrees to left
    oi_set_speed(0, 3.14159 / 4 / 1000); // Stop the linear movement, start rotational movement ( pi / 4 milliradians / s)
    while (oi_current_angle(sensor_status) < 90); // While the current angle is less than 90 degrees
  }
  
  oi_set_speed(0, 3.14159 / 4 / 1000); // Stop the linear movement, start rotational movement ( pi / 4 milliradians / s)
  
  wait_ms(1000); // pi/4 milliradians / s * 1 s =  pi / 4 radians?
  oi_set_speed(0, 0);

  
// 0 = no sensors pressed
// 1 = right sensor
// 2 = left sensor
// 3 = both sensors
  
  return 0;
}

void backup(oi_t *self) {
  int currentDistance = oi_current_distance(sensor_status);
  oi_set_speed(-250, 0); // Set the linear speed to -250 mm/s
  
  while (oi_current_distance(sensor_status) > (currentDistance - 15));
  
  return;
} 