package com.moddy.server.controller.designer.dto.request;

import com.moddy.server.domain.day_off.DayOfWeek;
import com.moddy.server.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema
public record DesignerCreateRequest(
        MultipartFile profileImg,
        String name,
        Gender gender,
        String phoneNumber,
        Boolean isMarketingAgree,
        String hairShopName,
        String hairShopAddress,
        String hairShopAddressDetail,
        String instagramUrl,
        String naverPlaceUrl,
        String introduction,
        String kakaoOpenChatUrl,
        List<DayOfWeek> dayOffs

) {
}
