package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class CommandDispatcher {

    private final List<CommandHandler> handlers;
    private final SendMessageFactory sendMessageFactory;

    public CommandDispatcher(List<CommandHandler> handlers, SendMessageFactory sendMessageFactory) {
        this.handlers = handlers;
        this.sendMessageFactory = sendMessageFactory;
    }

    public BotApiMethod<?> dispatch(Update update) {

        for (CommandHandler handler : handlers) {
            if (handler.canHandle(update)) {
                return handler.handle(update);
            }
        }

        return handleDefault(update);
    }


    private BotApiMethod<?> handleDefault(Update update) {

        Long chatId = null;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        }
        else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        if (chatId != null) {
            return sendMessageFactory.sendMessageWithMenu(chatId, "Desculpe, não entendi. Escolha uma opção");
        }

        return null;
    }

}
