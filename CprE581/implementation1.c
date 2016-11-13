/**
    @author  Brian Reber

    Load a text based PPM file (P3), perform some processing
    on it, and then write it out again.

    * Load image into contiguous block of memory
    * Process the image in memory order (row order)
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

#ifndef MIN
    #define MIN(X, Y) (((X) < (Y)) ? (X) : (Y))
#endif
#define ADJUST(percentage, maxVal, currentVal) MIN(currentVal + ((maxVal - currentVal) * percentage), maxVal);

void lighten(float percentage, struct image* image) {
    int row;

    for (row = 0; row < image->imHeight; ++row) {    
        int column;
        for (column = 0; column < image->imWidth; ++column) {
            int index = row * image->imWidth + column;

            image->data[index].red = ADJUST(percentage, image->maxVal, image->data[index].red);
            image->data[index].green = ADJUST(percentage, image->maxVal, image->data[index].green);
            image->data[index].blue = ADJUST(percentage, image->maxVal, image->data[index].blue);
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
