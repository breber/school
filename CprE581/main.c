/**
    @author  Brian Reber

    Load a text based PPM file (P3), perform some processing
    on it, and then write it out again.
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct pixel {
    int red;
    int green;
    int blue;
};

struct image {
    int imWidth;
    int imHeight;
    int maxVal;
    struct pixel* data;
};

void readPPMImage(FILE* f, struct image *image_ptr) {
    char * line = NULL;
    size_t len = 0;
    ssize_t read;
    int pixelIndex = 0;
    int colorIndex = 0;
    
    /* The "Magic" string */
    getline(&line, &len, f);
    
    /* Image width and height */
    getline(&line, &len, f);
    {
        int widthHeightIndex = 0;
        char *token = strtok(line, " ");
        while(token) {
            if (widthHeightIndex == 0) {
                image_ptr->imWidth = atoi(token);
            } else {
                image_ptr->imHeight = atoi(token);
            }
            widthHeightIndex = widthHeightIndex + 1;            
            token = strtok(NULL, " ");
        }
    }

    /* Max val */
    getline(&line, &len, f);
    image_ptr->maxVal = atoi(line);
    
    /* Need to eat the single whitespace character so it doesn't mess up the image data */
    getc(f);

    /* allocate and zero out the image data */
    image_ptr->data = (struct pixel *) malloc(image_ptr->imWidth * image_ptr->imHeight * sizeof(struct pixel));
    memset(image_ptr->data, 0, image_ptr->imWidth * image_ptr->imHeight * sizeof(struct pixel));
    
    /* read the file line by line, pixel by pixel and populate the image data */
    while ((read = getline(&line, &len, f)) != -1) {
        char *token = strtok(line, " ");
        while(token && (strcmp("\n", token) != 0)) {
            if (colorIndex == 0) {
                image_ptr->data[pixelIndex].red = atoi(token);
            } else if (colorIndex == 1) {
                image_ptr->data[pixelIndex].green = atoi(token);
            } else {
                image_ptr->data[pixelIndex].blue = atoi(token);
            }

            colorIndex = (colorIndex + 1);
            if(colorIndex == 3) {
                pixelIndex = (pixelIndex + 1);
                colorIndex = 0;
            }
            
            token = strtok(NULL, " ");
        }
    }
}

/**
 * Writes the PPM image to stdout
 *
 * @param  *image_ptr - the image struct containing the data to be written to ppm
 */
void writePPMImage(FILE* f, struct image *image_ptr) {
    int i = 0;

    /* The "Magic" character */
    fprintf(f, "P3");
    fprintf(f, "\n");
    
    /* Height and Width */
    fprintf(f, "%d %d\n", image_ptr->imWidth, image_ptr->imHeight); 
    
    /* Maxval */
    fprintf(f, "%d\n", image_ptr->maxVal);
    
    for (i = 0; i < image_ptr->imWidth * image_ptr->imHeight; i++) {
        fprintf(f, "%d %d %d\n", image_ptr->data[i].red, image_ptr->data[i].green, image_ptr->data[i].blue);
    }
}

/**
 * Gets the adjusted color value with the given percentage and max val
 *
 * @param  percentage - the percentage to darken the image by
 * @param  maxVal - the maximum value each pixel can hold
 * @param  currentVal - the current color value
 */
unsigned short int getAdjustedValue(double percentage, int maxVal, unsigned short int currentVal) {
    if (currentVal + ((maxVal - currentVal) * percentage) > maxVal) {
        return maxVal;
    } else {
        return currentVal + ((maxVal - currentVal) * percentage);
    }
}

/**
 * Lightens each pixel in data by the given percentage
 *
 * @param  percentage - the percentage to darken the image by
 * @param  numPixels - the total number of pixels in the image
 * @param  maxVal - the maximum value each pixel can hold
 * @param  *data - the image (pixel) data
 */
void lighten(int percentage, int numPixels, int maxVal, struct pixel *data) {
    int i;
    double perc = percentage / 100.0;
    
    if (percentage == 0) {
        return;
    }
    
    for (i = 0; i < numPixels; i++) {
        data[i].red = getAdjustedValue(perc, maxVal, data[i].red);
        data[i].green = getAdjustedValue(perc, maxVal, data[i].green);
        data[i].blue = getAdjustedValue(perc, maxVal, data[i].blue);
    }
}

int main(int argc, char *argv[]) {
    struct image image;
    int percentage = 25;
    FILE* inFile = fopen("/home/breber/Downloads/PANO_20151031_113743_full.ppm", "r");
    FILE* outFile = fopen("/home/breber/Downloads/PANO_20151031_113743_full_out.ppm", "w");

    readPPMImage(inFile, &image);
    lighten(percentage, image.imWidth * image.imHeight, image.maxVal, image.data);
    writePPMImage(outFile, &image);
    
    return 0;
}
