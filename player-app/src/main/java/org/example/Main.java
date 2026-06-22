package org.example;

/*
 Same JVM runner.
*/
public class Main {

    public static void main(String[] args) {

        InMemoryBus bus = new InMemoryBus();

        Player p1 = new Player("P1", "P2", bus);

        Player p2 = new Player("P2", "P1", bus);

        p1.register();
        p2.register();

        p1.start(true);

        System.out.println("Type: 1");
    }
}

