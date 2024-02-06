package com.moddy.server.controller.application;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.model.dto.request.ModelApplicationRequest;
import com.moddy.server.service.application.HairModelApplicationRegisterService;
import com.moddy.server.service.application.HairModelApplicationRetrieveService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final HairModelApplicationRegisterService hairModelApplicationRegisterService;
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;

    @Tag(name = "ModelController")
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

    @Tag(name = "DesignerController")
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
}

