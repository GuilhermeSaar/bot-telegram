package com.gsTech.telegramBot.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Interface para handlers de comandos que processam atualizações (updates) recebidas do Telegram.
 */
public interface CommandHandler {


    /**
     * Verifica se o handler pode processar o update recebido.
     *
     * @param update a atualização recebida do Telegram
     * @return true se o handler pode lidar com esse update, false caso contrário
     */
    boolean canHandle(Update update);


    /**
     * Processa o update recebido e retorna a ação que deve ser executada pelo bot.
     *
     * @param update a atualização recebida do Telegram
     * @return um objeto BotApiMethod que representa a resposta ou ação do bot
     */
    BotApiMethod<?> handle(Update update);
}
