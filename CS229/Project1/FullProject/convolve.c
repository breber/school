/**
 * @file convolve.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles processing of a convolution on a CS229 image
 */

#include <stdio.h>
#include <stdlib.h>
#include "convolve.h"

int main(int argc, char *argv[]) {
	struct image image;
	FILE *kernelFile;
	
	struct kernel kernelR;
	struct kernel kernelG;
	struct kernel kernelB;
	
	int error;
	
	if (argc < 2) {
		fprintf(stderr, "Convolve requires 1 parameter, indicating the file containing the kernel\n\tconvolve kernel_file\n");
		return 0;
	} else {
		kernelFile = fopen(argv[1], "r");
		
		if (kernelFile == NULL) {
			fprintf(stderr, "Error opening kernel file\n");
			return 0;
		}
	}
	
	/* Read image data from stdin */
	readCSImageValues(stdin, &image);
	readImageData(stdin, &image);
	
	error = parseKernelFile(kernelFile, &kernelR, &kernelG, &kernelB);
	
	if (image.isBW && (kernelG.size != -1 || kernelB.size != -1)) {
		fprintf(stderr, "Image is black and white, and this is a color kernel\n");
		return 0;
	}
	if (!image.isBW && (kernelG.size == -1 || kernelB.size == -1)) {
		fprintf(stderr, "Image is color, and this kernel is missing data\n");
		return 0;
	}
	
	if (error == -1) {
		return 0;
	}

	performConvolution(&image, &kernelR, &kernelG, &kernelB);
	
	writeCSImage(stdout, &image);

	return 0;
}

/*
 * Reads the information about the kernel from the given kernel file
 * 
 * @param *kernelFile - the file containing the kernel info
 * @param *kernelR - the kernel for the red channel
 * @param *kernelG - the kernel for the green channel
 * @param *kernelB - the kernel for the blue channel
 * @return -1 if there was a parsing error, 0 otherwise
 */
int parseKernelFile(FILE *kernelFile, struct kernel *kernelR, struct kernel *kernelG, struct kernel *kernelB) {
	int error;
	
	
	error = readSingleKernel(kernelFile, kernelR);
	if (error) {
		return -1;
	}
	
	error = readSingleKernel(kernelFile, kernelG);
	if (error == -2) {
		kernelG->size = -1;
		return -1;
	} else if (error) {
		return -1;
	}
	
	error = readSingleKernel(kernelFile, kernelB);
	if (error == -2) {
		kernelB->size = -1;
		return -1;
	} else if (error) {
		return -1;
	}
	
	return 0;
}

/*
 * Reads the information about a single kernel from the given kernel file
 * 
 * @param *kernelFile - the file containing the kernel info
 * @param *kernel - the kernel to write the data into
 * @return -1 if there was a parsing error, -2 if there is a channel missing, 0 otherwise
 */
int readSingleKernel(FILE *kernelFile, struct kernel *kernel) {
	int numRead;
	int size;
	int i;
	
	numRead = fscanf(kernelFile, "%d", &size);
	
	if (numRead < 1) {
		fprintf(stderr, "Could not read kernel size\n");
		return -2;
	} else if (size % 2 == 0) {
		fprintf(stderr, "Kernel must have an odd number size\n");
		return -1;
	} else if (size > 15 || size < 1) {
		fprintf(stderr, "Kernel size must be greater than 1 and less than or equal to 15\n");
		return -1;
	}
	
	kernel->size = size;
	kernel->kernelValues = (double *) malloc(size * size * sizeof(double));
	
	for (i = 0; i < size * size; i++) {
		double temp;
		
		numRead = fscanf(kernelFile, "%lf", &temp);
		if (numRead == 1) {
			kernel->kernelValues[i] = temp;
		} else {
			fprintf(stderr, "This kernel file is missing information\n");
			return -1;
		}		
	}
	
	return 0;
}

/*
 * Iterates through the image performing the convolution on each pixel. At the end of this, the given image's
 * data will be updated with the new data.
 * 
 * @param *image - the image
 * @param *kernelR - the kernel for the red channel
 * @param *kernelG - the kernel for the green channel
 * @param *kernelB - the kernel for the blue channel
 */
