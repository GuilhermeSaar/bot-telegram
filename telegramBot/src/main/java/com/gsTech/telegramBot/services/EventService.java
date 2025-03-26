package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.orm.Event;
import com.gsTech.telegramBot.repositories.EventRepository;
import com.gsTech.telegramBot.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public EventDTO findByEventId(Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResourceNotFoundException("Resource not found for ID: " + eventId));

        return new EventDTO(event);
    }
}
