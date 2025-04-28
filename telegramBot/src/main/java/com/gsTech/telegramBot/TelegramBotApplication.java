package com.gsTech.telegramBot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramBotApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
