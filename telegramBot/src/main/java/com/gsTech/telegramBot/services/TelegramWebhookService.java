package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramWebhookService {

    @Autowired
    private EventService eventService;

    private Map<Long, String> userState = new HashMap<>();
    private Map<Long, EventDTO> userEvent = new HashMap<>();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    @Autowired
    private TelegramApiService telegramApiService;


    public void processUpdate(TelegramUpdate update) {

        if(update.getMessage() == null) return;

        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChat().getId();

        if(messageText.equalsIgnoreCase("/novo")) {
            startNewEvent(chatId);
            return;
        }

        if(messageText.equalsIgnoreCase("S")) {
            cancelOperation(chatId);
            return;
        }
        processState(chatId, messageText);
    }

    private void startNewEvent(Long chatId) {

        userState.put(chatId, "WAITING_FOR_NAME");
        userEvent.put(chatId, new EventDTO());
        telegramApiService.sendMessage(chatId, "Novo compromisso!\n\nNome do compromisso:");
    }

    private void cancelOperation(Long chatId) {

        userState.remove(chatId);
        userEvent.remove(chatId);
        telegramApiService.sendMessage(chatId, "Operação cancelada!");
    }

    private void processState(Long chatId, String messageText) {
        String state = userState.get(chatId);
        EventDTO event = userEvent.get(chatId);

        if (state == null || event == null) {
            return;
        }

        switch (state) {
            case "WAITING_FOR_NAME":
                event.setEventName(messageText);
                userState.put(chatId, "WAITING_FOR_TYPE");
                telegramApiService.sendMessage(chatId, "Tipo de compromisso (Pessoal, Profissional...)");
                break;

            case "WAITING_FOR_TYPE":
                event.setEventType(messageText);
                userState.put(chatId, "WAITING_FOR_LOCATION");
                telegramApiService.sendMessage(chatId, "Local do compromisso:");
                break;

            case "WAITING_FOR_LOCATION":
                event.setLocation(messageText);
                userState.put(chatId, "WAITING_FOR_DATE");
                telegramApiService.sendMessage(chatId, "Data do compromisso (ex: dd/MM/yyyy HH:mm):");
                break;

            case "WAITING_FOR_DATE":
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(messageText, DATE_TIME_FORMATTER);
                    event.setTime(dateTime);
                    eventService.newEvent(event);
                    telegramApiService.sendMessage(chatId, "Compromisso criado:\n" + event);
                    userState.remove(chatId);
                } catch (DateTimeParseException e) {
                    telegramApiService.sendMessage(chatId, "Formato inválido! EX: 14/09/2025 19:30");
                }
                break;
        }
    }
}
