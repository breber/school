#include "ir.h"
#include "lcd.h"
#include "scan.h"
#include "servo.h"
#include "sonar.h"
#include "util.h"
#include "irobotsensors.h"
#include "movement.h"
#include <avr/io.h>
#include <avr/interrupt.h>


/// Command to stop immediately
#define EMERGENCY_STOP 27 //ESC

/// Command to move forward 10 cm
#define MOVE_FORWARD 'w'

/// Command to move backward 5 cm
#define MOVE_BACKWARD 's'

/// Command to turn left 45 degrees
#define TURN_LEFT 'a'

/// Command to turn right 45 degrees
#define TURN_RIGHT 'd'

/// Command to turn left 10 degrees
#define TURN_LEFT_SMALL 'z'

/// Command to turn right 10 degrees
#define TURN_RIGHT_SMALL 'c'

/// Command to perform a sweep of the sonar and IR
#define SWEEP '1'

/// Command to move forward into retrieval zone
#define RETRIEVAL 'r'

/// Command to send the come get me message
#define COMEGETME 'p'

