//
//  ViewController.m
//  UserDefault
//
//  Created by Nelson, Ashley M on 10/5/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)storePressed
{
    NSUserDefaults *storage = [NSUserDefaults standardUserDefaults];
    [storage setObject:[self.userInput text] forKey:@"Text Field"];
    [storage synchronize];
    self.userInput.text = @"";
}

- (IBAction)restorePressed
{
    NSUserDefaults *storage = [NSUserDefaults standardUserDefaults];
    self.userInput.text = [storage objectForKey:@"Text Field"];
}

@end
