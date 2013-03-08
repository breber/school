/**
 * @file flip.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the flipping of an image
 */

#ifndef FLIP
#define FLIP

#include "imageutil.h"

/**
 * Flips the image across the x axis (flips vertically).
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void flipV(struct image *image_ptr);

/**
 * Flips the image across the y axis (flips horizontally).
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void flipH(struct image *image_ptr);

#endif
