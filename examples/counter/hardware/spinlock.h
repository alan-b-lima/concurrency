#ifndef SPIN_LOCK_HEADER__
#define SPIN_LOCK_HEADER__

#include <stdbool.h>

// SpinLock is the implementation of a concurrent and parallel safe mutual
// exclusion spin lock, a zero-valued spin lock is unlocked and ready for use.
//
// The LOCK field should not be tempered with.
typedef struct { int lock; } SpinLock;

// spinlock_lock spins forever until the lock can be acquired. spinlock_lock
// may not return.
void spinlock_lock(SpinLock *sl);

// spinlock_trylock spins at a maximum of TRIES tries or until the lock is
// acquired. spinlock_trylock reports whether the lock was acquired.
bool spinlock_trylock(SpinLock *sl, int tries);

// spinlock_unlock unlock the spin lock. spinlock_unlock reports whether a
// locked lock was unlocked.
bool spinlock_unlock(SpinLock *sl);

#endif