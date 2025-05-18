package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


/**
 * Componente responsável por despachar atualizações (updates) recebidas
 * para o handler apropriado que pode processá-las.
 *
 * Recebe uma lista de handlers que implementam a interface {@link CommandHandler}
 * e encaminha o update para o primeiro handler que indicar capacidade de processá-lo.
 *
 * Caso nenhum handler consiga processar o update, envia uma mensagem padrão com o menu.
 */
@Component
public class CommandDispatcher {

    private final List<CommandHandler> handlers;
    private final SendMessageFactory sendMessageFactory;

    @Autowired
    private UserService userService;


    /**
     * Construtor do despachante de comandos.
     *
     * @param handlers lista de handlers disponíveis para processar comandos
     * @param sendMessageFactory fábrica para criação de mensagens do bot
     */
    public CommandDispatcher(List<CommandHandler> handlers, SendMessageFactory sendMessageFactory) {
        this.handlers = handlers;
        this.sendMessageFactory = sendMessageFactory;
    }

    /**
     * Recebe uma atualização do Telegram e a despacha para o handler adequado.
     *
     * @param update atualização recebida do Telegram
     * @return uma resposta para ser enviada ao usuário, ou null se não aplicável
     */
    public BotApiMethod<?> dispatch(Update update) {

        Long chatId = null;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        if (chatId != null) {
            userService.getOrCreateUserByChatId(chatId);
        }

        for (CommandHandler handler : handlers) {
            if (handler.canHandle(update)) {
                return handler.handle(update);
            }
        }
        return handleDefault(update);
    }


    /**
     * Método privado que lida com updates não reconhecidos por nenhum handler.
     * Retorna uma mensagem padrão com o menu de interação.
     *
     * @param update atualização recebida
     * @return mensagem padrão com menu para o usuário, ou null se não for possível determinar chatId
     */
    private BotApiMethod<?> handleDefault(Update update) {

        Long chatId = null;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        }
        else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        if (chatId != null) {
            return sendMessageFactory.sendMessageWithMenu(chatId, "Menu de interação do gerenciador de tarefas");
        }

        return null;
    }
}
