/**
 * @file darken.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the darkening of an image by a given percent
 */

#include <stdio.h>
#include "darken.h"

int main(int argc, char *argv[]) {
	struct image image;
	int percentage = 0;
	int error;
		
	if (argc < 2) {
		fprintf(stderr, "Darken requires a single parameter 'percentage' to be between 0 and 100\n\tdarken 50\n");
		return 0;
	} else {
		error = sscanf(argv[1], "%d", &percentage);
		
		if (!error) {
			fprintf(stderr, "Darken requires a single parameter 'percentage' to be between 0 and 100\n\tdarken 50\n");
			return 0;
		}
		
		if (percentage < 0 || percentage > 100) {
			fprintf(stderr, "Please enter a percentage between 0 and 100\n");
			return 0;
		}
	}
	
	readCSImageValues(stdin, &image);
	
	readImageData(stdin, &image);
	
	darken(percentage, image.imWidth * image.imHeight, image.data);
	
	writeCSImage(stdout, &image);
	
	return 0;
}

/**
 * Darkens each pixel in data by the given percentage
 *
 *
 * @param  percentage - the percentage to darken the image by
 * @param  numPixels - the total number of pixels in the image
 * @param  *data - the image (pixel) data
 */
void darken(int percentage, int numPixels, struct pixel *data) {
	int i;
	double perc = percentage / 100.0;
	
	if (percentage == 0) {
		return;
	}
	
	for (i = 0; i < numPixels; i++) {
		data[i].red -= (data[i].red * perc);
		data[i].green -= (data[i].green * perc);
		data[i].blue -= (data[i].blue * perc);
	}
}
