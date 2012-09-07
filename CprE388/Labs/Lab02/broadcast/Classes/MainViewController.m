//
//  MainViewController.m
//  broadcast
//
//  Created by Chad Nelson on 8/28/10.
//

#import "MainViewController.h"
// Import Unix Socket headers
#import <sys/socket.h>
#import <netinet/in.h>
#import <arpa/inet.h>
#import <netdb.h>
#import <unistd.h>


@implementation MainViewController
@synthesize rate;
@synthesize count;
@synthesize slider;
@synthesize onoff;


- (IBAction)toggleBroadcast {
	if (onoff.on) {
		NSLog(@"Start transmitting. Switch state: %d", onoff.on);
		// Schedule packets to be sent at a given interval
		float delay = 1.0 / (slider.value * 100.0);
		broadcastTimer = [NSTimer scheduledTimerWithTimeInterval:delay target:self selector:@selector(broadcast) userInfo:nil repeats:YES];
		// Lock slider
		[slider setEnabled:NO];
		[sliderTimer invalidate];
	} else {
		NSLog(@"Stop transmitting. Switch state: %d", onoff.on);
		// Invalidate timer
		[broadcastTimer invalidate];
		// Unlock slider
		[slider setEnabled:YES];
//		sliderTimer = [NSTimer scheduledTimerWithTimeInterval:0.5 target:self selector:@selector(updateRate) userInfo:nil repeats:YES];
	}
}


- (IBAction)showInfo:(id)sender { 
	NSLog(@"Show About Screen");
	FlipsideViewController *controller = [[FlipsideViewController alloc] initWithNibName:@"FlipsideView" bundle:nil];
	controller.delegate = self;
	
	controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
	[self presentModalViewController:controller animated:YES];
	
	[controller release];
}


- (void)sendPacket {
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	totalPacketsSent++;
	count.text = [NSString stringWithFormat:@"%d", totalPacketsSent];
	
	// Create socket
	int fd;
	if ((fd = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP)) < 0) {
		NSLog(@"Failed to create socket");
	}
	struct sockaddr_in addr_client;
	bzero(&addr_client, sizeof(addr_client));
	addr_client.sin_len = sizeof(addr_client);
	addr_client.sin_family = AF_INET;
	addr_client.sin_port = htons(9176);
	addr_client.sin_addr.s_addr = htonl(INADDR_BROADCAST);
	int opt = 1;
	if (setsockopt(fd, SOL_SOCKET, SO_BROADCAST, &opt, sizeof(int)) == -1) {
		NSLog(@"Error adding broadcast settings");
	}
	
	// Send message
	char *toSend = "Broadcasting: Hello world!";
	if (sendto(fd, toSend, 27, 0, (struct sockaddr *) &addr_client, sizeof(addr_client)) == -1) {
		NSLog(@"Error occured sending message");
	}
	
	close(fd);
	
	[pool drain];
}


- (void)broadcast {
	// Sent a packet on a background tread
	[self performSelectorInBackground:@selector(sendPacket) withObject:nil];
}


- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller {
	[self dismissModalViewControllerAnimated:YES];
}


- (void)viewDidLoad {
	totalPacketsSent = 0;
	[self toggleBroadcast];
}


- (IBAction)updateRate {
	NSLog(@"Polling slider. Slider value: %f", slider.value);
	NSNumberFormatter *formatter = [[NSNumberFormatter alloc] init];
	[formatter setMaximumFractionDigits:0];
	rate.text = [formatter stringFromNumber:[NSNumber numberWithFloat:slider.value*100.0]];
    [formatter release];
}


- (void)dealloc {
    [super dealloc];
	[rate release];
	[count release];
	[onoff release];
	[slider release];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


@end
