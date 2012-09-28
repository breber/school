//
//  FirstViewController.h
//  Lab5
//
//  Created by Nelson, Ashley M on 9/28/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SecondViewController.h"

@interface FirstViewController : UIViewController <UIAccelerometerDelegate, GKPeerPickerControllerDelegate, GKSessionDelegate> {
    
    GKPeerPickerController *myPicker;
    GKSession *mySession;
    NSString *myPeerID;
}

@property SecondViewController *soundView;

- (IBAction)showPeerPicker;

@end
