

#include <jni.h>
#include <stdio.h>

#include "HelloWorld.h"

JNIEXPORT void JNICALL Java_HelloWorld_print(JNIEnv * a, jobject b) {
	printf("Hello World!\n");
}
