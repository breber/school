//
//  MainViewController.m
//  SimpleCalculator
//
//  Created by Nelson, Chad M [E CPE] on 8/29/12.
//  Copyright (c) 2012 Chaddington Software Studios, LLC. All rights reserved.
//

#import "MainViewController.h"

@implementation MainViewController

@synthesize calculator;
@synthesize display;


- (void)viewDidLoad
{
    [super viewDidLoad];
	self.calculator = [[Calculator alloc] init];
	self.display.text = [calculator getCurrentDisplay];
}


- (IBAction)issueNumberCommand:(id)sender {
	UILabel *button = [(UIButton *) sender titleLabel];
	[calculator issueNumberCommand:button.text];
	self.display.text = [calculator getCurrentDisplay];
}


- (IBAction)issueOperatorCommand:(id)sender {
	UILabel *button = [(UIButton *) sender titleLabel];
	[calculator issueOperatorCommand:button.text];
	self.display.text = [calculator getCurrentDisplay];
}




- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
    } else {
        return YES;
    }
}

#pragma mark - Flipside View Controller

- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller
{
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        [self dismissModalViewControllerAnimated:YES];
    } else {
        [self.flipsidePopoverController dismissPopoverAnimated:YES];
        self.flipsidePopoverController = nil;
    }
}

- (void)popoverControllerDidDismissPopover:(UIPopoverController *)popoverController
{
    self.flipsidePopoverController = nil;
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"showAlternate"]) {
        [[segue destinationViewController] setDelegate:self];
        
        if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad) {
            UIPopoverController *popoverController = [(UIStoryboardPopoverSegue *)segue popoverController];
            self.flipsidePopoverController = popoverController;
            popoverController.delegate = self;
        }
    }
}


- (IBAction)togglePopover:(id)sender
{
    if (self.flipsidePopoverController) {
        [self.flipsidePopoverController dismissPopoverAnimated:YES];
        self.flipsidePopoverController = nil;
    } else {
        [self performSegueWithIdentifier:@"showAlternate" sender:sender];
    }
}

- (void)viewDidUnload {
    [self setDisplay:nil];
    [super viewDidUnload];
}
@end
