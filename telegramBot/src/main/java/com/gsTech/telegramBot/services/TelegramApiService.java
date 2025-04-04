package com.gsTech.telegramBot.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class TelegramApiService {

    @Value("${telegram.token}")
    private String token;

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot%s/setWebhook?url=%s";
    private static final String SEND_MESSAGE_URL = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";


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


    public void sendMessage(Long chatId, String text) {

        String url = String.format(SEND_MESSAGE_URL, this.token, chatId, text);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("Resposta da API do telegram: " + URLDecoder.decode(Objects.requireNonNull(response.getBody()),
                    StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
        }
    }


}

