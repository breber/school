//
//  ViewController.m
//  iThread
//
//  Created by Nelson, Ashley M on 11/2/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "ViewController.h"

@interface ViewController () {
    AVAudioPlayer *myPlayer;
    NSData *fileData;
}

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	//NSString *myObject = @"http://download.thinkbroadband.com/50MB.zip";
    NSString *myObject = @"http://media.vad1.com/temporary_url_20070929kldcg/tchaikovsky-1812-overture-op49-finale-oslo_philharmonic_orchestra.mp3";
    [self performSelectorInBackground:@selector(myFunc:) withObject:myObject];
}

- (void)myFunc:(NSString *)param {
    @autoreleasepool {
        NSLog(@"%@", param);
        NSURL *fileUrl = [NSURL URLWithString:param];
        fileData = [NSData dataWithContentsOfURL:fileUrl];
        [self performSelectorOnMainThread:@selector(myMainThread:) withObject:fileData waitUntilDone:YES];
    }
}

- (void)myMainThread:(NSData *)myMainObject {
    NSLog(@"Finished downloading");
    myPlayer = [[AVAudioPlayer alloc] initWithData:myMainObject error:nil];
    myPlayer.volume = 1.0;
    [myPlayer play];
}

- (IBAction)buttonPressed {
    NSLog(@"I am responsive!");
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    
}

@end
