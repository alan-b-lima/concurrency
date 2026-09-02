#include <stdio.h>
#include <threads.h>

int print(void *word) {
    printf("%s ", (char*)word);
    return 0;
}

int main(void) {
    char *words[] = { "Hello,", "My", "Beautiful", "World!" };
    thrd_t thds[4];

    for (int i = 0; i < 4; i++) {
        thrd_create(&thds[i], print, words[i]);
    }
    for (int i = 0; i < 4; i++) {
        thrd_join(thds[i], NULL);
    }

    printf("\n");
    return 0;
}
