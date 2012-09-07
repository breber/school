//
//  broadcastAppDelegate.h
//  broadcast
//
//  Created by Chad Nelson on 8/28/10.
//

#import <UIKit/UIKit.h>

@class MainViewController;

@interface broadcastAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    MainViewController *mainViewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet MainViewController *mainViewController;

@end

