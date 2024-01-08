package com.moddy.server.controller.designer.dto.response;

import com.moddy.server.config.jwt.JwtService;
import com.moddy.server.domain.designer.Designer;

public record DesignerCreateResponse(
        String accessToken,
        String refreshToken
) {
    public static DesignerCreateResponse of() {
        public static DesignerCreateResponse of() {

        }
    }
}
