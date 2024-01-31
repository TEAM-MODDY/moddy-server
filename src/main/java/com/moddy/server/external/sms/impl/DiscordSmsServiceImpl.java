package com.moddy.server.external.sms.impl;

import com.google.gson.JsonObject;
import com.moddy.server.external.sms.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@Profile({"!prod"})
public class DiscordSmsServiceImpl implements SmsService {
    private static final String CONTENT_PROPERTY = "content";
    @Value("${discord.webhook-url}")
    private String discordWebHookUrl;

    @Override
    public boolean sendSms(String to, String verificationCode) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CONTENT_PROPERTY, verificationCode);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
        restTemplate.postForObject(discordWebHookUrl, entity, String.class);
        return true;
    }
}
