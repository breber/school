/**
 * @file convolve.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles processing of a convolution on a CS229 image
 */

#ifndef CONVOLVE
#define CONVOLVE

#include "imageutil.h"

#define OUTSIDE -1
#define WITHIN 0

/*
 * Represents a convolution kernel
 */
struct kernel {
	int size;
	double *kernelValues;
};

/*
 * Represents a coordinate on a 2D plane
 */
struct coord {
	int x;
	int y;
};

/*
 * Stores the location of the 4 corners of an image
 */
struct boundaries {
	struct coord topR;
	struct coord topL;
	struct coord botR;
	struct coord botL;
};

/*
 * Reads the information about the kernel from the given kernel file
 * 
 * @param *kernelFile - the file containing the kernel info
 * @param *kernelR - the kernel for the red channel
 * @param *kernelG - the kernel for the green channel
 * @param *kernelB - the kernel for the blue channel
 * @return -1 if there was a parsing error, 0 otherwise
 */
int parseKernelFile(FILE *kernelFile, struct kernel *kernelR, struct kernel *kernelG, struct kernel *kernelB);

/*
 * Reads the information about a single kernel from the given kernel file
 * 
 * @param *kernelFile - the file containing the kernel info
 * @param *kernel - the kernel to write the data into
 * @return -1 if there was a parsing error, -2 if there is a channel missing, 0 otherwise
 */
int readSingleKernel(FILE *kernelFile, struct kernel *kernel);

/*
 * Iterates through the image performing the convolution on each pixel. At the end of this, the given image's
 * data will be updated with the new data.
 * 
 * @param *image - the image
 * @param *kernelR - the kernel for the red channel
 * @param *kernelG - the kernel for the green channel
 * @param *kernelB - the kernel for the blue channel
 */
void performConvolution(struct image *image, struct kernel *kernelR, struct kernel *kernelG, struct kernel *kernelB);

/*
 * Processes the kernel and image data for the given kernel boundaries, and returns the result for the center position
 * 
 * @param *image - the actual image to perform the convolution on
 * @param *kernel - the kernel information
 * @param *kernelBound - the struct containing the boundaries of the kernel
 * @param channel - the struct containing information about which channel the operation is on
 * @return the value to put in the image data for the center of the kernel
 */
unsigned short int processNecessarySpaces(struct image *image, struct kernel *kernel, struct boundaries kernelBounds, struct pixel channel);

/*
 * Fills in the boundaries of the kernel in relation to the image
 * 
 * @param centerPos - the center position of the kernel
 * @param *kernel - the kernel information
 * @param *kernelBound - the struct to place the boundaries of the kernel
 */
void fillInKernelBoundaries(struct coord centerPos, struct kernel *kernel, struct boundaries *kernelBound);

/*
 * Figures out if the given coordinate is within the given boundaries.
 * 
 * @param x - the x coordinate
 * @param y - the y coordinate
 * @param boundaries - the boundaries to check against
 * @return OUTSIDE  if the coordinate is outside the image coordinate system
 *		   WITHIN	if the coordinate is within the boundaries
 */
int boundsCheck(int x, int y, struct image *boundaries);

#endif
