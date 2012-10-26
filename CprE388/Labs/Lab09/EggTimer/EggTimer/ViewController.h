//
//  ViewController.h
//  EggTimer
//
//  Created by Nelson, Ashley M on 10/26/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController {
    UILocalNotification *notification;
}

@property IBOutlet UITextField *userTime;
@property NSTimer *timer;

- (IBAction)startTimer:(id)sender;

@end
