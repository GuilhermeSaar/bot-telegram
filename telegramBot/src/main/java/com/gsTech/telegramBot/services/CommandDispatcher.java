package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.handler.CommandHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class CommandDispatcher {

    private final List<CommandHandler> handlers;

    public CommandDispatcher(List<CommandHandler> handlers) {
        this.handlers = handlers;
    }

    public BotApiMethod<?> dispatch(Update update) {

        for (CommandHandler handler : handlers) {

            if (handler.canHandle(update)) {
                return handler.handle(update);
            }
        }
        return null;
    }
}
