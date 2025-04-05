package com.gsTech.telegramBot.handler;

import com.gsTech.telegramBot.DTO.telegram.TelegramUpdate;
import com.gsTech.telegramBot.services.TelegramApiService;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class NewEventHandler implements CommandHandler {

    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private TelegramApiService telegramApiService;


    @Override
    public boolean canHandle(TelegramUpdate update) {
        if(update.getMessage() == null || update.getMessage().getText() == null) {
            return false;
        }

        return update.getMessage().getText().equalsIgnoreCase("/novo");
    }

    @Override
    public void handle(TelegramUpdate update) {

        Long chatId = update.getMessage().getChat().getId();
        startNewEvent(chatId);

    }

    private void startNewEvent(Long chatId) {

        userState.setUserState(chatId, "WAITING_FOR_NAME");
        userEvent.setUserEvent(chatId);
        telegramApiService.sendMessage(chatId, "Novo compromisso!\n\nNome do compromisso:");
    }
}
