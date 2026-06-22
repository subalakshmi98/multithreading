package org.example;

import java.util.function.Consumer;

/*
 Communication abstraction.
*/
public interface MessageBus {

    void send(String receiver, Message msg);
    void register(String name, Consumer<Message> handler);
    void shutdown();
}
