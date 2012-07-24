/**
 * @file makesquare.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles writing an image with a square
 * of the specified size in the middle of it.
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "makesquare.h"

int main(int argc, char **argv) {	
	struct image image;
	
	int sqWidth = 0;
	int sqHeight = 0;
	int sqColors[3] = {-1, -1, -1};

	image.channelSize.red = 0x02;
	image.channelSize.green = 0x02;
	image.channelSize.blue = 0x02;
	
	if (argc <= 1) {
		readValues(&image, &sqWidth, &sqHeight, sqColors);
	} else {
		image.isBW = isBlackWhite(argv[1]);
		if (image.isBW == -1) {
			fprintf(stderr, "Black and white not specified correctly. Should be '-bw' or '-c'\n");
			return -1;
		}
		
		
		if (!image.isBW && argc < 9) {
			fprintf(stderr, "Not enough command line args\n");
			fprintf(stderr, "makesquare  (-c | -bw) imagewidth imageheight squarewidth squareheight squarecolor ([0-255 0-255 0-255] | [0-255])\n");
			return 0;
		} else if (image.isBW && argc < 7) {
			fprintf(stderr, "Not enough command line args\n");
			fprintf(stderr, "makesquare  (-c | -bw) imagewidth imageheight squarewidth squareheight squarecolor ([0-255 0-255 0-255] | [0-255])\n");
			return 0;
		}
		
		sscanf(argv[2], "%u", &image.imWidth);
		if (image.imWidth <= 0) {
			fprintf(stderr, "Image width can't be less than 1\n");
			return 0;
		}
		
		sscanf(argv[3], "%u", &image.imHeight);
		if (image.imHeight <= 0) {
			fprintf(stderr, "Image height can't be less than 1\n");
			return 0;
		}
		
		sscanf(argv[4], "%d", &sqWidth);
		if (sqWidth < 0 || sqWidth > image.imWidth) {
			fprintf(stderr, "Square width can't be less than 0 or greater than the image width\n");
			return 0;
		}
		
		sscanf(argv[5], "%d", &sqHeight);
		if (sqHeight < 0 || sqHeight > image.imHeight) {
			fprintf(stderr, "Square height can't be less than 0 or greater than the image height\n");
			return 0;
		}
		
		if (image.isBW) {
			sscanf(argv[6], "%d", &sqColors[0]);
			if (sqColors[0] < BLACK_8 || sqColors[0] > WHITE_8) {
				fprintf(stderr, "Color must be in range of 0 to 255\n");
				return 0;
			}
		} else {
			sscanf(argv[6], "%d", &sqColors[0]);
			sscanf(argv[7], "%d", &sqColors[1]);
			sscanf(argv[8], "%d", &sqColors[2]);
			
			if (sqColors[0] == -1 || sqColors[1] == -1 || sqColors[2] == -1) {
				fprintf(stderr, "Improper color format\n");
				return 0;
			}
			
			if (sqColors[0] < BLACK_8 || sqColors[0] > WHITE_8 || sqColors[1] < BLACK_8 || sqColors[1] > WHITE_8 || sqColors[2] < BLACK_8 || sqColors[2] > WHITE_8) {
				fprintf(stderr, "Color must be in range of 0 to 255\n");
				return 0;
			}
		}
	}
		
	image.data = (struct pixel *) malloc(image.imWidth * image.imHeight * sizeof(struct pixel));
	
	if (image.data == NULL) {
		fprintf(stderr, "There was an error allocating the necessary memory to hold this image\n");
		return 0;
	}
	
	createImageData(&image, sqWidth, sqHeight, sqColors);	
	
	writeCSImage(stdout, &image);
	
	return 0;
}

/**
 * Reads the image properties from stdin.
 *
 * @param  *image_ptr - the image struct to write the image properties into
 * @param  *sqWidth - the width of the square in to be drawn in this image
 * @param  *sqHeight - the height of the square in to be drawn in this image
 * @param  sqColors[] - represents the colors of the square. Should be an array of length 3,
 *			with red channel as first element, blue channel as second, and green channel 
 *			being third. If image is black and white, only the first element is used.
 * @return If everything completes successfully, returns a 0. If user enters bad data, return -1
 */
