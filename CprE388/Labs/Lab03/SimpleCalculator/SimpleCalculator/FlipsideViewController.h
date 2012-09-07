//
//  FlipsideViewController.h
//  SimpleCalculator
//
//  Created by Nelson, Chad M [E CPE] on 8/29/12.
//  Copyright (c) 2012 Chaddington Software Studios, LLC. All rights reserved.
//

#import <UIKit/UIKit.h>

@class FlipsideViewController;

@protocol FlipsideViewControllerDelegate
- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller;
@end

@interface FlipsideViewController : UIViewController

@property (weak, nonatomic) id <FlipsideViewControllerDelegate> delegate;
@property (weak, nonatomic) IBOutlet UILabel *about;

- (IBAction)done:(id)sender;

@end
