/**
    @author  Brian Reber

    Load a text based PPM file (P3), perform some processing
    on it, and then write it out again.

    2D storage (as opposed to single contiguous block) (row order)
    * Load image into memory, stored row by row
    * Process the image in row order
    * Write out the image
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
    struct pixel** data;
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
    image_ptr->data = (struct pixel **) malloc(image_ptr->imHeight * sizeof(struct pixel*));

    /* allocate and zero out the first row of image data */
    image_ptr->data[0] = (struct pixel *) malloc(image_ptr->imWidth * sizeof(struct pixel));

    /* read the file line by line, pixel by pixel and populate the image data */
    while ((read = getline(&line, &len, f)) != -1) {
        char *token = strtok(line, " ");
        while(token && (strcmp("\n", token) != 0)) {
            int pixelRow = pixelIndex / image_ptr->imWidth;
            int pixelCol = pixelIndex % image_ptr->imWidth;

            if (colorIndex == 0) {
                image_ptr->data[pixelRow][pixelCol].red = atoi(token);
            } else if (colorIndex == 1) {
                image_ptr->data[pixelRow][pixelCol].green = atoi(token);
            } else {
                image_ptr->data[pixelRow][pixelCol].blue = atoi(token);
            }

            colorIndex = (colorIndex + 1);
            if(colorIndex == 3) {
                pixelIndex = (pixelIndex + 1);
                colorIndex = 0;

                /* allocate and zero out the next row of image data */
                if((pixelCol == image_ptr->imWidth - 1) && (pixelRow != image_ptr->imHeight - 1)) {
                    image_ptr->data[pixelRow + 1] = (struct pixel *) malloc(image_ptr->imWidth * sizeof(struct pixel));
                }
            }

            token = strtok(NULL, " ");
        }
    }
}

void writePPMImage(FILE* f, struct image *image_ptr) {
    int row;

    /* The "Magic" character */
    fprintf(f, "P3");
    fprintf(f, "\n");

    /* Height and Width */
    fprintf(f, "%d %d\n", image_ptr->imWidth, image_ptr->imHeight);

    /* Maxval */
    fprintf(f, "%d\n", image_ptr->maxVal);

    for (row = 0; row < image_ptr->imHeight; ++row) {
        int column;
        struct pixel* rowData = image_ptr->data[row];
        for (column = 0; column < image_ptr->imWidth; ++column) {
            fprintf(f, "%d %d %d\n", rowData[column].red, rowData[column].green, rowData[column].blue);
        }
    }
}

#ifndef MIN
    #define MIN(X, Y) (((X) < (Y)) ? (X) : (Y))
#endif
#define ADJUST(percentage, maxVal, currentVal) MIN(currentVal + ((maxVal - currentVal) * percentage), maxVal);

void lighten(float percentage, struct image* image) {
    int row;

    for (row = 0; row < image->imHeight; ++row) {
        int column;
        struct pixel* rowData = image->data[row];
        for (column = 0; column < image->imWidth; ++column) {
            rowData[column].red = ADJUST(percentage, image->maxVal, rowData[column].red);
            rowData[column].green = ADJUST(percentage, image->maxVal, rowData[column].green);
            rowData[column].blue = ADJUST(percentage, image->maxVal, rowData[column].blue);
        }
    }
}

int main(int argc, char *argv[]) {
    struct image image;
    FILE* inFile = fopen("/home/breber/Downloads/in.ppm", "r");
    FILE* outFile = fopen("/home/breber/Downloads/out.ppm", "w");

    readPPMImage(inFile, &image);
    lighten(25 / 100.0f, &image);
    writePPMImage(outFile, &image);

    return 0;
}