int readValues(struct image *image_ptr, int *sqWidth, int *sqHeight, int sqColors[]) {
	char colorBW[10];
	
	scanf("%s", colorBW);
	image_ptr->isBW = isBlackWhite(colorBW);
	if (image_ptr->isBW == -1) {
		fprintf(stderr, "Black and white not specified correctly. Should be '-bw' or '-c'\n");
		return -1;
	}
	
	scanf("%u", &(image_ptr->imWidth));
	if (image_ptr->imWidth <= 0) {
		fprintf(stderr, "Image width can't be less than 1\n");
		return -1;
	}
	
	scanf("%u", &(image_ptr->imHeight));
	if (image_ptr->imHeight <= 0) {
		fprintf(stderr, "Image height can't be less than 1\n");
		return -1;
	}
	
	scanf("%d", sqWidth);
	if (*sqWidth < 0 || *sqWidth > image_ptr->imWidth) {
		fprintf(stderr, "Square width can't be less than 0 or greater than the image width\n");
		return -1;
	}
	
	scanf("%d", sqHeight);
	if (*sqHeight < 0 || *sqHeight > image_ptr->imHeight) {
		fprintf(stderr, "Square height can't be less than 0 or greater than the image height\n");
		return -1;
	}
	
	if (image_ptr->isBW) {
		scanf("%d", &sqColors[0]);
		if (sqColors[0] == -1) {
			fprintf(stderr, "Improper color format\n");
			return -1;
		}
		
		if (sqColors[0] < BLACK_8 || sqColors[0] > WHITE_8) {
			fprintf(stderr, "Color must be in range of 0 to 255\n");
			return -1;
		}
		
		sqColors[1] = -1;
		sqColors[2] = -1;
	} else {
		scanf("%d %d %d", &sqColors[0], &sqColors[1], &sqColors[2]);
		if (sqColors[0] == -1 || sqColors[1] == -1 || sqColors[2] == -1) {
			fprintf(stderr, "Improper color format\n");
			return -1;
		}
		
		if (sqColors[0] < BLACK_8 || sqColors[0] > WHITE_8 || sqColors[1] < BLACK_8 || sqColors[1] > WHITE_8 || sqColors[2] < BLACK_8 || sqColors[2] > WHITE_8) {
			fprintf(stderr, "Color must be in range of 0 to 255\n");
			return -1;
		}
	}
	
	return 0;
}

/**
 * Writes the image data to the given file.
 *
 * @param  *image_ptr - the image struct to write the image data into
 * @param  sqWidth - the width of the square in to be drawn in this image
 * @param  sqHeight - the height of the square in to be drawn in this image
 * @param  sqColors[] - the colors of the square. Should be an array of length 3,
 *			with red channel as first element, blue channel as second, and green channel 
 *			being third. If image is black and white, only the first element is used.
 * @param  *data - the image (pixel) data array to be filled in
 */
void createImageData(struct image *image_ptr, int sqWidth, int sqHeight, int sqColors[]) {
	int i;
	int j;
	int count = 0;
	int rowSquareMin = (image_ptr->imHeight - sqHeight) / 2;
	int rowSquareMax = image_ptr->imHeight - ((image_ptr->imHeight - sqHeight) / 2) - 1;
	int colSquareMin = (image_ptr->imWidth - sqWidth) / 2;
	int colSquareMax = image_ptr->imWidth - ((image_ptr->imWidth - sqWidth) / 2) - 1;
		
	for (i = 0; i < image_ptr->imHeight; i++) {
		for (j = 0; j < image_ptr->imWidth; j++) {
			if (i < colSquareMin || i > colSquareMax || j < rowSquareMin || j > rowSquareMax) { 
				if (image_ptr->isBW) {
					image_ptr->data[count].red = WHITE_8;
				} else {
					image_ptr->data[count].red = WHITE_8;
					image_ptr->data[count].green = WHITE_8;
					image_ptr->data[count].blue = WHITE_8;
				}
				
			} else {
				if (image_ptr->isBW) {
					image_ptr->data[count].red = sqColors[0];
				} else {
					image_ptr->data[count].red = sqColors[0];
					image_ptr->data[count].green = sqColors[1];
					image_ptr->data[count].blue = sqColors[2];
				}
			}
			count++;
		}
	}
}

/**
 * Figures out whether this image is black and white.
 *
 * @param  *bwString - the string the user passed in via command line or stdin
 * @return 0 if this image is color, 1 if this image is color, -1 if the string
 *			doesn't express either.
 */
int isBlackWhite(char *bwString) {
	if (!strcmp(bwString, "-bw") || !strcmp(bwString, "bw")) {
		return 1;
	}
	
	if (!strcmp(bwString, "-c") || !strcmp(bwString, "c")) {
		return 0;
	}
	
	return -1;
}
