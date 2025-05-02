package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User getOrCreateUserByChatId(Long chatId) {

        return userRepository.findByChatId(chatId).orElseGet(
                () -> {
                    var newUser = new User();
                    newUser.setChatId(chatId);
                    newUser.setName("USER TEST");
                    return userRepository.save(newUser);
                });
    }
}
