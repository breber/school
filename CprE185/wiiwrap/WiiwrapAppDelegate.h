//
//  WiiwrapAppDelegate.h
//  Wiiwrap
//
//  Created by Brian Reber on 1/25/10.
//  Copyright 2010 Iowa State University. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "IOBluetooth/IOBluetooth.h"

@interface WiiwrapAppDelegate : NSObject <NSApplicationDelegate> {
    NSWindow *window;
	
	// The two Bluetooth Channels that we open to connect
	// to our found Wiimotes
	IOBluetoothL2CAPChannel *controlC;
	IOBluetoothL2CAPChannel *interruptC;
	
	// Inquiry - used to search for Wiimotes in Discoverable mode
	IOBluetoothDeviceInquiry *inquire;
	
	// Used to notify us if we lose the connection
	IOBluetoothUserNotification *notify;
	
	// The actual device
	IOBluetoothDevice *wiimote;
	
	// The variable we use to send and receive hex values to & from
	// the Wiimote
	unsigned char * dp;
	
	// Boolean value to tell us whether to print out buttons and
	// acceleration values - the Wiimote sends us button data
	// along with acceleration values 
	BOOL accelAndButton;
	
	// Our calibration data
	unsigned char xCal;
	unsigned char yCal;
	unsigned char zCal;
	unsigned char xCalG;
	unsigned char yCalG;
	unsigned char zCalG;
	
	// Wheter calibration has been taken yet
	BOOL calibr;

	BOOL accel;
	BOOL buttons;
	BOOL time;
	BOOL btnsOnly;
	
	NSDate* date;
	
	// An array to hold the values of the buttons - we use the hex
	// data given out by Wiimote to figure out which buttons are
	// pressed down, and then update the array accordingly
	int btnsArray[11];
	
	// The app delegate - we use this to know which object will be receiving
	// the data from the Wiimote
	id delegate;
	
	char printOrder[3];
	
}

- (void) startSearchingForWiimotes;
- (void) connectToFoundDevices;
- (IOReturn) sendCommand:(const unsigned char *) data length:(size_t) length;
- (void) twoLights;
- (void) closeConnection;

-(void) getCalibration;

// Used to sort out the hex from Wiimote, and print out accordingly
- (void) buttonData;
- (void) accelerationData:(BOOL) calib;

// Which data do we want to receive?
- (void) reportButtons;
- (void) reportAccelerometer;
- (void) reportButtonAndAccel;


//@property (assign) IBOutlet NSWindow *window;

@end
