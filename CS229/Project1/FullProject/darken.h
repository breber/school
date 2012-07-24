/**
 * @file darken.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the darkening of an image by a given percent
 */

#ifndef DARKEN
#define DARKEN

#include "imageutil.h"

/**
 * Darkens each pixel in data by the given percentage
 *
 *
 * @param  percentage - the percentage to darken the image by
 * @param  numPixels - the total number of pixels in the image
 * @param  *data - the image (pixel) data
 */
void darken(int percentage, int numPixels, struct pixel *data);

#endif
