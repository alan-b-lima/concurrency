-module(client).
-export([listen/1]).

listen(Server) ->
    Loop = spawn(fun() -> connect(Server) end),
    Server ! {bind, Loop},
    Loop.

connect(Server) ->
    receive
        connect ->
            loop(Server)
    after 5000 ->
        io:format("Connection Failed~n")
    end.

loop(Server) ->
    receive
        close ->
            Server ! {bind, self()},
            connect(Server);
        P ->
            io:format("Page: ~p~n", [P]),
            loop(Server)
    end.
