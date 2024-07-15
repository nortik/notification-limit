package org.notification;

public interface NotificationService {
    void send(String type, String userId, String message);
}
