package com.gsTech.telegramBot.handlers.notify;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.orm.Event;
import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.NotificationService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Component
public class NotifyMinutesHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SendMessageFactory sendMessage;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && update.getCallbackQuery().getData().startsWith("NOTIFY_MINUTES:");
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        String[] parts = update.getCallbackQuery().getData().split(":");
        int minutesBefore = Integer.parseInt(parts[1]);
        Long eventId = Long.parseLong(parts[2]);

        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        Event event = eventService.getEntityById(eventId);
        User user = event.getUser();

        LocalDateTime notifyAt = event.getTime().minusMinutes(minutesBefore);

        notificationService.create(user, event, notifyAt);

        return sendMessage.editMessageText(chatId, messageId, "✅ Notificação agendada para " + minutesBefore + " minutos antes do evento.");

    }
}
