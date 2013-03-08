/**
 * @file convolvejni.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file handles processing of a convolution on a CS229 image from the Java UI
 */

#include <stdio.h>
#include <stdlib.h>
#include "convolve.h"
#include "convolvejni.h"

JNIEXPORT void JNICALL Java_CS229Image_convoluteFromJava(JNIEnv *env, jobject obj, jint kernelDim, jdoubleArray kernelData) {
	struct kernel kernel;
	struct image image;
	int i, j;
	int loc = 0;
	
	jclass imgClass = (*env)->GetObjectClass(env, obj);
	jfieldID isBWFid = (*env)->GetFieldID(env, imgClass, "isBW", "Z");
	jfieldID widthFid = (*env)->GetFieldID(env, imgClass, "width", "I");
	jfieldID heightFid = (*env)->GetFieldID(env, imgClass, "height", "I");
	jfieldID chanRFid = (*env)->GetFieldID(env, imgClass, "channelSizeR", "I");
	jfieldID chanGFid = (*env)->GetFieldID(env, imgClass, "channelSizeG", "I");
	jfieldID chanBFid = (*env)->GetFieldID(env, imgClass, "channelSizeB", "I");
	jfieldID dataFid = (*env)->GetFieldID(env, imgClass, "data", "[LCS229Image$Pixel;");
	jboolean isBWBool = (*env)->GetBooleanField(env, obj, isBWFid);
	jobjectArray pixelArrObj = (jobjectArray) (*env)->GetObjectField(env, obj, dataFid);
	
	/* Set up kernel struct */
	kernel.size = kernelDim;
	kernel.kernelValues = (*env)->GetDoubleArrayElements(env, kernelData, 0);
	
	/* Set up image struct */
	image.isBW = (isBWBool == JNI_TRUE) ? 0xFF : 0x00;
	image.imWidth = (*env)->GetIntField(env, obj, widthFid);
	image.imHeight = (*env)->GetIntField(env, obj, heightFid);
	image.channelSize.red = (*env)->GetIntField(env, obj, chanRFid);
	image.channelSize.green = (*env)->GetIntField(env, obj, chanGFid);
	image.channelSize.blue = (*env)->GetIntField(env, obj, chanBFid);
	image.data = (struct pixel *) malloc(image.imWidth * image.imHeight * sizeof(struct pixel));
	
	if (image.data == NULL) {
		fprintf(stderr, "There was an error allocating the necessary memory to hold this image\n");
		return;
	}
	
	for (i = 0; i < image.imHeight; i++) {
		for (j = 0; j < image.imWidth; j++, loc++) {
			jobject currentPixel = (*env)->GetObjectArrayElement(env, pixelArrObj, loc);
			jclass pxlClass = (*env)->GetObjectClass(env, currentPixel);
			
			image.data[loc].red = (*env)->GetIntField(env, currentPixel, (*env)->GetFieldID(env, pxlClass, "red", "I"));
			image.data[loc].green = (*env)->GetIntField(env, currentPixel, (*env)->GetFieldID(env, pxlClass, "green", "I"));
			image.data[loc].blue = (*env)->GetIntField(env, currentPixel, (*env)->GetFieldID(env, pxlClass, "blue", "I"));
		}
	}
	
	/* Perform Convolution */
	performConvolution(&image, &kernel, &kernel, &kernel);
	
	/* Update the imageObj */
	for (loc = 0, i = 0; i < image.imHeight; i++) {
		for (j = 0; j < image.imWidth; j++, loc++) {
			jobject currentPixel = (*env)->GetObjectArrayElement(env, pixelArrObj, loc);
			jclass pxlClass = (*env)->GetObjectClass(env, currentPixel);
			
			(*env)->SetIntField(env, currentPixel, (*env)->GetFieldID(env, pxlClass, "red", "I"), image.data[loc].red);
			(*env)->SetIntField(env, currentPixel, (*env)->GetFieldID(env, pxlClass, "green", "I"), image.data[loc].green);
			(*env)->SetIntField(env, currentPixel, (*env)->GetFieldID(env, pxlClass, "blue", "I"), image.data[loc].blue);
		}
	}
	
}
