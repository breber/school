
#import "Calculator.h"

// Todo - Insert private method signatures and instance variables in this category
@interface Calculator (Private) {
    // int mynewprivateinstancevariable;
}
// - (void)mynewprivatemethod;
@end



@implementation Calculator

- (id)init {
    self = [super init];
    
	if (self) {
		// TODO - Implement Initialization code
		// ...
		
	}
	return self;
}



/*
 * This function takes the numbers @"0" through @"9" or @"." as arguments and
 * simulates the affect of a number key or the decimal point being
 * pressed on the calculator.
 */
- (void)issueNumberCommand:(NSString *)command {
	// TODO - Implement
	// ...
}



/*
 * This function takes the following operators as arguments: @"+", @"-", @"*", @"/", @"=", @"C", @"±".
 * It simulates the affect of pressing an operator key on the calculator.
 */
- (void)issueOperatorCommand:(NSString *)command {
	// TODO - Implement
	// ...
}



/*
 * Returns the currently displayed number of the calculator.
 */
- (NSString *)getCurrentDisplay {
	// TODO - Implement a function that displays the current state of the calculator
	return @"Not implemented";
}



/*
 * Returns the student's Name and NetID
 */
- (NSString *)about {
	// TODO - Enter your name and netID below
	return @"Brian Reber - breber";
}



// TODO - Add helper methods below
// ...

@end
