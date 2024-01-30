package com.moddy.server.service.model;

import com.moddy.server.controller.model.dto.response.OpenChatResponse;
import com.moddy.server.service.application.HairModelApplicationRetrieveService;
import com.moddy.server.service.designer.DesignerRetrieveService;
import com.moddy.server.service.offer.HairServiceOfferRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelRetrieveService {

    private final DesignerRetrieveService designerRetrieveService;
    private final HairServiceOfferRetrieveService hairServiceOfferRetrieveService;
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;

    public OpenChatResponse getOpenChatInfo(Long userId, Long offerId) {
        Long designerId = hairServiceOfferRetrieveService.getDesignerApplicationId(offerId).designerId();
        Long applicationId = hairServiceOfferRetrieveService.getDesignerApplicationId(offerId).applicationId();

        OpenChatResponse openChatResponse = new OpenChatResponse(hairModelApplicationRetrieveService.getApplicationCaptureUrl(applicationId), designerRetrieveService.getDesignerOpenDetail(designerId).kakaoUrl(), designerRetrieveService.getDesignerOpenChatDetail(designerId));

        return openChatResponse;
    }
}
