package com.gsTech.telegramBot.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Serviço responsável por gerenciar o estado atual do usuário durante a interação.
 *
 * Armazena e recupera estados temporários associados a cada chatId.
 */
@Service
public class UserStateService {

    private final Map<Long, String> userState = new HashMap<>();


    /**
     * Define o estado atual do usuário identificado pelo chatId.
     *
     * @param chatId identificador do chat do usuário
     * @param state estado a ser associado ao usuário
     */
    public void setUserState(Long chatId, String state) {
        userState.put(chatId, state);
    }


    /**
     * Obtém o estado atual do usuário pelo chatId.
     *
     * @param chatId identificador do chat do usuário
     * @return estado associado ao usuário ou null se não existir
     */
    public String getUserState(Long chatId) {
        return userState.get(chatId);
    }


    /**
     * Limpa o estado armazenado do usuário pelo chatId.
     *
     * @param chatId identificador do chat do usuário
     */
    public void clearUserState(Long chatId) {
        userState.remove(chatId);
    }
}
