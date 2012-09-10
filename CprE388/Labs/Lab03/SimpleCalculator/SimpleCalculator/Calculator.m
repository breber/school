
#import "Calculator.h"

// Todo - Insert private method signatures and instance variables in this category
@interface Calculator()

/*
 * The operation to be performed on the next number entered
 */
@property(nonatomic) char currentOperation;

/*
 * The NSString for the accumulated value
 */
@property(nonatomic) NSString *value1;

/*
 * The NSString for the current operation
 */
@property(nonatomic) NSString *value2;

/*
 * Represents whether the last button pressed was an operator
 */
@property(nonatomic) BOOL operatorLast;

/*
 * Represents whether the last button pressed was the equal operator
 */
@property(nonatomic) BOOL equalLast;

- (double) getOperationResult;
@end


@implementation Calculator

@synthesize currentOperation = _currentOperation;
@synthesize value1 = _value1;
@synthesize value2 = _value2;
@synthesize operatorLast = _operatorLast;

- (id)init {
    self = [super init];
    
	if (self) {
        self.currentOperation = '+';
        self.value1 = @"0";
        self.value2 = @"0";
        self.operatorLast = YES;
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
    if (self.operatorLast) {
        self.value2 = [NSString stringWithFormat:@"%@", command];
    } else {
        self.value2 = [NSString stringWithFormat:@"%@%@", self.value2, command];
    }
    
    // If the last button pressed was the equal operator, and the user
    // is now entering numbers, we should clear the accumulated value
    if (self.operatorLast && self.equalLast) {
        self.value1 = @"0";
    }
    
    self.operatorLast = NO;
}

/*
 * This function takes the following operators as arguments: @"+", @"-", @"*", @"/", @"=", @"C", @"±".
 * It simulates the affect of pressing an operator key on the calculator.
 */
- (void)issueOperatorCommand:(NSString *)command {
    // If we are given 'C', clear the previous display, the current display,
    // and any stored operation. Essentially clear everything.

    self.operatorLast = YES;

    if ([@"C" isEqualToString:command]) {
        self.value1 = @"0";
        self.value2 = @"0";
    } else if ([@"±" isEqualToString:command]) {
        // Inverse operation - if we are currently displaying a negative
        // value, remove the negative. If the display is positive, make it negative
        self.value2 = [NSString stringWithFormat:@"%g", (0 - [self.value2 doubleValue])];
        self.operatorLast = NO;
    } else {
        // On any other operation, follow this line
        float result = [self getOperationResult];
        
        NSLog(@"Performing: %@ %c %@", self.value1, self.currentOperation, self.value2);
        
        // Update the display, store the current value as the previous value,
        // indicate we are waiting for a new number, and update the currentOperation
        self.value1 = [NSString stringWithFormat:@"%g", result];
        
        if (![@"=" isEqualToString:command]) {
            self.currentOperation = [command characterAtIndex:0];
            self.value2 = @"0";
            self.equalLast = NO;
        } else {
            self.equalLast = YES;
        }
    }
}

/*
 * Returns the currently displayed number of the calculator.
 */
- (NSString *)getCurrentDisplay {
    if (self.operatorLast) {
        return [NSString stringWithFormat:@"%@", self.value1];
    } else {
        return [NSString stringWithFormat:@"%@", self.value2];
    }
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
    double value1 = [self.value1 doubleValue];
    double value2 = [self.value2 doubleValue];
    
    // If we are currently storing an operation, perform the operation
    // on the values we stored
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
    
    return result;
}

@end
