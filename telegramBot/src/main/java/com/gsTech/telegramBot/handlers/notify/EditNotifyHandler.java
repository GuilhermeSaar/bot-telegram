package com.gsTech.telegramBot.handlers.notify;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EditNotifyHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().startsWith(CallbackAction.EDIT_NOTIFY.name());
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        String[] parts = update.getCallbackQuery().getData().split(":");
        Long eventId = Long.parseLong(parts[1]);

        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        EventDTO event = eventService.findByEventId(eventId);

        return sendMessage.editMessageNotifyOptions(chatId, event.getId(), messageId);
    }
}
