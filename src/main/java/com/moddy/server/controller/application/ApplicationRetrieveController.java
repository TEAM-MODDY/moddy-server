package com.moddy.server.controller.application;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.response.ApplicationDetailInfoResponse;
import com.moddy.server.controller.designer.dto.response.ApplicationInfoResponse;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.designer.dto.response.DownloadUrlResponseDto;
import com.moddy.server.controller.designer.dto.response.ModelInfoResponse;
import com.moddy.server.controller.model.dto.ApplicationDto;
import com.moddy.server.controller.model.dto.ApplicationModelInfoDto;
import com.moddy.server.controller.model.dto.response.ApplicationImgUrlResponse;
import com.moddy.server.service.application.HairModelApplicationRetrieveService;
import com.moddy.server.service.model.ModelRetrieveService;
import com.moddy.server.service.offer.HairServiceOfferRetrieveService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.moddy.server.common.exception.enums.SuccessCode.GET_PRE_SIGNED_URL_SUCCESS;

@Tag(name = "Application Controller", description = "지원서 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationRetrieveController {
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;
    private final ModelRetrieveService modelRetrieveService;
    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;

    @Operation(summary = "[JWT] 제안서 다운로드 링크", description = "디자이너 제안서 다운로드 링크 불러오는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원서 다운로드 URL 조회 성공"),
            @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다. 다시 로그인 해주세요."),
            @ApiResponse(responseCode = "404", description = "해당 지원서는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{applicationId}/download-url")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DownloadUrlResponseDto> getOfferImageDownloadUrl(
            @Parameter(hidden = true) @UserId final Long modelId,
            @PathVariable final Long applicationId
    ) {
        return SuccessResponse.success(GET_PRE_SIGNED_URL_SUCCESS, hairModelApplicationRetrieveService.getApplicationCaptureDownloadUrl(applicationId));
    }

    @Operation(summary = "[JWT] 디자이너 메인 뷰 조회", description = "디자이너 메인 뷰 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = DesignerMainResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DesignerMainResponse> getDesignerMainInfo(
            @Parameter(hidden = true) @UserId Long designerId,
            @Parameter(name = "page", description = "페이지 ") @RequestParam(value = "page") int page,
            @Parameter(name = "size", description = "페이지 ") @RequestParam(value = "size") int size) {
        return SuccessResponse.success(SuccessCode.FIND_DESIGNER_MAIN_INFO_SUCCESS, hairModelApplicationRetrieveService.getDesignerMainInfo(designerId, page, size));
    }

    @Operation(summary = "[JWT] 모델 지원서 상세 조회", description = "모델 지원서 상세 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 지원서 상세 조회 성공", content = @Content(schema = @Schema(implementation = ApplicationDetailInfoResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "지원서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{applicationId}")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<ApplicationDetailInfoResponse> getApplicationDetailInfo(
            @Parameter(hidden = true) @UserId Long designerId,
            @PathVariable(value = "applicationId") Long applicationId) {
        ApplicationDto applicationDto = hairModelApplicationRetrieveService.getApplicationDetailInfo(applicationId);
        ApplicationModelInfoDto modelInfoDto = modelRetrieveService.getApplicationModelInfo(applicationDto.modelId());
        ApplicationInfoResponse applicationInfoResponse = new ApplicationInfoResponse(
                applicationId,
                applicationDto.modelImgUrl(),
                applicationDto.hairLength(),
                applicationDto.preferHairStyleList(),
                applicationDto.recordResponseList(),
                applicationDto.hairDetail(),
                hairServiceOfferRetrieveService.getIsSendStatus(applicationId, designerId),
                applicationDto.instgramId()
        );

        ModelInfoResponse modelInfoResponse = new ModelInfoResponse(
                modelInfoDto.modelId(),
                modelInfoDto.name(),
                modelInfoDto.age(),
                modelInfoDto.gender(),
                modelInfoDto.regionList()

        );

        ApplicationDetailInfoResponse applicationDetailInfoResponse = new ApplicationDetailInfoResponse(applicationInfoResponse, modelInfoResponse);
        return SuccessResponse.success(SuccessCode.MODEL_APPLICATION_DETAil_INFO_SUCCESS, applicationDetailInfoResponse);
    }

    @Operation(summary = "[JWT] 지원서 이미지 가져오기", description = "오픈채팅 뷰에서 지원서 이미지 가져오기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 지원서 이미지 가져오기 성공", content = @Content(schema = @Schema(implementation = ApplicationDetailInfoResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "지원서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{applicationId}/img-url")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<ApplicationImgUrlResponse> getApplicationImgUrlOpenChat(
            @Parameter(hidden = true) @UserId Long modelId,
            @PathVariable(value = "applicationId") Long applicationId) {
        return SuccessResponse.success(SuccessCode.GET_APPLICATION_IMG_URL_SUCCESS, hairModelApplicationRetrieveService.getApplicationImgUrl(applicationId));
    }

    @Operation(summary = "[JWT] 나의 지원서 유무 확인하기", description = "마이페이지에서 유효한 지원서 유무를 확인하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유효한 지원서 존재여부 조회 성공", content = @Content(schema = @Schema(implementation = ApplicationDetailInfoResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 지원서를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/check")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessNonDataResponse getValidApplicationStatus(
            @Parameter(hidden = true) @UserId Long modelId) {
        hairModelApplicationRetrieveService.checkValidApplicationStatus(modelId);
        return SuccessNonDataResponse.success(SuccessCode.CHECK_VALID_APPLICATION_SUCCESS);
    }

}
