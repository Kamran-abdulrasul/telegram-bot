package com.example.telegram_bot.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Data
public class TelegramBotConfig {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.webhook.url}")
    private String webhookUrl;

    @Value("${telegram.bot.name}")
    private  String botName;



}
