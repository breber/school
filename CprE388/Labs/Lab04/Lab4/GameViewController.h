//
//  GameViewController.h
//  Lab4
//
//  Created by Ashley Nelson on 9/20/12.
//  Copyright (c) 2012 Ashley Nelson. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface GameViewController : UIViewController
{
    int TTTBoard[3][3];
    BOOL turn;
}

@property NSString *playerName;

- (IBAction)button0Pressed:(id)sender;
- (IBAction)button1Pressed:(id)sender;
- (IBAction)button2Pressed:(id)sender;
- (IBAction)button3Pressed:(id)sender;
- (IBAction)button4Pressed:(id)sender;
- (IBAction)button5Pressed:(id)sender;
- (IBAction)button6Pressed:(id)sender;
- (IBAction)button7Pressed:(id)sender;
- (IBAction)button8Pressed:(id)sender;

- (void)handlePress:(int)button withButton:(id)sender;
- (BOOL)checkWin;
- (BOOL)checkFull;
- (BOOL)checkIfSame:(int)first alongWith:(int)second andThis:(int)third;

@end
