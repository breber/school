//
//  ViewController.h
//  CoreLocation
//
//  Created by Nelson, Ashley M on 10/26/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@interface ViewController : UIViewController <CLLocationManagerDelegate> {
    CLLocationManager *manager;
}

@end
