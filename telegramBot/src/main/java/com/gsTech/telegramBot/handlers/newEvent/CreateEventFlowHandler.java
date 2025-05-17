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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

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

    


    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public boolean canHandle(Update update) {

        if (update.getMessage() == null || update.getMessage().getText() == null) return false;

        Long chatId = update.getMessage().getChat().getId();
        String state = userState.getUserState(chatId);

        System.out.println("🔍 Verificando estado: " + state + " para chatId: " + chatId);

        return state != null && state.startsWith("WAITING_FOR_");

    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getMessage().getChat().getId();
        String messageText = update.getMessage().getText();

        return processCreateState(chatId, messageText);
    }


    private BotApiMethod<?> processCreateState(Long chatId, String messageText) {


        String state = userState.getUserState(chatId);
        EventDTO event = userEvent.getEvent(chatId);

        if (state == null || event == null) {
            return sendMessage.sendMessage(chatId, "Erro interno");
        }

        switch (state) {

            case "WAITING_FOR_NAME":
                userState.setUserState(chatId, "WAITING_FOR_DESCRIPTION");
                event.setEventName(messageText);
                return sendMessage.sendMessage(chatId, "Descrição da tarefa: ");

            case "WAITING_FOR_DESCRIPTION":
                userState.setUserState(chatId, "WAITING_FOR_DATE");
                event.setDescription(messageText);
                return sendMessage.sendMessage(chatId, "Data da tarefa: \n" +
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

                    return sendMessage.sendMessageWithBackTAndSave(chatId, event.toString());

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
        return sendMessage.sendMessage(chatId, "Tarefa criada com sucesso:\n" + event);
    }
}
