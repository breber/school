//
//  FirstViewController.m
//  Lab05
//
//  Created by Brian Reber on 9/28/12.
//  Copyright (c) 2012 Brian Reber. All rights reserved.
//

#import "FirstViewController.h"

@interface FirstViewController ()

@end

@implementation FirstViewController

- (void)viewWillAppear:(BOOL)animated {
    [UIAccelerometer sharedAccelerometer].delegate = self;
}

- (void)viewWillDisappear:(BOOL)animated {
    [UIAccelerometer sharedAccelerometer].delegate = nil;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)accelerometer:(UIAccelerometer *)accelerometer didAccelerate:(UIAcceleration *)acceleration {
    NSLog(@"Acceleration Event (x:%g, y:%g, z:%g)", acceleration.x, acceleration.y, acceleration.z);
    // TODO – Implement Step 7 Here
}

@end
