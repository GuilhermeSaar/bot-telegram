package com.gsTech.telegramBot.handlers.menu;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BackMenuHandler implements CommandHandler {

    @Autowired
    private SendMessageFactory sendMessageFactory;
    @Autowired
    private UserStateService userState;


    @Override
    public boolean canHandle(Update update) {

        return update.hasCallbackQuery()
                && CallbackAction.MENU.name().equals(update.getCallbackQuery().getData());
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        userState.clearUserState(chatId);
        return sendMessageFactory.sendMessageWithMenu(chatId, "Menu de interação do gerenciador de tarefas");
    }
}
