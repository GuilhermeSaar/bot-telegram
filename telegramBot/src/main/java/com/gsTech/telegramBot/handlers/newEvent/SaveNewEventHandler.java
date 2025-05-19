package com.gsTech.telegramBot.handlers.newEvent;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.services.UserEventService;
import com.gsTech.telegramBot.services.UserService;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Manipulador responsável por salvar um novo evento quando o usuário confirma a criação
 * através do botão com callback {@code SAVE_EVENT}.
 *
 * <p>Fluxo:</p>
 * <ul>
 *   <li>Recupera o evento temporário armazenado para o usuário.</li>
 *   <li>Garante a existência do usuário no sistema.</li>
 *   <li>Salva o novo evento definitivo no banco de dados.</li>
 *   <li>Limpa o estado temporário do usuário.</li>
 *   <li>Atualiza a mensagem com uma confirmação e retorna ao menu principal.</li>
 * </ul>
 */
@Component
public class SaveNewEventHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private UserService userService;
    @Autowired
    private SendMessageFactory sendMessage;


    /**
     * Verifica se este handler pode lidar com o update recebido.
     * Este handler responde apenas a callbacks com a ação {@code SAVE_EVENT}.
     *
     * @param update Objeto do Telegram contendo a atualização.
     * @return {@code true} se o callback corresponde a {@code SAVE_EVENT}, senão {@code false}.
     */
    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && CallbackAction.SAVE_EVENT.name().equals(update.getCallbackQuery().getData());

    }


    /**
     * Processa a criação de um novo evento:
     * - Recupera o evento em preparação
     * - Salva o evento com o usuário associado
     * - Limpa o estado temporário
     * - Retorna uma mensagem de confirmação com botão de voltar ao menu
     *
     * @param update Objeto do Telegram com o callback da ação.
     * @return Mensagem editada confirmando a criação do evento.
     */
    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        EventDTO event = userEvent.getEvent(chatId);

        eventService.newEvent(event, chatId);
        userState.clearUserState(chatId);

        return sendMessage.editMessageReturnBackMenu(chatId, "Nova Tarefa Criada", messageId);
    }
}
