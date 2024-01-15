package com.moddy.server.external.sms.impl;

import com.google.gson.Gson;
import com.moddy.server.external.sms.SmsService;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

@Component
public class GabiaSmsServiceImpl implements SmsService {
    public static final String SMS_SEND_URL = "https://sms.gabia.com/api/send/sms";
    private static final String SMS_ID = "YOUR_SMS_ID"; // SMS ID 를 입력해 주세요.
    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN"; // ACCESS TOKEN 을 입력해 주세요.
    private static final String AUTH_VALUE = Base64.getEncoder()
            .encodeToString(String.format("%s:%s", SMS_ID, ACCESS_TOKEN)
                    .getBytes(StandardCharsets.UTF_8));
    private static final String AUTHORIZATION_CODE = "Basic " + AUTH_VALUE;

    @Override
    public boolean sendSms(final String to, final String verificationCode) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String message = String.format(MESSAGE_FORM, verificationCode);

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone", "RECEIPT_PHONE_NUMBER")
                .addFormDataPart("callback", to)
                .addFormDataPart("message", message)
                .addFormDataPart("refkey", "YOUR_REF_KEY")
                .build();

        Request request = new Request.Builder()
                .url(SMS_SEND_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", AUTHORIZATION_CODE)
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();

        HashMap<String, String> result = new Gson().fromJson(Objects.requireNonNull(response.body()).string(), HashMap.class);
        for (String key : result.keySet()) {
            System.out.printf("%s: %s%n", key, result.get(key));
        }
        return true;
    }
}
