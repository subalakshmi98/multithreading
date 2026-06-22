package org.example;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/*
 Reliable TCP-based message bus.
*/
public class SocketBus implements MessageBus {

    private final int myPort;
    private final int otherPort;

    private ServerSocket server;

    private final ExecutorService pool =
            Executors.newSingleThreadExecutor();

    public SocketBus(int myPort, int otherPort) {

        this.myPort = myPort;
        this.otherPort = otherPort;
    }

    @Override
    public void register(String name,
                         Consumer<Message> handler) {

        // START SERVER SYNCHRONOUSLY (IMPORTANT)
        try {
            server = new ServerSocket(myPort);

            System.out.println(
                    name + " listening on port " + myPort);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot open port " + myPort, e);
        }

        // Accept loop in background
        pool.submit(() -> {

            try {
                while (!server.isClosed()) {

                    Socket s = server.accept();

                    BufferedReader in =
                            new BufferedReader(
                                    new InputStreamReader(
                                            s.getInputStream()));

                    String line = in.readLine();

                    if (line != null) {

                        Message msg =
                                Message.deserialize(line);

                        handler.accept(msg);
                    }

                    s.close();
                }

            } catch (IOException e) {
                // socket closed → exit
            }
        });
    }

    @Override
    public void send(String receiver,
                     Message msg) {

        // RETRY LOGIC (IMPORTANT)
        for (int i = 0; i < 10; i++) {

            try (Socket s = new Socket("localhost", otherPort)) {

                PrintWriter out = new PrintWriter(s.getOutputStream(), true);

                out.println(msg.serialize());

                return; // success

            } catch (IOException e) {

                // Wait and retry
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {}
            }
        }

        System.out.println(
                "Failed to send to port " + otherPort);
    }

    @Override
    public void shutdown() {

        try {
            if (server != null)
                server.close();
        } catch (IOException e) {
        }

        pool.shutdownNow();
    }
}
