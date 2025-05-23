package com.gsTech.telegramBot.handlers.newEvent;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Manipulador responsável por iniciar o fluxo de criação de uma nova tarefa quando o usuário
 * seleciona a opção no menu (callback {@code NEW_EVENT}).
 *
 * <p>Fluxo:</p>
 * <ul>
 *   <li>Verifica se o callback recebido é {@code NEW_EVENT}.</li>
 *   <li>Define o estado do usuário como aguardando o nome da tarefa.</li>
 *   <li>Inicializa o evento temporário associado ao usuário.</li>
 *   <li>Envia uma mensagem solicitando o nome da nova tarefa.</li>
 * </ul>
 */
@Component
public class CreateInMenuHandler implements CommandHandler {


    @Autowired
    private SendMessageFactory sendMessageFactory;
    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;


    /**
     * Verifica se este handler deve processar a atualização recebida.
     * Este handler responde apenas a callbacks com a ação {@code NEW_EVENT}.
     *
     * @param update Objeto de atualização do Telegram.
     * @return {@code true} se o callback for {@code NEW_EVENT}, senão {@code false}.
     */
    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.NEW_EVENT.name().equals(update.getCallbackQuery().getData());
    }


    /**
     * Inicia o processo de criação de uma nova tarefa, chamando o método {@code startNewEvent}.
     *
     * @param update Objeto de atualização do Telegram contendo o callback.
     * @return Mensagem solicitando o nome da nova tarefa.
     */
    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        return startNewEvent(chatId, messageId);
    }


    /**
     * Define o estado do usuário como aguardando o nome da tarefa e inicializa o evento temporário.
     *
     * @param chatId ID do chat do usuário que iniciou a criação da tarefa.
     * @return Mensagem solicitando o nome da nova tarefa.
     */
    private EditMessageText startNewEvent(Long chatId, Integer messageId) {

        userState.setUserState(chatId, "WAITING_FOR_NAME");
        userEvent.setUserEvent(chatId);
        return sendMessageFactory.editMessageWithReturn(chatId, messageId,"Nova tarefa!\n\nNome da tarefa:");
    }
}
