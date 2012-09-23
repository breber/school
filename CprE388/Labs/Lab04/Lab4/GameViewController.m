//
//  GameViewController.m
//  Lab4
//
//  Created by Ashley Nelson on 9/20/12.
//  Copyright (c) 2012 Ashley Nelson. All rights reserved.
//

#import "GameViewController.h"
#import "EndGameViewController.h"

@interface GameViewController ()

@end

@implementation GameViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    for (int i = 0; i < 3; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            TTTBoard[i][j] = 2;
        }
    }
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)handlePress:(int)button withButton:(id)sender
{
    UIButton *but = (UIButton *)sender;
    if (turn) {
        [but setTitle:@"X" forState:UIControlStateNormal];
    }
    else {
        [but setTitle:@"O" forState:UIControlStateNormal];
    }
    
    TTTBoard[button/3][button%3] = turn;
    
    [but setEnabled:NO];
    
    if ([self checkWin])
    {
        if (!turn)
        {
            self.playerName = @"Player 1";
            
        }
        else
        {
            self.playerName = @"Player 2";
        }
        
        [self performSegueWithIdentifier:@"endGame" sender:self];
    }
    
    else if ([self checkFull])
    {
        self.playerName = @"Nobody";
        [self performSegueWithIdentifier:@"endGame" sender:self];
    }
    
    else turn = !turn;
}

- (BOOL)checkWin
{
    BOOL backDiag = [self checkIfSame:0 alongWith:4 andThis:8];
    BOOL frontDiag = [self checkIfSame:2 alongWith:4 andThis:6];
    BOOL result;
    
    for (int i = 0; i < 3; i++) {
        result = [self checkIfSame:i alongWith:i+3 andThis:i+6];
        
        int j = i * 3;
        result = result || [self checkIfSame:j alongWith:j+1 andThis:j+2];
        
        if (result) return YES;
    }
    
    return backDiag || frontDiag;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"endGame"]) {
        EndGameViewController *egvc = (EndGameViewController *)[segue destinationViewController];
        egvc.parent = self;
    }
}

- (BOOL)checkFull
{
    for (int i = 0; i < 3; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            if (TTTBoard[i][j] == 2) return NO;
        }
        
    }
    
    return YES;
}

- (BOOL)checkIfSame:(int)first alongWith:(int)second andThis:(int)third
{
    return (TTTBoard[first/3][first%3] == TTTBoard[second/3][second%3]) && (TTTBoard[second/3][second%3] == TTTBoard[third/3][third%3]) && TTTBoard[first/3][first%3] != 2;
}

- (IBAction)button0Pressed:(id)sender
{
    [self handlePress:0 withButton:sender];
}

- (IBAction)button1Pressed:(id)sender
{
    [self handlePress:1 withButton:sender];
}

- (IBAction)button2Pressed:(id)sender
{
    [self handlePress:2 withButton:sender];
}

- (IBAction)button3Pressed:(id)sender
{
    [self handlePress:3 withButton:sender];
}

- (IBAction)button4Pressed:(id)sender
{
    [self handlePress:4 withButton:sender];
}

- (IBAction)button5Pressed:(id)sender
{
    [self handlePress:5 withButton:sender];
}

- (IBAction)button6Pressed:(id)sender
{
    [self handlePress:6 withButton:sender];
}

- (IBAction)button7Pressed:(id)sender
{
    [self handlePress:7 withButton:sender];
}

- (IBAction)button8Pressed:(id)sender
{
    [self handlePress:8 withButton:sender];
}

@end
