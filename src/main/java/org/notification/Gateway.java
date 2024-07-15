package org.notification;

public class Gateway {
    public void send(String userId, String message) {
        System.out.println("sending message to user -> " + userId + ": " + message);
    }
}
