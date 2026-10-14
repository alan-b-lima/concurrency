-module(slides).
-export([start/0, serve/1]).

start() ->
    register(slides, self()),
    serve(0).

serve(Page) when is_integer(Page) ->
    serve(Page, []).

serve(Page, Clients) ->
    receive
        {bind, Client} ->
            Client ! connect,
            Client ! Page,
            Server = self(),
            Timeout = spawn(fun() -> timeout(Server, 10000, Client) end),
            io:format("(bind ~p)~n", [Client]),
            serve(Page, Clients ++ [{Client, Timeout}]);
        {timeout, Client} ->
            Client ! close,
            io:format("(timeout ~p)~n", [Client]),
            serve(Page, del(Client, Clients));
        {close, Client} ->
            io:format("(close ~p)~n", [Client]),
            serve(Page, del(Client, Clients));
        {change, To} ->
            if
                Page /= To -> send(Clients, Page);
                true -> nothing
            end,
            io:format("(change ~p)~n", [To]),
            serve(To, Clients);
        {exit} ->
            ok
    end.

send([], _) ->
    ok;
send([{Client, _} | Tail], Page) ->
    Client ! Page,
    send(Tail, Page).

del(_, []) ->
    [];
del(Item, [Head | Tail]) ->
    case Head of
        {Client, Timeout} when Client == Item ->
            Timeout ! done,
            del(Item, Tail);
        _ ->
            [Head] ++ del(Item, Tail)
    end.

timeout(Server, Timeout, Client) ->
    receive
        done -> done
    after Timeout ->
        Server ! {timeout, Client}
    end.
