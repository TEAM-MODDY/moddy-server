package com.moddy.server.controller.model;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.controller.model.dto.request.ModelCreateRequest;
import com.moddy.server.service.model.ModelRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Model Controller")
@RequestMapping("/model")
public class ModelRegisterController {

    private final ModelRegisterService modelRegisterService;

    @Operation(summary = "[JWT] 모델 회원가입 API", description = "모델 회원가입 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모델 회원가입 성공"),
            @ApiResponse(responseCode = "401", description = "인증오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 입력했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<UserCreateResponse> createModel(
            @Parameter(hidden = true) @UserId Long userId,
            @Valid @RequestBody ModelCreateRequest modelCreateRequest) {
        return SuccessResponse.success(SuccessCode.MODEL_CREATE_SUCCESS, modelRegisterService.createModel(userId, modelCreateRequest));
    }

}
