package com.moddy.server.external.sms.impl;

import com.google.gson.Gson;
import com.moddy.server.external.sms.SmsService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Component
public class GabiaSmsServiceImpl implements SmsService {
    public static final String SMS_OAUTH_TOKEN_URL = "https://sms.gabia.com/oauth/token";
    public static final String SMS_SEND_URL = "https://sms.gabia.com/api/send/sms";

    @Value("${gabia.sms-id}")
    private String smsId;
    @Value("${gabia.api-key}")
    private String apiKey;
    @Value("${gabia.caller-phone-number}")
    private String from;
    @Value("${gabia.ref-key}")
    private String refKey;

    @Override
    public boolean sendSms(final String to, final String verificationCode) throws IOException {
        final String accessToken = getGabiaAccessToken();
        final String smsValue = createAuthValue(accessToken);

        final String message = String.format(MESSAGE_FORM, verificationCode);
        final RequestBody requestBody = createMessageRequestBody(to, message);
        final Request request = createRequest(requestBody, smsValue, SMS_SEND_URL);
        final Response response = callExecute(request);

        final HashMap<String, String> result = getExecuteResult(response);

        final boolean status = result.get("code").equals("200");
        saveLog(status, result);

        return status;
    }

    private void saveLog(final boolean status, final HashMap<String, String> result) {
        final StringBuilder sb = new StringBuilder();
        for (String key : result.keySet()) {
            sb.append(String.format("%s: %s \n", key, result.get(key)));
        }
        if (status) log.info(sb.toString());
        else log.error(sb.toString());
    }

    private String createAuthValue(final String key) {
        return Base64.getEncoder()
                .encodeToString(String.format("%s:%s", smsId, key)
                        .getBytes(StandardCharsets.UTF_8));
    }

    private String getGabiaAccessToken() throws IOException {
        final String authValue = createAuthValue(apiKey);

        final RequestBody requestBody = createAuthRequestBody();
        final Request request = createRequest(requestBody, authValue, SMS_OAUTH_TOKEN_URL);
        final Response response = callExecute(request);

        final HashMap<String, String> result = getExecuteResult(response);
        return result.get("access_token");
    }

    private HashMap<String, String> getExecuteResult(final Response response) throws IOException {
        return new Gson().fromJson(Objects.requireNonNull(response.body()).string(), HashMap.class);
    }

    private Response callExecute(final Request request) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute();
    }

    private Request createRequest(final RequestBody requestBody, final String authorization, final String url) {
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + authorization)
                .addHeader("cache-control", "no-cache")
                .build();
    }

    private RequestBody createAuthRequestBody() {
        return new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "client_credentials")
                .build();
    }

    private RequestBody createMessageRequestBody(final String to, final String message) {
        return new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone", to)
                .addFormDataPart("callback", from)
                .addFormDataPart("message", message)
                .addFormDataPart("refkey", refKey)
                .build();
    }
}
