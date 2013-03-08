/**
 * @file imageutil.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file contains utility functions for CS229 images
 * including reading image values, and image data.
 */

#include <stdio.h>
#include "imageutil.h"

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
int getChannelSize(unsigned int key) {
	if (key == FOUR_BIT) {
		return 4;
	} else if (key == EIGHT_BIT) {
		return 8;
	} else if (key == SIXTEEN_BIT) {
		return 16;
	}
	
	fprintf(stderr, "Unknown channel size\n");
	return -1;
}

/**
 * Gets the corresponding black constant for the given channel size
 *
 * @param  channelSize - represents the channel size
 * @return 4 bit = 0x0, 8 bit = 0x00, 16 bit = 0x0000, -1 if channel size doesn't
 *		match any of the three options
 */
int getBlack(unsigned int channelSize) {
	switch (channelSize) {
		case FOUR_BIT:
			return BLACK_4;
		case EIGHT_BIT:
			return BLACK_8;
			break;
		case SIXTEEN_BIT:
			return BLACK_16;
			break;
		default:
			return -1;
	}
}

/**
 * Gets the corresponding white constant for the given channel size
 *
 * @param  channelSize - represents the channel size
 * @return 4 bit = 0xF, 8 bit = 0xFF, 16 bit = 0xFFFF, -1 if channel size doesn't
 *		match any of the three options
 */
int getWhite(unsigned int channelSize) {
	switch (channelSize) {
		case FOUR_BIT:
			return WHITE_4;
		case EIGHT_BIT:
			return WHITE_8;
			break;
		case SIXTEEN_BIT:
			return WHITE_16;
			break;
		default:
			return -1;
	}
}

/**
 * Reads the image values (bw, width, height, channel size) from the given CS229 file.
 *
 * @param  *file - the file to read from
 * @param  *image_ptr - the image struct to put the image properties in
 */
int readCSImageValues(FILE *file, struct image *image_ptr) {
	unsigned int is229;
	unsigned int temp;
	
	/* The "Magic" character */
	is229 = fgetc(file);
	if (is229 != CS229) {
		fprintf(stderr, "This isn't a CS229 image\n");
		return -1;
	}
	
	/* Channel Size */
	image_ptr->channelSize.red = fgetc(file);
	image_ptr->channelSize.green = fgetc(file);
	image_ptr->channelSize.blue = fgetc(file);
	
	/* Black and White */
	temp = fgetc(file);
	image_ptr->isBW = (temp == ZERO);
	
	/* 
	 * Getting the height and width back to one number from
	 * the 4 bits involves setting the LSB value to the variable
	 * and then each consecutive (more significant) gets shifted left
	 * 8, and then "OR"ed with what is in the variable currently
	 */
	
	/* Image Width */
	image_ptr->imWidth = fgetc(file);
	temp = fgetc(file);
	image_ptr->imWidth = image_ptr->imWidth | (temp << 8);
	temp = fgetc(file);
	image_ptr->imWidth = image_ptr->imWidth | (temp << 8);
	temp = fgetc(file);
	image_ptr->imWidth = image_ptr->imWidth | (temp << 8);
	
	/* Image Height */
	image_ptr->imHeight = fgetc(file);
	temp = fgetc(file);
	image_ptr->imHeight = image_ptr->imHeight | (temp << 8);
	temp = fgetc(file);
	image_ptr->imHeight = image_ptr->imHeight | (temp << 8);
	temp = fgetc(file);
	image_ptr->imHeight = image_ptr->imHeight | (temp << 8);
	
	return 0;
}

/**
 * Reads the image (pixel) data from the given file
 *
 * @param  *file - the file to read from
 * @param  *image_ptr - the image struct to put the image pixel data into
 */
