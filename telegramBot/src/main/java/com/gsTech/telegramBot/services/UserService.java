package com.gsTech.telegramBot.services;

import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Serviço responsável pela lógica de negócio relacionada a usuários.
 *
 * Fornece método para obter um usuário pelo chatId, criando-o caso não exista.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    /**
     * Obtém um usuário pelo chatId. Caso o usuário não exista, cria um novo usuário com o chatId informado
     * e um nome padrão.
     *
     * @param chatId identificador do chat do usuário
     * @return o usuário existente ou recém-criado
     */
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
