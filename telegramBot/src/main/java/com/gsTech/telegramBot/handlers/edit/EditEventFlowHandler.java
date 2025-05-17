package com.gsTech.telegramBot.handlers.edit;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.DateParseService;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Handler responsável pelo fluxo de edição de um evento/tarefa.
 *
 * Processa as mensagens do usuário quando ele está no estado de edição,
 * atualizando o evento conforme o campo que está sendo editado (nome, descrição, data).
 *
 * Valida o formato da data e atualiza o evento via EventService.
 */
@Component
public class EditEventFlowHandler implements CommandHandler {

    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private EventService eventService;
    @Autowired
    private DateParseService dateParseService;
    @Autowired
    private SendMessageFactory sendMessage;

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    /**
     * Verifica se o handler pode tratar o update.
     * Retorna true se a mensagem do usuário está em um estado de edição esperado.
     *
     * @param update atualização recebida do Telegram
     * @return true se pode processar a edição, false caso contrário
     */
    @Override
    public boolean canHandle(Update update) {

        if (update.getMessage() == null || update.getMessage().getText() == null) {
            return false;
        }

        Long chatId = update.getMessage().getChat().getId();
        String state = userState.getUserState(chatId);
        return state != null && state.startsWith("EDIT_WAITING_FOR");
    }


    /**
     * Processa a mensagem do usuário e atualiza o evento conforme o estado de edição.
     *
     * @param update atualização recebida do Telegram
     * @return resposta do bot ao usuário
     */
    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getMessage().getChat().getId();
        String messageText = update.getMessage().getText();
        return processEditState(chatId, messageText);
    }


    /**
     * Lógica para atualizar o campo correto do evento conforme o estado do usuário.
     *
     * @param chatId id do chat do usuário
     * @param messageText texto enviado pelo usuário
     * @return mensagem do bot com confirmação ou erro
     */
    private BotApiMethod<?> processEditState(Long chatId, String messageText) {

        String state = userState.getUserState(chatId);
        EventDTO event = userEvent.getEvent(chatId);

        if (state == null || event == null) {

            return sendMessage.sendMessage(chatId, "Erro: evento não encontrado para edição.");
        }

        switch (state) {

            case "EDIT_WAITING_FOR_NAME":
                event.setEventName(messageText);
                break;


            case "EDIT_WAITING_FOR_DESCRIPTION":
                event.setDescription(messageText);
                break;

            case "EDIT_WAITING_FOR_DATE":
                try {
                    String dateRegex = dateParseService.parseDate(messageText);
                    var date = LocalDateTime.parse(dateRegex, DATE_TIME_FORMATTER);
                    event.setTime(date);
                } catch (DateTimeParseException e) {
                    return sendMessage.sendMessage(chatId, "Data inválida. Tente algo como:\n14/09/2025 19:30");
                }
                break;

            default:
                return sendMessage.sendMessage(chatId, "Estado de edição desconhecido.");
        }

        eventService.update(event);
        userState.clearUserState(chatId);

        return sendMessage.sendMessageBackToMenu(chatId, event.toString());
    }
}
