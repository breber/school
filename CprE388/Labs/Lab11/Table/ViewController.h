//
//  ViewController.h
//  Table
//
//  Created by Nelson, Ashley M on 10/12/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DownloadImageOperation.h"

@interface ViewController : UITableViewController

@property NSMutableArray *photoNames;
@property NSMutableArray *photoURLs;
@property NSMutableDictionary *imageCache;
@property NSOperationQueue *queue;

@end
