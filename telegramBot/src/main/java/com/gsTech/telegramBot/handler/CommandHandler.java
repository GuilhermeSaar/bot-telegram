package com.gsTech.telegramBot.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {

    boolean canHandle(Update update);
    BotApiMethod<?> handle(Update update);
}