void readImageData(FILE *file, struct image *image_ptr) {
	unsigned int currentBit[3];
	int currentChannel = 0;
	int pos = 0;
	
	image_ptr->data = (struct pixel *) malloc(image_ptr->imWidth * image_ptr->imHeight * sizeof(struct pixel));
	
	if (image_ptr->data == NULL) {
		fprintf(stderr, "There was an error allocating the necessary memory to hold this image\n");
		return;
	}
	
	if (image_ptr->isBW) {
		/* 
		 * While we haven't finished the file, read in bit by bit
		 * the values of the image. 
		 */
		while ((currentBit[0] = fgetc(file)) != EOF) {
			if (image_ptr->channelSize.red == 1) {
				/* 4 bit - we want to read 2 pixels for each bit */
				image_ptr->data[pos++].red = currentBit[0] & 0xF0;
				image_ptr->data[pos++].red = currentBit[0] & 0x0F; 
			} else if (image_ptr->channelSize.red == 2) {
				/* 8 bit - we want to read 1 pixels for each bit */
				image_ptr->data[pos++].red = currentBit[0];
			} else if (image_ptr->channelSize.red == 4 && currentChannel != 1) {
				/* 
				 * 16 bit - we want to read 1/2 of a pixel for each bit 
				 * here we set the current position to the current read 
				 * left shifted by 4 bits.
				 */
				image_ptr->data[pos].red = currentBit[0] << 8;
				currentChannel = 1;
			} else if (image_ptr->channelSize.red == 4 && currentChannel == 1) {
				/* 
				 * 16 bit (part 2) - we want to read 1/2 of a pixel for each bit 
				 * here we or the current position to the current position, and  
				 * then increment our position
				 */
				image_ptr->data[pos++].red |= currentBit[0];
				currentChannel = 0;
			}
		}
	} else {
		unsigned char *tempData = (unsigned char *) malloc(image_ptr->imWidth * image_ptr->imHeight * sizeof(struct pixel));
		int i = 0;
		unsigned int tempBit = 0;
		int totalBytesRead = 0;
		int offset = 0;
		int readUpper = 0xF0;
		
		/* 
		 * While we haven't finished the file, read in bit by bit
		 * the values of the image. Since this is a color image,
		 * we need to handle 3 bits at a time, so we need to keep 
		 * track of the current 'channel'
		 */
		while ((tempBit = getc(file)) != EOF) {
			tempData[i++] = tempBit;
		}
		
		totalBytesRead = i;
		
		i = 0;
		while ((offset < totalBytesRead) && (i < (image_ptr->imWidth * image_ptr->imHeight))) {
			readPackedBits(tempData, &offset, &readUpper, image_ptr->channelSize.red, &image_ptr->data[i].red);
			readPackedBits(tempData, &offset, &readUpper, image_ptr->channelSize.green, &image_ptr->data[i].green);
			readPackedBits(tempData, &offset, &readUpper, image_ptr->channelSize.blue, &image_ptr->data[i].blue);
			i++;
		}
	}
}

/**
 * Reads bits from the int array given the channel size, and offset
 *
 * @param  *readByte - the array of data
 * @param  *offset - where we are in the data array
 * @param  *readUpper - the mask to use for the current byte to get the necessary bits
 * @param  channelSize - the channel size for the current data
 * @param  *data - the place to put the read data
 */
void readPackedBits(unsigned char *readByte, int *offset, int *readUpper, unsigned char channelSize, unsigned short int *data) {
	if (channelSize == 1) {
		if (*readUpper == 0xF0) {
			*data = (readByte[*offset] & 0xF0) >> 4;
			*readUpper = 0x0F;
		} else if (*readUpper == 0x0F) {
			*data = readByte[*offset] & 0x0F;
			*readUpper = 0xF0;
			(*offset)++;
		}
	} else if (channelSize == 2) {
		if (*readUpper == 0xF0) {
			*data = readByte[*offset];
			(*offset)++;
			*readUpper = 0xF0;
		} else if (*readUpper == 0x0F) {
			*data = readByte[*offset] & 0x0F;
			(*offset)++;
			*data |= ((readByte[*offset] & 0xF0) >> 4);
			*readUpper = 0x0F;
		}
	} else {
		if (*readUpper == 0xF0) {
			*data = (readByte[*offset] << 8);
			(*offset)++;
			*data |= readByte[*offset];
			(*offset)++;
			*readUpper = 0xF0;
		} else if (*readUpper == 0x0F) {
			*data = ((readByte[*offset] & 0x0F) << 12);
			(*offset)++;
			*data |= (readByte[*offset] << 4);
			(*offset)++;
			*data |= ((readByte[*offset] & 0xF0) >> 4);
			*readUpper = 0x0F;
		}
	}
}

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
void packBitsAndWrite(FILE *file, unsigned char *byteToWrite, int *numBitsInByteToWrite, unsigned char channelSize, unsigned short int data) {
	if (channelSize == 1) {
		if (*numBitsInByteToWrite == 0) {
			/* 
			 * The current channel is 4 bits, and there isn't anything in the buffer 
			 * We will add the data to the buffer
			 */
			*byteToWrite = data;
			*numBitsInByteToWrite = 4;
		} else if (*numBitsInByteToWrite == 4) {
			/* 
			 * The current channel is 4 bits, and there are 4 bits in the buffer.
			 * We will left-shift the buffer by 4 bits, and or the data to the buffer,
			 * write the data to the file, and clear the buffer
			 */
			*byteToWrite = (*byteToWrite << 4) | data;
			putc(*byteToWrite, file);
			*numBitsInByteToWrite = 0;
			*byteToWrite = 0;
		}
	} else if (channelSize == 2) {
		if (*numBitsInByteToWrite == 0) {
			/* 
			 * The current channel is 8 bits, and there are 0 bits in the buffer.
			 * We will write the byte size data to the file, and clear the buffer
			 * for good measure
			 */
			putc(data, file);
			*numBitsInByteToWrite = 0;
			*byteToWrite = 0;
		} else if (*numBitsInByteToWrite == 4) {
			/* 
			 * The current channel is 8 bits, and there are 4 bits in the buffer.
			 * We will left-shift the buffer by 4 bits, and or the upper 4 bits of data to the buffer,
			 * write the buffer to the file, and then add the lower 4 bits to the buffer
			 */
			*byteToWrite = (*byteToWrite << 4) | ((data & 0xF0) >> 4);
			putc(*byteToWrite, file);
			*numBitsInByteToWrite = 4;
			*byteToWrite = data & 0x0F;
		}
	} else {
		if (*numBitsInByteToWrite == 0) {
			/* 
			 * The current channel is 16 bits, and there are 0 bits in the buffer.
			 * We will write the 2 bytes to the file, and clear the buffer for good measure
			 */
			putc(data & 0x00FF, file);
			putc((data & 0xFF00) >> 8, file);
			*numBitsInByteToWrite = 0;
			*byteToWrite = 0;
		} else if (*numBitsInByteToWrite == 4) {
			/* 
			 * The current channel is 16 bits, and there are 4 bits in the buffer.
			 * We will left-shift the buffer by 4 bits, and or the upper-most 4 bits of data to the buffer,
			 * write the data to the file.  Then we will write the middle 2 bits to the file,
			 * and finally add the lowest 4 bits to the buffer.
			 */
			*byteToWrite = (*byteToWrite << 4) | ((data & 0xF000) >> 12);
			putc(*byteToWrite, file);
			*byteToWrite = (data & 0x0FF0) >> 4;
			putc(*byteToWrite, file);
			*byteToWrite = (data & 0x000F);
			*numBitsInByteToWrite = 4;
		}
	}
}

