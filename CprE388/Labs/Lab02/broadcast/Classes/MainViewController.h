//
//  MainViewController.h
//  broadcast
//
//  Created by Chad Nelson on 8/28/10.
//

#import "FlipsideViewController.h"

@interface MainViewController : UIViewController <FlipsideViewControllerDelegate> {
	NSTimer *broadcastTimer;
	NSTimer *sliderTimer;
	int totalPacketsSent;
}

@property (retain) IBOutlet UISwitch * onoff;
@property (nonatomic, retain) IBOutlet UILabel * rate;
@property (nonatomic, retain) IBOutlet UILabel * count;
@property (nonatomic, retain) IBOutlet UISlider * slider;

- (IBAction)showInfo:(id)sender;
- (IBAction)toggleBroadcast;
- (IBAction)updateRate;

@end
