package com.moddy.server.controller.application;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.response.ApplicationDetailInfoResponse;
import com.moddy.server.controller.designer.dto.response.ApplicationInfoResponse;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.designer.dto.response.ModelInfoResponse;
import com.moddy.server.controller.model.dto.ApplicationDto;
import com.moddy.server.controller.model.dto.ApplicationModelInfoDto;
import com.moddy.server.controller.model.dto.request.ModelApplicationRequest;
import com.moddy.server.service.application.HairModelApplicationRegisterService;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Application Controller")
public class ApplicationController {

    private final HairModelApplicationRegisterService hairModelApplicationRegisterService;
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;
    private final ModelRetrieveService modelRetrieveService;
    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;

    @Operation(summary = "[JWT] 모델 지원서 작성", description = "모델 지원서 작성 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 지원서 작성 성공"),
            @ApiResponse(responseCode = "400", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping(value = "/model/application", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public SuccessNonDataResponse submitModelApplication(
            @Parameter(hidden = true) @UserId Long modelId,
            @RequestPart(value = "modelImgUrl", required = false) MultipartFile modelImgUrl,
            @RequestPart(value = "applicationCaptureImgUrl", required = false) MultipartFile applicationCaptureImgUrl,
            @RequestPart(value = "applicationInfo") @Valid ModelApplicationRequest applicationInfo) {
        hairModelApplicationRegisterService.postApplication(modelId, modelImgUrl, applicationCaptureImgUrl, applicationInfo);
        return SuccessNonDataResponse.success(SuccessCode.CREATE_MODEL_APPLICATION_SUCCESS);
    }
    @Operation(summary = "[JWT] 디자이너 메인 뷰 조회", description = "디자이너 메인 뷰 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = DesignerMainResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping(value = "/designer")
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
    @GetMapping("/application/{applicationId}")
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

        ApplicationDetailInfoResponse applicationDetailInfoResponse = new ApplicationDetailInfoResponse(applicationInfoResponse,modelInfoResponse);
        return SuccessResponse.success(SuccessCode.MODEL_APPLICATION_DETAil_INFO_SUCCESS, applicationDetailInfoResponse);
    }
}

