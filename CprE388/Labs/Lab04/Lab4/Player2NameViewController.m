//
//  Player2NameViewController.m
//  Lab4
//
//  Created by Brian Reber on 9/23/12.
//  Copyright (c) 2012 Ashley Nelson. All rights reserved.
//

#import "Player2NameViewController.h"

@implementation Player2NameViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [player2Name becomeFirstResponder];
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:[textField text] forKey:@"player2"];
    
    [self performSegueWithIdentifier:@"player2done" sender:self];
}

@end
