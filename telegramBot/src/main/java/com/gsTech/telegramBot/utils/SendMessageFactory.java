package com.gsTech.telegramBot.utils;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import com.gsTech.telegramBot.utils.enums.CreateCallbackAction;
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

    public EditMessageText editMessageBackToMenu(Long chatId, String text, Integer messageId) {

        var message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(text);

        var backButton = new InlineKeyboardButton("\uD83D\uDD19 Voltar ao Menu");
        backButton.setCallbackData(CallbackAction.MENU.name());

        var row = List.of(backButton);

        List<List<InlineKeyboardButton>> rows = List.of(row);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        message.setReplyMarkup(markup);

        return message;
    }


    public SendMessage sendMessageBackToMenu(Long chatId, String text) {

        var message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        var backButton = new InlineKeyboardButton("\uD83D\uDD19 Voltar ao Menu");
        backButton.setCallbackData(CallbackAction.MENU.name());

        var row = List.of(backButton);

        List<List<InlineKeyboardButton>> rows = List.of(row);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        message.setReplyMarkup(markup);

        return message;
    }


    public EditMessageText editMessageWithMenu(Long chatId, String text, Integer messageId) {

        var message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(text);

        // criar botoes
        InlineKeyboardButton button1 = new InlineKeyboardButton("➕ Nova tarefa");
        button1.setCallbackData(CallbackAction.NEW_EVENT.name());

        InlineKeyboardButton button2 = new InlineKeyboardButton("\uD83D\uDCCB Listar tarefas");
        button2.setCallbackData(CallbackAction.LIST_EVENTS.name());

        InlineKeyboardButton button3 = new InlineKeyboardButton("✏ Editar");
        button3.setCallbackData(CallbackAction.EDIT.name());

        InlineKeyboardButton button4 = new InlineKeyboardButton("\uD83D\uDDD1\uFE0F Excluir tarefa");
        button4.setCallbackData(CallbackAction.DELETE.name());

        var row1 = List.of(button1);
        var row2 = List.of(button2);
        var row3 = List.of(button3);
        var row4 = List.of(button4);

        List<List<InlineKeyboardButton>> rows = List.of(row1, row2, row3, row4);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        message.setReplyMarkup(markup);

        return message;
    }

    public SendMessage sendMessageWithMenu(Long chatId, String text) {

        var message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        // criar botoes
        InlineKeyboardButton button1 = new InlineKeyboardButton("➕ Nova tarefa");
        button1.setCallbackData(CallbackAction.NEW_EVENT.name());

        InlineKeyboardButton button2 = new InlineKeyboardButton("\uD83D\uDCCB Listar tarefas");
        button2.setCallbackData(CallbackAction.LIST_EVENTS.name());

        InlineKeyboardButton button3 = new InlineKeyboardButton("✏ Editar");
        button3.setCallbackData(CallbackAction.EDIT.name());

        InlineKeyboardButton button4 = new InlineKeyboardButton("\uD83D\uDDD1\uFE0F Excluir tarefa");
        button4.setCallbackData(CallbackAction.DELETE.name());

        var row1 = List.of(button1);
        var row2 = List.of(button2);
        var row3 = List.of(button3);
        var row4 = List.of(button4);

        List<List<InlineKeyboardButton>> rows = List.of(row1, row2, row3, row4);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        message.setReplyMarkup(markup);

        return message;
    }

    public SendMessage sendMessageWithBackTAndSave(Long chatId, String text) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        var backButton = new InlineKeyboardButton("\uD83D\uDD19 Voltar ao Menu");
        backButton.setCallbackData(CallbackAction.MENU.name());

        var saveButton = new InlineKeyboardButton("\uD83D\uDCBE Salvar");
        saveButton.setCallbackData(CallbackAction.SAVE_EVENT.name());

        List<InlineKeyboardButton> row = List.of(backButton, saveButton);
        List<List<InlineKeyboardButton>> keyboard = List.of(row);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        message.setReplyMarkup(markup);

        return message;
    }

    public EditMessageText editMessageReturnBackMenu(Long chatId, String text, Integer messageId) {

        var message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(text);

        var backButton = new InlineKeyboardButton("\uD83D\uDD19 Voltar ao Menu");
        backButton.setCallbackData(CallbackAction.MENU.name());

        var row = List.of(backButton);

        List<List<InlineKeyboardButton>> rows = List.of(row);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        message.setReplyMarkup(markup);

        return message;
    }


    public EditMessageText editEventDelete(Long chatId, List<EventDTO> events, Integer messageId) {

        String header = "Selecione a tarefa:\n\n";
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();


        for (EventDTO event : events) {

            var deleteButton = new InlineKeyboardButton(" " + event.getEventName() + " ❌");
            deleteButton.setCallbackData(CallbackAction.DELETE_EVENT.name() + ":" + event.getId());

            rows.add(List.of(deleteButton));
        }

        var backButton = new InlineKeyboardButton("\uD83D\uDD19 Voltar ao Menu");
        backButton.setCallbackData(CallbackAction.MENU.name());
        var back = List.of(backButton);
        rows.add(back);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        var message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(header);
        message.setReplyMarkup(markup);

        return message;
    }


    // 1° editar
    public EditMessageText editMessageEditEvent(Long chatId, List<EventDTO> events, Integer messageId) {

        String header = "Escolha um compromisso para editar:\n\n";
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();


        for (EventDTO event : events) {

            var editButton = new InlineKeyboardButton(event.getEventName());
            editButton.setCallbackData(CallbackAction.EDIT_EVENT.name() + ":" + event.getId());

            rows.add(List.of(editButton));
        }

        var backButton = new InlineKeyboardButton("\uD83D\uDD19 Voltar ao Menu");
        backButton.setCallbackData(CallbackAction.MENU.name());
        var back = List.of(backButton);
        rows.add(back);

        var markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        var message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(header);
        message.setReplyMarkup(markup);

        return message;
    }

    // 2° menu
    public EditMessageText editMessageEditOptions(Long chatId, EventDTO event, Integer messageId) {

        var markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(buildButton("Editar nome", "EDIT_FIELD:NAME")));
        buttons.add(List.of(buildButton("Editar local", "EDIT_FIELD:LOCATION")));
        buttons.add(List.of(buildButton("Editar data", "EDIT_FIELD:DATE")));

        markup.setKeyboard(buttons);

        var message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText("Selecione um campo para editar:\n" + event);
        message.setReplyMarkup(markup);

        return message;
    }


    public EditMessageText editMessageEventList(Long chatId, Integer messageId, List<EventDTO> events) {

        String header = "Escolha um compromisso para editar:\n\n";
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (EventDTO event : events) {

            var deleteButton = new InlineKeyboardButton(" " + event.getEventName());
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

        return message;
    }

    private InlineKeyboardButton buildButton(String text, String callBackData) {

        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callBackData);
        return button;

    }

}
