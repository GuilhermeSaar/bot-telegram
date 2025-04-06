package com.gsTech.telegramBot.repositories;

import com.gsTech.telegramBot.orm.Event;
import com.gsTech.telegramBot.orm.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByChatId(Long id);

}
