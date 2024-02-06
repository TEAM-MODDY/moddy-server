package com.moddy.server.controller.offer;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OfferController {

    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;
    private final HairServiceOfferRegisterService hairServiceOfferRegisterService;

    @Tag(name = "ModelController")
    @Operation(summary = "[JWT] 카카오톡 오픈채팅", description = "지원서 캡처 이미지 및 디자이너 정보 조회입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = OpenChatResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/model/{offerId}/agree")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<OpenChatResponse> getOpenChat(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId) {
        return SuccessResponse.success(SuccessCode.OPEN_CHAT_GET_SUCCESS, hairServiceOfferRetrieveService.getOpenChatInfo(userId, offerId));
    }

    @Tag(name = "ModelController")
    @Operation(summary = "[JWT] 디자이너 제안서 승낙하기", description = "디자이너 제안서 승낙하기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 제안서 승낙하기"),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping("/model/offer/{offerId}")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessNonDataResponse acceptOffer(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId) {
        hairServiceOfferRegisterService.updateOfferAgreeStatus(offerId);
        return SuccessNonDataResponse.success(SuccessCode.OFFER_ACCEPT_SUCCESS);
    }

    @Tag(name = "ModelController")
    @Operation(summary = "[JWT] 모델 메인 뷰 조회", description = "모델 메인 뷰 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = ModelMainOfferResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/model")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<ModelMainOfferResponse> getModelMainInfo(
            @Parameter(hidden = true) @UserId Long modelId,
            @Parameter(name = "page", description = "페이지 ") @RequestParam(value = "page") int page,
            @Parameter(name = "size", description = "페이지 ") @RequestParam(value = "size") int size) {
        return SuccessResponse.success(SuccessCode.FIND_MODEL_MAIN_INFO_SUCCESS, hairServiceOfferRetrieveService.getModelMainOfferInfo(modelId, page, size));
    }

    @Tag(name = "ModelController")
    @Operation(summary = "[JWT] 모델에게 온 디자이너 제안 상세보기", description = "제안서 상세보기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "200", description = "제안서 상세보기 조회 성공", content = @Content(schema = @Schema(implementation = DetailOfferResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/model/offer/{offerId}")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DetailOfferResponse> getModelDetailOfferInfo(
            @Parameter(hidden = true) @UserId Long modelId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId) {
        return SuccessResponse.success(SuccessCode.FIND_MODEL_DETAIL_OFFER_SUCCESS, hairServiceOfferRetrieveService.getOfferDetail(modelId, offerId));
    }
}