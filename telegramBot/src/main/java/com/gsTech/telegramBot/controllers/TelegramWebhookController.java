package com.gsTech.telegramBot.controllers;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import com.gsTech.telegramBot.enums.EventType;
import com.gsTech.telegramBot.enums.Priority;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.TelegramBotService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping(value = "/webhook")
public class TelegramWebhookController {

    @Autowired
    private EventService eventService;

    @Value("${telegram.webhook.url}")
    private String urlNgrok;

    @Autowired
    private TelegramBotService telegramBotService;

    private Map<String, Consumer<Long>> commandHandlers = new HashMap<>();
    private Map<Long, String> userState = new HashMap<>();
    private Map<Long, EventDTO> eventInput = new HashMap<>();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @PostConstruct
    public void init() {
        commandHandlers.put("/novo", this::newCommitment);
        commandHandlers.put("/compromisso", this::commitments);
    }

    private void newCommitment(Long chatId) {
        telegramBotService.sendMessage(chatId, "Qual é o título do compromisso?");
        userState.put(chatId, "WAITING_FOR_TITLE");
        eventInput.put(chatId, new EventDTO());
    }

    @PostMapping
    public ResponseEntity<String> handleUpdate(@RequestBody TelegramUpdate data) {


        Long chatId = data.getMessage().getChat().getId();
        String messageText = data.getMessage().getText();

        if (userState.containsKey(chatId)) {
            String currentState = userState.get(chatId);

            switch (currentState) {
                case "WAITING_FOR_TITLE":
                    EventDTO tempEvent = eventInput.get(chatId);
                    tempEvent.setEventName(messageText);

                    telegramBotService.sendMessage(chatId, "Qual é o tipo do compromisso? (Ex: MEETING, TASK)");
                    userState.put(chatId, "WAITING_FOR_TYPE");
                    break;

                case "WAITING_FOR_TYPE":
                    try {
                        eventInput.get(chatId).setEventType(EventType.valueOf(messageText.toUpperCase()));
                        telegramBotService.sendMessage(chatId, "Onde será o compromisso?");
                        userState.put(chatId, "WAITING_FOR_LOCATION");
                    } catch (IllegalArgumentException e) {
                        telegramBotService.sendMessage(chatId, "Tipo inválido! Tente novamente (Ex: MEETING, TASK).");
                    }
                    break;

                case "WAITING_FOR_LOCATION":
                    eventInput.get(chatId).setLocation(messageText);
                    telegramBotService.sendMessage(chatId, "Qual é a prioridade do compromisso? (Ex: HIGH, MEDIUM, LOW)");
                    userState.put(chatId, "WAITING_FOR_PRIORITY");
                    break;

                case "WAITING_FOR_PRIORITY":
                    try {
                        eventInput.get(chatId).setPriority(Priority.valueOf(messageText.toUpperCase()));
                        telegramBotService.sendMessage(chatId, "Qual é o horário do compromisso? (Ex: 14:00)");
                        userState.put(chatId, "WAITING_FOR_TIME");
                    } catch (IllegalArgumentException e) {
                        telegramBotService.sendMessage(chatId, "Prioridade inválida! Tente novamente (Ex: HIGH, MEDIUM, LOW).");
                    }
                    break;

                case "WAITING_FOR_TIME":
                    try {

                        LocalDateTime dateTime = LocalDateTime.parse(messageText, DATE_TIME_FORMATTER);
                        eventInput.get(chatId).setTime(LocalDateTime.parse(dateTime.toString()));

                        EventDTO savedEvent = eventService.newEvent(eventInput.get(chatId));
                        telegramBotService.sendMessage(chatId, "compromisso" +
                                savedEvent.getEventName());

                        userState.remove(chatId);
                        eventInput.remove(chatId);
                    } catch (DateTimeParseException e) {
                        telegramBotService.sendMessage(chatId, "formato de data/hora inválido");
                    }
                    break;

                default:
                    telegramBotService.sendMessage(chatId, "Não entendi. Digite /compromisso para começar.");
            }
        } else {
            commandHandlers.getOrDefault(messageText.toLowerCase(), this::handleDefault).accept(chatId);  // Corrigido
        }

        return ResponseEntity.ok("OK");
    }

    private void commitments(Long chatId) {
        telegramBotService.sendMessage(chatId, eventService.findAll().toString());
    }

    private void handleHelp(Long chatId) {
        telegramBotService.sendMessage(chatId, "Comandos Disponíveis\n" +
                "Comando 1\n" +
                "Comando 2\n" +
                "Comando 3");
    }

    private void handleDefault(Long chatId) {
        telegramBotService.sendMessage(chatId,"Comando não reconhecido.");
    }

    @GetMapping("/setWebhook")
    public String setWebhook() {
        String webhookUrl = urlNgrok+"/webhook";
        telegramBotService.setWebhook(webhookUrl);
        return "Webhook configurado.";
    }

}
