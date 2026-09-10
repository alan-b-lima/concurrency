#include <threads.h>

#include "mutex.h"
#include "tas.h"

static bool mutex_trylock__(Mutex *mu, int tries);

void mutex_lock(Mutex *mu) {
    if (!test_and_set(&mu->lock)) {
        return;
    }

    while (!mutex_trylock__(mu, 30)) {
        thrd_yield();
    }
}

static bool mutex_trylock__(Mutex *mu, int tries) {
    for (int i = 0; i < tries; i++) {
        if (!test_and_set(&mu->lock)) {
            return true;
        }
    }
    return false;
}

bool mutex_unlock(Mutex *mu) {
    return test_and_reset(&mu->lock);
}
