#import <Foundation/Foundation.h>


@interface Calculator : NSObject

// Public methods
- (id)init;
- (void)issueNumberCommand:(NSString *)command;
- (void)issueOperatorCommand:(NSString *)command;
- (NSString *)getCurrentDisplay;
- (NSString *)about;

@end
