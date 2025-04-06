package com.gsTech.telegramBot.controllers;

import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import com.gsTech.telegramBot.services.CommandDispatcher;
import com.gsTech.telegramBot.services.TelegramApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/webhook")
public class TelegramWebhookController {


    @Value("${telegram.webhook.url}")
    private String urlNgrok;

    @Autowired
    private TelegramApiService telegramApiService;

    @Autowired
    private CommandDispatcher dispatcher;


    @PostMapping
    public ResponseEntity<String> handleUpdate(@RequestBody TelegramUpdate update) {
        dispatcher.dispatch(update);

        return ResponseEntity.ok().body("Success");
    }


    @GetMapping("/setWebhook")
    public String setWebhook() {
        String webhookUrl = urlNgrok+"/webhook";
        telegramApiService.setWebhook(webhookUrl);
        return "Webhook configurado.";
    }

}
