#include "spinlock.h"
#include "tas.h"

void spinlock_lock(SpinLock *sl) {
    while (test_and_set(&sl->lock));
}

bool spinlock_trylock(SpinLock *sl, int tries) {
    for (int i = 0; i < tries; i++) {
        if (!test_and_set(&sl->lock)) {
            return true;
        }
    }
    return false;
}

bool spinlock_unlock(SpinLock *sl) {
    return test_and_reset(&sl->lock);
}
