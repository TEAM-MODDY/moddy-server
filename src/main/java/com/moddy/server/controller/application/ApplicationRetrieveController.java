package com.moddy.server.controller.application;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.response.DownloadUrlResponseDto;
import com.moddy.server.service.application.HairModelApplicationRetrieveService;
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
import org.springframework.web.bind.annotation.RestController;

import static com.moddy.server.common.exception.enums.SuccessCode.GET_PRE_SIGNED_URL_SUCCESS;

@Tag(name = "Application Controller", description = "지원서 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationRetrieveController {
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;

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
}
