package com.moddy.server.service.offer;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.DesignerInfoOpenChatDto;
import com.moddy.server.controller.model.dto.response.DesignerInfoOpenChatResponse;
import com.moddy.server.controller.model.dto.response.OpenChatResponse;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.service.application.HairModelApplicationRetrieveService;
import com.moddy.server.service.designer.DesignerRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HairServiceOfferRetrieveService {

    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final DesignerRetrieveService designerRetrieveService;
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;

    public OpenChatResponse getOpenChatInfo(Long userId, Long offerId) {
        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUNT_OFFER_EXCEPTION));

        Long designerId = hairServiceOffer.getDesigner().getId();
        Long applicationId = hairServiceOffer.getHairModelApplication().getId();

        DesignerInfoOpenChatDto openChatDto = designerRetrieveService.getDesignerOpenDetail(designerId);

        DesignerInfoOpenChatResponse response = new DesignerInfoOpenChatResponse(openChatDto.imgUrl(), openChatDto.shopName(), openChatDto.name(), openChatDto.introduction());

        OpenChatResponse openChatResponse = new OpenChatResponse(hairModelApplicationRetrieveService.getApplicationCaptureUrl(applicationId), openChatDto.kakaoUrl(), response);

        return openChatResponse;
    }
}
