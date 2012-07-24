#include <avr/io.h> 
#include <stdio.h>
#include <avr/interrupt.h>
#include "usart.h"
#include "util.h"
#include "string.h"

/// Initializes the USART for receiving and sending via Bluetooth
/**
 *	Sets up the proper registers for Receive (via interrupt) and Transmit
 */
void usart_init() { 
	unsigned int baud = 16;
	
	//Initialize UCSRxB for turning on the transmission and reception circuitry 
	UCSR0B = (1<<RXEN)|(1<<TXEN)|(1<<RXCIE);
	// Initialize UCSRxC for setting character size, parity bits etc
	UCSR0C = (1<<USBS)|(3<<UCSZ0);
	// Initialize UBRRxH and UBRRxL for setting USART clock (lower and higher bits) 
	UBRR0H = (unsigned char) (baud >> 8);
	UBRR0L = (unsigned char) baud;	
} 

/// Transmits a string from the iRobot to whoever is listening on the serial port
/**
 *	Sends the string from the iRobot to the listeners via bluetooth
 *
 *	@param *c - the string to send
 */
void usart_transmit(char *c) {
	// Transmit data to RealTerm 
	int length = strlen(c);
	int i = 0;

	while (i < length) {
		while ( !( UCSR0A & (1<<UDRE0)) );
		UDR0 = c[i];
		wait_ms(1);
		i++;
	}
}

/// Usart Receive Interrupt Handler - for notifying we have received a command from Bluetooth
/**
 *	Sets the command to the value sent
 */
ISR(USART0_RX_vect) {
	cmd = UDR0;
}

