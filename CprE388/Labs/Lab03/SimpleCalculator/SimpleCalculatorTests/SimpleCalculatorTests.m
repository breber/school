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

// ADDED TESTS

- (void)testChainedAddition
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"2"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"2", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"2", nil);
    [calculator issueNumberCommand:@"3"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"3", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", @". Pressing + should perform previous addition");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"10", @".  Failed to perform chained addition (2 + 3 + 5)");
}

- (void)testMultiDigitAddition
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueNumberCommand:@"10"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"10", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"15", @".  Failed to perform addition with values over 10");
}

- (void)testNegationAddition
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"±"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"-5", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"-5", nil);
    [calculator issueNumberCommand:@"10"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"10", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", @".  Failed to perform addition with a negative value");
}

- (void)testNegationSubtraction
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"-"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueNumberCommand:@"10"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"10", nil);
    [calculator issueOperatorCommand:@"±"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"-10", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"15", @".  Failed to perform subtraction with a negative value (5 - (-10))");
}

- (void)testSubtractionWithAccidentalAddition
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"-"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueNumberCommand:@"10"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"10", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"-5", @".  Failed to perform subtraction with a negative value (5 - 10)");
}

- (void)testAdditionWithDecimals
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueNumberCommand:@"."];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5.", nil);
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5.5", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5.5", nil);
    [calculator issueNumberCommand:@"10"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"10", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"15.5", @".  Failed to perform addition with decimals (5.5 + 10)");
}

- (void)testEqualsFollowedByNumber
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueNumberCommand:@"6"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"6", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"11", @".  Failed to perform addition (5 + 6)");
    
    // Start new operation without clearing the calculator
    [calculator issueNumberCommand:@"7"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"7", nil);
    [calculator issueOperatorCommand:@"*"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"7", nil);
    [calculator issueNumberCommand:@"3"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"3", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"21", @".  Failed to perform multiplication without clearing calculator");
}

- (void)testEqualsFollowedByOperator
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueNumberCommand:@"6"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"6", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"11", @".  Failed to perform addition (5 + 6)");
    
    // Start new operation without clearing the calculator
    [calculator issueOperatorCommand:@"*"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"11", nil);
    [calculator issueNumberCommand:@"7"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"7", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"77", @".  Failed to perform multiplication without clearing calculator");
}

- (void)testAccumulation
{
    [calculator issueOperatorCommand:@"C"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"0", @".  Clearing the calculator should set the display to '0'.");
    [calculator issueNumberCommand:@"5"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueOperatorCommand:@"+"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"5", nil);
    [calculator issueNumberCommand:@"10"];
    STAssertEqualObjects([calculator getCurrentDisplay], @"10", nil);
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"15", @".  Failed to perform simple addition");
    [calculator issueOperatorCommand:@"="];
    STAssertEqualObjects([calculator getCurrentDisplay], @"25", @".  Failed to accumulate on multiple equals");
}

@end
