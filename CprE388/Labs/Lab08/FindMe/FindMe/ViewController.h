//
//  ViewController.h
//  FindMe
//
//  Created by Nelson, Ashley M on 10/19/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "PushPin.h"

@interface ViewController : UIViewController <MKMapViewDelegate, UISearchBarDelegate>

@property IBOutlet MKMapView *mapView;

- (IBAction)addPushPin;

@end
