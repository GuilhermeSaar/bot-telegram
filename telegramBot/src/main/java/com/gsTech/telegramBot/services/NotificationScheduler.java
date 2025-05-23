package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.orm.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void checkNotifications() {

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        List<Notification> pending = notificationService.findAllToSend(now);

        for (Notification notification : pending) {

            notificationService.sendNotification(notification);
        }
    }

}
