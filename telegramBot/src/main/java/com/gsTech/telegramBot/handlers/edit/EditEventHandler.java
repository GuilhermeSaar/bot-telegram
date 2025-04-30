package com.gsTech.telegramBot.handlers.edit;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class EditEventHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;
    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;


    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && update.getCallbackQuery().getData().startsWith(CallbackAction.EDIT_EVENT.name() + ":");

    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        String callBackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        try {
            String[] parts = callBackData.split(":");
            if (parts.length < 2) {
                return sendMessage.sendMessage(chatId, "Erro ao excluir: dados incompletos");
            }

            Long eventId = Long.parseLong(parts[1]);
            EventDTO event = eventService.findByEventId(eventId);

            if (event == null) {
                return sendMessage.sendMessage(chatId, "Compromisso não encontrado.");
            }

            userEvent.setUserEvent(chatId, event);
            userState.setUserState(chatId, "EDIT_SELECT_FIELD");

            return sendMessage.sendMessageEditOptions(chatId, event);


        } catch (NumberFormatException e) {
            return sendMessage.sendMessage(chatId, "Erro ao excluir: ID invalido");
        }
        catch (Exception e) {
            return sendMessage.sendMessage(chatId, "Ocorreu um erro ao tentar excluir");

        }

    }
}
