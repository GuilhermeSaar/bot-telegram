package com.gsTech.telegramBot.handlers.edit;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.DateParseService;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
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
public class EditEventFlowHandler implements CommandHandler {

    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private EventService eventService;
    @Autowired
    private DateParseService dateParseService;
    @Autowired
    private SendMessageFactory sendMessage;


    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    @Override
    public boolean canHandle(Update update) {

        if (update.getMessage() == null || update.getMessage().getText() == null) {
            return false;
        }

        Long chatId = update.getMessage().getChat().getId();
        String state = userState.getUserState(chatId);
        return state != null && state.startsWith("EDIT_WAITING_FOR");
    }


    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getMessage().getChat().getId();
        String messageText = update.getMessage().getText();
        return processEditState(chatId, messageText);
    }

    private SendMessage processEditState(Long chatId, String messageText) {

        String state = userState.getUserState(chatId);
        EventDTO event = userEvent.getEvent(chatId);

        if (state == null || event == null) {

            return sendMessage.sendMessage(chatId, "Erro: evento não encontrado para edição.");
        }

        switch (state) {

            case "EDIT_WAITING_FOR_NAME":
                event.setEventName(messageText);
                break;

            case "EDIT_WAITING_FOR_TYPE":
                event.setEventType(messageText);
                break;

            case "EDIT_WAITING_FOR_LOCATION":
                event.setLocation(messageText);
                break;

            case "EDIT_WAITING_FOR_DATE":
                try {
                    String dateRegex = dateParseService.parseDate(messageText);
                    var date = LocalDateTime.parse(dateRegex, DATE_TIME_FORMATTER);
                    event.setTime(date);
                } catch (DateTimeParseException e) {
                    return sendMessage.sendMessage(chatId, "Data inválida. Tente algo como:\n14/09/2025 19:30");
                }
                break;

            default:
                return sendMessage.sendMessage(chatId, "Estado de edição desconhecido.");
        }

        eventService.update(event);
        userState.clearUserState(chatId);

        return sendMessage.sendMessage(chatId, "Compromisso atualizado com sucesso:\n" + event);

    }
}
