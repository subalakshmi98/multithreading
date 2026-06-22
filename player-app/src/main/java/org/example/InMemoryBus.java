package org.example;

import java.util.concurrent.*;
import java.util.*;
import java.util.function.Consumer;

/*
 In-process.
*/
public class InMemoryBus implements MessageBus {

    private final Map<String, BlockingQueue<Message>> queues = new ConcurrentHashMap<>();

    private final ExecutorService pool = Executors.newCachedThreadPool();

    @Override
    public void register(String name,
                         Consumer<Message> handler) {

        BlockingQueue<Message> q = new LinkedBlockingQueue<>();

        queues.put(name, q);

        pool.submit(() -> {
            try {
                while (true) {
                    Message m = q.take();
                    handler.accept(m);
                }
            } catch (InterruptedException e) {
            }
        });
    }

    @Override
    public void send(String receiver, Message msg) {

        BlockingQueue<Message> q = queues.get(receiver);

        if (q != null) {
            q.offer(msg);
        }
    }

    @Override
    public void shutdown() {
        pool.shutdownNow();
    }
}

