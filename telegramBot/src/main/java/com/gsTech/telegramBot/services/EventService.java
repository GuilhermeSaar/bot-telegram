package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.orm.Event;
import com.gsTech.telegramBot.repositories.EventRepository;
import com.gsTech.telegramBot.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<EventDTO> findAll() {

        List<Event> events = eventRepository.findAll();
        return events.stream().map(EventDTO::new).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public EventDTO newEvent(EventDTO eventDTO) {
        Event event = new Event();

        event.setEventName(eventDTO.getEventName());
        event.setEventType(eventDTO.getEventType());
        event.setLocation(eventDTO.getLocation());
        event.setTime(eventDTO.getTime());

        event = eventRepository.save(event);
        return new EventDTO(event);
    }
}
