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


    /**
     * Cria um objeto {@link SendMessage} com o texto especificado e o chatId do destinatário.
     * @param chatId o id do chat para onde a mensagem será enviada.
     * @param text mensagem a ser enviada
     * @return um objeto {@link SendMessage} pronto para envio
     */
    public SendMessage sendMessage(Long chatId, String text) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        return message;
    }


    /**
     * Cria um objeto {@link EditMessageText} para editar uma mensagem existente e adiciona um botão
     * de "voltar ao menu".
     * @param chatId ID do chat onde a mensagem sera editada.
     * @param text Novo conteúdo da mensagem
     * @param messageId ID da mensagem que sera editada
     * @return um objeto {@link EditMessageText} com o texto atualizado e um botão de retorno ao menu.
     */
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


    /**
     * Cria uma nova mensagem do tipo {@link SendMessage} com um botão de "voltar ao menu".
     * @param chatId ID do chat onde a mensagem será enviada.
     * @param text mensagem a ser enviada.
     * @return um objeto {@link SendMessage} com o texto e um botao de retorno ao menu
     */
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


    /**
     * Cria um objeto {@link EditMessageText} para editar uma mensagem existente, atualizando seu conteúdo e adicionando
     * um menu de opções para interação do usuário.
     * <p>
     * O menu inclui botões para:
     * <ul>
     *   <li>➕ Nova tarefa</li>
     *   <li>📋 Listar tarefas</li>
     *   <li>✏ Editar tarefa</li>
     *   <li>🗑️ Excluir tarefa</li>
     * </ul>
     *
     * @param chatId  ID do chat no Telegram onde a mensagem será editada.
     * @param text    novo conteúdo da mensagem.
     * @param messageId ID da mensagem que será editada.
     * @return Um objeto {@link EditMessageText} contendo o texto atualizado
     *         e o teclado inline com as opções de menu.
     */
    public EditMessageText editMessageWithMenu(Long chatId, String text, Integer messageId) {

        var message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText(text);


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


    /**
     * Cria uma mensagem do tipo {@link SendMessage} com um menu de botões inline.
     * <p>
     * O menu inclui os seguintes botões:
     * <ul>
     *   <li>➕ Nova tarefa</li>
     *   <li>📋 Listar tarefas</li>
     *   <li>✏ Editar</li>
     *   <li>🗑️ Excluir tarefa</li>
     * </ul>
     *
     * @param chatId  identificador do chat para onde a mensagem será enviada
     * @param text  texto que será exibido na mensagem
     * @return um objeto {@link SendMessage} com o texto e o menu de botões configurados
     */
    public SendMessage sendMessageWithMenu(Long chatId, String text) {

        var message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

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


    /**
     * Cria uma mensagem do tipo {@link SendMessage} com botões inline para
     * voltar ao menu e salvar.
     * <p>
     * Os botões disponíveis são:
     * <ul>
     *   <li>🔙 Voltar ao Menu</li>
     *   <li>💾 Salvar</li>
     * </ul>
     *
     * @param chatId o identificador do chat para onde a mensagem será enviada
     * @param text o texto que será exibido na mensagem
     * @return um objeto {@link SendMessage} contendo o texto e os botões configurados
     */
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


    /**
     * Cria um objeto {@link EditMessageText} para editar uma mensagem existente
     * e adiciona um botão inline para "Voltar ao Menu".
     * <p>
     * botão:
     * <ul>
     *   <li>🔙 Voltar ao Menu</li>
     * </ul>
     *
     * @param chatId ID do chat onde a mensagem será editada
     * @param text  novo texto que substituirá o conteúdo da mensagem
     * @param messageId ID da mensagem que será editada
     * @return um objeto {@link EditMessageText} com o texto atualizado e botão configurado
     */
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


    /**
     * Cria um objeto {@link EditMessageText} para editar uma mensagem existente, que exibe uma lista de tarefas
     * com botões para deletar cada tarefa, além de um botão para voltar ao menu.
     * <p>
     * Cada botão contém o nome da tarefa seguido de um ícone de exclusão (❌)
     * e envia um callback com a ação DELETE_EVENT e o ID da tarefa.
     * <p>
     * Ao final da lista, é adicionado um botão para voltar ao menu principal.
     *
     * @param chatId  ID do chat onde a mensagem será editada
     * @param events lista de eventos/tarefas que serão exibidos para seleção
     * @param messageId identificador da mensagem que será editada
     * @return um objeto {@link EditMessageText} com o texto e teclado inline configurados
     */
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


    /**
     * Cria um objeto {@link EditMessageText} para editar uma mensagem existente, que exibe uma lista de tarefas
     * com botões para editar cada um, além de um botão para voltar ao menu.
     * <p>
     * Cada botão representa uma tarefa e envia um callback com a ação EDIT_EVENT e o ID do compromisso.
     * <p>
     *
     * @param chatId o identificador do chat onde a mensagem será editada
     * @param events a lista de compromissos que serão exibidos para seleção
     * @param messageId o identificador da mensagem que será editada
     * @return um objeto de {@link EditMessageText} com o texto e teclado inline configurados
     */
    public EditMessageText editMessageEditEvent(Long chatId, List<EventDTO> events, Integer messageId) {

        String header = "Escolha uma tarefa para editar:\n\n";
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


    /**
     * Cria um objeto {@link EditMessageText} para editar uma mensagem existente, exibindo opções de edição
     * para os campos de um compromisso específico, com botões para editar nome, descrição e data.
     * <p>
     * Cada botão envia um callback com a ação correspondente para editar o campo selecionado.
     *
     * @param chatId o identificador do chat onde a mensagem será editada
     * @param event o compromisso cujos campos poderão ser editados
     * @param messageId o identificador da mensagem que será editada
     * @return um obejeto {@link EditMessageText} com o texto e teclado inline configurados
     */
    public EditMessageText editMessageEditOptions(Long chatId, EventDTO event, Integer messageId) {

        var markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(buildButton("Editar nome", "EDIT_FIELD:NAME")));
        buttons.add(List.of(buildButton("Editar descrição", "EDIT_FIELD:DESCRIPTION")));
        buttons.add(List.of(buildButton("Editar data", "EDIT_FIELD:DATE")));

        markup.setKeyboard(buttons);

        var message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(messageId);
        message.setText("Selecione um campo para editar:\n" + event);
        message.setReplyMarkup(markup);

        return message;
    }


    /**
     * Cria um botão inline para o teclado do Telegram com texto e callback personalizados.
     *
     * @param text o texto exibido no botão
     * @param callBackData o dado enviado no callback quando o botão é pressionado
     * @return um objeto {@link InlineKeyboardButton} configurado com o texto e callback informados
     */
    private InlineKeyboardButton buildButton(String text, String callBackData) {

        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callBackData);
        return button;
    }

}
