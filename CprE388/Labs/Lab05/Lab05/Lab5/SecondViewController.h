//
//  SecondViewController.h
//  Lab5
//
//  Created by Nelson, Ashley M on 9/28/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SecondViewController : UIViewController <GKPeerPickerControllerDelegate, GKSessionDelegate> {
    IBOutlet UIButton *button1;
    IBOutlet UIButton *button2;
    IBOutlet UIButton *button3;
    IBOutlet UIButton *button4;
}

- (void)receiveData:(NSData *)data fromPeer:(NSString *)peer inSession:(GKSession *)session context:(void *)context;

@end
