package com.moddy.server.controller.offer;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OfferController {

    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;
    private final HairServiceOfferRegisterService hairServiceOfferRegisterService;

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

}

