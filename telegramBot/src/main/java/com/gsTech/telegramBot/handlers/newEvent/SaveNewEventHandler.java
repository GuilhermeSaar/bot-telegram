package com.gsTech.telegramBot.handlers.newEvent;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserService;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class SaveNewEventHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private UserService userService;
    @Autowired
    private SendMessageFactory sendMessage;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.SAVE_EVENT.name().equals(update.getCallbackQuery().getData());

    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        EventDTO event = userEvent.getEvent(chatId);
        User user = userService.getOrCreateUserByChatId(chatId);
        eventService.newEvent(event, user);
        userState.clearUserState(chatId);

        return sendMessage.sendMessage(chatId, "Nova Tarefa Criada");
    }
}
