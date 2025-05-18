package com.gsTech.telegramBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramApiService extends TelegramWebhookBot {


    @Value("${telegram.token}")
    private String botToken;
    //@Value("${telegram.botUserName}")
    private String botUsername;
    //@Value("${telegram.webhook.url}")
    private String botPath;


    @Autowired
    private CommandDispatcher dispatcher;


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        return dispatcher.dispatch(update);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}

