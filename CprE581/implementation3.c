/**
    @author  Brian Reber

    Load a text based PPM file (P3), perform some processing
    on it, and then write it out again.

    Pipeline Process (process and write pixel by pixel instead of reading in whole image first)
    * Read pixel from file
    * Process the pixel
    * Write out the pixel
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct image {
    int imWidth;
    int imHeight;
    int maxVal;
};

#ifndef MIN
    #define MIN(X, Y) (((X) < (Y)) ? (X) : (Y))
#endif
#define ADJUST(percentage, maxVal, currentVal) MIN(currentVal + ((maxVal - currentVal) * percentage), maxVal);

void processPPMImage(FILE* f, FILE* outFile, float percentage) {
    char * line = NULL;
    size_t len = 0;
    ssize_t read;
    struct image im;

    /* The "Magic" string */
    getline(&line, &len, f);
    fprintf(outFile, "%s", line);

    /* Image width and height */
    getline(&line, &len, f);
    fprintf(outFile, "%s", line);
    {
        int widthHeightIndex = 0;
        char *token = strtok(line, " ");
        while(token) {
            if (widthHeightIndex == 0) {
                im.imWidth = atoi(token);
            } else {
                im.imHeight = atoi(token);
            }
            widthHeightIndex = widthHeightIndex + 1;
            token = strtok(NULL, " ");
        }
    }

    /* Max val */
    getline(&line, &len, f);
    fprintf(outFile, "%s", line);
    im.maxVal = atoi(line);

    /* Need to eat the single whitespace character so it doesn't mess up the image data */
    fprintf(outFile, "%c", getc(f));

    /* read the file line by line, pixel by pixel and populate the image data */
    while ((read = getline(&line, &len, f)) != -1) {
        char *token = strtok(line, " ");
        while(token && (strcmp("\n", token) != 0)) {
            int value = ADJUST(percentage, im.maxVal, atoi(token));
            fprintf(outFile, "%d ", value);
            token = strtok(NULL, " ");
        }
        fprintf(outFile, "\n");
    }
}

int main(int argc, char *argv[]) {
    FILE* inFile = fopen("/home/breber/Downloads/in.ppm", "r");
    FILE* outFile = fopen("/home/breber/Downloads/out.ppm", "w");

    processPPMImage(inFile, outFile, 25 / 100.0f);

    return 0;
}
