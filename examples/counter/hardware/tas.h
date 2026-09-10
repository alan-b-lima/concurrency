#ifndef TEST_AND_SET_HEADER__
#define TEST_AND_SET_HEADER__

// test_and_set describes an atomic instruction that saves the lowest bit of
// REF then sets the lowest bit to 1, the following pseudocode implements it:
//
// ```
// bool test_and_set(bool* ref) {
//     atomic {
//         bool b = Bit(*lock, 0);
//         Bit(*lock, 0) = 1;
//     }
//     return b;
// }
// ```
//
// The `Bit` function is a bitwise addresser, in this case, it is used to
// access the lowest bit of REF.
extern bool test_and_set(int *ref);

// test_and_reset describes an atomic instruction similar to test_and_set, but
// it sets the lowest bit to 0 instead.
extern bool test_and_reset(int *ref);

#endif