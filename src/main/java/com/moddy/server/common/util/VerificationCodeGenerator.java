package com.moddy.server.common.util;

import java.security.SecureRandom;

public class VerificationCodeGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int START_NUM = 100_000;
    private static final int END_NUM = 999_999;

    public static String generate() {
        return String.valueOf(secureRandom.nextInt(START_NUM, END_NUM));
    }
}
