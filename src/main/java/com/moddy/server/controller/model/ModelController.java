package com.moddy.server.controller.model;

import com.moddy.server.common.dto.PageInfo;
import com.moddy.server.common.dto.SuccessResponse;
import com.moddy.server.common.exception.enums.SuccessCode;
import com.moddy.server.config.resolver.user.UserId;
import com.moddy.server.controller.model.dto.response.ModelMainResponse;
import com.moddy.server.service.model.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "ModelController")
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @Operation(summary = "모델 메인 뷰 조회", description = "모델 메인 뷰 조회 API입니다.")
    @Parameters({
            @Parameter(name = "page", description = "페이지 넘버"),
            @Parameter(name = "size", description = "페이징 해서 가져올 정보들의 크기"),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모델 메인뷰 조회 성공", content = @Content(schema = @Schema(implementation = ModelMainResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "서버 내부 오류 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping
    public SuccessResponse<ModelMainResponse> getMain(@UserId Long userId, @RequestParam int page, @RequestParam int size){
        return SuccessResponse.success(SuccessCode.FIND_MODEL_MAIN_INFO_SUCCESS, modelService.getModelMainInfo(userId, page, size));
    }

}
