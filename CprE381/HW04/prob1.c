#include <stdio.h>

int main() {
	int input = 0;
	int result;
	
	printf("Please enter a number >= 1 : ");
	scanf("%d", &input);
	
	if (input < 1) {
		return 0;
	}
	
	result = 0;
	
	while (input >= 1) {
		result += input;
		
		input--;
	}
	
	printf("\n\nThe result is: %d\n", result);
	
	return 0;
}