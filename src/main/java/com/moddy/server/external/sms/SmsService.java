package com.moddy.server.external.sms;

public interface SmsService {
    String MESSAGE_FORM = "[%s] moddy에서 보낸 인증번호입니다. 해당 인증번호는 3분간 유효합니다.";

    boolean sendSms(String to, String verificationCode);
}
