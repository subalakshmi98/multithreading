package org.example;

import java.util.Scanner;

/*
 Both append sent + received numbers.
*/
public class Player {

    private static final int MAX = 10;

    private final String name;
    private final String partner;
    private final MessageBus bus;

    // Local full sequence
    private final StringBuilder sequence = new StringBuilder();

    private final Scanner scanner = new Scanner(System.in);

    public Player(String name, String partner, MessageBus bus) {

        this.name = name;
        this.partner = partner;
        this.bus = bus;
    }

    // Register receiver
    public void register() {
        bus.register(name, this::onMessage);
    }

    // Only P1 starts
    public void start(boolean initiator) {
        if (initiator) {
            new Thread(this::readFirst).start();
        }
    }

    // Read first "1"
    private void readFirst() {

        System.out.print(name + " > ");
        String input = scanner.nextLine();

        if (!input.equals("1")) {
            System.out.println("Start with 1");
            return;
        }

        // Append own number
        append(1);
        send("1");
    }

    // When message arrives
    private void onMessage(Message msg) {

        int num = Integer.parseInt(msg.text);
        // Append received number
        append(num);

        System.out.println(name + " received: " + sequence);

        // Stop at 10
        if (num >= MAX) {
            finish();
            return;
        }

        // Next number
        int next = num + 1;

        // Append own next number
        append(next);
        send(String.valueOf(next));
    }

    // Append to local sequence
    private void append(int num) {

        if (sequence.length() == 0) {
            sequence.append(num);
        } else {
            sequence.append("-").append(num);
        }
    }

    // Send only one number
    private void send(String text) {

        System.out.println(name + " sends: " + text);
        Message msg = new Message(name, text);
        bus.send(partner, msg);
    }

    private void finish() {

        System.out.println("Finished.");
        bus.shutdown();
    }
}
