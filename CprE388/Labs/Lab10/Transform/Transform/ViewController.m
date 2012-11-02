//
//  ViewController.m
//  Transform
//
//  Created by Nelson, Ashley M on 11/2/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)startAnimation {
    [UIView beginAnimations:@"example" context:NULL];
    [UIView setAnimationDuration:0.5];
    // Create transform matrix
    CGAffineTransform transform;
    transform = CGAffineTransformRotate(self.cody.transform, 30); //rotate 30 degrees
    transform = CGAffineTransformScale(transform, 0.8, 0.8); //shrink to 80%
    transform = CGAffineTransformTranslate(transform, 0, 10); //move right 10 pixels
    // Change animateable properties
    self.cody.transform = transform; // Done
    [UIView commitAnimations];
}

@end
