package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.DTO.EventDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Serviço responsável por gerenciar os eventos temporários associados aos usuários durante a interação.
 *
 * Armazena e recupera objetos {@link EventDTO} associados a cada chatId.
 */
@Service
public class UserEventService {

    private final Map<Long, EventDTO> userEvent = new ConcurrentHashMap<>();


    /**
     * Inicializa o evento associado ao usuário com um novo {@link EventDTO} vazio.
     *
     * @param chatId identificador do chat do usuário
     */
    public void setUserEvent(Long chatId) {
        userEvent.put(chatId, new EventDTO());
    }


    /**
     * Define o evento associado ao usuário.
     *
     * @param chatId identificador do chat do usuário
     * @param event objeto {@link EventDTO} a ser associado
     */
    public void setUserEvent(Long chatId, EventDTO event) {
        userEvent.put(chatId, event);
    }


    /**
     * Obtém o nome do evento associado ao usuário.
     *
     * @param chatId identificador do chat do usuário
     * @return nome do evento ou null se nenhum evento estiver associado
     */
    public String getEventName(Long chatId) {
        return userEvent.get(chatId).getEventName();
    }


    /**
     * Remove o evento associado ao usuário.
     *
     * @param chatId identificador do chat do usuário
     */
    public void clearUserEvent(Long chatId) {
        userEvent.remove(chatId);
    }


    /**
     * Obtém o evento associado ao usuário.
     *
     * @param chatId identificador do chat do usuário
     * @return objeto {@link EventDTO} associado ou null se não existir
     */
    public EventDTO getEvent(Long chatId) {
        return userEvent.get(chatId);
    }
}
