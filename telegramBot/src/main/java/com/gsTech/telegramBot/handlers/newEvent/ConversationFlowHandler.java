
package com.gsTech.telegramBot.handlers.newEvent;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.*;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private DateParseService dateParseService;
    @Autowired
    private SendMessageFactory sendMessage;

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    @Override
    public boolean canHandle(Update update) {

        if(update.getMessage() == null || update.getMessage().getText() == null) {

            return false;
        }

        Long chatId = update.getMessage().getChat().getId();
        String state = userState.getUserState(chatId);

        return state != null;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getMessage().getChat().getId();
        String messageText = update.getMessage().getText();
        String name = update.getMessage().getFrom().getFirstName();
        return processState(chatId, messageText, name);
    }


    private SendMessage processState(Long chatId, String messageText, String name) {

        String state = userState.getUserState(chatId);
        EventDTO event = userEvent.getEvent(chatId);


        if (state == null || event == null) {
            return sendMessage.sendMessage(chatId, "Erro interno: estado ou evento não encontrado.");
        }

        switch (state) {
            case "WAITING_FOR_NAME":
                event.setEventName(messageText);
                userState.setUserState(chatId, "WAITING_FOR_TYPE");
                return sendMessage.sendMessage(chatId, "Tipo de compromisso (Pessoal, Profisional...)");

            case "WAITING_FOR_TYPE":
                event.setEventType(messageText);
                userState.setUserState(chatId, "WAITING_FOR_LOCATION");
                return sendMessage.sendMessage(chatId, "Local do compromisso:");

            case "WAITING_FOR_LOCATION":
                event.setLocation(messageText);
                userState.setUserState(chatId, "WAITING_FOR_DATE");
                return sendMessage.sendMessage(chatId, "Data do compromisso\n" +
                        "Exs: " +
                        "14/09/2025 19:30:\n" +
                        "segunda às 14:00\n" +
                        "1 de dezembro às 09:45\n" +
                        "hoje as 08:30");

            case "WAITING_FOR_DATE":
                try {
                    String dateRegex = dateParseService.parseDate(messageText);
                    LocalDateTime date = LocalDateTime.parse(dateRegex, DATE_TIME_FORMATTER);
                    event.setTime(date);

                    var user = userService.getOrCreateUserByChatId(chatId, name);
                    eventService.saveNewEvent(event, user);

                    userState.clearUserState(chatId);
                    return sendMessage.sendMessage(chatId, "Compromisso criado:\n" + event);

                } catch (DateTimeParseException e) {
                    return sendMessage.sendMessage(chatId, "Formato inválido! " +
                            "Exs:\n" +
                            "Tente algo como:\n" +
                            "14/09/2025 19:30\n" +
                            "segunda às 14:00\n" +
                            "1 de dezembro às 09:45\n" +
                            "hoje as 08:30");
                }
        }
        return sendMessage.sendMessage(chatId, "Erro interno: estado desconhecido");
    }
}

