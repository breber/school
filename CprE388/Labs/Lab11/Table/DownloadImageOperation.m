//
//  DownloadImageOperation.m
//  Table
//
//  Created by Nelson, Ashley M on 11/9/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "DownloadImageOperation.h"

@implementation DownloadImageOperation

- (void)main {
    @autoreleasepool {
        NSData *imageData = [NSData dataWithContentsOfURL:self.url]; 
        NSDictionary *myDictionary = [[NSDictionary alloc] initWithObjectsAndKeys:imageData, @"imageData", self.url, @"selfURL", nil];
        [self.targetID performSelectorOnMainThread:self.actionSelector
                                        withObject:myDictionary waitUntilDone:YES];
    }
}

- (id)initWithURL:(NSURL *)url target:(id)targetID action:(SEL)actionSel {
    self = [super init];
    
    if (self != nil) {
        self.url = url;
        self.targetID = targetID;
        self.actionSelector = actionSel;
    }
    
    return self;
}

@end
