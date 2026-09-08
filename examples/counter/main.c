#include <stdio.h>
#include <stdlib.h>
#include <threads.h>

typedef struct {
    int  i;
    int *counter;
    int  inc;
} increment_env;

int increment(void* env) {
    increment_env *ienv = env;

    int  i       = ienv->i;
    int *counter = ienv->counter;
    int  inc     = ienv->inc;

    for (int j = 0; j < inc; j++) {
        *counter = *counter + 1;
    }

    return 0;
}

int main(int argc, char **argv) {
    int target = 1000000;
    if (argc >= 2) {
        target = atoi(argv[1]);
    }

    #define NUM 2

    int           counter = 0;
    increment_env env[NUM];

    int left = target;
    for (int i = 0; i < NUM; i++) {
        int inc = target / NUM;
        if (i == NUM-1) {
            inc = left;
        }
        left -= inc;

        env[i].i       = i;
        env[i].counter = &counter;
        env[i].inc     = inc;
    }

    thrd_t thrds[NUM];
    for (int i = 0; i < NUM; i++) {
        thrd_create(&thrds[i], increment, &env[i]);
    }
    for (int i = 0; i < NUM; i++) {
        thrd_join(thrds[i], NULL);
    }

    printf("Want: %d\n", target);
    printf("Got:  %d\n", counter);
    return 0;
}
