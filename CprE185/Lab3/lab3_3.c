#include <stdio.h>
#include <math.h>

int main() {
	float x, y, z;
	
	while (1) { 
		scanf("%f,%f,%f", &x, &y, &z);
		printf("Magnitude of (%5.2f,%5.2f,%5.2f) is: %6.2f\n",
			   x, y, z, sqrt(x*x+y*y+z*z));
	}
	
	return 0;
}
