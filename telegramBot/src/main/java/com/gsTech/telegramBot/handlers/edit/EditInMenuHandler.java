package com.gsTech.telegramBot.handlers.edit;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


/**
 * Handler que gerencia a ação de editar eventos a partir do menu.
 *
 * Este handler responde a callbacks do tipo EDIT e exibe a lista de eventos
 * disponíveis para edição para o usuário.
 */
@Component
public class EditInMenuHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessageFactory;


    /**
     * Verifica se este handler pode processar o update recebido.
     * Retorna true se o update contiver uma callback com a ação EDIT.
     *
     * @param update objeto com a atualização recebida do Telegram
     * @return true se o handler deve processar este update, false caso contrário
     */
    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.EDIT.name().equals(update.getCallbackQuery().getData());
    }


    /**
     * Processa o update e retorna uma mensagem para o usuário.
     * Se não houver eventos para editar, informa o usuário.
     * Caso contrário, apresenta a lista de eventos para edição.
     *
     * @param update objeto com a atualização recebida do Telegram
     * @return mensagem do bot para o usuário
     */
    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        List<EventDTO> events = eventService.findAllByChatId(chatId);

        return sendMessageFactory.editMessageEditEvent(chatId, events, messageId);
    }
}
