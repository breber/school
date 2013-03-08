/**
 * @file imagestats.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file reads a CS229 image file and displays info about it.
 */

#ifndef IMAGE_STATS
#define IMAGE_STATS

#include "imageutil.h"

/**
 * Reads the image data, and counts the number of black pixels
 * and the number of white pixels
 *
 * @param  *image_ptr - the image struct containg all the image information
 * @param  *white - the number of white pixels in the image (output param)
 * @param  *black - the number of black pixels in the image (output param)
 */
void processData(struct image *image_ptr, int *white, int *black);

#endif
