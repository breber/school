//
//  WiiwrapAppDelegate.m
//  Wiiwrap
//
//  Created by Brian Reber on 1/25/10.
//  Copyright 2010 Iowa State University. All rights reserved.
//

//TODO: Get IR Data
//TODO: Print out in order of cmd line args

#import "WiiwrapAppDelegate.h"

@implementation WiiwrapAppDelegate

//@synthesize window;

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification {
	NSArray *args = [[NSProcessInfo alloc] arguments];
	
	int i;
	if ([args count] == 1)
	{
		printf("Invalid argument to Wiiwrap\n");
		exit(0);
	}
	for (i = 1; i < [args count]; i++)
	{
		NSString *choice = [args objectAtIndex:i];
		char chs = [choice characterAtIndex:1];
		if (chs ==  'a' || chs == 'A')
		{
			printOrder[i-1] = 'a';
			accel = YES;
			calibr = YES;
		}
		if (chs ==  'b' || chs == 'B')
		{
			printOrder[i-1] = 'b';
			buttons = YES;
		}
		if (chs ==  't' || chs == 'T')
		{
			printOrder[i-1] = 't';
			time = YES;
		}
	}
	
	[self startSearchingForWiimotes];
}

/*
 
 Included this to override the default action. If not, it would pop up an
 error when passing command line args - it would try and open them as files
 and said that the files don't exist.
 
 */
- (void)application:(NSApplication *)sender openFiles:(NSArray
													   *)filenames
{
	return;
}

/*
 
 Searches for Wiimotes that are in discoverable mode
 
 */
- (void) startSearchingForWiimotes
{
	//NSLog(@"Defining our search inquiry...");
	inquire = [IOBluetoothDeviceInquiry inquiryWithDelegate:self];
	// Search for only Wiimotes...
	[inquire setSearchCriteria:kBluetoothServiceClassMajorAny majorDeviceClass:0x05 minorDeviceClass:0x01];
	[inquire setInquiryLength:20];
	[inquire setUpdateNewDeviceNames:NO];
	
	//NSLog(@"Starting inquiry...");
	IOReturn start = [inquire start];
	
	// If start is successful, we retain the data in the inquiry
	if (start == kIOReturnSuccess)
	{
		[inquire retain];
	}
}

/*
 
 Once it has found a device, we stop searching
 
 */
- (void) deviceInquiryDeviceFound:(IOBluetoothDeviceInquiry *) sender 
						   device:(IOBluetoothDevice *) device
{
	[inquire stop];
}

/*
 
 Once we have stopped searching, we will connect to the devices
 
 */
- (void) deviceInquiryComplete:(IOBluetoothDeviceInquiry*) sender 
						 error:(IOReturn) error
					   aborted:(BOOL) aborted
{		
	//NSLog(@"Starting to connect to the found devices...");

	[self connectToFoundDevices];
}

/*
 
 Open bluetooth connections to the device.
 
 */
- (void) connectToFoundDevices
{
	// We set the Wiimote to the first found device in our inquiry
	wiimote = [[inquire foundDevices] objectAtIndex:0];
	
	delegate = self;
	
	//NSLog(@"Connect to Found Devices");	
	
	[wiimote openL2CAPChannelSync:&controlC withPSM:kBluetoothL2CAPPSMHIDControl delegate:self];
	usleep (20000);
	
	[wiimote openL2CAPChannelSync:&interruptC withPSM:kBluetoothL2CAPPSMHIDInterrupt delegate:self];
	usleep (20000);
	
	notify = [wiimote registerForDisconnectNotification:self selector:@selector(disconnected:fromDevice:)];
	
	usleep (20000);
		
	[self getCalibration];
	
	if (accel && buttons)
		[self reportButtonAndAccel];
	else {
		if (accel) {
			[self reportAccelerometer];
		}
		if (buttons){
			date = [[NSDate alloc] init];
			btnsOnly = YES;
			[self reportButtons];
		}
	}
}


/*
 
 Closes the connection to the Wiimote.  For me, it needs to be called
 twice to get the Wiimote to completely disconnect from the computer.
 
 */
