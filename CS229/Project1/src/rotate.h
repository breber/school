/**
 * @file rotate.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles the rotating of an image
 */

#ifndef ROTATE_H
#define ROTATE_H

#include "imageutil.h"

/**
 * Rotates the image by 90 degrees clockwise. Note, this creates a new array, 
 * frees the old one from the heap, and returns the new pointer.
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void rotate90(struct image *image_ptr);

/**
 * Rotates the image by 90 degrees counter-clockwise. Note, this creates a new array, 
 * frees the old one from the heap, and returns the new pointer.
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void rotateN90(struct image *image_ptr);

/**
 * Rotates the image by 180 degrees. Note, this does all the processing
 * and modifies the given data array.
 *
 * @param  *image_ptr - the image struct containg all the info about the image
 */
void rotate180(struct image *image_ptr);

#endif
