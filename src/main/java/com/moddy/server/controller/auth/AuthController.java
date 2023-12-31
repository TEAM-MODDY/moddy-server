package com.moddy.server.controller.auth;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.config.resolver.kakao.KakaoCode;
import com.moddy.server.controller.auth.dto.response.LoginResponseDto;
import com.moddy.server.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.moddy.server.common.exception.enums.SuccessCode.SOCIAL_LOGIN_SUCCESS;

@Tag(name = "Auth Controller", description = "로그인 및 회원 가입 관련 API 입니다.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final String ORIGIN = "origin";
    private final AuthService authService;

    @Operation(summary = "[KAKAO CODE] 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 로그인 성공입니다."),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 카카오 코드를 입력했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<LoginResponseDto> login(
            @Parameter(hidden = true) @KakaoCode String kakaoCode,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        return SuccessResponse.success(SOCIAL_LOGIN_SUCCESS, authService.login(request.getHeader(ORIGIN), kakaoCode));
    }
}
