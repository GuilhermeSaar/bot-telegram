package com.gsTech.telegramBot.orm;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long chatId;
    private String name;

    @OneToMany(mappedBy = "user")
    private Set<Event> events;

    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Long getChatId() {
        return chatId;
    }
}
