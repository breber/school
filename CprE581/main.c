#include <stdio.h>

int main() {
    int i = 1;
    int t[10] = { 0 };
    for( i = 1; i < 10; ++i )
        t[i] += t[i - 1];
    return 0;
}