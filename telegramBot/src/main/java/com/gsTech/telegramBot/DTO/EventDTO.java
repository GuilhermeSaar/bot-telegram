package com.gsTech.telegramBot.DTO;

import com.gsTech.telegramBot.orm.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDTO {

    private Long id;
    private String eventName;
    private String description;
    private LocalDateTime time;
    private Long chatId;

    public EventDTO() {
    }

    public EventDTO(Event event) {
        id = event.getId();
        eventName = event.getEventName();
        description = event.getDescription();
        time = event.getTime();
        chatId = event.getUser().getChatId();
    }

    public Long getUserId() {
        return chatId;
    }

    public Long getId() {
        return id;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "\n" +
                "🔖 Compromisso: " + eventName + "\n" +
                "📍  Descrição: " + description + "\n" +
                "🕒 Horário: " + time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n";
    }

}
