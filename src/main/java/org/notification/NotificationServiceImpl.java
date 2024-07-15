package org.notification;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class NotificationServiceImpl implements NotificationService {

    private Gateway gateway;
    private Map<String, Map<String, List<Long>>> notif;
    private Properties rateLimit;

    public NotificationServiceImpl(Gateway gateway, String propertiesFilePath) {
        this.gateway = gateway;
        this.notif = new HashMap<>();
        this.rateLimit = new Properties();

        try (InputStream input = new FileInputStream(propertiesFilePath)) {
            rateLimit.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void send(String type, String userId, String message) {
        long currentTime = System.currentTimeMillis();
        if (isRateLimited(type, userId, currentTime)) {
            System.out.println("Rate limit exceeded -> user: " + userId + ", type: " + type);
            return;
        }
        gateway.send(userId, message);
        logNotification(type, userId, currentTime);
    }

    private boolean isRateLimited(String type, String userId, long currentTime) {
        long limitPeriod = getRateLimitPeriod(type);
        int limitCount = getRateLimitCount(type);

        if (!notif.containsKey(userId)) {
            notif.put(userId, new HashMap<>());
        }
        Map<String, List<Long>> userNotifications = notif.get(userId);
        if (!userNotifications.containsKey(type)) {
            userNotifications.put(type, new ArrayList<>());
        }
        List<Long> timestamps = userNotifications.get(type);
        Iterator<Long> iterator = timestamps.iterator();
        while (iterator.hasNext()) {
            Long timestamp = iterator.next();
            if (currentTime - timestamp > limitPeriod) {
                iterator.remove();
            }
        }

        return timestamps.size() >= limitCount;
    }

    private void logNotification(String type, String userId, long currentTime) {
        notif.get(userId).get(type).add(currentTime);
    }

    long getRateLimitPeriod(String type) {
        String periodKey = type + ".period";
        String periodValue = rateLimit.getProperty(periodKey);
        try {
            //just I will add numbers
            return periodValue != null ? Long.parseLong(periodValue.replaceAll("[^0-9]", "")) : 60000; // valor predeterminado de 1 minuto
        } catch (NumberFormatException e) {
            //just in casa if I have some issues
            System.err.println("Invalid period: " + type + ". Using default 1 min.");
            return 60000;// 1 min
        }
    }

    private int getRateLimitCount(String type) {
        String countKey = type + ".count";
        String countValue = rateLimit.getProperty(countKey);
        try {
            //just I will add numbers
            return countValue != null ? Integer.parseInt(countValue.replaceAll("[^0-9]", "")) : 1;
        } catch (NumberFormatException e) {
            //just in casa if I have some issues
            System.err.println("Invalid count value for type: " + type + ". Using default: 1");
            return 1;
        }
    }
}