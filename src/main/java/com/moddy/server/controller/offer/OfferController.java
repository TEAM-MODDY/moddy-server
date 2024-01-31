package com.moddy.server.controller.offer;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.offer.response.DetailOfferResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OfferController {

    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;

    @Tag(name = "ModelController")
    @Operation(summary = "[JWT] 모델에게 온 디자이너 제안 상세보기", description = "제안서 상세보기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제안서 상세보기 조회 성공", content = @Content(schema = @Schema(implementation = DetailOfferResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/model/offer/{offerId}")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DetailOfferResponse> getModelDetailOfferInfo(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId) {
        return SuccessResponse.success(SuccessCode.FIND_MODEL_DETAIL_OFFER_SUCCESS, hairServiceOfferRetrieveService.getOfferDetail(userId, offerId));
    }

}

