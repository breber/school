/**
 * @file matrix.c
 * @author  Brian Reber
 *
 * @section DESCRIPTION
 *
 * This file multiplies two matricies together
 */
#include <jni.h>
#include <stdio.h>
#include "MatrixMultiplier.h"

JNIEXPORT void JNICALL Java_MatrixMultiplier_multiply(JNIEnv *env, jobject obj, jobjectArray arr1, jobjectArray arr2, jobjectArray out) {
	int i, j, k;
	
	int cpy1[3][3];
	int cpy2[3][3];
	int cpyOUT[3][3];
	
	/* Initialize the output array */
	for (i = 0; i < 3; i++) {
		for (j = 0; j < 3; j++) {
			cpyOUT[i][j] = 0;
		}
	}
	
	/* Get a local copy of the arrays */
	for (i = 0; i < 3; i++) {
		jintArray oneDim = (*env)->GetObjectArrayElement(env, arr1, i);
		jintArray oneDim2 = (*env)->GetObjectArrayElement(env, arr2, i);
		for (j = 0; j < 3; j++) {
			jint *element1 = (*env)->GetIntArrayElements(env, oneDim, 0);
			jint *element2 = (*env)->GetIntArrayElements(env, oneDim2, 0);
				
			cpy1[i][j] = element1[j];
			cpy2[i][j] = element2[j];
		}
	}

	/* Perform the multiplication and update the output array */
	for (i = 0; i < 3; i++) {
		jintArray oneDim = (*env)->NewIntArray(env, 3);
		for (j = 0; j < 3; j++) {
			for (k = 0; k < 3; k++) {
				cpyOUT[i][j] += (cpy1[i][k] * cpy2[k][j]);
			}
			(*env)->SetIntArrayRegion(env, oneDim, 0, 3, (jint *) cpyOUT[i]);
		}
		(*env)->SetObjectArrayElement(env, out, i, oneDim);
	}
	
}
