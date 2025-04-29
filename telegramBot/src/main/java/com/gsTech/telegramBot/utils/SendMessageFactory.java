package com.gsTech.telegramBot.utils;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class SendMessageFactory {

    public SendMessage sendMessage(Long chatId, String text) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        return message;
    }

    public SendMessage sendMessageWithMenu(Long chatId, String text) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        // criar botoes
        InlineKeyboardButton button1 = new InlineKeyboardButton("Novo compromisso");
        button1.setCallbackData(CallbackAction.NEW_EVENT.name());

        InlineKeyboardButton button2 = new InlineKeyboardButton("Listar compromissos");
        button2.setCallbackData(CallbackAction.LIST_EVENTS.name());

        InlineKeyboardButton button3 = new InlineKeyboardButton("Editar");
        button3.setCallbackData(CallbackAction.EDIT.name());

        InlineKeyboardButton button4 = new InlineKeyboardButton("Excluir compromisso");
        button4.setCallbackData(CallbackAction.DELETE.name());

        InlineKeyboardButton button5 = new InlineKeyboardButton("Cancelar");
        button5.setCallbackData(CallbackAction.CANCEL.name());

        var row1 = List.of(button1);
        var row2 = List.of(button2);
        var row3 = List.of(button3);
        var row4 = List.of(button4);
        var row5 = List.of(button5);

        List<List<InlineKeyboardButton>> rows = List.of(row1, row2, row3, row4, row5);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        message.setReplyMarkup(markup);

        return message;
    }


    public SendMessage sendMessageEventDelete(Long chatId, List<EventDTO> events) {

        String header = "Escolha um compromisso para excluir:\n\n";
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();


        for (EventDTO event : events) {

            var deleteButton = new InlineKeyboardButton(" " + event.getEventName() + " ❌");
            deleteButton.setCallbackData(CallbackAction.DELETE_EVENT.name() + ":" + event.getId());

            rows.add(List.of(deleteButton));
        }

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(header);
        message.setReplyMarkup(markup);

        return message;
    }


    public EditMessageText editMessageEventList(Long chatId, Integer messageId, List<EventDTO> events) {

        String header = "Escolha um compromisso para excluir:\n\n";
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (EventDTO event : events) {

            var deleteButton = new InlineKeyboardButton(" " + event.getEventName() + " ❌");
            deleteButton.setCallbackData(CallbackAction.DELETE_EVENT.name() + event.getId());

            rows.add(List.of(deleteButton));
        }

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(header);
        message.setReplyMarkup(markup);
        message.setParseMode("HTML"); // Opcional, se usar formatação no texto

        return message;
    }

}
