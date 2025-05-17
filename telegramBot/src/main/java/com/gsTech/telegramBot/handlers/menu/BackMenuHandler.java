package com.gsTech.telegramBot.handlers.menu;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.UserStateService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import com.gsTech.telegramBot.utils.enums.CallbackAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Handler responsável por retornar o usuário ao menu principal.
 *
 * Esse handler responde a callback queries com a ação {@link CallbackAction#MENU},
 * limpa o estado do usuário e exibe o menu principal de interação do gerenciador de tarefas.
 */
@Component
public class BackMenuHandler implements CommandHandler {

    @Autowired
    private SendMessageFactory sendMessageFactory;
    @Autowired
    private UserStateService userState;


    /**
     * Verifica se o update recebido contém uma callback query
     * com a ação para voltar ao menu principal.
     *
     * @param update Update recebido do Telegram
     * @return true se o update contém a callback de menu principal; false caso contrário
     */
    @Override
    public boolean canHandle(Update update) {

        return update.hasCallbackQuery()
                && CallbackAction.MENU.name().equals(update.getCallbackQuery().getData());
    }


    /**
     * Processa o update, limpando o estado do usuário e exibindo o menu principal.
     *
     * @param update Update recebido do Telegram
     * @return BotApiMethod contendo a mensagem com o menu principal
     */
    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
        userState.clearUserState(chatId);
        return sendMessageFactory.editMessageWithMenu(chatId, "Menu de interação do gerenciador de tarefas", messageId);
    }
}
