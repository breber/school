//
//  MotionUnlockAppDelegate.h
//  MotionUnlock
//
//  Created by Brian Reber, Todd Lyon, Ashley Nelson on 2/24/10.
//  Copyright 2010. All rights reserved.
//

#import <UIKit/UIKit.h>

@class MotionUnlockViewController;

@interface MotionUnlockAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    MotionUnlockViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet MotionUnlockViewController *viewController;

@end

