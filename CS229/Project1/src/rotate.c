/**
 * @file rotate.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the rotating of an image
 */

#include <stdio.h>
#include <stdlib.h>
#include "rotate.h"

int main(int argc, char *argv[]) {
	struct image image;
	int error;
	int rotateAmount;
		
	if (argc < 2) {
		fprintf(stderr, "Rotate requires a single parameter '90' (CW), '-90' (CCW), '180' \n\trotate -90\n");
		return 0;
	} else {
		error = sscanf(argv[1], "%d", &rotateAmount);
		
		if (!error) {
			fprintf(stderr, "Rotate requires a single parameter '90' (CW), '-90' (CCW), '180' \n\trotate -90\n");
			return 0;
		}
		
		if (rotateAmount != 90 && rotateAmount != -90 && rotateAmount != 180) {
			fprintf(stderr, "Please enter an amount to rotate '90' (CW), '-90' (CCW), '180'\n");
			return 0;
		}
	}
	
	readCSImageValues(stdin, &image);
	
	readImageData(stdin, &image);
	
	if (rotateAmount == 90) {
		rotate90(&image);
	} else if (rotateAmount == 180) {
		rotate180(&image);	
	} else if (rotateAmount == -90) {
		rotateN90(&image);		
	}
	
	writeCSImage(stdout, &image);
	
	return 0;
}

/**
 * Rotates the image by 90 degrees clockwise. Note, this creates a new array, 
 * frees the old one from the heap, and returns the new pointer.
 *
 * This is done by iterating the image and swapping pixels.
 * 
 * Follows the following formula:
 *		swap data[(imHeight - 1 - i) + j * (imHeight)] for data[i * imWidth + j]
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void rotate90(struct image *image_ptr) {
	int i, j, pos;
	unsigned int temp;
	struct pixel *newData = (struct pixel *) malloc(image_ptr->imWidth * image_ptr->imHeight * sizeof(struct pixel));
	
	if (newData == NULL) {
		fprintf(stderr, "There was an error allocating the necessary memory to hold this image\n");
		return;
	}
	
	/* Perform the rotation, placing the data into a new array */
	for (i = 0; i < image_ptr->imHeight; i++) {
		for (j = 0; j < image_ptr->imWidth; j++) {
			pos = (image_ptr->imHeight - 1 - i) + j * (image_ptr->imHeight);
			newData[pos] = image_ptr->data[i * image_ptr->imWidth + j];
		}
	}

	/* Free the old data array and reassign it to the new data */
	free(image_ptr->data);
	image_ptr->data = newData;
	
	/* Modify the dimensions of the image */
	temp = image_ptr->imWidth;
	image_ptr->imWidth = image_ptr->imHeight;
	image_ptr->imHeight = temp;
}

/**
 * Rotates the image by 90 degrees counter-clockwise. Note, this creates a new array, 
 * frees the old one from the heap, and returns the new pointer.
 *
 * This is done by iterating the image and swapping pixels.
 * 
 * Follows the following formula:
 *		swap data[(imHeight - 1 - j) * (imHeight) + i] for data[i * imWidth + j]
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void rotateN90(struct image *image_ptr) {
	int i, j, pos;
	unsigned int temp;
	struct pixel *newData = (struct pixel *) malloc(image_ptr->imHeight * image_ptr->imWidth * sizeof(struct pixel));
	
	if (newData == NULL) {
		fprintf(stderr, "There was an error allocating the necessary memory to hold this image\n");
		return;
	}
	
	/* Perform the rotation, placing the data into a new array */
	for (i = 0; i < image_ptr->imHeight; i++) {
		for (j = 0; j < image_ptr->imWidth; j++) {
			pos = (image_ptr->imWidth - 1 - j) * (image_ptr->imHeight) + i;
			newData[pos] = image_ptr->data[i * image_ptr->imWidth + j];
		}
	}
	
	/* Free the old data array and reassign it to the new data */
	free(image_ptr->data);
	image_ptr->data = newData;
	
	/* Modify the dimensions of the image */
	temp = image_ptr->imWidth;
	image_ptr->imWidth = image_ptr->imHeight;
	image_ptr->imHeight = temp;
}

/**
 * Rotates the image by 180 degrees. Note, this does all the processing
 * and modifies the given data array.
 *
 * This is done by iterating the image and swapping pixels.
 * eg: first pixel  --> last pixel
 *     second pixel --> last - 1 pixel
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void rotate180(struct image *image_ptr) {
	int i;
	struct pixel temp;
	int numPixels = image_ptr->imWidth * image_ptr->imHeight;
		
	for (i = 0; i < numPixels / 2; i++) {
		temp = image_ptr->data[numPixels - i];
		image_ptr->data[numPixels - i] = image_ptr->data[i];
		image_ptr->data[i] = temp;
	}
}
