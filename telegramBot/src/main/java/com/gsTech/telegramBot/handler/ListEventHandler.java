package com.gsTech.telegramBot.handler;


import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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

        if(update.getMessage() == null || update.getMessage().getText() == null) return false;

        return update.getMessage().getText().equalsIgnoreCase("/agenda");
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getMessage().getChat().getId();

        List<EventDTO> events = eventService.findAllByChatId(chatId);

        StringBuilder eventList = new StringBuilder("Aqui estão seus compromissos\n");

        for(EventDTO event : events) {
            eventList.append(event.toString()).append("\n");
        }

        return sendMessage.sendMessage(chatId, eventList.toString());
    }
}
