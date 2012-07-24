/**
 * @file stitch.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the stitching of 2 CS229 images together
 */

#include <stdio.h>
#include <stdlib.h>
#include "stitch.h"

int main(int argc, char *argv[]) {
	struct image in1;
	struct image in2;
	struct image out;
	FILE *inFile1;
	FILE *inFile2;
	FILE *outFile;
	
	struct loc_value posOfOverlap;
	
	int error;
	
	if (argc < 4) {
		fprintf(stderr, "Stitch requires 3 parameters\n\tstitch infile1 infile2 outfile\n");
		return 0;
	} else {
		inFile1 = fopen(argv[1], "r");
		
		if (inFile1 == NULL) {
			fprintf(stderr, "Could not open first file for reading\n");
			return 0;
		}
		
		inFile2 = fopen(argv[2], "r");
		if (inFile2 == NULL) {
			fprintf(stderr, "Could not open second file for reading\n");
			return 0;
		}
		
		outFile = fopen(argv[3], "w");
		if (outFile == NULL) {
			fprintf(stderr, "Could not open third file for writing\n");
			return 0;
		}
	}
	
	/* Read image data from the two images */
	readCSImageValues(inFile1, &in1);
	readImageData(inFile1, &in1);
	
	readCSImageValues(inFile2, &in2);
	readImageData(inFile2, &in2);
	
	error = doErrorChecking(&in1);
	if (error == -1) {
		return 0;
	}
	error = doErrorChecking(&in2);
	if (error == -1) {
		return 0;
	}
	
	/* Process!! */
	findLocWithSmallestError(&in1, &in2, &posOfOverlap);
	
	/* Create the new image from the information found in the previous part */
	createNewImageData(&in1, &in2, &out, &posOfOverlap);
	
	/* Write the new image to the given output file */
	writeCSImage(outFile, &out);
	
	return 0;
}

/*
 * Checks if this image is ok to stitch
 * 
 * @param *image - the image to check
 * @return 0 if it is ok, -1 if not
 */
int doErrorChecking(struct image *image) {
	if (getChannelSize(image->channelSize.red) != 8 || getChannelSize(image->channelSize.green) != 8 || getChannelSize(image->channelSize.blue) != 8) {
		fprintf(stderr, "Stitch only works on 8-bit color or 8-bit black and white images\n");
		return -1;
	}
	
	return 0;
}

/*
 * Goes through both images, and finds the location with minimum error.
 * 
 * @param *im1 - the first image
 * @param *im2 - the second image
 * @param *minLoc - the struct to store the location of the minimum error
 */
void findLocWithSmallestError(struct image *im1, struct image *im2, struct loc_value *minLoc) {
	int numNeededToOverlap = findNeededOverlapPixels(im1, im2);
	int i, j;
	struct image *smallerIm;
	struct image *largerIm;
		
	minLoc->value = -1;
	
	/* 
	 * Figure out which image is bigger - for this algorithm, we keep the larger image
	 * stationary, while shifting the smaller image around to find the correct place
	 * to complete the stitch.
	 */
	if ((im1->imHeight * im1->imWidth) < (im2->imHeight * im2->imWidth)) {
		smallerIm = im1;
		largerIm = im2;
	} else {
		smallerIm = im2;
		largerIm = im1;
	}
	
	/* 
	 * Loop through every possible overlapping position, finding the one with the least
	 * error, and with enough overlapping pixels for it to be a vaild stitch.
	 */
	for (i = 0; i < smallerIm->imWidth + largerIm->imWidth - 1; i++) {
		for (j = 0; j < smallerIm->imHeight + largerIm->imHeight - 1; j++) {
			struct coord currentPos;
			int overlapping;
			int thisError;
			currentPos.x = i;
			currentPos.y = j;
			
			/* Calculate the error, and figure out how many pixels overlap in this specific position */
			thisError = calculateError(smallerIm, largerIm, currentPos, &overlapping, minLoc);
			
			/* 
			 * If there are enough overlapping pixels, and the error is less than the stored minimum,
			 * update our minimum with the current position and value. If this has the same error as the
			 * current low, we will choose the one with more overlapping pixels.
			 */
			if (overlapping >= numNeededToOverlap) {				
				if (minLoc->value == -1 || (thisError != -1 && thisError < minLoc->value)) {
					minLoc->value = thisError;
					minLoc->loc = currentPos;
					minLoc->numOverlapping = overlapping;
				} else if (thisError != -1 && thisError == minLoc->value && overlapping > minLoc->numOverlapping) {
					minLoc->value = thisError;
					minLoc->loc = currentPos;
					minLoc->numOverlapping = overlapping;
				}
			}
		}
	}
}

