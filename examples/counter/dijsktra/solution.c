#include <err.h>
#include <stdatomic.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>

#include "solution.h"

struct Solution {
    int   N;
    bool *b;
    bool *c;
    int   k;
};

Solution *solution_new(int N) {
    Solution *l = malloc(sizeof(Solution) + 2 * N * sizeof(bool));
    if (l == NULL) {
        err(EXIT_FAILURE, "bad alloc");
    }

    l->N = N;
    l->b = (bool*)((void*)l + sizeof(Solution));
    l->c = (bool*)((void*)l->b + N * sizeof(bool));
    l->k = 0;

    memset(l->b, 0, N * sizeof(bool));
    memset(l->c, 0, N * sizeof(bool));

    return l;
}

void solution_lock(Solution *l, int i) {
    l->b[i] = true;

Loop:
    while (l->k != i) {
        l->c[i] = false;
        if (!l->b[l->k]) {
            l->k = i;
        }
    }

    l->c[i] = true;
    for (int j = 0; j < l->N; j++) {
        if (j != i && l->c[j]) {
            goto Loop;
        }
    }
}

void solution_unlock(Solution *l, int i) {
    l->b[i] = false;
    l->c[i] = false;
}

void solution_free(Solution *l) {
    free(l);
}
