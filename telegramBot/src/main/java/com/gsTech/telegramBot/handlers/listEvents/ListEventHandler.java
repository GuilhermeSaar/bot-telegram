package com.gsTech.telegramBot.handlers.listEvents;


import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class ListEventHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;


    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.LIST_EVENTS.name().equals(update.getCallbackQuery().getData());
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

       Long chatId = update.getCallbackQuery().getMessage().getChatId();
       List<EventDTO> events = eventService.findAllByChatId(chatId);

       if(events.isEmpty()) {
           return sendMessage.sendMessage(chatId, "Nenhum compromisso encontrado");
       }

       StringBuilder response = new StringBuilder("Seus compromissos\n");
       events.forEach(event -> response.append("- ").append(event.toString()));

       return sendMessage.sendMessage(chatId, response.toString());
    }

}
