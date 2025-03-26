package com.gsTech.telegramBot.orm;

import com.gsTech.telegramBot.enums.EventType;
import com.gsTech.telegramBot.enums.Priority;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventName;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String location;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private String description;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Event() {
    }

    public Event(String eventName, EventType eventType, String location, Priority priority, String description, LocalDateTime time, User user) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.location = location;
        this.priority = priority;
        this.description = description;
        this.time = time;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
