#include <stdlib.h>
#include <stdio.h>
#include <threads.h>

#include "mutex.h"

typedef struct { 
    void *lock;
    int  *counter;
    int   inc;
} increment_env;

int increment(void* env) {
    increment_env *ienv = env;

    void *lock    = ienv->lock;
    int  *counter = ienv->counter;
    int   inc     = ienv->inc;

    for (int j = 0; j < inc; j++) {
        mutex_lock(lock);
        *counter = *counter + 1;
        mutex_unlock(lock);
    }

    return 0;
}

int main(int argc, char **argv) {
    int target = 1000000;
    if (argc >= 2) {
        target = atoi(argv[1]);
    }

    #define N 2

    int counter = 0;
    int lock    = 0;

    increment_env env[N];

    int left = target;
    for (int i = 0; i < N; i++) {
        int inc = target / N;
        if (i == N-1) {
            inc = left;
        }
        left -= inc;

        env[i].lock    = &lock;
        env[i].counter = &counter;
        env[i].inc     = inc;
    }

    thrd_t threads[N];
    for (int i = 0; i < N; i++) {
        thrd_create(&threads[i], increment, &env[i]);
    }
    for (int i = 0; i < N; i++) {
        thrd_join(threads[i], NULL);
    }

    printf("Want: %d\n", target);
    printf("Got:  %d\n", counter);
    return 0;
}