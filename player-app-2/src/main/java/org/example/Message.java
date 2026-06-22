package org.example;

/*
 Simple message object.
 Carries ONE number as text.
*/
public class Message {

    public final String sender;
    public final String text;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String serialize() {
        return sender + "|" + text;
    }

    public static Message deserialize(String s) {

        String[] p = s.split("\\|");
        return new Message(p[0], p[1]);
    }
}
