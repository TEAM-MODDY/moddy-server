package com.moddy.server.controller.user;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.user.dto.response.UserDetailResponseDto;
import com.moddy.server.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.moddy.server.common.exception.enums.SuccessCode.USER_MY_PAGE_SUCCESS;

@Tag(name = "User Controller", description = "유저 정보 조회 및 탈퇴 API 입니다.")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "[JWT] 유저 마이페이지 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이페이지 유저 정보 조회 성공입니다."),
            @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다. 다시 로그인 해주세요.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<UserDetailResponseDto> getUserDetail(@Parameter(hidden = true) @UserId Long userId) {
        return SuccessResponse.success(USER_MY_PAGE_SUCCESS, userService.getUserDetail(userId));
    }
}
