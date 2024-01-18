package com.moddy.server.common.util;

import com.moddy.server.external.sms.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SmsUtil {
    private static final String VERIFICATION_MESSAGE_FORM = "[%s] moddy에서 보낸 인증번호입니다.";
    private static final String OFFER_MESSAGE_FORM = "[moddy] %s님, 새로운 헤어모델 제안이 도착했어요!\nmoddy.kr";
    private final SmsService smsService;

    public boolean sendVerificationCode(final String to, final String verificationCode) throws IOException {
        final String verificationCodeMessage = String.format(VERIFICATION_MESSAGE_FORM, verificationCode);
        return smsService.sendSms(to, verificationCodeMessage);
    }

    public boolean sendOfferToModel(final String to, final String modelName) throws IOException {
        final String modelMessage = String.format(OFFER_MESSAGE_FORM, modelName);
        return smsService.sendSms(to, modelMessage);
    }
}
