#include <AVR/io.h>
#include "util.h"
#include "lcd.h"
#include "string.h"

char buff[20] = "";
int count = 0;


// comment
void writeBufferToScreen() {
	lcd_clear();
	wait_ms(10);
	lcd_puts(buff);
	int i;
	for (i = 0; i < 20; i++) {
		buff[i] = ' ';
	}
	count = 0;
}

void sendCharacter(char c) {
	while ( !( UCSR0A & (1<<UDRE)) )
		;
	UDR0 = c;
}

void sendString(char c[]) {
	int i;
	for (i = 0; i < strlen(c); i++) {
		while ( !( UCSR0A & (1<<UDRE)) )
			;
		UDR0 = c[i];
	}
}

ISR(USART0_RX_vect) {
	char temp = UDR0;
	
	if (temp == 13) {
		writeBufferToScreen();
		
		sendCharacter(temp);
		sendCharacter(10);

		return;
	}
	lcd_clear();
	wait_ms(10);
	lcd_putc(temp);
	buff[count] = temp;
	count++;

	if (count == 20) {
		writeBufferToScreen();
	}
	sendCharacter(temp);
}

void USART_Init( unsigned int baud )
{
	/* Set baud rate */
	UBRR0H = (unsigned char)(baud>>8);
	UBRR0L = (unsigned char)baud;
	/* Enable receiver and transmitter */
	UCSR0B = ((1<<RXEN)|(1<<TXEN))|(1<<RXCIE);
	/* Set frame format: 8data, 2stop bit */
	UCSR0C = (1<<USBS)|(3<<UCSZ0);
}

int main() {
	init_push_buttons();
	lcd_init();
	//USART_Init(25);
	USART_Init(16);

	int lastButtonPressed = 0;

	while (1) {
		int currentButton = read_push_buttons();

		if (currentButton != 0 && currentButton != lastButtonPressed) {
			if (currentButton == 6) {
				sendString("Yes");
			} else if (currentButton == 5) {
				sendString("No");
			} else if (currentButton == 4) {
				sendString("I will have to get back to you on that issue.");
			} else if (currentButton == 3) {
				sendString("CprE 288 is fun");
			} else if (currentButton == 2) {
				sendString("Just Kidding (jk)");
			} else if (currentButton == 1) {
				sendString("I'm out of clever ideas");
			}
		}

		lastButtonPressed = currentButton;
	}

}
