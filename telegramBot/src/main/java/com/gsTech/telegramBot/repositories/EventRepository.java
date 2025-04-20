package com.gsTech.telegramBot.repositories;

import com.gsTech.telegramBot.orm.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByUserId(Long userId);

}
