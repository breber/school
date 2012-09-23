//
//  EnterNameViewController.m
//  Lab4
//
//  Created by Ashley Nelson on 9/20/12.
//  Copyright (c) 2012 Ashley Nelson. All rights reserved.
//

#import "EnterNameViewController.h"

@interface EnterNameViewController ()

@end

@implementation EnterNameViewController

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    return [textField resignFirstResponder];
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    [self performSegueWithIdentifier:@"goToGame" sender:self];
}



@end
