package com.moddy.server.external.sms.impl;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.moddy.server.external.sms.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwsSmsServiceImpl implements SmsService {
    private final AmazonSNSClient amazonSNSClient;
    private static final String KR = "+82";

    @Override
    public boolean sendSms(String to, String verificationCode) {
        try {
            PublishRequest request = new PublishRequest();
            request.setMessage(String.format(MESSAGE_FORM, verificationCode));
            request.setPhoneNumber(KR + to);

            amazonSNSClient.publish(request);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
