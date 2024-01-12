package com.moddy.server.controller.model;

import com.moddy.server.common.dto.ErrorResponse;
import com.moddy.server.common.dto.SuccessNonDataResponse;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.model.dto.requestEditor.ListPropertyEditor;
import com.moddy.server.controller.model.dto.request.ModelApplicationRequest;
import com.moddy.server.controller.model.dto.request.ModelHairServiceRequest;
import com.moddy.server.controller.model.dto.response.DetailOfferResponse;
import com.moddy.server.controller.model.dto.response.ModelMainResponse;
import com.moddy.server.controller.model.dto.response.OpenChatResponse;
import com.moddy.server.service.model.ModelService;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "ModelController")
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @Operation(summary = "[JWT] 모델 메인 뷰 조회", description = "모델 메인 뷰 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = ModelMainResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<ModelMainResponse> getModelMainInfo(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "page", description = "페이지 ") @RequestParam(value = "page") int page,
            @Parameter(name = "size", description = "페이지 ") @RequestParam(value = "size") int size){
        return SuccessResponse.success(SuccessCode.FIND_MODEL_MAIN_INFO_SUCCESS, modelService.getModelMainInfo(userId, page, size));
    }

    @Operation(summary = "[JWT] 제안서 상세보기 뷰 조회", description = "제안서 상세보기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제안서 상세보기 조회 성공", content = @Content(schema = @Schema(implementation = DetailOfferResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/offer/{offerId}")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<DetailOfferResponse> getModelDetailOfferInfo(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId){
        return SuccessResponse.success(SuccessCode.FIND_MODEL_DETAIL_OFFER_SUCCESS, modelService.getModelDetailOfferInfo(userId, offerId));
    }

    @Operation(summary = "[JWT] 카카오톡 오픈채팅", description = "지원서 캡처 이미지 및 디자이너 정보 조회입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = OpenChatResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{offerId}/agree")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessResponse<OpenChatResponse> getOpenChat(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId) {
        return SuccessResponse.success(SuccessCode.OPEN_CHAT_GET_SUCCESS, modelService.getOpenChatInfo(userId, offerId));
    }

    @Operation(summary = "[JWT] 디자이너 제안서 승낙하기", description = "디자이너 제안서 승낙하기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자이너 제안서 승낙하기"),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "제안서 아이디가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping("/offer/{offerId}")
    @SecurityRequirement(name = "JWT Auth")
    public SuccessNonDataResponse acceptOffer(
            @Parameter(hidden = true) @UserId Long userId,
            @Parameter(name = "offerId", description = "제안서아이디") @PathVariable(value = "offerId") Long offerId){
        modelService.updateOfferAgreeStatus(offerId);
        return SuccessNonDataResponse.success(SuccessCode.OFFER_ACCEPT_SUCCESS);

    }

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
            @ModelAttribute ModelApplicationRequest request
    ) {
        modelService.createModelApplication(userId, request);
        return SuccessNonDataResponse.success(SuccessCode.CREATE_MODEL_APPLICATION_SUCCESS);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(List.class, "hairServiceRecords", new ListPropertyEditor(ModelHairServiceRequest.class));
    }

}
