package com.gsTech.telegramBot.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramBotService {

    @Value("${telegram.token}")
    private String token;

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot%s/setWebhook?url=%s";

    public void setWebhook(String webhookUrl) {
        String url = String.format(TELEGRAM_API_URL, this.token, webhookUrl);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("Resposta da API do telegram" + response.getBody());
        }catch (Exception e) {
            System.err.println("Erro ao tentar configurar o webhook: " + e.getMessage());
        }
    }

}
