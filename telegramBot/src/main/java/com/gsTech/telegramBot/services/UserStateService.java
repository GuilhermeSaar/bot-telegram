package com.gsTech.telegramBot.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserStateService {

    private final Map<Long, String> userState = new HashMap<>();

    public void setUserState(Long chatId, String state) {
        userState.put(chatId, state);
    }

    public String getUserState(Long chatId) {
        return userState.get(chatId);
    }

    public void clearUserState(Long chatId) {
        userState.remove(chatId);
    }


}
