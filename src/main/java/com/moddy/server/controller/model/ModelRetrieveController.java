package com.moddy.server.controller.model;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.auth.dto.response.RegionResponse;
import com.moddy.server.controller.model.dto.response.ApplicationUserDetailResponse;
import com.moddy.server.controller.model.dto.response.ModelMyPageResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "Model Controller", description = "모델과 관련된 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/model")
public class ModelRetrieveController {
    private final ModelRetrieveService modelRetrieveService;

    @Operation(summary = "모델 회원가입 시 희망 지역 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "희망 지역 리스트 조회 성공입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/regions")
    public SuccessResponse<List<RegionResponse>> getRegionList() {
        return SuccessResponse.success(SuccessCode.FIND_REGION_LIST_SUCCESS, modelRetrieveService.getRegionList());
    }

    @Operation(summary = "[JWT] 모델 지원서 최종 확인 시 유저 정보 조회 API", description = "[모델 뷰] 모델 지원서 최종 확인 시 유저 정보 조회 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 지원서 작성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @GetMapping(value = "/detail")
    public SuccessResponse<ApplicationUserDetailResponse> getUserDetailInApplication(@Parameter(hidden = true) @UserId final Long modelId) {
        return SuccessResponse.success(SuccessCode.CREATE_MODEL_APPLICATION_SUCCESS, modelRetrieveService.getModelDetailInApplication(modelId));
    }

    @Operation(summary = "[JWT] 마이페이지 모델 정보 조회", description = "마이페이지 수정시 모델 정보 조회입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 정보 조회 성공", content = @Content(schema = @Schema(implementation = ModelMyPageResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping()
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<ModelMyPageResponse> getDesignerInfo(
            @Parameter(hidden = true) @UserId Long modelId) {
        return SuccessResponse.success(SuccessCode.FIND_MODEL_INFO_SUCCESS, modelRetrieveService.getModelInfo(modelId));
    }
}