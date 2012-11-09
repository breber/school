//
//  DownloadImageOperation.h
//  Table
//
//  Created by Nelson, Ashley M on 11/9/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DownloadImageOperation : NSOperation

- (id)initWithURL:(NSURL *)url target:(id)targetID action:(SEL)actionSel;
- (void)main;

@property NSURL *url;
@property id targetID;
@property SEL actionSelector;

@end
