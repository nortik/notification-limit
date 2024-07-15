package org.notification;

public class Main {

    public static void main(String[] args) {
        NotificationServiceImpl service = new NotificationServiceImpl(new Gateway(),
                "src/main/resources/config.properties");

        // Just for test
        service.send("news", "user", "news 1");
        service.send("news", "user", "news 2");
        service.send("news", "user", "news 3");
        service.send("news", "another user", "news 1");
        service.send("update", "user", "update 1");
        service.send("pepe", "userPepe", "update 1");
        service.send("pepe2", "userPepe2", "update 1");
        service.send("update", "user2", "update 2");
        service.send("update", "user2", "update 3");
        service.send("news", "user", "news 4");
        service.send("status", "userStatus", "update 1");
        service.send("status", "userStatus", "update 2");
        service.send("status", "userStatus", "update 3");
    }
}