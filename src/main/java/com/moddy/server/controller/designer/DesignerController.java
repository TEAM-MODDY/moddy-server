package com.moddy.server.controller.designer;


import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.service.designer.DesignerService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designer")
@Tag(name = "DesignerController")
@RequiredArgsConstructor
public class DesignerController {
    private final DesignerService designerService;
    @Operation(summary = "[JWT] 디자이너 메인 뷰 조회", description = "디자이너 메인 뷰 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = DesignerMainResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DesignerMainResponse> getDesignerMainInfo(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "page", description = "페이지 ") @RequestParam(value = "page") int page,
            @Parameter(name = "size", description = "페이지 ") @RequestParam(value = "size") int size
    ){
        return SuccessResponse.success(SuccessCode.FIND_DESIGNER_MAIN_INFO_SUCCESS, designerService.getDesignerMainView(userId, page, size));
    }
}
