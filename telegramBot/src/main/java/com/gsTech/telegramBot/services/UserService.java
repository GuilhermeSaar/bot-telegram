package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public void getOrCreateUserByChatId(Long chatId, Update update) {

        String currentName = extractTelegramName(update);

        userRepository.findByChatId(chatId).ifPresentOrElse(user -> {

            if(!user.getName().equals(currentName)) {
                user.setName(currentName);
                userRepository.save(user);
            }
        }, () -> {
            var newUser = new User();
            newUser.setChatId(chatId);
            newUser.setName(currentName);
            userRepository.save(newUser);
        });
    }


    private String extractTelegramName(Update update) {

        if (update.hasMessage()) {
            var from = update.getMessage().getFrom();
            return from.getFirstName() + (from.getLastName() != null ? " " + from.getLastName() : "");
        }

        else if (update.hasCallbackQuery()) {
            var from = update.getCallbackQuery().getFrom();
            return from.getFirstName() + (from.getLastName() != null ? " " + from.getLastName() : "");
        }

        return "Usuário Desconhecido";
    }










}
