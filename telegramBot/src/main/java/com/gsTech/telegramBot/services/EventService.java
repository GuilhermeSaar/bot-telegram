package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.orm.Event;
import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.repositories.EventRepository;
import com.gsTech.telegramBot.repositories.UserRepository;
import com.gsTech.telegramBot.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Serviço responsável pela lógica de negócio relacionada a eventos.
 *
 * Fornece métodos para criar, atualizar, buscar e deletar eventos associados a usuários.
 */
@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;


    /**
     * Busca um evento pelo seu ID.
     *
     * @param eventId ID do evento a ser buscado
     * @return DTO representando o evento encontrado
     * @throws ResourceNotFoundException se o evento não for encontrado
     */
    @Transactional(readOnly = true)
    public EventDTO findByEventId(Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Resource not found for ID: " + eventId));

        return new EventDTO(event);
    }


    /**
     * Busca todos os eventos relacionados a um usuário identificado pelo chatId.
     *
     * @param chatId ID do chat do usuário
     * @return lista de DTOs dos eventos encontrados
     * @throws ResourceNotFoundException se o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<EventDTO> findAllByChatId(Long chatId) {

        User user = userRepository.findByChatId(chatId).orElseThrow(() ->
                new ResourceNotFoundException("Resource not found"));

        List<Event> listEvent = eventRepository.findAllByUserId(user.getId());

        return listEvent.stream().map(EventDTO::new).collect(Collectors.toList());
    }


    /**
     * Cria um novo evento para um usuário.
     *
     * @param dto  dados do evento a serem salvos
     * @param user usuário ao qual o evento estará associado
     */
    @Transactional
    public void newEvent(EventDTO dto, User user) {

        var event = new Event();

        event.setEventName(dto.getEventName());
        event.setDescription(dto.getDescription());
        event.setTime(dto.getTime());
        event.setUser(user);

        eventRepository.save(event);
    }


    /**
     * Exclui um evento pelo seu ID.
     *
     * @param id ID do evento a ser excluído
     * @throws ResourceNotFoundException se o evento não for encontrado
     */
    public void delete(Long id) {

        Event event = eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event not found")
        );
        eventRepository.delete(event);
    }


    /**
     * Atualiza um evento existente.
     *
     * @param dto dados do evento atualizados
     * @throws ResourceNotFoundException se o evento não for encontrado
     */
    public void update(EventDTO dto) {

        Event event = eventRepository.findById(dto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Event not found")
        );

        event.setEventName(dto.getEventName());
        event.setDescription(dto.getDescription());
        event.setTime(dto.getTime());

        eventRepository.save(event);
    }
}