-(void) closeConnection
{	
	NSLog(@"Wiimote disconnected");
	
	[notify unregister];
	notify = nil;
	usleep(200000);
	
	[wiimote closeConnection];
	usleep(200000);
	
	[controlC setDelegate:nil];
	[controlC closeChannel];
	controlC = nil;
	
	[interruptC setDelegate:nil];
	[interruptC closeChannel];
	interruptC = nil;	
	
	usleep(200000);
	
	exit(0);
}

- (void) disconnected:(IOBluetoothUserNotification*) note fromDevice:(IOBluetoothDevice*) device
{
	[self closeConnection];
}


/*
 
 This was a test method to learn about the Wiimote hex values, and
 how to send data to the device.
 
 */
-(void) twoLights
{	
	unsigned char cmd[] = {0x11,0x90};

	IOReturn ret = [self sendCommand:cmd length:2];
	
	if (ret != kIOReturnSuccess)
	{
		NSLog(@"Failed Two Lights");
		return;
	}
}

/*
 
 We want to get the calibration data from the Wiimote, so that
 we can get an accurate scaling of the accelerometer.
 
 */
-(void) getCalibration
{	
	short size = 7;
	
	//printf("Getting Calibration\n");
	
	unsigned char cmd[] = {0x17,0x00,0x00,0x00,0x16,0x00,(size & 0xff)};

	IOReturn ret = [self sendCommand:cmd length:7];
	
	if (ret != kIOReturnSuccess)
	{
		NSLog(@"Failed Two Lights");
		return;
	}
}

/*
 
 We want to get the live button data from the Wiimote.
 Will send us a constant stream of data.
 
 */
-(void) reportButtons
{
	unsigned char cmd[] = {0x12, 0x04, 0x30};
	
	IOReturn ret = [self sendCommand:cmd length:3];
	
	if (ret != kIOReturnSuccess)
	{
		NSLog(@"Failed report buttons");
		return;
	}
}

/*
 
 We want to get the live accelerometer data from the Wiimote.
 Will send us a constant stream of data.
 
 */
-(void) reportAccelerometer
{	
	unsigned char cmd[] = {0x12, 0x00, 0x31};
	
	// In order to get the accelerometer data, we also
	// need to get button data.  We set this bool value
	// to false so we know we only want accel values	
	accelAndButton = NO;
	
	IOReturn ret = [self sendCommand:cmd length:3];
	
	if (ret != kIOReturnSuccess)
	{
		NSLog(@"Failed report accelerometer");
		return;
	}
}

/*
 
 We want to get the live button & accel data from the Wiimote.
 Will send us a constant stream of data.
 
 */
-(void) reportButtonAndAccel
{
	unsigned char cmd[] = {0x12, 0x00, 0x31};
	
	accelAndButton = YES;
	
	IOReturn ret = [self sendCommand:cmd length:3];
	
	if (ret != kIOReturnSuccess)
	{
		NSLog(@"Failed report accelerometer");
		return;
	}
}

/*
 
 We want to stream the amount of milliseconds that have passed since we started.
 
 */
-(void) reportTime
{
	[date retain];
	printf("%d, ", (int)([date timeIntervalSinceNow] * -1000.0));
}

/*
 
 Sort through the hex values - we need to figure out what buttons are
 pressed. The Wiimote fills up some of the extra spaces in the button areas
 with extra values from the acceleration, so we need to figure out what is 
 button data and what is unnecessary.
 
 */
- (void) buttonData
{	
	// Reset the button array
	for (int i = 0; i< 11; i++)
		btnsArray[i] = 0;
	
	//Clear the LSBs From accelerometer data if it exists 
	if (dp[2] & 0x40)	dp[2]-=0x40;
	if (dp[2] & 0x20)	dp[2]-=0x20;	
	if (dp[3] & 0x40)	dp[3]-=0x40;
	if (dp[3] & 0x20)	dp[3]-=0x20;
	
	//Figure out which buttons are pressed
	if (dp[3] & 0x80)	btnsArray[3] = 1;//  Home
	if (dp[3] & 0x10)	btnsArray[4] = 1;//  - 
    if (dp[3] & 0x08)	btnsArray[0] = 1;//  A
	if (dp[3] & 0x04)	btnsArray[1] = 1;//  B
	if (dp[3] & 0x02)	btnsArray[5] = 1;//  1
	if (dp[3] & 0x01)	btnsArray[6] = 1;//  2
	if (dp[2] & 0x10)	btnsArray[2] = 1;//  +
    if (dp[2] & 0x08)	btnsArray[7] = 1;//  DUp
	if (dp[2] & 0x04)	btnsArray[8] = 1;//  DDown
	if (dp[2] & 0x02)	btnsArray[10] = 1;// DRight
	if (dp[2] & 0x01)	btnsArray[9] = 1;//  DLeft
}

