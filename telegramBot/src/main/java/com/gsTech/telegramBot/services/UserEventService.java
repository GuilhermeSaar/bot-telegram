package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.DTO.EventDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserEventService {

    private final Map<Long, EventDTO> userEvent = new ConcurrentHashMap<>();

    public void setUserEvent(Long chatId) {
        userEvent.put(chatId, new EventDTO());
    }

    public void setUserEvent(Long chatId, EventDTO event) {
        userEvent.put(chatId, event);
    }

    public String getEventName(Long chatId) {
        return userEvent.get(chatId).getEventName();
    }

    public void clearUserEvent(Long chatId) {
        userEvent.remove(chatId);
    }

    public EventDTO getEvent(Long chatId) {
        return userEvent.get(chatId);
    }
}
