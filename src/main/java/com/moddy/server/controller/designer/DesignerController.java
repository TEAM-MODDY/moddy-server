package com.moddy.server.controller.designer;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.request.OfferImageUrlRequestDto;
import com.moddy.server.controller.designer.dto.response.ApplicationDetailInfoResponse;
import com.moddy.server.controller.designer.dto.response.DownloadUrlResponseDto;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.service.application.HairModelApplicationRetrieveService;
import com.moddy.server.service.designer.DesignerRegisterService;
import com.moddy.server.service.designer.DesignerService;
import com.moddy.server.service.offer.HairServiceOfferRegisterService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.moddy.server.common.exception.enums.SuccessCode.GET_PRE_SIGNED_URL_SUCCESS;

@RestController
@RequestMapping("/designer")
@Tag(name = "DesignerController")
@RequiredArgsConstructor
public class DesignerController {

    private final DesignerService designerService;
    private final DesignerRegisterService designerRegisterService;
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;
    private final HairServiceOfferRegisterService hairServiceOfferRegisterService;

    @Tag(name = "Auth Controller", description = "로그인 및 회원 가입 관련 API 입니다.")
    @Operation(summary = "[JWT] 디자이너 회원가입 API", description = "디자이너 회원가입 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 회원가입 성공", content = @Content(schema = @Schema(implementation = UserCreateResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping(value = "/auth/signup/designer", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    SuccessResponse<UserCreateResponse> createDesigner(
            @Parameter(hidden = true) @UserId Long designerId,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg,
            @Valid @RequestPart("designerInfo") DesignerCreateRequest designerInfo) {
        return SuccessResponse.success(SuccessCode.DESIGNER_CREATE_SUCCESS, designerRegisterService.createDesigner(designerId, designerInfo, profileImg));
    }

    @Operation(summary = "[JWT] 제안서 다운로드 링크", description = "디자이너 제안서 다운로드 링크 불러오는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 지원서 상세 조회 성공", content = @Content(schema = @Schema(implementation = ApplicationDetailInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 디자이너는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/offer/download-url")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DownloadUrlResponseDto> getOfferImageDownloadUrl(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestBody OfferImageUrlRequestDto offerImageUrlRequestDto
    ) {
        return SuccessResponse.success(GET_PRE_SIGNED_URL_SUCCESS, designerService.getOfferImageDownloadUrl(userId, offerImageUrlRequestDto.offerImageUrl()));
    }

}
