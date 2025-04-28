package com.gsTech.telegramBot.handlers.menu;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MenuHandler implements CommandHandler {

    @Autowired
    private SendMessageFactory sendMessageFactory;


    @Override
    public boolean canHandle(Update update) {

        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().equalsIgnoreCase("/menu");
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getMessage().getChatId();
        return sendMessageFactory.sendMessageWithMenu(chatId, "Escolha uma opção:");
    }
}
