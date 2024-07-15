package org.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private Gateway gateway;
    private NotificationServiceImpl notificationService;

    @BeforeEach
    public void setUp() throws IOException {
        gateway = mock(Gateway.class);
        notificationService = new NotificationServiceImpl(gateway, "src/test/resources/config.properties");
    }

    @Test
    public void testSendLimitGood() {
        notificationService.send("news", "user", "news 1");
        verify(gateway, times(1)).send("user", "news 1");

        notificationService.send("status", "user", "status 1");
        verify(gateway, times(1)).send("user", "status 1");
    }

    @Test
    public void testSendLimitWrong() {
        notificationService.send("news", "user", "news 1");
        verify(gateway, times(1)).send("user", "news 1");

        //Exceeding news limit
        notificationService.send("news", "user", "news 2");
        verify(gateway, times(0)).send("user", "news 2");

        //Within status limit
        notificationService.send("status", "user", "status 1");
        verify(gateway, times(1)).send("user", "status 1");

        //Exceeding status limit
        notificationService.send("status", "user", "status 2");
        verify(gateway, times(1)).send("user", "status 2");
    }

    @Test
    public void sendDifferentUsersTest() {
        notificationService.send("news", "user1", "news 1");
        verify(gateway, times(1)).send("user1", "news 1");

        notificationService.send("news", "user2", "news 1");
        verify(gateway, times(1)).send("user2", "news 1");
    }

    @Test
    public void invalidPeriodValueTest() throws IOException {
        NotificationServiceImpl spyService = Mockito.spy(notificationService);

        //Mock the behavior -> getRateLimitPeriod
        doThrow(new NumberFormatException("Invalid period value for type: news")).when(spyService).getRateLimitPeriod("news");

        //Sending a notification and catching the exception
        try {
            spyService.send("news", "user", "news 1");
        } catch (NumberFormatException e) {
            assertEquals("Invalid period value for type: news", e.getMessage());
        }
        verify(gateway, times(0)).send(anyString(), anyString());
    }
}