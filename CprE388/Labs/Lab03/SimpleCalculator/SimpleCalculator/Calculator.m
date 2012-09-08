
#import "Calculator.h"

// Todo - Insert private method signatures and instance variables in this category
@interface Calculator (Private) {
//    int mCurrentDisplay;
}
// - (void)mynewprivatemethod;
@end



@implementation Calculator

@synthesize currentOperation = _currentOperation;
@synthesize currentDisplay = _currentDisplay;
@synthesize prevDisplay = _prevDisplay;
@synthesize startOver = _startOver;

- (id)init {
    self = [super init];
    
	if (self) {
        self.currentOperation = 0;
        self.currentDisplay = @"0";
        self.prevDisplay = @"0";
        self.startOver = YES;
	}
	return self;
}



/*
 * This function takes the numbers @"0" through @"9" or @"." as arguments and
 * simulates the affect of a number key or the decimal point being
 * pressed on the calculator.
 */
- (void)issueNumberCommand:(NSString *)command {
    NSLog(@"issueNumberCommand: %@", command);
    
    if (self.startOver) {
        self.currentDisplay = [NSString stringWithFormat:@"%@", command];
        self.startOver = NO;
    } else {
        self.currentDisplay = [NSString stringWithFormat:@"%@%@", self.currentDisplay, command];
    }
}



/*
 * This function takes the following operators as arguments: @"+", @"-", @"*", @"/", @"=", @"C", @"±".
 * It simulates the affect of pressing an operator key on the calculator.
 */
- (void)issueOperatorCommand:(NSString *)command {
    NSLog(@"issueOperatorCommand: %@", command);
    
    if ([@"C" isEqualToString:command]) {
        self.currentDisplay = @"0";
        self.prevDisplay = @"0";
        self.startOver = YES;
    } else if ([@"=" isEqualToString:command]) {
        // Equals just updates the display value
        float result = 0;
        float value1 = [self.prevDisplay floatValue];
        float value2 = [self.currentDisplay floatValue];
        
        switch (self.currentOperation) {
            case '+':
                result = value1 + value2;
                break;
                
            case '-':
                result = value1 - value2;
                break;
                
            case '*':
                result = value1 * value2;
                break;
                
            case '/':
                result = value1 / value2;
                break;
        }
        
        self.currentDisplay = [NSString stringWithFormat:@"%f", result]; // TODO: float?
    } else if ([@"±" isEqualToString:command]) {
        // Inverse operation
        if ([self.currentDisplay characterAtIndex:0] == '-') {
            self.currentDisplay = [self.currentDisplay substringFromIndex:1];
        } else {
            self.currentDisplay = [NSString stringWithFormat:@"-%@", self.currentDisplay];
        }
    } else {
        self.prevDisplay = self.currentDisplay;
        self.startOver = YES;
        self.currentOperation = [command characterAtIndex:0];
    }
}



/*
 * Returns the currently displayed number of the calculator.
 */
- (NSString *)getCurrentDisplay {
	return [NSString stringWithFormat:@"%@", self.currentDisplay];
}



/*
 * Returns the student's Name and NetID
 */
- (NSString *)about {
	return @"Brian Reber - breber";
}



// TODO - Add helper methods below
// ...

@end
