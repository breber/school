//
//  PushPin.h
//  FindMe
//
//  Created by Nelson, Ashley M on 10/19/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Mapkit/MKAnnotation.h>

@interface PushPin : NSObject <MKAnnotation>

@property CLLocationCoordinate2D coordinate;

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate;

@end
