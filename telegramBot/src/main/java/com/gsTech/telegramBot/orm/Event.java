package com.gsTech.telegramBot.orm;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventName;
    private String eventType;
    private String location;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Event() {
    }

    public Event(String eventName, String eventType, String location, LocalDateTime time, User user) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.location = location;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
