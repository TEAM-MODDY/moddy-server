package com.moddy.server.controller.designer;


import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.kakao.KakaoCode;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.response.DesignerCreateResponse;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth/signup/")
@Tag(name = "ModelController")
@RequiredArgsConstructor
public class DesignerController {

    private final DesignerService designerService;

    @Operation(summary = "[KAKAO CODE] 디자이너 회원가입 뷰 조회", description = "디자이너 회원가입 뷰 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 회원가입 성공", content = @Content(schema = @Schema(implementation = DesignerCreateResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping(value = "/designer", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    SuccessResponse<DesignerCreateResponse> createDesigner(@Parameter(hidden = true) @KakaoCode String kakaoCode, @ModelAttribute DesignerCreateRequest request) {
        return SuccessResponse.success(SuccessCode.DESIGNER_CREATE_SUCCESS, designerService.createDesigner(kakaoCode, request));
    }

}
