package com.moddy.server.controller.designer;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.model.dto.DesignerInfoOpenChatDto;
import com.moddy.server.service.designer.DesignerRetrieveService;
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

@RestController
@RequiredArgsConstructor
@Tag(name = "Designer Controller")
@RequestMapping("/designer")
public class DesignerRetrieveController {

    private final DesignerRetrieveService designerRetrieveService;
    @Operation(summary = "[JWT] 카카오톡 오픈채팅", description = "오픈채팅 뷰에서 디자이너 정보 조회입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 정보 조회 성공", content = @Content(schema = @Schema(implementation = DesignerInfoOpenChatDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{designerId}")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DesignerInfoOpenChatDto> getDesignerOpenChatInfo(
            @Parameter(hidden = true) @UserId Long modelId,
            @Parameter(name = "designerId", description = "디자이너아이디") @PathVariable(value = "designerId") Long designerId) {
        return SuccessResponse.success(SuccessCode.OPEN_CHAT_GET_SUCCESS,designerRetrieveService.getDesignerOpenChatInfo(designerId));
    }
}
