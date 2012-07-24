/**
 * @file imageutil.h
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file contains utility functions for CS229 images and PPM images
 * including reading image values, and image data.
 */

#ifndef IMAGEUTIL
#define IMAGEUTIL

#define PPM_MAGIC "P6"
#define ZERO 0x00
#define CS229 0x42
#define BLACK_4 0x0
#define WHITE_4 0xF
#define BLACK_8 0x00
#define WHITE_8 0xFF
#define BLACK_16 0x0000
#define WHITE_16 0xFFFF
#define FOUR_BIT 0x01
#define EIGHT_BIT 0x02
#define SIXTEEN_BIT 0x04

#include <stdlib.h>

/*
 * A struct to hold pixel data. Will work for 4 bit, 8 bit,
 * and 16 bit images.
 */
struct pixel {
	unsigned short int red;
	unsigned short int green;
	unsigned short int blue;
};

/*
 * A struct to hold channel size data
 */
struct channelSize {
	unsigned char red;
	unsigned char green;
	unsigned char blue;
};

/*
 * A struct to hold all the information needed about an image
 */
struct image {
	int isBW;
	unsigned int imWidth;
	unsigned int imHeight;
	struct channelSize channelSize;
	struct pixel *data;
};

/**
 * Gets the channel size in terms of 4, 8 or 16.
 *
 * This essentially is a map, mapping the actual number of bits
 * in the channel to the code we use to write it in the image
 *
 * @param  key - represents the hex value we stored in the image
 * @return 4, 8, 16 depending on the channel size. -1 if for some
 *		reason the param doesn't match any of the values
 */
int getChannelSize(unsigned int key);

/**
 * Gets the corresponding black constant for the given channel size
 *
 * @param  channelSize - represents the channel size
 * @return 4 bit = 0x0, 8 bit = 0x00, 16 bit = 0x0000, -1 if channel size doesn't
 *		match any of the three options
 */
int getBlack(unsigned int channelSize);

/**
 * Gets the corresponding white constant for the given channel size
 *
 * @param  channelSize - represents the channel size
 * @return 4 bit = 0xF, 8 bit = 0xFF, 16 bit = 0xFFFF, -1 if channel size doesn't
 *		match any of the three options
 */
int getWhite(unsigned int channelSize);

/**
 * Reads the image values (bw, width, height, channel size) from the given CS229 file.
 *
 * @param  *file - the file to read from
 * @param  *image_ptr - the image struct to put the image properties in
 */
int readCSImageValues(FILE *file, struct image *image_ptr);

/**
 * Reads the image (pixel) data from the given file
 *
 * @param  *file - the file to read from
 * @param  *image_ptr - the image struct to put the image pixel data into
 */
void readImageData(FILE *file, struct image *image_ptr);

/**
 * Reads bits from the int array given the channel size, and offset
 *
 * @param  *readByte - the array of data
 * @param  *offset - where we are in the data array
 * @param  *readUpper - the mask to use for the current byte to get the necessary bits
 * @param  channelSize - the channel size for the current data
 * @param  *data - the place to put the read data
 */
void readPackedBits(unsigned char *readByte, int *offset, int *readUpper, unsigned char channelSize, unsigned short int *data);

/**
 * Packs bytes correctly given the current byte buffer, and the given data.
 *
 * @param  *file - the file to write to
 * @param  *byteToWrite - the buffer of bits that is to be written - this function will modify
 *				this if when the data is packed it doesn't contain a full byte to write
 * @param  *numBitsInByteToWrite - the number of bits that have been added to the buffer
 * @param  channelSize - the channel size for the current data
 * @param  data - the data that needs to be written
 */
void packBitsAndWrite(FILE *file, unsigned char *byteToWrite, int *numBitsInByteToWrite, unsigned char channelSize, unsigned short int data);

/**
 * Writes the image (pixel) data to the given file
 *
 * @param  *file - the file to write to
 * @param  *image_ptr - the image struct containing the data to be written
 */
void writeImageData(FILE *file, struct image *image_ptr);

/**
 * Writes a CS229 image to the given file
 *
 * @param  *file - the file to write to
 * @param  *image_ptr - the image struct containing the data to be written to CS
 */
void writeCSImage(FILE *file, struct image *image_ptr);

/**
 * Writes the PPM image to stdout
 *
 * @param  *image_ptr - the image struct containing the data to be written to ppm
 */
void writePPMImage(struct image *image_ptr);

#endif
