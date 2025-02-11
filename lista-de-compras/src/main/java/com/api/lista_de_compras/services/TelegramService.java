package com.api.lista_de_compras.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {
    
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";
    @Value("${telegram.botToken}")
    private String botToken;
    @Value("${telegram.chatId}")
    private String chatId;

    private final RestTemplate restTemplate;

    public TelegramService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String message) {
        String url = TELEGRAM_API_URL + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + message;

        try {
            restTemplate.getForObject(url, String.class);
            System.out.println("Mensagem enviada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem para o Telegram: " + e.getMessage());
        }
    }
}
