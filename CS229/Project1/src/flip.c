/**
 * @file flip.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the flipping of an image
 */

#include <stdio.h>
#include "flip.h"

int main(int argc, char *argv[]) {
	struct image image;
	int error;
	char flip;
		
	if (argc < 2) {
		fprintf(stderr, "Rotate requires a single parameter 'h' or 'v'\n\tflip 'v'\n");
		return 0;
	} else {
		error = sscanf(argv[1], "%c", &flip);
		
		if (!error) {
			fprintf(stderr, "Rotate requires a single parameter 'h' or 'v'\n\tflip 'v'\n");
			return 0;
		}
		
		if (flip != 'v' && flip != 'h') {
			fprintf(stderr, "Rotate requires a single parameter 'h' or 'v'\n\tflip 'v'\n");
			return 0;
		}
	}
	
	readCSImageValues(stdin, &image);
		
	readImageData(stdin, &image);
	
	if (flip == 'h') {
		flipH(&image);
		writeCSImage(stdout, &image);
	} else if (flip == 'v') {
		flipV(&image);
		writeCSImage(stdout, &image);		
	}
	
	return 0;
}

/**
 * Flips the image across the x axis (flips vertically).
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void flipH(struct image *image_ptr) {
	int i, j, pos;
	struct pixel temp;
	
	for (i = 0; i < image_ptr->imHeight; i++) {
		for (j = 0; j < image_ptr->imWidth / 2 + 1; j++) {
			pos = (i * image_ptr->imWidth) + ((image_ptr->imWidth - 1) - j);
			temp = image_ptr->data[pos];
			image_ptr->data[pos] = image_ptr->data[(i * image_ptr->imWidth) + j];
			image_ptr->data[(i * image_ptr->imWidth) + j] = temp;
		}
	}
}

/**
 * Flips the image across the y axis (flips horizontally).
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void flipV(struct image *image_ptr) {
	int i, j, pos;
	struct pixel temp;
	
	for (i = 0; i < image_ptr->imHeight / 2 + 1; i++) {
		for (j = 0; j < image_ptr->imWidth; j++) {
			pos = ((image_ptr->imHeight - 1 - i) * image_ptr->imWidth) + j;
			temp = image_ptr->data[pos];
			image_ptr->data[pos] = image_ptr->data[(i * image_ptr->imWidth) + j];
			image_ptr->data[(i * image_ptr->imWidth) + j] = temp;
		}
	}
}
