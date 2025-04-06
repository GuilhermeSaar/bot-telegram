package com.gsTech.telegramBot.DTO.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private Long messageId;
    @JsonProperty("from")
    private TelegramUser from;
    private Chat chat;
    private String text;
    private Long date;

    @JsonProperty("message_id")
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public TelegramUser getFrom() {
        return from;
    }

    public void setFrom(TelegramUser from) {
        this.from = from;
    }


}
