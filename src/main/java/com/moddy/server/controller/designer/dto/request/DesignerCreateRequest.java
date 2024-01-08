package com.moddy.server.controller.designer.dto.request;

import com.moddy.server.domain.designer.HairShop;
import com.moddy.server.domain.designer.Portfolio;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record DesignerCreateRequest (
        MultipartFile profileImg,
        String name,
        String gender,
        String phoneNumber,
        Boolean isMarketingAgree,
        HairShop hairShop,
        Portfolio portfolio,
        String introduction,
        String kakaoOpenChatUrl,
        List<String> dayOffs

)
{



}
