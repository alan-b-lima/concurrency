#include <stdio.h>
#include <stdlib.h>

char *global_str = "Text";
int   global_int;

int main(void) {
    int   stack       = 0;
    int  *heap        = malloc(sizeof(int));
    int  *bss         = &global_int;
    char *init        = global_str;
    int (*text)(void) = main;

    printf("%p\n", &stack);
    printf("%p\n", heap);
    printf("%p\n", bss);
    printf("%p\n", init);
    printf("%p\n", text);
}