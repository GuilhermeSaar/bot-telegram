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

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public EventDTO findByEventId(Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Resource not found for ID: " + eventId));

        return new EventDTO(event);
    }

    @Transactional(readOnly = true)
    public List<EventDTO> findAllByChatId(Long chatId) {

        User user = userRepository.findByChatId(chatId).orElseThrow(() ->
                new ResourceNotFoundException("Usuário não encontrado."));

        List<Event> listEvent = eventRepository.findAllByUserId(user.getId());

        return listEvent.stream().map(EventDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public EventDTO saveNewEvent(EventDTO DTO, User user) {

        Event event = fromDTO(DTO, user);
        event = eventRepository.save(event);

        return new EventDTO(event);
    }


    private Event fromDTO(EventDTO dto, User user) {

        Event event = new Event();
        event.setEventName(dto.getEventName());
        event.setEventType(dto.getEventType());
        event.setLocation(dto.getLocation());
        event.setTime(dto.getTime());
        event.setUser(user);
        return event;
    }

}
