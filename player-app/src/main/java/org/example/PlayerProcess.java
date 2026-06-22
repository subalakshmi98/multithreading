package org.example;

/*
 Multi JVM runner.
*/
public class PlayerProcess {

    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println("Usage: name partner myPort otherPort");
            return;
        }

        String name = args[0];
        String partner = args[1];

        int myPort = Integer.parseInt(args[2]);

        int otherPort = Integer.parseInt(args[3]);

        SocketBus bus = new SocketBus(myPort, otherPort);

        Player player = new Player(name, partner, bus);

        player.register();

        // Only P1 starts
        boolean initiator = name.equals("P1");

        player.start(initiator);

        if (initiator) {
            System.out.println("Type: 1");
        } else {
            System.out.println("Waiting...");
        }
    }
}

