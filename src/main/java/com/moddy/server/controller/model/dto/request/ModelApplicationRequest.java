package com.moddy.server.controller.model.dto.request;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public record ModelApplicationRequest(
        String hairLength,
        List<String> preferHairStyles,
        String hairDetail,
        List<ModelHairServiceRequest> hairServiceRecords,
        MultipartFile modelImgUrl,
        String instagramId,
        MultipartFile applicationCaptureImgUrl
) {

    @Schema(description = "Hair service records in JSON array format", example = "[{\"hairService\": \"PERM\", \"hairServiceTerm\": \"UNDER_ONE\"}, {\"hairService\": \"BLACK\", \"hairServiceTerm\": \"ABOVE_TWELVE\"}]")
    public List<ModelHairServiceRequest> getHairServiceRecords() {
        return hairServiceRecords;
    }
}

