package com.gsTech.telegramBot.handlers.edit;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EditFieldSelectionHandler implements CommandHandler {

    @Autowired
    private UserStateService userState;
    @Autowired
    private SendMessageFactory sendMessage;


    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().startsWith("EDIT_FIELD:");
    }


    @Override
    public BotApiMethod<?> handle(Update update) {

        String callbackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        String fieldToEdit = callbackData.split(":")[1];

        String nextState = switch (fieldToEdit) {

            case "NAME" -> "EDIT_WAITING_FOR_NAME";
            case "DESCRIPTION" -> "EDIT_WAITING_FOR_DESCRIPTION";
            case "DATE" -> "EDIT_WAITING_FOR_DATE";
            default -> null;

        };

        if (nextState == null) {
            return sendMessage.sendMessage(chatId, "Campo invalido para edicao.");
        }

        userState.setUserState(chatId, nextState);

        String fieldQuestion = switch (fieldToEdit) {

            case "NAME" -> "Digite o novo nome da tarefa:";
            case "DESCRIPTION" -> "Digite a descrição da tarefa:";
            case "DATE" -> "Digite a nova data da tarefa:\nEx: 14/09/2025 19:30";
            default -> "Digite o novo valor:";

        };

        return sendMessage.sendMessage(chatId, fieldQuestion);
    }
}
