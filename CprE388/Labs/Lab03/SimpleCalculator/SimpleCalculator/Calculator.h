#import <Foundation/Foundation.h>


@interface Calculator : NSObject

@property(nonatomic) char currentOperation;
@property(nonatomic) NSString *currentDisplay;
@property(nonatomic) NSString *prevDisplay;
@property(nonatomic) BOOL startOver;

// Public methods
- (id)init;
- (void)issueNumberCommand:(NSString *)command;
- (void)issueOperatorCommand:(NSString *)command;
- (NSString *)getCurrentDisplay;
- (NSString *)about;

@end
