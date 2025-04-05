
package com.gsTech.telegramBot.handler;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.TelegramApiService;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class ConversationFlowHandler implements CommandHandler {

    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private TelegramApiService telegramApiService;
    @Autowired
    private EventService eventService;

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    @Override
    public boolean canHandle(TelegramUpdate update) {

        if(update.getMessage() == null || update.getMessage().getText() == null) {

            return false;
        }

        Long chatId = update.getMessage().getChat().getId();
        String state = userState.getUserState(chatId);

        return state != null;
    }

    @Override
    public void handle(TelegramUpdate update) {

        Long chatId = update.getMessage().getChat().getId();
        String messageText = update.getMessage().getText();

        processState(chatId, messageText);
    }


    private void processState(Long chatId, String messageText) {

        String state = userState.getUserState(chatId);
        EventDTO event = userEvent.getEvent(chatId);

        if (state == null || event == null) {
            return;
        }

        switch (state) {
            case "WAITING_FOR_NAME":
                event.setEventName(messageText);
                userState.setUserState(chatId, "WAITING_FOR_TYPE");
                telegramApiService.sendMessage(chatId, "Tipo de compromisso (Pessoal, Profisional...)");
                break;

            case "WAITING_FOR_TYPE":
                event.setEventType(messageText);
                userState.setUserState(chatId, "WAITING_FOR_LOCATION");
                telegramApiService.sendMessage(chatId, "Local do compromisso:");
                break;

            case "WAITING_FOR_LOCATION":
                event.setLocation(messageText);
                userState.setUserState(chatId, "WAITING_FOR_DATE");
                telegramApiService.sendMessage(chatId, "Data do compromisso (ex: dd/MM/yyyy HH:mm):");
                break;

            case "WAITING_FOR_DATE":
                try {
                    LocalDateTime date = LocalDateTime.parse(messageText, DATE_TIME_FORMATTER);
                    event.setTime(date);
                    eventService.newEvent(event);
                    telegramApiService.sendMessage(chatId, "Compromisso criado:\n" + event);
                    userState.clearUserState(chatId);
                } catch (DateTimeParseException e) {
                    telegramApiService.sendMessage(chatId, "Formato inválido! EX: 14/09/2025 19:30");
                }
                break;
        }
    }

}

