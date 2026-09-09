#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void) {
    const size_t GiB = (1 << 30);

    #define NUM 256

    char *str[NUM];
    for (int i = 0; i < NUM; i++) {
        str[i] = malloc(GiB);
        memset(str[i], '\0', GiB);
    }
    for (int i = 0; i < NUM; i++) {
        printf("%p\n", str[i]);
    }

    scanf("\n");
}