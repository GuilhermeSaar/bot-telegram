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
                new ResourceNotFoundException("Resource not found"));

        List<Event> listEvent = eventRepository.findAllByUserId(user.getId());

        return listEvent.stream().map(EventDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public void newEvent(EventDTO dto, User user) {

        var event = new Event();

        event.setEventName(dto.getEventName());
        event.setDescription(dto.getDescription());
        event.setTime(dto.getTime());
        event.setUser(user);

        eventRepository.save(event);
    }

    public void delete(Long id) {

        Event event = eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event not found")
        );
        eventRepository.delete(event);
    }


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
