package com.moddy.server.external.sms;

import java.io.IOException;

public interface SmsService {
    String MESSAGE_FORM = "[%s] moddy에서 보낸 인증번호입니다.";

    boolean sendSms(String to, String verificationCode) throws IOException;
}
