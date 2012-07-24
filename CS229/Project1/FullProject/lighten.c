/**
 * @file lighten.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the lightening of an image by a given percent
 */

#include <stdio.h>
#include "lighten.h"

int main(int argc, char *argv[]) {
	struct image image;
	int percentage = 0;
	int error;
		
	if (argc < 2) {
		fprintf(stderr, "Lighten requires a single parameter 'percentage' to be between 0 and 100\n\tlighten 50\n");
		return 0;
	} else {
		error = sscanf(argv[1], "%d", &percentage);
		
		if (!error) {
			fprintf(stderr, "Lighten requires a single parameter 'percentage' to be between 0 and 100\n\tlighten 50\n");
			return 0;
		}
		
		if (percentage < 0 || percentage > 100) {
			fprintf(stderr, "Please enter a percentage between 0 and 100\n");
			return 0;
		}
	}
	
	
	readCSImageValues(stdin, &image);
		
	readImageData(stdin, &image);
	
	lighten(percentage, image.imWidth * image.imHeight, (image.channelSize.red == EIGHT_BIT) ? WHITE_8 : WHITE_16, image.data);
	
	writeCSImage(stdout, &image);
	
	return 0;
}

/**
 * Lightens each pixel in data by the given percentage
 *
 * @param  percentage - the percentage to darken the image by
 * @param  numPixels - the total number of pixels in the image
 * @param  maxVal - the maximum value each pixel can hold
 * @param  *data - the image (pixel) data
 */
void lighten(int percentage, int numPixels, int maxVal, struct pixel *data) {
	int i;
	double perc = percentage / 100.0;
	
	if (percentage == 0) {
		return;
	}
	
	for (i = 0; i < numPixels; i++) {
		data[i].red = getAdjustedValue(perc, maxVal, data[i].red);
		data[i].green = getAdjustedValue(perc, maxVal, data[i].green);
		data[i].blue = getAdjustedValue(perc, maxVal, data[i].blue);
	}
}

/**
 * Gets the adjusted color value with the given percentage and max val
 *
 * @param  percentage - the percentage to darken the image by
 * @param  maxVal - the maximum value each pixel can hold
 * @param  currentVal - the current color value
 */
unsigned short int getAdjustedValue(double percentage, int maxVal, unsigned short int currentVal) {
	if (currentVal + ((maxVal - currentVal) * percentage) > maxVal) {
		return maxVal;
	} else {
		return currentVal + ((maxVal - currentVal) * percentage);
	}
}
