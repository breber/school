
//Start 8-bit Timer2 primarily to be used for a delay function
int timer2_start(char unit);

//Stop 8-bit Timer2
int timer2_stop(void); 

//Millisecond delay function
int wait_ms(unsigned int time_val);

//Shaft encoder initialization
void shaft_encoder_init(void);

//Shaft encoder read function
signed char read_shaft_encoder(void);

//Initialize Stepper Motor
void stepper_init(void);

//Stepper motor move function
void  move_stepper_motor_by_step(int num_steps, int direction);

// initialize PORTC, which is used by the push buttons
void init_push_buttons(void);

// Return the position of button being pushed, starting from zero
// for the leftmost button. Return 0 if no button is pushed
char read_push_buttons(void);

