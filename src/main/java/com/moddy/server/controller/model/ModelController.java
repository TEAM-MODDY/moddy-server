package com.moddy.server.controller.model;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.auth.dto.response.RegionResponse;
import com.moddy.server.controller.designer.dto.response.UserCreateResponse;
import com.moddy.server.controller.model.dto.request.ModelApplicationRequest;
import com.moddy.server.controller.model.dto.request.ModelCreateRequest;
import com.moddy.server.controller.model.dto.response.ApplicationUserDetailResponse;
import com.moddy.server.controller.model.dto.response.DetailOfferResponse;
import com.moddy.server.controller.model.dto.response.ModelMainResponse;
import com.moddy.server.controller.model.dto.response.OpenChatResponse;
import com.moddy.server.service.model.ModelRegisterService;
import com.moddy.server.service.model.ModelRetrieveService;
import com.moddy.server.service.model.ModelService;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;
    private final ModelRegisterService modelRegisterService;
    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;
    private final ModelRetrieveService modelRetrieveService;

    @Tag(name = "Auth Controller")
    @Operation(summary = "모델 회원가입 시 희망 지역 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "희망 지역 리스트 조회 성공입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/auth/regions")
    public SuccessResponse<List<RegionResponse>> getRegionList() {
        return SuccessResponse.success(SuccessCode.FIND_REGION_LIST_SUCCESS, modelRetrieveService.getRegionList());
    }

    @Tag(name = "Auth Controller", description = "로그인 및 회원 가입 관련 API 입니다.")
    @Operation(summary = "[JWT] 모델 회원가입 API", description = "모델 회원가입 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모델 회원가입 성공"),
            @ApiResponse(responseCode = "401", description = "인증오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 입력했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/auth/signup/model")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<UserCreateResponse> createModel(
            @Parameter(hidden = true) @UserId Long userId,
            @Valid @RequestBody ModelCreateRequest modelCreateRequest) {
        return SuccessResponse.success(SuccessCode.MODEL_CREATE_SUCCESS, modelRegisterService.createModel(userId, modelCreateRequest));
    }

    @Tag(name = "ModelController")
    @Operation(summary = "[JWT] 모델 메인 뷰 조회", description = "모델 메인 뷰 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = ModelMainResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/model")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<ModelMainResponse> getModelMainInfo(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "page", description = "페이지 ") @RequestParam(value = "page") int page,
            @Parameter(name = "size", description = "페이지 ") @RequestParam(value = "size") int size) {
        return SuccessResponse.success(SuccessCode.FIND_MODEL_MAIN_INFO_SUCCESS, modelService.getModelMainInfo(userId, page, size));
    }

    @Tag(name = "ModelController")
    @Operation(summary = "[JWT] 제안서 상세보기 뷰 조회", description = "제안서 상세보기 API입니다.")
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
        return SuccessResponse.success(SuccessCode.FIND_MODEL_DETAIL_OFFER_SUCCESS, modelService.getOfferDetail(userId, offerId));
    }

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
    @Operation(summary = "[JWT] 모델 지원서 작성", description = "모델 지원서 작성 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 지원서 작성 성공"),
            @ApiResponse(responseCode = "400", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @PostMapping(value = "/model/application", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public SuccessNonDataResponse submitModelApplication(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestPart(value = "modelImgUrl", required = false) MultipartFile modelImgUrl,
            @RequestPart(value = "applicationCaptureImgUrl", required = false) MultipartFile applicationCaptureImgUrl,
            @RequestPart(value = "applicationInfo") @Valid ModelApplicationRequest applicationInfo) {
        modelService.postApplication(userId, modelImgUrl, applicationCaptureImgUrl, applicationInfo);
        return SuccessNonDataResponse.success(SuccessCode.CREATE_MODEL_APPLICATION_SUCCESS);
    }

    @Tag(name = "ModelController")
    @Operation(summary = "[JWT] 모델 지원서 최종 확인 시 유저 정보 조회 API", description = "[모델 뷰] 모델 지원서 최종 확인 시 유저 정보 조회 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 지원서 작성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @SecurityRequirement(name = "JWT Auth")
    @GetMapping(value = "/model/application/user")
    public SuccessResponse<ApplicationUserDetailResponse> getUserDetailInApplication(@Parameter(hidden = true) @UserId Long userId) {
        return SuccessResponse.success(SuccessCode.CREATE_MODEL_APPLICATION_SUCCESS, modelService.getUserDetailInApplication(userId));
    }

}
