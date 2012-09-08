
#import "Calculator.h"

// Todo - Insert private method signatures and instance variables in this category
@interface Calculator()

/*
 * The operation to be performed on the next number entered
 */
@property(nonatomic) char currentOperation;

/*
 * The NSString currently displayed on the screen
 */
@property(nonatomic) NSString *currentDisplay;

/*
 * The NSString displayed on the screen prior to the current operation
 */
@property(nonatomic) NSString *prevDisplay;

/*
 * Are we to start a new number, or append on to the current number?
 */
@property(nonatomic) BOOL startOver;

- (double) getOperationResult;
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
        float result = [self getOperationResult];
        
        // Update the display, store the current value as the previous value,
        // indicate we are waiting for a new number, and update the currentOperation
        self.currentDisplay = [NSString stringWithFormat:@"%g", result];
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

/*
 * Perform the stored operation and return the result
 */
- (double) getOperationResult {
    double result = 0;
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
        // This is the case where we enter two operations
        // in a row (pressing + and then pressing -). In this
        // case we want to just keep our currently displayed value.
        result = value2;
    }
    
    return result;
}

@end
