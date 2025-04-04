package com.gsTech.telegramBot.controllers;

import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.TelegramApiService;
import com.gsTech.telegramBot.services.TelegramWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/webhook")
public class TelegramWebhookController {

    @Autowired
    private EventService eventService;

    @Value("${telegram.webhook.url}")
    private String urlNgrok;

    @Autowired
    private TelegramApiService telegramApiService;
    @Autowired
    private TelegramWebhookService telegramWebhookService;


    @PostMapping
    public ResponseEntity<String> handleUpdate(@RequestBody TelegramUpdate update) {
        telegramWebhookService.processUpdate(update);
        return ResponseEntity.ok().build();
    }

    private void commitments(Long chatId) {
        telegramApiService.sendMessage(chatId, eventService.findAll().toString());
    }

    private void handleHelp(Long chatId) {
        telegramApiService.sendMessage(chatId, "Comandos Disponíveis\n" +
                "Comando 1\n" +
                "Comando 2\n" +
                "Comando 3");
    }

    private void handleDefault(Long chatId) {
        telegramApiService.sendMessage(chatId,"Comando não reconhecido.");
    }

    @GetMapping("/setWebhook")
    public String setWebhook() {
        String webhookUrl = urlNgrok+"/webhook";
        telegramApiService.setWebhook(webhookUrl);
        return "Webhook configurado.";
    }

}
