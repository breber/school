#include <stdio.h>
#include "isprime.h"

main() {

	int i = 0;
	int j = 1;

	while (i < 20) {
		if (isPrime(j)) {
			printf("%d\n", j);
			i++;
		}
		j++;
	}
}
