package com.moddy.server.controller.model;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.model.dto.response.ApplicationUserDetailResponse;
import com.moddy.server.service.model.ModelRetrieveService;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ModelController", description = "모델과 관련된 API 입니다.")
@RestController
@RequiredArgsConstructor
public class ModelRetrieveController {
    private final ModelRetrieveService modelRetrieveService;

    @Operation(summary = "[JWT] 모델 지원서 최종 확인 시 유저 정보 조회 API", description = "[모델 뷰] 모델 지원서 최종 확인 시 유저 정보 조회 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원서 내에 들어가는 모델 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다. 다시 로그인 해주세요.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @GetMapping(value = "/model/detail")
    public SuccessResponse<ApplicationUserDetailResponse> getUserDetailInApplication(@Parameter(hidden = true) @UserId final Long modelId) {
        return SuccessResponse.success(SuccessCode.CREATE_MODEL_APPLICATION_SUCCESS, modelRetrieveService.getModelDetailInApplication(modelId));
    }
}
