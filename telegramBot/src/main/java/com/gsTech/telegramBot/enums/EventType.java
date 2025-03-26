package com.gsTech.telegramBot.enums;

public enum EventType {

    PROFISSIONAL("Profissional"),
    PESSOAL("Pessoal");

    final String eventType;

    EventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}
