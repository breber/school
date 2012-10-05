//
//  ViewController.h
//  Billy
//
//  Created by Nelson, Ashley M on 10/5/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController

@property IBOutlet UILabel *jokeField;
@property NSArray *jokeArray;

- (IBAction)jokeButtonPressed;

@end
