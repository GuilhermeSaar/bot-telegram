package com.gsTech.telegramBot.handlers.listEvents;


import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


/**
 * Handler para listar eventos do usuário.
 *
 * Esse handler responde a callback queries com a ação {@link CallbackAction#LIST_EVENTS},
 * buscando todos os eventos associados ao chat do usuário e enviando uma lista formatada.
 */
@Component
public class ListEventHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;

    /**
     * Verifica se o update recebido contém uma callback query
     * com a ação de listar eventos.
     *
     * @param update Update recebido do Telegram
     * @return true se o update contém a callback de listar eventos; false caso contrário
     */
    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.LIST_EVENTS.name().equals(update.getCallbackQuery().getData());
    }


    /**
     * Processa o update, buscando os eventos do usuário e montando a resposta.
     * Caso não haja eventos, retorna mensagem informando.
     *
     * @param update Update recebido do Telegram
     * @return BotApiMethod contendo a mensagem com a lista de eventos ou mensagem informando ausência deles
     */
    @Override
    public BotApiMethod<?> handle(Update update) {

       Long chatId = update.getCallbackQuery().getMessage().getChatId();
       Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

       List<EventDTO> events = eventService.findAllByChatId(chatId);

       if(events.isEmpty()) {
           return sendMessage.sendMessage(chatId, "Nenhuma tarefa encontrada");
       }

       StringBuilder response = new StringBuilder("\uD83D\uDCC5 *Suas tarefas:*\n\n");
       for(EventDTO event : events) {
           response.append("• ").append(event.toString()).append("\n");
       }

       return sendMessage.editMessageReturnBackMenu(chatId, response.toString(), messageId);
    }
}
