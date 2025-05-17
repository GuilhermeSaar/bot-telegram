package com.gsTech.telegramBot.handlers.delete;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


/**
 * Handler responsável por exibir o menu de exclusão de eventos/tarefas.
 *
 * Este handler verifica se a callback recebida corresponde à ação de exclusão e,
 * caso afirmativo, busca a lista de eventos do usuário para exibição.
 * Se não houver eventos, informa que não há tarefas para excluir.
 */
@Service
public class DeleteMenuHadler implements CommandHandler {


    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;


    /**
     * Verifica se o update recebido pode ser tratado por este handler.
     * @param update o objeto Update recebido da API do Telegram.
     * @return true se o update possuir uma callback com ação DELETE, false caso contrário.
     */
    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.DELETE.name().equals(update.getCallbackQuery().getData());
    }


    /**
     * Trata o update de exclusão mostrando a lista de eventos para o usuário excluir.
     * @param update o objeto Update recebido da API do Telegram.
     * @return um BotApiMethod com a mensagem ou menu de exclusão.
     */
    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        List<EventDTO> events = eventService.findAllByChatId(chatId);

        if (events.isEmpty()) {
            return sendMessage.editMessageBackToMenu(chatId, "📭 Nenhuma tarefa para excluir.", messageId);
        }

        return sendMessage.editEventDelete(chatId, events, messageId);
    }
}
