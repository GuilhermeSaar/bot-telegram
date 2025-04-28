package com.gsTech.telegramBot.handlers.newEvent;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class NewEventHandler implements CommandHandler {

    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private SendMessageFactory sendMessage;


    @Override
    public boolean canHandle(Update update) {
        if(update.getMessage() == null || update.getMessage().getText() == null) {
            return false;
        }

        return update.getMessage().getText().equalsIgnoreCase("/novo");
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getMessage().getChat().getId();
        return startNewEvent(chatId);

    }

    private SendMessage startNewEvent(Long chatId) {

        userState.setUserState(chatId, "WAITING_FOR_NAME");
        userEvent.setUserEvent(chatId);
        return sendMessage.sendMessage(chatId, "Novo compromisso!\n\nNome do compromisso:");
    }
}
