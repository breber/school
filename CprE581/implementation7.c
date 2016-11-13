/**
    @author  Brian Reber

    Load a text based PPM file (P3), perform some processing
    on it, and then write it out again.

    Linked list storage (as opposed to single contiguous block)
    * Load image into memory, stored as a linked list of pixels
    * Process the image in list order
    * Write out the image
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct pixel {
    int red;
    int green;
    int blue;

    struct pixel* next;
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
    struct pixel* currentNode;
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

    /* allocate the first pixel of image data */
    image_ptr->data = (struct pixel *) malloc(sizeof(struct pixel));
    image_ptr->data->next = NULL;
    currentNode = image_ptr->data;

    /* read the file line by line, pixel by pixel and populate the image data */
    while ((read = getline(&line, &len, f)) != -1) {
        char *token = strtok(line, " ");
        while(token && (strcmp("\n", token) != 0)) {
            if (colorIndex == 0) {
                currentNode->red = atoi(token);
            } else if (colorIndex == 1) {
                currentNode->green = atoi(token);
            } else {
                currentNode->blue = atoi(token);
            }

            colorIndex = (colorIndex + 1);
            if(colorIndex == 3) {
                if (pixelIndex + 1 != image_ptr->imHeight * image_ptr->imWidth) {
                    currentNode->next = (struct pixel *) malloc(sizeof(struct pixel));
                    currentNode = currentNode->next;
                    currentNode->next = NULL;
                }
                pixelIndex = (pixelIndex + 1);
                colorIndex = 0;
            }

            token = strtok(NULL, " ");
        }
    }
}

void writePPMImage(FILE* f, struct image *image_ptr) {
    struct pixel* currentNode = image_ptr->data;

    /* The "Magic" character */
    fprintf(f, "P3");
    fprintf(f, "\n");

    /* Height and Width */
    fprintf(f, "%d %d\n", image_ptr->imWidth, image_ptr->imHeight);

    /* Maxval */
    fprintf(f, "%d\n", image_ptr->maxVal);

    while (currentNode->next != NULL) {
        fprintf(f, "%d %d %d\n", currentNode->red, currentNode->green, currentNode->blue);
        currentNode = currentNode->next;
    }
}

#ifndef MIN
    #define MIN(X, Y) (((X) < (Y)) ? (X) : (Y))
#endif
#define ADJUST(percentage, maxVal, currentVal) MIN(currentVal + ((maxVal - currentVal) * percentage), maxVal);

void lighten(float percentage, struct image* image) {
    struct pixel* currentNode = image->data;

    while (currentNode->next != NULL) {
        currentNode->red = ADJUST(percentage, image->maxVal, currentNode->red);
        currentNode->green = ADJUST(percentage, image->maxVal, currentNode->green);
        currentNode->blue = ADJUST(percentage, image->maxVal, currentNode->blue);
        currentNode = currentNode->next;
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
