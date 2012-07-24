#include "evaluate.h"
#include <stdio.h>
#include <strings.h>

void readValues(char str[]) {
	int first;
	int second;
	char op;
	
	scanf("%d %d %c", &first, &second, &op);
		
	sprintf(str, "%d %d %c", first, second, op);
}

int main() {
	char *str = "5 5 +";
	printf(">");
	
	evaluate(str);
	printf("%s\n", str); 
	/*readValues(str);
	printf("%s\n", str);

	while (!strcmp(str, "END")) {
		evaluate(str);
		printf("%s\n", str);
		printf(">");
		readValues(str);
	}*/
	
	return 0;
}
