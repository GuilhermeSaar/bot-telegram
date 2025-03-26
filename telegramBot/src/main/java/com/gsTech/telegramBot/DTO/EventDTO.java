package com.gsTech.telegramBot.DTO;

import com.gsTech.telegramBot.enums.EventType;
import com.gsTech.telegramBot.enums.Priority;
import com.gsTech.telegramBot.orm.Event;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDTO {

    private Long id;
    private String eventName;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String location;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private String description;
    private LocalDateTime time;
    private Long UserId;

    public EventDTO() {
    }

    public EventDTO(Event event) {
        id = event.getId();
        eventName = event.getEventName();
        eventType = event.getEventType();
        location = event.getLocation();
        priority = event.getPriority();
        description = event.getDescription();
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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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
                "🔖  Compromisso: " + eventName + "\n" +
                "📂  Tipo: " + eventType + "\n" +
                "📍  Localização: " + location + "\n" +
                "⚠️  Prioridade: " + priority + "\n" +
                "📝  Descrição: " + (description.isBlank() ? "Sem descrição" : description) + "\n" +
                "🕒  Horário: " + time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n";
    }

}
