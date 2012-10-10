//
//  FlipsideViewController.h
//  BillyDB
//
//  Created by Nelson, Ashley M on 10/5/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>

@class FlipsideViewController;

@protocol FlipsideViewControllerDelegate
- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller;
- (void)addJoke:(NSString *)joke;
@end

@interface FlipsideViewController : UIViewController

@property (weak, nonatomic) id <FlipsideViewControllerDelegate> delegate;
@property IBOutlet UITextField *jokeField;

- (IBAction)done:(id)sender;
- (IBAction)addToDatabase;

@end
