package com.gsTech.telegramBot.handlers.delete;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


/**
 * Handler responsável por excluir um evento/tarefa a partir de uma callback.
 *
 * Verifica se a callback possui ação DELETE_EVENT com ID do evento.
 * Tenta excluir o evento pelo ID e atualiza a lista de eventos exibida.
 */
@Component
public class deleteEventHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;


    /**
     * Verifica se o update recebido pode ser tratado por este handler.
     * @param update o objeto Update recebido da API do Telegram.
     * @return true se o update possuir callback com ação DELETE_EVENT, false caso contrário.
     */
    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && update.getCallbackQuery().getData().startsWith(CallbackAction.DELETE_EVENT.name() + ":");

    }


    /**
     * Trata o update de exclusão, excluindo o evento correspondente.
     * @param update o objeto Update recebido da API do Telegram.
     * @return um BotApiMethod com a lista atualizada de eventos ou mensagem de erro.
     */
    @Override
    public BotApiMethod<?> handle(Update update) {

        String callBackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        try {
            String[] parts = callBackData.split(":");
            if(parts.length < 2) {
                return sendMessage.sendMessage(chatId, "Erro ao excluir: dados incompletos");
            }

            Long eventId = Long.parseLong(callBackData.split(":")[1]);
            eventService.delete(eventId);

            List<EventDTO> events = eventService.findAllByChatId(chatId);

            return sendMessage.editEventDelete(chatId,events, messageId);

        } catch (NumberFormatException e) {
            return sendMessage.sendMessage(chatId, "Erro ao excluir: ID invalido");
        } catch (Exception e) {
            return sendMessage.sendMessage(chatId, "Ocorreu um erro ao tentar excluir");
        }
    }
}
