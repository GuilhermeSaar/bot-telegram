package com.gsTech.telegramBot.DTO;

import com.gsTech.telegramBot.orm.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDTO {

    private Long id;
    private String eventName;
    private String eventType;
    private String location;
    private LocalDateTime time;
    private Long UserId;

    public EventDTO() {
    }

    public EventDTO(Event event) {
        id = event.getId();
        eventName = event.getEventName();
        eventType = event.getEventType();
        location = event.getLocation();
        time = event.getTime();
    }

    public Long getUserId() {
        return UserId;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
                "🔖  Compromisso: " + eventName + "\n" +
                "📂  Tipo: " + eventType + "\n" +
                "📍  Localização: " + location + "\n" +
                "🕒  Horário: " + time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n";
    }

}
