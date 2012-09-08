
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
    
    // If we are starting over (expecting a new number, set the currentDisplay
    // value to whatever the command value is
    if (self.startOver) {
        self.currentDisplay = [NSString stringWithFormat:@"%@", command];
        self.startOver = NO;
    } else {
        // We are not expecting a new number, so append the given command
        // value to the currently stored value
        self.currentDisplay = [NSString stringWithFormat:@"%@%@", self.currentDisplay, command];
    }
}



/*
 * This function takes the following operators as arguments: @"+", @"-", @"*", @"/", @"=", @"C", @"±".
 * It simulates the affect of pressing an operator key on the calculator.
 */
- (void)issueOperatorCommand:(NSString *)command {
    NSLog(@"issueOperatorCommand: %@", command);
    
    // If we are given 'C', clear the previous display, the current display,
    // and any stored operation. Essentially clear everything.
    if ([@"C" isEqualToString:command]) {
        self.currentDisplay = @"0";
        self.prevDisplay = @"0";
        self.startOver = YES;
    } else if ([@"±" isEqualToString:command]) {
        // Inverse operation - if we are currently displaying a negative
        // value, remove the negative. If the display is positive, make it negative
        if ([self.currentDisplay characterAtIndex:0] == '-') {
            self.currentDisplay = [self.currentDisplay substringFromIndex:1];
        } else {
            self.currentDisplay = [NSString stringWithFormat:@"-%@", self.currentDisplay];
        }
    } else {
        // On any other operation, follow this line
        float result = 0;
        double value1 = [self.prevDisplay doubleValue];
        double value2 = [self.currentDisplay doubleValue];
        
        // If we are currently storing an operation, perform the operation
        // on the values we stored
        if (!self.startOver) {
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
                    
                default:
                    result = value2;
                    break;
            }
        } else {
            result = value2;
        }
        
        // Clear the currently stored operation, and update the display
        self.currentOperation = 0;
        self.currentDisplay = [NSString stringWithFormat:@"%g", result];
        
        // If the current operation is not equals, store the operation
        // and indicate that we are waiting for a new number
        if (![@"=" isEqualToString:command]) {
            self.prevDisplay = self.currentDisplay;
            self.startOver = YES;
            self.currentOperation = [command characterAtIndex:0];
        }
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
