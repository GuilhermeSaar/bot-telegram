package com.gsTech.telegramBot.controllers;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    private EventService eventService;

    EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<EventDTO> findByEventId(@PathVariable Long id) {

        EventDTO eventDTO = eventService.findByEventId(id);
        return ResponseEntity.ok().body(eventDTO);
    }

}
