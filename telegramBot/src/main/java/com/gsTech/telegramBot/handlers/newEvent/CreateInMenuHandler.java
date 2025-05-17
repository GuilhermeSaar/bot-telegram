package com.gsTech.telegramBot.handlers.newEvent;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CreateInMenuHandler implements CommandHandler {


    @Autowired
    private SendMessageFactory sendMessageFactory;
    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;


    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.NEW_EVENT.name().equals(update.getCallbackQuery().getData());
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        return startNewEvent(chatId);
    }

    private SendMessage startNewEvent(Long chatId) {

        userState.setUserState(chatId, "WAITING_FOR_NAME");
        userEvent.setUserEvent(chatId);
        return sendMessageFactory.sendMessage(chatId,"Nova tarefa!\n\nNome da tarefa:");
    }
}
