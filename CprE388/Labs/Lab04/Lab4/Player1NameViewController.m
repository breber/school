//
//  Player1NameViewController.m
//  Lab4
//
//  Created by Ashley Nelson on 9/20/12.
//  Copyright (c) 2012 Ashley Nelson. All rights reserved.
//

#import "Player1NameViewController.h"

@interface Player1NameViewController ()

@end

@implementation Player1NameViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [player1Name becomeFirstResponder];
}

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
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:[textField text] forKey:@"player1"];
    
    [self performSegueWithIdentifier:@"player1done" sender:self];
}



@end
