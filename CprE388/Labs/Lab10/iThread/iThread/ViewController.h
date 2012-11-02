//
//  ViewController.h
//  iThread
//
//  Created by Nelson, Ashley M on 11/2/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>

@interface ViewController : UIViewController

- (IBAction)buttonPressed;

- (void)myFunc:(NSString *)param;
- (void)myMainThread:(NSData *)myMainObject;

@end
