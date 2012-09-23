//
//  EndGameViewController.h
//  Lab4
//
//  Created by Nelson, Ashley M on 9/21/12.
//  Copyright (c) 2012 Ashley Nelson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GameViewController.h"

@interface EndGameViewController : UIViewController
{
    IBOutlet UILabel *nameLabel;
}

@property GameViewController *parent;

@end
