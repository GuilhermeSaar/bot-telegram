package com.gsTech.telegramBot.handlers.start;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements CommandHandler {

    @Autowired
    private SendMessageFactory sendMessageFactory;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().equals("/start");
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getFrom().getFirstName();

        String message = "Olá, " + firstName + "! \uD83D\uDC4B\n Esse é seu gerenciador de tarefas!";

        return sendMessageFactory.sendMessageWithMenu(chatId, message);
    }
}
