//
//  SimpleCalculatorTests.m
//  SimpleCalculatorTests
//
//  Created by Nelson, Chad M [E CPE] on 8/29/12.
//  Copyright (c) 2012 Chaddington Software Studios, LLC. All rights reserved.
//

#import "SimpleCalculatorTests.h"

@implementation SimpleCalculatorTests

- (void)setUp
{
    [super setUp];
    
    calculator = [[Calculator alloc] init];
}

- (void)tearDown
{
    calculator = nil;
    
    [super tearDown];
}


- (void)testSimplePassingTest {
    // Any method who's name starts with "test" is a test!
    // If the test returns without failing, it passes.
}

- (void)testSimpleAddition
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"2"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"2", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"2", nil);
    [calculator issueNumberCommand:@"3"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"3", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", @".  Failed to add two single digit numbers! :( ");
}

- (void)testSimpleSubtraction
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"2"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"2", nil);
    [calculator issueOperatorCommand:@"-"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"2", nil);
    [calculator issueNumberCommand:@"3"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"3", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"-1", @".  Failed to subtract two single digit numbers! :( ");
}

- (void)testSimpleDivision
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"9"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"9", nil);
    [calculator issueOperatorCommand:@"/"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"9", nil);
    [calculator issueNumberCommand:@"3"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"3", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"3", @".  Failed to divide two single digit numbers! :( ");
}

- (void)testSimpleMultiplication
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"2"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"2", nil);
    [calculator issueOperatorCommand:@"*"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"2", nil);
    [calculator issueNumberCommand:@"3"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"3", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"6", @".  Failed to multiply two single digit numbers! :( ");
}

@end