/**
 * Writes the image (pixel) data to the given file
 *
 * @param  *file - the file to write to
 * @param  *image_ptr - the image struct containing the data to be written
 */
void writeImageData(FILE *file, struct image *image_ptr) {
	int i;
	unsigned char byteToWrite;
	int numBitsInByteToWrite = 0;
	
	for (i = 0; i < image_ptr->imWidth * image_ptr->imHeight; i++) {
		if (image_ptr->isBW) {
			packBitsAndWrite(file, &byteToWrite, &numBitsInByteToWrite, image_ptr->channelSize.red, image_ptr->data[i].red);
		} else {
			packBitsAndWrite(file, &byteToWrite, &numBitsInByteToWrite, image_ptr->channelSize.red, image_ptr->data[i].red);
			packBitsAndWrite(file, &byteToWrite, &numBitsInByteToWrite, image_ptr->channelSize.green, image_ptr->data[i].green);
			packBitsAndWrite(file, &byteToWrite, &numBitsInByteToWrite, image_ptr->channelSize.blue, image_ptr->data[i].blue);
		}
	}
	
	/*
	 * If we ended with data still in the byteToWrite buffer, output that, and fil in the rest
	 * with zeros
	 */
	if (numBitsInByteToWrite == 4) {
		putc((byteToWrite << 4) & 0xF0, file);
	}
}

/**
 * Writes a CS229 image to the given file
 *
 * @param  *file - the file to write to
 * @param  *image_ptr - the image struct containing the data to be written to CS
 */
void writeCSImage(FILE *file, struct image *image_ptr) {
	/* The "Magic" character */
	putc(CS229, file);
	
	/* Channel Size */
	putc(image_ptr->channelSize.red, file);
	putc(image_ptr->channelSize.green, file);
	putc(image_ptr->channelSize.blue, file);

	/* Black and White */
	putc((image_ptr->isBW) ? 0x00 : 0xFF, file);
	
	/* Image Width */
	putc((image_ptr->imWidth & 0xFF), file);
	putc(((image_ptr->imWidth >> 8) & 0xFF), file);
	putc(((image_ptr->imWidth >> 16) & 0xFF), file);
	putc(((image_ptr->imWidth >> 24) & 0xFF), file);
	
	/* Image Height */
	putc((image_ptr->imHeight & 0xFF), file);
	putc(((image_ptr->imHeight >> 8) & 0xFF), file);
	putc(((image_ptr->imHeight >> 16) & 0xFF), file);
	putc(((image_ptr->imHeight >> 24) & 0xFF), file);
	
	writeImageData(file, image_ptr);
}

/**
 * Writes the PPM image to stdout
 *
 * @param  *image_ptr - the image struct containing the data to be written to ppm
 */
void writePPMImage(struct image *image_ptr) {
	if (image_ptr->isBW) {
		fprintf(stderr, "PPM does not support BW images\n");
		return;
	}
	
	/* The "Magic" character */
	fprintf(stdout, PPM_MAGIC);
	
	fprintf(stdout, "\n");
	
	/* Height and Width */
	fprintf(stdout, "%d %d\n", image_ptr->imWidth, image_ptr->imHeight); 
	
	/* Maxval */
	fprintf(stdout, "%d\n", (image_ptr->channelSize.red == EIGHT_BIT) ? WHITE_8 : WHITE_16);
	
	writeImageData(stdout, image_ptr);
}
