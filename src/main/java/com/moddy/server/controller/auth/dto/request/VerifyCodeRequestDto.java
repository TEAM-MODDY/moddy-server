package com.moddy.server.controller.auth.dto.request;

public record VerifyCodeRequestDto(String phoneNumber, String verifyCode) {
}