/*
 * Calculates the distance between all the pixels that are overlayed in the two images
 * given the x and y coordinates of the upper left corner of the smaller image (param 1).
 * 
 * @param *smallerIm - the smaller of the two images
 * @param *largerIm - the larger of the two images
 * @param topLSmall - the position of the upper left corner of the smaller image in relation
 *				to the larger image
 * @param *overlappingCount - the output parameter that will contain the number of overlapping pixels
 *				this procedure found
 * @param *currentMin - the current minimum position and value
 * @return the distance between all the overlapping pixels given the coordinates of the smaller image
 *				in relation to the larger image
 */
int calculateError(struct image *smallerIm, struct image *largerIm, struct coord topLSmall, int *overlappingCount, struct loc_value *currentMin) {
	unsigned long int total = 0;
	int count = 0;
	struct boundaries smallerImBound;
	struct boundaries largerImBound;
	
	int i, j;
	
	/* Figure out where the two image boundaries are on the overall canvas */
	fillInBoundaries(smallerIm, largerIm, &smallerImBound, &largerImBound, topLSmall);

	/* 
	 * Loop through the smaller image (the one that is being moved around), and figure
	 * out how many pixels are overlapping, and what the error is.
	 */
	for (i = 0; i < smallerIm->imWidth; i++) {
		for (j = 0; j < smallerIm->imHeight; j++) {
			/* The coordinates of the cursor, in relation to the full canvas */
			int actualX = smallerImBound.topL.x + i;
			int actualY = smallerImBound.topL.y + j;
			
			/* Figure out the array indicies for the larger and smaller image data */
			int posInLarger = actualX - largerImBound.topL.x + (actualY - largerImBound.topL.y) * largerIm->imWidth;
			int posInSmaller = j * smallerIm->imWidth + i;
			
			int boundsTest = boundsCheck(actualX, actualY, largerImBound);
			
			/* If this location is within the larger image, we will find it's error, and increment the overlapping count */
			if (boundsTest == WITHIN) {
				total += findDistance(smallerIm->data[posInSmaller], largerIm->data[posInLarger]);
				count++;
				
				/* 
				 * Break out early if this is already over the current minimum error.
				 * This should cut down on processing time.
				 */
				if (total > currentMin->value && currentMin->value != -1) {
					return -1;
				}
			} else if (boundsTest == TO_RIGHT) {
				/* If we are to the right of the larger image, there is no point in continuing our error search */
				i = smallerIm->imWidth;
			} else if (boundsTest == TO_LEFT || boundsTest == BELOW) {
				/* 
				 * If we are to the left of the larger image, there is no point in searching the full height of
				 * the smaller image, so we will just shift to the next column
				 *
				 * If we are below the larger image, there is no point in searching the rest of the height,
				 * so we can move on to the next column.
				 */
				break;
			}
		}
	}
	
	*overlappingCount = count;
	
	return total / count;
}

/*
 * Creates the pixel data for the stitched image using the given 2 images, and the location of minimum
 * error.  It will fill in the necessary information into the *out image struct.
 *
 * @param *im1 - the first image
 * @param *im2 - the second image
 * @param *out - the output image - this is where the data will be stored
 * @param *minLoc - the location of the minimum error
 */
