//
//  ViewController.m
//  FindMe
//
//  Created by Nelson, Ashley M on 10/19/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	self.mapView.showsUserLocation = YES;
    self.mapView.mapType = MKMapTypeHybrid;
    
    MKCoordinateRegion ames;
    ames.center.latitude = 42.02832;
    ames.center.longitude = -93.651;
    ames.span.latitudeDelta = 0.005;
    ames.span.longitudeDelta = 0.005;
    
    [self.mapView setRegion:ames animated:YES];
}

- (IBAction)addPushPin {
    CLLocationCoordinate2D location;
    location.latitude = 42.02832;
    location.longitude = -93.65066;
    PushPin *myPlace = [[PushPin alloc] initWithCoordinate:location];
    [self.mapView addAnnotation:myPlace];
}

- (void)mapView:(MKMapView *)mapView didChangeUserTrackingMode:(MKUserTrackingMode)mode animated:(BOOL)animated {
    
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    if ([annotation class] != [PushPin class]) {
        return nil;
    }
    MKPinAnnotationView *pin = (MKPinAnnotationView *) [mapView dequeueReusableAnnotationViewWithIdentifier:@"i"];
    if (pin == nil) {
        pin = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"i"];
    } else {
        pin.annotation = annotation;
    }
    
    pin.pinColor = MKPinAnnotationColorGreen;
    pin.animatesDrop = YES;
    
    return pin;
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [searchBar resignFirstResponder];
    NSString *query = [NSString stringWithFormat:@"http://maps.google.com/maps/geo?q=%@&output=csv", searchBar.text];
    NSString *queryWithEncoding = [query stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL *queryURL = [NSURL URLWithString:queryWithEncoding];
    NSString *results = [NSString stringWithContentsOfURL:queryURL encoding:NSUTF8StringEncoding error:nil];
    NSArray *resultArray = [results componentsSeparatedByString:@","];
    
    CLLocationCoordinate2D userCoordinate;
    userCoordinate.latitude = [resultArray[2] doubleValue];
    userCoordinate.longitude = [resultArray[3] doubleValue];
    PushPin *newPin = [[PushPin alloc] initWithCoordinate:userCoordinate];
    
    [self.mapView addAnnotation:newPin];
    
    MKCoordinateRegion mapPosition;
    mapPosition.center.latitude = [resultArray[2] doubleValue];
    mapPosition.center.longitude = [resultArray[3] doubleValue];
    mapPosition.span.latitudeDelta = 0.005;
    mapPosition.span.longitudeDelta = 0.005;
    [self.mapView setRegion:mapPosition animated:YES];
}

@end
