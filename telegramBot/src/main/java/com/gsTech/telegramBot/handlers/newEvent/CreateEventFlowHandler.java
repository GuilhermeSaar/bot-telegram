package com.gsTech.telegramBot.handlers.newEvent;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.orm.User;
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
public class CreateEventFlowHandler implements CommandHandler {

    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private SendMessageFactory sendMessage;
    @Autowired
    private DateParseService dateParseService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public boolean canHandle(Update update) {

        if (update.getMessage() == null || update.getMessage().getText() == null) return false;

        Long chatId = update.getMessage().getChat().getId();
        String state = userState.getUserState(chatId);
        return state != null && state.startsWith("WAITING_FOR_");

    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getMessage().getChat().getId();
        String messageText = update.getMessage().getText();
        return processCreateState(chatId, messageText);
    }


    private SendMessage processCreateState(Long chatId, String messageText) {


        String state = userState.getUserState(chatId);
        EventDTO event = userEvent.getEvent(chatId);
        User user = userService.getOrCreateUserByChatId(chatId);

        if (state == null || event == null) {
            return sendMessage.sendMessage(chatId, "Erro interno");
        }

        switch (state) {

            case "WAITING_FOR_NAME":
                event.setEventName(messageText);
                userState.setUserState(chatId, "WAITING_FOR_TYPE");
                return sendMessage.sendMessage(chatId, "Tipo de compromisso (Pessoal, Profisional...)");

            case "WAITING_FOR_TYPE":
                userState.setUserState(chatId, "WAITING_FOR_LOCATION");
                event.setEventType(messageText);
                return sendMessage.sendMessage(chatId, "Local do compromisso:");

            case "WAITING_FOR_LOCATION":
                userState.setUserState(chatId, "WAITING_FOR_DATE");
                event.setLocation(messageText);
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

                    eventService.newEvent(event, user);
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
        return sendMessage.sendMessage(chatId, "Compromisso criado com sucesso:\n" + event);
    }
}