void performConvolution(struct image *image, struct kernel *kernelR, struct kernel *kernelG, struct kernel *kernelB) {
	int i, j, count = 0;
	struct boundaries kernelRBound;
	struct boundaries kernelGBound;
	struct boundaries kernelBBound;
	
	struct pixel *newData = (struct pixel *) malloc(image->imWidth * image->imHeight * sizeof(struct pixel));
	
	for (i = 0; i < image->imHeight; i++) {
		for (j = 0; j < image->imWidth; j++, count++) {
			struct coord kernelCenterPos;
			struct pixel channelProcessing;
			kernelCenterPos.y = i;
			kernelCenterPos.x = j;

			fillInKernelBoundaries(kernelCenterPos, kernelR, &kernelRBound);
			fillInKernelBoundaries(kernelCenterPos, kernelG, &kernelGBound);
			fillInKernelBoundaries(kernelCenterPos, kernelB, &kernelBBound);
			
			channelProcessing.red = 0xFF;
			channelProcessing.green = 0;
			channelProcessing.blue = 0;
			newData[count].red = processNecessarySpaces(image, kernelR, kernelRBound, channelProcessing);
			
			if (!(image->isBW)) {
				channelProcessing.red = 0;
				channelProcessing.green = 0xFF;
				channelProcessing.blue = 0;
				newData[count].green = processNecessarySpaces(image, kernelG, kernelGBound, channelProcessing);
				
				channelProcessing.red = 0;
				channelProcessing.green = 0;
				channelProcessing.blue = 0xFF;
				newData[count].blue = processNecessarySpaces(image, kernelB, kernelBBound, channelProcessing);
			} else {
				/* BW image - Just to be sure, we will set the new data to 0 */
				newData[count].green = 0;
				newData[count].blue = 0;
			}
		}
	}
	
	free(image->data);
	image->data = newData;
}

/*
 * Processes the kernel and image data for the given kernel boundaries, and returns the result for the center position
 * 
 * @param *image - the actual image to perform the convolution on
 * @param *kernel - the kernel information
 * @param *kernelBound - the struct containing the boundaries of the kernel
 * @param channel - the struct containing information about which channel the operation is on
 * @return the value to put in the image data for the center of the kernel
 */
unsigned short int processNecessarySpaces(struct image *image, struct kernel *kernel, struct boundaries kernelBounds, struct pixel channel) {
	int i, j;
	double tempPixelVal = 0;
	
	for (i = 0; i < kernel->size; i++) {
		for (j = 0; j < kernel->size; j++) {
			int imagePosInArray = (kernelBounds.topL.x + j) + image->imWidth * (kernelBounds.topL.y + i);
			unsigned short int currentChannelVal;
			if (boundsCheck(kernelBounds.topL.x + j, kernelBounds.topL.y + i, image) == WITHIN) {
				if (channel.red == 0xFF) {
					currentChannelVal = image->data[imagePosInArray].red;
				} else if (channel.green == 0xFF) {
					currentChannelVal = image->data[imagePosInArray].green;
				} else if (channel.blue == 0xFF) {
					currentChannelVal = image->data[imagePosInArray].blue;
				}
				tempPixelVal += kernel->kernelValues[i * kernel->size + j] * currentChannelVal;
			}
		}
	}
	
	if (tempPixelVal < 0) {
		tempPixelVal = 0;
	} else {
		tempPixelVal += .5;
	}
	
	if (channel.red == 0xFF && tempPixelVal > getWhite(image->channelSize.red)) {
		tempPixelVal = getWhite(image->channelSize.red);
	} else if (channel.green == 0xFF && tempPixelVal > getWhite(image->channelSize.green)) {
		tempPixelVal = getWhite(image->channelSize.green);
	} else if (channel.blue == 0xFF && tempPixelVal > getWhite(image->channelSize.blue)) {
		tempPixelVal = getWhite(image->channelSize.blue);
	}
	
	return (unsigned short int) tempPixelVal;
}

/*
 * Fills in the boundaries of the kernel in relation to the image
 * 
 * @param centerPos - the center position of the kernel
 * @param *kernel - the kernel information
 * @param *kernelBound - the struct to place the boundaries of the kernel
 */
void fillInKernelBoundaries(struct coord centerPos, struct kernel *kernel, struct boundaries *kernelBound) {
	int half = .5 * kernel->size;
	
	kernelBound->topL.x = centerPos.x - half;
	kernelBound->topL.y = centerPos.y - half;
	kernelBound->topR.x = centerPos.x + half;
	kernelBound->topR.y = kernelBound->topL.y;
	
	kernelBound->botL.x = kernelBound->topL.x;
	kernelBound->botL.y = centerPos.y + half;
	kernelBound->botR.x = kernelBound->topR.x;
	kernelBound->botR.y = kernelBound->botL.y;
}

/*
 * Figures out if the given coordinate is within the given boundaries.
 * 
 * @param x - the x coordinate
 * @param y - the y coordinate
 * @param boundaries - the boundaries to check against
 * @return OUTSIDE  if the coordinate is outside the image coordinate system
 *		   WITHIN	if the coordinate is within the boundaries
 */
int boundsCheck(int x, int y, struct image *boundaries) {
	if (x > (boundaries->imWidth - 1)) {
		return OUTSIDE;
	}
	if (x < 0) {
		return OUTSIDE;
	}
	
	if (y < 0) {
		return OUTSIDE;
	}
	if (y > (boundaries->imHeight - 1)) {
		return OUTSIDE;
	}
	
	return WITHIN;
}
