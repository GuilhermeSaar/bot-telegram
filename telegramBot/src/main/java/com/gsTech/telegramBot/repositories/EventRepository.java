package com.gsTech.telegramBot.repositories;

import com.gsTech.telegramBot.orm.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
