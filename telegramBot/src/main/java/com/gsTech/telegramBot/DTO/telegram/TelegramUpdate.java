package com.gsTech.telegramBot.DTO.telegram;

public class TelegramUpdate {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static class Message {
        private String text;
        private Chat chat;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }
    }

    public static class Chat {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}

