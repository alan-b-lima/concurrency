#ifndef DIJSKTRA_SOLUTION_HEADER
#define DIJSKTRA_SOLUTION_HEADER

typedef struct Solution Solution;

Solution *solution_new(int);
void      solution_free(Solution *l);

void solution_lock(Solution *l, int i);
void solution_unlock(Solution *l, int i);

#endif