package com.moddy.server.controller.designer;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.request.OfferCreateRequest;
import com.moddy.server.controller.designer.dto.request.OfferImageUrlRequestDto;
import com.moddy.server.controller.designer.dto.response.ApplicationDetailInfoResponse;
import com.moddy.server.controller.designer.dto.response.ApplicationInfoResponse;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.designer.dto.response.DownloadUrlResponseDto;
import com.moddy.server.controller.designer.dto.response.ModelInfoResponse;
import com.moddy.server.controller.model.dto.ApplicationDto;
import com.moddy.server.controller.model.dto.ApplicationModelInfoDto;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.service.application.HairModelApplicationRetrieveService;
import com.moddy.server.service.designer.DesignerService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.moddy.server.common.exception.enums.SuccessCode.GET_PRE_SIGNED_URL_SUCCESS;

@RestController
@RequestMapping("/designer")
@Tag(name = "DesignerController")
@RequiredArgsConstructor
public class DesignerController {

    private final DesignerService designerService;
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;
    private final ModelRetrieveService modelRetrieveService;
    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;

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

    @Operation(summary = "[JWT] 제안서 작성하기", description = "제안서 작성하기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제안서 작성 성공", content = @Content(schema = @Schema(implementation = SuccessNonDataResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping("{applicationId}/offer")
    public SuccessNonDataResponse offerCreateRequest(
            @Parameter(hidden = true) @UserId Long userId,
            @PathVariable(value = "applicationId") Long applicationId,
            @Valid @RequestBody OfferCreateRequest offerCreateRequest) throws IOException {
        designerService.postOffer(userId, applicationId, offerCreateRequest);
        return SuccessNonDataResponse.success(SuccessCode.POST_OFFER_SUCCESS);
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
