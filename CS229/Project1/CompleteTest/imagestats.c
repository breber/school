/**
 * @file imagestats.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file reads a CS229 image file and displays info about it.
 */

#include <stdio.h>
#include "imagestats.h"

int main(int argc, char **argv) {
	FILE *file;
	struct image image;
	
	int black = 0;
	int white = 0;
	int err = 0;
	
	if (argc <= 1) {
		file = stdin;
	} else {
		file = fopen(argv[1], "r");
		
		if (file == NULL) {
			fprintf(stderr, "Error opening the specified file\n");
			return 0;
		}
	}
	
	err = readCSImageValues(file, &image);
		
	if (err == -1) {
		return 0;
	}
		
	readImageData(file, &image);
	processData(&image, &white, &black);
	
	
	printf("CS229 Image Stats\n");
	printf("\t%s\n", image.isBW ? "Black and White Image":"Color Image");
	
	if (image.isBW) {
		printf("\tBits per channel: %d\n", getChannelSize(image.channelSize.red));
	} else {
		printf("\tBits for Red channel:   %d\n", getChannelSize(image.channelSize.red));
		printf("\tBits for Blue channel:  %d\n", getChannelSize(image.channelSize.green));
		printf("\tBits for Green channel: %d\n", getChannelSize(image.channelSize.blue));
	}
	
	printf("\tImage Width:  %d\n", image.imWidth);
	printf("\tImage Height: %d\n", image.imHeight);
	
	
	printf("\tPercent Black Pixels: %.2f%%\n", ((black / (image.imWidth * image.imHeight * 1.0)) * 100.0));
	printf("\tPercent White Pixels: %.2f%%\n", ((white / (image.imWidth * image.imHeight * 1.0)) * 100.0));
	
	return 0;
}

/**
 * Reads the image data, and counts the number of black pixels
 * and the number of white pixels
 *
 * @param  *image_ptr - the image struct containg all the image information
 * @param  *white - the number of white pixels in the image (output param)
 * @param  *black - the number of black pixels in the image (output param)
 */
void processData(struct image *image_ptr, int *white, int *black) {
	int i;
	int blackCount = 0;
	int whiteCount = 0;
	
	if (image_ptr->isBW) {
		for (i = 0; i < image_ptr->imWidth * image_ptr->imHeight; i++) {
			if (image_ptr->data[i].red == getBlack(image_ptr->channelSize.red)) {
				blackCount++;
			} else if (image_ptr->data[i].red == getWhite(image_ptr->channelSize.red)) {
				whiteCount++;
			}
		}
	} else {
		for (i = 0; i < image_ptr->imWidth * image_ptr->imHeight; i++) {
			/* Initialize our temporary pixel to something other than white or black */
			struct pixel isBW_RGB = {0x50, 0x50, 0x50};
			
			if (image_ptr->data[i].red == getBlack(image_ptr->channelSize.red)) {
				isBW_RGB.red = BLACK_8; 
			} else if (image_ptr->data[i].red == getWhite(image_ptr->channelSize.red)) {
				isBW_RGB.red = WHITE_8; 
			}
			if (image_ptr->data[i].green == getBlack(image_ptr->channelSize.green)) {
				isBW_RGB.green = BLACK_8; 
			} else if (image_ptr->data[i].green == getWhite(image_ptr->channelSize.green)) {
				isBW_RGB.green = WHITE_8; 
			}
			if (image_ptr->data[i].blue == getBlack(image_ptr->channelSize.blue)) {
				isBW_RGB.blue = BLACK_8; 
			} else if (image_ptr->data[i].blue == getWhite(image_ptr->channelSize.blue)) {
				isBW_RGB.blue = WHITE_8; 
			}
						
			if (isBW_RGB.red == BLACK_8 && isBW_RGB.green == BLACK_8 && isBW_RGB.blue == BLACK_8) {
				blackCount++;
			} else if (isBW_RGB.red == WHITE_8 && isBW_RGB.green == WHITE_8 && isBW_RGB.blue == WHITE_8) {
				whiteCount++;
			}
		}
	}
	
	*white = whiteCount;
	*black = blackCount;
}
