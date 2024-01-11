package com.moddy.server.controller.model.dto.response;

public record OpenChatResponse(
        String applicationImgUrl,
        String kakaoUrl,
        DesignerInfoOpenChatResponse designerInfo
) {
}
