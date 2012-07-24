#include "evaluate.h"
#include "operations.h"
#include <stdio.h>

void evaluate(char exp[]) {
	int first;
	int second;
	char op;	
	char newArr[30];
	printf("%s\n", exp);
	
	sscanf(exp, "%d %d %c", &first, &second, &op);
	
	printf("%d %d %c\n", first, second, op);
	
	sprintf(*exp, "Test");
	/*
	switch (op) {
		case '+' : sprintf(exp, "%d", add(first, second)); break;
		case '-' : sprintf(exp, "%d", subtract(first, second)); break;
	}*/
	
}
