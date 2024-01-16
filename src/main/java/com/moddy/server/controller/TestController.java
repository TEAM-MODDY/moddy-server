package com.moddy.server.controller;

import com.moddy.server.config.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final JwtService jwtService;

    @GetMapping("/test")
    public String getToken() {
        return jwtService.createAccessToken("1");
    }
}

