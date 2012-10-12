//
//  ViewController.m
//  Table
//
//  Created by Nelson, Ashley M on 10/12/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "ViewController.h"

#define SEARCH_STRING @"lolcat"
#define NUM_RESULTS 50

@interface ViewController () 

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	
    // Allocate arrays
    self.photoNames = [[NSMutableArray alloc] init];
    self.photoURLs = [[NSMutableArray alloc] init];
    
    // Form URL for API query to Flickr
    NSString *flickrAPIKey = @"78bd5fd661d75a1703ae1c533a30fae1";
    NSString *apiCall = @"http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=%@&tags=%@&per_page=%i&format=json&nojsoncallback=1";
    NSString *urlString = [NSString stringWithFormat:apiCall, flickrAPIKey, SEARCH_STRING, NUM_RESULTS];
    NSURL *url = [NSURL URLWithString:urlString];
    
    // Get result of Web query
    NSData *data = [NSData dataWithContentsOfURL:url];
    // Convert JSON into objects
    NSJSONReadingOptions options = 0;
    NSDictionary *results = [NSJSONSerialization JSONObjectWithData:data options:options error:nil];
    
    // Combine the information from the query to get the photo names & URLS
    NSArray *photos = [[results objectForKey:@"photos"] objectForKey:@"photo"];
    
    for (NSDictionary *photo in photos) {
        // Get the title for each photo
        NSString *title = [photo objectForKey:@"title"];
        [self.photoNames addObject:(title.length > 0 ? title : @"Untitled")];
        
        // Construct the URL for each photo
        NSString *photoURLString = [NSString stringWithFormat:@"http://farm%@.static.flickr.com/%@/%@_%@_m.jpg", [photo objectForKey:@"farm"], [photo objectForKey:@"server"], [photo objectForKey:@"id"], [photo objectForKey:@"secret"]];
        [self.photoURLs addObject:[NSURL URLWithString:photoURLString]];
    }
}

- (int)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (int)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.photoURLs.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MainCell"];
    
    NSData *imageData = [NSData dataWithContentsOfURL:[self.photoURLs objectAtIndex:indexPath.row]];
    
    cell.imageView.image = [UIImage imageWithData:imageData];
    cell.textLabel.text = [self.photoNames objectAtIndex:indexPath.row];
    
    return cell;
}

@end
