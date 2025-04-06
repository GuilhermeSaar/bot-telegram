package com.gsTech.telegramBot.handler;


import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.TelegramApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListEvent implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private TelegramApiService telegramApiService;



    @Override
    public boolean canHandle(TelegramUpdate update) {

        if(update.getMessage() == null || update.getMessage().getText() == null) return false;

        return update.getMessage().getText().equalsIgnoreCase("/eventos");
    }

    @Override
    public void handle(TelegramUpdate update) {

        Long chatId = update.getMessage().getChat().getId();

        List<EventDTO> events = eventService.findAll();

        for(EventDTO event : events) {
            telegramApiService.sendMessage(chatId, event.toString());
        }
    }
}
