/**
 * @file stitch.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the stitching of 2 CS229 images together
 */

#ifndef STITCH
#define STITCH

#include "imageutil.h"

#define BELOW 2
#define ABOVE -2
#define TO_RIGHT 1
#define TO_LEFT -1
#define WITHIN 0

/*
 * Represents a coordinate on a 2D plane
 */
struct coord {
	int x;
	int y;
};

/*
 * Stores a location, a value, and the number of overlapping
 */
struct loc_value {
	struct coord loc;
	unsigned long value;
	unsigned int numOverlapping;
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
 * Checks if this image is ok to stitch
 * 
 * @param *image - the image to check
 * @return 0 if it is ok, -1 if not
 */
int doErrorChecking(struct image *image);

/*
 * Goes through both images, and finds the location with minimum error.
 * 
 * @param *im1 - the first image
 * @param *im2 - the second image
 * @param *minLoc - the struct to store the location of the minimum error
 */
void findLocWithSmallestError(struct image *im1, struct image *im2, struct loc_value *minLoc);

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
int calculateError(struct image *smallerIm, struct image *largerIm, struct coord topLSmall, int *overlappingCount, struct loc_value *currentMin);

/*
 * Creates the pixel data for the stitched image using the given 2 images, and the location of minimum
 * error.  It will fill in the necessary information into the *out image struct.
 *
 * @param *im1 - the first image
 * @param *im2 - the second image
 * @param *out - the output image - this is where the data will be stored
 * @param *minLoc - the location of the minimum error
 */
void createNewImageData(struct image *im1, struct image *im2, struct image *out, struct loc_value *minLoc);

/*
 * Figures out what 10% of the smaller image is in order to figure out
 * how many pixels need to be overlapping.
 * 
 * @param *im1 - the first image
 * @param *im2 - the second image
 * @return	   10% of the area of the smaller image
 */
int findNeededOverlapPixels(struct image *im1, struct image *im2);

/*
 * Finds the 'distance' between two pixels' corresponding R, G, and B channels
 * 
 * @param p1 - the first pixel to compare
 * @param p2 - the second pixel to compare
 * @return	   the distance between the two
 */
unsigned long int findDistance(struct pixel p1, struct pixel p2);

/*
 * Fills in the boundaries of the smaller and larger images in relation to the full canvas
 * 
 * @param *smallerIm - the smaller image
 * @param *largerIm - the larger image
 * @param *smallerBound - the struct to place the boundaries of the smaller image
 * @param *largerBound - the struct to place the boundaries of the larger image
 * @param topL - the coordinates of the top left corner of the smaller image
 */
void fillInBoundaries(struct image *smallerIm, struct image *largerIm, struct boundaries *smallerBound, struct boundaries *largerBound, struct coord topL);

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
int boundsCheck(int x, int y, struct boundaries boundaries);

#endif
