package com.gsTech.telegramBot.enums;

public enum Priority {

    ALTA("Alta"),
    MEDIA("Media"),
    BAIXA("Baixa");

    String priority;

    Priority(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }
}