void createNewImageData(struct image *im1, struct image *im2, struct image *out, struct loc_value *minLoc) {
	int newW;
	int newH;
	int count = 0;
	struct boundaries smallerImBound;
	struct boundaries largerImBound;
	struct boundaries outputBound;
	struct image *smallerIm;
	struct image *largerIm;
	struct pixel WHITEPIXEL;
	int i, j;
	
	WHITEPIXEL.red = getWhite(im1->channelSize.red);
	WHITEPIXEL.green = getWhite(im1->channelSize.green);
	WHITEPIXEL.blue = getWhite(im1->channelSize.blue);
	
	/* Figure out the smaller and larger image */
	if ((im1->imHeight * im1->imWidth) < (im2->imHeight * im2->imWidth)) {
		smallerIm = im1;
		largerIm = im2;
	} else {
		smallerIm = im2;
		largerIm = im1;
	}
	
	/* Figure out where the two image boundaries are on the overall canvas */
	fillInBoundaries(smallerIm, largerIm, &smallerImBound, &largerImBound, minLoc->loc);
	
	/* 
	 * Now that we have the appropriate smaller and larger image boundaries,
	 * we need to find the new image's boundaries on the full canvas.
	 * We also need to find the new image width and height.
	 */
	if (smallerImBound.topL.x <= largerImBound.topL.x) {
		outputBound.topL.x = smallerImBound.topL.x;
		outputBound.botL.x = smallerImBound.topL.x;
	} else {
		outputBound.topL.x = largerImBound.topL.x;
		outputBound.botL.x = largerImBound.topL.x;
	}
	
	if (smallerImBound.topR.x >= largerImBound.topR.x) {
		outputBound.topR.x = smallerImBound.topR.x;
		outputBound.botR.x = smallerImBound.topR.x;
	} else {
		outputBound.topR.x = largerImBound.topR.x;
		outputBound.botR.x = largerImBound.topR.x;
	}
	
	if (smallerImBound.topL.y <= largerImBound.topL.y) {
		outputBound.topL.y = smallerImBound.topL.y;		
		outputBound.topR.y = smallerImBound.topR.y;
	} else {
		outputBound.topL.y = largerImBound.topL.y;
		outputBound.topR.y = smallerImBound.topR.y;
	}
	
	if (smallerImBound.botL.y > largerImBound.botL.y) {
		outputBound.botL.y = smallerImBound.botL.y;		
		outputBound.botR.y = smallerImBound.botR.y;
	} else {
		outputBound.botL.y = largerImBound.botL.y;
		outputBound.botR.y = smallerImBound.botR.y;
	}
	
	newW = outputBound.topR.x - outputBound.topL.x + 1;
	newH = outputBound.botL.y - outputBound.topL.y + 1;
	
	/* Allocate the necessary memory for the new image's height and width */
	out->data = (struct pixel *) malloc(newW * newH * sizeof(struct pixel));
	
	if (out->data == NULL) {
		fprintf(stderr, "There was an error allocating the necessary memory to hold this image\n");
		return;
	}
	
	/* Loop through the new image's dimensions and put the correct values in the new image's data */
	for (i = 0; i < newH; i++) {
		for (j = 0; j < newW; j++) {
			/* The coordinates of the cursor, in relation to the full canvas */
			int actualX = outputBound.topL.x + j;
			int actualY = outputBound.topL.y + i;
			
			/* Figure out the array indicies for the larger and smaller image data */
			int posInLarger = actualX - largerImBound.topL.x + (actualY - largerImBound.topL.y) * largerIm->imWidth;
			int posInSmaller = actualX - smallerImBound.topL.x + (actualY - smallerImBound.topL.y) * smallerIm->imWidth;
			
			int boundsCheckLarge = boundsCheck(actualX, actualY, largerImBound);
			int boundsCheckSmall = boundsCheck(actualX, actualY, smallerImBound);
			
			/* 
			 * Do the appropriate action depending on whether the current location is overlapping or not.
			 *	-If they are overlapping, add the average of the pixel value of both images
			 *	-If it is only a part of the larger image, add the larger image's pixel
			 *  -If it is only a part of the smaller image, add the smaller image's pixel
			 *  -If it isn't a part of either image, add a white pixel
			 */
			if (boundsCheckSmall == WITHIN && boundsCheckLarge == WITHIN) {
				out->data[count].red = (smallerIm->data[posInSmaller].red + largerIm->data[posInLarger].red) / 2;
				out->data[count].green = (smallerIm->data[posInSmaller].green + largerIm->data[posInLarger].green) / 2;
				out->data[count].blue = (smallerIm->data[posInSmaller].blue + largerIm->data[posInLarger].blue) / 2;
			} else if (boundsCheckLarge == WITHIN) {
				out->data[count] = largerIm->data[posInLarger];
			} else if (boundsCheckSmall == WITHIN) {
				out->data[count] = smallerIm->data[posInSmaller];
			} else {
				out->data[count] = WHITEPIXEL;
			}
			
			count++;
		}
	}
	
	/* Set the properties for the new image */
	out->isBW = largerIm->isBW;
	out->imWidth = newW;
	out->imHeight = newH;
	out->channelSize = largerIm->channelSize;
}

