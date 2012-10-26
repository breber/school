//
//  ViewController.m
//  CoreLocation
//
//  Created by Nelson, Ashley M on 10/26/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	manager = [[CLLocationManager alloc] init];
    manager.delegate = self;
    [manager startUpdatingLocation];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations {
    for (NSString *location in locations) {
        NSLog(@"%@", location);
    }
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    
}

@end
