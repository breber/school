//
//  ViewController.m
//  Lab1
//
//  Created by Brian Reber on 8/23/12.
//  Copyright (c) 2012 Brian Reber. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()
    
@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return (interfaceOrientation == UIInterfaceOrientationPortrait);
    } else {
        return YES;
    }
}

- (IBAction)userClickedButton:(id)sender {
    NSArray *strings = [NSArray arrayWithObjects:@"Hello World!", @"Goodbye World!", @"CprE388 is Awesome!", nil];
    int index = arc4random() % [strings count];
    myLabel.text = [strings objectAtIndex:index];
}

@end
