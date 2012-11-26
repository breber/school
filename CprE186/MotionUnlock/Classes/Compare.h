//
//  MotionUnlockViewController.m
//  MotionUnlock
//
//  Created by Brian Reber, Todd Lyon, Ashley Nelson on 4/21/10.
//  Copyright 2010. All rights reserved.
//

int compareCaller(double x_data[], double y_data[], double z_data[], double x_compare[], double y_compare[], double z_compare[]);

int compare(double preSlope[], double postSlope[], int start, int end);

void calculateSlopes(double preValues[], double postValues[], int start, int end);