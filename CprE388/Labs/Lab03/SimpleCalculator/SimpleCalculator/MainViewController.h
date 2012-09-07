//
//  MainViewController.h
//  SimpleCalculator
//
//  Created by Nelson, Chad M [E CPE] on 8/29/12.
//  Copyright (c) 2012 Chaddington Software Studios, LLC. All rights reserved.
//

#import "FlipsideViewController.h"
#import "Calculator.h"

@interface MainViewController : UIViewController <FlipsideViewControllerDelegate, UIPopoverControllerDelegate> {
   
}

@property (strong, nonatomic) UIPopoverController *flipsidePopoverController;
@property (strong, nonatomic) Calculator *calculator;

@property (weak, nonatomic) IBOutlet UILabel *display;



@end
