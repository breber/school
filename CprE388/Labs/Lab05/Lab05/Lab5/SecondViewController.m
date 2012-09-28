//
//  SecondViewController.m
//  Lab5
//
//  Created by Nelson, Ashley M on 9/28/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "SecondViewController.h"

@interface SecondViewController () {
    AVAudioPlayer *newPlayer;
}

@end

@implementation SecondViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)receiveData:(NSData *)data fromPeer:(NSString *)peer inSession:(GKSession *)session context:(void *)context {
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"mario_02" ofType:@"wav"];
    
    if (button1.highlighted) {
        path = [[NSBundle mainBundle] pathForResource:@"mario_03" ofType:@"wav"];
    } else if (button2.highlighted) {
        path = [[NSBundle mainBundle] pathForResource:@"mario_04" ofType:@"wav"];
    } else if (button3.highlighted) {
        path = [[NSBundle mainBundle] pathForResource:@"mario_07" ofType:@"wav"];
    } else if (button4.highlighted) {
        path = [[NSBundle mainBundle] pathForResource:@"mario_14" ofType:@"wav"];
    }
    
    NSURL *fileURL = [NSURL fileURLWithPath:path];
    
    newPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:fileURL error:nil];
    [newPlayer play];
}

@end
