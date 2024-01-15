package com.moddy.server.controller.model.dto.request;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public record ModelApplicationRequest(
        @Schema(description = "모델의 현재 머리 기장 예시입니다.", example ="SHORT")
        String hairLength,
        @Schema(description = "PreferHaireStyle의 예시 JSON 배열 포맷입니다.", example ="[\"NORMAL_CUT\", \"ALL_COLOR\"]")
        List<String> preferHairStyles,
        @Schema(description = "모델이 원하는 헤어스타일 예시입니다.", example = "안녕하세요 저는 숱을 많이 친 허쉬컷이 하고 싶어요 근데 머리가 곱슬이라 매직도 같이 해야지 이쁘게 될것 같아요. 그리고 머리가 얇아서 그거 감안하고 해야할것 같습니다.")
        String hairDetail,
        @Schema(description = "HairServiceRecords 의 예시 JSON 배열 포맷입니다.", example = "[{\"hairService\": \"PERM\", \"hairServiceTerm\": \"UNDER_ONE\"}, {\"hairService\": \"BLACK\", \"hairServiceTerm\": \"ABOVE_TWELVE\"}]")
        List<ModelHairServiceRequest> hairServiceRecords,
        MultipartFile modelImgUrl,
        @Schema(description = "모델의 인스타그램 예시입니다.", example ="hizo0")
        String instagramId,
        MultipartFile applicationCaptureImgUrl
) {
    public List<ModelHairServiceRequest> getHairServiceRecords() {
        return hairServiceRecords;
    }
}

