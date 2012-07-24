/**
 * @file ppm2cs.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file takes a PPM image file and converts it into a CS229 file.
 */

#ifndef PPM2CS
#define PPM2CS

#include "imageutil.h"

/**
 * Reads the image properties from stdin.
 *
 * @param  *imWidth - the image width
 * @param  *imHeight - the image height
 * @param  *maxVal - the maximum color value possible in this image
 */
void readPPMImageValues(unsigned int *imWidth, unsigned int *imHeight, int *maxVal);

/**
 * Scales the image pixels to the correct max value.
 *
 * @param  numPixels - the number of pixels in the image
 * @param  oldMaxVal - the max val read in from the image
 * @param  newMaxVal - the maximum color value to scale to (should be 0xFF or 0xFFFF)
 * @param  *data - the image (pixel) data
 */
void scaleImageValues(int numPixels, int oldMaxVal, int newMaxVal, struct pixel *data);

#endif
