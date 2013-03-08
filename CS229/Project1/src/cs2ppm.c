/**
 * @file cs2ppm.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file takes a CS229 image file and converts it into a PPM file.
 */
#include <stdio.h>
#include "imageutil.h"

int main(int argc, char **argv) {	
	struct image image;
	
	readCSImageValues(stdin, &image);
	
	if (image.channelSize.red == FOUR_BIT || image.channelSize.green == FOUR_BIT || image.channelSize.blue == FOUR_BIT) {
		fprintf(stderr, "This will not work on an image with 4 bit channels\n");
		return 0;
	}
	
	if (image.isBW) {
		fprintf(stderr, "PPM does not support BW images\n");
		return 0;
	}
	
	if (image.channelSize.red != image.channelSize.green || image.channelSize.red != image.channelSize.blue || image.channelSize.green != image.channelSize.blue) {
		fprintf(stderr, "CS2PPM needs images with the same size channels for Red, Green and Blue\n");
		return 0;
	}
	
	readImageData(stdin, &image);
	
	writePPMImage(&image);
	
	return 0;
}
