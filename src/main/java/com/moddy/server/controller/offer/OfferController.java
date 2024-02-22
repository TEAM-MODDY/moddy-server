package com.moddy.server.controller.offer;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.request.OfferCreateRequest;
import com.moddy.server.controller.model.dto.response.OpenChatResponse;
import com.moddy.server.controller.offer.dto.response.ModelMainOfferResponse;
import com.moddy.server.controller.offer.response.DetailOfferResponse;
import com.moddy.server.service.offer.HairServiceOfferRegisterService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "Offer Controller")
@RequestMapping("/offer")
public class OfferController {

    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;
    private final HairServiceOfferRegisterService hairServiceOfferRegisterService;

    @Operation(summary = "[JWT] 디자이너 제안서 승낙하기", description = "디자이너 제안서 승낙하기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 제안서 승낙하기"),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping("/{offerId}/agree")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessNonDataResponse acceptOffer(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId) {
        hairServiceOfferRegisterService.updateOfferAgreeStatus(offerId);
        return SuccessNonDataResponse.success(SuccessCode.OFFER_ACCEPT_SUCCESS);
    }

    @Operation(summary = "[JWT] 모델 메인 뷰 조회", description = "모델 메인 뷰 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = ModelMainOfferResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping()
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<ModelMainOfferResponse> getModelMainInfo(
            @Parameter(hidden = true) @UserId Long modelId,
            @Parameter(name = "page", description = "페이지 ") @RequestParam(value = "page") int page,
            @Parameter(name = "size", description = "페이지 ") @RequestParam(value = "size") int size) {
        return SuccessResponse.success(SuccessCode.FIND_MODEL_MAIN_INFO_SUCCESS, hairServiceOfferRetrieveService.getModelMainOfferInfo(modelId, page, size));
    }

    @Operation(summary = "[JWT] 모델에게 온 디자이너 제안 상세보기", description = "제안서 상세보기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "제안서 상세보기 조회 성공", content = @Content(schema = @Schema(implementation = DetailOfferResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{offerId}")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DetailOfferResponse> getModelDetailOfferInfo(
            @Parameter(hidden = true) @UserId Long modelId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId) {
        return SuccessResponse.success(SuccessCode.FIND_MODEL_DETAIL_OFFER_SUCCESS, hairServiceOfferRetrieveService.getOfferDetail(offerId));
    }

    @Operation(summary = "[JWT] 제안서 작성하기", description = "제안서 작성하기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제안서 작성 성공", content = @Content(schema = @Schema(implementation = SuccessNonDataResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping("/{applicationId}")
    public SuccessNonDataResponse offerCreateRequest(
            @Parameter(hidden = true) @UserId Long designerId,
            @PathVariable(value = "applicationId") Long applicationId,
            @Valid @RequestBody OfferCreateRequest offerCreateRequest) throws IOException {
        hairServiceOfferRegisterService.postOffer(designerId, applicationId, offerCreateRequest);
        return SuccessNonDataResponse.success(SuccessCode.POST_OFFER_SUCCESS);
    }
}