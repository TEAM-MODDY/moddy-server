package com.moddy.server.common.util;

import com.moddy.server.external.sms.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsUtil {
    private final SmsService smsService;

    public boolean sendVerificationCode(String to, String verificationCode) {
        return smsService.sendSms(to, verificationCode);
    }
}
