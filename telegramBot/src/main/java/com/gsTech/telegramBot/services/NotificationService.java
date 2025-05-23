package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.orm.Event;
import com.gsTech.telegramBot.orm.Notification;
import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.repositories.NotificationRepository;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;
    @Autowired
    @Lazy
    private TelegramApiService telegramBot;
    @Autowired
    private SendMessageFactory sendMessage;


    public void create(User user, Event event, LocalDateTime notifyAt) {
        var notification = new Notification();
        notification.setUser(user);
        notification.setEvent(event);
        notification.setNotifyAt(notifyAt);
        notification.setSent(false);
        repository.save(notification);
    }

    public List<Notification> findAllToSend(LocalDateTime now) {
        return repository.findBySentFalseAndNotifyAtBefore(now);
    }

    public void markAsSent(Notification notification) {
        notification.setSent(true);
        repository.save(notification);
    }

    public void sendNotification(Notification notification) {
        Long chatId = notification.getUser().getChatId();
        String eventName = notification.getEvent().getEventName();

        SendMessage message = sendMessage.sendMessage(chatId, "🔔 Notificação: Seu evento \"" + eventName + "\" está próximo!");

        try {
            telegramBot.execute(message);  // ENVIA a mensagem via Telegram API
            markAsSent(notification);
            System.out.println("✅ Mensagem enviada com sucesso para chatId=" + chatId);
        } catch (TelegramApiException e) {
            System.out.println("❌ Erro ao enviar mensagem para chatId=" + chatId);
            e.printStackTrace();
        }
    }

}
