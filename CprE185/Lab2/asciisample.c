#include "stdio.h"

int main()
{
	short AsciiLetter = 14;
	
	while (AsciiLetter < 128)
	{
		printf("ASCII Letter: %c  Numeric Representation: %d Other: %X\n", AsciiLetter, AsciiLetter, AsciiLetter);
		AsciiLetter = AsciiLetter + 1;
	}
	
	return 0;
}