/*
 
 Print out the acceleration data
 
 */
- (void) accelerationData:(BOOL) calib
{
	if (calib)
	{
		// Sets the calibration values so we can use them later.
		dp += 7;
		//printf("%.2X, %.2X, %.2X, %.2X, %.2X, %.2X\n", dp[0],dp[1],dp[2],dp[4],dp[5],dp[6]);
		xCal = (unsigned char) dp[0];
		yCal = (unsigned char) dp[1];
		zCal = (unsigned char) dp[2];
		xCalG = (unsigned char) dp[4];
		yCalG = (unsigned char) dp[5];
		zCalG = (unsigned char) dp[6];
		return;
	}

	printf("%.5f, ", ((double)dp[4]-xCal) / ((double)xCalG - xCal));
	printf("%.5f, ", ((double)dp[5]-yCal) / ((double)yCalG - yCal));
	printf("%.5f", ((double)dp[6]-zCal) / ((double)zCalG - zCal));
	//printf("%s", print);
}

/*
 
 Gets called when there is data coming in from the Wiimote.
 We figure out what is coming in, and reroute it to the correct method.
 
 */
- (void) l2capChannelData:(IOBluetoothL2CAPChannel*) l2capChannel data:(void *) dataPointer length:(size_t) dataLength
{		
	dp = (unsigned char *) dataPointer;
	
	if (0) {
	 int i;
	 printf ("channel:%i - ack%3u:", [l2capChannel getPSM], (unsigned int)dataLength);
	 for(i=0 ; i<dataLength ; i++) {
	 printf(" %.2X", dp[i]);
	 }
	 }

	// If the calibration data hasn't been set, we
	// go and get it.
	if (calibr)
	{
		[self accelerationData:YES];
		if (xCal)
		{
			calibr = NO;
			date = [[NSDate alloc] init];
		}
		return;
	}
	if (time) [self reportTime];
	
	//buttons only
	if (dp[1] == 0x30 && btnsOnly) {		
		[self buttonData];
		for (int i = 0; i < 11; i++){
			printf("%d", btnsArray[i]);
			if (i != 10)
				printf(", ");
		}
	} 
	
	//buttons and accelerometer
	if (dp[1] == 0x31 && accelAndButton) { 
		
		[self buttonData];
		
		[self accelerationData:NO];
		printf(", ");
		for (int i = 0; i < 11; i++){
			if (i != 0){
				printf(", ");
			}
			printf("%d", btnsArray[i]);
		}
		//if (btnsArray[0] == 1) [self closeConnection];
	} 
	
	//acceleration only
	if (dp[1] == 0x31 && !accelAndButton) { 
		[self accelerationData:NO];
	}
	
	printf("\n");
}

/*
 
 Sends commands to the Wiimote.  Copied this from DarwiinRemote...
 
 //
 //  WiiRemoteDiscovery.m
 //  DarwiinRemote
 //
 //  Created by Ian Rickard on 12/9/06.
 //  Copyright 2006 __MyCompanyName__. All rights reserved.
 //
 
 */
- (IOReturn) sendCommand:(const unsigned char *) data length:(size_t) length
{		
	unsigned char buf[40];
	memset (buf, 0, 40);
	
	buf[0] = 0x52;
	memcpy (buf+1, data, length);
	if (buf[1] == 0x16) length = 23;
	else				length++;
	
	// For debugging purposes
	if (NO) {
		int i;
		printf ("channel:%i - send%3u:", [controlC getPSM], (unsigned int)length);
		for(i=0 ; i<length ; i++) {
			printf(" %02X", buf[i]);
		}
		printf("\n");
	}
	
	IOReturn ret = kIOReturnSuccess;
	
	int i;
	
	for (i=0; i<10 ; i++) {
		ret = [controlC writeSync:buf length:length];		
		if (ret != kIOReturnSuccess) {
			//NSLog(@"Write Error for command 0x%x:", buf[1], ret);		
			usleep (10000);
		}
		else
			break;
	}
	
	return ret;
}

@end
