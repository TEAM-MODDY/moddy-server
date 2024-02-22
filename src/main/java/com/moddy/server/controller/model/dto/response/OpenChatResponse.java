package com.moddy.server.controller.model.dto.response;

import com.moddy.server.controller.model.dto.DesignerInfoOpenChatDto;

public record OpenChatResponse(
        String applicationImgUrl,
        DesignerInfoOpenChatDto designerInfo
) {
}

