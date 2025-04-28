package com.gsTech.telegramBot.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
        button1.setCallbackData("NEW_EVENT");

        InlineKeyboardButton button2 = new InlineKeyboardButton("Listar compromissos");
        button2.setCallbackData("LIST_EVENTS");

        InlineKeyboardButton button3 = new InlineKeyboardButton("Editar");
        button3.setCallbackData("EDIT");

        InlineKeyboardButton button4 = new InlineKeyboardButton("Excluir compromisso");
        button4.setCallbackData("DELETE");

        InlineKeyboardButton button5 = new InlineKeyboardButton("Cancelar");
        button5.setCallbackData("CANCEL");

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

}
