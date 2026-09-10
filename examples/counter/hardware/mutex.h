#ifndef SPIN_LOCK_HEADER__
#define SPIN_LOCK_HEADER__

#include <stdbool.h>

// Mutex is the implementation of a concurrent and parallel safe mutual
// exclusion lock, a zero-valued mutex is unlocked and ready for use.
//
// The LOCK field should not be tempered with.
typedef struct { int lock; } Mutex;

// mutex_lock spins forever until the lock can be acquired. mutex_lock does not
// do busy waiting.
void mutex_lock(Mutex *mu);

// mutex_unlock unlock the mutex. mutex_unlock reports whether a locked lock
// was unlocked.
bool mutex_unlock(Mutex *mu);

#endif