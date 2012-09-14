//
//  FirstViewController.m
//  Tabathon
//
//  Created by Brian Reber on 9/14/12.
//  Copyright (c) 2012 Brian Reber. All rights reserved.
//

#import "FirstViewController.h"

@interface FirstViewController ()

@end

@implementation FirstViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    UIDevice* device = [UIDevice currentDevice];
    [device setBatteryMonitoringEnabled:YES];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateLabels) name:UIDeviceBatteryLevelDidChangeNotification object:device];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateLabels) name:UIDeviceBatteryStateDidChangeNotification object:device];
    
    [self updateLabels];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

- (void)updateLabels {
    UIDevice* device = [UIDevice currentDevice];
    
    float batteryLevel = [device batteryLevel];
    levelLabel.text = [NSString stringWithFormat:@"%d%%", (int) (batteryLevel * 100)];
    
    if (batteryLevel < .3) {
        [self view].backgroundColor = [UIColor redColor];
    } else if (batteryLevel < .7) {
        [self view].backgroundColor = [UIColor yellowColor];
    } else {
        [self view].backgroundColor = [UIColor greenColor];
    }
    
    UIDeviceBatteryState currentState = [device batteryState];
    
    switch (currentState) {
        case UIDeviceBatteryStateCharging:
            statusLabel.text = @"Charging";
            break;
        case UIDeviceBatteryStateFull:
            statusLabel.text = @"Full";
            break;
        case UIDeviceBatteryStateUnplugged:
            statusLabel.text = @"Unplugged";
            break;
        case UIDeviceBatteryStateUnknown:
        default:
            statusLabel.text = @"Unknown";
            break;
    }
}

@end
