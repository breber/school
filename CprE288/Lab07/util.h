#include <inttypes.h>

// Millisecond delay function
int wait_ms(unsigned int time_val);

// Shaft encoder initialization
void shaft_encoder_init(void);

// Shaft encoder read function
int8_t read_shaft_encoder(void);

// Initialize Stepper Motor
void stepper_init(void);

// Stepper motor move function
void  move_stepper_motor_by_step(int num_steps, int direction);

// initialize PORTC, which is used by the push buttons
void init_push_buttons(void);

// Return the position of button being pushed, starting from 1
// for the rightmost button. Return 0 if no button is pushed
uint8_t read_push_buttons(void);

