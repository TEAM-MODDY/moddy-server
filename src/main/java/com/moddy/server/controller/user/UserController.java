package com.moddy.server.controller.user;

import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.user.dto.response.UserDetailResponseDto;
import com.moddy.server.service.user.UserService;
import io.swagger.v3.oas.annotations.Parameter;
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
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<UserDetailResponseDto> getUserDetail(@Parameter(hidden = true) @UserId Long userId) {
        return SuccessResponse.success(USER_MY_PAGE_SUCCESS, userService.getUserDetail(userId));
    }
}
