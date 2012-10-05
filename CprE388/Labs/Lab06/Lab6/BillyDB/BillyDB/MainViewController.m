//
//  MainViewController.m
//  BillyDB
//
//  Created by Nelson, Ashley M on 10/5/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "MainViewController.h"

@interface MainViewController () {
    sqlite3 *database;
    NSArray *oneLinerJokes;
    NSArray *userJokes;
}

@end

@implementation MainViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    database = NULL;
    NSString *path = [[NSBundle mainBundle] pathForResource:@"jokes" ofType:@"db"];
    const char *filename = [path cStringUsingEncoding:NSUTF8StringEncoding];
    sqlite3_open(filename, &database); // returns SQLITE_OK if the database is opened properly
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    oneLinerJokes = [[NSArray alloc] initWithArray:[self getRows:@"One Liners"]];
    userJokes = [[NSArray alloc] initWithArray:[self getRows:@"User"]];
    
    [self.tableView reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Flipside View

- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue identifier] isEqualToString:@"showAlternate"]) {
        [[segue destinationViewController] setDelegate:self];
    }
}

- (void)addJoke:(NSString *)joke
{
    NSString *query = [NSString stringWithFormat:@"insert into Billy values ('User', '%@')", joke];
    sqlite3_exec(database, [query cStringUsingEncoding:NSUTF8StringEncoding], &callbackInsert, nil, nil);
    sqlite3_exec(database, "Commit", NULL, NULL, NULL);
}

int callbackInsert(void *context, int count, char **values, char **columns)
{
    return SQLITE_OK;
}

int callBackSelect(void *context, int count, char **values, char **columns)
{
    NSMutableArray *rows = (__bridge NSMutableArray *)context;
    for (int i = 0; i < count; i++) {
        const char *nameCString = values[i];
        [rows addObject:[NSString stringWithUTF8String:nameCString]];
    }
    
    return SQLITE_OK;
}

- (NSMutableArray *)getRows:(NSString *)category
{
    NSString *query = [NSString stringWithFormat:@"select joke from Billy where category = '%@'", category];
    NSMutableArray *rows = [[NSMutableArray alloc] init];
    sqlite3_exec(database, [query cStringUsingEncoding:NSUTF8StringEncoding], &callBackSelect, (__bridge void *)(rows), nil);
    return rows;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MainCell"];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MainCell"];
    }
    
    NSString *joke = nil;
    
    switch (indexPath.section) {
        case 0:
            joke = [oneLinerJokes objectAtIndex:indexPath.row];
            break;
            
        default:
            joke = [userJokes objectAtIndex:indexPath.row];
            break;
    }
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@", joke];
    //set other properties of cell
    return cell;
}

- (int)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (int)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    switch (section) {
        case 0:
            return oneLinerJokes.count;
            break;
            
        default: return userJokes.count;
            break;
    }
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    switch (section) {
        case 0:
            return @"One Liner";
            break;
            
        default:
            return @"User";
            break;
    }
}

@end
