package com.gsTech.telegramBot;

import com.gsTech.telegramBot.services.DateParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramBotApplication implements CommandLineRunner {

	@Autowired
	private DateParseService dateParseService;

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Teste1: " + dateParseService.parseDate("25 de junho as 15:00"));
		System.out.println("Teste2: " + dateParseService.parseDate("segunda as 13:00"));
	}
}
