package com.gsTech.telegramBot.controllers;

import com.gsTech.telegramBot.services.TelegramApiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping(value = "/webhook")
public class TelegramWebhookController {


    private final TelegramApiService telegramApiService;

    public TelegramWebhookController(TelegramApiService telegramApiService) {
        this.telegramApiService = telegramApiService;
    }

    @PostMapping
    public BotApiMethod<?> handleWebhook(@RequestBody Update update) {

        return telegramApiService.onWebhookUpdateReceived(update);
    }
}
