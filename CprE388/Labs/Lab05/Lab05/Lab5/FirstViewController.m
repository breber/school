//
//  FirstViewController.m
//  Lab5
//
//  Created by Nelson, Ashley M on 9/28/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "FirstViewController.h"

@interface FirstViewController () {
    double lastAcc;
    BOOL strummed;
}

@end

@implementation FirstViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    lastAcc = 1.0;
    
    NSArray *controllers = self.tabBarController.viewControllers;
    self.soundView = controllers[1];
    
    myPicker = [[GKPeerPickerController alloc] init];
    myPicker.delegate = self;
    myPicker.connectionTypesMask = GKPeerPickerConnectionTypeNearby;
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [UIAccelerometer sharedAccelerometer].delegate = self;
}

- (void)viewWillDisappear:(BOOL)animated {
    [UIAccelerometer sharedAccelerometer].delegate = nil;
}

- (void)accelerometer:(UIAccelerometer *)accelerometer didAccelerate:(UIAcceleration *)acceleration {
    
    double currMag = sqrt(acceleration.x*acceleration.x + acceleration.y*acceleration.y +
                          acceleration.z*acceleration.z);

    if (currMag - lastAcc > 0.3) {
        
        NSData *data = [@"Hello World" dataUsingEncoding:NSUTF8StringEncoding];
        [mySession sendDataToAllPeers:data withDataMode:GKSendDataUnreliable error:nil];
    }
    
    lastAcc = currMag;
}

- (IBAction)showPeerPicker {
    [myPicker show];
}

- (void)peerPickerController:(GKPeerPickerController *)picker didConnectPeer:(NSString *)peerID toSession:(GKSession *)session {
    myPeerID = peerID;
    mySession = session;
    mySession.delegate = self;
    
    [mySession setDataReceiveHandler:self.soundView withContext:nil];
    
    [picker dismiss];
}

- (void)session:(GKSession *)session didReceiveConnectionRequestFromPeer:(NSString *)peerID {
    [session acceptConnectionFromPeer:peerID error:nil];
}

- (BOOL)shouldAutorotate {
    return NO;
}

@end
