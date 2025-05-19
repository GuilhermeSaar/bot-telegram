package com.gsTech.telegramBot.orm;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private Long chatId;
    private String name;
    private String username;

    @OneToMany(mappedBy = "user")
    private Set<Event> events;

    public User(String username) {
        this.username = username;
    }

    public User() {
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Long getChatId() {
        return chatId;
    }
}
