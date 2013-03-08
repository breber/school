/**
 * @file lighten.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the lightening of an image by a given percent
 */

#ifndef LIGHTEN
#define LIGHTEN

#include "imageutil.h"

/**
 * Lightens each pixel in data by the given percentage
 *
 * @param  percentage - the percentage to darken the image by
 * @param  numPixels - the total number of pixels in the image
 * @param  maxVal - the maximum value each pixel can hold
 * @param  *data - the image (pixel) data
 */
void lighten(int percentage, int numPixels, int maxVal, struct pixel *data);

/**
 * Gets the adjusted color value with the given percentage and max val
 *
 * @param  percentage - the percentage to darken the image by
 * @param  maxVal - the maximum value each pixel can hold
 * @param  currentVal - the current color value
 */
unsigned short int getAdjustedValue(double percentage, int maxVal, unsigned short int currentVal);

#endif
