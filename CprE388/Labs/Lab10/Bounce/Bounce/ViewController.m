//
//  ViewController.m
//  Bounce
//
//  Created by Nelson, Ashley M on 11/2/12.
//  Copyright (c) 2012 Nelson, Ashley M. All rights reserved.
//

#import "ViewController.h"

@interface ViewController () {
    double xvel, yvel;
    double xpos, ypos;
}

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    //NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"soccer-ball" ofType:@"jpg"];
    UIImage *myImage = [UIImage imageNamed:@"soccer-ball.jpg"];
    self.ball = [[UIImageView alloc] initWithImage:myImage];
    [self.view addSubview:self.ball];
    [NSTimer scheduledTimerWithTimeInterval:0.04 target:self selector:@selector(nextFrame) userInfo:nil repeats:YES];
    xpos = 0.0;
    ypos = 0.0;
    xvel = 3.0;
    yvel = 3.0;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)nextFrame {
    NSLog(@"nextFrame called");
    CGRect imageBounds = self.ball.bounds;
    double imageWidth = (xvel/xvel)*imageBounds.size.width;
    double imageHeight = (yvel/yvel)*imageBounds.size.height;
    double viewWidth = self.view.bounds.size.width;
    double viewHeight = self.view.bounds.size.height;
    if (xpos + xvel + imageWidth/2 > viewWidth || xpos + xvel + imageWidth/2 < 0) {
        xvel *= -1;
    }
    if (ypos + yvel + imageHeight/2 > viewHeight || ypos + yvel + imageHeight/2 < 0) {
        yvel *= -1;
    }
    xpos += xvel;
    ypos += yvel;
    CGPoint ballLocation = CGPointMake(xpos, ypos);
    self.ball.center = ballLocation;
    [self.view setNeedsDisplay];
}

@end
