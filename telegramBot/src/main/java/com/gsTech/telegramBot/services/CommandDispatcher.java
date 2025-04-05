package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import com.gsTech.telegramBot.handler.CommandHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandDispatcher {

    private final List<CommandHandler> handlers;


    public CommandDispatcher(List<CommandHandler> handlers) {
        this.handlers = handlers;
    }


    public void dispatch(TelegramUpdate update) {

        for (CommandHandler handler : handlers) {

            if (handler.canHandle(update)) {
                handler.handle(update);
                return;
            }
        }
    }
}
