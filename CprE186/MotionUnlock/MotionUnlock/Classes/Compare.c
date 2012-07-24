//
//  MotionUnlockViewController.m
//  MotionUnlock
//
//  Created by Brian Reber, Todd Lyon, Ashley Nelson on 4/21/10.
//  Copyright 2010. All rights reserved.
//

#include "Compare.h"
#include "stdlib.h"

#define DATA_NUM 3000
#define LENGTH 100
#define SAMPLE_LENGTH 25
#define TOLERANCE 0.1
#define TRUE 1
#define START 0

int compareCaller(double x_data[], double y_data[], double z_data[], double x_compare[], double y_compare[], double z_compare[]){
	int i =0;
	
//	int LENGTH = 100;
	
	double data_x_slopes[DATA_NUM];
	double data_y_slopes[DATA_NUM];
	double data_z_slopes[DATA_NUM];
	
	double compare_x_slopes[SAMPLE_LENGTH];
	double compare_y_slopes[SAMPLE_LENGTH];
	double compare_z_slopes[SAMPLE_LENGTH];
	
//	while (x_data[i] != 5000 && x_compare[i] != 5000) {
//		i++;
//	}
//	
//	if (i < LENGTH) {
//		LENGTH = i;
//	}
//	
//	for (i = 0; i < DATA_NUM; i++) {
//		data_x_slopes[i] = 5000;
//		data_y_slopes[i] = 5000;
//		data_z_slopes[i] = 5000;
//	}
//	
//	for (i = 0; i < LENGTH; i++) {
//		compare_x_slopes[i] = 5000;
//		compare_y_slopes[i] = 5000;
//		compare_z_slopes[i] = 5000;
//	}
	
	
	//Find slopes in selection window
	calculateSlopes(x_compare, compare_x_slopes, START, START + SAMPLE_LENGTH);
	calculateSlopes(y_compare, compare_y_slopes, START, START + SAMPLE_LENGTH);
	calculateSlopes(z_compare, compare_z_slopes, START, START + SAMPLE_LENGTH);
	
	//Find slopes for data to compare
	calculateSlopes(x_data, data_x_slopes, 0, LENGTH);
	calculateSlopes(y_data, data_y_slopes, 0, LENGTH);
	calculateSlopes(z_data, data_z_slopes, 0, LENGTH);
	
	int x_pass = 0, y_pass = 0, z_pass =0;
	
	//Compare Slopes
	for (i = 0; i < LENGTH - SAMPLE_LENGTH; i++) {
		x_pass = compare(compare_x_slopes, data_x_slopes, i, i + SAMPLE_LENGTH);
		y_pass = compare(compare_y_slopes, data_y_slopes, i, i + SAMPLE_LENGTH);
		z_pass = compare(compare_z_slopes, data_z_slopes, i, i + SAMPLE_LENGTH);
//		printf("Pass? %d, %d, %d \n", x_pass, y_pass, z_pass);
		if (x_pass && y_pass && z_pass) {
			return 1;
		}
	}
	
	return 0;
	
}

void calculateSlopes(double preValues[], double postValues[], int start, int end){
	int i;
	for (i = start; i < end; i++) {
		
		double slope = (preValues[i+1]-preValues[i]);
		
		postValues[i] = slope;
	}
}

int compare(double preSlope[], double postSlope[], int start, int end) {
	
	double difference = 0;
	
	int i = 0, j = 0; 
	
	for(i = start; i < end && j < SAMPLE_LENGTH - 1; i++) {
		if (postSlope[i] == 5000 || preSlope[i] == 5000)
			break;
		double postMinusPre = postSlope[START] - preSlope[i];
		if (postMinusPre < 0)
			postMinusPre *= -1;
		if(postMinusPre <= TOLERANCE) {
			for(j = i + 1; j < SAMPLE_LENGTH - 1 && difference <= TOLERANCE; j++) {
				if (postSlope[j] == 5000 || preSlope[j] == 5000)
					break;
				double postSlopePreSlope = postSlope[j] - preSlope[j];
				if (postSlopePreSlope < 0)
					postSlopePreSlope *= -1;
				difference = postSlopePreSlope;
			}
		}
	}
	
	if(j == SAMPLE_LENGTH - 1) return 1;
	else return 0;
}