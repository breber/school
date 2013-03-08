/**
 * @file ppm2cs.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file takes a PPM image file and converts it into a CS229 file.
 */
#include <stdio.h>
#include <string.h>
#include "ppm2cs.h"

int main(int argc, char **argv) {	
	struct image image;
	
	int maxVal = 0;
	int csMaxVal = 0;
	
	readPPMImageValues(&image.imWidth, &image.imHeight, &maxVal);
	image.isBW = 0;
	
	csMaxVal = (maxVal > WHITE_8) ? WHITE_16 : WHITE_8;
	
	if (csMaxVal == WHITE_8) {
		image.channelSize.red = EIGHT_BIT;
		image.channelSize.green = EIGHT_BIT;
		image.channelSize.blue = EIGHT_BIT;
	} else if (csMaxVal == WHITE_16) {
		image.channelSize.red = SIXTEEN_BIT;
		image.channelSize.green = SIXTEEN_BIT;
		image.channelSize.blue = SIXTEEN_BIT;
	}

	readImageData(stdin, &image);
	
	if (maxVal != csMaxVal) {
		scaleImageValues(image.imWidth * image.imHeight, maxVal, csMaxVal, image.data);
	}

	writeCSImage(stdout, &image);
	
	return 0;
}

/**
 * Scales the image pixels to the correct max value.
 *
 * Since PPM images can have any maxval lower than 2^16, we need
 * to scale it to either 2^8 (white value of 8 bit images), or 2^16 (white
 * value of 16 bit images).
 *
 * @param  numPixels - the number of pixels in the image
 * @param  oldMaxVal - the max val read in from the image
 * @param  newMaxVal - the maximum color value to scale to (should be 0xFF or 0xFFFF)
 * @param  *data - the image (pixel) data
 */
void scaleImageValues(int numPixels, int oldMaxVal, int newMaxVal, struct pixel *data) {
	int i;
	
	for (i = 0; i < numPixels; i++) {
		data[i].red = (data[i].red * newMaxVal) / oldMaxVal;
		data[i].green = (data[i].green * newMaxVal) / oldMaxVal;
		data[i].blue = (data[i].blue * newMaxVal) / oldMaxVal;
	}
}

/**
 * Reads the image properties from stdin.
 *
 * @param  *imWidth - the image width
 * @param  *imHeight - the image height
 * @param  *maxVal - the maximum color value possible in this image
 */
void readPPMImageValues(unsigned int *imWidth, unsigned int *imHeight, int *maxVal) {
	char isPPM[10] = "";
	
	/* The "Magic" string */
	scanf("%s", isPPM);
	if (strcmp(isPPM, PPM_MAGIC) != 0) {
		fprintf(stderr, "This isn't a PPM image");
	}
	
	/* Image width and height */
	scanf("%u", imWidth);
	scanf("%u", imHeight);
	
	/* Max val */
	scanf("%d", maxVal);
	
	/* Need to eat the single whitespace character so it doesn't mess up the image data */
	getc(stdin);
}