/*
 * Figures out what 10% of the smaller image is in order to figure out
 * how many pixels need to be overlapping.
 * 
 * @param *im1 - the first image
 * @param *im2 - the second image
 * @return	   10% of the area of the smaller image
 */
int findNeededOverlapPixels(struct image *im1, struct image *im2) {
	if ((im1->imHeight * im1->imWidth) < (im2->imHeight * im2->imWidth)) {
		return .1 * im1->imHeight * im1->imWidth + .5;
	} else {
		return .1 * im2->imHeight * im2->imWidth + .5;
	}
}

/*
 * Finds the 'distance' between two pixels' corresponding R, G, and B channels
 * 
 * @param p1 - the first pixel to compare
 * @param p2 - the second pixel to compare
 * @return	   the distance between the two
 */
unsigned long int findDistance(struct pixel p1, struct pixel p2) {
	short int red = (p1.red - p2.red);
	short int green = (p1.green - p2.green);
	short int blue = (p1.blue - p2.blue);
	
	return (red * red) + (green * green) + (blue * blue);
}

/*
 * Fills in the boundaries of the smaller and larger images in relation to the full canvas
 * 
 * @param *smallerIm - the smaller image
 * @param *largerIm - the larger image
 * @param *smallerBound - the struct to place the boundaries of the smaller image
 * @param *largerBound - the struct to place the boundaries of the larger image
 * @param topL - the coordinates of the top left corner of the smaller image
 */
void fillInBoundaries(struct image *smallerIm, struct image *largerIm, struct boundaries *smallerBound, struct boundaries *largerBound, struct coord topL) {
	smallerBound->topL.x = topL.x;
	smallerBound->topL.y = topL.y;
	smallerBound->topR.x = smallerBound->topL.x + smallerIm->imWidth - 1;
	smallerBound->topR.y = topL.y;
	smallerBound->botL.x = topL.x;
	smallerBound->botL.y = smallerBound->topL.y + smallerIm->imHeight - 1;
	smallerBound->botR.x = smallerBound->topL.x + smallerIm->imWidth - 1;
	smallerBound->botR.y = smallerBound->topR.y + smallerIm->imHeight - 1;
	
	largerBound->topL.x = smallerIm->imWidth - 1;
	largerBound->topL.y = smallerIm->imHeight - 1;
	largerBound->topR.x = smallerIm->imWidth + largerIm->imWidth - 2;
	largerBound->topR.y = smallerIm->imHeight - 1;
	largerBound->botL.x = smallerIm->imWidth - 1;
	largerBound->botL.y = smallerIm->imHeight + largerIm->imHeight - 2;
	largerBound->botR.x = smallerIm->imWidth + largerIm->imWidth - 2;
	largerBound->botR.y = smallerIm->imHeight + largerIm->imHeight - 2;	
}

/*
 * Figures out if the given coordinate is within the given boundaries.
 * 
 * @param x - the x coordinate
 * @param y - the y coordinate
 * @param boundaries - the boundaries to check against
 * @return TO_RIGHT if the coordinate is to the right of the boundaries
 *		   TO_LEFT  if the coordinate is to the left of the boundaries
 *		   BELOW	if the coordinate is below the boundaries
 *		   ABOVE	if the coordinate is above the boundaries
 *		   WITHIN	if the coordinate is within the boundaries
 */
int boundsCheck(int x, int y, struct boundaries boundaries) {
	if (x > boundaries.topR.x) {
		return TO_RIGHT;
	}
	if (x < boundaries.topL.x) {
		return TO_LEFT;
	}
	
	if (y < boundaries.topR.y) {
		return ABOVE;
	}
	if (y > boundaries.botR.y) {
		return BELOW;
	}
	
	return WITHIN;
}
