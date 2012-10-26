//
//  ViewController.m
//  EggTimer
//
//  Created by Nelson, Ashley M on 10/26/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	notification = [[UILocalNotification alloc] init];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)startTimer:(id)sender {
    notification.fireDate = [NSDate dateWithTimeIntervalSinceNow:[self.userTime.text intValue]];
    notification.alertBody = @"This is a notification";
    notification.alertAction = NSLocalizedString(@"View Details", nil);
    [[UIApplication sharedApplication] scheduleLocalNotification:notification];
    self.timer = [[NSTimer alloc] init];
    self.timer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(timerFireMethod:) userInfo:nil repeats:YES];
    [self.userTime resignFirstResponder];
}

- (void)timerFireMethod:(NSTimer *)theTimer {
    int timeLeft = [self.userTime.text intValue] - 1;
    self.userTime.text = [NSString stringWithFormat:@"%d", timeLeft];
    if (timeLeft == 0) {
        [theTimer invalidate];
    }
}

@end
