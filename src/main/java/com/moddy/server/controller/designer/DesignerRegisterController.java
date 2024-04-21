package com.moddy.server.controller.designer;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.request.DesignerUpdateRequest;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.service.designer.DesignerRegisterService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Designer Controller")
@RequestMapping("/designer")
public class DesignerRegisterController {
    private final DesignerRegisterService designerRegisterService;

    @Operation(summary = "[JWT] 디자이너 회원가입 API", description = "디자이너 회원가입 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 회원가입 성공", content = @Content(schema = @Schema(implementation = UserCreateResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    SuccessResponse<UserCreateResponse> createDesigner(
            @Parameter(hidden = true) @UserId Long designerId,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg,
            @Valid @RequestPart("designerInfo") DesignerCreateRequest designerInfo) {
        return SuccessResponse.success(SuccessCode.DESIGNER_CREATE_SUCCESS, designerRegisterService.createDesigner(designerId, designerInfo, profileImg));
    }

    @Operation(summary = "[JWT] 디자이너 마이페이지 수정 API", description = "디자이너 마이페이지 수정 API입니다.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "디자이너 마이페이지 수정 성공"),
                @ApiResponse(responseCode = "401", description = "인증오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 입력했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
        @SecurityRequirement(name = "JWT Auth")
        public SuccessNonDataResponse updateModel(
                @Parameter(hidden = true) @UserId Long designerId,
                @RequestPart(value = "profileImg", required = false) MultipartFile profileImg,
                @RequestPart(value = "designerUpdateInfo") @Valid DesignerUpdateRequest designerUpdateInfo){
            designerRegisterService.updateDesigner(designerId, profileImg, designerUpdateInfo);
            return SuccessNonDataResponse.success(SuccessCode.DESIGNER_UPDATE_SUCCESS);
        }
}
