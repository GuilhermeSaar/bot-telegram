package com.gsTech.telegramBot.DTO;

import com.gsTech.telegramBot.orm.User;

public class UserDTO {

    private Long id;
    private Long chatId;
    private String name;


    public UserDTO(Long id, Long chatId, String name) {
        this.id = id;
        this.chatId = chatId;
        this.name = name;

    }

    public UserDTO() {
    }

    public UserDTO(User user) {
        id = user.getId();
        chatId = user.getChatId();
        name = user.getName();

    }

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
