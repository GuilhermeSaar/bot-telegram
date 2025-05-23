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

import java.util.List;

@Component
public class NotifyInMenuHandler implements CommandHandler {


    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.NOTIFY.name().equals(update.getCallbackQuery().getData());
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        List<EventDTO> events = eventService.findAllByChatId(chatId);

        return sendMessage.editMessageNotifyEvent(chatId, events, messageId);
    }
}
