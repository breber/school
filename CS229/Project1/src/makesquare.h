/**
 * @file makesquare.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles writing an image with a square
 * of the specified size in the middle of it.
 */

#ifndef MAKE_SQUARE
#define MAKE_SQUARE

#include "imageutil.h"

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
int readValues(struct image *image_ptr, int *sqWidth, int *sqHeight, int sqColors[]);

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
void createImageData(struct image *image_ptr, int sqWidth, int sqHeight, int sqColors[]);

/**
 * Figures out whether this image is black and white.
 *
 * @param  *bwString - the string the user passed in via command line or stdin
 * @return 0 if this image is color, 1 if this image is color, -1 if the string
 *			doesn't express either.
 */
int isBlackWhite(char *bwString);

#endif
