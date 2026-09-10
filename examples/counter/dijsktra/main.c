#include <stdlib.h>
#include <stdio.h>
#include <threads.h>

#include "solution.h"

typedef struct { 
    Solution *lock;
    int       i;
    int      *counter;
    int       inc;
} increment_env;

int increment(void* env) {
    increment_env *ienv = env;

    Solution *lock    = ienv->lock;
    int       i       = ienv->i;
    int      *counter = ienv->counter;
    int       inc     = ienv->inc;

    for (int j = 0; j < inc; j++) {
        solution_lock(lock, i);
        *counter = *counter + 1;
        solution_unlock(lock, i);
    }

    return 0;
}

int main(int argc, char **argv) {
    int target = 1000000;
    if (argc >= 2) {
        target = atoi(argv[1]);
    }

    #define N 2

    int       counter = 0;
    Solution *lock    = solution_new(N);

    increment_env env[N];

    int left = target;
    for (int i = 0; i < N; i++) {
        int inc = target / N;
        if (i == N-1) {
            inc = left;
        }
        left -= inc;

        env[i].lock    = lock;
        env[i].i       = i;
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

    printf("Want: %d\nGot:  %d\n", target, counter);
    return 0;
}