package com.gsTech.telegramBot.handlers.edit;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Handler responsável por iniciar o fluxo de edição de uma tarefa/evento.
 *
 * Este handler processa callbacks que começam com "EDIT_EVENT:" seguidos do ID do evento.
 * Busca o evento pelo ID, define o evento e o estado do usuário para edição,
 * e envia as opções de campos que podem ser editados.
 */
@Component
public class EditEventHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;
    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;



    /**
     * Verifica se o handler pode tratar o update.
     * Retorna true se a callback tiver dados iniciando com "EDIT_EVENT:".
     *
     * @param update atualização recebida do Telegram
     * @return true se o handler pode processar o update, false caso contrário
     */
    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && update.getCallbackQuery().getData().startsWith(CallbackAction.EDIT_EVENT.name() + ":");

    }


    /**
     * Trata o update para iniciar o fluxo de edição da tarefa.
     * Verifica e extrai o ID do evento, busca o evento e define o estado para seleção do campo a editar.
     * Retorna mensagem com opções de edição ou mensagens de erro apropriadas.
     *
     * @param update atualização recebida do Telegram
     * @return mensagem do bot com opções para editar ou mensagem de erro
     */
    @Override
    public BotApiMethod<?> handle(Update update) {

        String callBackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        try {
            String[] parts = callBackData.split(":");
            if (parts.length < 2) {
                return sendMessage.sendMessage(chatId, "Erro ao editar: dados incompletos");
            }

            Long eventId = Long.parseLong(parts[1]);
            EventDTO event = eventService.findByEventId(eventId);

            if (event == null) {
                return sendMessage.sendMessage(chatId, "Tarefa não encontrada.");
            }

            userEvent.setUserEvent(chatId, event);
            userState.setUserState(chatId, "EDIT_SELECT_FIELD");

            return sendMessage.editMessageEditOptions(chatId, event, messageId);


        } catch (NumberFormatException e) {
            return sendMessage.sendMessage(chatId, "Erro ao editar: ID invalido");
        }
        catch (Exception e) {
            return sendMessage.sendMessage(chatId, "Ocorreu um erro ao tentar editar");

        }

    }
}
