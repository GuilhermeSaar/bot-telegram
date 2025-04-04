package com.gsTech.telegramBot.handler;

import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;

public interface CommandHandler {

    boolean canHandle(TelegramUpdate update);
    void handle(TelegramUpdate update);
}
