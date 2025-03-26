package com.gsTech.telegramBot.controllers;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.DTO.telegram.Message;
import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.TelegramBotService;
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
    private TelegramBotService telegramBotService;

    @PostMapping
    public ResponseEntity<String> handleUpdate(@RequestBody TelegramUpdate data) {

        if (data == null || data.getMessage() == null) {
            System.out.println("Update recebido sem mensagem.");
            return ResponseEntity.ok("OK");
        }

        TelegramUpdate.Message message = data.getMessage();
        Long chatId = message.getChat().getId();
        String messageText = message.getText();

        System.out.println("Message recebido: " + messageText + " chatId: " + chatId);

        if (messageText != null && messageText.equalsIgnoreCase("/compromisso")) {

            System.out.println("Próximo compromisso.\n");

            EventDTO eventDTO = eventService.findByEventId(1L);
            System.out.println("Evento: " + eventDTO);
        }

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/setWebhook")
    public String setWebhook() {
        String webhookUrl = urlNgrok+"/webhook";
        telegramBotService.setWebhook(webhookUrl);
        return "Webhook configurado.";
    }
}
