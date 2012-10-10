//
//  MainViewController.h
//  BillyDB
//
//  Created by Nelson, Ashley M on 10/5/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "FlipsideViewController.h"

#import <CoreData/CoreData.h>

@interface MainViewController : UIViewController <FlipsideViewControllerDelegate, UITableViewDataSource, UITableViewDelegate>

@property (strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property IBOutlet UITableView *tableView;

- (void)addJoke:(NSString *)joke;

@end
