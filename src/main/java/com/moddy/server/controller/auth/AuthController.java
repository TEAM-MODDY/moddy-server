package com.moddy.server.controller.auth;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.common.util.SmsUtil;
import com.moddy.server.config.resolver.kakao.KakaoCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.auth.dto.request.PhoneNumberRequestDto;
import com.moddy.server.controller.auth.dto.request.VerifyCodeRequestDto;
import com.moddy.server.controller.auth.dto.response.LoginResponseDto;
import com.moddy.server.controller.auth.dto.response.RegionResponse;
import com.moddy.server.controller.designer.dto.request.DesignerCreateRequest;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.controller.model.dto.request.ModelCreateRequest;
import com.moddy.server.service.auth.AuthService;
import com.moddy.server.service.designer.DesignerService;
import com.moddy.server.service.model.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.moddy.server.common.exception.enums.SuccessCode.LOGOUT_SUCCESS;
import static com.moddy.server.common.exception.enums.SuccessCode.SEND_VERIFICATION_CODE_SUCCESS;
import static com.moddy.server.common.exception.enums.SuccessCode.SOCIAL_LOGIN_SUCCESS;
import static com.moddy.server.common.exception.enums.SuccessCode.VERIFICATION_CODE_MATCH_SUCCESS;

@Tag(name = "Auth Controller", description = "로그인 및 회원 가입 관련 API 입니다.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final String ORIGIN = "origin";
    private final AuthService authService;
    private final DesignerService designerService;
    private final ModelService modelService;
    private final SmsUtil smsUtil;

    @Operation(summary = "[KAKAO CODE] 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 로그인 성공입니다."),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 카카오 코드를 입력했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<LoginResponseDto> login(
            @Parameter(hidden = true) @KakaoCode String kakaoCode,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        return SuccessResponse.success(SOCIAL_LOGIN_SUCCESS, authService.login(request.getHeader(ORIGIN), kakaoCode));
    }

    @Operation(summary = "모델 회원가입 시 희망 지역 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "희망 지역 리스트 조회 성공입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/regions")
    public SuccessResponse<List<RegionResponse>> getRegionList() {
        return SuccessResponse.success(SuccessCode.FIND_REGION_LIST_SUCCESS, authService.getRegionList());
    }

    @Operation(summary = "[KAKAO CODE] 디자이너 회원가입 API", description = "디자이너 회원가입 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 회원가입 성공", content = @Content(schema = @Schema(implementation = UserCreateResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping(value = "/signup/designer", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    SuccessResponse<UserCreateResponse> createDesigner(
            @Parameter(hidden = true) @KakaoCode String kakaoCode,
            @RequestPart MultipartFile profileImg,
            @RequestPart DesignerCreateRequest designerInfo,
            @Parameter(hidden = true) HttpServletRequest servletRequest
    ) {
        return SuccessResponse.success(SuccessCode.DESIGNER_CREATE_SUCCESS, designerService.createDesigner(servletRequest.getHeader(ORIGIN), kakaoCode, designerInfo, profileImg));
    }

    @Operation(summary = "[JWT] 모델 회원가입 API", description = "모델 회원가입 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모델 회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "인증오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 입력했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/signup/model")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<UserCreateResponse> createModel(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestBody ModelCreateRequest modelCreateRequest
    ) {
        return SuccessResponse.success(SuccessCode.MODEL_CREATE_SUCCESS, modelService.createModel(userId, modelCreateRequest));
    }

    @Operation(summary = "[SMS 기능 미완성] 인증번호 요청 API", description = "인증번호 요청 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전화번호 인증 요청 성공입니다."),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 카카오 코드를 입력했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 입력했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/phoneNumber")
    public SuccessNonDataResponse sendVerificationCodeMessageToUser(@RequestBody PhoneNumberRequestDto phoneNumberRequestDto) {
        authService.sendVerificationCodeMessageToUser(phoneNumberRequestDto.phoneNumber());
        return SuccessNonDataResponse.success(SEND_VERIFICATION_CODE_SUCCESS);
    }

    @Operation(summary = "전화번호 인증 API", description = "전화번호 인증 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전화번호 인증 성공입니다."),
            @ApiResponse(
                    responseCode = "400",
                    description = "1. 인증번호가 일치하지 않습니다."
                            + "2. 만료된 인증 코드입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "인증 코드가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/phoneNumber/verify")
    public SuccessNonDataResponse verifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto) {
        authService.verifyCode(verifyCodeRequestDto.phoneNumber(), verifyCodeRequestDto.verifyCode());
        return SuccessNonDataResponse.success(VERIFICATION_CODE_MATCH_SUCCESS);
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공입니다."),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping("/logout")
    public SuccessNonDataResponse logout(@UserId Long userId) {
        authService.logout(userId);
        return SuccessNonDataResponse.success(LOGOUT_SUCCESS);
    }
}
