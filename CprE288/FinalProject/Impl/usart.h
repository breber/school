#ifndef __USART__
#define __USART__

/// Initializes the USART for receiving and sending via Bluetooth
/**
 *	Sets up the proper registers for Receive (via interrupt) and Transmit
 */
void usart_init();

/// Transmits a string from the iRobot to whoever is listening on the serial port
/**
 *	Sends the string from the iRobot to the listeners via bluetooth
 *
 *	@param *c - the string to send
 */
void usart_transmit(char *c);

/// The latest character received by the USART
volatile char cmd;

#endif
