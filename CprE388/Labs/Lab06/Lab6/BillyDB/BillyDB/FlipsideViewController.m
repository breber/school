//
//  FlipsideViewController.m
//  BillyDB
//
//  Created by Nelson, Ashley M on 10/5/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "FlipsideViewController.h"

@interface FlipsideViewController ()



@end

@implementation FlipsideViewController

- (void)awakeFromNib
{
    [super awakeFromNib];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Actions

- (IBAction)done:(id)sender
{
    [self.delegate flipsideViewControllerDidFinish:self];
}

- (IBAction)addToDatabase
{
    [self.delegate addJoke:self.jokeField.text];
}

@end
