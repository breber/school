//
//  ViewController.h
//  UserDefault
//
//  Created by Nelson, Ashley M on 10/5/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController

@property IBOutlet UIButton *storeButton;
@property IBOutlet UIButton *restoreButton;
@property IBOutlet UITextField *userInput;

- (IBAction)storePressed;
- (IBAction)restorePressed;

@end
