-module(sieve).
-export([primes/1]).

-spec primes(N) -> list(integer()) when
    N :: integer().

primes(N) when is_integer(N) ->
    Self = self(),
    Filter = spawn(fun() -> filter(Self) end),
    feed(2, N, Filter),
    collect([]).

collect(List) ->
    receive
        done -> List;
        P -> collect(List ++ [P])
    end.

feed(Start, End, Pid) ->
    Pid ! Start,
    if
        Start < End -> feed(Start + 1, End, Pid);
        true -> Pid ! exit
    end.

filter(Collect) ->
    receive
        exit ->
            Collect ! done;
        P ->
            Collect ! P,
            Next = spawn(fun() -> filter(Collect) end),
            filter(Next, P)
    end.

filter(Next, P) ->
    receive
        exit ->
            Next ! exit;
        N ->
            if
                N rem P /= 0 -> Next ! N;
                true -> ok
            end,
            filter(Next, P)
    end.
