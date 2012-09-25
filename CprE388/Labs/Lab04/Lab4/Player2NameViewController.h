//
//  Player2NameViewController.h
//  Lab4
//
//  Created by Brian Reber on 9/23/12.
//  Copyright (c) 2012 Ashley Nelson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Player1NameViewController.h"

@interface Player2NameViewController : Player1NameViewController {
    IBOutlet UITextField *player2Name;
    BOOL canStoreName;
}

@end
